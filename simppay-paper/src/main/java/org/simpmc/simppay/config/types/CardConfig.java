package org.simpmc.simppay.config.types;

import de.exlll.configlib.Comment;
import de.exlll.configlib.Configuration;
import org.simpmc.simppay.data.card.CardPrice;
import org.simpmc.simppay.data.card.CardType;
import org.simpmc.simppay.handler.data.CardAPI;

import java.util.Map;

@Configuration
public class CardConfig {
    @Comment("Dịch vụ gạch thẻ: THESIEUTOC")
    public CardAPI cardAPI = CardAPI.THESIEUTOC;

    @Comment("Lượng xu cộng vào tương ứng với giá trị thẻ cào")
    public Map<CardPrice, Long> cardToCoins = Map.of(
            CardPrice._10K, 10L,
            CardPrice._20K, 20L,
            CardPrice._30K, 30L,
            CardPrice._50K, 50L,
            CardPrice._100K, 100L,
            CardPrice._200K, 200L,
            CardPrice._300K, 300L,
            CardPrice._500K, 500L,
            CardPrice._1000K, 1000L
    );

    @Comment("Các nhà mạng đang bật")
    public Map<CardType, Boolean> cardTypes = Map.of(
            CardType.VIETTEL, true,
            CardType.VINAPHONE, true,
            CardType.VIETNAMOBILE, true,
            CardType.MOBIFONE, true,
            CardType.GATE, true,
            CardType.GARENA, true,
            CardType.VCOIN, true,
            CardType.ZING, true
    );

    @Comment({"Lệnh thực thi tương ứng với giá trị thẻ cào", "Có hỗ trợ PlaceholderAPI"})
    public Map<CardPrice, String> cardToCommand = Map.of(
            CardPrice._10K, "tell %player_name% Bạn vừa nạp 10k",
            CardPrice._20K, "tell %player_name% Bạn vừa nạp 20k",
            CardPrice._30K, "tell %player_name% Bạn vừa nạp 30k",
            CardPrice._50K, "tell %player_name% Bạn vừa nạp 50k",
            CardPrice._100K, "tell %player_name% Bạn vừa nạp 100k",
            CardPrice._200K, "tell %player_name% Bạn vừa nạp 200k",
            CardPrice._300K, "tell %player_name% Bạn vừa nạp 300k",
            CardPrice._500K, "tell %player_name% Bạn vừa nạp 500k",
            CardPrice._1000K, "tell %player_name% Bạn vừa nạp 1000k"
    );
}
