package ewscli.subcommands.mail;

import ewscli.AppConfig;
import ewscli.exception.InvalidConfigException;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.core.service.item.Item;
import microsoft.exchange.webservices.data.property.complex.ItemId;
import picocli.CommandLine;

@CommandLine.Command(name = "describe-mail")
public class DescribeMailCommand implements Runnable {
    @CommandLine.Option(names = "--mail-id", paramLabel = "<Mail ID>")
    String itemId = "";

    @Override public void run() {
        ExchangeService service = null;
        try {
            service = AppConfig.getInstance().getService();
        } catch (InvalidConfigException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        try {
            Item item = Item.bind(service, ItemId.getItemIdFromString(itemId));
            if(item.getXmlElementName().equals("Message")) {
                EmailMessage emailItem = (EmailMessage)item;
                System.out.printf("%s\t%s\n%s\t%s\n", emailItem.getDateTimeReceived(), emailItem.getSender().getName(), emailItem.getSubject(), emailItem.getId());
            } else {
                throw new Exception("Given ID is NOT bound to mail item.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
