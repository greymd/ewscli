package ewscli.subcommands.mail;

import ewscli.AppConfig;
import ewscli.exception.InvalidConfigException;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.service.item.EmailMessage;
import microsoft.exchange.webservices.data.property.complex.MessageBody;
import org.apache.commons.lang3.StringUtils;
import picocli.CommandLine;

import java.util.ArrayList;

@CommandLine.Command(name = "create-mail")
public class CreateMailCommand implements Runnable {
    @CommandLine.Option(names = "--subject", paramLabel = "<Subject>", description = "Mail subject")
    String mailSubject = "";

    @CommandLine.Option(names = "--body", paramLabel = "<Body>", description = "Mail body. HTML can be used.")
    String mailBody = "";

    @CommandLine.Option(names = "--to", paramLabel = "<address>[,<address2>,...]", description = "Destination e-mail addresses. CSV is acceptable.", required = true)
    String mailTo = "";

    @CommandLine.Option(names = "--cc", paramLabel = "<address>[,<address2>,...]", description = "Carbon copy of e-mail addresses. CSV is acceptable.")
    String mailCc = "";

    @CommandLine.Option(names = "--bcc", paramLabel = "<address>[,<address2>,...]", description = "Blind carbon copy of e-mail addresses.")
    String mailBcc = "";

    @CommandLine.Option(names = "--draft", description = "Do not send the mail, save it into Drafts folder instead.")
    boolean draft = false;

    @Override public void run() {
        ExchangeService service = null;
        try {
            service = AppConfig.getInstance().getService();
        } catch (InvalidConfigException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        try {
            EmailMessage msg = new EmailMessage(service);
            msg.setSubject(mailSubject);
            msg.setBody(MessageBody.getMessageBodyFromText(mailBody));
            if(!StringUtils.isEmpty(mailTo)) {
                for (String address: splitCsv2ArrayList(mailTo)) {
                    msg.getToRecipients().add(address);
                }
            }
            if(!StringUtils.isEmpty(mailCc)) {
                for (String address: splitCsv2ArrayList(mailCc)) {
                    msg.getCcRecipients().add(address);
                }
            }
            if(!StringUtils.isEmpty(mailBcc)) {
                for (String address: splitCsv2ArrayList(mailBcc)) {
                    msg.getCcRecipients().add(address);
                }
            }
            if(draft) {
                msg.save();
                System.err.println("New mail has been saved in [Drafts] folder.\n" +
                        "Mail ID: " + msg.getId());
                return;
            }
            msg.sendAndSaveCopy();
            // msg.send();
            System.err.println("Mail has been sent. Copy is in [Sent Items] folder.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ArrayList<String> splitCsv2ArrayList (String csv) {
        var result = new ArrayList<String>();
        for(String elem: csv.trim().split(",")){
            result.add(elem.trim());
        }
        return result;
    }
}
