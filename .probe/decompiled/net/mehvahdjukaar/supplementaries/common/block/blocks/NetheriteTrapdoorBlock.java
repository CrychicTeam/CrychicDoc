package net.mehvahdjukaar.supplementaries.common.block.blocks;

import java.util.List;
import java.util.Optional;
import net.mehvahdjukaar.supplementaries.common.block.ILavaAndWaterLoggable;
import net.mehvahdjukaar.supplementaries.common.block.ModBlockProperties;
import net.mehvahdjukaar.supplementaries.common.block.tiles.KeyLockableTile;
import net.mehvahdjukaar.supplementaries.common.utils.MiscUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class NetheriteTrapdoorBlock extends TrapDoorBlock implements ILavaAndWaterLoggable, EntityBlock {

    public static final BooleanProperty LAVALOGGED = ModBlockProperties.LAVALOGGED;

    public NetheriteTrapdoorBlock(BlockBehaviour.Properties properties) {
        super(properties.lightLevel(state -> state.m_61143_(LAVALOGGED) ? 15 : 0), BlockSetType.IRON);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) this.m_49966_().m_61124_(f_54117_, Direction.NORTH)).m_61124_(f_57514_, false)).m_61124_(f_57515_, Half.BOTTOM)).m_61124_(f_57516_, false)).m_61124_(f_57517_, false)).m_61124_(LAVALOGGED, false));
    }

    @Override
    public SoundType getSoundType(BlockState state) {
        return SoundType.NETHERITE_BLOCK;
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (worldIn.getBlockEntity(pos) instanceof KeyLockableTile tile && tile.handleAction(player, handIn, "trapdoor")) {
            state = (BlockState) state.m_61122_(f_57514_);
            worldIn.setBlock(pos, state, 2);
            if ((Boolean) state.m_61143_(f_57517_)) {
                worldIn.m_186469_(pos, Fluids.WATER, Fluids.WATER.m_6718_(worldIn));
            }
            boolean open = (Boolean) state.m_61143_(f_57514_);
            this.m_57527_(player, worldIn, pos, open);
            worldIn.m_142346_(player, open ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);
        }
        return InteractionResult.sidedSuccess(worldIn.isClientSide);
    }

    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        if ((Boolean) state.m_61143_(f_57517_)) {
            worldIn.m_186469_(pos, Fluids.WATER, Fluids.WATER.m_6718_(worldIn));
        } else if ((Boolean) state.m_61143_(LAVALOGGED)) {
            worldIn.m_186469_(pos, Fluids.LAVA, Fluids.LAVA.m_6718_(worldIn));
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        if (state == null) {
            return null;
        } else {
            FluidState fluidstate = context.m_43725_().getFluidState(context.getClickedPos());
            state = (BlockState) state.m_61124_(LAVALOGGED, fluidstate.getType() == Fluids.LAVA);
            return (BlockState) ((BlockState) state.m_61124_(f_57514_, false)).m_61124_(f_57516_, false);
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new KeyLockableTile(pPos, pState);
    }

    @Override
    public BlockState updateShape(BlockState pState, Direction direction, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
        if ((Boolean) pState.m_61143_(LAVALOGGED)) {
            pLevel.scheduleTick(pCurrentPos, Fluids.LAVA, Fluids.LAVA.m_6718_(pLevel));
        }
        return super.updateShape(pState, direction, pFacingState, pLevel, pCurrentPos, pFacingPos);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(LAVALOGGED);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.m_61143_(LAVALOGGED) ? Fluids.LAVA.getSource(false) : super.getFluidState(state);
    }

    @Override
    public boolean canPlaceLiquid(BlockGetter p_204510_1_, BlockPos p_204510_2_, BlockState p_204510_3_, Fluid p_204510_4_) {
        return ILavaAndWaterLoggable.super.canPlaceLiquid(p_204510_1_, p_204510_2_, p_204510_3_, p_204510_4_);
    }

    @Override
    public boolean placeLiquid(LevelAccessor p_204509_1_, BlockPos p_204509_2_, BlockState p_204509_3_, FluidState p_204509_4_) {
        return ILavaAndWaterLoggable.super.placeLiquid(p_204509_1_, p_204509_2_, p_204509_3_, p_204509_4_);
    }

    @Override
    public Fluid takeLiquid(LevelAccessor p_204508_1_, BlockPos p_204508_2_, BlockState p_204508_3_) {
        return ILavaAndWaterLoggable.super.takeLiquid(p_204508_1_, p_204508_2_, p_204508_3_);
    }

    @Override
    public void appendHoverText(ItemStack stack, BlockGetter worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.m_5871_(stack, worldIn, tooltip, flagIn);
        if (MiscUtils.showsHints(worldIn, flagIn)) {
            tooltip.add(Component.translatable("message.supplementaries.key.lockable").withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY));
        }
    }

    @Override
    public ItemStack pickupBlock(LevelAccessor pLevel, BlockPos pPos, BlockState pState) {
        return ILavaAndWaterLoggable.super.pickupBlock(pLevel, pPos, pState);
    }

    @Override
    public Optional<SoundEvent> getPickupSound() {
        return super.m_142298_();
    }
}