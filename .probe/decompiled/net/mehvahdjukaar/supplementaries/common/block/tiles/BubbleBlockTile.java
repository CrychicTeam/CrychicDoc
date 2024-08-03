package net.mehvahdjukaar.supplementaries.common.block.tiles;

import net.mehvahdjukaar.supplementaries.common.block.blocks.BubbleBlock;
import net.mehvahdjukaar.supplementaries.configs.ClientConfigs;
import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class BubbleBlockTile extends BlockEntity {

    private int age = 0;

    private float prevScale = 0.1F;

    private float scale = 0.1F;

    public BubbleBlockTile(BlockPos pos, BlockState state) {
        super((BlockEntityType<?>) ModRegistry.BUBBLE_BLOCK_TILE.get(), pos, state);
    }

    public static void tick(Level pLevel, BlockPos pPos, BlockState pState, BubbleBlockTile tile) {
        if (pLevel.isClientSide) {
            tile.prevScale = tile.scale;
            tile.scale = (float) Math.min(1.0, (double) (tile.scale + 0.001F) + (double) (1.0F - tile.scale) * (Double) ClientConfigs.Blocks.BUBBLE_BLOCK_GROW_SPEED.get());
        } else {
            int lifetime = (Integer) CommonConfigs.Tools.BUBBLE_LIFETIME.get();
            if (lifetime == 10000) {
                return;
            }
            tile.age++;
            if ((tile.age + 5) % 20 == 0) {
                for (Direction d : Direction.values()) {
                    if (tile.f_58857_.getBlockState(tile.m_58899_().relative(d)).m_60713_((Block) ModRegistry.SOAP_BLOCK.get())) {
                        tile.age = 0;
                        return;
                    }
                }
            }
            if (tile.age > lifetime && pLevel.random.nextInt(500) == 0) {
                ((BubbleBlock) ModRegistry.BUBBLE_BLOCK.get()).breakBubble((ServerLevel) pLevel, pPos, pState);
            }
        }
    }

    public float getScale(float partialTicks) {
        return Mth.lerp(partialTicks, this.prevScale, this.scale);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putInt("age", this.age);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.age = pTag.getInt("age");
    }
}