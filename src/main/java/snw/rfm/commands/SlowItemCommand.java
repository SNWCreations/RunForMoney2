package snw.rfm.commands;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import snw.rfm.Main;

import java.util.ArrayList;
import java.util.List;

import static snw.rfm.util.Util.pluginMsg;
import static snw.rfm.util.Util.sendSuccess;

public class SlowItemCommand implements CommandExecutor {
    public static NamespacedKey SLOW_KEY;
    private static final String LORE = ChatColor.RED + "缓慢 II";

    public static void init(Main main) {
        SLOW_KEY = new NamespacedKey(main, "slow_item");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            ItemStack item = ((Player) sender).getInventory().getItemInMainHand();
            if (item.getType().isItem()) {
                ItemMeta itemMeta = item.getItemMeta();
                boolean prevHas = itemMeta.getPersistentDataContainer().has(SLOW_KEY, PersistentDataType.BYTE);
                if (prevHas) {
                    itemMeta.getPersistentDataContainer().remove(SLOW_KEY);
                    // cleanup lore
                    if (itemMeta.hasLore()) {
                        @SuppressWarnings("DataFlowIssue") // NotNull at this time!
                        List<String> lore = new ArrayList<>(itemMeta.getLore());
                        if (lore.size() > 1) {
                            if (lore.get(0).equals(LORE)) {
                                lore.remove(0);
                            }
                            if (lore.size() > 1) { // if still > 1
                                if (lore.get(0).equals("")) {
                                    lore.remove(0);
                                }
                            }
                        }
                        itemMeta.setLore(lore);
                    }
                } else {
                    itemMeta.getPersistentDataContainer().set(SLOW_KEY, PersistentDataType.BYTE, (byte) 1);
                    // add lore
                    List<String> lore = new ArrayList<>();
                    lore.add(LORE);
                    if (itemMeta.hasLore()) {
                        lore.add("");
                        // noinspection DataFlowIssue // NotNull at this time!
                        lore.addAll(itemMeta.getLore());
                    }
                    itemMeta.setLore(lore);
                }
                item.setItemMeta(itemMeta); // return back
                sendSuccess(sender);
                sender.sendMessage(pluginMsg("已" + ((prevHas) ? ChatColor.RED + "移除" : ChatColor.GREEN + "添加") + ChatColor.RESET + "标签。"));
            } else {
                sender.sendMessage(pluginMsg(ChatColor.RED + "操作失败。这不是一个有效的物品。"));
            }
        } else {
            sender.sendMessage(pluginMsg(ChatColor.RED + "此命令需要玩家执行。"));
        }
        return true;
    }
}
