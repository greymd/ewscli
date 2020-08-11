package ewscli.util;

import org.apache.commons.lang3.StringUtils;

import java.io.File;

public class Path {
    public static String regulatePath (String path) {
        return regulatePath(path, File.separator);
    }

    /**
     * Regulate path name. Remove File.separator if it located at...
     * * Prefix top of the path.
     * * Suffix of the path.
     * @param path i.e /A/B/C/D/
     * @return Regulated path i.e A/B/C/D
     */
    public static String regulatePath (String path, String separator) {
        if (StringUtils.isEmpty(separator)) {
            separator = File.separator;
        }
        // Squeeze repeated separators
        String result = path.replaceAll(separator + "+", separator)
                .replaceAll("^" + separator , "")
                .replaceAll(separator + "$", "");
        return result;
    }
}
