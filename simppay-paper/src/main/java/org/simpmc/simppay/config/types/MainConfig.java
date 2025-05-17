package org.simpmc.simppay.config.types;

import de.exlll.configlib.Comment;
import de.exlll.configlib.Configuration;

@Configuration
public class MainConfig {
    public boolean debug = true;
    @Comment("Thời gian gọi API kiểm tra thẻ và giao dịch ngân hàng, tính theo giây")
    public int intervalApiCall = 5;
    @Comment("Thời gian làm mới cache, được sử dụng cho placeholder và menu, tính theo giây")
    public int intervalRefreshCache = 5;
}
