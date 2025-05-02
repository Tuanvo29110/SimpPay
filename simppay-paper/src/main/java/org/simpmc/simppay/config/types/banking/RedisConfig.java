package org.simpmc.simppay.config.types.banking;

import de.exlll.configlib.Comment;
import de.exlll.configlib.Configuration;
import org.simpmc.simppay.handler.data.BankAPI;

@Configuration
public class RedisConfig {
    public String host = "localhost";
    public int port = 6379;
    public String password = "";

    @Comment({"Sử dụng Redis để nhận webhook từ dịch vụ thanh toán, khả dụng: PAYOS", "Yêu cầu cài đặt server simppay-webhook-receiver"})
    public BankAPI apiProvider = BankAPI.PAYOS; // TODO: dont allow redis mode...
}
