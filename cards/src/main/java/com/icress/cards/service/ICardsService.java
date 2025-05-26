package com.icress.cards.service;

import com.icress.cards.dto.CardsDto;
import com.icress.cards.dto.MobileNumberUpdateDto;

public interface ICardsService {
    void createCard(String mobileNumber);
    CardsDto fetchCard(String mobileNumber);
    boolean updateCard(CardsDto cardsDto);
    boolean deleteCard(String mobileNumber);
    boolean updateMobileNumber(MobileNumberUpdateDto mobileNumberUpdateDto);

    boolean rollbackMobileNumber(MobileNumberUpdateDto mobileNumberUpdateDto);
}

