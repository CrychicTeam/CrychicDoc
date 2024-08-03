package noobanidus.mods.lootr.block;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import noobanidus.mods.lootr.api.LootrAPI;
import noobanidus.mods.lootr.block.entities.LootrShulkerBlockEntity;
import noobanidus.mods.lootr.init.ModBlockEntities;
import noobanidus.mods.lootr.util.ChestUtil;

public class LootrShulkerBlock extends ShulkerBoxBlock {

    public LootrShulkerBlock(BlockBehaviour.Properties pProperties) {
        super(DyeColor.YELLOW, pProperties);
        this.m_49959_((BlockState) ((BlockState) this.m_49965_().any()).m_61124_(f_56183_, Direction.UP));
    }

    private static boolean canOpen(BlockState pState, Level pLevel, BlockPos pPos, LootrShulkerBlockEntity pBlockEntity) {
        if (pBlockEntity.getAnimationStatus() != ShulkerBoxBlockEntity.AnimationStatus.CLOSED) {
            return true;
        } else {
            AABB aabb = Shulker.getProgressDeltaAabb((Direction) pState.m_61143_(f_56183_), 0.0F, 0.5F).move(pPos).deflate(1.0E-6);
            return pLevel.m_45772_(aabb);
        }
    }

    @Override
    public float getExplosionResistance() {
        return LootrAPI.getExplosionResistance(this, super.m_7325_());
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pLevel.isClientSide) {
            return InteractionResult.SUCCESS;
        } else if (pPlayer.isSpectator()) {
            return InteractionResult.CONSUME;
        } else if (pLevel.getBlockEntity(pPos) instanceof LootrShulkerBlockEntity shulkerboxblockentity) {
            if (canOpen(pState, pLevel, pPos, shulkerboxblockentity)) {
                if (pPlayer.m_6144_()) {
                    ChestUtil.handleLootSneak(this, pLevel, pPos, pPlayer);
                } else {
                    ChestUtil.handleLootChest(this, pLevel, pPos, pPlayer);
                }
                pPlayer.awardStat(Stats.OPEN_SHULKER_BOX);
            }
            return InteractionResult.CONSUME;
        } else {
            return InteractionResult.PASS;
        }
    }

    @Override
    public void playerWillDestroy(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer) {
        this.m_142387_(pLevel, pPlayer, pPos, pState);
        if (pState.m_204336_(BlockTags.GUARDED_BY_PIGLINS)) {
            PiglinAi.angerNearbyPiglins(pPlayer, false);
        }
        pLevel.m_142346_(pPlayer, GameEvent.BLOCK_DESTROY, pPos);
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (!pState.m_60713_(pNewState.m_60734_())) {
            BlockEntity blockentity = pLevel.getBlockEntity(pPos);
            if (blockentity instanceof LootrShulkerBlockEntity) {
                pLevel.updateNeighbourForOutputSignal(pPos, pState.m_60734_());
            }
            if (pState.m_155947_() && (!pState.m_60713_(pNewState.m_60734_()) || !pNewState.m_155947_())) {
                pLevel.removeBlockEntity(pPos);
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable BlockGetter pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        BlockEntity blockentity = pLevel.getBlockEntity(pPos);
        return blockentity instanceof LootrShulkerBlockEntity ? Shapes.create(((LootrShulkerBlockEntity) blockentity).getBoundingBox(pState)) : Shapes.block();
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState pState) {
        return true;
    }

    @Nullable
    @Override
    public DyeColor getColor() {
        return DyeColor.YELLOW;
    }

    @Override
    public float getDestroyProgress(BlockState blockState0, Player player1, BlockGetter blockGetter2, BlockPos blockPos3) {
        return LootrAPI.getDestroyProgress(blockState0, player1, blockGetter2, blockPos3, super.m_5880_(blockState0, player1, blockGetter2, blockPos3));
    }

    @Override
    public int getAnalogOutputSignal(BlockState pBlockState, Level pLevel, BlockPos pPos) {
        return LootrAPI.getAnalogOutputSignal(pBlockState, pLevel, pPos, 0);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new LootrShulkerBlockEntity(ModBlockEntities.LOOTR_SHULKER.get(), pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return m_152132_(pBlockEntityType, ModBlockEntities.LOOTR_SHULKER.get(), LootrShulkerBlockEntity::tick);
    }
}