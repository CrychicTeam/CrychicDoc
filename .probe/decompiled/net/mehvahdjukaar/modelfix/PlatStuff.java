package net.mehvahdjukaar.modelfix;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import net.mehvahdjukaar.modelfix.forge.PlatStuffImpl;

public class PlatStuff {

    @ExpectPlatform
    @Transformed
    public static boolean isModStateValid() {
        return PlatStuffImpl.isModStateValid();
    }
}