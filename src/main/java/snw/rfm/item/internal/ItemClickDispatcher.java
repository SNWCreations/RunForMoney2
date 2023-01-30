package snw.rfm.item.internal;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import snw.rfm.Main;
import snw.rfm.api.item.ItemRegistry;
import snw.rfm.entity.IngamePlayer;
import snw.rfm.entity.TeamRegistry;

import java.util.Optional;

public class ItemClickDispatcher implements Listener {
    private final Main main;

    public ItemClickDispatcher(Main main) {
        this.main = main;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onClick(PlayerInteractEvent e) {
        if (main.isGamePresent()) {
            if (e.hasItem()) {
                if (TeamRegistry.RUNNER.contains(IngamePlayer.getWrappedPlayer(e.getPlayer()))) {
                    if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                        Optional.ofNullable(ItemRegistry.getCallback(e.getItem())).ifPresent(i -> {
                            boolean remove = i.onClick(e.getPlayer(), e.getItem());
                            if (remove) {
                                // NotNull at this time
                                // noinspection DataFlowIssue
                                e.getItem().setAmount(e.getItem().getAmount() - 1);
                            }
                        });
                    }
                }
            }
        }
    }
}
