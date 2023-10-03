package snw.rfm.tasks;

import org.bukkit.Particle;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import snw.rfm.ConfigConstant;
import snw.rfm.Main;
import snw.rfm.api.events.HunterCatchPlayerEvent;

import java.util.Objects;

public class PlayerSlow extends BukkitRunnable implements Listener {
    private final Player player;
    private final BlockData blockData;
    private int ticks;

    public PlayerSlow(Player player, BlockData blockData) {
        this.player = player;
        this.blockData = blockData;
        this.ticks = ConfigConstant.FREEZE_TIME * 20;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (e.getPlayer() == player) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onCaught(HunterCatchPlayerEvent e) {
        if (e.getHunter().getBukkitPlayer() == player) {
            e.setCancelled(true);
        }
    }

    @Override
    public void run() {
        if (ticks-- > 0) {
            Objects.requireNonNull(player.getLocation().getWorld())
                    .spawnParticle(Particle.BLOCK_DUST, player.getLocation(), 5, blockData);
        } else {
            cleanup();
        }
    }

    private void cleanup() {
        HandlerList.unregisterAll(this);
        cancel();
    }

    public void start(Main main) {
        runTaskTimer(main, 0L, 1L);
        main.getServer().getPluginManager().registerEvents(this, main);
    }
}
