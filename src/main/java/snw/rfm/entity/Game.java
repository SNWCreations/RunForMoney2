package snw.rfm.entity;

import java.util.Collection;

import org.apache.commons.lang.Validate;

import org.bukkit.event.Listener;
import snw.rfm.listeners.AttackListener;
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
        listeners.add(new AttackListener(this));
        // TODO add CoinTimer
    }

    public void stop() {
        // TODO game termination
        listeners.clear();
    }

    public CoinMap getCoinMap() {
        return coinMap;
    }
}
