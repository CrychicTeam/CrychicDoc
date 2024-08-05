package net.mehvahdjukaar.supplementaries.integration;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import net.mehvahdjukaar.supplementaries.integration.forge.TetraCompatImpl;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Contract;

public class TetraCompat {

    @Contract
    @ExpectPlatform
    @Transformed
    public static boolean isTetraSword(Item i) {
        return TetraCompatImpl.isTetraSword(i);
    }

    @Contract
    @ExpectPlatform
    @Transformed
    public static boolean isTetraTool(Item i) {
        return TetraCompatImpl.isTetraTool(i);
    }
}