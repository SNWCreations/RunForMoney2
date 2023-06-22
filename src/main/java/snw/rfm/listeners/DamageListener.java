package snw.rfm.listeners;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;
import snw.rfm.ConfigConstant;
import snw.rfm.ExitReason;
import snw.rfm.Main;
import snw.rfm.api.events.HunterCatchPlayerEvent;
import snw.rfm.entity.Game;
import snw.rfm.entity.IngamePlayer;
import snw.rfm.entity.TeamRegistry;

import java.util.Objects;

import static snw.rfm.util.Util.broadcast;
import static snw.rfm.util.Util.fireEvent;

public class DamageListener implements Listener {
    protected final Main main;
    protected final Game game;

    public DamageListener(Main main, Game game) {
        this.main = main;
        this.game = game;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onAttack(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getEntity() instanceof Player) {
            IngamePlayer attacker = IngamePlayer.getWrappedPlayer((Player) event.getDamager());
            IngamePlayer attacked = IngamePlayer.getWrappedPlayer(((Player) event.getEntity()));
            if (TeamRegistry.HUNTER.contains(attacker)) {
                if (TeamRegistry.RUNNER.contains(attacked)) {
                    event.setDamage(0);
                    HunterCatchPlayerEvent hcp = new HunterCatchPlayerEvent(attacker, attacked);
                    fireEvent(hcp);
                    if (hcp.isCancelled()) {
                        return;
                    }
                    TeamRegistry.OUT.add(attacked, false);

                    if (ConfigConstant.DROP_ITEM_AFTER_CAUGHT) {
                        Location loc = attacked.getBukkitPlayer().getLocation();
                        final PlayerInventory inv = attacked.getBukkitPlayer().getInventory();
                        for (ItemStack stack : inv.getContents()) {
                            if (stack == null) {
                                continue;
                            }
                            Objects.requireNonNull(loc.getWorld()).dropItem(loc, stack);
                        }
                        inv.clear();
                    }

                    if (ConfigConstant.END_ROOM_LOCATION != null) {
                        if (ConfigConstant.TELEPORT_AFTER_CAUGHT <= 0) {
                            attacked.getBukkitPlayer().teleport(ConfigConstant.END_ROOM_LOCATION);
                        } else {
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    attacked.getBukkitPlayer().teleport(ConfigConstant.END_ROOM_LOCATION);
                                }
                            }.runTaskLater(main, 20L * ConfigConstant.TELEPORT_AFTER_CAUGHT);
                        }
                    }
                    game.getCoinMap().calc(attacked);
                    broadcast(attacked, ExitReason.BE_CAUGHT);

                    // stop game if there is no more player
                    if (ConfigConstant.STOP_GAME_ON_NO_PLAYER) {
                        if (TeamRegistry.RUNNER.size() == 0) {
                            main.terminateGame();
                        }
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onGeneralDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            if (e.getCause() != EntityDamageEvent.DamageCause.VOID) {
                e.setCancelled(true);
            }
        }
    }
}
