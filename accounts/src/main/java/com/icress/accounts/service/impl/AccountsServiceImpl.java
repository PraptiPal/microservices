package com.icress.accounts.service.impl;

import com.icress.accounts.constants.AccountsConstants;
import com.icress.accounts.dto.AccountsDto;
import com.icress.accounts.dto.AccountsMsgDto;
import com.icress.accounts.dto.CustomerDto;
import com.icress.accounts.dto.MobileNumberUpdateDto;
import com.icress.accounts.entity.Accounts;
import com.icress.accounts.entity.Customer;
import com.icress.accounts.exceptions.CustomerAlreadyExistsException;
import com.icress.accounts.exceptions.ResourceNotFoundException;
import com.icress.accounts.mapper.AccountsMapper;
import com.icress.accounts.mapper.CustomerMapper;
import com.icress.accounts.repository.AccountsRepository;
import com.icress.accounts.repository.CustomerRepository;
import com.icress.accounts.service.IAccountsService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class AccountsServiceImpl implements IAccountsService {

    private static final Logger log = LoggerFactory.getLogger(AccountsServiceImpl.class);
    private AccountsRepository accountsRepository;
    private CustomerRepository customerRepository;

    private final StreamBridge streamBridge;

    @Override
    public void createAccount(CustomerDto customerDto){

        Customer customer = CustomerMapper.mapToCustomer(customerDto, new Customer());

        Optional<Customer> optionalCustomer = customerRepository.findByMobileNumber(customerDto.getMobileNumber());

        if(optionalCustomer.isPresent()) {
            throw new CustomerAlreadyExistsException("Customer already registered with given mobileNumber "
                    + customerDto.getMobileNumber());
        }

        //customer.setCreatedAt(LocalDateTime.now());
        //customer.setCreatedBy("iCress");

        Customer savedCustomer = customerRepository.save(customer);

        Accounts savedAccount = accountsRepository.save(createNewAccount(savedCustomer));
        sendCommunication(savedAccount, savedCustomer);
    }

    private void sendCommunication(Accounts account, Customer customer) {
        var accountsMsgDto = new AccountsMsgDto(account.getAccountNumber(), customer.getName(),
                customer.getEmail(), customer.getMobileNumber());
        log.info("Sending Communication request for the details: {}", accountsMsgDto);
        var result = streamBridge.send("sendCommunication-out-0", accountsMsgDto);
        log.info("Is the Communication request successfully triggered ? : {}", result);
    }
    @Override
    public CustomerDto getAccountByMobileNumber(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );

        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString())
        );

        CustomerDto customerDto = CustomerMapper.mapToCustomerDto(customer, new CustomerDto());
        customerDto.setAccountsDto(AccountsMapper.mapToAccountsDto(accounts, new AccountsDto()));

        return customerDto;
    }

    @Override
    public boolean updateAccount(CustomerDto customerDto) {

        boolean isUpdated = false;
        AccountsDto accountsDto = customerDto.getAccountsDto();

        if(accountsDto != null) {
            Accounts accounts = accountsRepository.findById(accountsDto.getAccountNumber()).orElseThrow(
                    () -> new ResourceNotFoundException("Account", "AccountNumber", accountsDto.getAccountNumber().toString())
            );
            AccountsMapper.mapToAccounts(accountsDto, accounts);

            accounts = accountsRepository.save(accounts);

            Long customerId = accounts.getCustomerId();

            Customer customer = customerRepository.findById(customerId).orElseThrow(
                    () -> new ResourceNotFoundException("Customer", "CustomerID", customerId.toString())
            );

            CustomerMapper.mapToCustomer(customerDto,customer);
            customerRepository.save(customer);
            isUpdated = true;
        }
        return isUpdated;
    }

    @Override
    public boolean deleteAccount(String mobileNumber) {
        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        accountsRepository.deleteByCustomerId(customer.getCustomerId());
        customerRepository.deleteById(customer.getCustomerId());
        return true;
    }

    private Accounts createNewAccount(Customer customer) {
        Accounts newAccount = new Accounts();
        newAccount.setCustomerId(customer.getCustomerId());
        long randomAccNumber = 1000000000L + new Random().nextInt(900000000);
        newAccount.setAccountNumber(randomAccNumber);
        newAccount.setAccountType(AccountsConstants.SAVINGS);
        newAccount.setBranchAddress(AccountsConstants.ADDRESS);
        //newAccount.setCreatedAt(LocalDateTime.now());
        //newAccount.setCreatedBy("iCress");
        return newAccount;
    }

    @Override
    @Transactional
    public boolean updateMobileNumber(MobileNumberUpdateDto mobileNumberUpdateDto) {
        boolean result = false;

        try {
            String currentMobileNum = mobileNumberUpdateDto.getCurrentMobileNumber();
            String currentMobileNum2 = mobileNumberUpdateDto.getNewMobileNumber();
            Accounts accounts = accountsRepository.findByMobileNumber(currentMobileNum2).orElseThrow(() -> new ResourceNotFoundException("Customer", "mobileNumber", currentMobileNum2)
            );
            Long customerId = accounts.getCustomerId();

            Customer customer = customerRepository.findById(customerId).orElseThrow(
                    () -> new ResourceNotFoundException("Customer", "CustomerID", customerId.toString())
            );

            customer.setMobileNumber(mobileNumberUpdateDto.getNewMobileNumber());
            customerRepository.save(customer);

            log.info("Account service call made, mobile number already updated in customer service");

            updateCardMobileNumber(mobileNumberUpdateDto);
            result = true;
        }catch (Exception e){
            log.error("Error occurred while updating mobile number", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

            rollbackCustomerMobileNumber(mobileNumberUpdateDto);
        }
        return result;
    }

    private void updateCardMobileNumber(MobileNumberUpdateDto mobileNumberUpdateDto) {
        log.info("Sending updateCardMobileNumber request for the details: {}", mobileNumberUpdateDto);
        var result = streamBridge.send("updateCardMobileNumber-out-0", mobileNumberUpdateDto);
        log.info("Is the updateCardMobileNumber request successfully triggered ? : {}", result);
    }

    /*Added later to rollback mobile number update from account*/
    @Override
    public boolean rollbackMobileNumber(MobileNumberUpdateDto mobileNumberUpdateDto) {

        String newMobileNumber = mobileNumberUpdateDto.getNewMobileNumber();
        Customer customer = customerRepository.findByMobileNumber(newMobileNumber).orElseThrow(() -> new ResourceNotFoundException("Customer", "mobileNumber", newMobileNumber)
        );
        customer.setMobileNumber(mobileNumberUpdateDto.getCurrentMobileNumber());
        customerRepository.save(customer);

        /*we should call rollback from customer service also
        rollbackCustomerMobileNumber()*/
        return true;
    }

    private void rollbackCustomerMobileNumber(MobileNumberUpdateDto mobileNumberUpdateDto) {
        log.info("Sending rollbackCustomerMobileNumber request for the details: {}", mobileNumberUpdateDto);
        var result = streamBridge.send("rollbackCustomerMobileNumber-out-0",mobileNumberUpdateDto);
        log.info("Is the rollbackCustomerMobileNumber request successfully triggered ? : {}", result);
    }
}
