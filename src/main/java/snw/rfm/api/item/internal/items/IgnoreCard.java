package snw.rfm.api.item.internal.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import snw.rfm.ConfigConstant;
import snw.rfm.Main;
import snw.rfm.api.item.ItemRegistry;
import snw.rfm.api.item.RightClickCallback;
import snw.rfm.events.HunterCatchPlayerEvent;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class IgnoreCard implements Listener, RightClickCallback {
    private static final ItemStack ITEM;
    private final Set<String> activeSet = new HashSet<>();

    static {
        ItemStack stack = new ItemStack(Material.IRON_INGOT);
        stack.addUnsafeEnchantment(Enchantment.DURABILITY, 0);
        ItemMeta meta = stack.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "抗性卡");
        meta.setLore(Arrays.asList(
                ChatColor.YELLOW + "右键以使自己在 " + ConfigConstant.IGNORE_TIME + " 秒内处于无敌状态。",
                ChatColor.YELLOW + "在无敌状态时猎人对你的抓捕无效。",
                ChatColor.RED + "一次性用品！"
        ));
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        stack.setItemMeta(meta);
        ITEM = stack;
    }

    public IgnoreCard(Main main) {
        ItemRegistry.add("ignore_card", ITEM, this);
        main.getServer().getPluginManager().registerEvents(this, main);
    }

    @Override
    public boolean onClick(Player player, ItemStack stack) {
        synchronized (activeSet) { // keep sync
            activeSet.add(player.getUniqueId().toString());
        }
        return true;
    }

    @EventHandler
    public void onCaught(HunterCatchPlayerEvent e) {
        synchronized (activeSet) {
            if (activeSet.contains(e.getPlayer().getBukkitPlayer().getUniqueId().toString())) {
                e.setCancelled(true);
            }
        }
    }
}
