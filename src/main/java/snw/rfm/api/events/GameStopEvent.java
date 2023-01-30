package snw.rfm.api.events;

import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import snw.rfm.entity.Game;

public class GameStopEvent extends RFMEvent {
    private static final HandlerList handlers = new HandlerList();
    private final Game game;

    public GameStopEvent(Game game) {
        super(true);
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
