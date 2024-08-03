package net.minecraft.world.level.block;

import java.util.Arrays;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.contents.LiteralContents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SignApplicator;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.entity.SignText;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public abstract class SignBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    protected static final float AABB_OFFSET = 4.0F;

    protected static final VoxelShape SHAPE = Block.box(4.0, 0.0, 4.0, 12.0, 16.0, 12.0);

    private final WoodType type;

    protected SignBlock(BlockBehaviour.Properties blockBehaviourProperties0, WoodType woodType1) {
        super(blockBehaviourProperties0);
        this.type = woodType1;
    }

    @Override
    public BlockState updateShape(BlockState blockState0, Direction direction1, BlockState blockState2, LevelAccessor levelAccessor3, BlockPos blockPos4, BlockPos blockPos5) {
        if ((Boolean) blockState0.m_61143_(WATERLOGGED)) {
            levelAccessor3.scheduleTick(blockPos4, Fluids.WATER, Fluids.WATER.m_6718_(levelAccessor3));
        }
        return super.m_7417_(blockState0, direction1, blockState2, levelAccessor3, blockPos4, blockPos5);
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return SHAPE;
    }

    @Override
    public boolean isPossibleToRespawnInThis(BlockState blockState0) {
        return true;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos0, BlockState blockState1) {
        return new SignBlockEntity(blockPos0, blockState1);
    }

    @Override
    public InteractionResult use(BlockState blockState0, Level level1, BlockPos blockPos2, Player player3, InteractionHand interactionHand4, BlockHitResult blockHitResult5) {
        ItemStack $$6 = player3.m_21120_(interactionHand4);
        Item $$7 = $$6.getItem();
        SignApplicator $$9 = $$6.getItem() instanceof SignApplicator $$8 ? $$8 : null;
        boolean $$10 = $$9 != null && player3.mayBuild();
        if (level1.getBlockEntity(blockPos2) instanceof SignBlockEntity $$11) {
            if (!level1.isClientSide) {
                boolean $$12 = $$11.isFacingFrontText(player3);
                SignText $$13 = $$11.getText($$12);
                boolean $$14 = $$11.executeClickCommandsIfPresent(player3, level1, blockPos2, $$12);
                if ($$11.isWaxed()) {
                    level1.m_247517_(null, $$11.m_58899_(), SoundEvents.WAXED_SIGN_INTERACT_FAIL, SoundSource.BLOCKS);
                    return InteractionResult.PASS;
                } else if ($$10 && !this.otherPlayerIsEditingSign(player3, $$11) && $$9.canApplyToSign($$13, player3) && $$9.tryApplyToSign(level1, $$11, $$12, player3)) {
                    if (!player3.isCreative()) {
                        $$6.shrink(1);
                    }
                    level1.m_220407_(GameEvent.BLOCK_CHANGE, $$11.m_58899_(), GameEvent.Context.of(player3, $$11.m_58900_()));
                    player3.awardStat(Stats.ITEM_USED.get($$7));
                    return InteractionResult.SUCCESS;
                } else if ($$14) {
                    return InteractionResult.SUCCESS;
                } else if (!this.otherPlayerIsEditingSign(player3, $$11) && player3.mayBuild() && this.hasEditableText(player3, $$11, $$12)) {
                    this.openTextEdit(player3, $$11, $$12);
                    return InteractionResult.SUCCESS;
                } else {
                    return InteractionResult.PASS;
                }
            } else {
                return !$$10 && !$$11.isWaxed() ? InteractionResult.CONSUME : InteractionResult.SUCCESS;
            }
        } else {
            return InteractionResult.PASS;
        }
    }

    private boolean hasEditableText(Player player0, SignBlockEntity signBlockEntity1, boolean boolean2) {
        SignText $$3 = signBlockEntity1.getText(boolean2);
        return Arrays.stream($$3.getMessages(player0.isTextFilteringEnabled())).allMatch(p_279411_ -> p_279411_.equals(CommonComponents.EMPTY) || p_279411_.getContents() instanceof LiteralContents);
    }

    public abstract float getYRotationDegrees(BlockState var1);

    public Vec3 getSignHitboxCenterPosition(BlockState blockState0) {
        return new Vec3(0.5, 0.5, 0.5);
    }

    @Override
    public FluidState getFluidState(BlockState blockState0) {
        return blockState0.m_61143_(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.m_5888_(blockState0);
    }

    public WoodType type() {
        return this.type;
    }

    public static WoodType getWoodType(Block block0) {
        WoodType $$1;
        if (block0 instanceof SignBlock) {
            $$1 = ((SignBlock) block0).type();
        } else {
            $$1 = WoodType.OAK;
        }
        return $$1;
    }

    public void openTextEdit(Player player0, SignBlockEntity signBlockEntity1, boolean boolean2) {
        signBlockEntity1.setAllowedPlayerEditor(player0.m_20148_());
        player0.openTextEdit(signBlockEntity1, boolean2);
    }

    private boolean otherPlayerIsEditingSign(Player player0, SignBlockEntity signBlockEntity1) {
        UUID $$2 = signBlockEntity1.getPlayerWhoMayEdit();
        return $$2 != null && !$$2.equals(player0.m_20148_());
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level0, BlockState blockState1, BlockEntityType<T> blockEntityTypeT2) {
        return m_152132_(blockEntityTypeT2, BlockEntityType.SIGN, SignBlockEntity::m_276836_);
    }
}