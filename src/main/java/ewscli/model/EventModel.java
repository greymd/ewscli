package ewscli.model;

import microsoft.exchange.webservices.data.core.enumeration.notification.EventType;
import microsoft.exchange.webservices.data.core.service.item.Item;

public class EventModel {

    public String type;
    public ItemModel payload;

    public EventModel(EventType eventType, Item item) {
        this.payload = new ItemModel(item);
        InitializeEventType(eventType);
    }

    private void InitializeEventType(EventType eventType) {
        switch (eventType) {
            case Created: this.type = "Created"; break;
            case NewMail: this.type = "NewMail"; break;
            case Deleted: this.type = "Deleted"; break;
            case Modified: this.type = "Modified"; break;
            case Status: this.type = "Status"; break;
            case Copied: this.type = "Copied"; break;
            case Moved: this.type = "Moved"; break;
            case FreeBusyChanged: this.type = "FreeBusyChanged"; break;
        }
    }
}
