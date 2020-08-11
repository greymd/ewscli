package ewscli;
import org.junit.Test;

public class MainTest {
    // Those test are supposed to be executed manually.
    // Do not execute in automated testing.
    @Test public void mainVersion() {
        Main classUnderTest = new Main();
        classUnderTest.main(new String[]{"--version"});
    }
    @Test public void mainDescribeFoldersEmpty() {
        Main classUnderTest = new Main();
        classUnderTest.main(new String[]{"mail", "describe-folders"});
    }
    @Test public void mainDescribeFolders() {
        Main classUnderTest = new Main();
        classUnderTest.main(new String[]{"mail", "describe-folders", "--folder-name", "Inbox"});
    }
    @Test public void mainDescribeFoldersVerbose() {
        Main classUnderTest = new Main();
        classUnderTest.main(new String[]{"mail", "describe-folders", "--verbose", "--folder-name", "Inbox"});
    }
    @Test public void mainDescribeItems() {
        Main classUnderTest = new Main();
        classUnderTest.main(new String[]{"mail", "describe-mails", "--folder-name", "Inbox", "--max", "10"});
    }
    @Test public void mainDescribeItemsVerbose() {
        Main classUnderTest = new Main();
        classUnderTest.main(new String[]{"mail", "describe-mails", "--folder-name", "Inbox", "--max", "10", "--verbose"});
    }
    @Test public void mainDescribeItemsEmpty() {
        Main classUnderTest = new Main();
        classUnderTest.main(new String[]{"mail", "describe-mails", "--folder-name", "Calendar", "--max", "10"});
    }
    @Test public void mainDeleteMail() {
        Main classUnderTest = new Main();
        classUnderTest.main(new String[]{"mail", "describe-mail", "--mail-id", "..."});
    }
    @Test public void mainDeleteMails() {
        Main classUnderTest = new Main();
        classUnderTest.main(new String[]{"mail", "delete-mails", "--folder-name", "Deleted Items", "--mode", "hard"});
    }
    @Test public void mainDeleteMailsDryRun() {
        Main classUnderTest = new Main();
        classUnderTest.main(new String[]{"mail", "delete-mails", "--folder-name", "Inbox", "--mode", "bin", "--dry-run"});
    }
    @Test public void mainCreateMail() {
        Main classUnderTest = new Main();
        // classUnderTest.main(new String[]{"mail", "create-mail", "--subject", "test", "--body", "test\nand\ntest", "--draft", "--to", "testuser@example.com"});
    }
    @Test public void mainGeneralDescribeFolders() {
        Main classUnderTest = new Main();
        classUnderTest.main(new String[]{"general", "describe-folders", "-v"});
    }
    @Test public void mainGeneralDescribeItems() {
        Main classUnderTest = new Main();
        classUnderTest.main(new String[]{"general", "describe-items", "--folder-name", "AllItems", "--max", "10"});
    }
    @Test public void mainGeneralDescribeItemsVerbose() {
        Main classUnderTest = new Main();
        classUnderTest.main(new String[]{"general", "describe-items", "--folder-name", "AllItems", "--max", "10", "-v"});
    }
}
