package journeymap.client.render.ingame;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Collection;
import journeymap.client.JourneymapClient;
import journeymap.client.cartography.color.RGB;
import journeymap.client.properties.WaypointProperties;
import journeymap.client.waypoint.Waypoint;
import journeymap.client.waypoint.WaypointStore;
import journeymap.common.Journeymap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.phys.Vec3;

public abstract class WaypointRenderer {

    protected WaypointProperties waypointProperties;

    private final Minecraft minecraft = Minecraft.getInstance();

    protected EntityRenderDispatcher renderManager = this.minecraft.getEntityRenderDispatcher();

    protected WaypointRenderer() {
    }

    public void render(PoseStack poseStack) {
        this.waypointProperties = JourneymapClient.getInstance().getWaypointProperties();
        Collection<Waypoint> waypoints = WaypointStore.INSTANCE.getAll();
        String playerDim = this.minecraft.player.m_9236_().dimension().location().toString();
        for (Waypoint waypoint : waypoints) {
            if (this.canDrawWaypoint(waypoint, playerDim) && (!this.waypointProperties.shaderBeacon.get() || this.waypointProperties.showRotatingBeam.get() || this.waypointProperties.showStaticBeam.get())) {
                try {
                    MultiBufferSource.BufferSource buffers = this.minecraft.renderBuffers().bufferSource();
                    poseStack.pushPose();
                    this.renderWaypoint(waypoint, poseStack, buffers);
                    poseStack.popPose();
                    buffers.endBatch();
                } catch (Exception var7) {
                    Journeymap.getLogger().error("EntityWaypoint failed to render for " + waypoint + ": ", var7);
                }
            }
        }
    }

    private boolean canDrawWaypoint(Waypoint waypoint, String playerDim) {
        return (Waypoint.Origin.EXTERNAL_FORCE.getValue().equals(waypoint.getOrigin()) || waypoint.isEnable() && JourneymapClient.getInstance().getWaypointProperties().beaconEnabled.get() && JourneymapClient.getInstance().getStateHandler().canShowInGameBeacons() && JourneymapClient.getInstance().getStateHandler().isWaypointsAllowed()) && waypoint.getDimensions().contains(playerDim);
    }

    protected void renderWaypoint(Waypoint waypoint, PoseStack poseStack, MultiBufferSource buffers) {
        float partialTicks = this.minecraft.getFrameTime();
        long gameTime = this.minecraft.level.m_46467_();
        float fadeAlpha = 1.0F;
        Vec3 waypointVec = waypoint.getPosition().add(0.0, 0.118, 0.0);
        Vec3 playerVec = this.minecraft.player.m_20182_();
        double actualDistance = playerVec.distanceTo(waypointVec);
        int maxDistance = this.waypointProperties.maxDistance.get();
        int minDistance = this.waypointProperties.minDistance.get();
        float[] rgba = RGB.floats(waypoint.getColor(), fadeAlpha * 0.4F);
        double viewX = this.renderManager.camera.getPosition().x();
        double viewY = this.renderManager.camera.getPosition().y();
        double viewZ = this.renderManager.camera.getPosition().z();
        double viewDistance = actualDistance;
        double maxRenderDistance = (double) (this.minecraft.options.renderDistance().get() * 16);
        if (maxDistance <= 0 || !(actualDistance > (double) maxDistance)) {
            if (waypoint.isDeathPoint() && this.waypointProperties.autoRemoveDeathpoints.get() && actualDistance < (double) this.waypointProperties.autoRemoveDeathpointDistance.get().intValue() && actualDistance > 1.5) {
                Journeymap.getLogger().debug("Auto removing deathpoint " + waypoint);
                WaypointStore.INSTANCE.remove(waypoint, true);
            } else {
                if (Waypoint.Origin.TEMP.getValue().equals(waypoint.getOrigin()) && (actualDistance <= (double) this.waypointProperties.autoRemoveTempWaypoints.get().intValue() || actualDistance <= (double) (minDistance + 4)) && actualDistance > 1.5) {
                    Journeymap.getLogger().debug("Auto removing temp waypoint " + waypoint);
                    WaypointStore.INSTANCE.remove(waypoint, true);
                }
                if (minDistance > 0) {
                    if ((int) actualDistance <= minDistance) {
                        return;
                    }
                    if ((int) actualDistance <= minDistance + 4) {
                        fadeAlpha = Math.min((float) (actualDistance - (double) minDistance) / 3.0F, 1.0F);
                    }
                }
                if (actualDistance > maxRenderDistance) {
                    Vec3 delta = waypointVec.subtract(playerVec).normalize();
                    waypointVec = playerVec.add(delta.x * maxRenderDistance, delta.y * maxRenderDistance, delta.z * maxRenderDistance);
                    viewDistance = maxRenderDistance;
                }
                double scale = 0.00390625 * ((viewDistance + 4.0) / 3.0);
                double shiftX = waypointVec.x - viewX;
                double shiftY = waypointVec.y - viewY;
                double shiftZ = waypointVec.z - viewZ;
                this.render(poseStack, buffers, waypoint, partialTicks, gameTime, rgba, fadeAlpha, shiftX, shiftY, shiftZ, playerVec, waypointVec, viewDistance, actualDistance, scale);
            }
        }
    }

    protected abstract void render(PoseStack var1, MultiBufferSource var2, Waypoint var3, float var4, long var5, float[] var7, float var8, double var9, double var11, double var13, Vec3 var15, Vec3 var16, double var17, double var19, double var21);
}