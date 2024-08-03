package journeymap.client.event.handlers;

import java.util.Collection;
import journeymap.client.Constants;
import journeymap.client.JourneymapClient;
import journeymap.client.api.display.ModPopupMenu;
import journeymap.client.api.model.IFullscreen;
import journeymap.client.command.CmdTeleportWaypoint;
import journeymap.client.ui.UIManager;
import journeymap.client.ui.fullscreen.Fullscreen;
import journeymap.client.waypoint.Waypoint;
import journeymap.client.waypoint.WaypointStore;
import journeymap.common.Journeymap;
import journeymap.common.nbt.RegionData;
import journeymap.common.nbt.RegionDataStorageHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;

public class PopupMenuEventHandler {

    public void onFullscreenPopupMenu(ModPopupMenu menu, Fullscreen fullscreen) {
        try {
            if (CmdTeleportWaypoint.isPermitted(Minecraft.getInstance())) {
                menu.addMenuItem(Constants.getString("jm.waypoint.teleport"), this.doTeleport(fullscreen));
            }
            if (JourneymapClient.getInstance().getStateHandler().isWaypointsAllowed()) {
                menu.createSubItemList(Constants.getString("jm.waypoint.waypoints")).addMenuItem(Constants.getString("key.journeymap.create_waypoint"), this.createWaypoint(fullscreen, false)).addMenuItem(Constants.getString("jm.waypoint.create_temp_waypoint"), this.createWaypoint(fullscreen, true)).addMenuItem(Constants.getString("jm.waypoint.show_all"), b -> WaypointStore.INSTANCE.getAll().forEach(wp -> wp.setEnable(true).setDirty())).addMenuItem(Constants.getString("jm.waypoint.hide_all"), b -> WaypointStore.INSTANCE.getAll().forEach(wp -> wp.setEnable(false).setDirty()));
            }
            menu.addMenuItem(Constants.getString("key.journeymap.fullscreen.menu.chat_position"), this.chatAtPos(fullscreen));
        } catch (Exception var4) {
            Journeymap.getLogger().error("Error handling PopupMenuEvent.FullscreenPopupMenuEvent", var4);
        }
    }

    public void onWaypointPopupMenu(ModPopupMenu menu, String waypointId, Fullscreen fullscreen) {
        try {
            if (JourneymapClient.getInstance().getStateHandler().isWaypointsAllowed()) {
                Collection<Waypoint> waypoints = WaypointStore.INSTANCE.getAll();
                Waypoint waypoint = (Waypoint) waypoints.stream().filter(wp -> waypointId.equals(wp.getId())).findAny().orElse(null);
                if (waypoint != null) {
                    if (CmdTeleportWaypoint.isPermitted(Minecraft.getInstance()) && CmdTeleportWaypoint.isPermitted(Minecraft.getInstance())) {
                        menu.addMenuItem(Constants.getString("jm.waypoint.teleport"), blockPos -> new CmdTeleportWaypoint(waypoint).run());
                    }
                    menu.addMenuItem(Constants.getString("jm.waypoint.edit"), blockPos -> UIManager.INSTANCE.openWaypointEditor(waypoint, false, fullscreen));
                    menu.addMenuItem(Constants.getString("jm.waypoint.disable"), blockPos -> {
                        waypoint.setEnable(false).setDirty();
                        WaypointStore.INSTANCE.save(waypoint, false);
                    });
                    menu.addMenuItem(Constants.getString("jm.waypoint.remove"), blockPos -> WaypointStore.INSTANCE.remove(waypoint, true));
                }
            }
        } catch (Exception var6) {
            Journeymap.getLogger().error("Error handling PopupMenuEvent.FullscreenPopupMenuEvent", var6);
        }
    }

    private ModPopupMenu.Action chatAtPos(IFullscreen fullscreen) {
        return blockPos -> {
            Waypoint waypoint = Waypoint.at(blockPos, Waypoint.Type.Normal, fullscreen.getMinecraft().player.m_9236_().dimension().location().toString());
            ((Fullscreen) fullscreen).chatOpenedFromEvent = true;
            ((Fullscreen) fullscreen).openChat(waypoint.toChatString());
        };
    }

    private ModPopupMenu.Action createWaypoint(IFullscreen fullscreen, boolean temp) {
        return blockPos -> {
            int y = blockPos.m_123342_();
            RegionData regionData = RegionDataStorageHandler.getInstance().getRegionDataAsyncNoCache(blockPos, ((Fullscreen) fullscreen).getMapType());
            if (regionData != null) {
                regionData.getTopY(blockPos);
            }
            BlockPos pos = new BlockPos(blockPos.m_123341_(), y + 1, blockPos.m_123343_());
            Waypoint waypoint = Waypoint.at(pos, Waypoint.Type.Normal, fullscreen.getMinecraft().player.m_9236_().dimension().location().toString());
            if (temp) {
                waypoint.setOrigin("temp");
                waypoint.setName(Constants.getString("jm.waypoint.temp") + waypoint.getName());
                waypoint.updateId();
                WaypointStore.INSTANCE.save(waypoint, true);
            } else {
                UIManager.INSTANCE.openWaypointEditor(waypoint, true, (Fullscreen) fullscreen);
            }
        };
    }

    private ModPopupMenu.Action doTeleport(IFullscreen fullscreen) {
        return blockPos -> {
            int y = blockPos.m_123342_();
            RegionData regionData = RegionDataStorageHandler.getInstance().getRegionDataAsyncNoCache(blockPos, ((Fullscreen) fullscreen).getMapType());
            if (regionData != null) {
                y = regionData.getTopY(blockPos);
            }
            BlockPos pos = new BlockPos(blockPos.m_123341_(), y + 1, blockPos.m_123343_());
            CmdTeleportWaypoint.teleport(pos, Minecraft.getInstance().level.m_46472_());
        };
    }
}