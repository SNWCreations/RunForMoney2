package snw.rfm.entity;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.*;

public class TeamRegistry {
    public static Team HUNTER;
    public static Team RUNNER;
    public static Team OUT;
    private static final Map<String, Team> allTeams = new HashMap<>();

    public static void init() {
        org.bukkit.scoreboard.Team hunterTeam = refreshTeam("rfm_hunter");
        hunterTeam.setColor(ChatColor.RED);
        HUNTER = new Team("猎人", hunterTeam);

        org.bukkit.scoreboard.Team runnerTeam = refreshTeam("rfm_runner");
        runnerTeam.setColor(ChatColor.AQUA);
        RUNNER = new Team("逃走队员", runnerTeam);

        org.bukkit.scoreboard.Team outTeam = refreshTeam("rfm_out");
        outTeam.setColor(ChatColor.GRAY);
        OUT = new Team("已淘汰", outTeam);

        registerTeam("hunter", HUNTER);
        registerTeam("runner", RUNNER);
        registerTeam("out", OUT);
    }

    public static Map<String, Team> getAllTeams() {
        return Collections.unmodifiableMap(allTeams);
    }

    public static Team getTeamByName(String name) {
        return allTeams.get(name);
    }

    public static void registerTeam(String name, Team teamObj) {
        Validate.isTrue(!allTeams.containsKey(name), "The team named " + name + " has already registered");
        allTeams.put(name, teamObj);
    }

    public static void unregisterTeam(String name) {
        Optional.ofNullable(allTeams.remove(name)).ifPresent(i -> i.getBukkitTeam().unregister());
    }

    private static org.bukkit.scoreboard.Team refreshTeam(String name) {
        Optional.ofNullable(
                Objects.requireNonNull(Bukkit.getScoreboardManager()).getMainScoreboard().getTeam(name) // impossible to fail
        ).ifPresent(org.bukkit.scoreboard.Team::unregister);
        final org.bukkit.scoreboard.Team newTeam = Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam(name);
        newTeam.setAllowFriendlyFire(false);
        newTeam.setOption(org.bukkit.scoreboard.Team.Option.NAME_TAG_VISIBILITY, org.bukkit.scoreboard.Team.OptionStatus.NEVER);
        return newTeam;
    }

    public static void cleanup() {
        new HashSet<>(allTeams.keySet()).forEach(TeamRegistry::unregisterTeam);
    }
}
