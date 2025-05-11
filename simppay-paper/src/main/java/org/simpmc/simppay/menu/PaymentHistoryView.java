package org.simpmc.simppay.menu;

import com.google.common.base.Preconditions;
import me.devnatan.inventoryframework.View;
import me.devnatan.inventoryframework.ViewConfigBuilder;
import me.devnatan.inventoryframework.component.Pagination;
import me.devnatan.inventoryframework.context.OpenContext;
import me.devnatan.inventoryframework.context.RenderContext;
import me.devnatan.inventoryframework.state.State;
import org.jetbrains.annotations.NotNull;
import org.simpmc.simppay.SPPlugin;
import org.simpmc.simppay.config.ConfigManager;
import org.simpmc.simppay.config.types.data.menu.DisplayItem;
import org.simpmc.simppay.config.types.data.menu.RoleType;
import org.simpmc.simppay.config.types.menu.PaymentHistoryMenuConfig;
import org.simpmc.simppay.data.PaymentType;
import org.simpmc.simppay.database.entities.SPPlayer;
import org.simpmc.simppay.util.MessageUtil;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class PaymentHistoryView extends View {


    private final State<Pagination> paginationState = buildLazyPaginationState(context -> {
        SPPlayer spPlayer;
        if (context.getInitialData() == null) { // TODO: should be async ?
            spPlayer = SPPlugin.getInstance().getPlayerService().findByUuid(context.getPlayer().getUniqueId());
        } else {
            spPlayer = SPPlugin.getInstance().getPlayerService().findByName((String) context.getInitialData());
        }
        Preconditions.checkNotNull(spPlayer, "Player not found");
        return SPPlugin.getInstance().getPaymentLogService().getPaymentsByPlayer(spPlayer);

    }).elementFactory((ctx, bukkitItemComponentBuilder, i, paymentRecord) -> {
        PaymentHistoryMenuConfig config = ConfigManager.getInstance().getConfig(PaymentHistoryMenuConfig.class);

        if (paymentRecord.getPaymentType() == PaymentType.BANKING) {
            DisplayItem item = config.cardItem.clone().replaceStringInName("{amount}", value -> {
                String formattedValue = String.format("%,.0f", paymentRecord.getAmount());
                return formattedValue + "đ";
            });
            List<String> lores = item.getLores().stream()
                    .map(line ->
                            line.replace("{time}", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                                            .format(Instant.ofEpochMilli(paymentRecord.getTimestamp().getTime()).atZone(ZoneId.systemDefault())))
                                    .replace("{serial}", paymentRecord.getSerial().orElse("0"))
                                    .replace("{pin}", paymentRecord.getPin().orElse("0"))
                                    .replace("{api}", paymentRecord.getProvider())
                                    .replace("{transaction_id}", paymentRecord.getRefId()
                                    ))
                    .toList();
            item.setLores(lores);
            bukkitItemComponentBuilder.withItem(item.getItemStack(ctx.getPlayer()));
        }

        if (paymentRecord.getPaymentType() == PaymentType.CARD) {
            DisplayItem item = config.cardItem.clone().replaceStringInName("{amount}", value -> {
                        String formattedValue = String.format("%,.0f", paymentRecord.getAmount());
                        return formattedValue + "đ";
                    })
                    .replaceStringInName("{card_type}", paymentRecord.getTelco());
            List<String> lores = item.getLores().stream()
                    .map(line ->
                            line.replace("{time}", String.valueOf(paymentRecord.getTimestamp()))
                                    .replace("{api}", paymentRecord.getProvider())
                                    .replace("{transaction_id}", paymentRecord.getRefId()
                                    ))
                    .toList();
            item.setLores(lores);
            bukkitItemComponentBuilder.withItem(item.getItemStack(ctx.getPlayer()));
        }
    }).build();

    @Override
    public void onInit(ViewConfigBuilder config) {
        config.cancelInteractions();
        config.layout(ConfigManager.getInstance().getConfig(PaymentHistoryMenuConfig.class).layout.toArray(new String[0]));
    }

    @Override
    public void onOpen(@NotNull OpenContext open) {
        PaymentHistoryMenuConfig menuConfig = ConfigManager.getInstance().getConfig(PaymentHistoryMenuConfig.class);
        Pagination pagination = paginationState.get(open);
        String title = menuConfig.title.replace("{page}", String.valueOf(pagination.currentPage()))
                .replace("{maxPage}", String.valueOf(pagination.lastPage()));
        open.modifyConfig().title(MessageUtil.getComponentParsed(title, open.getPlayer())); // Title support papi
    }

    @Override
    public void onFirstRender(@NotNull RenderContext render) {
        PaymentHistoryMenuConfig menuConfig = ConfigManager.getInstance().getConfig(PaymentHistoryMenuConfig.class);

        Map<Character, DisplayItem> displayedItems = menuConfig.displayItems;

        for (Map.Entry<Character, DisplayItem> entry : displayedItems.entrySet()) {
            DisplayItem item = entry.getValue();
            if (item.getRole() == RoleType.NONE) {
                render.layoutSlot(entry.getKey(), entry.getValue().getItemStack());
            }
            if (item.getRole() == RoleType.PREV_PAGE) {
                render.layoutSlot(entry.getKey(), item.getItemStack())
                        .updateOnStateChange(paginationState)
                        .onClick((ctx) -> {
                            paginationState.get(ctx).back();
                        });
            }
            if (item.getRole() == RoleType.NEXT_PAGE) {
                render.layoutSlot(entry.getKey(), item.getItemStack())
                        .updateOnStateChange(paginationState)
                        .onClick((ctx) -> {
                            paginationState.get(ctx).advance();
                        });
            }
        }
    }
}
