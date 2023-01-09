package snw.rfm.entity;

import snw.rfm.Main;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class TeamRegistry {
    public static Team HUNTER;
    public static Team RUNNER;
    public static Team OUT;
    private static final Map<String, Team> external = new HashMap<>();

    public static void init() {
        org.bukkit.scoreboard.Team hunterTeam = refreshTeam("rfm_hunter");
        hunterTeam.setAllowFriendlyFire(false);
        HUNTER = new Team(hunterTeam);

        org.bukkit.scoreboard.Team runnerTeam = refreshTeam("rfm_runner");
        runnerTeam.setAllowFriendlyFire(false);
        RUNNER = new Team(runnerTeam);

        org.bukkit.scoreboard.Team outTeam = refreshTeam("rfm_runner");
        runnerTeam.setAllowFriendlyFire(false);
        OUT = new Team(outTeam);
    }

    public static Team getExternalTeam(String name) {
        return external.get(name);
    }

    public static void registerExternalTeam(String name, Team teamObj) {
        external.put(name, teamObj);
    }

    public static void unregisterExternalTeam(String name) {
        external.remove(name);
    }

    private static org.bukkit.scoreboard.Team refreshTeam(String name) {
        Optional.ofNullable(
                Main.getInstance().getServer().getScoreboardManager().getMainScoreboard().getTeam(name)
        ).ifPresent(org.bukkit.scoreboard.Team::unregister);
        return Main.getInstance().getServer().getScoreboardManager().getMainScoreboard().registerNewTeam(name);
    }
}
