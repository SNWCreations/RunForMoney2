package snw.rfm;

import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.java.JavaPlugin;
import snw.rfm.commands.RFMGameCommand;
import snw.rfm.commands.RFMTeamCommand;
import snw.rfm.entity.Game;
import snw.rfm.entity.TeamRegistry;

import java.util.Optional;

public final class Main extends JavaPlugin {
    private static Main INSTANCE;
    private Game game;

    @Override
    public void onLoad() {
        saveDefaultConfig();
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        INSTANCE = this;
        TeamRegistry.init();
        ConfigConstant.init();
        registerCommand("rfmgame", new RFMGameCommand(this));
        registerCommand("rfmteam", new RFMTeamCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Optional.ofNullable(game).ifPresent(Game::stop);
        TeamRegistry.cleanup();
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

    public static Main getInstance() {
        return INSTANCE;
    }

    private void registerCommand(String name, TabExecutor executor) {
        PluginCommand command = getCommand(name);
        command.setExecutor(executor);
        command.setTabCompleter(executor);
    }
}
