package com.simibubi.create.content.equipment.clipboard;

import com.google.common.collect.ImmutableList;
import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.AllShapes;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.block.ProperWaterloggedBlock;
import com.simibubi.create.foundation.gui.ScreenOpener;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.DistExecutor;

public class ClipboardBlock extends FaceAttachedHorizontalDirectionalBlock implements IBE<ClipboardBlockEntity>, IWrenchable, ProperWaterloggedBlock {

    public static final BooleanProperty WRITTEN = BooleanProperty.create("written");

    public ClipboardBlock(BlockBehaviour.Properties pProperties) {
        super(pProperties);
        this.m_49959_((BlockState) ((BlockState) this.m_49966_().m_61124_(WATERLOGGED, false)).m_61124_(WRITTEN, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.m_7926_(pBuilder.add(WRITTEN, f_53179_, f_54117_, WATERLOGGED));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        BlockState stateForPlacement = super.getStateForPlacement(pContext);
        if (stateForPlacement == null) {
            return null;
        } else {
            if (stateForPlacement.m_61143_(f_53179_) != AttachFace.WALL) {
                stateForPlacement = (BlockState) stateForPlacement.m_61124_(f_54117_, ((Direction) stateForPlacement.m_61143_(f_54117_)).getOpposite());
            }
            return (BlockState) this.withWater(stateForPlacement, pContext).m_61124_(WRITTEN, pContext.m_43722_().hasTag());
        }
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return (switch((AttachFace) pState.m_61143_(f_53179_)) {
            case FLOOR ->
                AllShapes.CLIPBOARD_FLOOR;
            case CEILING ->
                AllShapes.CLIPBOARD_CEILING;
            default ->
                AllShapes.CLIPBOARD_WALL;
        }).get((Direction) pState.m_61143_(f_54117_));
    }

    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        return !pLevel.m_8055_(pPos.relative(m_53200_(pState).getOpposite())).m_247087_();
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pPlayer.m_6144_()) {
            this.breakAndCollect(pState, pLevel, pPos, pPlayer);
            return InteractionResult.SUCCESS;
        } else {
            return this.onBlockEntityUse(pLevel, pPos, cbe -> {
                if (pLevel.isClientSide()) {
                    DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> this.openScreen(pPlayer, cbe.dataContainer, pPos));
                }
                return InteractionResult.SUCCESS;
            });
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void openScreen(Player player, ItemStack stack, BlockPos pos) {
        if (Minecraft.getInstance().player == player) {
            ScreenOpener.open(new ClipboardScreen(player.getInventory().selected, stack, pos));
        }
    }

    @Override
    public void attack(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer) {
        this.breakAndCollect(pState, pLevel, pPos, pPlayer);
    }

    private void breakAndCollect(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer) {
        if (!(pPlayer instanceof FakePlayer)) {
            if (!pLevel.isClientSide) {
                ItemStack cloneItemStack = this.getCloneItemStack(pLevel, pPos, pState);
                pLevel.m_46961_(pPos, false);
                if (pLevel.getBlockState(pPos) != pState) {
                    pPlayer.getInventory().placeItemBackInInventory(cloneItemStack);
                }
            }
        }
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter world, BlockPos pos, BlockState state) {
        return world.getBlockEntity(pos) instanceof ClipboardBlockEntity cbe ? cbe.dataContainer : new ItemStack(this);
    }

    @Override
    public void playerWillDestroy(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer) {
        if (pLevel.getBlockEntity(pPos) instanceof ClipboardBlockEntity cbe) {
            if (!pLevel.isClientSide && !pPlayer.isCreative()) {
                Block.popResource(pLevel, pPos, cbe.dataContainer.copy());
            }
        }
    }

    @Override
    public List<ItemStack> getDrops(BlockState pState, LootParams.Builder pBuilder) {
        if (pBuilder.getOptionalParameter(LootContextParams.BLOCK_ENTITY) instanceof ClipboardBlockEntity cbe) {
            pBuilder.withDynamicDrop(ShulkerBoxBlock.CONTENTS, p_56219_ -> p_56219_.accept(cbe.dataContainer.copy()));
            return ImmutableList.of(cbe.dataContainer.copy());
        } else {
            return super.m_49635_(pState, pBuilder);
        }
    }

    @Override
    public FluidState getFluidState(BlockState pState) {
        return this.fluidState(pState);
    }

    @Override
    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
        this.updateWater(pLevel, pState, pCurrentPos);
        return super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
    }

    @Override
    public Class<ClipboardBlockEntity> getBlockEntityClass() {
        return ClipboardBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends ClipboardBlockEntity> getBlockEntityType() {
        return (BlockEntityType<? extends ClipboardBlockEntity>) AllBlockEntityTypes.CLIPBOARD.get();
    }
}