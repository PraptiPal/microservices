package com.icress.accounts.client;


import com.icress.accounts.dto.CardsDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "cards", fallback = CardsFallback.class)
public interface CardsFeignClient {

    @GetMapping(value = "/api/cards/{mobileNumber}", consumes = "application/json")
    public ResponseEntity<CardsDto> fetchCardDetails(@PathVariable String mobileNumber);
}
