package ewscli.subcommands.base;

import ewscli.AppConfig;
import ewscli.exception.InvalidConfigException;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.PropertySet;
import microsoft.exchange.webservices.data.core.exception.service.remote.ServiceRequestException;
import microsoft.exchange.webservices.data.core.service.item.Item;
import microsoft.exchange.webservices.data.search.FindItemsResults;
import microsoft.exchange.webservices.data.search.ItemView;
import org.apache.commons.lang3.StringUtils;
import picocli.CommandLine;

import java.io.IOException;

public abstract class DescribeItemsBase implements Runnable {
    @CommandLine.Option(names = "--folder-name", paramLabel = "( <FolderName> | '/' )")
    String folderName = "";

    @CommandLine.Option(names = "--max", description = "Maximum number of results. [default: 30, maximum: 1000)]")
    private int max = 30;

    @CommandLine.Option(names = {"-v", "--verbose"}, description = "Print with verbose format." +
            "<Received date>, <Subject>, <MailId>")
    private boolean verbose = false;

    @CommandLine.Option(names = "--query",
            paramLabel = "<QueryString>",
            description = "i.e, \"Sent:01/01/2001..01/15/2001\".\nSee https://docs.microsoft.com/en-us/exchange/client-developer/web-service-reference/querystring-querystringtype")
    protected String query = "";

    protected ItemView getView () {
        return new ItemView(max);
    }

    protected abstract FindItemsResults<Item> getItems(ExchangeService service, String folderName) throws Exception;

    protected abstract String getOutputString(Item item, boolean verbose) throws IOException;

    @Override public void run() {
        ExchangeService service = null;
        try {
            service = AppConfig.getInstance().getService();
        } catch (InvalidConfigException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        try {
            FindItemsResults<Item> findResults = getItems(service, folderName);
            if (findResults.getTotalCount() == 0) return;
            service.loadPropertiesForItems(findResults, PropertySet.FirstClassProperties);
            System.err.println("Total " + findResults.getTotalCount() + " items");
            String output;
            for (Item item : findResults.getItems()) {
                output = getOutputString(item, verbose);
                if(StringUtils.isEmpty(output)) continue;
                System.out.println(output);
            }
        } catch (NullPointerException | ServiceRequestException e) {
            System.err.println("ewscli: [Error] If error includes SSLHandshakeException, the Exchange endpoint is not trusted.");
            System.err.println("ewscli: [Error] Run `ewscli configure' again and trust the certificate.");
            System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
