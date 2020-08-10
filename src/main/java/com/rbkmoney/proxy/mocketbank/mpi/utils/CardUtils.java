package com.rbkmoney.proxy.mocketbank.mpi.utils;

import com.rbkmoney.proxy.mocketbank.mpi.model.Card;

import java.util.List;
import java.util.Optional;

public class CardUtils {

    public static Optional<Card> extractCardByPan(List<Card> cardList, String pan) {
        return cardList.stream().filter(card -> card.getPan().equals(pan)).findFirst();
    }

    public static boolean isEnrolled(Optional<Card> card) {
        boolean result = false;
        if (card.isPresent()) {
            MpiAction action = MpiAction.findByValue(card.get().getAction());
            switch (action) {
                case THREE_D_SECURE_FAILURE:
                case THREE_D_SECURE_TIMEOUT:
                case THREE_D_SECURE_SUCCESS:
                    result = true;
                    break;
                default:
                    result = false;
            }
        }
        return result;
    }
}
