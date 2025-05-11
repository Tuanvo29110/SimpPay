package org.simpmc.simppay.menu.card.anvil;

import me.devnatan.inventoryframework.AnvilInput;
import me.devnatan.inventoryframework.View;
import me.devnatan.inventoryframework.ViewConfigBuilder;
import me.devnatan.inventoryframework.ViewType;
import me.devnatan.inventoryframework.context.OpenContext;
import me.devnatan.inventoryframework.context.RenderContext;
import org.jetbrains.annotations.NotNull;
import org.simpmc.simppay.config.ConfigManager;
import org.simpmc.simppay.config.types.menu.card.anvil.CardSerialMenuConfig;
import org.simpmc.simppay.model.detail.CardDetail;
import org.simpmc.simppay.util.MessageUtil;

public class CardSerialView extends View {
    final AnvilInput anvilInput = AnvilInput.createAnvilInput();

    @Override
    public void onInit(ViewConfigBuilder config) {

        config.cancelInteractions();
        config.type(ViewType.ANVIL);
        config.use(anvilInput);
    }

    @Override
    public void onOpen(@NotNull OpenContext open) {
        CardSerialMenuConfig menuConfig = ConfigManager.getInstance().getConfig(CardSerialMenuConfig.class);
        open.modifyConfig().title(MessageUtil.getComponentParsed(menuConfig.title, open.getPlayer())); // Title support papi
    }

    @Override
    public void onFirstRender(@NotNull RenderContext render) {
        CardSerialMenuConfig menuConfig = ConfigManager.getInstance().getConfig(CardSerialMenuConfig.class);

        render.firstSlot(
                menuConfig.item.getItemStack(render.getPlayer())
        );

        render.resultSlot().onClick(
                click -> {
                    final String serial = anvilInput.get(click);
                    CardDetail detail = (CardDetail) click.getInitialData();
                    detail.setSerial(serial);
                    click.openForPlayer(CardPINView.class, detail);
                }
        );

    }
}
