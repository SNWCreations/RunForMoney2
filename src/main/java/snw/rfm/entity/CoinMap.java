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
    private final Map<UUID, Double> map = new ConcurrentHashMap<>();
    private BiFunction<UUID, Double, Double> calcLogic;
    private boolean reverse;

    public CoinMap() {
        calcLogic = (k, v) -> v + ConfigConstant.COIN_PER_SECOND;
    }

    public void increaseAll() {
        TeamRegistry.RUNNER.toBukkitOfflinePlayerSet()
                .stream()
                .map(OfflinePlayer::getUniqueId)
                .forEach(i ->
                        {
                            map.computeIfAbsent(i, k -> 0.0);
                            map.computeIfPresent(i, calcLogic);
                        }
                );
    }

    public void addAll(int times) {
        TeamRegistry.RUNNER.toBukkitOfflinePlayerSet()
                .stream()
                .map(OfflinePlayer::getUniqueId)
                .forEach(i ->
                        {
                            int a = times * ConfigConstant.COIN_PER_SECOND;
                            for (Map.Entry<UUID, Double> e : map.entrySet()) {
                                e.setValue(e.getValue() + a);
                            }
                        }
                );
    }

    public void reset() {
        map.clear();
    }

    public void calc(IngamePlayer player) {
        map.computeIfPresent(
                player.getBukkitPlayer().getUniqueId(),
                (k, v) -> v * ConfigConstant.COIN_DELETION_MULTIPLIER
        );
    }

    public void reverse() {
        reverse = !reverse;
        setComputeLogic(reverse ? (k, v) -> v - ConfigConstant.COIN_PER_SECOND : (k, v) -> v + ConfigConstant.COIN_PER_SECOND);
    }

    public boolean isReversed() {
        return reverse;
    }

    public void setComputeLogic(BiFunction<UUID, Double, Double> logic) {
        calcLogic = logic;
    }

    public Map<OfflinePlayer, Double> toView() {
        Map<OfflinePlayer, Double> result = new HashMap<>();
        for (Map.Entry<UUID, Double> e : map.entrySet()) {
            OfflinePlayer key = Bukkit.getOfflinePlayer(e.getKey());
            Double value = e.getValue();
            result.put(key, value);
        }
        return Collections.unmodifiableMap(result);
    }

    public double get(OfflinePlayer player) {
        return map.computeIfAbsent(player.getUniqueId(), i -> 0D);
    }

    public void set(OfflinePlayer player, double amount) {
        map.put(player.getUniqueId(), amount);
    }

    public void add(OfflinePlayer player, double amount) {
        set(player, map.get(player.getUniqueId()) + amount);
    }
}
