package snw.rfm.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import snw.rfm.Main;
import snw.rfm.entity.Game;
import snw.rfm.entity.TeamRegistry;

import java.util.Arrays;
import java.util.List;

import static snw.rfm.util.Util.pluginMsg;
import static snw.rfm.util.Util.sendSuccess;

public class RFMGameCommand implements TabExecutor {
    private final Main main;

    public RFMGameCommand(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage(pluginMsg(ChatColor.RED + "参数不足。"));
            return false;
        }
        switch (args[0]) {
            case "start":
                if (!main.isGamePresent()) {
                    if (args.length == 1 || !Boolean.parseBoolean(args[1])) { // if we need to check players
                        if (TeamRegistry.RUNNER.size() == 0 && TeamRegistry.HUNTER.size() == 0) {
                            sender.sendMessage(pluginMsg("玩家不足。"));
                            return true;
                        }
                    }
                    Game game = new Game();
                    game.start();
                    main.setGame(game);
                    sendSuccess(sender);
                } else {
                    sender.sendMessage(pluginMsg(ChatColor.RED + "游戏已在运行。"));
                }
                break;
            case "stop":
                if (main.isGamePresent()) {
                    main.getGame().stop();
                    main.setGame(null);
                    sendSuccess(sender);
                } else {
                    sender.sendMessage(pluginMsg(ChatColor.RED + "游戏未运行。"));
                }
                break;
            case "pause":
                if (main.isGamePresent()) {
                    if (!main.getGame().isPaused()) {
                        main.getGame().pause();
                        sendSuccess(sender);
                    } else {
                        sender.sendMessage(pluginMsg(ChatColor.RED + "游戏已经暂停。"));
                    }
                } else {
                    sender.sendMessage(pluginMsg(ChatColor.RED + "游戏未运行。"));
                }
                break;
            case "resume":
                if (main.isGamePresent()) {
                    if (main.getGame().isPaused()) {
                        sender.sendMessage(pluginMsg(ChatColor.RED + "游戏未暂停。"));
                    } else {
                        main.getGame().resume();
                        sendSuccess(sender);
                    }
                } else {
                    sender.sendMessage(pluginMsg(ChatColor.RED + "游戏未运行。"));
                }
                break;
            default:
                sender.sendMessage(pluginMsg("未知命令。"));
                return false;
        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        switch (args.length) {
            case 0:
            case 1:
                return Arrays.asList("start", "stop", "pause", "resume");
            case 2:
                if ("start".equals(args[0])) {
                    return Arrays.asList("true", "false");
                }
                break;
        }
        return null;
    }
}
