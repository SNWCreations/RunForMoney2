package snw.rfm.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import snw.rfm.ConfigConstant;
import snw.rfm.ExitReason;
import snw.rfm.Main;
import snw.rfm.util.Util;
import snw.rfm.entity.Game;
import snw.rfm.entity.IngamePlayer;
import snw.rfm.entity.TeamRegistry;
import snw.rfm.events.HunterCatchPlayerEvent;

public class AttackListener implements Listener {
    protected final Game game;

    public AttackListener(Game game) {
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
                    Main.getInstance().getServer().getPluginManager().callEvent(hcp);
                    if (hcp.isCancelled()) {
                        return;
                    }
                    TeamRegistry.RUNNER.remove(attacked);
                    if (ConfigConstant.END_ROOM_LOCATION != null) {
                        attacked.getBukkitPlayer().teleport(ConfigConstant.END_ROOM_LOCATION);
                    }
                    game.getCoinMap().calc(attacked);
                    Util.broadcast(attacked, ExitReason.BE_CAUGHT);
                }
            }
        }
    }
}
