package ewscli.subcommands.mail;

import ewscli.AppConfig;
import ewscli.exception.InvalidConfigException;
import ewscli.logic.FolderSearchLogic;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName;
import microsoft.exchange.webservices.data.core.enumeration.service.DeleteMode;
import microsoft.exchange.webservices.data.core.service.folder.Folder;
import microsoft.exchange.webservices.data.property.complex.FolderId;
import picocli.CommandLine;

@CommandLine.Command(name = "delete-mails")
public class DeleteMailsCommand implements Runnable {
    @CommandLine.Option(names = "--folder-name", paramLabel = "( <FolderName> | '/' )")
    String folderName = "";

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
        var folderId = new FolderSearchLogic().getDeepestFolderId(service, new FolderId(WellKnownFolderName.MsgFolderRoot), folderName);
        try {
            Folder folder = Folder.bind(service, folderId);
            if (!folder.getFolderClass().equals("IPF.Note")) {
                throw new Exception("Given ID is NOT bound to mail folder.");
            }
            var deleteMode = DeleteMode.MoveToDeletedItems;
            if (mode.equals("hard")) {
                deleteMode = DeleteMode.HardDelete;
            } else if (mode.equals("soft")) {
                deleteMode = DeleteMode.SoftDelete;
            }
            if (dryrun) {
                System.err.println("[" + folderName + "] would have been emptied.");
                System.err.println("Total [" + folder.getTotalCount() + "] items would have been deleted with [" + mode + "] mode.");
                return;
            }
            folder.empty(deleteMode,false);
            System.err.println(folderName + " got emptied.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
