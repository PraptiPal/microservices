package com.icress.accounts.service.impl;

import com.icress.accounts.client.CardsFeignClient;
import com.icress.accounts.client.LoansFeignClient;
import com.icress.accounts.dto.*;
import com.icress.accounts.entity.Accounts;
import com.icress.accounts.entity.Customer;
import com.icress.accounts.exceptions.ResourceNotFoundException;
import com.icress.accounts.mapper.AccountsMapper;
import com.icress.accounts.mapper.CustomerMapper;
import com.icress.accounts.repository.AccountsRepository;
import com.icress.accounts.repository.CustomerRepository;
import com.icress.accounts.service.ICustomerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Slf4j
public class CustomerServiceImpl implements ICustomerService {

    private AccountsRepository accountsRepository;
    private CustomerRepository customerRepository;
    private CardsFeignClient cardsFeignClient;
    private LoansFeignClient loansFeignClient;
    private final StreamBridge streamBridge;
    @Override
    public CustomerDetailsDto getCustomerDetails(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );

        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString())
        );

        CustomerDetailsDto customerDetailsDto = CustomerMapper.mapToCustomerDetailsDto(customer, new CustomerDetailsDto());

        customerDetailsDto.setAccountsDto(AccountsMapper.mapToAccountsDto(accounts, new AccountsDto()));

        ResponseEntity<LoansDto> loansDtoResponseEntity = loansFeignClient.fetchLoanDetails(mobileNumber);

        if(null != loansDtoResponseEntity){
            customerDetailsDto.setLoansDto(loansDtoResponseEntity.getBody());
        }

        ResponseEntity<CardsDto> cardsDtoResponseEntity = cardsFeignClient.fetchCardDetails(mobileNumber);

        if(null != cardsDtoResponseEntity){
            customerDetailsDto.setCardsDto(cardsDtoResponseEntity.getBody());
        }

        return customerDetailsDto;
    }

    @Override
    @Transactional
    public boolean updateMobileNumber(MobileNumberUpdateDto mobileNumberUpdateDto) {
        String currentMobileNum = mobileNumberUpdateDto.getCurrentMobileNumber();
        Customer customer = customerRepository.findByMobileNumber(currentMobileNum).orElseThrow(() -> new ResourceNotFoundException("Customer", "mobileNumber", currentMobileNum));
        customer.setMobileNumber(mobileNumberUpdateDto.getNewMobileNumber());
        customerRepository.save(customer);

        updateAccountMobileNumber(mobileNumberUpdateDto);
        return true;
    }


    private void updateAccountMobileNumber(MobileNumberUpdateDto mobileNumberUpdateDto) {
        log.info("Sending updateAccountMobileNumber request for the details: {}", mobileNumberUpdateDto);
        var result = streamBridge.send("updateAccountMobileNumber-out-0",mobileNumberUpdateDto);
        log.info("Is the updateAccountMobileNumber request successfully triggered ? : {}", result);
    }

    @Override
    public boolean rollbackMobileNumber(MobileNumberUpdateDto mobileNumberUpdateDto) {

        String newMobileNumber = mobileNumberUpdateDto.getNewMobileNumber();
        Customer customer = customerRepository.findByMobileNumber(newMobileNumber).orElseThrow(() -> new ResourceNotFoundException("Customer", "mobileNumber", newMobileNumber)
        );
        customer.setMobileNumber(mobileNumberUpdateDto.getCurrentMobileNumber());
        customerRepository.save(customer);

        //we should call rollback from customer service also
        return true;
    }
}
