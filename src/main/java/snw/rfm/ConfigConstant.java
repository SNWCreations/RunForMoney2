package snw.rfm;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.Nullable;

import java.util.logging.Logger;

public class ConfigConstant {
    public static int GAME_TIME; // in minutes, so use GAME_TIME * 60 if you want the time in seconds
    public static double COIN_DELETION_MULTIPLIER;
    public static int COIN_PER_SECOND;
    public static int HUNTER_RELEASE_TIME;
    public static @Nullable Location END_ROOM_LOCATION = null;

    public static void init() {
        Logger logger = Main.getInstance().getLogger();
        FileConfiguration config = Main.getInstance().getConfig();

        logger.info("加载配置...");

        COIN_DELETION_MULTIPLIER = config.getDouble("coin_multiplier_on_be_caught", 0.1);
        COIN_PER_SECOND = config.getInt("coin_per_second", 100);
        GAME_TIME = config.getInt("game_time", 30);
        HUNTER_RELEASE_TIME = config.getInt("hunter_release_time", 60);

        // region end room
        String rawERL = config.getString("end_room", "");
        String[] arr = rawERL.split(" ");
        if (arr.length == 3) {
            World gameWorld = BukkitHandle.getWorld(config.getString("game_world", "world"));
            try {
                END_ROOM_LOCATION = new Location(gameWorld, Integer.parseInt(arr[0]), Integer.parseInt(arr[1]), Integer.parseInt(arr[2]));
            } catch (NumberFormatException e) {
                logger.warning("无法加载终止间位置: 参数无效。");
            }
        }

        logger.info("当逃走队员被捕时，当时拥有的硬币将乘以: " + COIN_DELETION_MULTIPLIER);
        logger.info("每秒增加的硬币量: " + COIN_PER_SECOND);
    }
}
