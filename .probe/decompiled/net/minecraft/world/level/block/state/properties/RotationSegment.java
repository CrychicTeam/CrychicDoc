package net.minecraft.world.level.block.state.properties;

import java.util.Optional;
import net.minecraft.core.Direction;
import net.minecraft.util.SegmentedAnglePrecision;

public class RotationSegment {

    private static final SegmentedAnglePrecision SEGMENTED_ANGLE16 = new SegmentedAnglePrecision(4);

    private static final int MAX_SEGMENT_INDEX = SEGMENTED_ANGLE16.getMask();

    private static final int NORTH_0 = 0;

    private static final int EAST_90 = 4;

    private static final int SOUTH_180 = 8;

    private static final int WEST_270 = 12;

    public static int getMaxSegmentIndex() {
        return MAX_SEGMENT_INDEX;
    }

    public static int convertToSegment(Direction direction0) {
        return SEGMENTED_ANGLE16.fromDirection(direction0);
    }

    public static int convertToSegment(float float0) {
        return SEGMENTED_ANGLE16.fromDegrees(float0);
    }

    public static Optional<Direction> convertToDirection(int int0) {
        Direction $$1 = switch(int0) {
            case 0 ->
                Direction.NORTH;
            case 4 ->
                Direction.EAST;
            case 8 ->
                Direction.SOUTH;
            case 12 ->
                Direction.WEST;
            default ->
                null;
        };
        return Optional.ofNullable($$1);
    }

    public static float convertToDegrees(int int0) {
        return SEGMENTED_ANGLE16.toDegrees(int0);
    }
}