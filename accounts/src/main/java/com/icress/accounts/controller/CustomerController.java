package com.icress.accounts.controller;

import com.icress.accounts.constants.AccountsConstants;
import com.icress.accounts.dto.CustomerDetailsDto;
import com.icress.accounts.dto.MobileNumberUpdateDto;
import com.icress.accounts.dto.ResponseDto;
import com.icress.accounts.service.ICustomerService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.apache.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="/api/accounts", produces = {MediaType.APPLICATION_JSON_VALUE})
@Validated
public class CustomerController {

    private final ICustomerService iCustomerService;
    public CustomerController(ICustomerService iCustomerService){
        this.iCustomerService = iCustomerService;
    }

    @GetMapping("/customer/{mobileNumber}")
    public ResponseEntity<CustomerDetailsDto> getCustomerDetails(@PathVariable
                                                                 @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
                                                                 String mobileNumber){

        CustomerDetailsDto customerDetailsDto = iCustomerService.getCustomerDetails(mobileNumber);
        return ResponseEntity.status(HttpStatus.SC_OK).body(customerDetailsDto);
    }

    // /api/accounts/customer/
    @PutMapping("/customer/{mobileNumber}")
    public ResponseEntity<ResponseDto> updateMobileNumber(@Valid @RequestBody MobileNumberUpdateDto mobileNumberUpdateDto,
                                                            @PathVariable
                                                            @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digits")
                                                                    String mobileNumber
    ) {
        iCustomerService.updateMobileNumber(mobileNumberUpdateDto);
        return ResponseEntity.status(org.springframework.http.HttpStatus.OK).
                body(new ResponseDto(AccountsConstants.STATUS_200, AccountsConstants.MESSAGE_200));
    }
}
