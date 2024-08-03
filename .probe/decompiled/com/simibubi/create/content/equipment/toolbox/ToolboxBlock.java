package com.simibubi.create.content.equipment.toolbox;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllShapes;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.utility.BlockHelper;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.network.NetworkHooks;

public class ToolboxBlock extends HorizontalDirectionalBlock implements SimpleWaterloggedBlock, IBE<ToolboxBlockEntity> {

    protected final DyeColor color;

    public ToolboxBlock(BlockBehaviour.Properties properties, DyeColor color) {
        super(properties);
        this.color = color;
        this.m_49959_((BlockState) this.m_49966_().m_61124_(BlockStateProperties.WATERLOGGED, false));
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.m_61143_(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : Fluids.EMPTY.defaultFluidState();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.m_7926_(builder.add(BlockStateProperties.WATERLOGGED).add(f_54117_));
    }

    @Override
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.m_6402_(worldIn, pos, state, placer, stack);
        if (!worldIn.isClientSide) {
            if (stack != null) {
                this.withBlockEntityDo(worldIn, pos, be -> {
                    CompoundTag orCreateTag = stack.getOrCreateTag();
                    be.readInventory(orCreateTag.getCompound("Inventory"));
                    if (orCreateTag.contains("UniqueId")) {
                        be.setUniqueId(orCreateTag.getUUID("UniqueId"));
                    }
                    if (stack.hasCustomHoverName()) {
                        be.setCustomName(stack.getHoverName());
                    }
                });
            }
        }
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moving) {
        if (state.m_155947_() && (!newState.m_155947_() || !(newState.m_60734_() instanceof ToolboxBlock))) {
            world.removeBlockEntity(pos);
        }
    }

    @Override
    public void attack(BlockState state, Level world, BlockPos pos, Player player) {
        if (!(player instanceof FakePlayer)) {
            if (!world.isClientSide) {
                this.withBlockEntityDo(world, pos, ToolboxBlockEntity::unequipTracked);
                if (world instanceof ServerLevel) {
                    ItemStack cloneItemStack = this.getCloneItemStack(world, pos, state);
                    world.m_46961_(pos, false);
                    if (world.getBlockState(pos) != state) {
                        player.getInventory().placeItemBackInInventory(cloneItemStack);
                    }
                }
            }
        }
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter world, BlockPos pos, BlockState state) {
        ItemStack item = new ItemStack(this);
        Optional<ToolboxBlockEntity> blockEntityOptional = this.getBlockEntityOptional(world, pos);
        CompoundTag tag = item.getOrCreateTag();
        CompoundTag inv = (CompoundTag) blockEntityOptional.map(tb -> tb.inventory.serializeNBT()).orElse(new CompoundTag());
        tag.put("Inventory", inv);
        blockEntityOptional.map(tb -> tb.getUniqueId()).ifPresent(uid -> tag.putUUID("UniqueId", uid));
        blockEntityOptional.map(ToolboxBlockEntity::m_7770_).ifPresent(item::m_41714_);
        return item;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighbourState, LevelAccessor world, BlockPos pos, BlockPos neighbourPos) {
        if ((Boolean) state.m_61143_(BlockStateProperties.WATERLOGGED)) {
            world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.m_6718_(world));
        }
        return state;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return AllShapes.TOOLBOX.get((Direction) state.m_61143_(f_54117_));
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult ray) {
        if (player != null && !player.m_6047_()) {
            ItemStack stack = player.m_21120_(hand);
            DyeColor color = DyeColor.getColor(stack);
            if (color != null && color != this.color) {
                if (world.isClientSide) {
                    return InteractionResult.SUCCESS;
                } else {
                    BlockState newState = BlockHelper.copyProperties(state, AllBlocks.TOOLBOXES.get(color).getDefaultState());
                    world.setBlockAndUpdate(pos, newState);
                    return InteractionResult.SUCCESS;
                }
            } else if (player instanceof FakePlayer) {
                return InteractionResult.PASS;
            } else if (world.isClientSide) {
                return InteractionResult.SUCCESS;
            } else {
                this.withBlockEntityDo(world, pos, toolbox -> NetworkHooks.openScreen((ServerPlayer) player, toolbox, toolbox::sendToMenu));
                return InteractionResult.SUCCESS;
            }
        } else {
            return InteractionResult.PASS;
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState ifluidstate = context.m_43725_().getFluidState(context.getClickedPos());
        return (BlockState) ((BlockState) super.m_5573_(context).m_61124_(f_54117_, context.m_8125_().getOpposite())).m_61124_(BlockStateProperties.WATERLOGGED, ifluidstate.getType() == Fluids.WATER);
    }

    @Override
    public Class<ToolboxBlockEntity> getBlockEntityClass() {
        return ToolboxBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends ToolboxBlockEntity> getBlockEntityType() {
        return (BlockEntityType<? extends ToolboxBlockEntity>) AllBlockEntityTypes.TOOLBOX.get();
    }

    public DyeColor getColor() {
        return this.color;
    }

    public static Ingredient getMainBox() {
        return Ingredient.of((ItemLike) AllBlocks.TOOLBOXES.get(DyeColor.BROWN).get());
    }
}