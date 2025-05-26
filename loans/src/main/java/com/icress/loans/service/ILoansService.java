package com.icress.loans.service;

import com.icress.loans.dto.LoansDto;
import com.icress.loans.dto.MobileNumberUpdateDto;

public interface ILoansService {

    void createLoan(String mobileNumber);
    LoansDto fetchLoan(String mobileNumber);
    boolean updateLoan(LoansDto loansDto);
    boolean deleteLoan(String mobileNumber);
    boolean updateMobileNumber(MobileNumberUpdateDto mobileNumberUpdateDto);

}
