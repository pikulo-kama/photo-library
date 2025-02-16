package com.adrabazha.photo_library.rest.v1;

import com.adrabazha.photo_library.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Objects;


@RestController
@RequestMapping("/rest/v1/auth")
public class AuthController {

    @GetMapping("/user-info")
    public ResponseEntity<ApiResponse> getUser(@AuthenticationPrincipal OAuth2User user) {

        if (Objects.isNull(user)) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Authentication principal is missing."));
        }

        final Map<String, Object> userAttributes = Map.of(
                "name", Objects.requireNonNull(user.getAttribute("name")),
                "picture", Objects.requireNonNull(user.getAttribute("picture"))
        );

        return ResponseEntity.ok(ApiResponse.success(userAttributes));
    }
}
