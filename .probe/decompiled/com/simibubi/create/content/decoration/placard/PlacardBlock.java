package com.simibubi.create.content.decoration.placard;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllShapes;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.logistics.filter.FilterItem;
import com.simibubi.create.content.logistics.filter.FilterItemStack;
import com.simibubi.create.content.schematics.requirement.ISpecialBlockItemRequirement;
import com.simibubi.create.content.schematics.requirement.ItemRequirement;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.block.ProperWaterloggedBlock;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.items.ItemHandlerHelper;

public class PlacardBlock extends FaceAttachedHorizontalDirectionalBlock implements ProperWaterloggedBlock, IBE<PlacardBlockEntity>, ISpecialBlockItemRequirement, IWrenchable {

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public PlacardBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
        this.m_49959_((BlockState) ((BlockState) this.m_49966_().m_61124_(WATERLOGGED, false)).m_61124_(POWERED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.m_7926_(pBuilder.add(f_53179_, f_54117_, WATERLOGGED, POWERED));
    }

    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        return canAttachLenient(pLevel, pPos, m_53200_(pState).getOpposite());
    }

    public static boolean canAttachLenient(LevelReader pReader, BlockPos pPos, Direction pDirection) {
        BlockPos blockpos = pPos.relative(pDirection);
        return !pReader.m_8055_(blockpos).m_60812_(pReader, blockpos).isEmpty();
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        BlockState stateForPlacement = super.getStateForPlacement(pContext);
        if (stateForPlacement == null) {
            return null;
        } else {
            if (stateForPlacement.m_61143_(f_53179_) == AttachFace.FLOOR) {
                stateForPlacement = (BlockState) stateForPlacement.m_61124_(f_54117_, ((Direction) stateForPlacement.m_61143_(f_54117_)).getOpposite());
            }
            return this.withWater(stateForPlacement, pContext);
        }
    }

    @Override
    public boolean isSignalSource(BlockState pState) {
        return true;
    }

    @Override
    public int getSignal(BlockState pBlockState, BlockGetter pBlockAccess, BlockPos pPos, Direction pSide) {
        return pBlockState.m_61143_(POWERED) ? 15 : 0;
    }

    @Override
    public int getDirectSignal(BlockState pBlockState, BlockGetter pBlockAccess, BlockPos pPos, Direction pSide) {
        return pBlockState.m_61143_(POWERED) && m_53200_(pBlockState) == pSide ? 15 : 0;
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return AllShapes.PLACARD.get(m_53200_(pState));
    }

    @Override
    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
        this.updateWater(pLevel, pState, pCurrentPos);
        return super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
    }

    @Override
    public FluidState getFluidState(BlockState pState) {
        return this.fluidState(pState);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player player, InteractionHand pHand, BlockHitResult pHit) {
        if (player.m_6144_()) {
            return InteractionResult.PASS;
        } else if (pLevel.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            ItemStack inHand = player.m_21120_(pHand);
            return this.onBlockEntityUse(pLevel, pPos, pte -> {
                ItemStack inBlock = pte.getHeldItem();
                if (player.mayBuild() && !inHand.isEmpty() && inBlock.isEmpty()) {
                    pLevel.playSound(null, pPos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, 1.0F, 1.0F);
                    pte.setHeldItem(ItemHandlerHelper.copyStackWithSize(inHand, 1));
                    if (!player.isCreative()) {
                        inHand.shrink(1);
                        if (inHand.isEmpty()) {
                            player.m_21008_(pHand, ItemStack.EMPTY);
                        }
                    }
                    return InteractionResult.SUCCESS;
                } else if (inBlock.isEmpty()) {
                    return InteractionResult.FAIL;
                } else if (inHand.isEmpty()) {
                    return InteractionResult.FAIL;
                } else if ((Boolean) pState.m_61143_(POWERED)) {
                    return InteractionResult.FAIL;
                } else {
                    boolean test = inBlock.getItem() instanceof FilterItem ? FilterItemStack.of(inBlock).test(pLevel, inHand) : ItemHandlerHelper.canItemStacksStack(inHand, inBlock);
                    if (!test) {
                        AllSoundEvents.DENY.play(pLevel, null, pPos, 1.0F, 1.0F);
                        return InteractionResult.SUCCESS;
                    } else {
                        AllSoundEvents.CONFIRM.play(pLevel, null, pPos, 1.0F, 1.0F);
                        pLevel.setBlock(pPos, (BlockState) pState.m_61124_(POWERED, true), 3);
                        updateNeighbours(pState, pLevel, pPos);
                        pte.poweredTicks = 19;
                        pte.notifyUpdate();
                        return InteractionResult.SUCCESS;
                    }
                }
            });
        }
    }

    public static Direction connectedDirection(BlockState state) {
        return m_53200_(state);
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        boolean blockChanged = !pState.m_60713_(pNewState.m_60734_());
        if (!pIsMoving && blockChanged && (Boolean) pState.m_61143_(POWERED)) {
            updateNeighbours(pState, pLevel, pPos);
        }
        if (pState.m_155947_() && (blockChanged || !pNewState.m_155947_())) {
            if (!pIsMoving) {
                this.withBlockEntityDo(pLevel, pPos, be -> Block.popResource(pLevel, pPos, be.getHeldItem()));
            }
            pLevel.removeBlockEntity(pPos);
        }
    }

    public static void updateNeighbours(BlockState pState, Level pLevel, BlockPos pPos) {
        pLevel.updateNeighborsAt(pPos, pState.m_60734_());
        pLevel.updateNeighborsAt(pPos.relative(m_53200_(pState).getOpposite()), pState.m_60734_());
    }

    @Override
    public void attack(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer) {
        if (!pLevel.isClientSide) {
            this.withBlockEntityDo(pLevel, pPos, pte -> {
                ItemStack heldItem = pte.getHeldItem();
                if (!heldItem.isEmpty()) {
                    pPlayer.getInventory().placeItemBackInInventory(heldItem);
                    pLevel.playSound(null, pPos, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.BLOCKS, 1.0F, 1.0F);
                    pte.setHeldItem(ItemStack.EMPTY);
                }
            });
        }
    }

    @Override
    public ItemRequirement getRequiredItems(BlockState state, BlockEntity be) {
        ItemStack placardStack = AllBlocks.PLACARD.asStack();
        if (be instanceof PlacardBlockEntity pbe) {
            ItemStack heldItem = pbe.getHeldItem();
            if (!heldItem.isEmpty()) {
                return new ItemRequirement(List.of(new ItemRequirement.StackRequirement(placardStack, ItemRequirement.ItemUseType.CONSUME), new ItemRequirement.StrictNbtStackRequirement(heldItem, ItemRequirement.ItemUseType.CONSUME)));
            }
        }
        return new ItemRequirement(ItemRequirement.ItemUseType.CONSUME, placardStack);
    }

    @Override
    public Class<PlacardBlockEntity> getBlockEntityClass() {
        return PlacardBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends PlacardBlockEntity> getBlockEntityType() {
        return (BlockEntityType<? extends PlacardBlockEntity>) AllBlockEntityTypes.PLACARD.get();
    }
}