package snw.rfm.api.events;

import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import snw.rfm.entity.Game;

public class HunterReleasedEvent extends RFMEvent {
    private static final HandlerList handlers = new HandlerList();
    private final Game game;

    public HunterReleasedEvent(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
