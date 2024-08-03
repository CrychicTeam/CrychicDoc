package com.sihenzhang.crockpot.block;

import com.sihenzhang.crockpot.block.entity.CrockPotBlockEntities;
import com.sihenzhang.crockpot.block.entity.CrockPotBlockEntity;
import com.sihenzhang.crockpot.item.CrockPotItems;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.minecraftforge.network.NetworkHooks;

public class CrockPotBlock extends BaseEntityBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;

    private final int potLevel;

    public CrockPotBlock(int potLevel) {
        super(BlockBehaviour.Properties.of().requiresCorrectToolForDrops().strength(1.5F, 6.0F).lightLevel(state -> state.m_61143_(BlockStateProperties.LIT) ? 13 : 0).noOcclusion());
        this.potLevel = potLevel;
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(FACING, Direction.NORTH)).m_61124_(LIT, false)).m_61124_(OPEN, false));
    }

    public int getPotLevel() {
        return this.potLevel;
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (!pLevel.isClientSide && pLevel.getBlockEntity(pPos) instanceof CrockPotBlockEntity crockPotBlockEntity) {
            NetworkHooks.openScreen((ServerPlayer) pPlayer, crockPotBlockEntity, pPos);
        }
        return InteractionResult.sidedSuccess(pLevel.isClientSide);
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        if (!pState.m_60713_(pNewState.m_60734_()) && pLevel.getBlockEntity(pPos) instanceof CrockPotBlockEntity crockPotBlockEntity) {
            Containers.dropContents(pLevel, pPos, new RecipeWrapper(crockPotBlockEntity.getItemHandler()));
            if (crockPotBlockEntity.isCooking()) {
                Containers.dropContents(pLevel, pPos, new SimpleContainer(CrockPotItems.WET_GOOP.get().getDefaultInstance()));
            }
        }
        super.m_6810_(pState, pLevel, pPos, pNewState, pMovedByPiston);
    }

    @Override
    public void tick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        if (pLevel.m_7702_(pPos) instanceof CrockPotBlockEntity crockPotBlockEntity) {
            crockPotBlockEntity.recheckOpen();
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return (BlockState) this.m_49966_().m_61124_(FACING, pContext.m_8125_().getOpposite());
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {
        if ((Boolean) pState.m_61143_(LIT)) {
            double xPos = (double) pPos.m_123341_() + 0.5;
            double yPos = (double) pPos.m_123342_() + 0.2;
            double zPos = (double) pPos.m_123343_() + 0.5;
            if (pRandom.nextInt(10) == 0) {
                pLevel.playLocalSound(xPos, yPos, zPos, SoundEvents.CAMPFIRE_CRACKLE, SoundSource.BLOCKS, pRandom.nextFloat() + 0.5F, Mth.nextFloat(pRandom, 0.6F, 1.3F), false);
            }
            double xOffset = Mth.nextDouble(pRandom, -0.15, 0.15);
            double zOffset = Mth.nextDouble(pRandom, -0.15, 0.15);
            pLevel.addParticle(ParticleTypes.SMOKE, xPos + xOffset, yPos, zPos + zOffset, 0.0, 0.0, 0.0);
            pLevel.addParticle(ParticleTypes.FLAME, xPos + xOffset, yPos, zPos + zOffset, 0.0, 0.0, 0.0);
        }
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
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING, LIT, OPEN);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new CrockPotBlockEntity(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return pLevel.isClientSide ? null : m_152132_(pBlockEntityType, CrockPotBlockEntities.CROCK_POT_BLOCK_ENTITY.get(), CrockPotBlockEntity::serverTick);
    }
}