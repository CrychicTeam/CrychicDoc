package journeymap.client.event.handlers;

import java.util.concurrent.CompletableFuture;
import journeymap.client.JourneymapClient;
import journeymap.client.api.event.DeathWaypointEvent;
import journeymap.client.api.impl.ClientAPI;
import journeymap.client.properties.WaypointProperties;
import journeymap.client.waypoint.Waypoint;
import journeymap.client.waypoint.WaypointStore;
import journeymap.common.Journeymap;
import journeymap.common.log.LogFormatter;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class DeathPointHandler {

    Minecraft mc = Minecraft.getInstance();

    public void handlePlayerDeath() {
        if (this.mc.player != null) {
            this.createDeathpoint();
        }
    }

    private void createDeathpoint() {
        try {
            Player player = this.mc.player;
            if (player == null) {
                Journeymap.getLogger().error("Lost reference to player before Deathpoint could be created");
                return;
            }
            WaypointProperties waypointProperties = JourneymapClient.getInstance().getWaypointProperties();
            boolean enabled = waypointProperties.managerEnabled.get() && waypointProperties.createDeathpoints.get() && JourneymapClient.getInstance().getStateHandler().isAllowDeathPoints();
            boolean cancelled = false;
            int buildMinY = player.m_9236_().dimensionType().minY() + 2;
            int playerY = Math.max(Mth.floor(player.m_20186_()), buildMinY);
            BlockPos pos = new BlockPos(Mth.floor(player.m_20185_()), playerY, Mth.floor(player.m_20189_()));
            if (enabled) {
                ResourceKey<Level> dim = Minecraft.getInstance().player.m_9236_().dimension();
                DeathWaypointEvent event = new DeathWaypointEvent(pos, dim);
                ClientAPI.INSTANCE.getClientEventManager().fireDeathpointEvent(event);
                if (!event.isCancelled()) {
                    Waypoint deathpoint = Waypoint.at(pos, Waypoint.Type.Death, dim.location().toString());
                    CompletableFuture.runAsync(() -> {
                        try {
                            Thread.sleep(3000L);
                            WaypointStore.INSTANCE.save(deathpoint, true);
                        } catch (InterruptedException var2x) {
                            throw new RuntimeException(var2x);
                        }
                    }, Util.backgroundExecutor());
                } else {
                    cancelled = true;
                }
            }
            Journeymap.getLogger().info(String.format("%s died at %s. Deathpoints enabled: %s. Deathpoint created: %s", player.getName().getString(), pos, enabled, cancelled ? "cancelled" : true));
        } catch (Throwable var11) {
            Journeymap.getLogger().error("Unexpected Error in createDeathpoint(): " + LogFormatter.toString(var11));
        }
    }
}