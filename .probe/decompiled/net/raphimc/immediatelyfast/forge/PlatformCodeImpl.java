package net.raphimc.immediatelyfast.forge;

import java.nio.file.Path;
import java.util.Optional;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.loading.moddiscovery.ModFileInfo;
import net.raphimc.immediatelyfast.ImmediatelyFast;
import net.raphimc.immediatelyfast.PlatformCode;

public class PlatformCodeImpl {

    public static Path getConfigDirectory() {
        return FMLPaths.CONFIGDIR.get();
    }

    public static Optional<String> getModVersion(String mod) {
        return ModList.get() == null ? Optional.ofNullable(FMLLoader.getLoadingModList().getModFileById(mod)).map(ModFileInfo::versionString) : ModList.get().getModContainerById(mod).map(m -> m.getModInfo().getVersion().toString());
    }

    public static void checkModCompatibility() {
        if (!ImmediatelyFast.config.debug_only_and_not_recommended_disable_mod_conflict_handling) {
            PlatformCode.getModVersion("optifine").ifPresent(version -> {
                throw new IllegalStateException("Found OptiFine " + version + ". ImmediatelyFast is not compatible with OptiFine.");
            });
        }
    }
}