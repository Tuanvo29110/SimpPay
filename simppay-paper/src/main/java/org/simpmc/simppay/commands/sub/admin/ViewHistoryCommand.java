package org.simpmc.simppay.commands.sub.admin;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.StringArgument;
import dev.jorel.commandapi.executors.CommandArguments;
import org.bukkit.entity.Player;
import org.simpmc.simppay.SPPlugin;
import org.simpmc.simppay.menu.PaymentHistoryView;

public class ViewHistoryCommand {
    public static CommandAPICommand commandCreate() {
        return new CommandAPICommand("viewhistory")
                .withPermission("simppay.admin.viewhistory")
                .withArguments(
                        new StringArgument("player")
                )
                .executesPlayer(ViewHistoryCommand::execute);
    }

    public static void execute(Player player, CommandArguments args) {
        String playerName = (String) args.get("player");
        SPPlugin.getInstance().getViewFrame().open(PaymentHistoryView.class, player, playerName);
    }
}
