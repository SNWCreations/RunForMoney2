package snw.rfm.util;

import org.bukkit.ChatColor;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import snw.rfm.ExitReason;
import snw.rfm.Main;
import snw.rfm.entity.IngamePlayer;
import snw.rfm.entity.TeamRegistry;

import java.util.function.Consumer;
import java.util.function.Function;

public class Util {
    public static void broadcast(IngamePlayer player, ExitReason reason) {
        Main.getInstance().getServer().broadcastMessage(
                String.format("%s%s%s %s", ChatColor.RED, ChatColor.BOLD, buildPlayerName(player), reason.MESSAGE)
        );
        Main.getInstance().getServer().broadcastMessage(String.format("%s%s剩余 %s 人。", ChatColor.RED, ChatColor.BOLD, TeamRegistry.RUNNER.size()));
    }

    public static String buildPlayerName(IngamePlayer player) {
        return player.getBukkitPlayer().getName(); // TODO add nickname support here
    }

    public static String pluginMsg(String msg) {
        return ChatColor.GRAY + "[" + ChatColor.GOLD + "RunForMoney" + ChatColor.GRAY + "] " + ChatColor.RESET + msg;
    }
    
    public static <T extends Event> void tempListener(Class<T> type, Function<T, Boolean> function) {
        Listener listener = new Listener() {
            @EventHandler
            public void onEvent(T event) {
                Boolean apply = function.apply(event);
                if (Boolean.TRUE.equals(apply)) {
                    HandlerList.unregisterAll(this);
                }
            }
        };
        Main.getInstance().getServer().getPluginManager().registerEvents(listener, Main.getInstance());
    }
    
    public static void fireEvent(Event event) {
        Main.getInstance().getServer().getPluginManager().callEvent(event);
    }
}
