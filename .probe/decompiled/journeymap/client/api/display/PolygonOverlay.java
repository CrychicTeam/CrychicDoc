package journeymap.client.api.display;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import journeymap.client.api.model.MapPolygon;
import journeymap.client.api.model.MapPolygonWithHoles;
import journeymap.client.api.model.ShapeProperties;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

@ParametersAreNonnullByDefault
public final class PolygonOverlay extends Overlay {

    private MapPolygon outerArea;

    private List<MapPolygon> holes;

    private ShapeProperties shapeProperties;

    public PolygonOverlay(String modId, String displayId, ResourceKey<Level> dimension, ShapeProperties shapeProperties, MapPolygon outerArea) {
        this(modId, displayId, dimension, shapeProperties, outerArea, null);
    }

    public PolygonOverlay(String modId, String displayId, ResourceKey<Level> dimension, ShapeProperties shapeProperties, MapPolygonWithHoles polygon) {
        this(modId, displayId, dimension, shapeProperties, polygon.hull, polygon.holes);
    }

    public PolygonOverlay(String modId, String displayId, ResourceKey<Level> dimension, ShapeProperties shapeProperties, MapPolygon outerArea, @Nullable List<MapPolygon> holes) {
        super(modId, displayId);
        this.setDimension(dimension);
        this.setShapeProperties(shapeProperties);
        this.setOuterArea(outerArea);
        this.setHoles(holes);
    }

    public MapPolygon getOuterArea() {
        return this.outerArea;
    }

    public PolygonOverlay setOuterArea(MapPolygon outerArea) {
        this.outerArea = outerArea;
        return this;
    }

    public List<MapPolygon> getHoles() {
        return this.holes;
    }

    public PolygonOverlay setHoles(@Nullable List<MapPolygon> holes) {
        if (holes == null) {
            this.holes = null;
        } else {
            this.holes = new ArrayList(holes);
        }
        return this;
    }

    public PolygonOverlay setPolygonWithHoles(MapPolygonWithHoles polygon) {
        return this.setOuterArea(polygon.hull).setHoles(polygon.holes);
    }

    public ShapeProperties getShapeProperties() {
        return this.shapeProperties;
    }

    public PolygonOverlay setShapeProperties(ShapeProperties shapeProperties) {
        this.shapeProperties = shapeProperties;
        return this;
    }

    public String toString() {
        return this.toStringHelper(this).add("holes", this.holes).add("outerArea", this.outerArea).add("shapeProperties", this.shapeProperties).toString();
    }
}