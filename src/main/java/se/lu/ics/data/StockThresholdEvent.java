package se.lu.ics.data;

import javafx.event.Event;
import javafx.event.EventType;

public class StockThresholdEvent extends Event {
    public static final EventType<StockThresholdEvent> STOCK_THRESHOLD_REACHED =
            new EventType<>(Event.ANY, "This warehouse has reached 80 of its max capacity!");

    public StockThresholdEvent() {
        super(STOCK_THRESHOLD_REACHED);
    }
}

