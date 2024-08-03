package me.jellysquid.mods.sodium.client.compatibility.checks;

class Configuration {

    public static final boolean WIN32_RTSS_HOOKS = configureCheck("win32.rtss", true);

    public static final boolean WIN32_DRIVER_INTEL_GEN7 = configureCheck("win32.intelGen7", true);

    private static boolean configureCheck(String name, boolean defaultValue) {
        String propertyValue = System.getProperty(getPropertyKey(name), null);
        return propertyValue == null ? defaultValue : Boolean.parseBoolean(propertyValue);
    }

    private static String getPropertyKey(String name) {
        return "sodium.checks." + name;
    }
}