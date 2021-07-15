package ewscli;

import com.sun.jna.Platform;

import java.io.File;

public class DefaultParams {
    public static final String APPNAME = "ewscli";
    public static String defaultConfigPath () {
        if(Platform.isWindows()) {
            return String.join(File.separator, new String[]{System.getProperties().getProperty("java.home"), APPNAME + ".config"});
        }
        // Follow "XDG Base Directory" in *NIX
        return String.join(File.separator, new String[]{System.getProperty("user.home") ,".config", APPNAME, APPNAME + ".config"});
    }
}