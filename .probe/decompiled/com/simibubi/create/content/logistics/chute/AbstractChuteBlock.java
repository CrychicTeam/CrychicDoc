package com.simibubi.create.content.logistics.chute;

import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.kinetics.belt.behaviour.DirectBeltInputBehaviour;
import com.simibubi.create.foundation.advancement.AdvancementBehaviour;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.block.render.ReducedDestroyEffects;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.Iterate;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientBlockExtensions;
import net.minecraftforge.common.util.LazyOptional;

public abstract class AbstractChuteBlock extends Block implements IWrenchable, IBE<ChuteBlockEntity> {

    public AbstractChuteBlock(BlockBehaviour.Properties p_i48440_1_) {
        super(p_i48440_1_);
    }

    @OnlyIn(Dist.CLIENT)
    public void initializeClient(Consumer<IClientBlockExtensions> consumer) {
        consumer.accept(new ReducedDestroyEffects());
    }

    public static boolean isChute(BlockState state) {
        return state.m_60734_() instanceof AbstractChuteBlock;
    }

    public static boolean isOpenChute(BlockState state) {
        return isChute(state) && ((AbstractChuteBlock) state.m_60734_()).isOpen(state);
    }

    public static boolean isTransparentChute(BlockState state) {
        return isChute(state) && ((AbstractChuteBlock) state.m_60734_()).isTransparent(state);
    }

    @Nullable
    public static Direction getChuteFacing(BlockState state) {
        return !isChute(state) ? null : ((AbstractChuteBlock) state.m_60734_()).getFacing(state);
    }

    public Direction getFacing(BlockState state) {
        return Direction.DOWN;
    }

    public boolean isOpen(BlockState state) {
        return true;
    }

    public boolean isTransparent(BlockState state) {
        return false;
    }

    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, LivingEntity pPlacer, ItemStack pStack) {
        super.setPlacedBy(pLevel, pPos, pState, pPlacer, pStack);
        AdvancementBehaviour.setPlacedBy(pLevel, pPos, pPlacer);
    }

    @Override
    public void updateEntityAfterFallOn(BlockGetter worldIn, Entity entityIn) {
        super.updateEntityAfterFallOn(worldIn, entityIn);
        if (entityIn instanceof ItemEntity) {
            if (!entityIn.level().isClientSide) {
                if (entityIn.isAlive()) {
                    DirectBeltInputBehaviour input = BlockEntityBehaviour.get(entityIn.level(), BlockPos.containing(entityIn.position().add(0.0, 0.5, 0.0)).below(), DirectBeltInputBehaviour.TYPE);
                    if (input != null) {
                        if (input.canInsertFromSide(Direction.UP)) {
                            ItemEntity itemEntity = (ItemEntity) entityIn;
                            ItemStack toInsert = itemEntity.getItem();
                            ItemStack remainder = input.handleInsertion(toInsert, Direction.UP, false);
                            if (remainder.isEmpty()) {
                                itemEntity.m_146870_();
                            }
                            if (remainder.getCount() < toInsert.getCount()) {
                                itemEntity.setItem(remainder);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState p_220082_4_, boolean p_220082_5_) {
        this.withBlockEntityDo(world, pos, ChuteBlockEntity::onAdded);
        this.updateDiagonalNeighbour(state, world, pos);
    }

    protected void updateDiagonalNeighbour(BlockState state, Level world, BlockPos pos) {
        if (isChute(state)) {
            AbstractChuteBlock block = (AbstractChuteBlock) state.m_60734_();
            Direction facing = block.getFacing(state);
            BlockPos toUpdate = pos.below();
            if (facing.getAxis().isHorizontal()) {
                toUpdate = toUpdate.relative(facing.getOpposite());
            }
            BlockState stateToUpdate = world.getBlockState(toUpdate);
            if (isChute(stateToUpdate) && !world.m_183326_().m_183582_(toUpdate, stateToUpdate.m_60734_())) {
                world.m_186460_(toUpdate, stateToUpdate.m_60734_(), 1);
            }
        }
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        IBE.onRemove(state, world, pos, newState);
        if (!state.m_60713_(newState.m_60734_())) {
            this.updateDiagonalNeighbour(state, world, pos);
            for (Direction direction : Iterate.horizontalDirections) {
                BlockPos toUpdate = pos.above().relative(direction);
                BlockState stateToUpdate = world.getBlockState(toUpdate);
                if (isChute(stateToUpdate) && !world.m_183326_().m_183582_(toUpdate, stateToUpdate.m_60734_())) {
                    world.m_186460_(toUpdate, stateToUpdate.m_60734_(), 1);
                }
            }
        }
    }

    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        BlockState updated = this.updateChuteState(pState, pLevel.m_8055_(pPos.above()), pLevel, pPos);
        if (pState != updated) {
            pLevel.m_46597_(pPos, updated);
        }
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState above, LevelAccessor world, BlockPos pos, BlockPos p_196271_6_) {
        return direction != Direction.UP ? state : this.updateChuteState(state, above, world, pos);
    }

    @Override
    public void neighborChanged(BlockState p_220069_1_, Level world, BlockPos pos, Block p_220069_4_, BlockPos neighbourPos, boolean p_220069_6_) {
        if (pos.below().equals(neighbourPos)) {
            this.withBlockEntityDo(world, pos, ChuteBlockEntity::blockBelowChanged);
        } else if (pos.above().equals(neighbourPos)) {
            this.withBlockEntityDo(world, pos, chute -> chute.capAbove = LazyOptional.empty());
        }
    }

    public abstract BlockState updateChuteState(BlockState var1, BlockState var2, BlockGetter var3, BlockPos var4);

    @Override
    public VoxelShape getShape(BlockState p_220053_1_, BlockGetter p_220053_2_, BlockPos p_220053_3_, CollisionContext p_220053_4_) {
        return ChuteShapes.getShape(p_220053_1_);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState p_220071_1_, BlockGetter p_220071_2_, BlockPos p_220071_3_, CollisionContext p_220071_4_) {
        return ChuteShapes.getCollisionShape(p_220071_1_);
    }

    @Override
    public Class<ChuteBlockEntity> getBlockEntityClass() {
        return ChuteBlockEntity.class;
    }

    @Override
    public InteractionResult use(BlockState p_225533_1_, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult p_225533_6_) {
        if (!player.m_21120_(hand).isEmpty()) {
            return InteractionResult.PASS;
        } else {
            return world.isClientSide ? InteractionResult.SUCCESS : this.onBlockEntityUse(world, pos, be -> {
                if (be.item.isEmpty()) {
                    return InteractionResult.PASS;
                } else {
                    player.getInventory().placeItemBackInInventory(be.item);
                    be.setItem(ItemStack.EMPTY);
                    return InteractionResult.SUCCESS;
                }
            });
        }
    }
}