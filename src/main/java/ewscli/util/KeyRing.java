package ewscli.util;

import com.sun.jna.Platform;
import net.east301.keyring.BackendNotSupportedException;
import net.east301.keyring.KeyringBackend;
import net.east301.keyring.PasswordRetrievalException;
import net.east301.keyring.PasswordSaveException;
import net.east301.keyring.osx.OSXKeychainBackend;
import net.east301.keyring.util.LockException;

public class KeyRing {
    KeyringBackend backend;
    public KeyRing() {
        if(Platform.isMac()) {
            this.backend = new OSXKeychainBackend();
        } else if (Platform.isWindows()) {
            // return new WindowsDPAPIBackend().isSupported();
            // TODO: Windows keychain
            this.backend = null;
        } else if (Platform.isLinux()){
            // TODO: Linux keychain
            this.backend = null;
        } else {
            this.backend = null;
        }

        if(this.backend != null ) {
            try {
                this.backend.setup();
            } catch (BackendNotSupportedException e) {
                this.backend = null;
            }
        }
    }

    public static String getKeychainName() {
        if(Platform.isMac()) {
            return "macOS Keychain";
        } else if (Platform.isWindows()) {
            return "Windows DPAPI";
        } else {
            return "";
        }
    }

    public boolean isKeyStoreAvailable () {
        if (backend == null) return false;
        return backend.isSupported();
    }

    public boolean hasKey (String serviceName, String key) {
        try {
            backend.getPassword(serviceName, key);
        } catch (LockException | PasswordRetrievalException e) {
            // No password stored.
            return false;
        }
        return true;
    }

    public void store (String serviceName, String key, String value) {
        try {
            backend.setPassword(serviceName, key, value);
        } catch (LockException | PasswordSaveException e) {
            e.printStackTrace();
        }
    }

    public String getValue (String serviceName, String key) {
        try {
            return backend.getPassword(serviceName, key);
        } catch (LockException | PasswordRetrievalException e) {
            e.printStackTrace();
        }
        return null;
    }
}
