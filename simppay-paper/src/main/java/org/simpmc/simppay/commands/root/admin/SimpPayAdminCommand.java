package org.simpmc.simppay.commands.root.admin;

import dev.jorel.commandapi.CommandAPICommand;
import org.simpmc.simppay.commands.sub.admin.FakeBankCommand;
import org.simpmc.simppay.commands.sub.admin.FakeCardCommand;
import org.simpmc.simppay.commands.sub.admin.ReloadCommand;
import org.simpmc.simppay.commands.sub.admin.ViewHistoryCommand;

public class SimpPayAdminCommand {
    public SimpPayAdminCommand() {
        new CommandAPICommand("simppayadmin")
                .withPermission("simppay.admin")
                .withSubcommands(
                        ReloadCommand.commandCreate(),
                        ViewHistoryCommand.commandCreate(),
                        FakeBankCommand.commandCreate(),
                        FakeCardCommand.commandCreate()
                )
                .register();
    }
}
