package net.minecraft.world.level;

import java.util.function.Predicate;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class ClipBlockStateContext {

    private final Vec3 from;

    private final Vec3 to;

    private final Predicate<BlockState> block;

    public ClipBlockStateContext(Vec3 vec0, Vec3 vec1, Predicate<BlockState> predicateBlockState2) {
        this.from = vec0;
        this.to = vec1;
        this.block = predicateBlockState2;
    }

    public Vec3 getTo() {
        return this.to;
    }

    public Vec3 getFrom() {
        return this.from;
    }

    public Predicate<BlockState> isTargetBlock() {
        return this.block;
    }
}