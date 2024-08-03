package journeymap.client.api.event;

import com.google.common.base.MoreObjects;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public class DeathWaypointEvent extends ClientEvent {

    public final BlockPos location;

    public DeathWaypointEvent(BlockPos location, ResourceKey<Level> dimension) {
        super(ClientEvent.Type.DEATH_WAYPOINT, dimension);
        this.location = location;
    }

    public String toString() {
        return MoreObjects.toStringHelper(this).add("location", this.location).toString();
    }
}