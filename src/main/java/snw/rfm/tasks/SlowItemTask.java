package snw.rfm.tasks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import snw.rfm.Main;
import snw.rfm.api.events.GameStopEvent;
import snw.rfm.commands.SlowItemCommand;
import snw.rfm.entity.Game;

public class SlowItemTask extends BukkitRunnable implements Listener {
    private final Game game;

    public SlowItemTask(Game game) {
        this.game = game;
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerInventory inventory = player.getInventory();
            ItemStack[] storageContents = inventory.getContents();
            Integer level = null;
            for (ItemStack stack : storageContents) {
                if (stack == null) {
                    continue;
                }
                if (stack.getItemMeta() != null) {
                    Integer t = stack.getItemMeta().getPersistentDataContainer().get(SlowItemCommand.SLOW_KEY, PersistentDataType.INTEGER);
                    if (t != null) {
                        if (level == null || t > level) {
                            level = t;
                        }
                    }
                }
            }
            if (level != null) { // found a level
                player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20, level - 1, false, false));
            }
        }
    }

    public void start(Main main) {
        runTaskTimer(main, 20L, 15L);
        main.getServer().getPluginManager().registerEvents(this, main);
    }

    @EventHandler
    public void onStop(GameStopEvent e) {
        if (e.getGame() == game) {
            cancel();
        }
    }
}
