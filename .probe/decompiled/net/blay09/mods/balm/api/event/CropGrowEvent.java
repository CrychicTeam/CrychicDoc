package net.blay09.mods.balm.api.event;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class CropGrowEvent extends BalmEvent {

    private final Level level;

    private final BlockPos pos;

    private final BlockState state;

    public CropGrowEvent(Level level, BlockPos pos, BlockState state) {
        this.level = level;
        this.pos = pos;
        this.state = state;
    }

    public Level getLevel() {
        return this.level;
    }

    public BlockPos getPos() {
        return this.pos;
    }

    public BlockState getState() {
        return this.state;
    }

    public static class Post extends CropGrowEvent {

        public Post(Level level, BlockPos pos, BlockState state) {
            super(level, pos, state);
        }
    }

    public static class Pre extends CropGrowEvent {

        public Pre(Level level, BlockPos pos, BlockState state) {
            super(level, pos, state);
        }
    }
}