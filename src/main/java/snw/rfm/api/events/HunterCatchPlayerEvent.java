package snw.rfm.api.events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import snw.rfm.entity.IngamePlayer;

// Cancel this event if you think the player should still alive.
public class HunterCatchPlayerEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final IngamePlayer hunter;
    private final IngamePlayer player;
    private boolean cancelled = false;

    public HunterCatchPlayerEvent(IngamePlayer hunter, IngamePlayer player) {
        this.hunter = hunter;
        this.player = player;
    }

    public IngamePlayer getHunter() {
        return hunter;
    }

    public IngamePlayer getPlayer() {
        return player;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
