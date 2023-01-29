package snw.rfm.events.internal;

import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import snw.rfm.entity.Game;

public class RequestTimeEvent extends RFMInternalEvent {
    private static final HandlerList handlers = new HandlerList();

    private final Game game;
    private int data;

    public RequestTimeEvent(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
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
