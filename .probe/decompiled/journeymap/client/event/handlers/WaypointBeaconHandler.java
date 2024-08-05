package journeymap.client.event.handlers;

import com.mojang.blaze3d.vertex.PoseStack;
import journeymap.client.JourneymapClient;
import journeymap.client.render.ingame.WaypointBeaconRenderer;
import journeymap.client.render.ingame.WaypointDecorationRenderer;
import journeymap.client.render.ingame.WaypointRenderer;
import net.minecraft.client.Minecraft;

public class WaypointBeaconHandler {

    final Minecraft mc = Minecraft.getInstance();

    final WaypointRenderer beaconRenderer = new WaypointBeaconRenderer();

    final WaypointRenderer decorationRenderer = new WaypointDecorationRenderer();

    public void onRenderWaypoints(PoseStack poseStack, boolean shaderBeacon) {
        boolean onlyBeacon = JourneymapClient.getInstance().getWaypointProperties().shaderBeacon.get() && shaderBeacon;
        boolean skipBeacon = JourneymapClient.getInstance().getWaypointProperties().shaderBeacon.get() && !shaderBeacon;
        if (this.mc.player != null && !this.mc.options.hideGui) {
            this.mc.getProfiler().push("journeymap");
            if (onlyBeacon || !skipBeacon) {
                this.mc.getProfiler().push("waypoint_beacons");
                this.beaconRenderer.render(poseStack);
                this.mc.getProfiler().pop();
            }
            if (!onlyBeacon) {
                this.mc.getProfiler().push("waypoint_decorations");
                this.decorationRenderer.render(poseStack);
                this.mc.getProfiler().pop();
            }
            this.mc.getProfiler().pop();
        }
    }

    public void onRenderWaypoints(PoseStack poseStack) {
        this.onRenderWaypoints(poseStack, false);
    }
}