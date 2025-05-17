package org.simpmc.simppay.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class MilestoneCompleteEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    public MilestoneCompleteEvent() {
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
