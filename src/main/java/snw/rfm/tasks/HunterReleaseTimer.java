package snw.rfm.tasks;

import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import snw.rfm.Main;
import snw.rfm.api.events.HunterReleasedEvent;
import snw.rfm.entity.Game;
import snw.rfm.entity.TeamRegistry;

import java.util.Collection;

import static snw.rfm.util.Util.fireEvent;

public final class HunterReleaseTimer extends BukkitRunnable {
    private final Main main;
    private final Game game;
    private int secs;

    public HunterReleaseTimer(Main main, Game game, int time) {
        this.main = main;
        this.game = game;
        this.secs = time;
    }

    @Override
    public void run() {
        if (main.getGame() != this.game) {
            cancel();
            return;
        }
        if (--secs > 0) {
            onNewSecond();
        } else {
            onZero();
            cancel();
        }
    }

    private void onZero() {
        new SendingActionBarMessage(
                new TextComponent(ChatColor.DARK_RED + "" + ChatColor.BOLD + "猎人已放出")
        ).start(main);
        fireEvent(new HunterReleasedEvent(game));
    }

    private void onNewSecond() {
        ChatColor color = null;
        if (secs == 30) {
            color = ChatColor.GREEN;
        } else if (secs == 15) {
            color = ChatColor.YELLOW;
        } else if (secs <= 10) {
            color = ChatColor.DARK_RED;
        }
        if (color != null) {
            new SendingActionBarMessage(
                    new TextComponent(
                            String.format("%s%s猎人将在 %s%s %s%s%s秒后放出", ChatColor.RED, ChatColor.BOLD, color, secs, ChatColor.RESET, ChatColor.RED, ChatColor.BOLD)
                    ),
                    TeamRegistry.RUNNER.toBukkitPlayerSet()
            ).start(main);
        }
    }

    public void start() {
        Collection<? extends Player> players = Bukkit.getOnlinePlayers();
        for (Player player : players) {
            player.sendTitle(
                    ChatColor.DARK_RED + "" + ChatColor.BOLD + "全员逃走中",
                    String.format("%s猎人将在 %s 秒之后放出", ChatColor.RED, secs),
                    10, 70, 20
            );
        }
        runTaskTimer(main, 20L, 20L);
    }

}
