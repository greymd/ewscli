package ewscli.model;

import microsoft.exchange.webservices.data.core.exception.service.local.ServiceLocalException;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.property.complex.EmailAddress;

import java.util.ArrayList;
import java.util.List;

public class MailItemModel extends ItemModel {
    public String from;
    public List<String> ccAll;
    public List<String> toAll;
    public Boolean isRead;
    // public String sender;

    public MailItemModel(EmailMessage emailMessage) {
        super(emailMessage);
        try {
            this.from = emailMessage.getFrom().getAddress();
            this.isRead = emailMessage.getIsRead();
            /*
             * As far as I investigated, this "from" is same as "sender".
             */
            // this.sender = emailMessage.getSender().toString();

            this.ccAll = new ArrayList<String>();
            for (EmailAddress ea : emailMessage.getCcRecipients().getItems()) {
                this.ccAll.add(ea.getAddress());
            }
            this.toAll = new ArrayList<String>();
            for (EmailAddress ea : emailMessage.getToRecipients().getItems()) {
                this.toAll.add(ea.getAddress());
            }
        } catch (ServiceLocalException e) {
            e.printStackTrace();
        }
    }
}
