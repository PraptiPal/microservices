package com.icress.cards.repository;

import com.icress.cards.entity.Cards;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CardsRepository extends JpaRepository<Cards,Long> {

    Optional<Cards> findByMobileNumber(String mobileNumber);

    Optional<Cards> findByCardNumber(String cardNumber);
}
