package snw.rfm.api.item;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface RightClickCallback {

    boolean onClick(Player player, ItemStack stack);
}
