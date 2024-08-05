package cristelknight.wwoo;

import cristelknight.wwoo.forge.EEExpectPlatformImpl;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;

public class EEExpectPlatform {

    @ExpectPlatform
    @Transformed
    public static boolean isNewer(String oldVersion, String newVersion) {
        return EEExpectPlatformImpl.isNewer(oldVersion, newVersion);
    }

    @ExpectPlatform
    @Transformed
    public static String getVersionForMod(String modId) {
        return EEExpectPlatformImpl.getVersionForMod(modId);
    }
}