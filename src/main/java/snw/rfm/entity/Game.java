package snw.rfm.entity;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang.Validate;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import snw.rfm.ConfigConstant;
import snw.rfm.Main;
import snw.rfm.events.GameStartEvent;
import snw.rfm.events.GameStopEvent;
import snw.rfm.listeners.AttackListener;
import snw.rfm.tasks.CoinTimer;
import snw.rfm.tasks.HunterReleaseTimer;
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
        Main.getInstance().getServer().getScheduler().runTaskAsynchronously(
            Main.getInstance(),
            () -> {
                Main.getInstance().getServer().getPluginManager().callEvent(new GameStartEvent(this));
            }
        );
        listeners.add(new AttackListener(this));
        if (ConfigConstant.HUNTER_RELEASE_TIME > 0) {
            new HunterReleaseTimer(this, ConfigConstant.HUNTER_RELEASE_TIME).start();
        } else {
            Main.getInstance().getServer().broadcastMessage(ChatColor.RED + "" + ChatColor.BOLD + "游戏开始");
        }
    }

    public void stop() {
        // TODO game termination
        for (Player player : TeamRegistry.RUNNER.toBukkitPlayerList()) {
            player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "游戏结束");
        }
        Main.getInstance().getServer().getScheduler().runTaskAsynchronously(
                Main.getInstance(),
                () -> {
                    Main.getInstance().getServer().getPluginManager().callEvent(new GameStopEvent(this));
                }
        );
        listeners.clear();
        TeamRegistry.HUNTER.clear();
        TeamRegistry.RUNNER.clear();
        IngamePlayer.getAllKnownWrappers().clear();
    }

    public CoinMap getCoinMap() {
        return coinMap;
    }
}
