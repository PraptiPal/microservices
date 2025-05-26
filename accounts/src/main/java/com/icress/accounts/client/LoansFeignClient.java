package com.icress.accounts.client;

import com.icress.accounts.dto.LoansDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "loans", fallback = LoansFallback.class)
public interface LoansFeignClient {

    @GetMapping(value = "/api/loans/{mobileNumber}", consumes = "application/json")
    public ResponseEntity<LoansDto> fetchLoanDetails(@PathVariable String mobileNumber);
}
