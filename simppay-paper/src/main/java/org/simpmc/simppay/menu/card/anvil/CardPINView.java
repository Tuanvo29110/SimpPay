package org.simpmc.simppay.menu.card.anvil;

import me.devnatan.inventoryframework.AnvilInput;
import me.devnatan.inventoryframework.View;
import me.devnatan.inventoryframework.ViewConfigBuilder;
import me.devnatan.inventoryframework.ViewType;
import me.devnatan.inventoryframework.context.OpenContext;
import me.devnatan.inventoryframework.context.RenderContext;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.simpmc.simppay.SPPlugin;
import org.simpmc.simppay.config.ConfigManager;
import org.simpmc.simppay.config.types.MessageConfig;
import org.simpmc.simppay.config.types.menu.card.anvil.CardPinMenuConfig;
import org.simpmc.simppay.data.PaymentStatus;
import org.simpmc.simppay.model.Payment;
import org.simpmc.simppay.model.detail.CardDetail;
import org.simpmc.simppay.util.MessageUtil;
import org.simpmc.simppay.util.SoundUtil;

import java.util.UUID;

public class CardPINView extends View {
    final AnvilInput anvilInput = AnvilInput.createAnvilInput();

    @Override
    public void onInit(ViewConfigBuilder config) {

        config.cancelInteractions();
        config.type(ViewType.ANVIL);
        config.use(anvilInput);
    }

    @Override
    public void onOpen(@NotNull OpenContext open) {
        CardPinMenuConfig menuConfig = ConfigManager.getInstance().getConfig(CardPinMenuConfig.class);
        open.modifyConfig().title(MessageUtil.getComponentParsed(menuConfig.title, open.getPlayer())); // Title support papi
    }

    @Override
    public void onFirstRender(@NotNull RenderContext render) {
        CardPinMenuConfig menuConfig = ConfigManager.getInstance().getConfig(CardPinMenuConfig.class);

        render.firstSlot(
                menuConfig.item.getItemStack(render.getPlayer())
        );

        render.resultSlot().onClick(
                click -> {
                    final String pin = anvilInput.get(click);
                    CardDetail detail = (CardDetail) click.getInitialData();
                    detail.setPin(pin);
                    UUID uuid = UUID.nameUUIDFromBytes(detail.serial.getBytes()); // payment uuid is based on serial number of the card
                    Player player = click.getPlayer();
                    Payment payment = new Payment(uuid, player.getUniqueId(), detail);
                    click.closeForPlayer();

                    MessageConfig messageConfig = ConfigManager.getInstance().getConfig(MessageConfig.class);

                    if (SPPlugin.getInstance().getPaymentService().getPayments().containsKey(payment.getPaymentID())) {
                        MessageUtil.sendMessage(player, messageConfig.pendingCard);
                        SoundUtil.sendSound(player, messageConfig.soundEffect.get(PaymentStatus.PENDING).toSound());
                        return;
                    }

                    PaymentStatus status = SPPlugin.getInstance().getPaymentService().sendCard(payment);

                    if (status == PaymentStatus.FAILED) {
                        MessageUtil.sendMessage(player, messageConfig.failedCard);
                        SoundUtil.sendSound(player, messageConfig.soundEffect.get(PaymentStatus.FAILED).toSound());
                    }


                }
        );

    }
}
