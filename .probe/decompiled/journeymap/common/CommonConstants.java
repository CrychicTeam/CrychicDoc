package journeymap.common;

import com.google.common.base.Joiner;
import java.io.File;
import java.util.regex.Pattern;
import net.minecraft.world.entity.Entity;

public class CommonConstants {

    private static final Joiner path = Joiner.on(File.separator).useForNull("");

    private static final String END = null;

    public static final String JOURNEYMAP_DIR = "journeymap";

    public static final Pattern LEGAL_CHARS = Pattern.compile("[^a-zA-Z0-9 ]");

    public static final Pattern PATTERN_WITH_UNICODE = Pattern.compile("[^\\w\\s\\p{L}]+", 256);

    public static final Pattern CSS_SAFE_PATTERN = Pattern.compile("[^\\w\\p{L}]+", 256);

    public static boolean isDev(Entity sender) {
        return "79f597fe-2877-4ecb-acdf-8c58cc1854ca".equalsIgnoreCase(sender.getUUID().toString()) || "a2039b6c-5a3d-407d-b49c-091405062b85".equalsIgnoreCase(sender.getUUID().toString());
    }

    public static boolean debugOverride(Entity sender) {
        return isDev(sender);
    }

    public static String getSafeString(String string, String replacement) {
        return string.replaceAll(PATTERN_WITH_UNICODE.pattern(), replacement);
    }

    public static String getCSSSafeString(String string, String replacement) {
        return string.replaceAll(CSS_SAFE_PATTERN.pattern(), replacement);
    }

    public static String getServerConfigDir() {
        return path.join(LoaderHooks.getServer().getServerDirectory(), "journeymap", new Object[] { "server", Journeymap.JM_VERSION.toMajorMinorString(), END });
    }
}