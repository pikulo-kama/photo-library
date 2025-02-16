package com.adrabazha.photo_library.service;

import com.adrabazha.photo_library.dto.DirectoryDto;
import com.adrabazha.photo_library.dto.DirectoryListDto;
import com.adrabazha.photo_library.exception.ApiException;
import com.adrabazha.photo_library.model.Directory;
import com.adrabazha.photo_library.repository.DirectoryRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static com.adrabazha.photo_library.util.CloudUtil.DIRECTORY_PREFIX;


@Service
public class DirectoryService {

    private static final String ROOT_DIRECTORY_NAME = "Library";

    private final DirectoryRepository directoryRepository;

    public DirectoryService(DirectoryRepository directoryRepository) {
        this.directoryRepository = directoryRepository;
    }

    /**
     * Used to retrieve information about provided directory, including
     * list of all direct child directories, as well as path to the requested directory.
     * Where first element is the root directory and last element is requested directory.
     *
     * @param directoryDto directory for which details should be retrieved
     * @return DTO with directory details
     */
    public DirectoryListDto getDirectoryDetails(DirectoryDto directoryDto) {

        List<DirectoryDto> childDirectories = findDirectories(directoryDto).stream()
                .map(this::mapToDirectory)
                .toList();

        List<DirectoryDto> directoryPath = getDirectoryPath(directoryDto).stream()
                .map(this::mapToDirectory)
                .toList();

        return DirectoryListDto.builder()
                .directories(childDirectories)
                .directoryPath(directoryPath)
                .build();
    }

    /**
     * Used to list all directories that are direct children of provided one.
     *
     * @param directoryDto directory for which children should be retrieved
     * @return list of child directories of provided directory
     */
    public List<Directory> findDirectories(DirectoryDto directoryDto) {
        Directory directory = getDirectory(directoryDto);
        return directoryRepository.findByParentDirectoryIdAndOwner(directory.getDirectoryId(), directoryDto.getOwner());
    }

    /**
     * Used to build list of directories based on their relation, if directory B has parent directory A
     * which has parent directory C, then the following list would be returned - [C, A, B].
     *
     * @param directoryDto directory for which path should be built
     * @return path to the directory starting from root and ending with requested directory
     */
    public List<Directory> getDirectoryPath(DirectoryDto directoryDto) {

        Directory directory = getDirectory(directoryDto);

        final List<Directory> path = new ArrayList<>();
        path.add(directory);

        while (Objects.nonNull(directory.getParentDirectory())) {

            directory = directory.getParentDirectory();
            path.add(directory);
        }

        Collections.reverse(path);
        return path;
    }

    /**
     * Used to get directory information.
     *
     * @param directoryDto DTO with directory info
     * @return directory information from DB
     */
    public Directory getDirectory(DirectoryDto directoryDto) {

        final String directoryUUID = directoryDto.getDirectoryUUID();
        final String owner = directoryDto.getOwner();

        // If directory UUID is not provided then just use user's root
        // directory then.
        if (Objects.isNull(directoryUUID)) {
            return getOrCreateRootDirectory(owner);
        }

        return directoryRepository.findByDirectoryUUIDAndOwner(directoryUUID, owner)
                .orElseThrow(() -> new ApiException("Directory doesn't exist."));
    }

    /**
     * Used to create directory using provided information.
     *
     * @param directoryDto directory information
     * @return create directory
     */
    public Directory createDirectory(DirectoryDto directoryDto) {

        final String directoryUUID = DIRECTORY_PREFIX + UUID.randomUUID();
        final String parentDirectoryUUID = directoryDto.getParentDirectoryUUID();
        final String directoryName = directoryDto.getDirectoryName();
        final String owner = directoryDto.getOwner();

        // If directory doesn't have parent directory specified, we are assuming
        // that it's located in root directory, so we need to assign it.
        if (Objects.isNull(parentDirectoryUUID)) {
            Directory rootDirectory = getOrCreateRootDirectory(owner);
            directoryDto.setParentDirectoryUUID(rootDirectory.getDirectoryUUID());
        }

        Directory parentDirectory = directoryRepository.findByDirectoryUUIDAndOwner(directoryDto.getParentDirectoryUUID(), owner)
                .orElseThrow(() -> new ApiException("Parent directory doesn't exist."));

        // Check if you already have directory with this name at the same location.
        boolean directoryAlreadyExists = directoryRepository.existsByParentDirectoryIdAndDirectoryNameAndOwner(
                parentDirectory.getParentDirectoryId(), directoryName, owner
        );

        if (directoryAlreadyExists) {
            throw new ApiException("Directory with such name already exists.");
        }

        Directory newDirectory = Directory.builder()
                .directoryUUID(directoryUUID)
                .parentDirectory(parentDirectory)
                .directoryName(directoryName)
                .owner(owner)
                .build();

        return directoryRepository.save(newDirectory);
    }

    /**
     * Used to remove directory and all children directory
     * metadata.
     *
     * @param directoryDto directory that should be deleted
     */
    public void deleteDirectory(DirectoryDto directoryDto) {
        Directory directory = getDirectory(directoryDto);
        directoryRepository.delete(directory);
    }

    /**
     * Used to get root directory for provided user.
     * If directory doesn't exist it would be created first.
     *
     * @param owner user whose root directory should be provided
     * @return root directory
     */
    public Directory getOrCreateRootDirectory(String owner) {

        Optional<Directory> existingRootDirectory = directoryRepository.findByOwnerAndParentDirectoryIdIsNull(owner);

        if (existingRootDirectory.isPresent()) {
            return existingRootDirectory.get();
        }

        Directory newRootDirectory = Directory.builder()
                .directoryUUID(DIRECTORY_PREFIX + UUID.randomUUID())
                .directoryName(ROOT_DIRECTORY_NAME)
                .owner(owner)
                .build();

        return directoryRepository.save(newRootDirectory);
    }

    /**
     * Used to create directory DTO from directory.
     *
     * @param directory directory for which DTO should be created
     * @return directory DTO
     */
    private DirectoryDto mapToDirectory(Directory directory) {

        return DirectoryDto.builder()
                .directoryUUID(directory.getDirectoryUUID())
                .directoryName(directory.getDirectoryName())
                .build();
    }
}
