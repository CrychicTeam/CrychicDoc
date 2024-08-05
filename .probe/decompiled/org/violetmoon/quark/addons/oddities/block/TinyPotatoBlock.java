package org.violetmoon.quark.addons.oddities.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.quark.addons.oddities.block.be.TinyPotatoBlockEntity;
import org.violetmoon.quark.addons.oddities.item.TinyPotatoBlockItem;
import org.violetmoon.quark.addons.oddities.module.TinyPotatoModule;
import org.violetmoon.zeta.block.OldMaterials;
import org.violetmoon.zeta.block.ZetaBlock;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.registry.IZetaBlockItemProvider;
import org.violetmoon.zeta.util.ItemNBTHelper;

public class TinyPotatoBlock extends ZetaBlock implements SimpleWaterloggedBlock, EntityBlock, IZetaBlockItemProvider {

    public static final DirectionProperty HORIZONTAL_FACING = BlockStateProperties.HORIZONTAL_FACING;

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private static final VoxelShape SHAPE = m_49796_(6.0, 0.0, 6.0, 10.0, 6.0, 10.0);

    public static final String ANGRY = "angery";

    public static boolean isAngry(ItemStack stack) {
        return ItemNBTHelper.getBoolean(stack, "angery", false);
    }

    public TinyPotatoBlock(@Nullable ZetaModule module) {
        super("tiny_potato", module, OldMaterials.wool().strength(0.25F));
        this.m_49959_((BlockState) this.m_49966_().m_61124_(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH));
        if (module != null) {
            this.setCreativeTab(CreativeModeTabs.FUNCTIONAL_BLOCKS);
        }
    }

    @Override
    public boolean hasAnalogOutputSignal(@NotNull BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(@NotNull BlockState state, @NotNull Level world, @NotNull BlockPos pos) {
        return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(world.getBlockEntity(pos));
    }

    @Override
    protected void createBlockStateDefinition(@NotNull StateDefinition.Builder<Block, BlockState> builder) {
        super.m_7926_(builder);
        builder.add(HORIZONTAL_FACING, WATERLOGGED);
    }

    @NotNull
    @Override
    public FluidState getFluidState(BlockState state) {
        return state.m_61143_(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.m_5888_(state);
    }

    @Override
    public void onRemove(@NotNull BlockState state, @NotNull Level world, @NotNull BlockPos pos, @NotNull BlockState newState, boolean isMoving) {
        if (!state.m_60713_(newState.m_60734_())) {
            if (world.getBlockEntity(pos) instanceof TinyPotatoBlockEntity inventory) {
                Containers.dropContents(world, pos, inventory);
            }
            super.m_6810_(state, world, pos, newState, isMoving);
        }
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        ItemStack stack = super.m_7397_(level, pos, state);
        if (level.getBlockEntity(pos) instanceof TinyPotatoBlockEntity tater) {
            if (tater.m_8077_()) {
                stack.setHoverName(tater.getCustomName());
            }
            if (tater.angry) {
                ItemNBTHelper.setBoolean(stack, "angery", true);
            }
        }
        return stack;
    }

    @NotNull
    @Override
    public VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter world, @NotNull BlockPos pos, @NotNull CollisionContext ctx) {
        return SHAPE;
    }

    @NotNull
    @Override
    public InteractionResult use(@NotNull BlockState state, Level world, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hit) {
        if (world.getBlockEntity(pos) instanceof TinyPotatoBlockEntity tater) {
            tater.interact(player, hand, player.m_21120_(hand), hit.getDirection());
            if (player instanceof ServerPlayer sp) {
                TinyPotatoModule.patPotatoTrigger.trigger(sp);
            }
            if (world instanceof ServerLevel serverLevel) {
                AABB box = SHAPE.bounds();
                serverLevel.sendParticles(ParticleTypes.HEART, (double) pos.m_123341_() + box.minX + Math.random() * (box.maxX - box.minX), (double) pos.m_123342_() + box.maxY, (double) pos.m_123343_() + box.minZ + Math.random() * (box.maxZ - box.minZ), 1, 0.0, 0.0, 0.0, 0.0);
            }
        }
        return InteractionResult.sidedSuccess(world.isClientSide);
    }

    @NotNull
    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext ctx) {
        return (BlockState) ((BlockState) this.m_49966_().m_61124_(HORIZONTAL_FACING, ctx.m_8125_().getOpposite())).m_61124_(WATERLOGGED, ctx.m_43725_().getFluidState(ctx.getClickedPos()).getType() == Fluids.WATER);
    }

    @NotNull
    @Override
    public BlockState updateShape(BlockState state, @NotNull Direction facing, @NotNull BlockState facingState, @NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull BlockPos facingPos) {
        if ((Boolean) state.m_61143_(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.m_6718_(level));
        }
        return super.m_7417_(state, facing, facingState, level, pos, facingPos);
    }

    @Override
    public void setPlacedBy(@NotNull Level world, @NotNull BlockPos pos, @NotNull BlockState state, @Nullable LivingEntity living, ItemStack stack) {
        boolean hasCustomName = stack.hasCustomHoverName();
        boolean isAngry = isAngry(stack);
        if ((hasCustomName || isAngry) && world.getBlockEntity(pos) instanceof TinyPotatoBlockEntity tater) {
            if (hasCustomName) {
                tater.name = stack.getHoverName();
            }
            tater.angry = isAngry(stack);
        }
    }

    @Override
    public boolean triggerEvent(@NotNull BlockState state, @NotNull Level world, @NotNull BlockPos pos, int id, int param) {
        super.m_8133_(state, world, pos, id, param);
        BlockEntity tile = world.getBlockEntity(pos);
        return tile != null && tile.triggerEvent(id, param);
    }

    @Override
    public BlockItem provideItemBlock(Block block, Item.Properties properties) {
        return new TinyPotatoBlockItem(block, properties);
    }

    @NotNull
    @Override
    public RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @NotNull
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new TinyPotatoBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> type) {
        return createTickerHelper(type, TinyPotatoModule.blockEntityType, TinyPotatoBlockEntity::commonTick);
    }
}