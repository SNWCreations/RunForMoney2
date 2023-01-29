package snw.rfm.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import snw.rfm.ExitReason;
import snw.rfm.Main;
import snw.rfm.entity.IngamePlayer;
import snw.rfm.entity.TeamRegistry;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Util {
    public static void broadcast(IngamePlayer player, ExitReason reason) {
        Bukkit.broadcastMessage(
                String.format("%s%s%s %s", ChatColor.RED, ChatColor.BOLD, buildPlayerName(player), reason.MESSAGE)
        );
        Bukkit.broadcastMessage(String.format("%s%s剩余 %s 人。", ChatColor.RED, ChatColor.BOLD, TeamRegistry.RUNNER.size()));
    }

    public static String buildPlayerName(IngamePlayer player) {
        return player.getBukkitPlayer().getName(); // TODO add nickname support here
    }

    public static String pluginMsg(String msg) {
        return ChatColor.GRAY + "[" + ChatColor.GOLD + "RunForMoney" + ChatColor.GRAY + "] " + ChatColor.RESET + msg;
    }
    
    public static void sendSuccess(CommandSender sender) {
        sender.sendMessage(pluginMsg(ChatColor.GREEN + "操作成功。"));
    }

    // the class type is ignored, it is just for providing specific methods access
    public static <T extends Event> void tempListener(Main main, Class<T> ignoredType, Function<T, Boolean> function) {
        Listener listener = new Listener() {
            @EventHandler
            public void onEvent(T event) {
                Boolean apply = function.apply(event);
                if (Boolean.TRUE.equals(apply)) {
                    HandlerList.unregisterAll(this);
                }
            }
        };
        Bukkit.getPluginManager().registerEvents(listener, main);
    }
    
    public static void fireEvent(Event event) {
        Bukkit.getPluginManager().callEvent(event);
    }

    public static boolean isInternalTeamName(String name) {
        return "runner".equals(name) || "hunter".equals(name) || "out".equals(name);
    }

    public static boolean isInvisibleTeamName(String name) {
        return "out".equals(name);
    }

    // --- Command utilities ---

    public static boolean isPlayer(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(pluginMsg(ChatColor.RED + "需要玩家执行此分支。"));
            return false;
        }
        return true;
    }

    public static List<String> filterTab(String current, Collection<String> collection) {
        return collection.stream().filter(i -> i.startsWith(current)).collect(Collectors.toList());
    }

    public static List<String> getAllPlayersName() {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
    }
}
