package ewscli.model;

import microsoft.exchange.webservices.data.core.exception.service.local.ServiceLocalException;
import microsoft.exchange.webservices.data.core.service.folder.Folder;

public class FolderModel {
    // public String id;
    public String object;
    public String displayName;
    public int totalCount;
    public int unreadCount;
    public int childFolderCount;

    public FolderModel(Folder folder) {
        try {
            this.totalCount = folder.getTotalCount();
            this.object = folder.getFolderClass();
            this.displayName = folder.getDisplayName();
            //
            // FolderId is avoided because it may just confuse users.
            //
            // this.id = folder.getId().getUniqueId();
            this.unreadCount = folder.getUnreadCount();
            this.childFolderCount = folder.getChildFolderCount();
        } catch (ServiceLocalException e) {
            e.printStackTrace();
        }
    }
}
