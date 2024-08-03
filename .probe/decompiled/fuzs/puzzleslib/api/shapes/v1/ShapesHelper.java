package fuzs.puzzleslib.api.shapes.v1;

import com.google.common.collect.Maps;
import java.util.Collections;
import java.util.Map;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.joml.Quaternionf;
import org.joml.Vector3d;

public class ShapesHelper {

    public static Map<Direction, VoxelShape> rotate(VoxelShape voxelShape) {
        Map<Direction, VoxelShape> shapes = Maps.newEnumMap(Direction.class);
        for (Direction direction : Direction.values()) {
            shapes.put(direction, rotate(direction, voxelShape));
        }
        return Collections.unmodifiableMap(shapes);
    }

    public static VoxelShape rotate(Direction direction, VoxelShape voxelShape) {
        return rotate(direction, voxelShape, new Vector3d(0.5, 0.5, 0.5));
    }

    public static VoxelShape rotate(Direction direction, VoxelShape voxelShape, Vector3d originOffset) {
        VoxelShape[] value = new VoxelShape[] { Shapes.empty() };
        Quaternionf rotation = direction.getRotation();
        voxelShape.forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> {
            Vector3d start = rotation.transform(new Vector3d(minX, minY, minZ).sub(originOffset)).add(originOffset);
            Vector3d end = rotation.transform(new Vector3d(maxX, maxY, maxZ).sub(originOffset)).add(originOffset);
            value[0] = Shapes.or(value[0], box(start.x, start.y, start.z, end.x, end.y, end.z));
        });
        return value[0];
    }

    public static VoxelShape box(double startX, double startY, double startZ, double endX, double endY, double endZ) {
        return Shapes.box(Math.min(startX, endX), Math.min(startY, endY), Math.min(startZ, endZ), Math.max(startX, endX), Math.max(startY, endY), Math.max(startZ, endZ));
    }
}