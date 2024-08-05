package journeymap.client.render.draw;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import journeymap.client.JourneymapClient;
import journeymap.client.data.DataCache;
import journeymap.client.render.map.GridRenderer;
import journeymap.client.waypoint.Waypoint;
import journeymap.common.Journeymap;
import journeymap.common.log.LogFormatter;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class WaypointDrawStepFactory {

    final List<DrawWayPointStep> drawStepList = new ArrayList();

    public List<DrawWayPointStep> prepareSteps(Collection<Waypoint> waypoints, GridRenderer grid, boolean checkDistance, boolean showLabel) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        int maxDistance = JourneymapClient.getInstance().getWaypointProperties().maxDistance.get();
        float waypointLabelScale = JourneymapClient.getInstance().getFullMapProperties().waypointLabelScale.get();
        float waypointIconScale = JourneymapClient.getInstance().getFullMapProperties().waypointIconScale.get();
        checkDistance = checkDistance && maxDistance > 0;
        Vec3 playerVec = checkDistance ? player.m_20182_() : null;
        this.drawStepList.clear();
        try {
            for (Waypoint waypoint : waypoints) {
                if (waypoint.isEnable() && waypoint.isInPlayerDimension()) {
                    if (checkDistance) {
                        double actualDistance = playerVec.distanceTo(waypoint.getPosition());
                        if (actualDistance > (double) maxDistance) {
                            continue;
                        }
                    }
                    DrawWayPointStep wayPointStep = DataCache.INSTANCE.getDrawWayPointStep(waypoint);
                    if (wayPointStep != null) {
                        this.drawStepList.add(wayPointStep);
                        wayPointStep.setShowLabel(showLabel);
                        wayPointStep.setLabelScale(waypointLabelScale);
                        wayPointStep.setIconScale(waypointIconScale);
                    }
                }
            }
        } catch (Throwable var15) {
            Journeymap.getLogger().error("Error during prepareSteps: " + LogFormatter.toString(var15));
        }
        return this.drawStepList;
    }
}