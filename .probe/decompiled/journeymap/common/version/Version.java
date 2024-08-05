package journeymap.common.version;

import com.google.common.base.Joiner;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.Arrays;
import journeymap.common.Journeymap;
import journeymap.common.network.data.Side;
import org.apache.logging.log4j.util.Strings;

public class Version implements Comparable<Version> {

    private static final String DIGIT_NUMBER_SPLIT_MATCHER = "(?<=\\D)(?=\\d)";

    private static final String DIGIT_PATCH_VERSION_SPLIT_MATCHER = "(?<=\\d)(?=\\p{L})";

    public final int major;

    public final int minor;

    public final int micro;

    public final String patch;

    public String loader;

    public String loaderVersion;

    public String minecraftVersion;

    public Version(int major, int minor, int micro) {
        this(major, minor, micro, "");
    }

    public Version(int major, int minor, int micro, String patch) {
        this.major = major;
        this.minor = minor;
        this.micro = micro;
        this.patch = patch != null ? patch : "";
    }

    public static Version from(String major, String minor, String micro, String patch, Version defaultVersion) {
        Version result = null;
        try {
            if (!major.contains("@")) {
                result = new Version(parseInt(major), parseInt(minor), parseInt(micro), patch);
            }
        } catch (Exception var7) {
            Journeymap.getLogger().warn(String.format("Version had problems when parsed: %s, %s, %s, %s", major, minor, micro, patch));
        }
        if (result == null) {
            if (defaultVersion == null) {
                defaultVersion = new Version(0, 0, 0);
            }
            result = defaultVersion;
        }
        return result;
    }

    public static Version from(String versionString, Version defaultVersion) {
        try {
            String[] strings = versionString.split("(?<=\\d)(?=\\p{L})");
            String[] majorMinorMicro = strings[0].split("\\.");
            String patch = strings.length == 2 ? strings[1] : "";
            if (majorMinorMicro.length < 3) {
                majorMinorMicro = (String[]) Arrays.copyOf(strings, 3);
            }
            return from(majorMinorMicro[0], majorMinorMicro[1], majorMinorMicro[2], patch, defaultVersion);
        } catch (Exception var5) {
            Journeymap.getLogger().warn(String.format("Version had problems when parsed: %s", versionString));
            if (defaultVersion == null) {
                defaultVersion = new Version(0, 0, 0);
            }
            return defaultVersion;
        }
    }

    private static int parseInt(String number) {
        return number == null ? 0 : Integer.parseInt(number);
    }

    public String toMajorMinorString() {
        return Joiner.on(".").join(this.major, this.minor, new Object[0]);
    }

    public String toMajorMinorMicroString() {
        return Joiner.on(".").join(this.major, this.minor, new Object[] { this.micro });
    }

    public boolean isNewerThan(Version other) {
        return this.compareTo(other) > 0;
    }

    public boolean isRelease() {
        return Strings.isEmpty(this.patch);
    }

    public String toString() {
        return Joiner.on(".").join(this.major, this.minor, new Object[] { this.micro + this.patch });
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            Version version = (Version) o;
            if (this.major != version.major) {
                return false;
            } else if (this.micro != version.micro) {
                return false;
            } else {
                return this.minor != version.minor ? false : this.patch.equals(version.patch);
            }
        } else {
            return false;
        }
    }

    public int hashCode() {
        int result = this.major;
        result = 31 * result + this.minor;
        result = 31 * result + this.micro;
        return 31 * result + this.patch.hashCode();
    }

    public int compareTo(Version other) {
        int result = Integer.compare(this.major, other.major);
        if (result == 0) {
            result = Integer.compare(this.minor, other.minor);
        }
        if (result == 0) {
            result = Integer.compare(this.micro, other.micro);
        }
        if (result == 0) {
            result = this.patch.compareToIgnoreCase(other.patch);
            if (result != 0) {
                result = this.comparePatchVersion(other);
            }
        }
        return result;
    }

    private int comparePatchVersion(Version other) {
        if (this.patch.equals("") && other.patchInt() == 3) {
            return -1;
        } else if (other.patch.equals("") && this.patchInt() == 3) {
            return 1;
        } else if (this.patch.equals("")) {
            return 1;
        } else if (other.patch.equals("")) {
            return -1;
        } else {
            int result = Integer.compare(this.patchInt(), other.patchInt());
            if (result == 0) {
                return other.patchSplit().length > 1 && this.patchSplit().length > 1 ? Integer.compare(this.patchVersion(), other.patchVersion()) : Integer.compare(this.patchSplit().length, other.patchSplit().length);
            } else {
                return result;
            }
        }
    }

    private int patchVersion() {
        return Integer.parseInt(this.patchSplit()[1]);
    }

    private String patchText() {
        return this.patchSplit()[0];
    }

    private String[] patchSplit() {
        return this.patch.replace(".jar", "").split("(?<=\\D)(?=\\d)");
    }

    private int patchInt() {
        String var1 = this.patchText();
        switch(var1) {
            case "a":
            case "alpha":
                return 0;
            case "b":
            case "beta":
                return 1;
            case "rc":
                return 2;
            case "p":
                return 3;
            case "dev":
                return 4;
            default:
                return -1;
        }
    }

    public String toJson() {
        JsonObject version = new JsonObject();
        JsonObject node = new JsonObject();
        node.addProperty("full", this.toString());
        node.addProperty("major", this.major);
        node.addProperty("minor", this.minor);
        node.addProperty("micro", this.micro);
        node.addProperty("patch", this.isRelease() ? null : this.patch);
        version.add("journeymap_version", node);
        version.addProperty("loader_version", Journeymap.LOADER_VERSION);
        version.addProperty("loader", Journeymap.LOADER_NAME);
        version.addProperty("minecraft_version", Journeymap.MC_VERSION);
        return version.toString();
    }

    public static Version fromJson(String string) {
        JsonObject node = (JsonObject) JsonParser.parseString(string);
        JsonObject versions = node.get("journeymap_version").getAsJsonObject();
        String full = versions.get("full").getAsString();
        Version version = from(full, null);
        version.loaderVersion = node.get("loader_version").getAsString();
        version.loader = node.get("loader").getAsString();
        version.minecraftVersion = node.get("minecraft_version").getAsString();
        return version;
    }

    public boolean isValid(Version min, Side side) {
        if (Journeymap.DEV_VERSION.equals(this)) {
            Journeymap.getLogger().info("Dev Mode? " + this + " " + Journeymap.JM_VERSION);
            return true;
        } else if (min.isNewerThan(this)) {
            Journeymap.getLogger().info("Version Mismatch need at least " + min + " or higher. Current " + side.opposite() + " version attempt -> " + this);
            return false;
        } else {
            return true;
        }
    }
}