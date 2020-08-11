package ewscli.util;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class ConfigFileTest {
    @Rule
    public TemporaryFolder folder= new TemporaryFolder();

    @Test
    public void isExist() {
        var cfg = new ConfigFile();
        File tmp = null;
        try {
            tmp = folder.newFile("tmpconfig");
            // This must be false because empty config file.
            assertEquals(false, cfg.isPropertyExist(tmp.getPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void createAndDelete() {
        var cfg = new ConfigFile();
        File tmp = null;
        String filename = folder.getRoot().getPath() + File.separator + "tmpconfig";
        assertEquals(true, cfg.create(filename));
        assertEquals(false, cfg.isPropertyExist(filename));
        cfg.setProperty("username", "hogehoge");
        cfg.save(filename);
        assertEquals(true, cfg.isPropertyExist(filename));
        cfg.load(filename);
        assertEquals("hogehoge", cfg.getProperty("username"));
        assertEquals(true, cfg.isPropertyExist(filename));
        cfg.delete(filename);
        assertEquals(false, cfg.isPropertyExist(filename));
    }
}