package net.minecraft.world.level.block;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.TheEndGatewayBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;

public class EndGatewayBlock extends BaseEntityBlock {

    protected EndGatewayBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos0, BlockState blockState1) {
        return new TheEndGatewayBlockEntity(blockPos0, blockState1);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level0, BlockState blockState1, BlockEntityType<T> blockEntityTypeT2) {
        return m_152132_(blockEntityTypeT2, BlockEntityType.END_GATEWAY, level0.isClientSide ? TheEndGatewayBlockEntity::m_155834_ : TheEndGatewayBlockEntity::m_155844_);
    }

    @Override
    public void animateTick(BlockState blockState0, Level level1, BlockPos blockPos2, RandomSource randomSource3) {
        BlockEntity $$4 = level1.getBlockEntity(blockPos2);
        if ($$4 instanceof TheEndGatewayBlockEntity) {
            int $$5 = ((TheEndGatewayBlockEntity) $$4).getParticleAmount();
            for (int $$6 = 0; $$6 < $$5; $$6++) {
                double $$7 = (double) blockPos2.m_123341_() + randomSource3.nextDouble();
                double $$8 = (double) blockPos2.m_123342_() + randomSource3.nextDouble();
                double $$9 = (double) blockPos2.m_123343_() + randomSource3.nextDouble();
                double $$10 = (randomSource3.nextDouble() - 0.5) * 0.5;
                double $$11 = (randomSource3.nextDouble() - 0.5) * 0.5;
                double $$12 = (randomSource3.nextDouble() - 0.5) * 0.5;
                int $$13 = randomSource3.nextInt(2) * 2 - 1;
                if (randomSource3.nextBoolean()) {
                    $$9 = (double) blockPos2.m_123343_() + 0.5 + 0.25 * (double) $$13;
                    $$12 = (double) (randomSource3.nextFloat() * 2.0F * (float) $$13);
                } else {
                    $$7 = (double) blockPos2.m_123341_() + 0.5 + 0.25 * (double) $$13;
                    $$10 = (double) (randomSource3.nextFloat() * 2.0F * (float) $$13);
                }
                level1.addParticle(ParticleTypes.PORTAL, $$7, $$8, $$9, $$10, $$11, $$12);
            }
        }
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter blockGetter0, BlockPos blockPos1, BlockState blockState2) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canBeReplaced(BlockState blockState0, Fluid fluid1) {
        return false;
    }
}