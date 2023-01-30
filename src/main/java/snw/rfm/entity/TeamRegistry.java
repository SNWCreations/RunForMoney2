package snw.rfm.entity;

import org.bukkit.Bukkit;

import java.util.*;

public class TeamRegistry {
    public static Team HUNTER;
    public static Team RUNNER;
    public static Team OUT;
    private static final Map<String, Team> allTeams = new HashMap<>();

    public static void init() {
        org.bukkit.scoreboard.Team hunterTeam = refreshTeam("rfm_hunter");
        hunterTeam.setAllowFriendlyFire(false);
        HUNTER = new Team("猎人队伍", hunterTeam);

        org.bukkit.scoreboard.Team runnerTeam = refreshTeam("rfm_runner");
        runnerTeam.setAllowFriendlyFire(false);
        RUNNER = new Team("逃走队员", runnerTeam);

        org.bukkit.scoreboard.Team outTeam = refreshTeam("rfm_out");
        runnerTeam.setAllowFriendlyFire(false);
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
        allTeams.put(name, teamObj);
    }

    public static void unregisterTeam(String name) {
        Optional.ofNullable(allTeams.remove(name)).ifPresent(i -> i.getBukkitTeam().unregister());
    }

    private static org.bukkit.scoreboard.Team refreshTeam(String name) {
        Optional.ofNullable(
                Objects.requireNonNull(Bukkit.getScoreboardManager()).getMainScoreboard().getTeam(name) // impossible to fail
        ).ifPresent(org.bukkit.scoreboard.Team::unregister);
        return Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam(name);
    }

    public static void cleanup() {
        new HashSet<>(allTeams.keySet()).forEach(TeamRegistry::unregisterTeam);
    }
}
