package ewscli.subcommands.base;

import ewscli.AppConfig;
import ewscli.exception.InvalidConfigException;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.service.folder.Folder;
import org.apache.commons.lang3.StringUtils;
import picocli.CommandLine;

import java.io.IOException;
import java.util.ArrayList;

public abstract class DescribeFoldersBase implements Runnable {

    @CommandLine.Option(names = "--folder-name", paramLabel = "( <Folder Name> | '/' )")
    String folderName = "";

    @CommandLine.Option(names = {"-v", "--verbose"}, description = "Print with verbose format." +
            "<Number of messages>, <Number of child folders>, <Folder Name>")
    boolean verbose = false;

    protected abstract ArrayList<Folder> getChildFolders(ExchangeService service, String folderName);
    protected abstract String getOutputString(Folder folder, boolean verbose) throws IOException;

    @Override public void run() {
        ExchangeService service = null;
        try {
            service = AppConfig.getInstance().getService();
        } catch (InvalidConfigException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        var results = getChildFolders(service, folderName);
        try {
            String output;
            for (Folder folder : results) {
                output = getOutputString(folder, verbose);
                if(StringUtils.isEmpty(output)) continue;
                System.out.println(output);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
