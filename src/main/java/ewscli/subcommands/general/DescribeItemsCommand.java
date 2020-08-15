package ewscli.subcommands.general;

import com.fasterxml.jackson.databind.ObjectMapper;
import ewscli.subcommands.base.DescribeItemsBase;
import ewscli.model.ItemModel;
import ewscli.logic.FolderSearchLogic;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName;
import microsoft.exchange.webservices.data.core.service.item.Item;
import microsoft.exchange.webservices.data.property.complex.FolderId;
import microsoft.exchange.webservices.data.search.FindItemsResults;
import picocli.CommandLine;

import java.io.IOException;

@CommandLine.Command(name = "describe-items")
public class DescribeItemsCommand extends DescribeItemsBase {
    @Override
    protected FindItemsResults<Item> getItems(ExchangeService service, String folderName) throws Exception {
        var root = new FolderId(WellKnownFolderName.Root);
        var folderId = new FolderSearchLogic().getDeepestFolderId(service, root, folderName);
        var view = this.getView();
        return service.findItems(folderId, query, view);
    }

    @Override
    protected String getOutputString(Item item, boolean verbose) throws IOException {
        var mapper = new ObjectMapper();
        return mapper.writeValueAsString(new ItemModel(item));
    }
}
