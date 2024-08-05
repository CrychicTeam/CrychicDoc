package net.minecraft.world.level.block.piston;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.PistonType;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class MovingPistonBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING = PistonHeadBlock.f_52588_;

    public static final EnumProperty<PistonType> TYPE = PistonHeadBlock.TYPE;

    public MovingPistonBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(FACING, Direction.NORTH)).m_61124_(TYPE, PistonType.DEFAULT));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos0, BlockState blockState1) {
        return null;
    }

    public static BlockEntity newMovingBlockEntity(BlockPos blockPos0, BlockState blockState1, BlockState blockState2, Direction direction3, boolean boolean4, boolean boolean5) {
        return new PistonMovingBlockEntity(blockPos0, blockState1, blockState2, direction3, boolean4, boolean5);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level0, BlockState blockState1, BlockEntityType<T> blockEntityTypeT2) {
        return m_152132_(blockEntityTypeT2, BlockEntityType.PISTON, PistonMovingBlockEntity::m_155915_);
    }

    @Override
    public void onRemove(BlockState blockState0, Level level1, BlockPos blockPos2, BlockState blockState3, boolean boolean4) {
        if (!blockState0.m_60713_(blockState3.m_60734_())) {
            BlockEntity $$5 = level1.getBlockEntity(blockPos2);
            if ($$5 instanceof PistonMovingBlockEntity) {
                ((PistonMovingBlockEntity) $$5).finalTick();
            }
        }
    }

    @Override
    public void destroy(LevelAccessor levelAccessor0, BlockPos blockPos1, BlockState blockState2) {
        BlockPos $$3 = blockPos1.relative(((Direction) blockState2.m_61143_(FACING)).getOpposite());
        BlockState $$4 = levelAccessor0.m_8055_($$3);
        if ($$4.m_60734_() instanceof PistonBaseBlock && (Boolean) $$4.m_61143_(PistonBaseBlock.EXTENDED)) {
            levelAccessor0.m_7471_($$3, false);
        }
    }

    @Override
    public InteractionResult use(BlockState blockState0, Level level1, BlockPos blockPos2, Player player3, InteractionHand interactionHand4, BlockHitResult blockHitResult5) {
        if (!level1.isClientSide && level1.getBlockEntity(blockPos2) == null) {
            level1.removeBlock(blockPos2, false);
            return InteractionResult.CONSUME;
        } else {
            return InteractionResult.PASS;
        }
    }

    @Override
    public List<ItemStack> getDrops(BlockState blockState0, LootParams.Builder lootParamsBuilder1) {
        PistonMovingBlockEntity $$2 = this.getBlockEntity(lootParamsBuilder1.getLevel(), BlockPos.containing(lootParamsBuilder1.getParameter(LootContextParams.ORIGIN)));
        return $$2 == null ? Collections.emptyList() : $$2.getMovedState().m_287290_(lootParamsBuilder1);
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return Shapes.empty();
    }

    @Override
    public VoxelShape getCollisionShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        PistonMovingBlockEntity $$4 = this.getBlockEntity(blockGetter1, blockPos2);
        return $$4 != null ? $$4.getCollisionShape(blockGetter1, blockPos2) : Shapes.empty();
    }

    @Nullable
    private PistonMovingBlockEntity getBlockEntity(BlockGetter blockGetter0, BlockPos blockPos1) {
        BlockEntity $$2 = blockGetter0.getBlockEntity(blockPos1);
        return $$2 instanceof PistonMovingBlockEntity ? (PistonMovingBlockEntity) $$2 : null;
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter blockGetter0, BlockPos blockPos1, BlockState blockState2) {
        return ItemStack.EMPTY;
    }

    @Override
    public BlockState rotate(BlockState blockState0, Rotation rotation1) {
        return (BlockState) blockState0.m_61124_(FACING, rotation1.rotate((Direction) blockState0.m_61143_(FACING)));
    }

    @Override
    public BlockState mirror(BlockState blockState0, Mirror mirror1) {
        return blockState0.m_60717_(mirror1.getRotation((Direction) blockState0.m_61143_(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(FACING, TYPE);
    }

    @Override
    public boolean isPathfindable(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, PathComputationType pathComputationType3) {
        return false;
    }
}