package com.icress.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Schema(
        name = "ErrorResponse",
        description = "Schema to hold error response information"
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseDto {

    @Schema(
            description = "API path invoked by client", example = "uri=/api/accounts/234555"
    )
    private String apiPath;

    @Schema(
            description = "Error code representing the error happened", example = "BAD_REQUEST"
    )
    private HttpStatus errorCode;

    @Schema(
            description = "Error message representing the error happened", example = "Method 'PUT' is not supported."
    )
    private String errorMessage;

    @Schema(
            description = "Time representing when the error happened", example = "2005-01-26T17:34:18.3380024"
    )
    private LocalDateTime errorTime;

}
