package org.simpmc.simppay.commands.sub.admin;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.StringArgument;
import dev.jorel.commandapi.executors.CommandArguments;
import org.bukkit.entity.Player;
import org.simpmc.simppay.SPPlugin;
import org.simpmc.simppay.menu.PaymentHistoryView;
import org.simpmc.simppay.menu.ServerPaymentHistoryView;

public class ViewHistoryCommand {
    public static CommandAPICommand commandCreate() {
        return new CommandAPICommand("viewhistory")
                .withPermission("simppay.admin.viewhistory")
                .withAliases("lichsu", "lichsunap")
                .withOptionalArguments(
                        new StringArgument("player")
                )
                .executesPlayer(ViewHistoryCommand::execute);
    }

    public static void execute(Player player, CommandArguments args) {
        String playerName = (String) args.getOptional("player").orElse(null);
        if (playerName == null) {
            // view entire server history
            SPPlugin.getInstance().getViewFrame().open(ServerPaymentHistoryView.class, player);
        }
//        if (FloodgateApi.getInstance().isFloodgateId(player.getUniqueId())) {
//            Player targetPlayer = Bukkit.getPlayer(playerName);
//            FloodgateApi.getInstance().sendForm(player.getUniqueId(), ViewHistoryForm.getHistoryForm(targetPlayer));
//            return;
//        }
        SPPlugin.getInstance().getViewFrame().open(PaymentHistoryView.class, player, playerName);
    }
}
