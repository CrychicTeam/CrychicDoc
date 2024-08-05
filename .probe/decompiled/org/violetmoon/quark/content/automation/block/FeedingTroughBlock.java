package org.violetmoon.quark.content.automation.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.util.ForgeSoundType;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.quark.content.automation.block.be.FeedingTroughBlockEntity;
import org.violetmoon.zeta.block.ZetaBlock;
import org.violetmoon.zeta.module.ZetaModule;

public class FeedingTroughBlock extends ZetaBlock implements EntityBlock {

    private static final SoundType WOOD_WITH_PLANT_STEP = new ForgeSoundType(1.0F, 1.0F, () -> SoundEvents.WOOD_BREAK, () -> SoundEvents.GRASS_STEP, () -> SoundEvents.WOOD_PLACE, () -> SoundEvents.WOOD_HIT, () -> SoundEvents.WOOD_FALL);

    public static final BooleanProperty FULL = BooleanProperty.create("full");

    public static final VoxelShape CUBOID_SHAPE = m_49796_(0.0, 0.0, 0.0, 16.0, 8.0, 16.0);

    public static final VoxelShape EMPTY_SHAPE = Shapes.join(CUBOID_SHAPE, m_49796_(2.0, 2.0, 2.0, 14.0, 8.0, 14.0), BooleanOp.ONLY_FIRST);

    public static final VoxelShape FULL_SHAPE = Shapes.join(CUBOID_SHAPE, m_49796_(2.0, 6.0, 2.0, 14.0, 8.0, 14.0), BooleanOp.ONLY_FIRST);

    public static final VoxelShape ANIMAL_SHAPE = m_49796_(0.0, 0.0, 0.0, 16.0, 24.0, 16.0);

    public FeedingTroughBlock(String regname, @Nullable ZetaModule module, BlockBehaviour.Properties properties) {
        super(regname, module, properties);
        this.m_49959_((BlockState) this.m_49966_().m_61124_(FULL, false));
        if (module != null) {
            this.setCreativeTab(CreativeModeTabs.FUNCTIONAL_BLOCKS, Blocks.COMPOSTER, true);
        }
    }

    @NotNull
    @Override
    public VoxelShape getCollisionShape(@NotNull BlockState state, @NotNull BlockGetter world, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        Entity entity = context instanceof EntityCollisionContext ecc ? ecc.getEntity() : null;
        return entity instanceof Animal ? ANIMAL_SHAPE : EMPTY_SHAPE;
    }

    @NotNull
    @Override
    public VoxelShape getInteractionShape(@NotNull BlockState state, @NotNull BlockGetter world, @NotNull BlockPos pos) {
        return CUBOID_SHAPE;
    }

    @NotNull
    @Override
    public VoxelShape getShape(BlockState state, @NotNull BlockGetter world, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return state.m_61143_(FULL) ? FULL_SHAPE : EMPTY_SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FULL);
    }

    @Override
    public SoundType getSoundType(BlockState pState) {
        return pState.m_61143_(FULL) ? WOOD_WITH_PLANT_STEP : super.m_49962_(pState);
    }

    @Override
    public void fallOn(Level level, @NotNull BlockState state, @NotNull BlockPos pos, @NotNull Entity entity, float distance) {
        if ((Boolean) level.getBlockState(pos).m_61143_(FULL)) {
            entity.causeFallDamage(distance, 0.2F, level.damageSources().fall());
        } else {
            super.m_142072_(level, state, pos, entity, distance);
        }
    }

    @Override
    public void onRemove(BlockState state, @NotNull Level world, @NotNull BlockPos pos, @NotNull BlockState newState, boolean isMoving) {
        if (state.m_60734_() != newState.m_60734_()) {
            if (world.getBlockEntity(pos) instanceof FeedingTroughBlockEntity f) {
                Containers.dropContents(world, pos, f);
                world.updateNeighbourForOutputSignal(pos, this);
            }
            super.m_6810_(state, world, pos, newState, isMoving);
        }
    }

    @Override
    public boolean hasAnalogOutputSignal(@NotNull BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(@NotNull BlockState state, Level world, @NotNull BlockPos pos) {
        return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(world.getBlockEntity(pos));
    }

    @NotNull
    @Override
    public InteractionResult use(@NotNull BlockState state, Level world, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult trace) {
        if (world.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            MenuProvider container = this.getMenuProvider(state, world, pos);
            if (container != null) {
                NetworkHooks.openScreen((ServerPlayer) player, container);
            }
            return InteractionResult.CONSUME;
        }
    }

    @Override
    public boolean triggerEvent(@NotNull BlockState state, @NotNull Level world, @NotNull BlockPos pos, int id, int param) {
        super.m_8133_(state, world, pos, id, param);
        BlockEntity tile = world.getBlockEntity(pos);
        return tile != null && tile.triggerEvent(id, param);
    }

    @Nullable
    @Override
    public MenuProvider getMenuProvider(@NotNull BlockState state, Level world, @NotNull BlockPos pos) {
        return world.getBlockEntity(pos) instanceof MenuProvider m ? m : null;
    }

    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new FeedingTroughBlockEntity(pos, state);
    }
}