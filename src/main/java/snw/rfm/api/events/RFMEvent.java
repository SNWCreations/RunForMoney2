package snw.rfm.api.events;

import org.bukkit.event.Event;

public abstract class RFMEvent extends Event {
    public RFMEvent() {
        this(false);
    }

    public RFMEvent(boolean isAsync) {
        super(isAsync);
    }
}
