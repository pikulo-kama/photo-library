package com.adrabazha.photo_library.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse {

    private Boolean success;
    private String message;
    private Object response;

    public static ApiResponse success() {
        return success(null);
    }

    public static ApiResponse success(Object response) {
        return new ApiResponse(true, null, response);
    }

    public static ApiResponse error(String message) {
        return new ApiResponse(false, message, null);
    }
}
