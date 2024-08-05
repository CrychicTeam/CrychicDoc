package net.raphimc.immediatelyfast;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import java.nio.file.Path;
import java.util.Optional;
import net.raphimc.immediatelyfast.forge.PlatformCodeImpl;

public class PlatformCode {

    @ExpectPlatform
    @Transformed
    public static Path getConfigDirectory() {
        return PlatformCodeImpl.getConfigDirectory();
    }

    @ExpectPlatform
    @Transformed
    public static Optional<String> getModVersion(String mod) {
        return PlatformCodeImpl.getModVersion(mod);
    }

    @ExpectPlatform
    @Transformed
    public static void checkModCompatibility() {
        PlatformCodeImpl.checkModCompatibility();
    }
}