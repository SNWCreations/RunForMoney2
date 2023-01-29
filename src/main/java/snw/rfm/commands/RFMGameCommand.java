package snw.rfm.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import snw.rfm.Main;
import snw.rfm.entity.Game;
import snw.rfm.entity.TeamRegistry;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static snw.rfm.util.Util.*;

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
                    Game game = new Game(main);
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
            case "control":
                if (main.isGamePresent()) {
                    if (args.length >= 2) {
                        switch (args[1]) {
                            case "money":
                                switch (args[2]) {
                                    case "reset":
                                        main.getGame().getCoinMap().reset();
                                        sendSuccess(sender);
                                        break;
                                    case "add":
                                        if (args.length >= 4) {
                                            Player player = Bukkit.getPlayer(args[3]);
                                            if (player == null) {
                                                sender.sendMessage(pluginMsg(ChatColor.RED + "玩家不在线。"));
                                            } else {
                                                int a = Integer.parseInt(args[3]);
                                                main.getGame().getController().addMoney(player, a);
                                            }
                                        } else {
                                            sender.sendMessage(pluginMsg(ChatColor.RED + "参数不足。"));
                                            return false;
                                        }
                                        break;
                                    case "set":
                                        if (args.length == 4) {
                                            Player player = Bukkit.getPlayer(args[3]);
                                            if (player == null) {
                                                sender.sendMessage(pluginMsg(ChatColor.RED + "玩家不在线。"));
                                            } else {
                                                int b = Integer.parseInt(args[3]);
                                                main.getGame().getController().setMoney(player, b);
                                            }
                                        } else {
                                            sender.sendMessage(pluginMsg(ChatColor.RED + "参数数量错误。"));
                                            return false;
                                        }
                                        break;
                                }
                                break;
                            case "reverse":
                                main.getGame().getController().setTimeReversed(!main.getGame().getController().isTimeReversed());
                                sendSuccess(sender);
                                break;
                            case "forceout":
                                if (args.length == 2) {
                                    Player player = Bukkit.getPlayer(args[1]);
                                    if (player == null) {
                                        sender.sendMessage(pluginMsg(ChatColor.RED + "玩家不在线。"));
                                    } else {
                                        main.getGame().getController().forceOut(player);
                                        sendSuccess(sender);
                                    }
                                } else {
                                    sender.sendMessage(pluginMsg(ChatColor.RED + "参数数量错误。"));
                                    return false;
                                }
                                break;
                            case "respawn":
                                if (args.length == 2) {
                                    Player player = Bukkit.getPlayer(args[1]);
                                    if (player == null) {
                                        sender.sendMessage(pluginMsg(ChatColor.RED + "玩家不在线。"));
                                    } else {
                                        boolean success = main.getGame().getController().respawn(player);
                                        if (success) {
                                            sendSuccess(sender);
                                        } else {
                                            sender.sendMessage(ChatColor.YELLOW + "没有发生变化。此玩家现在仍在游戏中。");
                                        }
                                    }
                                } else {
                                    sender.sendMessage(pluginMsg(ChatColor.RED + "参数数量错误。"));
                                    return false;
                                }
                                break;
                            default:
                                sender.sendMessage(pluginMsg("未知命令。"));
                                return false;
                        }
                    } else {
                        sender.sendMessage(pluginMsg(ChatColor.RED + "参数不足。"));
                        return false;
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
                return filterTab(args[0], Arrays.asList("start", "stop", "pause", "resume", "control"));
            case 2:
                switch (args[0]) {
                    case "start":
                        return filterTab(args[1], Arrays.asList("true", "false"));
                    case "control":
                        return filterTab(args[1], Arrays.asList("money", "reverse", "forceout", "respawn"));
                }
                break;
            case 3:
                if ("control".equals(args[0])) {
                    switch (args[1]) {
                        case "money":
                            return filterTab(args[2], Arrays.asList("add", "set", "reset"));
                        case "forceout":
                            return filterTab(args[2], TeamRegistry.RUNNER.toNameSet());
                        case "respawn":
                            return filterTab(args[2], TeamRegistry.OUT.toNameSet());
                    }
                }
            case 4:
                if ("control".equals(args[0])) {
                    switch (args[1]) {
                        case "money":
                            switch (args[2]) {
                                case "add":
                                case "set":
                                    return filterTab(args[3], getAllPlayersName());
                            }
                    }
                }

        }
        return Collections.emptyList();
    }
}
