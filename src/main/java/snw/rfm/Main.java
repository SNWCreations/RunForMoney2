package snw.rfm;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import snw.rfm.commands.*;
import snw.rfm.entity.Game;
import snw.rfm.entity.TeamRegistry;
import snw.rfm.item.*;
import snw.rfm.listeners.PlayerJoinListener;
import snw.rfm.util.NickSupport;

import java.util.Objects;

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
        new PlayerJoinListener(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        terminateGame();
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

    public void terminateGame() {
        if (game != null) {
            game.stop();
            setGame(null);
        }
    }

    private void initData() {
        TeamRegistry.init();
        ConfigConstant.init(this, true);
        NickSupport.init(this);
        SlowItemCommand.init(this);
    }

    private void registerCommands() {
        registerCommand("rfmgame", new RFMGameCommand(this));
        registerCommand("rfmteam", new RFMTeamCommand(this));
        registerCommand("rfmitem", new RFMItemCommand());
        registerCommand("rfmtimer", new RFMTimerCommand(this));
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
        new ExitPickaxe(this);
    }
}
