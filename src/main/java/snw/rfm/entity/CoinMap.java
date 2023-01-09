package snw.rfm.entity;

import org.bukkit.entity.Player;
import snw.rfm.ConfigConstant;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CoinMap {
    private final Map<String, Double> map = new ConcurrentHashMap<>(); // key: player uuid, value: coin count

    public void increaseAll() {
        TeamRegistry.RUNNER.toBukkitPlayerList()
                .stream()
                .map(Player::getUniqueId)
                .map(UUID::toString)
                .forEach(i -> map.computeIfAbsent(i, k -> 0.0));
        for (Map.Entry<String, Double> entry : map.entrySet()) {
            entry.setValue(entry.getValue() + ConfigConstant.COIN_PER_SECOND);
        }
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
}
