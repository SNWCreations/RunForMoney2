package snw.rfm.api.item.internal.items;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import snw.rfm.Main;
import snw.rfm.api.item.ItemRegistry;
import snw.rfm.entity.IngamePlayer;
import snw.rfm.entity.TeamRegistry;
import snw.rfm.events.GameStopEvent;

import java.util.Arrays;

import static snw.rfm.util.Util.pluginMsg;

public class RespawnCard implements Listener {
    private static final ItemStack ITEM;
    private final Main main;

    static {
        ItemStack stack = new ItemStack(Material.IRON_INGOT);
        stack.addUnsafeEnchantment(Enchantment.DURABILITY, 0);
        ItemMeta meta = stack.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "复活卡");
        meta.setLore(Arrays.asList(
                ChatColor.YELLOW + "右键一个玩家以使 TA 复活。",
                ChatColor.RED + "一次性用品！"
        ));
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        stack.setItemMeta(meta);
        ITEM = stack;
    }

    public RespawnCard(Main main) {
        this.main = main;
        ItemRegistry.add("respawn_card", ITEM);
        main.getServer().getPluginManager().registerEvents(this, main);
    }

    @EventHandler
    public void onInteract(PlayerInteractAtEntityEvent e) {
        if (main.isGamePresent()) {
            if (e.getRightClicked() instanceof Player) {
                if (e.getHand() == EquipmentSlot.HAND) {
                    ItemStack item = e.getPlayer().getInventory().getItemInMainHand();
                    if (ITEM.isSimilar(item)) {
                        Player rightClicked = (Player) e.getRightClicked();
                        if (!TeamRegistry.RUNNER.contains(IngamePlayer.getWrappedPlayer(rightClicked))) {
                            TeamRegistry.RUNNER.add(IngamePlayer.getWrappedPlayer(rightClicked));
                            Bukkit.broadcastMessage(pluginMsg(ChatColor.GREEN + "" + e.getPlayer().getName() + " 复活了 " + rightClicked.getName() + " ！"));
                        }
                        item.setAmount(item.getAmount() - 1);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onStop(GameStopEvent e) {
        HandlerList.unregisterAll(this);
    }
}