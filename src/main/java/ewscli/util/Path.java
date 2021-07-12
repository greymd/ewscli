package ewscli.util;

import org.apache.commons.lang3.StringUtils;

public class Path {
    // Unfortunately, I couldn't respect MSDOS style file separator due to the inconvenience in the regex.
    public static String SEPARATOR = "/";
    // private String DEFAULT_DEPARATOR = File.separator;
    public static String regulatePath (String path) {
        return regulatePath(path, SEPARATOR);
    }

    /**
     * Regulate path name. Remove the file separator if it located at...
     * * Prefix top of the path.
     * * Suffix of the path.
     * @param path i.e /A/B/C/D/
     * @return Regulated path i.e A/B/C/D
     */
    public static String regulatePath (String path, String separator) {
        String sep = separator;
        if (StringUtils.isEmpty(sep)) {
            sep = SEPARATOR;
        }
        // Squeeze repeated separators
        String result = path.replaceAll(sep + "+", sep)
                .replaceAll("^" + sep , "")
                .replaceAll(sep + "$", "");
        return result;
    }
}
