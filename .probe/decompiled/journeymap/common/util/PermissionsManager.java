package journeymap.common.util;

import journeymap.common.CommonConstants;
import journeymap.common.Journeymap;
import journeymap.common.LoaderHooks;
import journeymap.common.config.AdminAccessConfig;
import journeymap.common.helper.DimensionHelper;
import journeymap.common.nbt.WorldIdData;
import journeymap.common.properties.DimensionProperties;
import journeymap.common.properties.GlobalProperties;
import journeymap.common.properties.PermissionProperties;
import journeymap.common.properties.PropertiesManager;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

public class PermissionsManager {

    private static PermissionsManager INSTANCE;

    public static PermissionsManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PermissionsManager();
        }
        return INSTANCE;
    }

    public void sendPermissions(ServerPlayer playerEntity) {
        GlobalProperties permissions = this.getPlayerConfig(playerEntity);
        if (permissions != null) {
            Journeymap.getInstance().getDispatcher().sendClientPermissions(playerEntity, permissions.toJsonString(false), this.canServerAdmin(playerEntity));
        }
    }

    private GlobalProperties getPlayerConfig(ServerPlayer player) {
        if (LoaderHooks.isDedicatedServer() || Minecraft.getInstance().getSingleplayerServer() != null && Minecraft.getInstance().getSingleplayerServer().isPublished()) {
            boolean useWorldId = PropertiesManager.getInstance().getGlobalProperties().useWorldId.get();
            String worldId = useWorldId ? WorldIdData.getWorldId() : null;
            Journeymap.getInstance().getDispatcher().sendWorldIdPacket(player, worldId);
        }
        return this.buildPermissions(player);
    }

    public boolean canServerAdmin(ServerPlayer player) {
        if (player != null) {
            for (String admin : AdminAccessConfig.getInstance().getAdmins()) {
                if (player.m_20148_().toString().equals(admin) || player.m_7755_().getString().equalsIgnoreCase(admin) || CommonConstants.debugOverride(player)) {
                    return true;
                }
            }
            if (Journeymap.isOp(player)) {
                return AdminAccessConfig.getInstance().getOpAccess() || CommonConstants.debugOverride(player);
            }
        }
        return false;
    }

    private boolean canTeleport(ServerPlayer player) {
        ResourceKey<Level> playerDim = DimensionHelper.getDimension(player);
        if (PropertiesManager.getInstance().getDimProperties(playerDim).enabled.get()) {
            return PropertiesManager.getInstance().getDimProperties(playerDim).teleportEnabled.get();
        } else {
            return PropertiesManager.getInstance().getGlobalProperties().teleportEnabled.get() ? true : Journeymap.isOp(player);
        }
    }

    private GlobalProperties buildPermissions(ServerPlayer player) {
        PermissionProperties prop = PropertiesManager.getInstance().getDimProperties(player.m_9236_().dimension());
        GlobalProperties globalProp = PropertiesManager.getInstance().getGlobalProperties();
        boolean isOp = Journeymap.isOp(player);
        if (!((DimensionProperties) prop).enabled.get()) {
            prop = globalProp;
        }
        GlobalProperties to = new GlobalProperties().loadForClient(prop.toJsonString(true), true);
        to.journeymapEnabled.set(globalProp.journeymapEnabled.get() || isOp || CommonConstants.debugOverride(player));
        to.worldPlayerRadar.set(globalProp.worldPlayerRadar.get().enabled(isOp));
        to.allowDeathPoints.set(globalProp.allowDeathPoints.get());
        to.allowWaypoints.set(globalProp.allowWaypoints.get());
        to.showInGameBeacons.set(globalProp.showInGameBeacons.get());
        to.allowMultiplayerSettings.set(globalProp.allowMultiplayerSettings.get());
        to.viewOnlyServerProperties.set(globalProp.viewOnlyServerProperties.get());
        to.teleportEnabled.set(this.canTeleport(player));
        to.radarEnabled.set(prop.radarEnabled.get().enabled(isOp));
        to.surfaceMapping.set(prop.surfaceMapping.get().enabled(isOp));
        to.topoMapping.set(prop.topoMapping.get().enabled(isOp));
        to.caveMapping.set(prop.caveMapping.get().enabled(isOp));
        to.renderRange.set(prop.renderRange.get());
        return to;
    }
}