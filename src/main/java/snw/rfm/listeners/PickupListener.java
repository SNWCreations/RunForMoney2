package snw.rfm.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import snw.rfm.commands.SlowItemCommand;
import snw.rfm.entity.TeamRegistry;

public class PickupListener implements Listener {
    @EventHandler
    public void onPick(EntityPickupItemEvent e) {
        if (e.getEntity() instanceof Player) {
            if (TeamRegistry.HUNTER.contains(((Player) e.getEntity()))) {
                ItemStack stack = e.getItem().getItemStack();
                if (stack.hasItemMeta()) {
                    if (stack.getItemMeta()
                            .getPersistentDataContainer()
                            .has(SlowItemCommand.SLOW_KEY, PersistentDataType.BYTE)
                    ) {
                        e.setCancelled(true);
                    }
                }
            }
        }
    }
}
