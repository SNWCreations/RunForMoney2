package snw.rfm.entity;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import snw.rfm.ConfigConstant;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;

public class CoinMap {
    private final Map<String, Double> map = new ConcurrentHashMap<>(); // key: player uuid, value: coin count
    private BiFunction<String, Double, Double> calcLogic;

    public CoinMap() {
        calcLogic = (k, v) -> v + ConfigConstant.COIN_PER_SECOND;
    }

    public void increaseAll() {
        TeamRegistry.RUNNER.toBukkitOfflinePlayerSet()
                .stream()
                .map(OfflinePlayer::getUniqueId)
                .map(UUID::toString)
                .forEach(i ->
                        {
                            map.computeIfAbsent(i, k -> 0.0);
                            map.computeIfPresent(i, calcLogic);
                        }
                );
    }

    public void reset() {
        map.clear();
    }

    public void calc(IngamePlayer player) {
        map.computeIfPresent(
                player.getBukkitPlayer().getUniqueId().toString(),
                (k, v) -> v * ConfigConstant.COIN_DELETION_MULTIPLIER
        );
    }

    public void setComputeLogic(BiFunction<String, Double, Double> logic) {
        calcLogic = logic;
    }

    public Map<OfflinePlayer, Double> toView() {
        Map<OfflinePlayer, Double> result = new HashMap<>();
        for (Map.Entry<String, Double> e : map.entrySet()) {
            OfflinePlayer key = Bukkit.getOfflinePlayer(UUID.fromString(e.getKey()));
            Double value = e.getValue();
            result.put(key, value);
        }
        return Collections.unmodifiableMap(result);
    }
}
