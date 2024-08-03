package io.redspace.ironsspellbooks.block.pedestal;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.Tags;
import org.jetbrains.annotations.Nullable;

public class PedestalBlock extends BaseEntityBlock {

    public static final VoxelShape SHAPE_COLUMN = Block.box(3.0, 4.0, 3.0, 13.0, 12.0, 13.0);

    public static final VoxelShape SHAPE_BOTTOM = Block.box(0.0, 0.0, 0.0, 16.0, 4.0, 16.0);

    public static final VoxelShape SHAPE_TOP = Block.box(0.0, 12.0, 0.0, 16.0, 16.0, 16.0);

    public static final VoxelShape SHAPE = Shapes.or(SHAPE_BOTTOM, SHAPE_TOP, SHAPE_COLUMN);

    public static final List<BlockPos> BOOKSHELF_OFFSETS = BlockPos.betweenClosedStream(-3, -1, -3, 3, 1, 3).filter(blockPos -> Math.abs(blockPos.m_123341_()) == 3 || Math.abs(blockPos.m_123343_()) == 3).map(BlockPos::m_7949_).toList();

    public PedestalBlock() {
        super(BlockBehaviour.Properties.copy(Blocks.LODESTONE).noOcclusion());
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    public InteractionResult use(BlockState state, Level pLevel, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!pLevel.isClientSide() && pLevel.getBlockEntity(pos) instanceof PedestalTile pedestalTile) {
            ItemStack currentPedestalItem = pedestalTile.getHeldItem();
            ItemStack handItem = player.m_21120_(hand);
            ItemStack playerItem = currentPedestalItem.copy();
            if (!handItem.isEmpty() && handItem.getCount() != 1) {
                this.dropItem(playerItem, player);
            } else {
                player.m_21008_(hand, playerItem);
            }
            pedestalTile.setHeldItem(ItemStack.EMPTY);
            currentPedestalItem = handItem.copy();
            if (!currentPedestalItem.isEmpty()) {
                currentPedestalItem.setCount(1);
                pedestalTile.setHeldItem(currentPedestalItem);
                handItem.shrink(1);
            }
            pLevel.sendBlockUpdated(pos, state, state, 2);
        }
        return InteractionResult.sidedSuccess(pLevel.isClientSide());
    }

    @Override
    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {
        super.m_214162_(pState, pLevel, pPos, pRandom);
        for (BlockPos blockpos : BOOKSHELF_OFFSETS) {
            if (pRandom.nextInt(16) == 0 && pLevel.getBlockState(pPos.offset(blockpos)).m_204336_(Tags.Blocks.BOOKSHELVES)) {
                pLevel.addParticle(ParticleTypes.ENCHANT, (double) pPos.m_123341_() + 0.5, (double) pPos.m_123342_() + 2.0, (double) pPos.m_123343_() + 0.5, (double) ((float) blockpos.m_123341_() + pRandom.nextFloat()) - 0.5, (double) ((float) blockpos.m_123342_() - pRandom.nextFloat() - 1.0F), (double) ((float) blockpos.m_123343_() + pRandom.nextFloat()) - 0.5);
            }
        }
    }

    private void dropItem(ItemStack itemstack, Player owner) {
        if (owner instanceof ServerPlayer serverplayer) {
            ItemEntity itementity = serverplayer.m_36176_(itemstack, false);
            if (itementity != null) {
                itementity.setNoPickUpDelay();
                itementity.setThrower(serverplayer.m_20148_());
            }
        }
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (pState.m_60734_() != pNewState.m_60734_()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof PedestalTile) {
                ((PedestalTile) blockEntity).drops();
            }
        }
        super.m_6810_(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new PedestalTile(pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }
}