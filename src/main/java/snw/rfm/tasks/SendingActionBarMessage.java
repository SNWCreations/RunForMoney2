package snw.rfm.tasks;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import snw.rfm.Main;

import java.util.Collection;

public final class SendingActionBarMessage extends BukkitRunnable {
    private final TextComponent text;
    private final Collection<? extends Player> playersToSend;
    private int ticked = 0;
    private final int ticks;

    public SendingActionBarMessage(@NotNull TextComponent textToSend) {
        this(textToSend, Bukkit.getOnlinePlayers());
    }

    public SendingActionBarMessage(@NotNull TextComponent textToSend, int ticks) {
        this(textToSend, Bukkit.getOnlinePlayers(), ticks);
    }

    public SendingActionBarMessage(@NotNull TextComponent textToSend, @NotNull Collection<? extends Player> playersToSend) {
        this(textToSend, playersToSend, 20);
    }

    public SendingActionBarMessage(@NotNull TextComponent textToSend, @NotNull Collection<? extends Player> playersToSend, int ticks) {

        Validate.notNull(textToSend, "No text to send :(");
        Validate.notNull(playersToSend, "No players can receive this message :(");
        Validate.isTrue(ticks > 0, "The ticks cannot be negative or zero.");
        this.text = textToSend;
        this.playersToSend = playersToSend;
        this.ticks = ticks;
    }

    @Override
    public void run() {
        playersToSend.forEach((IT) -> IT.spigot().sendMessage(ChatMessageType.ACTION_BAR, text));
        if (ticked++ >= ticks) {
            cancel();
        }
    }

    public void start(Main main) {
        super.runTaskTimer(main, 0L, 1L);
    }
}