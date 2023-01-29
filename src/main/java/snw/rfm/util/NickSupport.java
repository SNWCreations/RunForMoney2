package snw.rfm.util;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import snw.rfm.Main;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public final class NickSupport {
    private static final Map<String, String> nickMap = new HashMap<>();

    private NickSupport() {}

    public static void init(Main main) {
        YamlConfiguration conf = YamlConfiguration.loadConfiguration(new File(main.getDataFolder(), "nickname.yml"));
        for (Map.Entry<String, Object> nameEntry : conf.getValues(false).entrySet()) {
            if (nameEntry.getValue() instanceof String) {
                nickMap.put(nameEntry.getKey(), (String) nameEntry.getValue());
            } else {
                main.getLogger().warning("Cannot set " + nameEntry.getKey() + "'s nickname, because the value is not a String.");
            }
        }
    }

    public static String getNickName(String playerName) {
        return nickMap.containsKey(playerName) ? ChatColor.translateAlternateColorCodes('&', nickMap.get(playerName)) : playerName;
    }
}
