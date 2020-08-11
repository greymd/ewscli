package ewscli.subcommands.mail;

import ewscli.AppConfig;
import ewscli.exception.InvalidConfigException;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.service.DeleteMode;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.core.service.item.Item;
import microsoft.exchange.webservices.data.property.complex.ItemId;
import picocli.CommandLine;

@CommandLine.Command(name = "delete-mail")
public class DeleteMailCommand implements Runnable {
    @CommandLine.Option(names = "--mail-id", paramLabel = "<Mail ID>", description = "Delete specified Email message item.")
    String itemId = "";

    @CommandLine.Option(names = "--mode", paramLabel = "MODE", description = "MODE:\n" +
            "   hard ... Delete folder/item permanently.\n" +
            "   soft ... Move the item to the dumpster.\n" +
            "   bin ... Move the item to the Deleted Items mailbox (default).\n")
    String mode = "bin";

    @CommandLine.Option(names = "--dry-run", description = "Show what would have been deleted")
    boolean dryrun = false;

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
            if(!item.getXmlElementName().equals("Message")) {
                throw new Exception("Given ID is NOT bound to mail item.");
            }
            EmailMessage emailItem = (EmailMessage)item;
            var deleteMode = DeleteMode.MoveToDeletedItems;
            if (mode.equals("hard")) {
                deleteMode = DeleteMode.HardDelete;
            } else if (mode.equals("soft")) {
                deleteMode = DeleteMode.SoftDelete;
            }
            if (dryrun) {
                System.err.println("[" + emailItem.getSubject() + "] would have been deleted with [" + mode + "] mode.");
                System.err.println("Mail ID: " + emailItem.getId());
                return;
            }
            emailItem.delete(deleteMode);
            System.err.println("Deleted Mail ID: " + emailItem.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
