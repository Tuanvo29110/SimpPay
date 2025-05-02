package org.simpmc.simppay.commands.root;

import dev.jorel.commandapi.CommandAPICommand;

public class BankingCommand {
    public BankingCommand() {
        new CommandAPICommand("banking")
                .withPermission("simppay.banking")
                .withAliases("bank")
                .executesPlayer((player, args) -> {
                    // start a new banking session
                })
                .register();
    }
}
