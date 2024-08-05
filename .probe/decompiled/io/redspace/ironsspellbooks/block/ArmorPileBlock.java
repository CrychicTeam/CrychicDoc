package io.redspace.ironsspellbooks.block;

import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.entity.mobs.keeper.KeeperEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ArmorPileBlock extends Block {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    private static final VoxelShape BASE = Block.box(2.0, 0.0, 2.0, 14.0, 4.0, 14.0);

    public ArmorPileBlock() {
        super(BlockBehaviour.Properties.of().strength(5.0F, 8.0F).requiresCorrectToolForDrops().noOcclusion().sound(SoundType.CHAIN).mapColor(MapColor.COLOR_BLACK));
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return BASE;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return (BlockState) this.m_49966_().m_61124_(FACING, pContext.m_8125_().getOpposite());
    }

    @Override
    public void destroy(LevelAccessor pLevel, BlockPos pPos, BlockState pState) {
        super.destroy(pLevel, pPos, pState);
    }

    @Override
    public void spawnAfterBreak(BlockState pState, ServerLevel level, BlockPos pos, ItemStack pStack, boolean pDropExperience) {
        super.m_213646_(pState, level, pos, pStack, pDropExperience);
        KeeperEntity keeper = new KeeperEntity(level);
        keeper.m_20219_(Vec3.atCenterOf(pos));
        keeper.finalizeSpawn(level, level.m_6436_(pos), MobSpawnType.TRIGGERED, null, null);
        level.addFreshEntity(keeper);
        MagicManager.spawnParticles(level, ParticleTypes.SOUL, (double) pos.m_123341_(), (double) pos.m_123342_(), (double) pos.m_123343_(), 20, 0.1, 0.1, 0.1, 0.05, false);
        level.m_5594_(null, pos, SoundEvents.SOUL_ESCAPE, SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    @Override
    public BlockState rotate(BlockState pState, Rotation pRotation) {
        return (BlockState) pState.m_61124_(FACING, pRotation.rotate((Direction) pState.m_61143_(FACING)));
    }

    @Override
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.m_60717_(pMirror.getRotation((Direction) pState.m_61143_(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }
}