package snw.rfm;

import org.bukkit.plugin.java.JavaPlugin;

import snw.rfm.entity.TeamRegistry;

public final class Main extends JavaPlugin {
    private static Main INSTANCE;

    @Override
    public void onEnable() {
        // Plugin startup logic
        INSTANCE = this;
        TeamRegistry.init();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Main getInstance() {
        return INSTANCE;
    }
}
