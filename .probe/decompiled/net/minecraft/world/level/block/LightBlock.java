package net.minecraft.world.level.block;

import java.util.function.ToIntFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class LightBlock extends Block implements SimpleWaterloggedBlock {

    public static final int MAX_LEVEL = 15;

    public static final IntegerProperty LEVEL = BlockStateProperties.LEVEL;

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public static final ToIntFunction<BlockState> LIGHT_EMISSION = p_153701_ -> (Integer) p_153701_.m_61143_(LEVEL);

    public LightBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(LEVEL, 15)).m_61124_(WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(LEVEL, WATERLOGGED);
    }

    @Override
    public InteractionResult use(BlockState blockState0, Level level1, BlockPos blockPos2, Player player3, InteractionHand interactionHand4, BlockHitResult blockHitResult5) {
        if (!level1.isClientSide && player3.canUseGameMasterBlocks()) {
            level1.setBlock(blockPos2, (BlockState) blockState0.m_61122_(LEVEL), 2);
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.CONSUME;
        }
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return collisionContext3.isHoldingItem(Items.LIGHT) ? Shapes.block() : Shapes.empty();
    }

    @Override
    public boolean propagatesSkylightDown(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2) {
        return true;
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState0) {
        return RenderShape.INVISIBLE;
    }

    @Override
    public float getShadeBrightness(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2) {
        return 1.0F;
    }

    @Override
    public BlockState updateShape(BlockState blockState0, Direction direction1, BlockState blockState2, LevelAccessor levelAccessor3, BlockPos blockPos4, BlockPos blockPos5) {
        if ((Boolean) blockState0.m_61143_(WATERLOGGED)) {
            levelAccessor3.scheduleTick(blockPos4, Fluids.WATER, Fluids.WATER.m_6718_(levelAccessor3));
        }
        return super.m_7417_(blockState0, direction1, blockState2, levelAccessor3, blockPos4, blockPos5);
    }

    @Override
    public FluidState getFluidState(BlockState blockState0) {
        return blockState0.m_61143_(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.m_5888_(blockState0);
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter blockGetter0, BlockPos blockPos1, BlockState blockState2) {
        return setLightOnStack(super.getCloneItemStack(blockGetter0, blockPos1, blockState2), (Integer) blockState2.m_61143_(LEVEL));
    }

    public static ItemStack setLightOnStack(ItemStack itemStack0, int int1) {
        if (int1 != 15) {
            CompoundTag $$2 = new CompoundTag();
            $$2.putString(LEVEL.m_61708_(), String.valueOf(int1));
            itemStack0.addTagElement("BlockStateTag", $$2);
        }
        return itemStack0;
    }
}