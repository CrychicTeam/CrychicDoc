package dev.ftb.mods.ftbxmodcompat.generic.permissions;

import dev.ftb.mods.ftblibrary.integration.permissions.PermissionHelper;
import dev.ftb.mods.ftbxmodcompat.FTBXModCompat;
import dev.ftb.mods.ftbxmodcompat.config.FTBXModConfig;
import org.jetbrains.annotations.NotNull;

public class PermissionsSetup {

    public static void init() {
        FTBXModConfig.PermSelector sel = FTBXModConfig.PERMISSION_SELECTOR.get();
        if (sel != FTBXModConfig.PermSelector.DEFAULT && !sel.isUsable()) {
            FTBXModCompat.LOGGER.error("Permissions implementation {} isn't available, falling back to default", sel);
            sel = FTBXModConfig.PermSelector.DEFAULT;
        }
        PermissionHelper pHelper = setupPermissionHelper(sel);
        FTBXModCompat.LOGGER.info("Chose [{}] as the active permissions implementation", pHelper.getProvider().getName());
    }

    @NotNull
    private static PermissionHelper setupPermissionHelper(FTBXModConfig.PermSelector sel) {
        PermissionHelper pHelper = PermissionHelper.INSTANCE;
        switch(sel) {
            case LUCKPERMS:
                pHelper.setProviderImpl(new LuckPermsProvider());
                break;
            case FTB_RANKS:
                pHelper.setProviderImpl(new FTBRanksProvider());
                break;
            case DEFAULT:
                if (FTBXModCompat.isFTBRanksLoaded) {
                    pHelper.setProviderImpl(new FTBRanksProvider());
                } else if (FTBXModCompat.isLuckPermsLoaded) {
                    pHelper.setProviderImpl(new LuckPermsProvider());
                }
        }
        return pHelper;
    }
}