package com.demo.model.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class ErrorResponse {

    @Builder.Default
    private Long timestamp = Instant.now().toEpochMilli();
    private int code;
    private String message;
    private List<ValidationInfo> validations;

}
