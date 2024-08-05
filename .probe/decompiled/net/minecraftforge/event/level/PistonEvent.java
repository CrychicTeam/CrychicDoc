package net.minecraftforge.event.level;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.piston.PistonStructureResolver;
import net.minecraftforge.eventbus.api.Cancelable;
import org.jetbrains.annotations.Nullable;

public abstract class PistonEvent extends BlockEvent {

    private final Direction direction;

    private final PistonEvent.PistonMoveType moveType;

    public PistonEvent(Level world, BlockPos pos, Direction direction, PistonEvent.PistonMoveType moveType) {
        super(world, pos, world.getBlockState(pos));
        this.direction = direction;
        this.moveType = moveType;
    }

    public Direction getDirection() {
        return this.direction;
    }

    public BlockPos getFaceOffsetPos() {
        return this.getPos().relative(this.direction);
    }

    public PistonEvent.PistonMoveType getPistonMoveType() {
        return this.moveType;
    }

    @Nullable
    public PistonStructureResolver getStructureHelper() {
        return this.getLevel() instanceof Level ? new PistonStructureResolver((Level) this.getLevel(), this.getPos(), this.getDirection(), this.getPistonMoveType().isExtend) : null;
    }

    public static enum PistonMoveType {

        EXTEND(true), RETRACT(false);

        public final boolean isExtend;

        private PistonMoveType(boolean isExtend) {
            this.isExtend = isExtend;
        }
    }

    public static class Post extends PistonEvent {

        public Post(Level world, BlockPos pos, Direction direction, PistonEvent.PistonMoveType moveType) {
            super(world, pos, direction, moveType);
        }
    }

    @Cancelable
    public static class Pre extends PistonEvent {

        public Pre(Level world, BlockPos pos, Direction direction, PistonEvent.PistonMoveType moveType) {
            super(world, pos, direction, moveType);
        }
    }
}