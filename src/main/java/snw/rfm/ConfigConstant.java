package snw.rfm;

import org.bukkit.Bukkit;
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
    public static int TELEPORT_AFTER_CAUGHT;
    public static boolean DROP_ITEM_AFTER_CAUGHT;
    public static @Nullable Location END_ROOM_LOCATION = null;

    // Item Configuration Constants
    public static int HUNTER_PAUSE_TIME;
    public static int IGNORE_TIME;
    public static int FREEZE_TIME;
    public static int FREEZE_RADIUS;
    public static boolean STOP_GAME_ON_NO_PLAYER;
    public static boolean NO_TIMER;

    public static void init(Main main, boolean log) {
        Logger logger = main.getLogger();
        FileConfiguration config = main.getConfig();

        if (log) {
            logger.info("加载配置...");
        }

        COIN_DELETION_MULTIPLIER = config.getDouble("coin_multiplier_on_be_caught", 0.1);
        COIN_PER_SECOND = config.getInt("coin_per_second", 100);
        GAME_TIME = config.getInt("game_time", 30);
        HUNTER_RELEASE_TIME = config.getInt("hunter_release_time", 60);
        TELEPORT_AFTER_CAUGHT = config.getInt("teleport_after_caught", 0);
        DROP_ITEM_AFTER_CAUGHT = config.getBoolean("drop_item_after_caught", false);

        HUNTER_PAUSE_TIME = config.getInt("hpc_time", 3);
        IGNORE_TIME = config.getInt("ignore_time", 5);
        FREEZE_TIME = config.getInt("freeze_time", 15);
        FREEZE_RADIUS = config.getInt("freeze_radius", 10);
        STOP_GAME_ON_NO_PLAYER = config.getBoolean("stop_game_on_no_player", true);
        NO_TIMER = config.getBoolean("no_timer", false);

        // region end room
        String rawERL = config.getString("end_room", "");
        String[] arr = rawERL.split(" ");
        if (arr.length == 3) {
            World gameWorld = Bukkit.getWorld(config.getString("game_world", "world"));
            try {
                END_ROOM_LOCATION = new Location(gameWorld, Integer.parseInt(arr[0]), Integer.parseInt(arr[1]), Integer.parseInt(arr[2]));
            } catch (NumberFormatException e) {
                if (log) {
                    logger.warning("无法加载终止间位置: 参数无效。");
                }
            }
        }

        if (log) {
            logger.info("终止间位置: " + END_ROOM_LOCATION);
            logger.info("当逃走队员被捕时，当时拥有的硬币将乘以: " + COIN_DELETION_MULTIPLIER);
            logger.info("每秒增加的硬币量: " + COIN_PER_SECOND);
            logger.info("游戏时长(单位: 分钟): " + GAME_TIME);
            logger.info("猎人放出前的时间(单位: 秒): " + HUNTER_RELEASE_TIME);
            logger.info("在被捕后传送到终止间之前的时间(单位: 秒): " + TELEPORT_AFTER_CAUGHT);
            logger.info("在被捕后是否掉落拥有的物品: " + cnBool(DROP_ITEM_AFTER_CAUGHT));

            logger.info("猎人暂停卡有效时长(单位: 秒): " + HUNTER_PAUSE_TIME);
            logger.info("抗性卡有效时长(单位: 秒): " + IGNORE_TIME);
            logger.info("冷冻卡有效时长(单位: 秒): " + FREEZE_TIME);
            logger.info("冷冻卡有效半径(单位: 格): " + FREEZE_RADIUS);
            logger.info("当没有逃走队员时，插件是否结束游戏: " + cnBool(STOP_GAME_ON_NO_PLAYER));
            logger.info("是否禁用计时器: " + cnBool(NO_TIMER));
        }
    }

    private static String cnBool(boolean bool) {
        return bool ? "是" : "否";
    }
}
