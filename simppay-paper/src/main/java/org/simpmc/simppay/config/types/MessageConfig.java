package org.simpmc.simppay.config.types;

import de.exlll.configlib.Configuration;
import de.exlll.configlib.SerializeWith;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.simpmc.simppay.config.serializers.SoundComponentSerializer;
import org.simpmc.simppay.config.serializers.TextComponentSerializer;
import org.simpmc.simppay.data.PaymentStatus;

import java.util.Map;

@Configuration
public class MessageConfig {
    private final MiniMessage mm = MiniMessage.miniMessage();

    // <gradient:#f9535c:#FCD05C>SimpPay</gradient>
    public Component prefix = mm.deserialize("<gray>[<gradient:#f9535c:#FCD05C>SimpPay</gradient><gray>] <reset>");

    public Component configReloaded = mm.deserialize("<green>Đã reload config thành công!");

    public Component successQueueCard = mm.deserialize("<green>Thẻ của bạn đang được xử lý, vui lòng chờ trong giây lát...");

    public Component failedCard = mm.deserialize("<red>Nạp thẻ thất bại!");

    public Component successBanking = mm.deserialize("<green>Nạp qua ngân hàng thành công với mệnh giá {amount}đ!");

    public Component cancelBanking = mm.deserialize("<red>Đã hủy yêu cầu thanh toán ngân hàng!");

    public Component successQueueBanking = mm.deserialize("&aHãy quét mã QR ở bên tay trái của bạn để thanh toán!");


    // TODO: store sound directly or have a toSound method
    public Map<PaymentStatus, SoundConfig> soundEffect = Map.of(
            PaymentStatus.SUCCESS, new SoundConfig(Key.key(Key.MINECRAFT_NAMESPACE, "entity.player.levelup"), 1, 1), // /playsound minecraft:entity.player.levelup ambient @a ~ ~ ~ 1 1
            PaymentStatus.PENDING, new SoundConfig(Key.key(Key.MINECRAFT_NAMESPACE, "block.amethyst_block.resonate"), 1, 1), // /playsound minecraft:block.amethyst_block.resonate master ThatCorona ~ ~ ~ 1 1
            PaymentStatus.FAILED, new SoundConfig(Key.key(Key.MINECRAFT_NAMESPACE, "block.amethyst_block.resonate"), 1, 0.5F) // /playsound minecraft:block.amethyst_block.resonate master ThatCorona ~ ~ ~ 1 0.5
    );

}
