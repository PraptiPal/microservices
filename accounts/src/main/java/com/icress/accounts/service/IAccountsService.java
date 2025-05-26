package com.icress.accounts.service;

import com.icress.accounts.dto.CustomerDto;
import com.icress.accounts.dto.MobileNumberUpdateDto;

public interface IAccountsService {

    void createAccount(CustomerDto customerDto);

    CustomerDto getAccountByMobileNumber(String mobileNumber);

    boolean updateAccount(CustomerDto customerDto);

    boolean deleteAccount(String mobileNumber);

    boolean updateMobileNumber(MobileNumberUpdateDto mobileNumberUpdateDto);

    boolean rollbackMobileNumber(MobileNumberUpdateDto mobileNumberUpdateDto);
}
