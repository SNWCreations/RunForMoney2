package snw.rfm.api.item;

import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class ItemRegistry {
    private static final Map<String, ItemStack> map = new ConcurrentHashMap<>();
    private static final Map<ItemStack, RightClickCallback> callbackMap = new ConcurrentHashMap<>();

    private ItemRegistry() {} // can't call constructor


    public static void add(String key, ItemStack value) {
        map.put(key, value.clone());
    }

    public static void add(String key, ItemStack value, RightClickCallback callback) {
        add(key, value);
        callbackMap.put(value.clone(), callback);
    }

    public static ItemStack get(String key) {
        ItemStack itemStack = map.get(key);
        return itemStack != null ? itemStack.clone() : null;
    }

    public static RightClickCallback getCallback(ItemStack key) {
        return callbackMap.get(key);
    }

    public static Set<String> keySet() {
        return Collections.unmodifiableSet(map.keySet());
    }

    public static Map<String, ItemStack> getView() {
        return Collections.unmodifiableMap(map);
    }
}
