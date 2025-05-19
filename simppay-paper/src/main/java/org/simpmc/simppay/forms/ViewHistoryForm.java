package org.simpmc.simppay.forms;

import com.google.common.base.Preconditions;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.SimpleForm;
import org.simpmc.simppay.SPPlugin;
import org.simpmc.simppay.database.dto.PaymentRecord;
import org.simpmc.simppay.database.entities.SPPlayer;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ViewHistoryForm {
    public static SimpleForm getHistoryForm(Player player) {
        SimpleForm.Builder simpleForm = SimpleForm.builder();

        fetchPaymentRecordsAsync(player).thenAccept(paymentRecords -> {
            for (PaymentRecord paymentRecord : paymentRecords) {
                // TODO: Check and fix this
                String message = String.format(ChatColor.GREEN + "Số tiền: %s",
                        String.format("%,.0f", paymentRecord.getAmount()) + "đ");
                simpleForm.button(message);
            }
        });
        return simpleForm.build();
    }

    private static CompletableFuture<List<PaymentRecord>> fetchPaymentRecordsAsync(Player player) {
        return CompletableFuture.supplyAsync(() -> {
            SPPlayer spPlayer;
            spPlayer = SPPlugin.getInstance().getDatabaseService().getPlayerService().findByUuid(player.getUniqueId());
            Preconditions.checkNotNull(spPlayer, "Player not found");
            List<PaymentRecord> paymentRecords = SPPlugin.getInstance().getDatabaseService().getPaymentLogService().getPaymentsByPlayer(spPlayer);
            return paymentRecords;
        });
    }
}
