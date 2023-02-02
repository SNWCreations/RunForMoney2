package snw.rfm.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static snw.rfm.util.Util.pluginMsg;
import static snw.rfm.util.Util.sendSuccess;

public class RFMTimerCommand implements CommandExecutor {
    public static final Set<UUID> ACTIVE = Collections.synchronizedSet(new HashSet<>());

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            ACTIVE.add(((Player) sender).getUniqueId());
            sendSuccess(sender);
        } else {
            sender.sendMessage(pluginMsg(ChatColor.RED + "需要玩家执行此分支。"));
        }
        return true;
    }
}
