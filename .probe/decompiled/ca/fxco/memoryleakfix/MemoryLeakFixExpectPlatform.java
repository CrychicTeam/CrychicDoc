package ca.fxco.memoryleakfix;

import ca.fxco.memoryleakfix.forge.MemoryLeakFixExpectPlatformImpl;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;

public class MemoryLeakFixExpectPlatform {

    @ExpectPlatform
    @Transformed
    public static boolean isModLoaded(String id) {
        return MemoryLeakFixExpectPlatformImpl.isModLoaded(id);
    }

    @ExpectPlatform
    @Transformed
    public static int compareMinecraftToVersion(String version) {
        return MemoryLeakFixExpectPlatformImpl.compareMinecraftToVersion(version);
    }

    @ExpectPlatform
    @Transformed
    public static String getMappingType() {
        return MemoryLeakFixExpectPlatformImpl.getMappingType();
    }

    @ExpectPlatform
    @Transformed
    public static boolean isDevEnvironment() {
        return MemoryLeakFixExpectPlatformImpl.isDevEnvironment();
    }
}