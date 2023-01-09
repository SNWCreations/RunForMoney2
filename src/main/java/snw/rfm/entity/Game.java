package snw.rfm.entity;

import java.util.Collection;

import org.apache.commons.lang.Validate;

import org.bukkit.event.Listener;
import snw.rfm.ConfigConstant;
import snw.rfm.Main;
import snw.rfm.events.GameStartEvent;
import snw.rfm.events.GameStopEvent;
import snw.rfm.listeners.AttackListener;
import snw.rfm.tasks.CoinTimer;
import snw.rfm.util.ListenerList;

public class Game {
    protected volatile boolean running = false;
    protected final CoinMap coinMap;
    protected final Collection<Listener> listeners = new ListenerList();

    public Game() {
        coinMap = new CoinMap();
    }

    public boolean isRunning() {
        return running;
    }

    public void start() {
        Validate.isTrue(!isRunning(), "The game is running.");
        running = true;
        // TODO game initialization
        Main.getInstance().getServer().getPluginManager().callEvent(new GameStartEvent(this));
        listeners.add(new AttackListener(this));
        new CoinTimer(this, ConfigConstant.GAME_TIME * 60).start();
    }

    public void stop() {
        // TODO game termination
        Main.getInstance().getServer().getPluginManager().callEvent(new GameStopEvent(this));
        listeners.clear();
    }

    public CoinMap getCoinMap() {
        return coinMap;
    }
}
