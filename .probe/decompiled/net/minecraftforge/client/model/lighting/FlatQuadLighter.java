package net.minecraftforge.client.model.lighting;

import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class FlatQuadLighter extends QuadLighter {

    private static final Direction[] SIDES = Direction.values();

    private static final float MAX_POSITION = 0.99F;

    private static final byte MAX_NORMAL = 127;

    private boolean isFullCube;

    private final int[] packedLight = new int[7];

    public FlatQuadLighter(BlockColors colors) {
        super(colors);
    }

    @Override
    protected void computeLightingAt(BlockAndTintGetter level, BlockPos pos, BlockState state) {
        this.isFullCube = Block.isShapeFullBlock(state.m_60812_(level, pos));
        for (Direction side : SIDES) {
            this.packedLight[side.ordinal()] = LevelRenderer.getLightColor(level, state, pos.relative(side));
        }
        this.packedLight[6] = LevelRenderer.getLightColor(level, state, pos);
    }

    @Override
    protected float calculateBrightness(float[] position) {
        return 1.0F;
    }

    @Override
    protected int calculateLightmap(float[] position, byte[] normal) {
        if ((this.isFullCube || position[1] < -0.99F) && normal[1] <= -127) {
            return this.packedLight[Direction.DOWN.ordinal()];
        } else if ((this.isFullCube || position[1] > 0.99F) && normal[1] >= 127) {
            return this.packedLight[Direction.UP.ordinal()];
        } else if ((this.isFullCube || position[2] < -0.99F) && normal[2] <= -127) {
            return this.packedLight[Direction.NORTH.ordinal()];
        } else if ((this.isFullCube || position[2] > 0.99F) && normal[2] >= 127) {
            return this.packedLight[Direction.SOUTH.ordinal()];
        } else if ((this.isFullCube || position[0] < -0.99F) && normal[0] <= -127) {
            return this.packedLight[Direction.WEST.ordinal()];
        } else {
            return (this.isFullCube || position[0] > 0.99F) && normal[0] >= 127 ? this.packedLight[Direction.EAST.ordinal()] : this.packedLight[6];
        }
    }
}