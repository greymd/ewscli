package ewscli.subcommands.general;

import com.fasterxml.jackson.databind.ObjectMapper;
import ewscli.model.FolderModel;
import ewscli.logic.FolderSearchLogic;
import ewscli.subcommands.base.DescribeFoldersBase;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.service.folder.Folder;
import picocli.CommandLine;

import java.io.IOException;
import java.util.ArrayList;

@CommandLine.Command(name = "describe-folders")
public class DescribeFoldersCommand extends DescribeFoldersBase {

    @Override
    protected ArrayList<Folder> getChildFolders(ExchangeService service, String folderName) {
        return new FolderSearchLogic().findChildFolders(service, folderName);
    }

    @Override
    protected String getOutputString(Folder folder, boolean verbose) throws IOException {
        var mapper = new ObjectMapper();
        return mapper.writeValueAsString(new FolderModel(folder));
    }
}
