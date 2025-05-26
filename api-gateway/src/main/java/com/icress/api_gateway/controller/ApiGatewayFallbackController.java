package com.icress.api_gateway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiGatewayFallbackController {

    @RequestMapping("/accounts-fallback")
    public ResponseEntity<String> accountsFallback() {
        return new ResponseEntity<>("Accounts service is temporarily unavailable. Please try after some time.",
                HttpStatus.SERVICE_UNAVAILABLE);
    }

    @RequestMapping("/loans-fallback")
    public ResponseEntity<String> loansFallback() {
        return new ResponseEntity<>("Loans service is temporarily unavailable. Please try after some time.",
                HttpStatus.SERVICE_UNAVAILABLE);
    }

    @RequestMapping("/cards-fallback")
    public ResponseEntity<String> cardsFallback() {
        return new ResponseEntity<>("Card service is temporarily unavailable. Please try after some time.",
                HttpStatus.SERVICE_UNAVAILABLE);
    }
}
