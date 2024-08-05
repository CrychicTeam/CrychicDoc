package eu.midnightdust.lib.util;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import eu.midnightdust.lib.util.forge.PlatformFunctionsImpl;
import java.nio.file.Path;
import net.minecraft.commands.CommandSourceStack;

public class PlatformFunctions {

    @ExpectPlatform
    @Transformed
    public static Path getConfigDirectory() {
        return PlatformFunctionsImpl.getConfigDirectory();
    }

    @ExpectPlatform
    @Transformed
    public static boolean isClientEnv() {
        return PlatformFunctionsImpl.isClientEnv();
    }

    @ExpectPlatform
    @Transformed
    public static boolean isModLoaded(String modid) {
        return PlatformFunctionsImpl.isModLoaded(modid);
    }

    @ExpectPlatform
    @Transformed
    public static void registerCommand(LiteralArgumentBuilder<CommandSourceStack> command) {
        PlatformFunctionsImpl.registerCommand(command);
    }
}