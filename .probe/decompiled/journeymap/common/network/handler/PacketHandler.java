package journeymap.common.network.handler;

import journeymap.client.Constants;
import journeymap.common.Journeymap;
import journeymap.common.LoaderHooks;
import journeymap.common.helper.DimensionHelper;
import journeymap.common.nbt.PlayerData;
import journeymap.common.nbt.WorldIdData;
import journeymap.common.network.data.ServerPropertyType;
import journeymap.common.network.data.model.Location;
import journeymap.common.properties.DimensionProperties;
import journeymap.common.properties.GlobalProperties;
import journeymap.common.properties.MultiplayerProperties;
import journeymap.common.properties.PropertiesBase;
import journeymap.common.properties.PropertiesManager;
import journeymap.common.util.JourneyMapTeleport;
import journeymap.common.util.PermissionsManager;
import net.minecraft.server.level.ServerPlayer;

public class PacketHandler {

    public void handleTeleportPacket(ServerPlayer player, Location location) {
        JourneyMapTeleport.instance().attemptTeleport(player, location);
    }

    public void onClientPermsRequest(ServerPlayer sender) {
        PermissionsManager.getInstance().sendPermissions(sender);
    }

    public void onAdminScreenOpen(ServerPlayer sender, int type, String dimension) {
        PropertiesManager pm = PropertiesManager.getInstance();
        if (!PermissionsManager.getInstance().canServerAdmin(sender) && !LoaderHooks.isClient() && !pm.getGlobalProperties().viewOnlyServerProperties.get()) {
            sender.sendSystemMessage(Constants.getStringTextComponent("You do not have permission to modify Journeymap's server options!"));
        } else {
            Journeymap.getInstance().getDispatcher().sendServerAdminPacket(sender, type, switch(ServerPropertyType.getFromType(type)) {
                case GLOBAL ->
                    pm.getGlobalProperties().toJsonString(false);
                case DEFAULT ->
                    pm.getDefaultDimensionProperties().toJsonString(false);
                default ->
                    pm.getDimProperties(DimensionHelper.getWorldKeyForName(dimension)).toJsonString(false);
            }, dimension);
        }
    }

    public void onMultiplayerOptionsOpen(ServerPlayer sender) {
        GlobalProperties globalProperties = PropertiesManager.getInstance().getGlobalProperties();
        if (globalProperties.allowMultiplayerSettings.get().hasOption(Journeymap.isOp(sender))) {
            PlayerData.Player player = PlayerData.getPlayerData().getPlayer(sender);
            MultiplayerProperties properties = new MultiplayerProperties();
            properties.visible.set(player.isVisible());
            properties.hideSelfUnderground.set(player.isHiddenUnderground());
            Journeymap.getInstance().getDispatcher().sendMultiplayerOptionsPacket(sender, properties.toJsonString(false));
        }
    }

    public void onMultiplayerOptionsSave(ServerPlayer sender, String payload) {
        GlobalProperties globalProperties = PropertiesManager.getInstance().getGlobalProperties();
        if (globalProperties.allowMultiplayerSettings.get().hasOption(Journeymap.isOp(sender))) {
            PlayerData.Player player = PlayerData.getPlayerData().getPlayer(sender);
            MultiplayerProperties properties = new MultiplayerProperties();
            properties.load(payload, false);
            player.setHiddenUnderground(properties.hideSelfUnderground.get());
            player.setVisible(properties.visible.get());
        }
    }

    public void onServerAdminSave(ServerPlayer sender, Integer type, String payload, String dimension) {
        if (!PermissionsManager.getInstance().canServerAdmin(sender) && !LoaderHooks.isClient()) {
            sender.sendSystemMessage(Constants.getStringTextComponent("You do not have permission to modify Journeymap's server options!"));
        } else {
            switch(ServerPropertyType.getFromType(type)) {
                case GLOBAL:
                    PropertiesManager.getInstance().getGlobalProperties().<PropertiesBase>load(payload, false).save();
                    updatePlayers("global");
                    break;
                case DEFAULT:
                    PropertiesManager.getInstance().getDefaultDimensionProperties().<PropertiesBase>load(payload, false).save();
                    break;
                case DIMENSION:
                default:
                    DimensionProperties prop = PropertiesManager.getInstance().getDimProperties(DimensionHelper.getWorldKeyForName(dimension)).load(payload, true);
                    prop.save();
                    if (prop.enabled.get()) {
                        updatePlayers(dimension);
                    }
            }
            PropertiesManager.getInstance().reloadConfigs();
        }
    }

    private static void updatePlayers(String dim) {
        for (ServerPlayer player : LoaderHooks.getServer().getPlayerList().getPlayers()) {
            if (dim.equals(DimensionHelper.getDimKeyName(player)) || "global".equals(dim)) {
                PermissionsManager.getInstance().sendPermissions(player);
            }
        }
    }

    public void onWorldIdRequest(ServerPlayer sender) {
        if (PropertiesManager.getInstance().getGlobalProperties().useWorldId.get()) {
            String worldId = WorldIdData.getWorldId();
            Journeymap.getInstance().getDispatcher().sendWorldIdPacket(sender, worldId);
        }
    }
}