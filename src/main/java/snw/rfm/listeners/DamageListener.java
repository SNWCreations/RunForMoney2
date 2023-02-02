package snw.rfm.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import snw.rfm.ConfigConstant;
import snw.rfm.ExitReason;
import snw.rfm.api.events.HunterCatchPlayerEvent;
import snw.rfm.entity.Game;
import snw.rfm.entity.IngamePlayer;
import snw.rfm.entity.TeamRegistry;

import static snw.rfm.util.Util.broadcast;
import static snw.rfm.util.Util.fireEvent;

public class DamageListener implements Listener {
    protected final Game game;

    public DamageListener(Game game) {
        this.game = game;
    }

    @EventHandler
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
                    if (ConfigConstant.END_ROOM_LOCATION != null) {
                        attacked.getBukkitPlayer().teleport(ConfigConstant.END_ROOM_LOCATION);
                    }
                    game.getCoinMap().calc(attacked);
                    broadcast(attacked, ExitReason.BE_CAUGHT);

                    // stop game if there is no more player
                    if (ConfigConstant.STOP_GAME_ON_NO_PLAYER) {
                        if (TeamRegistry.RUNNER.size() == 0) {
                            game.stop();
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
