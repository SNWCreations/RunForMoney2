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
import snw.rfm.commands.SlowItemCommand;
import snw.rfm.entity.Game;
import snw.rfm.api.events.GameStopEvent;

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
            for (ItemStack stack : storageContents) {
                if (stack == null) {
                    continue;
                }
                if (stack.getItemMeta() != null) {
                    Integer level = stack.getItemMeta().getPersistentDataContainer().get(SlowItemCommand.SLOW_KEY, PersistentDataType.INTEGER);
                    if (level != null) {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20, level - 1, false, false));
                    }
                    break; // do not apply for many times
                }
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
