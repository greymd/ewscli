package ewscli.logic;

import ewscli.util.Path;
import microsoft.exchange.webservices.data.core.ExchangeService;
import microsoft.exchange.webservices.data.core.enumeration.property.WellKnownFolderName;
import microsoft.exchange.webservices.data.core.enumeration.search.FolderTraversal;
import microsoft.exchange.webservices.data.core.service.folder.Folder;
import microsoft.exchange.webservices.data.core.service.schema.FolderSchema;
import microsoft.exchange.webservices.data.property.complex.FolderId;
import microsoft.exchange.webservices.data.search.FindFoldersResults;
import microsoft.exchange.webservices.data.search.FolderView;
import microsoft.exchange.webservices.data.search.filter.SearchFilter;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FolderSearchLogic {

    public ArrayList<Folder> findChildFolders(ExchangeService service, String fullPath) {
        return findChildFolders(service, fullPath, new FolderId(WellKnownFolderName.Root), null);
    }

    public ArrayList<Folder> findChildFoldersForEmail(ExchangeService service, String fullPath) {
        var emailRoot = new FolderId(WellKnownFolderName.MsgFolderRoot);
        var emailFilter = new SearchFilter.IsEqualTo(FolderSchema.FolderClass, "IPF.Note");
        return findChildFolders(service, fullPath, emailRoot, emailFilter);
    }

    public ArrayList<Folder> findChildFolders(ExchangeService service, String fullPath, FolderId rootFolderId, SearchFilter searchFilter) {
        FolderId deepest = getDeepestFolderId(service, rootFolderId, fullPath);
        try {
            FindFoldersResults findResults = null;
            if (searchFilter == null) {
                findResults = service.findFolders(deepest, new FolderView(Integer.MAX_VALUE));
            } else {
                findResults = service.findFolders(deepest, searchFilter, new FolderView(Integer.MAX_VALUE));
            }
            return findResults.getFolders();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Return the folderId bound to the lowest folder.
     * If {@code fullPath} is /folderA/folderB/folderC, the result is folderC's folderId.
     * @param service Authorized service.
     * @param rootFolderId Folder id of root.
     * @param fullPath Full Path from root.
     * @return folderId
     */
    public FolderId getDeepestFolderId(ExchangeService service, FolderId rootFolderId, String fullPath) {
        List<FolderId> chain = getFolderIdChain(service, rootFolderId, fullPath);
        FolderId deepest = chain.get(chain.size() - 1);
        return deepest;
    }

    /**
     * Finds the folderId of the lowest folder and returns the ArrayList includes all the parent folder ids.
     * If {@code fullPath} is /folderA/folderB, the result is {@code ArrayList} which has {<folderA's ID>, <folderB's ID>}.
     * @param service Authorized service.
     * @param fullPath Directory name. It may include directory separator like '/' or '\'.
     * @return ArrayList of folderId
     */
    public ArrayList<FolderId> getFolderIdChain(ExchangeService service, FolderId rootFolderId, String fullPath) {
        var chain = new ArrayList<FolderId>();
        FolderId currentFolderId = rootFolderId;
        chain.add(currentFolderId);
        fullPath = Path.regulatePath(fullPath);
        if (StringUtils.isEmpty(fullPath) || fullPath.equals(Path.SEPARATOR)) {
            return chain;
        }
        String[] dirs = fullPath.split(Path.SEPARATOR);
        try {
            for (String dir : dirs) {
                var filter = new SearchFilter.IsEqualTo(FolderSchema.DisplayName, dir);
                var view = new FolderView(Integer.MAX_VALUE);
                view.setTraversal(FolderTraversal.Shallow);
                var results = service.findFolders(currentFolderId, filter, view);
                if (results.getTotalCount() == 1) {
                    currentFolderId = results.getFolders().get(0).getId();
                    chain.add(currentFolderId);
                } else {
                    throw new Exception("No such the folder:" + dir);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return chain;
    }
}
