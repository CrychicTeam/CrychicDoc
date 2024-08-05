package dev.ftb.mods.ftblibrary.integration.permissions;

import dev.ftb.mods.ftblibrary.FTBLibrary;
import java.util.Objects;

public enum PermissionHelper {

    INSTANCE;

    private static final PermissionProvider FALLBACK_PROVIDER = new FallbackPermissionProvider();

    private PermissionProvider activeImpl = null;

    public PermissionHelper getInstance() {
        return INSTANCE;
    }

    public void setProviderImpl(PermissionProvider newProvider) {
        if (this.activeImpl != null) {
            FTBLibrary.LOGGER.warn("Overriding existing permissions provider: {} -> {}", this.activeImpl.getName(), newProvider.getName());
        } else {
            FTBLibrary.LOGGER.info("Setting permissions provider implementation to: {}", newProvider.getName());
        }
        this.activeImpl = newProvider;
    }

    public PermissionProvider getProvider() {
        return (PermissionProvider) Objects.requireNonNullElse(this.activeImpl, FALLBACK_PROVIDER);
    }
}