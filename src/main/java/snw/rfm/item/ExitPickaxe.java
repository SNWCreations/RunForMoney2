package snw.rfm.item;

import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import snw.rfm.ExitReason;
import snw.rfm.Main;
import snw.rfm.api.item.ItemRegistry;
import snw.rfm.entity.IngamePlayer;
import snw.rfm.entity.TeamRegistry;

import java.util.Arrays;
import java.util.Objects;

import static snw.rfm.util.Util.broadcast;

public class ExitPickaxe implements Listener {
    private static final ItemStack ITEM;
    private final Main main;

    static {
        ItemStack ep = new ItemStack(Material.WOODEN_PICKAXE);
        ItemMeta epmeta = ep.getItemMeta();
        //noinspection ConstantConditions
        epmeta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "弃权镐");
        epmeta.setLore(
                Arrays.asList(
                        ChatColor.GOLD + "" + ChatColor.BOLD + "你好？",
                        ChatColor.GOLD + "" + ChatColor.BOLD + "你是谁？",
                        ChatColor.GOLD + "" + ChatColor.BOLD + "你要做什么？",
                        ChatColor.GOLD + "" + ChatColor.BOLD + "你要弃权吗？",
                        ChatColor.GOLD + "" + ChatColor.BOLD + "你真的要弃权吗？",
                        ChatColor.GOLD + "" + ChatColor.BOLD + "你真的确定要弃权吗？",
                        ChatColor.GOLD + "" + ChatColor.BOLD + "你真的确定一定要弃权吗？"
                )
        );
        Damageable converted_meta = (Damageable) epmeta;
        converted_meta.setDamage(58);
        ep.setItemMeta((ItemMeta) converted_meta);
        NBTItem item = new NBTItem(ep);
        item.getStringList("CanDestroy").add("minecraft:diamond_block");
        ITEM = item.getItem();
    }

    public ExitPickaxe(Main main) {
        this.main = main;
        ItemRegistry.add("exit_pickaxe", ITEM);
        main.getServer().getPluginManager().registerEvents(this, main);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if (main.isGamePresent()) {
            ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
            if (!item.hasItemMeta()) {
                return;
            }
            ItemMeta meta = item.getItemMeta();
            assert meta != null;
            ((Damageable) meta).setDamage(58);
            item.setItemMeta(meta);

            if (!ITEM.isSimilar(item)) {
                return;
            }

            if (event.getBlock().getType() != Material.DIAMOND_BLOCK) {
                return;
            }

            Player player = event.getPlayer();
            IngamePlayer wrap = IngamePlayer.getWrappedPlayer(player);
            TeamRegistry.OUT.add(wrap, false);
            player.setHealth(Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue());
            broadcast(wrap, ExitReason.SELF_EXIT);
        }
    }
}
