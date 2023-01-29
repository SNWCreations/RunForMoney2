package snw.rfm.util;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import snw.rfm.Main;

import java.util.LinkedList;

public class ListenerList extends LinkedList<Listener> {
    private final Main main;

    public ListenerList(Main main) {
        this.main = main;
    }

    @Override
    public boolean add(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, main);
        return super.add(listener);
    }

    @Override
    public boolean remove(Object o) {
        if (!(o instanceof Listener)) {
            return false;
        }
        HandlerList.unregisterAll((Listener) o);
        return super.remove(o);
    }

    @Override
    public void clear() {
        forEach(HandlerList::unregisterAll);
        super.clear();
    }
}
