package ewscli;

import java.io.File;

public class DefaultParams {
    public static final String APPNAME = "ewscli";
    public static String defaultConfigPath () {
        return String.join(File.separator, new String[]{System.getProperties().getProperty("java.home"), APPNAME + ".config"});
    }
}
