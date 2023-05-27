package snw.rfm.commands;

import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
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
