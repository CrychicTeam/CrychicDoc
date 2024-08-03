package fuzs.puzzleslib.impl.core;

import com.google.common.base.Suppliers;
import fuzs.puzzleslib.api.core.v1.ModContainer;
import fuzs.puzzleslib.api.core.v1.ModLoader;
import fuzs.puzzleslib.api.core.v1.ModLoaderEnvironment;
import fuzs.puzzleslib.api.core.v1.ObjectShareAccess;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.forgespi.language.IModInfo;

public final class ForgeEnvironment implements ModLoaderEnvironment {

    private final Supplier<Map<String, ModContainer>> modList = Suppliers.memoize(() -> ModContainer.toModList(this::getForgeModContainers));

    private Stream<? extends ModContainer> getForgeModContainers() {
        Map<String, ForgeModContainer> allMods = (Map<String, ForgeModContainer>) getForgeModList().stream().map(ForgeModContainer::new).collect(Collectors.toMap(modContainerx -> modContainerx.getURI().getSchemeSpecificPart(), Function.identity(), (o1, o2) -> {
            o2.setParent(o1);
            return o1;
        }));
        for (ForgeModContainer modContainer : allMods.values()) {
            if (modContainer.getURI().getScheme().equals("union")) {
                String schemePart = getParentSchemePart(modContainer.getURI().getSchemeSpecificPart());
                modContainer.setParent((ForgeModContainer) allMods.get(schemePart));
            }
        }
        return allMods.values().stream();
    }

    private static List<? extends IModInfo> getForgeModList() {
        if (ModList.get() != null) {
            return ModList.get().getMods();
        } else if (FMLLoader.getLoadingModList() != null) {
            return FMLLoader.getLoadingModList().getMods();
        } else {
            throw new NullPointerException("mod list is null");
        }
    }

    private static String getParentSchemePart(String schemePart) {
        return schemePart.replace("/jij:file:///", "file:///").replaceAll("_/META-INF/.+(#|%23)\\d+!/$", "!/");
    }

    @Override
    public ModLoader getModLoader() {
        return ModLoader.FORGE;
    }

    @Override
    public boolean isClient() {
        return FMLEnvironment.dist.isClient();
    }

    @Override
    public boolean isServer() {
        return FMLEnvironment.dist.isDedicatedServer();
    }

    @Override
    public Path getGameDirectory() {
        return FMLPaths.GAMEDIR.get();
    }

    @Override
    public Path getModsDirectory() {
        return FMLPaths.MODSDIR.get();
    }

    @Override
    public Path getConfigDirectory() {
        return FMLPaths.CONFIGDIR.get();
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return !FMLEnvironment.production;
    }

    @Override
    public Map<String, ModContainer> getModList() {
        return (Map<String, ModContainer>) this.modList.get();
    }

    @Override
    public ObjectShareAccess getObjectShareAccess() {
        return ForgeObjectShareAccess.INSTANCE;
    }
}