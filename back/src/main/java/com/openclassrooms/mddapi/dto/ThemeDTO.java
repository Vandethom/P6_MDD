package com.openclassrooms.mddapi.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ThemeDTO {
    private Long          id;
    private String        title;
    private String        description;
    private LocalDateTime createdAt;
}
