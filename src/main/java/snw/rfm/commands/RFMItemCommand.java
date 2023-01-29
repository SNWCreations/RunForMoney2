package snw.rfm.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import snw.rfm.api.item.ItemRegistry;

import java.util.Collections;
import java.util.List;

import static snw.rfm.util.Util.*;

public class RFMItemCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (isPlayer(sender)) {
            if (args.length == 1) {
                String itemName = args[0];
                ItemStack itemStack = ItemRegistry.get(itemName);
                if (itemStack != null) {
                    ((Player) sender).getInventory().addItem(itemStack);
                    sendSuccess(sender);
                    sender.sendMessage(pluginMsg(ChatColor.YELLOW + "若在背包中找不到物品，请检查你的背包是否已满。"));
                } else {
                    sender.sendMessage(pluginMsg(ChatColor.RED + "操作失败。找不到物品。"));
                }
            } else {
                sender.sendMessage(pluginMsg(ChatColor.RED + "参数不足。"));
                return false;
            }
        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return args.length == 1 ? filterTab(args[0], ItemRegistry.keySet()) : Collections.emptyList();
    }
}
