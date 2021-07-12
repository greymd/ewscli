package ewscli.logic;

import ewscli.AppConfig;
import ewscli.exception.InvalidConfigException;
import ewscli.util.Path;
import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName;
import microsoft.exchange.webservices.data.core.exception.service.local.ServiceLocalException;
import microsoft.exchange.webservices.data.core.service.folder.Folder;
import microsoft.exchange.webservices.data.property.complex.FolderId;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class FolderSearchLogicTest {

    @Test
    public void findChildFoldersRoot() throws InvalidConfigException {
        var service = AppConfig.getInstance().getService();
        var result = new ArrayList<FolderId>();
        result = new FolderSearchLogic().getFolderIdChain(service, new FolderId(WellKnownFolderName.MsgFolderRoot), Path.SEPARATOR);
        assertEquals(1, result.size());

        result = new FolderSearchLogic().getFolderIdChain(service, new FolderId(WellKnownFolderName.MsgFolderRoot), "");
        assertEquals(1, result.size());
        System.out.println(result.get(0).getFolderName());
    }

    @Test
    public void findChildFoldersDeep() throws InvalidConfigException {
        var service = AppConfig.getInstance().getService();
        var result = new ArrayList<FolderId>();

        result = new FolderSearchLogic().getFolderIdChain(service, new FolderId(WellKnownFolderName.MsgFolderRoot), "/Inbox");
        // result = new FolderSearchLogic().getFolderIdChain(service, "Inbox/AAA/BBB/CCC");
        assertNotEquals(0, result.size());
    }

    @Test
    public void findChildFolders() throws InvalidConfigException {
        var service = AppConfig.getInstance().getService();
        var result = new FolderSearchLogic().findChildFolders(service, "");
        for (Folder res :result) {
            try {
                System.out.println(res.getDisplayName());
            } catch (ServiceLocalException e) {
                e.printStackTrace();
            }
        }
        assertNotEquals(0, result.size());
    }

    @Test
    public void findChildFoldersForEmail() throws InvalidConfigException {
        var service = AppConfig.getInstance().getService();
        var result = new FolderSearchLogic().findChildFoldersForEmail(service, "");
        for (Folder res :result) {
            try {
                System.out.println(res.getDisplayName());
            } catch (ServiceLocalException e) {
                e.printStackTrace();
            }
        }
        assertNotEquals(0, result.size());
    }
}