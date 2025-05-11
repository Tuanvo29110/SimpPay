package org.simpmc.simppay.commands.root;

import dev.jorel.commandapi.CommandAPICommand;
import org.simpmc.simppay.SPPlugin;
import org.simpmc.simppay.menu.card.CardListView;

public class NaptheCommand {

    public NaptheCommand() {
        new CommandAPICommand("napthe")
                .withPermission("simppay.napthe")
                .executesPlayer((player, args) -> {
                    // start a new napthe session
                    SPPlugin.getInstance().getViewFrame().open(CardListView.class, player);
                })
                .register();
    }
}
