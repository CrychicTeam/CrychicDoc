package net.blay09.mods.craftingtweaks.registry;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.craftingtweaks.api.CraftingGridProvider;
import net.blay09.mods.craftingtweaks.api.CraftingTweaksAPI;
import net.blay09.mods.craftingtweaks.config.CraftingTweaksConfig;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonCompatLoader implements ResourceManagerReloadListener {

    private static final Logger logger = LoggerFactory.getLogger(JsonCompatLoader.class);

    private static final Gson gson = new Gson();

    private static final FileToIdConverter COMPAT_JSONS = FileToIdConverter.json("craftingtweaks_compat");

    private final List<CraftingGridProvider> providersFromDataPacks = new ArrayList();

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        for (CraftingGridProvider providersFromDataPack : this.providersFromDataPacks) {
            CraftingTweaksAPI.unregisterCraftingGridProvider(providersFromDataPack);
        }
        this.providersFromDataPacks.clear();
        for (Entry<ResourceLocation, Resource> entry : COMPAT_JSONS.listMatchingResources(resourceManager).entrySet()) {
            try {
                BufferedReader reader = ((Resource) entry.getValue()).openAsReader();
                try {
                    CraftingGridProvider gridProvider = load((CraftingTweaksRegistrationData) gson.fromJson(reader, CraftingTweaksRegistrationData.class));
                    if (gridProvider != null) {
                        this.providersFromDataPacks.add(gridProvider);
                    }
                } catch (Throwable var8) {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (Throwable var7) {
                            var8.addSuppressed(var7);
                        }
                    }
                    throw var8;
                }
                if (reader != null) {
                    reader.close();
                }
            } catch (Exception var9) {
                logger.error("Parsing error loading CraftingTweaks data file at {}", entry.getKey(), var9);
            }
        }
    }

    private static boolean isCompatEnabled(String modId) {
        return !CraftingTweaksConfig.getActive().client.disabledAddons.contains(modId);
    }

    private static CraftingGridProvider load(CraftingTweaksRegistrationData data) {
        String modId = data.getModId();
        if ((modId.equals("minecraft") || Balm.isModLoaded(modId)) && isCompatEnabled(modId) && data.isEnabled()) {
            CraftingGridProvider gridProvider = DataDrivenGridFactory.createGridProvider(data);
            if (gridProvider != null) {
                CraftingTweaksAPI.registerCraftingGridProvider(gridProvider);
                logger.info("{} has registered {} for CraftingTweaks via data pack", data.getModId(), data.getContainerClass());
            }
            return gridProvider;
        } else {
            return null;
        }
    }
}