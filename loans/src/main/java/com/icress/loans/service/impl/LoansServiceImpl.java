package com.icress.loans.service.impl;

import com.icress.loans.constants.LoansConstants;
import com.icress.loans.dto.LoansDto;
import com.icress.loans.dto.MobileNumberUpdateDto;
import com.icress.loans.entity.Loans;
import com.icress.loans.exceptions.LoanAlreadyExistsException;
import com.icress.loans.exceptions.ResourceNotFoundException;
import com.icress.loans.mapper.LoansMapper;
import com.icress.loans.repository.LoansRepository;
import com.icress.loans.service.ILoansService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
@Slf4j
public class LoansServiceImpl implements ILoansService {

    private LoansRepository loansRepository;

    private final StreamBridge streamBridge;
    @Override
    public void createLoan(String mobileNumber) {
        Optional<Loans> optionalLoans= loansRepository.findByMobileNumber(mobileNumber);
        if(optionalLoans.isPresent()){
            throw new LoanAlreadyExistsException("Loan already registered with given mobileNumber "+mobileNumber);
        }
        loansRepository.save(createNewLoan(mobileNumber));
    }

    private Loans createNewLoan(String mobileNumber) {
        Loans newLoan = new Loans();
        long randomLoanNumber = 100000000000L + new Random().nextInt(900000000);
        newLoan.setLoanNumber(Long.toString(randomLoanNumber));
        newLoan.setMobileNumber(mobileNumber);
        newLoan.setLoanType(LoansConstants.HOME_LOAN);
        newLoan.setTotalLoan(LoansConstants.NEW_LOAN_LIMIT);
        newLoan.setAmountPaid(0);
        newLoan.setOutstandingAmount(LoansConstants.NEW_LOAN_LIMIT);
        return newLoan;
    }

    @Override
    public LoansDto fetchLoan(String mobileNumber) {
        Loans loans = loansRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Loan", "mobileNumber", mobileNumber)
        );
        return LoansMapper.mapToLoansDto(loans, new LoansDto());
    }

    @Override
    public boolean updateLoan(LoansDto loansDto) {
        Loans loans = loansRepository.findByLoanNumber(loansDto.getLoanNumber()).orElseThrow(
                () -> new ResourceNotFoundException("Loan", "LoanNumber", loansDto.getLoanNumber()));
        LoansMapper.mapToLoans(loansDto, loans);
        loansRepository.save(loans);
        return  true;
    }

     @Override
    public boolean deleteLoan(String mobileNumber) {
        Loans loans = loansRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Loan", "mobileNumber", mobileNumber)
        );
        loansRepository.deleteById(loans.getLoanId());
        return true;
    }

    @Override
    @Transactional
    public boolean updateMobileNumber(MobileNumberUpdateDto mobileNumberUpdateDto) {
        boolean result = false;

        try {
            String currentMobileNum = mobileNumberUpdateDto.getCurrentMobileNumber();
            Loans loans = loansRepository.findByMobileNumber(currentMobileNum).orElseThrow(() -> new ResourceNotFoundException("Customer", "mobileNumber", currentMobileNum)
            );

            loans.setMobileNumber(mobileNumberUpdateDto.getNewMobileNumber());
            loansRepository.save(loans);
        }catch (Exception e){
            log.error("Error occurred while updating mobile number", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

            rollbackCardMobileNumber(mobileNumberUpdateDto);
        }
        return result;
    }

    private void rollbackCardMobileNumber(MobileNumberUpdateDto mobileNumberUpdateDto) {
        log.info("Sending rollbackCardMobileNumber request for the details: {}", mobileNumberUpdateDto);
        var result = streamBridge.send("rollbackCardMobileNumber-out-0",mobileNumberUpdateDto);
        log.info("Is the rollbackCardMobileNumber request successfully triggered ? : {}", result);
    }
}
