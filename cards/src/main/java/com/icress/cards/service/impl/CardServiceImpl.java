package com.icress.cards.service.impl;

import com.icress.cards.constants.CardsConstants;
import com.icress.cards.dto.CardsDto;
import com.icress.cards.dto.MobileNumberUpdateDto;
import com.icress.cards.entity.Cards;
import com.icress.cards.exceptions.CardAlreadyExistsException;
import com.icress.cards.exceptions.ResourceNotFoundException;
import com.icress.cards.mapper.CardsMapper;
import com.icress.cards.repository.CardsRepository;
import com.icress.cards.service.ICardsService;
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
public class CardServiceImpl implements ICardsService {

    private CardsRepository cardsRepository;
    private final StreamBridge streamBridge;
    @Override
    public void createCard(String mobileNumber) {

        Optional<Cards> optionalCards= cardsRepository.findByMobileNumber(mobileNumber);
        if(optionalCards.isPresent()){
            throw new CardAlreadyExistsException("Card already registered with given mobileNumber "+mobileNumber);
        }
        cardsRepository.save(createNewCard(mobileNumber));
    }

    @Override
    public CardsDto fetchCard(String mobileNumber) {
        Cards cards = cardsRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Card", "mobileNumber", mobileNumber)
        );
        return CardsMapper.mapToCardsDto(cards, new CardsDto());
    }

    @Override
    public boolean updateCard(CardsDto cardsDto) {
        Cards cards = cardsRepository.findByCardNumber(cardsDto.getCardNumber()).orElseThrow(
                () -> new ResourceNotFoundException("Card", "CardNumber", cardsDto.getCardNumber()));
        CardsMapper.mapToCards(cardsDto, cards);
        cardsRepository.save(cards);
        return  true;
    }

    @Override
    public boolean deleteCard(String mobileNumber) {
        Cards cards = cardsRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Card", "mobileNumber", mobileNumber)
        );
        cardsRepository.deleteById(cards.getCardId());
        return true;
    }

    private Cards createNewCard(String mobileNumber) {
        Cards newCard = new Cards();
        long randomCardNumber = 100000000000L + new Random().nextInt(900000000);
        newCard.setCardNumber(Long.toString(randomCardNumber));
        newCard.setMobileNumber(mobileNumber);
        newCard.setCardType(CardsConstants.CREDIT_CARD);
        newCard.setTotalLimit(CardsConstants.NEW_CARD_LIMIT);
        newCard.setAmountUsed(0);
        newCard.setAvailableAmount(CardsConstants.NEW_CARD_LIMIT);
        return newCard;
    }
    @Override
    @Transactional
    public boolean updateMobileNumber(MobileNumberUpdateDto mobileNumberUpdateDto) {
        boolean result = false;
        try {
            String currentMobileNum = mobileNumberUpdateDto.getCurrentMobileNumber();

            Cards cards = cardsRepository.findByMobileNumber(currentMobileNum).orElseThrow(() -> new ResourceNotFoundException("Customer", "mobileNumber", currentMobileNum)
            );

            cards.setMobileNumber(mobileNumberUpdateDto.getNewMobileNumber());

            cardsRepository.save(cards);

            updateLoanMobileNumber(mobileNumberUpdateDto);
        }catch (Exception e){
            log.error("Error occurred while updating mobile number", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

            rollbackAccountMobileNumber(mobileNumberUpdateDto);
        }
        return result;
    }


    private void updateLoanMobileNumber(MobileNumberUpdateDto mobileNumberUpdateDto) {
        log.info("Sending updateLoanMobileNumber request for the details: {}", mobileNumberUpdateDto);
        var result = streamBridge.send("updateLoanMobileNumber-out-0",mobileNumberUpdateDto);
        log.info("Is the updateLoanMobileNumber request successfully triggered ? : {}", result);
    }

    @Override
    public boolean rollbackMobileNumber(MobileNumberUpdateDto mobileNumberUpdateDto) {
        log.info("Rolling back mobile number update from cards");
        String newMobileNumber = mobileNumberUpdateDto.getNewMobileNumber();

        Cards cards = cardsRepository.findByMobileNumber(newMobileNumber).orElseThrow(() -> new ResourceNotFoundException("Customer", "mobileNumber", newMobileNumber)
        );

        cards.setMobileNumber(mobileNumberUpdateDto.getCurrentMobileNumber());
        cardsRepository.save(cards);

        rollbackAccountMobileNumber(mobileNumberUpdateDto);

        return true;
    }
    private void rollbackAccountMobileNumber(MobileNumberUpdateDto mobileNumberUpdateDto) {
        log.info("Sending rollbackAccountMobileNumber request for the details: {}", mobileNumberUpdateDto);
        var result = streamBridge.send("rollbackAccountMobileNumber-out-0",mobileNumberUpdateDto);
        log.info("Is the rollbackAccountMobileNumber request successfully triggered ? : {}", result);
    }
}
