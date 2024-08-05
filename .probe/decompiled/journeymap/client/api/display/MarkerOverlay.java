package journeymap.client.api.display;

import javax.annotation.ParametersAreNonnullByDefault;
import journeymap.client.api.model.MapImage;
import net.minecraft.core.BlockPos;

@ParametersAreNonnullByDefault
public final class MarkerOverlay extends Overlay {

    private BlockPos point;

    private MapImage icon;

    public MarkerOverlay(String modId, String markerId, BlockPos point, MapImage icon) {
        super(modId, markerId);
        this.setPoint(point);
        this.setIcon(icon);
    }

    public BlockPos getPoint() {
        return this.point;
    }

    public MarkerOverlay setPoint(BlockPos point) {
        this.point = point;
        return this;
    }

    public MapImage getIcon() {
        return this.icon;
    }

    public MarkerOverlay setIcon(MapImage icon) {
        this.icon = icon;
        return this;
    }

    public String toString() {
        return this.toStringHelper(this).add("icon", this.icon).add("point", this.point).toString();
    }
}