package org.simpmc.simppay.data.card;

import java.util.Arrays;

public enum CardType {
    VIETTEL,
    VINAPHONE,
    VIETNAMOBILE,
    MOBIFONE,
    GATE,
    GARENA,
    VCOIN,
    ZING;


    // adapt from string to card type, the given string is lowercase
    public static CardType fromString(String type) {
        return CardType.valueOf(type.toUpperCase());
    }

    public static String[] getAllCardTypes() {
        return Arrays.stream(CardType.values()).map(CardType::toString).toArray(String[]::new);
    }
}
