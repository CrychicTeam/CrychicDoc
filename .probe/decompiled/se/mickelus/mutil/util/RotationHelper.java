package se.mickelus.mutil.util;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

@ParametersAreNonnullByDefault
public class RotationHelper {

    public static Rotation rotationFromFacing(Direction facing) {
        switch(facing) {
            case UP:
            case DOWN:
            case NORTH:
                return Rotation.NONE;
            case SOUTH:
                return Rotation.CLOCKWISE_180;
            case EAST:
                return Rotation.CLOCKWISE_90;
            case WEST:
                return Rotation.COUNTERCLOCKWISE_90;
            default:
                return Rotation.NONE;
        }
    }

    public static BlockPos rotatePitch(BlockPos pos, float pitch) {
        float f = Mth.cos(pitch);
        float f1 = Mth.sin(pitch);
        float x = (float) pos.m_123341_();
        float y = (float) pos.m_123342_() * f + (float) pos.m_123343_() * f1;
        float z = (float) pos.m_123343_() * f - (float) pos.m_123342_() * f1;
        return new BlockPos(Math.round(x), Math.round(y), Math.round(z));
    }

    public static BlockPos rotateYaw(BlockPos pos, float yaw) {
        float f = Mth.cos(yaw);
        float f1 = Mth.sin(yaw);
        double x = (double) pos.m_123341_() * (double) f + (double) pos.m_123343_() * (double) f1;
        double y = (double) pos.m_123342_();
        double z = (double) pos.m_123343_() * (double) f - (double) pos.m_123341_() * (double) f1;
        return new BlockPos((int) Math.round(x), (int) Math.round(y), (int) Math.round(z));
    }

    public static BlockPos rotateDirection(BlockPos pos, Direction facing) {
        switch(facing) {
            case NORTH:
                return new BlockPos(-pos.m_123341_(), pos.m_123342_(), -pos.m_123343_());
            case SOUTH:
            default:
                return pos;
            case EAST:
                return new BlockPos(pos.m_123343_(), pos.m_123342_(), -pos.m_123341_());
            case WEST:
                return new BlockPos(-pos.m_123343_(), pos.m_123342_(), pos.m_123341_());
        }
    }

    public static VoxelShape rotateDirection(VoxelShape shape, Direction facing) {
        VoxelShape[] temp = new VoxelShape[] { shape.move(-0.5, 0.0, -0.5), Shapes.empty() };
        for (int i = 0; i < facing.get2DDataValue(); i++) {
            temp[0].forAllBoxes((x1, y1, z1, x2, y2, z2) -> temp[1] = Shapes.or(temp[1], Shapes.box(Math.min(-z1, -z2), y1, Math.min(x1, x2), Math.max(-z1, -z2), y2, Math.max(x1, x2))));
            temp[0] = temp[1];
            temp[1] = Shapes.empty();
        }
        return temp[0].move(0.5, 0.0, 0.5);
    }

    public static Vec3i shiftAxis(Vec3i pos) {
        return new Vec3i(pos.getY(), pos.getZ(), pos.getX());
    }

    public static double getHorizontalAngle(Vec3 a, Vec3 b) {
        return Mth.atan2(a.x - b.x, a.z - b.z);
    }
}