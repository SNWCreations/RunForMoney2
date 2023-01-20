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
import java.util.List;
import java.util.stream.Collectors;

import static snw.rfm.util.Util.*;

public class RFMTeamCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage(pluginMsg(ChatColor.RED + "参数不足。"));
            return false;
        }
        switch (args[0]) {
            case "join":
                if (isPlayer(sender)) {
                    if (args.length == 1) {
                        sender.sendMessage(pluginMsg(ChatColor.RED + "参数不足。"));
                        return false;
                    }
                    String teamName = args[1];
                    Team team = TeamRegistry.getTeamByName(teamName);
                    if (isInvisibleTeamName(teamName) || team == null) { // you can't join invisible team
                        sender.sendMessage(pluginMsg(ChatColor.RED + "队伍不存在。"));
                    } else {
                        team.add(IngamePlayer.getWrappedPlayer(((Player) sender)));
                        sendSuccess(sender);
                    }
                }
                break;
            case "leave":
                if (isPlayer(sender)) {
                    if (Main.getInstance().isGamePresent()) {
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
            case 1:
                return Arrays.asList("join", "leave");
            case 2:
                if ("join".equals(args[0])) {
                    return TeamRegistry.getAllTeams().keySet()
                            .stream()
                            .filter(i -> !isInvisibleTeamName(i)) // you can't join invisible teams (e.g. out)
                            .collect(Collectors.toList());
                }
        }
        return null;
    }

    private static boolean isPlayer(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(pluginMsg(ChatColor.RED + "需要玩家执行此分支。"));
            return false;
        }
        return true;
    }
}
