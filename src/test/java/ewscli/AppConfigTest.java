package ewscli;

import ewscli.exception.InvalidConfigException;
import org.junit.Test;

import static org.junit.Assert.*;

public class AppConfigTest {

    @Test
    public void getInstance() throws InvalidConfigException {
        AppConfig appConfig = AppConfig.getInstance();
        assertNotNull(appConfig.getService().getCredentials());
    }
}