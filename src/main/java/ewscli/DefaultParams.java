package ewscli;

import com.sun.jna.Platform;

import java.io.File;

public class DefaultParams {
    public static final String APPNAME = "ewscli";
    public static String defaultConfigPath () {
        if(Platform.isWindows()) {
            // return String.join(File.separator, new String[]{System.getProperty("user.home") , APPNAME, "config"});
            return String.join(File.separator, new String[]{System.getenv("LOCALAPPDATA") , APPNAME, "config"});
        }
        // Follow "XDG Base Directory" in *NIX
        return String.join(File.separator, new String[]{System.getProperty("user.home") ,".config", APPNAME, "config"});
    }
}
