package journeymap.client.ui.fullscreen.event;

import java.util.EnumSet;
import journeymap.client.api.IClientAPI;
import journeymap.client.api.IClientPlugin;
import journeymap.client.api.display.Waypoint;
import journeymap.client.api.event.ClientEvent;
import journeymap.client.api.event.RegistryEvent;
import journeymap.client.api.event.WaypointEvent;
import journeymap.client.api.event.forge.EntityRadarUpdateEvent;
import journeymap.client.api.impl.ClientAPI;
import journeymap.client.texture.TextureCache;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class FullscreenEventReceiver implements IClientPlugin {

    ClientAPI api;

    String test = "This is a test class I use for internal testing of api features. it is not enabled in production";

    @Override
    public void initialize(IClientAPI api) {
        api.subscribe("journeymap", EnumSet.of(ClientEvent.Type.MAPPING_STARTED, ClientEvent.Type.MAPPING_STOPPED, ClientEvent.Type.REGISTRY, ClientEvent.Type.WAYPOINT));
        this.api = (ClientAPI) api;
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public String getModId() {
        return "journeymap";
    }

    @Override
    public void onEvent(ClientEvent clientEvent) {
        if (ClientEvent.Type.MAPPING_STARTED.equals(clientEvent.type)) {
        }
        if (ClientEvent.Type.MAPPING_STOPPED.equals(clientEvent.type)) {
        }
        if (ClientEvent.Type.WAYPOINT.equals(clientEvent.type)) {
            this.onWaypointEvent((WaypointEvent) clientEvent);
        } else if (ClientEvent.Type.REGISTRY.equals(clientEvent.type)) {
            RegistryEvent registryEvent = (RegistryEvent) clientEvent;
            switch(registryEvent.getRegistryType()) {
                case INFO_SLOT:
                    ((RegistryEvent.InfoSlotRegistryEvent) registryEvent).register("Test", "Current Millis", 1000L, () -> "Millis: " + System.currentTimeMillis());
                    ((RegistryEvent.InfoSlotRegistryEvent) registryEvent).register("Test", "Current Ticks", 10L, () -> "");
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onRadarEntityUpdateEvent(EntityRadarUpdateEvent event) {
        LivingEntity entity = (LivingEntity) event.getWrappedEntity().getEntityLivingRef().get();
        if (entity instanceof Creeper) {
            event.getWrappedEntity().setEntityIconLocation(TextureCache.Logo);
        }
    }

    public void onWaypointEvent(WaypointEvent event) {
        if (event.getContext() == WaypointEvent.Context.UPDATE && event.getWaypoint().getId().equals("test123")) {
            this.api.getAllWaypoints();
            this.api.getAllWaypoints(Minecraft.getInstance().level.m_46472_());
            Waypoint wp = this.api.getWaypoint(this.getModId(), "test123");
            this.api.getWaypoints(this.getModId());
            this.api.remove(wp);
        }
        if (event.getContext() == WaypointEvent.Context.DELETED) {
            Waypoint wp = new Waypoint(this.getModId(), "test123", "Test Addon Waypoint", Minecraft.getInstance().level.m_46472_(), new BlockPos(0, 0, 0));
            this.api.show(wp);
        }
    }
}