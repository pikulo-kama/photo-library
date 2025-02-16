package com.adrabazha.photo_library.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DirectoryListDto {

    private List<DirectoryDto> directoryPath;

    private List<DirectoryDto> directories;
}
