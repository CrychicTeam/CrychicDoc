package net.cristellib.forge;

import com.mojang.datafixers.util.Pair;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;
import net.cristellib.CristelLib;
import net.cristellib.CristelLibRegistry;
import net.cristellib.StructureConfig;
import net.cristellib.api.CristelLibAPI;
import net.cristellib.data.ReadData;
import net.cristellib.forge.extraapiutil.APIFinder;
import net.cristellib.util.Platform;
import net.cristellib.util.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PathPackResources;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.loading.LoadingModList;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;
import net.minecraftforge.forgespi.language.IModInfo;
import net.minecraftforge.forgespi.locating.IModFile;

public class CristelLibExpectPlatformImpl {

    public static Path getConfigDirectory() {
        return FMLPaths.CONFIGDIR.get();
    }

    public static PackResources registerBuiltinResourcePack(ResourceLocation id, Component displayName, String modid) {
        Path path = getResourceDirectory(modid, id.getPath());
        return path != null ? new PathPackResources(displayName.getString(), path, true) : null;
    }

    @Nullable
    public static Path getResourceDirectory(String modId, String subPath) {
        ModList modList = ModList.get();
        IModFile file;
        if (modList == null) {
            ModInfo info = ModLoadingUtilImpl.getPreLoadedModInfo(modId);
            if (info == null) {
                CristelLib.LOGGER.warn("Mod info for modId:" + modId + " is null");
                return null;
            }
            file = info.getOwningFile().getFile();
        } else {
            ModContainer container = (ModContainer) modList.getModContainerById(modId).orElse(null);
            if (container == null) {
                CristelLib.LOGGER.warn("Mod container for modId:" + modId + " is null");
                return null;
            }
            file = container.getModInfo().getOwningFile().getFile();
        }
        Path path = file.findResource(new String[] { subPath });
        if (path == null) {
            CristelLib.LOGGER.warn("Path for subPath: " + subPath + " in modId: " + modId + " is null");
        }
        return path;
    }

    public static Map<String, Set<StructureConfig>> getConfigs(CristelLibRegistry registry) {
        Map<String, Set<StructureConfig>> configs = new HashMap();
        for (Pair<List<String>, CristelLibAPI> apiPair : APIFinder.scanForAPIs()) {
            CristelLibAPI api = (CristelLibAPI) apiPair.getSecond();
            List<String> modIds = (List<String>) apiPair.getFirst();
            Set<StructureConfig> set = new HashSet();
            api.registerConfigs(set);
            configs.put((String) modIds.get(0), set);
            api.registerStructureSets(registry);
        }
        Util.addAll(configs, data(registry));
        return configs;
    }

    public static Map<String, Set<StructureConfig>> data(CristelLibRegistry registry) {
        Map<String, Set<StructureConfig>> modidAndConfigs = new HashMap();
        for (String modid : getModIds()) {
            ReadData.getBuiltInPacks(modid);
            ReadData.copyFile(modid);
            ReadData.getStructureConfigs(modid, modidAndConfigs, registry);
        }
        return modidAndConfigs;
    }

    public static List<String> getModIds() {
        ModList modList = ModList.get();
        List<String> modIds = new ArrayList();
        if (modList != null) {
            for (IModInfo modInfo : modList.getMods()) {
                modIds.add(modInfo.getModId());
            }
        } else {
            for (IModInfo modInfo : LoadingModList.get().getMods()) {
                modIds.add(modInfo.getModId());
            }
        }
        return modIds;
    }

    public static List<Path> getRootPaths(String modId) {
        ModList modList = ModList.get();
        List<Path> paths = new ArrayList();
        IModFile file;
        if (modList == null) {
            ModInfo info = ModLoadingUtilImpl.getPreLoadedModInfo(modId);
            if (info == null) {
                return paths;
            }
            file = info.getOwningFile().getFile();
        } else {
            ModContainer container = (ModContainer) modList.getModContainerById(modId).orElse(null);
            if (container == null) {
                return paths;
            }
            file = container.getModInfo().getOwningFile().getFile();
        }
        return Collections.singletonList(file.getSecureJar().getRootPath());
    }

    public static Platform getPlatform() {
        return Platform.FORGE;
    }
}