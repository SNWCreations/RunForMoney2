package snw.rfm.entity;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import snw.rfm.ConfigConstant;
import snw.rfm.ExitReason;
import snw.rfm.Main;
import snw.rfm.api.GameController;
import snw.rfm.api.events.internal.RemoveTimeEvent;
import snw.rfm.api.events.internal.RequestTimeEvent;

import static snw.rfm.util.Util.broadcast;
import static snw.rfm.util.Util.fireEvent;

public final class GameControllerImpl implements GameController {
    private final Main main;
    private final Game game;

    public GameControllerImpl(Main main, Game game) {
        this.main = main;
        this.game = game;
    }

    @Override
    public void timeReverse() {
        game.getCoinMap().reverse();
    }

    @Override
    public boolean isTimeReversed() {
        return game.getCoinMap().isReversed();
    }

    @Override
    public int getRemainingTime() {
        RequestTimeEvent e = new RequestTimeEvent(game);
        fireEvent(e);
        return e.getData();
    }

    @Override
    public void removeRemainingTime(int secsToRemove) throws IllegalArgumentException {
        removeRemainingTime(secsToRemove, true);
    }

    @Override
    public void removeRemainingTime(int secsToRemove, boolean addCoin) throws IllegalArgumentException {
        RemoveTimeEvent e = new RemoveTimeEvent(game);
        e.setData(secsToRemove);
        e.setAddCoin(addCoin);
        fireEvent(e);
    }

    @Override
    public void pause() throws IllegalStateException {
        game.pause();
    }

    @Override
    public void resume() throws IllegalStateException {
        game.resume();
    }

    @Override
    public boolean isPaused() {
        return game.isPaused();
    }

    @Override
    public boolean respawn(Player player) {
        if (TeamRegistry.RUNNER.contains(player)) {
            return false;
        }
        TeamRegistry.RUNNER.add(IngamePlayer.getWrappedPlayer(player), false);
        return true;
    }

    @Override
    public void forceOut(Player player) throws IllegalStateException {
        if (TeamRegistry.OUT.contains(player)) {
            throw new IllegalStateException("Already out");
        }
        broadcast(IngamePlayer.getWrappedPlayer(player), ExitReason.FORCEOUT);
        TeamRegistry.OUT.add(player, false);
    }

    @Override
    public void setCoinPerSecond(int cps) throws IllegalArgumentException {
        if (cps == 0) {
            throw new IllegalArgumentException("Coin per second cannot be zero");
        }
        ConfigConstant.COIN_PER_SECOND = cps;
    }

    @Override
    public int getCoinPerSecond() {
        return ConfigConstant.COIN_PER_SECOND;
    }

    @Override
    public double getMoney(OfflinePlayer player) {
        return game.getCoinMap().get(player);
    }

    @Override
    public void setMoney(OfflinePlayer player, double amount) {
        game.getCoinMap().set(player, amount);
    }

    @Override
    public void addMoney(OfflinePlayer player, double coin) {
        game.getCoinMap().add(player, coin);
    }

    @Override
    public void clearCoin() {
        game.getCoinMap().reset();
    }
}
