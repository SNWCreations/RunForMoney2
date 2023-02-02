package snw.rfm.item;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import snw.rfm.ConfigConstant;
import snw.rfm.Main;
import snw.rfm.api.events.HunterCatchPlayerEvent;
import snw.rfm.api.item.ItemRegistry;
import snw.rfm.api.item.RightClickCallback;
import snw.rfm.entity.IngamePlayer;
import snw.rfm.entity.TeamRegistry;

import java.util.Arrays;

import static snw.rfm.util.Util.buildPlayerName;
import static snw.rfm.util.Util.pluginMsg;

public class PauseCard implements RightClickCallback, Listener {
    private static final ItemStack ITEM;
    private final Main main;
    private boolean working;

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
        main.getServer().getPluginManager().registerEvents(this, main);
        ItemRegistry.add("pause_card", ITEM, this);
    }

    @Override
    public boolean onClick(Player player, ItemStack stack) {
        working = true;
        Bukkit.broadcastMessage(pluginMsg(
                String.format("%s%s 使用了猎人暂停卡。全体猎人暂停 %s 秒。", ChatColor.GREEN, buildPlayerName(player), ConfigConstant.HUNTER_PAUSE_TIME)
        ));
        new BukkitRunnable() {
            @Override
            public void run() {
                PauseCard.this.working = false;
            }
        }.runTaskLater(main, ConfigConstant.HUNTER_PAUSE_TIME * 20L);
        return true;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (main.isGamePresent()) {
            if (TeamRegistry.HUNTER.contains(IngamePlayer.getWrappedPlayer(e.getPlayer()))) {
                if (working) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onCatch(HunterCatchPlayerEvent e) {
        if (working) {
            e.setCancelled(true);
        }
    }
}