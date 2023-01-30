package snw.rfm.listeners;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import snw.rfm.Main;

import static snw.rfm.util.Util.pluginMsg;

public class PlayerJoinListener implements Listener {
    private final Main main;

    public PlayerJoinListener(Main main) {
        this.main = main;
        main.getServer().getPluginManager().registerEvents(this, main);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        if (player.getGameMode() == GameMode.SURVIVAL) {
            player.setGameMode(GameMode.ADVENTURE); // No Survival Mode in game!
        }
        player.sendMessage(pluginMsg(ChatColor.GREEN + "欢迎！"));
        player.sendMessage(pluginMsg(ChatColor.GREEN + "插件版本: " + main.getDescription().getVersion()));
        player.sendMessage(pluginMsg(ChatColor.GREEN + "作者: ZX夏夜之风"));
    }
}
