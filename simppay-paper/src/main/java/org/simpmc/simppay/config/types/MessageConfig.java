package org.simpmc.simppay.config.types;

import de.exlll.configlib.Comment;
import de.exlll.configlib.Configuration;
import net.kyori.adventure.key.Key;
import org.simpmc.simppay.config.types.data.SoundConfig;
import org.simpmc.simppay.data.PaymentStatus;

import java.util.Map;

@Configuration
public class MessageConfig {

    // <gradient:#f9535c:#FCD05C>SimpPay</gradient>
    @Comment({"Tất cả message đều hỗ trợ PlaceholderAPI dưới dạng [papi:<placeholder>]",
            "Không điền % %, ví dụ <papi:player_name>"})
    public String prefix = "<gray>[<gradient:#f9535c:#FCD05C>SimpPay</gradient><gray>] <reset>";

    public String configReloaded = "<green>Đã reload config thành công!";

    public String successQueueCard = "<green>Thẻ của bạn đang được xử lý, vui lòng chờ trong giây lát...";

    public String failedCard = "<red>Nạp thẻ thất bại!";

    public String wrongPriceCard = "<red>Thẻ cào của bạn nhập sai mệnh giá, bạn đã được cộng thẻ trị giá <amount> vào tài khoản!";

    public String pendingCard = "<yellow>Thẻ của bạn đang được xử lý, vui lòng chờ trong giây lát...";

    public String successPayment = "<green>Nạp thành công với mệnh giá <amount>đ!";

    public String cancelBanking = "<red>Đã hủy yêu cầu thanh toán ngân hàng!";

    public String existBankingSession = "<red>Bạn đã tạo lệnh nạp trước đó rồi! Nếu muốn tạo lệnh nạp mới, hãy gõ /bank cancel";

    public String noExistBankingSession = "<red>Bạn chưa tạo lệnh nạp nào cả! Hãy gõ /bank <số tiền> để tạo lệnh nạp mới!";

    public String successQueueBanking = "<green>Hãy quét mã QR ở bên tay của bạn để thanh toán!";

    public String promptPaymentLink = "<green>Bạn có thể thanh toán qua đường dẫn sau nếu QR trên tay bị lỗi: <link>";

    public String mustDivisibleBy1000 = "<red>Số tiền phải chia hết cho 1000!";

    public String unknownErrror = "<red>Đã xảy ra lỗi không xác định, hãy báo cho admin server check log server!";

    public String invalidAmount = "<red>Số tiền nạp tối thiểu là {amount}!";

    // TODO: store sound directly or have a toSound method
    public Map<PaymentStatus, SoundConfig> soundEffect = Map.of(
            PaymentStatus.SUCCESS, new SoundConfig(Key.key(Key.MINECRAFT_NAMESPACE, "entity.player.levelup"), 1, 1), // /playsound minecraft:entity.player.levelup ambient @a ~ ~ ~ 1 1
            PaymentStatus.PENDING, new SoundConfig(Key.key(Key.MINECRAFT_NAMESPACE, "block.amethyst_block.resonate"), 1, 1), // /playsound minecraft:block.amethyst_block.resonate master ThatCorona ~ ~ ~ 1 1
            PaymentStatus.FAILED, new SoundConfig(Key.key(Key.MINECRAFT_NAMESPACE, "block.amethyst_block.resonate"), 1, 0.5F) // /playsound minecraft:block.amethyst_block.resonate master ThatCorona ~ ~ ~ 1 0.5
    );

}
