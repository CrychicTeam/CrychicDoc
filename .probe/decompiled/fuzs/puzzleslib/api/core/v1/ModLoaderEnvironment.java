package fuzs.puzzleslib.api.core.v1;

import fuzs.puzzleslib.impl.core.ModContext;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;

public interface ModLoaderEnvironment {

    ModLoaderEnvironment INSTANCE = ServiceProviderHelper.load(ModLoaderEnvironment.class);

    ModLoader getModLoader();

    default boolean isForge() {
        return this.getModLoader().isForge();
    }

    default boolean isFabric() {
        return this.getModLoader().isFabric();
    }

    default boolean isQuilt() {
        return this.getModLoader().isQuilt();
    }

    @Deprecated(forRemoval = true)
    default DistType getEnvironmentType() {
        return this.isClient() ? DistType.CLIENT : DistType.SERVER;
    }

    @Deprecated(forRemoval = true)
    default boolean isEnvironmentType(DistType envType) {
        return this.getEnvironmentType() == envType;
    }

    boolean isClient();

    boolean isServer();

    Path getGameDirectory();

    Path getModsDirectory();

    Path getConfigDirectory();

    boolean isDevelopmentEnvironment();

    Map<String, ModContainer> getModList();

    default boolean isModLoaded(String modId) {
        return this.getModList().containsKey(modId);
    }

    default Optional<ModContainer> getModContainer(String modId) {
        return this.isModLoaded(modId) ? Optional.of((ModContainer) this.getModList().get(modId)) : Optional.empty();
    }

    default boolean isModPresentServerside(String modId) {
        return ModContext.isPresentServerside(modId);
    }

    @Deprecated(forRemoval = true)
    default boolean isModLoadedSafe(String modId) {
        return this.isModLoaded(modId);
    }

    @Deprecated(forRemoval = true)
    default Optional<String> getModName(String modId) {
        return this.getModContainer(modId).map(ModContainer::getDisplayName);
    }

    @Deprecated(forRemoval = true)
    default Optional<Path> findModResource(String modId, String... path) {
        return this.getModContainer(modId).flatMap(t -> t.findResource(path));
    }

    ObjectShareAccess getObjectShareAccess();
}