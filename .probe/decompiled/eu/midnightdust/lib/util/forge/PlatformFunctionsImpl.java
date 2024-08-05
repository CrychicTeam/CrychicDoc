package eu.midnightdust.lib.util.forge;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.nio.file.Path;
import net.minecraft.commands.CommandSourceStack;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLPaths;

public class PlatformFunctionsImpl {

    public static Path getConfigDirectory() {
        return FMLPaths.CONFIGDIR.get();
    }

    public static boolean isClientEnv() {
        return FMLEnvironment.dist.isClient();
    }

    public static boolean isModLoaded(String modid) {
        return ModList.get().isLoaded(modid);
    }

    public static void registerCommand(LiteralArgumentBuilder<CommandSourceStack> command) {
    }
}