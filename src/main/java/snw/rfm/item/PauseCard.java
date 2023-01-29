package snw.rfm.item;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import snw.rfm.ConfigConstant;
import snw.rfm.Main;
import snw.rfm.api.item.ItemRegistry;
import snw.rfm.api.item.RightClickCallback;
import snw.rfm.entity.IngamePlayer;
import snw.rfm.entity.TeamRegistry;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

import static snw.rfm.util.Util.tempListener;

public class PauseCard implements RightClickCallback {
    private static final ItemStack ITEM;
    private final Main main;

    static {
        ItemStack stack = new ItemStack(Material.GOLD_INGOT);
        stack.addUnsafeEnchantment(Enchantment.DURABILITY, 0);
        ItemMeta meta = stack.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "猎人暂停卡");
        meta.setLore(Arrays.asList(
                ChatColor.YELLOW + "右键以使所有猎人暂停 " + ConfigConstant.HUNTER_PAUSE_TIME + "秒。",
                ChatColor.RED + "一次性用品！"
        ));
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        stack.setItemMeta(meta);
        ITEM = stack;
    }

    public PauseCard(Main main) {
        this.main = main;
        ItemRegistry.add("pause_card", ITEM, this);
    }

    @Override
    public boolean onClick(Player player, ItemStack stack) {
        AtomicBoolean dead = new AtomicBoolean(false);
        new BukkitRunnable() {
            @Override
            public void run() {
                dead.set(true);
            }
        }.runTaskLater(main, ConfigConstant.HUNTER_PAUSE_TIME * 20L);
        tempListener(main, PlayerMoveEvent.class, i -> {
            if (TeamRegistry.HUNTER.contains(IngamePlayer.getWrappedPlayer(i.getPlayer()))) {
                if (!dead.get()) {
                    i.setCancelled(true);
                }
            }
            return dead.get();
        });
        return true;
    }
}