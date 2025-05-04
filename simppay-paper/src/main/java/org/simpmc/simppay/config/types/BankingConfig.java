package org.simpmc.simppay.config.types;

import de.exlll.configlib.Comment;
import de.exlll.configlib.Configuration;
import org.simpmc.simppay.handler.data.BankAPI;

@Configuration
public class BankingConfig {
    @Comment("Dịch vụ cổng banking: REDIS, PAYOS")
    public BankAPI bankAPI = BankAPI.PAYOS;

    @Comment("Thời gian chờ thanh toán ngân hàng (giây)")
    public int bankingTimeout = 60 * 5; // 5 minutes
}
