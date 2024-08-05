package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TheEndPortalBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class EndPortalBlock extends BaseEntityBlock {

    protected static final VoxelShape SHAPE = Block.box(0.0, 6.0, 0.0, 16.0, 12.0, 16.0);

    protected EndPortalBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos0, BlockState blockState1) {
        return new TheEndPortalBlockEntity(blockPos0, blockState1);
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return SHAPE;
    }

    @Override
    public void entityInside(BlockState blockState0, Level level1, BlockPos blockPos2, Entity entity3) {
        if (level1 instanceof ServerLevel && entity3.canChangeDimensions() && Shapes.joinIsNotEmpty(Shapes.create(entity3.getBoundingBox().move((double) (-blockPos2.m_123341_()), (double) (-blockPos2.m_123342_()), (double) (-blockPos2.m_123343_()))), blockState0.m_60808_(level1, blockPos2), BooleanOp.AND)) {
            ResourceKey<Level> $$4 = level1.dimension() == Level.END ? Level.OVERWORLD : Level.END;
            ServerLevel $$5 = ((ServerLevel) level1).getServer().getLevel($$4);
            if ($$5 == null) {
                return;
            }
            entity3.changeDimension($$5);
        }
    }

    @Override
    public void animateTick(BlockState blockState0, Level level1, BlockPos blockPos2, RandomSource randomSource3) {
        double $$4 = (double) blockPos2.m_123341_() + randomSource3.nextDouble();
        double $$5 = (double) blockPos2.m_123342_() + 0.8;
        double $$6 = (double) blockPos2.m_123343_() + randomSource3.nextDouble();
        level1.addParticle(ParticleTypes.SMOKE, $$4, $$5, $$6, 0.0, 0.0, 0.0);
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