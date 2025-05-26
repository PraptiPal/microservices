package com.icress.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Schema(
        name = "Customer",
        description = "Schema to hold Customer and Account information"
)
@Data
public class CustomerDto {

    @Schema(
            description = "Name of the customer", example = "MD Ali Sharukh Nawaz"
    )
    @NotEmpty(message = "Name can not be a null or empty")
    @Size(min = 6, max = 50, message = "The length of the customer name should be between 6 and 50")
    private String name;

    @Schema(
            description = "Email address of the customer", example = "md.ali@icress.in"
    )
    @NotEmpty(message = "Email can not be a null or empty")
    @Email(message = "Email address should be valid value")
    private String email;

    @Schema(
            description = "Mobile Number of the customer", example = "1234567890"
    )
    @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
    private String mobileNumber;

    @Schema(
            description = "Account details of the Customer"
    )
    private AccountsDto accountsDto;
}
