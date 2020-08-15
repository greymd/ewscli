package ewscli.subcommands.mail;

import com.fasterxml.jackson.databind.ObjectMapper;
import ewscli.AppConfig;
import ewscli.exception.InvalidConfigException;
import ewscli.logic.FolderSearchLogic;
import ewscli.model.EventModel;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.notification.EventType;
import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.notification.*;
import microsoft.exchange.webservices.data.property.complex.FolderId;
import picocli.CommandLine;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@CommandLine.Command(name = "monitor-folders", description = "Monitor new mails.")
public class MonitorFoldersCommand implements Runnable{

    @CommandLine.Option(names = "--folder-name",
            paramLabel = "<FolderName>",
            description = "Name of folder to be monitored. Multiple options are allowed.",
            required = true)
    String[] folderNames = {""};

    @Override public void run() {
        ExchangeService service = null;
        // Subscribe to pull notifications in the Inbox folder, and get notified when a new mail is received, when an item or folder is created, or when an item or folder is deleted.
        List folder = new ArrayList();
        try {
            service = AppConfig.getInstance().getService();
        } catch (InvalidConfigException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        var emailRoot = new FolderId(WellKnownFolderName.MsgFolderRoot);
        for (String folderName: folderNames) {
            FolderId deepest = new FolderSearchLogic().getDeepestFolderId(service, emailRoot, folderName);
            folder.add(deepest);
            System.err.println("Check existence of " + folderName);
        }

        try {
            PullSubscription subscription = service.subscribeToPullNotifications(folder, 5,null, EventType.NewMail);
            System.err.println("Start polling ...");

            GetEventsResults events = null;
            var mapper = new ObjectMapper();

            // Loop through all item-related events.
            while(true) {
                events = subscription.getEvents();
                for(ItemEvent itemEvent : events.getItemEvents()) {
                    if (itemEvent.getEventType() == EventType.NewMail) {
                        EmailMessage message = EmailMessage.bind(service, itemEvent.getItemId());
                        System.out.println(mapper.writeValueAsString(new EventModel(itemEvent.getEventType(), message)));
                    }
                }
                TimeUnit.SECONDS.sleep(3);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
