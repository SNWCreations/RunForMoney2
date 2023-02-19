package snw.rfm.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import snw.rfm.Main;
import snw.rfm.entity.IngamePlayer;
import snw.rfm.entity.Team;
import snw.rfm.entity.TeamRegistry;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static snw.rfm.util.Util.*;

public class RFMTeamCommand implements TabExecutor {
    private final Main main;

    public RFMTeamCommand(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage(pluginMsg(ChatColor.RED + "参数不足。"));
            return false;
        }
        switch (args[0]) {
            case "join":
                if (args.length == 1) {
                    sender.sendMessage(pluginMsg(ChatColor.RED + "参数不足。"));
                    return false;
                } else {
                    String teamName = args[1];
                    Team team = TeamRegistry.getTeamByName(teamName);
                    if (isInvisibleTeamName(teamName) || team == null) { // you can't join invisible team
                        sender.sendMessage(pluginMsg(ChatColor.RED + "队伍不存在。"));
                    } else {
                        if (args.length > 2) {
                            if (sender.isOp()) {
                                batch(sender, args, 2, team::add);
                            } else {
                                sender.sendMessage(pluginMsg(ChatColor.RED + "无权操作。"));
                            }
                        } else if (isPlayer(sender)) {
                            if (main.isGamePresent()) {
                                sender.sendMessage(pluginMsg(ChatColor.RED + "你不可以在游戏过程中加入队伍。"));
                            } else {
                                team.add((Player) sender);
                                sendSuccess(sender);
                            }
                        }
                    }
                }
                break;
            case "leave":
                if (isPlayer(sender)) {
                    if (main.isGamePresent()) {
                        sender.sendMessage(pluginMsg(ChatColor.RED + "你不可以在游戏过程中退出队伍。"));
                    } else {
                        Team team = IngamePlayer.getWrappedPlayer(((Player) sender)).getTeam();
                        if (team != null) {
                            team.remove(IngamePlayer.getWrappedPlayer(((Player) sender)));
                            sendSuccess(sender);
                        } else {
                            sender.sendMessage(pluginMsg(ChatColor.RED + "你并不在某个队伍中。"));
                        }
                    }
                }
                break;
            default:
                sender.sendMessage(pluginMsg(ChatColor.RED + "未知命令。"));
                return false;
        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        switch (args.length) {
            case 0:
                return Arrays.asList("join", "leave");
            case 1:
                return filterTab(args[0], Arrays.asList("join", "leave"));
            case 2:
                if ("join".equals(args[0])) {
                    return TeamRegistry.getAllTeams().keySet()
                            .stream()
                            .filter(i -> !isInvisibleTeamName(i)) // you can't join invisible teams (e.g. out)
                            .collect(Collectors.toList());
                }
        }
        if (args.length > 2) {
            if (sender.isOp()) {
                if ("join".equals(args[0])) {
                    return filterTab(args[args.length - 1], getAllPlayersName());
                }
            }
        }
        return Collections.emptyList();
    }

}
