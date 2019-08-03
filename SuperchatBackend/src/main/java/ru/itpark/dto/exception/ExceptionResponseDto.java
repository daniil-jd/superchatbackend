package ru.itpark.dto.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionResponseDto {
    private String code;
    private String message;
    private List<ErrorDto> errors = new LinkedList<>();
}
