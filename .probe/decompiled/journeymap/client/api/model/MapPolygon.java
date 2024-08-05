package journeymap.client.api.model;

import com.google.common.base.MoreObjects;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.minecraft.core.BlockPos;

public final class MapPolygon {

    private List<BlockPos> points;

    public MapPolygon(BlockPos... points) {
        this(Arrays.asList(points));
    }

    public MapPolygon(List<BlockPos> points) {
        this.setPoints(points);
    }

    public List<BlockPos> getPoints() {
        return this.points;
    }

    public MapPolygon setPoints(List<BlockPos> points) {
        if (points.size() < 3) {
            throw new IllegalArgumentException("MapPolygon must have at least 3 points.");
        } else {
            this.points = Collections.unmodifiableList(points);
            return this;
        }
    }

    public Iterator<BlockPos> iterator() {
        return this.points.iterator();
    }

    public String toString() {
        return MoreObjects.toStringHelper(this).add("points", this.points).toString();
    }
}