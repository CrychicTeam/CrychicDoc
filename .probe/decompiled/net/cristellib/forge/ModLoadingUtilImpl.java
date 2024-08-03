package net.cristellib.forge;

import javax.annotation.Nullable;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.LoadingModList;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;

public class ModLoadingUtilImpl {

    public static boolean isModLoaded(String modid) {
        ModList modList = ModList.get();
        return modList != null ? modList.isLoaded(modid) : isModPreLoaded(modid);
    }

    public static boolean isModPreLoaded(String modid) {
        return getPreLoadedModInfo(modid) != null;
    }

    @Nullable
    public static ModInfo getPreLoadedModInfo(String modId) {
        for (ModInfo info : LoadingModList.get().getMods()) {
            if (info.getModId().equals(modId)) {
                return info;
            }
        }
        return null;
    }

    public static boolean isModLoadedWithVersion(String modid, String minVersion) {
        if (isModLoaded(modid)) {
            ModList modList = ModList.get();
            ArtifactVersion version;
            if (modList != null) {
                version = ((ModContainer) modList.getModContainerById(modid).get()).getModInfo().getVersion();
            } else {
                version = getPreLoadedModVersion(modid);
            }
            ArtifactVersion min = new DefaultArtifactVersion(minVersion);
            return version.compareTo(min) >= 0;
        } else {
            return false;
        }
    }

    public static ArtifactVersion getPreLoadedModVersion(String modid) {
        ModInfo info = getPreLoadedModInfo(modid);
        if (info == null) {
            throw new RuntimeException("Couldn't find mod: " + modid);
        } else {
            return info.getVersion();
        }
    }
}