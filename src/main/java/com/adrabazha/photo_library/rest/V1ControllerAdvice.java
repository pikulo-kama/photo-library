package com.adrabazha.photo_library.rest;

import com.adrabazha.photo_library.dto.ApiResponse;
import com.adrabazha.photo_library.exception.ApiException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class V1ControllerAdvice {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse> handleApiException(ApiException apiException) {
        return ResponseEntity.badRequest().body(ApiResponse.error(apiException.getMessage()));
    }
}
