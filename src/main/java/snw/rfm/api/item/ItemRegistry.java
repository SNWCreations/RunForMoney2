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
        map.put(key, toOneAmount(value));
    }

    public static void add(String key, ItemStack value, RightClickCallback callback) {
        add(key, value);
        callbackMap.put(toOneAmount(value), callback);
    }

    public static ItemStack get(String key) {
        ItemStack itemStack = map.get(key);
        return itemStack != null ? toOneAmount(itemStack.clone()) : null;
    }

    public static RightClickCallback getCallback(ItemStack key) {
        return callbackMap.get(toOneAmount(key));
    }

    public static Set<String> keySet() {
        return Collections.unmodifiableSet(map.keySet());
    }

    public static Map<String, ItemStack> getView() {
        return Collections.unmodifiableMap(map);
    }

    private static ItemStack toOneAmount(ItemStack stack) {
        ItemStack clone = stack.clone();
        clone.setAmount(1);
        return clone;
    }
}
