package snw.rfm.item;

import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import snw.rfm.ConfigConstant;
import snw.rfm.Main;
import snw.rfm.api.events.GameStopEvent;
import snw.rfm.api.events.HunterCatchPlayerEvent;
import snw.rfm.api.item.ItemRegistry;
import snw.rfm.api.item.RightClickCallback;
import snw.rfm.tasks.SendingActionBarMessage;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class IgnoreCard implements Listener, RightClickCallback {
    private static final ItemStack ITEM;
    private final Main main;
    private final Map<String, AtomicInteger> timeMap = new ConcurrentHashMap<>();

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
        this.main = main;
        ItemRegistry.add("ignore_card", ITEM, this);
        main.getServer().getPluginManager().registerEvents(this, main);
    }

    @Override
    public boolean onClick(Player player, ItemStack stack) {
        if (timeMap.containsKey(player.getUniqueId().toString())) {
            timeMap.get(player.getUniqueId().toString()).addAndGet(ConfigConstant.IGNORE_TIME);
        } else {
            timeMap.put(player.getUniqueId().toString(), new AtomicInteger(ConfigConstant.IGNORE_TIME));
            new BukkitRunnable() {
                @Override
                public void run() {
                    int remaining = timeMap.get(player.getUniqueId().toString()).getAndDecrement();
                    new SendingActionBarMessage(
                            new TextComponent(ChatColor.RED + "抗性卡还有 " + remaining + " 秒失效"),
                            Collections.singleton(player)
                    ).start(main);
                    if (remaining <= 0) {
                        timeMap.remove(player.getUniqueId().toString());
                        cancel();
                    }
                }
            }.runTaskTimer(main, 0L, 1L);
        }
        return true;
    }

    @EventHandler
    public void onCaught(HunterCatchPlayerEvent e) {
        if (timeMap.containsKey(e.getPlayer().getBukkitPlayer().getUniqueId().toString())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onStop(GameStopEvent e) {
        timeMap.clear();
    }
}
