package snw.rfm.item;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import snw.rfm.ConfigConstant;
import snw.rfm.Main;
import snw.rfm.api.item.ItemRegistry;
import snw.rfm.api.item.RightClickCallback;
import snw.rfm.entity.IngamePlayer;
import snw.rfm.entity.TeamRegistry;
import snw.rfm.tasks.PlayerSlow;

import java.util.Arrays;

public class FreezeCard implements Listener, RightClickCallback {
    private static final ItemStack ITEM;
    private final BlockData iceData;
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
        this.iceData = main.getServer().createBlockData(Material.ICE);
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
                .filter(IT -> TeamRegistry.HUNTER.contains(IngamePlayer.getWrappedPlayer(((Player) IT))))
                .forEach(i -> new PlayerSlow(((Player) i), iceData).start(main));
        return true;
    }

}

