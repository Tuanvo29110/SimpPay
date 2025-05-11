package org.simpmc.simppay.data.card;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public enum CardPrice {
    _10K(10000),
    _20K(20000),
    _30K(30000),
    _50K(50000),
    _100K(100000),
    _200K(200000),
    _300K(300000),
    _500K(500000),
    _1000K(1000000);


    private final int value;

    CardPrice(int value) {
        this.value = value;
    }

    /**
     * Find the enum constant matching the given int value, or null if none.
     */
    public static CardPrice fromValue(int value) {
        for (CardPrice price : CardPrice.values()) {
            if (price.value == value) {
                return price;
            }
        }
        return null;
    }

    public static CardPrice fromString(String value) {
        for (CardPrice price : CardPrice.values()) {
            if (String.valueOf(price.value).equalsIgnoreCase(value)) {
                return price;
            }
        }
        return null;
    }

    public static List<String> getAllCardPrices() {
        return Arrays.stream(CardPrice.values())
                .map(cardPrice -> {
                    // sperated every 3 zeros and đ at the end
                    String formattedValue = String.format("%,d", cardPrice.getValue());
                    return formattedValue + "đ";
                })
                .collect(Collectors.toList());
    }
}
