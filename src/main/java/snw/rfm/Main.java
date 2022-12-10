package snw.rfm;

import org.bukkit.plugin.java.JavaPlugin;

import snw.rfm.entity.GameManager;

public final class Main extends JavaPlugin {
    private static Main INSTANCE;
    private GameManager gameManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        INSTANCE = this;
        gameManager = new GameManager();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        gameManager.getGames().forEach(i -> i.stop());
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public static Main getInstance() {
        return INSTANCE;
    }
}
