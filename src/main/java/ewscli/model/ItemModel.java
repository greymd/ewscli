package ewscli.model;

import microsoft.exchange.webservices.data.core.exception.service.local.ServiceLocalException;
import microsoft.exchange.webservices.data.core.service.item.Item;

public class ItemModel {
    public String object;
    public String id;
    public String to;
    public String cc;
    public long created;
    public String subject;
    public String body;

    public ItemModel(Item item) {
        try {

            this.id = item.getId().getUniqueId();
            this.body = item.getBody().toString();
            this.subject = item.getSubject();
            this.cc = item.getDisplayCc();
            this.to = item.getDisplayTo();
            this.object = item.getItemClass();
            this.created = item.getDateTimeCreated().getTime() / 1000L; // Convert to seconds
            //
            // As far as I investigated, this "received" is same as "created".
            //
            // this.received = item.getDateTimeReceived();
        } catch (ServiceLocalException e) {
            e.printStackTrace();
        }
    }
}
