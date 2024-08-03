package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class MagmaBlock extends Block {

    private static final int BUBBLE_COLUMN_CHECK_DELAY = 20;

    public MagmaBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
    }

    @Override
    public void stepOn(Level level0, BlockPos blockPos1, BlockState blockState2, Entity entity3) {
        if (!entity3.isSteppingCarefully() && entity3 instanceof LivingEntity && !EnchantmentHelper.hasFrostWalker((LivingEntity) entity3)) {
            entity3.hurt(level0.damageSources().hotFloor(), 1.0F);
        }
        super.stepOn(level0, blockPos1, blockState2, entity3);
    }

    @Override
    public void tick(BlockState blockState0, ServerLevel serverLevel1, BlockPos blockPos2, RandomSource randomSource3) {
        BubbleColumnBlock.updateColumn(serverLevel1, blockPos2.above(), blockState0);
    }

    @Override
    public BlockState updateShape(BlockState blockState0, Direction direction1, BlockState blockState2, LevelAccessor levelAccessor3, BlockPos blockPos4, BlockPos blockPos5) {
        if (direction1 == Direction.UP && blockState2.m_60713_(Blocks.WATER)) {
            levelAccessor3.scheduleTick(blockPos4, this, 20);
        }
        return super.m_7417_(blockState0, direction1, blockState2, levelAccessor3, blockPos4, blockPos5);
    }

    @Override
    public void onPlace(BlockState blockState0, Level level1, BlockPos blockPos2, BlockState blockState3, boolean boolean4) {
        level1.m_186460_(blockPos2, this, 20);
    }
}