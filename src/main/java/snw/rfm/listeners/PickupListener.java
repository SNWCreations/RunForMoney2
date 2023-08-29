package snw.rfm.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import snw.rfm.entity.TeamRegistry;

public class PickupListener implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPick(EntityPickupItemEvent e) {
        if (e.getEntity() instanceof Player) {
            if (TeamRegistry.HUNTER.contains(((Player) e.getEntity()))) {
                e.setCancelled(true);
            }
        }
    }
}
