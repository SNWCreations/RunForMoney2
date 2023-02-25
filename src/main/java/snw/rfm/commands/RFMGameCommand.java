package snw.rfm.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import snw.rfm.ConfigConstant;
import snw.rfm.Main;
import snw.rfm.entity.Game;
import snw.rfm.entity.TeamRegistry;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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
                            sender.sendMessage(pluginMsg(ChatColor.RED + "玩家不足。"));
                            return true;
                        }
                    }
                    Game game = new Game(main);
                    if (args.length == 3) {
                        int realReleaseTime;
                        try {
                            realReleaseTime = Integer.parseInt(args[2]);
                        } catch (NumberFormatException e) {
                            sender.sendMessage(pluginMsg(ChatColor.RED + "无效参数。期望一个数字。"));
                            return false;
                        }
                        ConfigConstant.init(main, false);
                        int bak = ConfigConstant.HUNTER_RELEASE_TIME;
                        ConfigConstant.HUNTER_RELEASE_TIME = realReleaseTime;
                        game.start();
                        ConfigConstant.HUNTER_RELEASE_TIME = bak; // switch back
                    } else {
                        ConfigConstant.init(main, false);
                        game.start();
                    }
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
                        main.getGame().resume();
                        sendSuccess(sender);
                    } else {
                        sender.sendMessage(pluginMsg(ChatColor.RED + "游戏未暂停。"));
                    }
                } else {
                    sender.sendMessage(pluginMsg(ChatColor.RED + "游戏未运行。"));
                }
                break;
            case "control":
                if (args.length == 2) {
                    if ("endroom".equals(args[1])) {
                        if (isPlayer(sender)) {
                            if (sender.isOp()) {
                                ConfigConstant.END_ROOM_LOCATION = ((Player) sender).getLocation();
                                sendSuccess(sender);
                            } else {
                                sender.sendMessage(pluginMsg(ChatColor.RED + "无权操作。"));
                            }
                        }
                        return true;
                    }
                }
                if (main.isGamePresent()) {
                    if (args.length >= 2) {
                        switch (args[1]) {
                            case "money":
                                if (args.length < 3) {
                                    sender.sendMessage(pluginMsg(ChatColor.RED + "参数数量错误。"));
                                    return false;
                                }
                                switch (args[2]) {
                                    case "reset":
                                        main.getGame().getController().clearCoin();
                                        sendSuccess(sender);
                                        break;
                                    case "add":
                                        if (args.length == 5) {
                                            Player player = Bukkit.getPlayer(args[3]);
                                            if (player == null) {
                                                sender.sendMessage(pluginMsg(ChatColor.RED + "玩家不在线。"));
                                            } else {
                                                int a = Integer.parseInt(args[4]);
                                                main.getGame().getController().addMoney(player, a);
                                            }
                                        } else {
                                            sender.sendMessage(pluginMsg(ChatColor.RED + "参数不足。"));
                                            return false;
                                        }
                                        break;
                                    case "set":
                                        if (args.length == 5) {
                                            Player player = Bukkit.getPlayer(args[3]);
                                            if (player == null) {
                                                sender.sendMessage(pluginMsg(ChatColor.RED + "玩家不在线。"));
                                            } else {
                                                int b = Integer.parseInt(args[4]);
                                                main.getGame().getController().setMoney(player, b);
                                            }
                                        } else {
                                            sender.sendMessage(pluginMsg(ChatColor.RED + "参数数量错误。"));
                                            return false;
                                        }
                                        break;
                                    case "get":
                                        if (args.length == 4) {
                                            Player player = Bukkit.getPlayer(args[3]);
                                            if (player == null) {
                                                sender.sendMessage(pluginMsg(ChatColor.RED + "玩家不在线。"));
                                            } else {
                                                sender.sendMessage(pluginMsg(buildPlayerName(args[3], ChatColor.GREEN) + " 现有 " + main.getGame().getController().getMoney(player) + " 硬币"));
                                            }
                                        } else {
                                            Map<OfflinePlayer, Double> view = main.getGame().getCoinMap().toView();
                                            sender.sendMessage(pluginMsg(ChatColor.BOLD + "=== 硬币榜 ==="));
                                            for (Map.Entry<OfflinePlayer, Double> entry : view.entrySet()) {
                                                sender.sendMessage(pluginMsg(
                                                        buildPlayerName(entry.getKey().getName()) + " - " + entry.getValue()
                                                ));
                                            }
                                        }
                                        break;
                                }
                                break;
                            case "reverse":
                                main.getGame().getController().timeReverse();
                                sendSuccess(sender);
                                break;
                            case "forceout":
                                if (args.length == 3) {
                                    Player player = Bukkit.getPlayer(args[2]);
                                    if (player == null) {
                                        sender.sendMessage(pluginMsg(ChatColor.RED + "玩家不在线。"));
                                    } else {
                                        main.getGame().getController().forceOut(player);
                                        sendSuccess(sender);
                                    }
                                } else if (args.length > 3) {
                                    if (sender.isOp()) {
                                        batch(sender, args, 2, p -> main.getGame().getController().forceOut(p));
                                    } else {
                                        sender.sendMessage(pluginMsg(ChatColor.RED + "无权操作。"));
                                    }
                                } else {
                                    sender.sendMessage(pluginMsg(ChatColor.RED + "参数数量错误。"));
                                    return false;
                                }
                                break;
                            case "respawn":
                                if (args.length == 3) {
                                    Player player = Bukkit.getPlayer(args[2]);
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
                                } else if (args.length > 3) {
                                    if (sender.isOp()) {
                                        batch(sender, args, 2, p -> main.getGame().getController().respawn(p));
                                    } else {
                                        sender.sendMessage(pluginMsg(ChatColor.RED + "无权操作。"));
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
                return Arrays.asList("start", "stop", "pause", "resume", "control");
            case 1:
                return filterTab(args[0], Arrays.asList("start", "stop", "pause", "resume", "control"));
            case 2:
                switch (args[0]) {
                    case "start":
                        return filterTab(args[1], Arrays.asList("true", "false"));
                    case "control":
                        return filterTab(args[1], Arrays.asList("money", "reverse", "forceout", "respawn", "endroom"));
                }
                break;
            case 3:
                if ("control".equals(args[0])) {
                    switch (args[1]) {
                        case "money":
                            return filterTab(args[2], Arrays.asList("add", "set", "reset", "get"));
                        case "forceout":
                            return filterTab(args[2], TeamRegistry.RUNNER.toNameSet());
                        case "respawn":
                            return filterTab(args[2], TeamRegistry.OUT.toNameSet());
                    }
                }
                break;
            case 4:
                if ("control".equals(args[0])) {
                    if (args[1].equals("money")) {
                        switch (args[2]) {
                            case "add":
                            case "set":
                            case "get":
                                return filterTab(args[3], getAllPlayersName());
                        }
                    }
                }
                break;
        }
        if (args.length > 3) {
            if ("control".equals(args[0])) {
                switch (args[1]) {
                    case "forceout":
                        return filterTab(args[args.length - 1], TeamRegistry.RUNNER.toNameSet());
                    case "respawn":
                        return filterTab(args[args.length - 1], TeamRegistry.OUT.toNameSet());
                }
            }
        }
        return Collections.emptyList();
    }
}
