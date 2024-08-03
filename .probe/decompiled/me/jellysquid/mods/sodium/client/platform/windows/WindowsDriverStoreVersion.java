package me.jellysquid.mods.sodium.client.platform.windows;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record WindowsDriverStoreVersion(int driverModel, int featureLevel, int major, int minor) {

    private static final Pattern PATTERN = Pattern.compile("^(?<driverModel>[0-9]{1,2})\\.(?<featureLevel>[0-9]{1,2})\\.(?<major>[0-9]{1,5})\\.(?<minor>[0-9]{1,5})$");

    public static WindowsDriverStoreVersion parse(String version) throws WindowsDriverStoreVersion.ParseException {
        Matcher matcher = PATTERN.matcher(version);
        if (!matcher.matches()) {
            throw new WindowsDriverStoreVersion.ParseException(version);
        } else {
            int driverModel = Integer.parseInt(matcher.group("driverModel"));
            int featureLevel = Integer.parseInt(matcher.group("featureLevel"));
            int major = Integer.parseInt(matcher.group("major"));
            int minor = Integer.parseInt(matcher.group("minor"));
            return new WindowsDriverStoreVersion(driverModel, featureLevel, major, minor);
        }
    }

    public String getFriendlyString() {
        return "%s.%s.%s.%s".formatted(this.driverModel, this.featureLevel, this.major, this.minor);
    }

    public static class ParseException extends Exception {

        private ParseException(String version) {
            super("Not a valid driver store version (%s)".formatted(version));
        }
    }
}