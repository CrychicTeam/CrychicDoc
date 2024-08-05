package net.mehvahdjukaar.supplementaries.common.block.blocks;

import java.util.List;
import java.util.UUID;
import net.mehvahdjukaar.supplementaries.common.block.ILavaAndWaterLoggable;
import net.mehvahdjukaar.supplementaries.common.block.ModBlockProperties;
import net.mehvahdjukaar.supplementaries.common.block.tiles.SafeBlockTile;
import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class SafeBlock extends Block implements ILavaAndWaterLoggable, EntityBlock {

    public static final VoxelShape SHAPE = Block.box(1.0, 0.0, 1.0, 15.0, 16.0, 15.0);

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public static final BooleanProperty LAVALOGGED = ModBlockProperties.LAVALOGGED;

    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;

    public static final ResourceLocation CONTENTS = new ResourceLocation("contents");

    public SafeBlock(BlockBehaviour.Properties properties) {
        super(properties.lightLevel(state -> state.m_61143_(LAVALOGGED) ? 15 : 0));
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(OPEN, false)).m_61124_(FACING, Direction.NORTH)).m_61124_(WATERLOGGED, false)).m_61124_(LAVALOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(OPEN, FACING, WATERLOGGED, LAVALOGGED);
    }

    @Override
    public void tick(BlockState state, ServerLevel serverLevel, BlockPos pos, RandomSource rand) {
        if (serverLevel.m_7702_(pos) instanceof SafeBlockTile tile) {
            tile.recheckOpen();
        }
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return (BlockState) state.m_61124_(FACING, rot.rotate((Direction) state.m_61143_(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.m_60717_(mirrorIn.getRotation((Direction) state.m_61143_(FACING)));
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        if ((Boolean) stateIn.m_61143_(LAVALOGGED)) {
            level.scheduleTick(currentPos, Fluids.LAVA, Fluids.LAVA.m_6718_(level));
        } else if ((Boolean) stateIn.m_61143_(WATERLOGGED)) {
            level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.m_6718_(level));
        }
        return super.m_7417_(stateIn, facing, facingState, level, currentPos, facingPos);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidState = context.m_43725_().getFluidState(context.getClickedPos());
        Fluid fluid = fluidState.getType();
        boolean full = fluidState.getAmount() == 8;
        return (BlockState) ((BlockState) ((BlockState) this.m_49966_().m_61124_(FACING, context.m_8125_().getOpposite())).m_61124_(WATERLOGGED, full && fluid == Fluids.WATER)).m_61124_(LAVALOGGED, full && fluid == Fluids.LAVA);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
        return true;
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter worldIn, BlockPos pos, PathComputationType type) {
        return false;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SafeBlockTile(pos, state);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else if (player.isSpectator()) {
            return InteractionResult.CONSUME;
        } else {
            if (level.getBlockEntity(pos) instanceof SafeBlockTile tile && tile.handleAction(player, handIn)) {
                return InteractionResult.CONSUME;
            }
            return InteractionResult.PASS;
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter level, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, level, tooltip, flagIn);
        CompoundTag compoundTag = stack.getTagElement("BlockEntityTag");
        if (compoundTag != null) {
            if ((Boolean) CommonConfigs.Functional.SAFE_SIMPLE.get()) {
                if (compoundTag.contains("Owner")) {
                    UUID id = compoundTag.getUUID("Owner");
                    if (!id.equals(Minecraft.getInstance().player.m_20148_())) {
                        String name = compoundTag.getString("OwnerName");
                        tooltip.add(Component.translatable("message.supplementaries.safe.owner", name).withStyle(ChatFormatting.GRAY));
                        return;
                    }
                }
                if (compoundTag.contains("LootTable", 8)) {
                    tooltip.add(Component.literal("???????").withStyle(ChatFormatting.GRAY));
                }
                if (compoundTag.contains("Items", 9)) {
                    NonNullList<ItemStack> itemStacks = NonNullList.withSize(27, ItemStack.EMPTY);
                    ContainerHelper.loadAllItems(compoundTag, itemStacks);
                    int i = 0;
                    int j = 0;
                    for (ItemStack itemstack : itemStacks) {
                        if (!itemstack.isEmpty()) {
                            j++;
                            if (i <= 4) {
                                i++;
                                MutableComponent component = itemstack.getHoverName().copy();
                                component.append(" x").append(String.valueOf(itemstack.getCount()));
                                tooltip.add(component.withStyle(ChatFormatting.GRAY));
                            }
                        }
                    }
                    if (j - i > 0) {
                        tooltip.add(Component.translatable("container.shulkerBox.more", j - i).withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY));
                    }
                }
                return;
            }
            if (compoundTag.contains("Password")) {
                tooltip.add(Component.translatable("message.supplementaries.safe.bound").withStyle(ChatFormatting.GRAY));
                return;
            }
        }
        tooltip.add(Component.translatable("message.supplementaries.safe.unbound").withStyle(ChatFormatting.GRAY));
    }

    private static void saveTileToItem(SafeBlockTile tile, ItemStack itemstack) {
        CompoundTag compoundTag = tile.m_187482_();
        if (!compoundTag.isEmpty()) {
            itemstack.addTagElement("BlockEntityTag", compoundTag);
        }
        if (tile.m_8077_()) {
            itemstack.setHoverName(tile.m_7770_());
        }
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (level.getBlockEntity(pos) instanceof SafeBlockTile tile) {
            if (!level.isClientSide && player.isCreative() && !tile.m_7983_()) {
                ItemStack itemstack = new ItemStack(this);
                saveTileToItem(tile, itemstack);
                ItemEntity itementity = new ItemEntity(level, (double) pos.m_123341_() + 0.5, (double) pos.m_123342_() + 0.5, (double) pos.m_123343_() + 0.5, itemstack);
                itementity.setDefaultPickUpDelay();
                level.m_7967_(itementity);
            } else {
                tile.m_59640_(player);
            }
        }
        super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        if (builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY) instanceof SafeBlockTile tile) {
            builder = builder.withDynamicDrop(CONTENTS, context -> {
                for (int i = 0; i < tile.m_6643_(); i++) {
                    context.accept(tile.m_8020_(i));
                }
            });
        }
        return super.m_49635_(state, builder);
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        ItemStack itemstack = super.getCloneItemStack(level, pos, state);
        if (level.getBlockEntity(pos) instanceof SafeBlockTile tile) {
            saveTileToItem(tile, itemstack);
        }
        return itemstack;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        if (level.getBlockEntity(pos) instanceof SafeBlockTile tile) {
            if (stack.hasCustomHoverName()) {
                tile.m_58638_(stack.getHoverName());
            }
            if (placer instanceof Player && tile.getOwner() == null) {
                tile.setOwner(placer.m_20148_());
            }
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.m_60713_(newState.m_60734_())) {
            level.updateNeighbourForOutputSignal(pos, state.m_60734_());
            super.m_6810_(state, level, pos, newState, isMoving);
        }
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof SafeBlockTile tile) {
            return tile.isPublic() ? 0 : 15;
        } else {
            return 0;
        }
    }

    @Override
    public MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        return level.getBlockEntity(pos) instanceof MenuProvider m ? m : null;
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        if ((Boolean) state.m_61143_(LAVALOGGED)) {
            return Fluids.LAVA.getSource(false);
        } else {
            return state.m_61143_(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.m_5888_(state);
        }
    }
}