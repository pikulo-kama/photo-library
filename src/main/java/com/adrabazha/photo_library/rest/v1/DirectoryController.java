package com.adrabazha.photo_library.rest.v1;

import com.adrabazha.photo_library.dto.ApiResponse;
import com.adrabazha.photo_library.dto.DirectoryDto;
import com.adrabazha.photo_library.model.Directory;
import com.adrabazha.photo_library.service.DirectoryService;
import com.adrabazha.photo_library.service.GoogleStorageService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;


@RestController
@RequestMapping("/rest/v1/directory")
public class DirectoryController {

    private final DirectoryService directoryService;
    private final GoogleStorageService storageService;

    public DirectoryController(DirectoryService directoryService, GoogleStorageService storageService) {
        this.directoryService = directoryService;
        this.storageService = storageService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse> listDirectories(@AuthenticationPrincipal OAuth2User user) {
        return listDirectories(null, user);
    }

    @GetMapping("/{directory}")
    public ResponseEntity<ApiResponse> listDirectories(@PathVariable("directory") String directoryUUID,
                                                       @AuthenticationPrincipal OAuth2User user) {

        DirectoryDto directoryDto = DirectoryDto.builder()
                .owner(user.getAttribute("email"))
                .directoryUUID(directoryUUID)
                .build();

        return ok(ApiResponse.success(directoryService.getDirectoryDetails(directoryDto)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createDirectory(@RequestBody DirectoryDto directoryDto, @AuthenticationPrincipal OAuth2User user) {

        directoryDto.setOwner(user.getAttribute("email"));

        Directory directory = directoryService.createDirectory(directoryDto);
        storageService.createDirectory(directory);

        return ok(ApiResponse.success(directory));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse> deleteDirectory(@RequestBody DirectoryDto directoryDto, @AuthenticationPrincipal OAuth2User user) {

        directoryDto.setOwner(user.getAttribute("email"));

        storageService.deleteDirectory(directoryDto);
        directoryService.deleteDirectory(directoryDto);

        return ok(ApiResponse.success());
    }
}
