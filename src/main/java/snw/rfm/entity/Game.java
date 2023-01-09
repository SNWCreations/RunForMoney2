package snw.rfm.entity;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.Validate;

import snw.rfm.Main;

public class Game {
    protected volatile boolean running = false;

    public boolean isRunning() {
        return running;
    }

    public void start() {
        Validate.isTrue(!isRunning(), "The game is running.");
        running = true;
        // TODO game initialization
    }

    public void stop() {
        // TODO game termination
    }

}
