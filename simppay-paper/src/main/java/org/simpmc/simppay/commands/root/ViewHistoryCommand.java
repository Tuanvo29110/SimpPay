package org.simpmc.simppay.commands.root;

import dev.jorel.commandapi.CommandAPICommand;
import org.simpmc.simppay.SPPlugin;
import org.simpmc.simppay.menu.PaymentHistoryView;

public class ViewHistoryCommand {
    public ViewHistoryCommand() {
        new CommandAPICommand("lichsunapthe")
                .withPermission("simppay.lichsunapthe")
                .withAliases("napthehistory", "xemgdnapthe")
                .executesPlayer((player, args) -> {
                    SPPlugin.getInstance().getViewFrame().open(PaymentHistoryView.class, player);
                })
                .register();
    }
}
