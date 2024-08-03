package journeymap.client.api.model;

import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class MapPolygonWithHoles {

    @Nonnull
    public final MapPolygon hull;

    @Nullable
    public final List<MapPolygon> holes;

    public MapPolygonWithHoles(@Nonnull MapPolygon hull, @Nullable List<MapPolygon> holes) {
        this.hull = hull;
        this.holes = holes;
    }
}