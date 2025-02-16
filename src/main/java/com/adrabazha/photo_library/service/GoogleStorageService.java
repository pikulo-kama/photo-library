package com.adrabazha.photo_library.service;

import com.adrabazha.photo_library.dto.DirectoryDto;
import com.adrabazha.photo_library.dto.ImageDto;
import com.adrabazha.photo_library.exception.ApiException;
import com.adrabazha.photo_library.model.Directory;
import com.google.api.gax.paging.Page;
import com.google.cloud.storage.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.StreamSupport;

import static com.adrabazha.photo_library.util.CloudUtil.*;


@Service
@Slf4j
public class GoogleStorageService {

    private static final String PLACEHOLDER_FILE = "placeholder";

    private final Storage googleStorage;
    private final DirectoryService directoryService;

    @Value("${cloud.bucket-name}")
    private String bucketName;

    public GoogleStorageService(DirectoryService directoryService) {
        this.directoryService = directoryService;
        this.googleStorage = StorageOptions.getDefaultInstance().getService();
    }

    /**
     * Used to get list of images
     * that are located in provided directory.
     *
     * @param directoryDto directory for which contents should be retrieved
     * @return DTO with collection of images associated with provided directory
     */
    public List<ImageDto> getImages(DirectoryDto directoryDto) {

        final List<ImageDto> images = new ArrayList<>();

        // Need to add image prefix, so it will only list images in requested directory
        // without this prefix images from all child directories would be retrieved.
        String directoryPath = buildCloudPath(getDirectoryPath(directoryDto), IMAGE_PREFIX);

        Storage.BlobListOption blobFilter = Storage.BlobListOption.prefix(directoryPath);
        Page<Blob> directoryPage = googleStorage.list(bucketName, blobFilter);

        for (Blob image : directoryPage.iterateAll()) {

            if (image.getName().endsWith(PLACEHOLDER_FILE)) {
                continue;
            }

            BlobInfo imageInfo = BlobInfo.newBuilder(bucketName, image.getName()).build();

            // Since all images in bucket are private, we need to sign them so that they would be accessible
            // for some time.
            URL signedUrl = googleStorage.signUrl(imageInfo, 1, TimeUnit.HOURS, Storage.SignUrlOption.withV4Signature());

            ImageDto imageDto = ImageDto.builder()
                    .imageName(imageInfo.getName())
                    .imageURL(signedUrl.toString())
                    .build();

            images.add(imageDto);
        }

        return images;
    }

    /**
     * Used to upload image to the GCS bucket.
     * Will upload it to provided directory.
     *
     * @param image image object that should be added to bucket
     * @param directoryDto directory to which file should be uploaded
     */
    public void createImage(MultipartFile image, DirectoryDto directoryDto) {

        if (image.isEmpty()) {
            throw new ApiException("File is empty.");
        }

        if (!isImage(image)) {
            throw new ApiException("File is not an image.");
        }

        String directoryPath = getDirectoryPath(directoryDto);
        String imagePath = buildCloudPath(directoryPath, IMAGE_PREFIX + UUID.randomUUID());

        BlobInfo imageInfo = BlobInfo.newBuilder(bucketName, imagePath)
                .setContentType(image.getContentType())
                .build();

        try {
            googleStorage.create(imageInfo, image.getBytes());

        } catch (IOException exception) {
            log.error("There was an error uploading image to bucket", exception);
        }
    }

    /**
     * Used to delete image from GCS bucket.
     *
     * @param imagePath name of the image that should be removed
     */
    public void deleteImage(String imagePath, String owner) {

        // Since imagePath is already an actual path to the image in the bucket,
        // and we can just use it directly to remove image from bucket.
        // But we need to have at least some validation when deleting an image,
        // that's why we need to check if user has access to the directory.
        String parentDirectory = getParentDirectoryFromPath(imagePath);

        DirectoryDto directoryDto = DirectoryDto.builder()
                .directoryUUID(parentDirectory)
                .owner(owner)
                .build();

        // Just to validate that user actually has access to the directory image in.
        getDirectoryPath(directoryDto);

        BlobId existingImageId = BlobId.of(bucketName, imagePath);
        googleStorage.delete(existingImageId);
    }

    /**
     * Used to create directory in GCS bucket.
     *
     * @param directory directory that should be created
     */
    public void createDirectory(Directory directory) {

        // Since there are no such thing as directory in GCS, and actually it's just the way
        // GCP visualizes files, we need to create file in order to create a directory.
        String directoryPath = buildCloudPath(getDirectoryPath(directory), PLACEHOLDER_FILE);
        BlobInfo directoryInfo = BlobInfo.newBuilder(bucketName, directoryPath).build();

        googleStorage.create(directoryInfo);
    }

    /**
     * Used to remove directory from GCS including all children files
     * it contain.
     *
     * @param directoryDto directory that should be removed
     */
    public void deleteDirectory(DirectoryDto directoryDto) {

        String directoryPath = getDirectoryPath(directoryDto);
        Storage.BlobListOption prefix = Storage.BlobListOption.prefix(directoryPath);

        Page<Blob> matchingFiles = googleStorage.list(bucketName, prefix);
        List<BlobId> matchingFileIDs = StreamSupport.stream(matchingFiles.getValues().spliterator(), false)
                .map(Blob::getBlobId)
                .toList();

        if (!matchingFileIDs.isEmpty()) {
            googleStorage.delete(matchingFileIDs);
        }
    }

    /**
     * Used to build GCS compliant string that would represent
     * directory structure in GCP.
     * <p>
     * Will resolve path recursively in case if directory has parent directories.
     *
     * @param directoryDto directory for which path should be built
     * @return string representation of directory path
     */
    private String getDirectoryPath(DirectoryDto directoryDto) {
        final Directory directory = directoryService.getDirectory(directoryDto);
        return getDirectoryPath(directory);
    }

    private String getDirectoryPath(Directory directory) {

        final List<String> pathArray = new ArrayList<>();
        Directory parentDirectory = directory;

        // If there are parent directories add them to the path.
        while (Objects.nonNull(parentDirectory.getParentDirectory())) {
            parentDirectory = parentDirectory.getParentDirectory();
            pathArray.add(parentDirectory.getDirectoryUUID());
        }

        // Reverse array so it will start from root directory to last child
        // and then add UUID of requested directory.
        Collections.reverse(pathArray);
        pathArray.add(directory.getDirectoryUUID());

        return buildCloudPath(pathArray.toArray(new String[] {}));
    }

    /**
     * Used to check whether file is of image type.
     *
     * @param file file that needs to be verified
     * @return true if image otherwise false
     */
    private Boolean isImage(MultipartFile file) {

        final String contentType = file.getContentType();
        return Objects.nonNull(contentType) && contentType.startsWith("image/");
    }
}
