package snw.rfm.util;

import org.bukkit.ChatColor;
import snw.rfm.ExitReason;
import snw.rfm.Main;
import snw.rfm.entity.IngamePlayer;
import snw.rfm.entity.TeamRegistry;

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
}
