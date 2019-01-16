package info.techsdev.nxt;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Simple settings file to configure the rest of the patcher
 */
public class Settings {

    /**
     * The regex in the launcher to see if websites are "legal" or not
     */
    public static final String RUNESCAPE_REGEX = "^https?://[a-z0-9\\-]*\\.?runescape.com(:[0-9]+)?/\0";

    /**
     * The regex we should patch. The string should be shorter than RuneScape's Regex
     */
    public static String PATCHED_REGEX = "^.*";

    /**
     * The RuneScape config URI where the game configuration is loaded from by default
     */
    public static final String RUNESCAPE_CONFIG_URI = "http://www.runescape.com/k=5/l=$(Language:0)/jav_config.ws\0";

    /**
     * The patched config URI where the game configuration should be loaded from instead. The string should be
     * shorter than RuneScape's config URI
     */
    public static String PATCHED_CONFIG_URI = "http://localhost/jav_config.ws";

    /**
     * The base path to patch files from
     */
    public static final Path BASE_PATH = Paths.get("library");

}
