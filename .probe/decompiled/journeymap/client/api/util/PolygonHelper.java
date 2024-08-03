package journeymap.client.api.util;

import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.Area;
import java.awt.geom.PathIterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import journeymap.client.api.model.MapPolygon;
import journeymap.client.api.model.MapPolygonWithHoles;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Tuple;
import net.minecraft.world.level.ChunkPos;

public class PolygonHelper {

    public static MapPolygon createChunkPolygonForWorldCoords(int x, int y, int z) {
        return createChunkPolygon(x >> 4, y, z >> 4);
    }

    public static MapPolygon createChunkPolygon(int chunkX, int y, int chunkZ) {
        int x = chunkX << 4;
        int z = chunkZ << 4;
        BlockPos sw = new BlockPos(x, y, z + 16);
        BlockPos se = new BlockPos(x + 16, y, z + 16);
        BlockPos ne = new BlockPos(x + 16, y, z);
        BlockPos nw = new BlockPos(x, y, z);
        return new MapPolygon(sw, se, ne, nw);
    }

    public static MapPolygon createBlockRect(BlockPos corner1, BlockPos corner2) {
        int minX = Math.min(corner1.m_123341_(), corner2.m_123341_());
        int maxX = Math.max(corner1.m_123341_(), corner2.m_123341_());
        int minZ = Math.min(corner1.m_123343_(), corner2.m_123343_());
        int maxZ = Math.max(corner1.m_123343_(), corner2.m_123343_());
        BlockPos sw = new BlockPos(minX, corner1.m_123342_(), maxZ);
        BlockPos se = new BlockPos(maxX, corner1.m_123342_(), maxZ);
        BlockPos ne = new BlockPos(maxX, corner2.m_123342_(), minZ);
        BlockPos nw = new BlockPos(minX, corner2.m_123342_(), minZ);
        return new MapPolygon(sw, se, ne, nw);
    }

    @Nonnull
    public static Area createChunksArea(@Nonnull Collection<ChunkPos> chunks) {
        Area area = new Area();
        for (ChunkPos chunkPos : chunks) {
            area.add(new Area(new Rectangle(chunkPos.getMinBlockX(), chunkPos.getMinBlockZ(), 16, 16)));
        }
        return area;
    }

    @Nonnull
    public static List<MapPolygonWithHoles> createChunksPolygon(@Nonnull Collection<ChunkPos> chunks, int y) {
        return createPolygonFromArea(createChunksArea(chunks), y);
    }

    @Nonnull
    public static Area toArea(@Nonnull MapPolygon polygon) {
        List<BlockPos> points = polygon.getPoints();
        int[] xPoints = new int[points.size()];
        int[] yPoints = new int[points.size()];
        for (int i = 0; i < points.size(); i++) {
            xPoints[i] = ((BlockPos) points.get(i)).m_123341_();
            yPoints[i] = ((BlockPos) points.get(i)).m_123343_();
        }
        return new Area(new Polygon(xPoints, yPoints, points.size()));
    }

    @Nonnull
    public static List<MapPolygonWithHoles> createPolygonFromArea(@Nonnull Area area, int y) {
        List<MapPolygon> polygons = new ArrayList();
        List<BlockPos> poly = new ArrayList();
        PathIterator iterator = area.getPathIterator(null);
        for (float[] points = new float[6]; !iterator.isDone(); iterator.next()) {
            int type = iterator.currentSegment(points);
            switch(type) {
                case 0:
                    if (!poly.isEmpty()) {
                        poly = simplify(poly);
                        polygons.add(new MapPolygon(poly));
                        poly = new ArrayList();
                    }
                    poly.add(new BlockPos(Math.round(points[0]), y, Math.round(points[1])));
                    break;
                case 1:
                    poly.add(new BlockPos(Math.round(points[0]), y, Math.round(points[1])));
            }
        }
        if (!poly.isEmpty()) {
            polygons.add(new MapPolygon(poly));
        }
        return classifyAndGroup(polygons);
    }

    @Nonnull
    public static List<MapPolygonWithHoles> classifyAndGroup(@Nonnull List<MapPolygon> polygons) {
        List<MapPolygon> hulls = new ArrayList();
        List<MapPolygon> holes = new ArrayList();
        for (MapPolygon polygon : polygons) {
            if (isHole(polygon)) {
                holes.add(polygon);
            } else {
                hulls.add(polygon);
            }
        }
        List<Tuple<MapPolygon, Area>> holeAreas = (List<Tuple<MapPolygon, Area>>) holes.stream().map(hole -> new Tuple<>(hole, toArea(hole))).collect(Collectors.toList());
        List<MapPolygonWithHoles> result = new ArrayList();
        for (MapPolygon hull : hulls) {
            Area hullArea = toArea(hull);
            List<MapPolygon> hullHoles = new ArrayList();
            Iterator<Tuple<MapPolygon, Area>> iterator = holeAreas.iterator();
            while (iterator.hasNext()) {
                Tuple<MapPolygon, Area> holeArea = (Tuple<MapPolygon, Area>) iterator.next();
                Area intersection = new Area(hullArea);
                intersection.intersect(holeArea.getB());
                if (!intersection.isEmpty()) {
                    hullHoles.add(holeArea.getA());
                    iterator.remove();
                }
            }
            result.add(new MapPolygonWithHoles(hull, hullHoles));
        }
        return result;
    }

    @Nonnull
    private static List<BlockPos> simplify(@Nonnull List<BlockPos> points) {
        List<BlockPos> result = new ArrayList();
        BlockPos prev2 = (BlockPos) points.get(0);
        BlockPos prev1 = (BlockPos) points.get(1);
        result.add(prev2);
        for (int index = 2; index < points.size(); index++) {
            BlockPos next = (BlockPos) points.get(index);
            if (prev2.m_123341_() == prev1.m_123341_() && prev1.m_123341_() == next.m_123341_()) {
                prev1 = next;
            } else if (prev2.m_123343_() == prev1.m_123343_() && prev1.m_123343_() == next.m_123343_()) {
                prev1 = next;
            } else {
                result.add(prev1);
                prev2 = prev1;
                prev1 = next;
            }
        }
        result.add(prev1);
        return result;
    }

    private static boolean isHole(@Nonnull MapPolygon polygon) {
        long sum = 0L;
        List<BlockPos> points = polygon.getPoints();
        BlockPos a = (BlockPos) points.get(points.size() - 1);
        for (BlockPos b : points) {
            sum += (long) (b.m_123341_() - a.m_123341_()) * (long) (b.m_123343_() + a.m_123343_());
            a = b;
        }
        return sum < 0L;
    }
}