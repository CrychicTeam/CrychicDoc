package net.minecraft.server;

import net.minecraft.core.LayeredRegistryAccess;
import net.minecraft.server.packs.resources.CloseableResourceManager;
import net.minecraft.world.level.storage.WorldData;

public record WorldStem(CloseableResourceManager f_206892_, ReloadableServerResources f_206893_, LayeredRegistryAccess<RegistryLayer> f_244542_, WorldData f_206895_) implements AutoCloseable {

    private final CloseableResourceManager resourceManager;

    private final ReloadableServerResources dataPackResources;

    private final LayeredRegistryAccess<RegistryLayer> registries;

    private final WorldData worldData;

    public WorldStem(CloseableResourceManager f_206892_, ReloadableServerResources f_206893_, LayeredRegistryAccess<RegistryLayer> f_244542_, WorldData f_206895_) {
        this.resourceManager = f_206892_;
        this.dataPackResources = f_206893_;
        this.registries = f_244542_;
        this.worldData = f_206895_;
    }

    public void close() {
        this.resourceManager.close();
    }
}