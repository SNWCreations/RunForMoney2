package snw.rfm.commands;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import snw.rfm.ConfigConstant;
import snw.rfm.Main;
import snw.rfm.tasks.SendingActionBarMessage;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static snw.rfm.util.Util.*;

public class RFMTimerCommand implements CommandExecutor {
    public static final Set<UUID> ACTIVE = Collections.synchronizedSet(new HashSet<>());
    private final Main main;

    public RFMTimerCommand(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (isPlayer(sender)) {
            if (!main.isGamePresent()) {
                sender.sendMessage(pluginMsg(ChatColor.RED + "游戏未运行。"));
            }
            if (ConfigConstant.NO_TIMER) {
                ((Player) sender).spigot()
                        .sendMessage(ChatMessageType.ACTION_BAR,
                                new TextComponent(ChatColor.RED.asBungee() + "计时器不可用"));
                return true;
            }
            if (!sender.isOp()) {
                final int remainingTimeSec = main.getGame().getController().getRemainingTime();
                String sec = String.valueOf(remainingTimeSec % 60);
                new SendingActionBarMessage(
                        new TextComponent("剩余时间: " +
                                (remainingTimeSec / 60) + ":" + (sec.length() == 1 ? ("0" + sec) : sec)
                        ),
                        Collections.singleton((Player) sender),
                        60
                ).start(main);
            } else {
                if (!ACTIVE.contains(((Player) sender).getUniqueId())) {
                    ACTIVE.add(((Player) sender).getUniqueId());
                } else {
                    ACTIVE.remove(((Player) sender).getUniqueId());
                }
                sendSuccess(sender);
            }
        }
        return true;
    }
}
