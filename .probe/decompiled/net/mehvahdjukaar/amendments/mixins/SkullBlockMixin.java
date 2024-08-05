package net.mehvahdjukaar.amendments.mixins;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.AbstractSkullBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = { "net.minecraft.world.level.block.SkullBlock", "net.minecraft.world.level.block.WallSkullBlock" })
public abstract class SkullBlockMixin extends AbstractSkullBlock implements SimpleWaterloggedBlock {

    public SkullBlockMixin(SkullBlock.Type type, BlockBehaviour.Properties properties) {
        super(type, properties);
    }

    @Override
    public boolean placeLiquid(LevelAccessor level, BlockPos pos, BlockState state, FluidState fluidState) {
        if (!state.m_61138_(BlockStateProperties.WATERLOGGED)) {
            return false;
        } else if (!(Boolean) state.m_61143_(BlockStateProperties.WATERLOGGED) && fluidState.getType() == Fluids.WATER) {
            if (!level.m_5776_()) {
                level.m_7731_(pos, (BlockState) state.m_61124_(BlockStateProperties.WATERLOGGED, true), 3);
                level.scheduleTick(pos, fluidState.getType(), fluidState.getType().getTickDelay(level));
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public ItemStack pickupBlock(LevelAccessor level, BlockPos pos, BlockState state) {
        if (!state.m_61138_(BlockStateProperties.WATERLOGGED)) {
            return ItemStack.EMPTY;
        } else if ((Boolean) state.m_61143_(BlockStateProperties.WATERLOGGED)) {
            level.m_7731_(pos, (BlockState) state.m_61124_(BlockStateProperties.WATERLOGGED, false), 3);
            if (!state.m_60710_(level, pos)) {
                level.m_46961_(pos, true);
            }
            return new ItemStack(Items.WATER_BUCKET);
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Inject(method = { "<init>" }, at = { @At("RETURN") })
    private void amendments$dangerousAddWaterlogging(SkullBlock.Type type, BlockBehaviour.Properties properties, CallbackInfo ci) {
        if (this.m_49966_().m_61138_(BlockStateProperties.WATERLOGGED)) {
            this.m_49959_((BlockState) this.m_49966_().m_61124_(BlockStateProperties.WATERLOGGED, Boolean.FALSE));
        }
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.m_61138_(BlockStateProperties.WATERLOGGED) && state.m_61143_(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : super.m_5888_(state);
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (stateIn.m_61138_(BlockStateProperties.WATERLOGGED) && (Boolean) stateIn.m_61143_(BlockStateProperties.WATERLOGGED)) {
            worldIn.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.m_6718_(worldIn));
        }
        return stateIn;
    }

    @ModifyReturnValue(method = { "getStateForPlacement" }, at = { @At("RETURN") })
    public BlockState amendments$addPlacementWaterlogging(BlockState original, BlockPlaceContext context) {
        if (original != null && original.m_61138_(BlockStateProperties.WATERLOGGED)) {
            FluidState fluidstate = context.m_43725_().getFluidState(context.getClickedPos());
            return (BlockState) original.m_61124_(BlockStateProperties.WATERLOGGED, fluidstate.is(FluidTags.WATER) && fluidstate.getAmount() == 8);
        } else {
            return original;
        }
    }

    @Inject(method = { "createBlockStateDefinition" }, at = { @At("RETURN") })
    protected void amendments$addWaterlogging(StateDefinition.Builder<Block, BlockState> builder, CallbackInfo ci) {
        if (!builder.properties.containsValue(BlockStateProperties.WATERLOGGED)) {
            builder.add(BlockStateProperties.WATERLOGGED);
        }
    }
}