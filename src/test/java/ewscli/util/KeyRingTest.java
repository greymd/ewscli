package ewscli.util;

import com.sun.jna.Platform;
import net.east301.keyring.BackendNotSupportedException;
import net.east301.keyring.PasswordRetrievalException;
import net.east301.keyring.PasswordSaveException;
import net.east301.keyring.osx.OSXKeychainBackend;
import net.east301.keyring.windows.WindowsDPAPIBackend;
import org.junit.Test;

import static org.junit.Assert.*;

public class KeyRingTest {
    private static final String SERVICE = "ewscli unit test";
    private static final String ACCOUNT = "testaccount";
    private static final String PASSWORD = "a%#14d@33aaaa";

    private static void checkExistanceOfPasswordEntry(OSXKeychainBackend backend) {
        try {
            backend.getPassword(SERVICE, ACCOUNT);

            System.err.println(String.format(
                    "Please remove password entry '%s' " +
                            "by using Keychain Access before running the tests",
                    SERVICE));
        } catch (PasswordRetrievalException ex) {
            // do nothing
        }
    }

    @Test
    public void keyringSupport() {
        if(Platform.isMac()) {
            assertEquals(true, new OSXKeychainBackend().isSupported());
        } else if (Platform.isWindows()) {
            assertEquals(true, new WindowsDPAPIBackend().isSupported());
        } else {
            System.out.println("It is not supported platform.");
            // skip test
        }
    }
    @Test
    public void keyringStoreTest() throws BackendNotSupportedException, PasswordSaveException, PasswordRetrievalException {
        if(Platform.isMac()) {
            OSXKeychainBackend backend = new OSXKeychainBackend();
            backend.setup();
            // checkExistanceOfPasswordEntry(backend);
            backend.setPassword(SERVICE, ACCOUNT, PASSWORD);
            assertEquals(PASSWORD, backend.getPassword(SERVICE, ACCOUNT));
        } else if (Platform.isWindows()) {
            //
        } else {
            System.out.println("It is not supported platform.");
            // skip test
        }
    }

    @Test
    public void isKeyStoreAvailable() {
        if(Platform.isMac()) {
            assertEquals(true, new KeyRing().isKeyStoreAvailable());
        }
    }

    @Test
    public void hasKey() {
        if(Platform.isMac()) {
            var keyStore = new KeyRing();
            keyStore.store(SERVICE, ACCOUNT, PASSWORD);
            assertEquals(true, keyStore.hasKey(SERVICE, ACCOUNT));
            assertEquals(PASSWORD, keyStore.getValue(SERVICE, ACCOUNT));
        }
    }
}