package journeymap.client.api.display;

import javax.annotation.ParametersAreNonnullByDefault;
import journeymap.client.api.model.MapImage;
import net.minecraft.core.BlockPos;

@ParametersAreNonnullByDefault
public final class ImageOverlay extends Overlay {

    private BlockPos northWestPoint;

    private BlockPos southEastPoint;

    private MapImage image;

    public ImageOverlay(String modId, String imageId, BlockPos northWestPoint, BlockPos southEastPoint, MapImage image) {
        super(modId, imageId);
        this.setNorthWestPoint(northWestPoint);
        this.setSouthEastPoint(southEastPoint);
        this.setImage(image);
    }

    public BlockPos getNorthWestPoint() {
        return this.northWestPoint;
    }

    public ImageOverlay setNorthWestPoint(BlockPos northWestPoint) {
        this.northWestPoint = northWestPoint;
        return this;
    }

    public BlockPos getSouthEastPoint() {
        return this.southEastPoint;
    }

    public ImageOverlay setSouthEastPoint(BlockPos southEastPoint) {
        this.southEastPoint = southEastPoint;
        return this;
    }

    public MapImage getImage() {
        return this.image;
    }

    public ImageOverlay setImage(MapImage image) {
        this.image = image;
        return this;
    }

    public String toString() {
        return this.toStringHelper(this).add("image", this.image).add("northWestPoint", this.northWestPoint).add("southEastPoint", this.southEastPoint).toString();
    }
}