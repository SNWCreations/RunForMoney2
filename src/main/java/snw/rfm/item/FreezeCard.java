package snw.rfm.item;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.data.BlockData;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import snw.rfm.ConfigConstant;
import snw.rfm.Main;
import snw.rfm.api.item.ItemRegistry;
import snw.rfm.api.item.RightClickCallback;

import java.util.Arrays;
import java.util.Objects;

public class FreezeCard implements Listener, RightClickCallback {
    private static final ItemStack ITEM;
    static BlockData ICE;
    private final Main main;

    static {
        ItemStack stack = new ItemStack(Material.IRON_INGOT);
        stack.addUnsafeEnchantment(Enchantment.DURABILITY, 0);
        ItemMeta meta = stack.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "冰冻卡");
        meta.setLore(Arrays.asList(
                ChatColor.YELLOW + "右键以使距离自己 " + ConfigConstant.FREEZE_RADIUS + " 格内的所有猎人" +
                        "被冰冻 " + ConfigConstant.FREEZE_TIME + " 秒。",
                ChatColor.YELLOW + "在无敌状态时猎人对你的抓捕无效。",
                ChatColor.RED + "一次性用品！"
        ));
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        stack.setItemMeta(meta);
        ITEM = stack;
    }

    public FreezeCard(Main main) {
        this.main = main;
        ICE = main.getServer().createBlockData(Material.ICE);
        ItemRegistry.add("freeze_card", ITEM, this);
    }

    @Override
    public boolean onClick(Player player, ItemStack stack) {
        //noinspection IntegerDivisionInFloatingPointContext
        player
                .getNearbyEntities(ConfigConstant.FREEZE_RADIUS / 2, 1, ConfigConstant.FREEZE_RADIUS / 2)
                .stream()
                .filter(IT -> IT.getType() == EntityType.PLAYER)
                .filter(IT -> IT != player)
                .forEach(i -> new PlayerSlow(((Player) i)).start(main));
        return true;
    }

}

class PlayerSlow extends BukkitRunnable implements Listener {
    private final Player player;
    private int ticks;

    public PlayerSlow(Player player) {
        this.player = player;
        this.ticks = ConfigConstant.FREEZE_TIME * 20;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (e.getPlayer() == player) {
            e.setCancelled(true);
        }
    }

    @Override
    public void run() {
        if (ticks-- > 0) {
            Objects.requireNonNull(player.getLocation().getWorld())
                    .spawnParticle(Particle.BLOCK_DUST, player.getLocation(), 5, FreezeCard.ICE);
        } else {
            cleanup();
        }
    }

    private void cleanup() {
        HandlerList.unregisterAll(this);
        cancel();
    }

    public void start(Main main) {
        runTaskTimer(main, 0L, 1L);
        main.getServer().getPluginManager().registerEvents(this, main);
    }
}
