package org.simpmc.simppay.commands.sub.admin;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.StringArgument;
import dev.jorel.commandapi.executors.CommandArguments;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.simpmc.simppay.SPPlugin;
import org.simpmc.simppay.database.entities.SPPlayer;

public class ReloadServerMilestoneCommand {
    public static CommandAPICommand commandCreate() {
        return new CommandAPICommand("reloadservermilestone")
                .withPermission("simppay.admin.reloadservermilestone")
                .executes(ReloadServerMilestoneCommand::execute);
    }

    public static void execute(CommandSender player, CommandArguments args) {
        SPPlugin plugin = SPPlugin.getInstance();

        plugin.getMilestoneService().loadServerMilestone();
    }
}
