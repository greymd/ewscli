package ewscli.subcommands.base;

import ewscli.AppConfig;
import ewscli.exception.InvalidConfigException;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.exception.service.remote.ServiceRequestException;
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
            e.printStackTrace();
            System.exit(1);
        }
        try {
            var results = getChildFolders(service, folderName);
            String output;
            for (Folder folder : results) {
                output = getOutputString(folder, verbose);
                if(StringUtils.isEmpty(output)) continue;
                System.out.println(output);
            }
        } catch (NullPointerException e) {
            System.err.println("ewscli: [Error] If error includes SSLHandshakeException, the Exchange endpoint is not trusted.");
            System.err.println("ewscli: [Error] Run `ewscli configure' again and trust the certificate.");
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
