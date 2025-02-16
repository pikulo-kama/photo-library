package com.adrabazha.photo_library.rest.v1;

import com.adrabazha.photo_library.dto.ApiResponse;
import com.adrabazha.photo_library.dto.DirectoryDto;
import com.adrabazha.photo_library.dto.ImageDto;
import com.adrabazha.photo_library.service.GoogleStorageService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.http.ResponseEntity.ok;


@RestController
@RequestMapping("/rest/v1/attachment")
public class AttachmentController {

    private final GoogleStorageService storageService;

    public AttachmentController(GoogleStorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse> listAttachments(@AuthenticationPrincipal OAuth2User user) {
        return listAttachments(null, user);
    }

    @GetMapping("/{directory}")
    public ResponseEntity<ApiResponse> listAttachments(@PathVariable("directory") String directoryUUID,
                                                       @AuthenticationPrincipal OAuth2User user) {

        final DirectoryDto directory = DirectoryDto.builder()
                .directoryUUID(directoryUUID)
                .owner(user.getAttribute("email"))
                .build();

        return ok(ApiResponse.success(storageService.getImages(directory)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> uploadAttachment(@RequestParam(value = "directory", required = false) String directory,
                                                        @RequestParam("file") MultipartFile file,
                                                        @AuthenticationPrincipal OAuth2User user) {

        final DirectoryDto directoryDto = DirectoryDto.builder()
                .directoryUUID(directory)
                .owner(user.getAttribute("email"))
                .build();

        storageService.createImage(file, directoryDto);
        return ok(ApiResponse.success());
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse> deleteAttachment(@RequestBody ImageDto imageDto,
                                                        @AuthenticationPrincipal OAuth2User user) {

        storageService.deleteImage(imageDto.getImageName(), user.getAttribute("email"));
        return ok(ApiResponse.success());
    }
}
