package com.icress.accounts.service;

import com.icress.accounts.dto.CustomerDetailsDto;
import com.icress.accounts.dto.MobileNumberUpdateDto;

public interface ICustomerService {
    CustomerDetailsDto getCustomerDetails(String mobileNumber);

    boolean updateMobileNumber(MobileNumberUpdateDto mobileNumberUpdateDto);

    boolean rollbackMobileNumber(MobileNumberUpdateDto mobileNumberUpdateDto);
}
