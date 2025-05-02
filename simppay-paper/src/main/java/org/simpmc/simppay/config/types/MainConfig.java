package org.simpmc.simppay.config.types;

import de.exlll.configlib.Comment;
import de.exlll.configlib.Configuration;
import org.simpmc.simppay.handler.data.CoinsAPI;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class MainConfig {
    final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    public boolean debug = true;
    public int intervalAPICall = 5;
    @Comment("Plugin sử dụng để cộng xu: PLAYERPOINTS")
    public CoinsAPI pointsProvider = CoinsAPI.PLAYERPOINTS;

    @Comment({"Giá trị khuyến mãi thêm",
            "VD: ",
            "0: Không có khuyến mãi",
            "0.5: Khuyến mãi 50%, VD: nạp 100k được 150k",
            "1: Khuyến mãi 100%, VD: nạp 100k được 200k",
            "1.5: Khuyến mãi 150%, VD: nạp 100k được 250k"})
    public double promoRate = 0.0;

    @Comment("Thời gian kết thúc khuyến mãi")
    public LocalDateTime promoEndTime = LocalDateTime.parse("30/04/1975 11:30", formatter);
}
