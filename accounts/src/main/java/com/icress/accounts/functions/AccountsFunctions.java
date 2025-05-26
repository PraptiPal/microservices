package com.icress.accounts.functions;

import com.icress.accounts.dto.MobileNumberUpdateDto;
import com.icress.accounts.service.IAccountsService;
import com.icress.accounts.service.ICustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class AccountsFunctions {

    private static final Logger log = LoggerFactory.getLogger(AccountsFunctions.class);

    @Bean
    public Consumer<String> ackFromMsgMs(){
        return accountNumber -> {
            log.info("ACK From Message Service for the account number : " + accountNumber.toString());
        };
    }

    @Bean
    public Consumer<MobileNumberUpdateDto> updateAccountMobileNumber(IAccountsService iAccountsService) {
        return (mobileNumberUpdateDto) -> {
            log.info("Received  updateAccountMobileNumber request  for the details: {}", mobileNumberUpdateDto);
            iAccountsService.updateMobileNumber(mobileNumberUpdateDto);
        };
    }

    @Bean
    public Consumer<MobileNumberUpdateDto> rollbackAccountMobileNumber(IAccountsService iAccountsService) {
        return (mobileNumberUpdateDto) -> {
            log.info("Received  rollbackAccountMobileNumber request  for the details: {}", mobileNumberUpdateDto);
            iAccountsService.rollbackMobileNumber(mobileNumberUpdateDto);
        };
    }

    @Bean
    public Consumer<MobileNumberUpdateDto> rollbackCustomerMobileNumber(ICustomerService iCustomerService) {
        return (mobileNumberUpdateDto) -> {
            log.info("Received  rollbackCustomerMobileNumber request  for the details: {}", mobileNumberUpdateDto);
            iCustomerService.rollbackMobileNumber(mobileNumberUpdateDto);
        };
    }

}
