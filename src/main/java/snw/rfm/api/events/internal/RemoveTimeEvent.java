package snw.rfm.api.events.internal;

import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import snw.rfm.entity.Game;

public class RemoveTimeEvent extends RFMInternalEvent {
    private static final HandlerList handlers = new HandlerList();

    private final Game game;
    private int data;
    private boolean addCoin;

    public RemoveTimeEvent(Game game) {
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

    public boolean isAddCoin() {
        return addCoin;
    }

    public void setAddCoin(boolean addCoin) {
        this.addCoin = addCoin;
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
