package com.simibubi.create.content.equipment.armor;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.AllEnchantments;
import com.simibubi.create.AllShapes;
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.util.FakePlayer;

public class BacktankBlock extends HorizontalKineticBlock implements IBE<BacktankBlockEntity>, SimpleWaterloggedBlock {

    public BacktankBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.m_49959_((BlockState) this.m_49966_().m_61124_(BlockStateProperties.WATERLOGGED, false));
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.m_61143_(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : Fluids.EMPTY.defaultFluidState();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BlockStateProperties.WATERLOGGED);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState p_149740_1_) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
        return (Integer) this.getBlockEntityOptional(world, pos).map(BacktankBlockEntity::getComparatorOutput).orElse(0);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighbourState, LevelAccessor world, BlockPos pos, BlockPos neighbourPos) {
        if ((Boolean) state.m_61143_(BlockStateProperties.WATERLOGGED)) {
            world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.m_6718_(world));
        }
        return state;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidState = context.m_43725_().getFluidState(context.getClickedPos());
        return (BlockState) super.getStateForPlacement(context).m_61124_(BlockStateProperties.WATERLOGGED, fluidState.getType() == Fluids.WATER);
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face == Direction.UP;
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return Direction.Axis.Y;
    }

    @Override
    public void setPlacedBy(Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.m_6402_(worldIn, pos, state, placer, stack);
        if (!worldIn.isClientSide) {
            if (stack != null) {
                this.withBlockEntityDo(worldIn, pos, be -> {
                    be.setCapacityEnchantLevel(stack.getEnchantmentLevel((Enchantment) AllEnchantments.CAPACITY.get()));
                    be.setAirLevel(stack.getOrCreateTag().getInt("Air"));
                    if (stack.isEnchanted()) {
                        be.setEnchantmentTag(stack.getEnchantmentTags());
                    }
                    if (stack.hasCustomHoverName()) {
                        be.setCustomName(stack.getHoverName());
                    }
                });
            }
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (player == null) {
            return InteractionResult.PASS;
        } else if (player instanceof FakePlayer) {
            return InteractionResult.PASS;
        } else if (player.m_6144_()) {
            return InteractionResult.PASS;
        } else if (player.m_21205_().getItem() instanceof BlockItem) {
            return InteractionResult.PASS;
        } else if (!player.getItemBySlot(EquipmentSlot.CHEST).isEmpty()) {
            return InteractionResult.PASS;
        } else {
            if (!world.isClientSide) {
                world.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.75F, 1.0F);
                player.setItemSlot(EquipmentSlot.CHEST, this.getCloneItemStack(world, pos, state));
                world.m_46961_(pos, false);
            }
            return InteractionResult.SUCCESS;
        }
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter blockGetter, BlockPos pos, BlockState state) {
        Item item = this.m_5456_();
        if (item instanceof BacktankItem.BacktankBlockItem placeable) {
            item = placeable.getActualItem();
        }
        ItemStack stack = new ItemStack(item);
        Optional<BacktankBlockEntity> blockEntityOptional = this.getBlockEntityOptional(blockGetter, pos);
        int air = (Integer) blockEntityOptional.map(BacktankBlockEntity::getAirLevel).orElse(0);
        CompoundTag tag = stack.getOrCreateTag();
        tag.putInt("Air", air);
        ListTag enchants = (ListTag) blockEntityOptional.map(BacktankBlockEntity::getEnchantmentTag).orElse(new ListTag());
        if (!enchants.isEmpty()) {
            ListTag enchantmentTagList = stack.getEnchantmentTags();
            enchantmentTagList.addAll(enchants);
            tag.put("Enchantments", enchantmentTagList);
        }
        Component customName = (Component) blockEntityOptional.map(BacktankBlockEntity::m_7770_).orElse(null);
        if (customName != null) {
            stack.setHoverName(customName);
        }
        return stack;
    }

    @Override
    public VoxelShape getShape(BlockState p_220053_1_, BlockGetter p_220053_2_, BlockPos p_220053_3_, CollisionContext p_220053_4_) {
        return AllShapes.BACKTANK;
    }

    @Override
    public Class<BacktankBlockEntity> getBlockEntityClass() {
        return BacktankBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends BacktankBlockEntity> getBlockEntityType() {
        return (BlockEntityType<? extends BacktankBlockEntity>) AllBlockEntityTypes.BACKTANK.get();
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter reader, BlockPos pos, PathComputationType type) {
        return false;
    }
}