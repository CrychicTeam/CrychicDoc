package journeymap.common.network.handler;

import journeymap.client.JourneymapClient;
import journeymap.client.texture.TextureCache;
import journeymap.client.ui.UIManager;
import journeymap.client.ui.dialog.MultiplayerOptionsManager;
import journeymap.client.ui.dialog.ServerOptionsManager;
import journeymap.client.ui.fullscreen.Fullscreen;
import journeymap.client.waypoint.Waypoint;
import journeymap.client.waypoint.WaypointStore;
import journeymap.common.Journeymap;
import journeymap.common.command.CreateWaypoint;
import journeymap.common.network.data.ServerPropertyType;
import journeymap.common.network.data.model.ClientState;
import journeymap.common.network.data.model.PlayerLocation;
import journeymap.common.util.PlayerRadarManager;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public class ClientPacketHandler {

    public void onClientStateUpdate(ClientState packet) {
        JourneymapClient.getInstance().getStateHandler().setStates(packet);
    }

    public void onServerAdminDataResponse(int type, String payload, String dimension) {
        ServerOptionsManager serverOptionsManager = UIManager.INSTANCE.getServerEditor();
        if (serverOptionsManager != null) {
            serverOptionsManager.setData(ServerPropertyType.getFromType(type), payload, dimension);
        }
    }

    public void onMultiplayerDataResponse(String payload) {
        MultiplayerOptionsManager multiplayerOptionsManager = UIManager.INSTANCE.getMultiplayerOptions();
        if (multiplayerOptionsManager != null) {
            multiplayerOptionsManager.setData(payload);
        }
    }

    public void onPlayerLocationPacket(PlayerLocation packet) {
        PlayerRadarManager.getInstance().updatePlayers(packet);
    }

    public void onWaypointCreatePacket(String waypoint, String action, boolean announce) {
        Component message = null;
        if ("create".equalsIgnoreCase(action)) {
            Waypoint clientWaypoint = Waypoint.fromString(waypoint);
            if ("waypoint-normal.png".equals(clientWaypoint.getIcon().getPath())) {
                clientWaypoint.setIcon(TextureCache.Waypoint);
            }
            if (Waypoint.Origin.EXTERNAL_FORCE.getValue().equals(clientWaypoint.getOrigin())) {
                clientWaypoint.setPersistent(false);
            }
            WaypointStore.INSTANCE.save(clientWaypoint, true);
            Fullscreen.state().requireRefresh();
            message = Component.translatable("jm.common.waypoint.create_packet", clientWaypoint.getPrettyName(), clientWaypoint.getX(), clientWaypoint.getY(), clientWaypoint.getZ(), clientWaypoint.getDimensions());
        } else {
            if (!"delete".equalsIgnoreCase(action)) {
                Journeymap.getLogger().warn("Error invalid action is being sent: \"{}\", \"create\" and \"delete\" are accepted values", action);
                return;
            }
            CreateWaypoint.CommandWaypoint commandWaypoint = CreateWaypoint.CommandWaypoint.fromString(waypoint);
            for (Waypoint wp : WaypointStore.INSTANCE.getAll()) {
                if (commandWaypoint.name.equalsIgnoreCase(wp.getName()) && wp.getOrigin().equals(commandWaypoint.origin) && validOrigin(commandWaypoint.origin)) {
                    WaypointStore.INSTANCE.remove(wp, true);
                    message = Component.translatable("jm.common.waypoint.delete_packet", wp.getPrettyName());
                    break;
                }
            }
        }
        if (announce && message != null) {
            Minecraft.getInstance().gui.getChat().addMessage(message);
        }
        Journeymap.getLogger().info(message.getString());
    }

    private static boolean validOrigin(String origin) {
        return Waypoint.Origin.getValues().contains(origin);
    }

    public void onWorldIdReceived(String worldId) {
        JourneymapClient.getInstance().setCurrentWorldId(worldId);
    }
}