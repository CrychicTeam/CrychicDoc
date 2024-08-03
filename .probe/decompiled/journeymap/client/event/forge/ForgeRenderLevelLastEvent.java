package journeymap.client.event.forge;

import journeymap.client.event.handlers.WaypointBeaconHandler;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ForgeRenderLevelLastEvent implements ForgeEventHandlerManager.EventHandler {

    private final WaypointBeaconHandler waypointBeaconHandler = new WaypointBeaconHandler();

    @SubscribeEvent
    public void onRenderWorldLastEvent(RenderLevelStageEvent event) {
        if (RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS.equals(event.getStage()) && Minecraft.useShaderTransparency()) {
            this.waypointBeaconHandler.onRenderWaypoints(event.getPoseStack());
        } else if (RenderLevelStageEvent.Stage.AFTER_PARTICLES.equals(event.getStage())) {
            this.waypointBeaconHandler.onRenderWaypoints(event.getPoseStack());
        }
    }
}