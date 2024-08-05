package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.entity.EntityIceDragon;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class BlockIceSpikes extends Block {

    protected static final VoxelShape AABB = Block.box(1.0, 0.0, 1.0, 15.0, 8.0, 15.0);

    public Item itemBlock;

    public BlockIceSpikes() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.ICE).noOcclusion().dynamicShape().randomTicks().sound(SoundType.GLASS).strength(2.5F).requiresCorrectToolForDrops());
    }

    @NotNull
    @Override
    public BlockState updateShape(BlockState stateIn, @NotNull Direction facing, @NotNull BlockState facingState, @NotNull LevelAccessor worldIn, @NotNull BlockPos currentPos, @NotNull BlockPos facingPos) {
        return !stateIn.m_60710_(worldIn, currentPos) ? Blocks.AIR.defaultBlockState() : super.m_7417_(stateIn, facing, facingState, worldIn, currentPos, facingPos);
    }

    @Override
    public boolean canSurvive(@NotNull BlockState state, @NotNull LevelReader worldIn, BlockPos pos) {
        BlockPos blockpos = pos.below();
        return this.isValidGround(worldIn.m_8055_(blockpos), worldIn, blockpos);
    }

    @Override
    public boolean propagatesSkylightDown(@NotNull BlockState state, @NotNull BlockGetter reader, @NotNull BlockPos pos) {
        return true;
    }

    private boolean isValidGround(BlockState blockState, LevelReader worldIn, BlockPos blockpos) {
        return blockState.m_60815_();
    }

    @NotNull
    @Override
    public VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter worldIn, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return AABB;
    }

    @NotNull
    @Override
    public VoxelShape getCollisionShape(@NotNull BlockState state, @NotNull BlockGetter worldIn, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return AABB;
    }

    @Override
    public void stepOn(Level worldIn, BlockPos pos, BlockState pState, Entity entityIn) {
        if (!(entityIn instanceof EntityIceDragon)) {
            entityIn.hurt(worldIn.damageSources().cactus(), 1.0F);
            if (entityIn instanceof LivingEntity && entityIn.getDeltaMovement().x != 0.0 && entityIn.getDeltaMovement().z != 0.0) {
                ((LivingEntity) entityIn).knockback(0.5, entityIn.getDeltaMovement().x, entityIn.getDeltaMovement().z);
            }
        }
    }

    @Override
    public boolean useShapeForLightOcclusion(@NotNull BlockState state) {
        return true;
    }
}