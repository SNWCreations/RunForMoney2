package snw.rfm.events;

import org.bukkit.event.Event;

public abstract class RFMEvent extends Event {
    public RFMEvent() {
    }

    public RFMEvent(boolean isAsync) {
        super(isAsync);
    }
}
