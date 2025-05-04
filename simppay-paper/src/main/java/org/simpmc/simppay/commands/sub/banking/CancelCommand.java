package org.simpmc.simppay.commands.sub.banking;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.executors.CommandArguments;
import org.bukkit.entity.Player;
import org.simpmc.simppay.SPPlugin;
import org.simpmc.simppay.config.ConfigManager;
import org.simpmc.simppay.config.types.MessageConfig;
import org.simpmc.simppay.util.MessageUtil;

public class CancelCommand {
    public static CommandAPICommand commandCreate() {
        return new CommandAPICommand("cancel")
                .executesPlayer(CancelCommand::execute);
    }

    public static void execute(Player player, CommandArguments args) {
        SPPlugin plugin = SPPlugin.getInstance();
        MessageConfig messageConfig = (MessageConfig) ConfigManager.configs.get(MessageConfig.class);

        if (!plugin.getPaymentService().getPlayerBankingSessionPayment().containsKey(player.getUniqueId())) {
            MessageUtil.sendMessage(player, messageConfig.noExistBankingSession);
        } else {
            MessageUtil.sendMessage(player, messageConfig.cancelBanking);
            plugin.getPaymentService().removePlayerQRSession(player.getUniqueId()); // nice
            player.updateInventory(); // remove qr map
        }
    }
}
