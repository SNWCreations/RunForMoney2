package snw.rfm;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import snw.rfm.commands.RFMGameCommand;
import snw.rfm.commands.RFMItemCommand;
import snw.rfm.commands.RFMTeamCommand;
import snw.rfm.commands.SlowItemCommand;
import snw.rfm.entity.Game;
import snw.rfm.entity.TeamRegistry;
import snw.rfm.item.FreezeCard;
import snw.rfm.item.IgnoreCard;
import snw.rfm.item.PauseCard;
import snw.rfm.item.RespawnCard;
import snw.rfm.util.NickSupport;

import java.util.Objects;
import java.util.Optional;

public final class Main extends JavaPlugin {
    private Game game;

    @Override
    public void onLoad() {
        saveDefaultConfig();
        saveResource("nickname.yml", false);
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        initData();
        registerCommands();
        registerInternalItems();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Optional.ofNullable(game).ifPresent(Game::stop);
        TeamRegistry.cleanup();
        getServer().getScheduler().cancelTasks(this);
        HandlerList.unregisterAll(this);
    }

    public Game getGame() {
        return game;
    }

    public boolean isGamePresent() {
        return game != null;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    private void initData() {
        TeamRegistry.init();
        ConfigConstant.init(this, true);
        NickSupport.init(this);
    }

    private void registerCommands() {
        registerCommand("rfmgame", new RFMGameCommand(this));
        registerCommand("rfmteam", new RFMTeamCommand(this));
        registerCommand("rfmitem", new RFMItemCommand());
        registerCommand("slowitem", new SlowItemCommand());
    }

    private void registerCommand(String name, CommandExecutor executor) {
        PluginCommand command = Objects.requireNonNull(getCommand(name), "Command " + name + " not found!");
        command.setExecutor(executor);
        if (executor instanceof TabCompleter) {
            command.setTabCompleter(((TabCompleter) executor));
        }
    }

    private void registerInternalItems() {
        new PauseCard(this);
        new RespawnCard(this);
        new IgnoreCard(this);
        new FreezeCard(this);
    }
}
