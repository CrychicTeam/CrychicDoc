package com.sihenzhang.crockpot.block;

import com.sihenzhang.crockpot.base.FoodCategory;
import com.sihenzhang.crockpot.base.FoodValues;
import com.sihenzhang.crockpot.block.entity.BirdcageBlockEntity;
import com.sihenzhang.crockpot.block.entity.CrockPotBlockEntities;
import com.sihenzhang.crockpot.entity.Birdcage;
import com.sihenzhang.crockpot.entity.CrockPotEntities;
import com.sihenzhang.crockpot.recipe.CrockPotRecipes;
import com.sihenzhang.crockpot.recipe.FoodValuesDefinition;
import com.sihenzhang.crockpot.recipe.ParrotFeedingRecipe;
import com.sihenzhang.crockpot.util.I18nUtils;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BirdcageBlock extends BaseEntityBlock {

    public static final VoxelShape LOWER_SHAPE_WITHOUT_BASE = Block.box(1.0, 5.0, 1.0, 15.0, 16.0, 15.0);

    public static final VoxelShape LOWER_SHAPE = Shapes.or(Block.box(4.0, 0.0, 4.0, 12.0, 2.0, 12.0), Block.box(6.5, 2.0, 6.5, 9.5, 5.0, 9.5), LOWER_SHAPE_WITHOUT_BASE);

    public static final VoxelShape UPPER_SHAPE_WITHOUT_CHAIN = Block.box(1.0, 0.0, 1.0, 15.0, 9.0, 15.0);

    public static final VoxelShape UPPER_SHAPE = Shapes.or(UPPER_SHAPE_WITHOUT_CHAIN, Block.box(6.5, 9.0, 6.5, 9.5, 13.0, 9.5));

    public static final VoxelShape HANGING_UPPER_SHAPE = Shapes.or(UPPER_SHAPE_WITHOUT_CHAIN, Block.box(6.5, 9.0, 6.5, 9.5, 16.0, 9.5));

    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;

    public static final BooleanProperty HANGING = BlockStateProperties.HANGING;

    public BirdcageBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.GOLD).requiresCorrectToolForDrops().strength(3.0F, 6.0F).sound(SoundType.LANTERN).noOcclusion());
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(HALF, DoubleBlockHalf.LOWER)).m_61124_(HANGING, false));
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (this.getBlockEntity(pLevel, pPos, pState) instanceof BirdcageBlockEntity birdcageBlockEntity) {
            BlockPos lowerPos = pState.m_61143_(HALF) == DoubleBlockHalf.LOWER ? pPos : pPos.below();
            List<Parrot> parrots = pLevel.m_45976_(Parrot.class, new AABB((double) lowerPos.m_123341_(), (double) lowerPos.m_123342_(), (double) lowerPos.m_123343_(), (double) lowerPos.m_123341_() + 1.0, (double) lowerPos.m_123342_() + 2.0, (double) lowerPos.m_123343_() + 1.0));
            ItemStack stackInHand = pPlayer.m_21120_(pHand);
            if (parrots.isEmpty()) {
                if (pHand == InteractionHand.MAIN_HAND && stackInHand.isEmpty()) {
                    CompoundTag leftShoulderEntity = pPlayer.getShoulderEntityLeft();
                    CompoundTag rightShoulderEntity = pPlayer.getShoulderEntityRight();
                    if (!leftShoulderEntity.isEmpty() || !rightShoulderEntity.isEmpty()) {
                        boolean isLeftShoulder = !leftShoulderEntity.isEmpty();
                        Optional<Parrot> optionalParrot = EntityType.create(isLeftShoulder ? leftShoulderEntity : rightShoulderEntity, pLevel).filter(Parrot.class::isInstance).map(Parrot.class::cast);
                        Optional<Birdcage> optionalBirdcage = Optional.ofNullable(CrockPotEntities.BIRDCAGE.get().create(pLevel));
                        if (optionalParrot.isPresent() && optionalBirdcage.isPresent()) {
                            Parrot parrot = (Parrot) optionalParrot.get();
                            Birdcage birdcage = (Birdcage) optionalBirdcage.get();
                            if (!pLevel.isClientSide() && birdcageBlockEntity.captureParrot(pLevel, lowerPos, pPlayer, parrot, birdcage, isLeftShoulder)) {
                                return InteractionResult.SUCCESS;
                            }
                            return InteractionResult.CONSUME;
                        }
                    }
                }
            } else {
                Parrot parrot = (Parrot) parrots.get(0);
                if (pHand == InteractionHand.MAIN_HAND && stackInHand.isEmpty() && pPlayer.m_6144_()) {
                    if (pPlayer.m_20148_().equals(parrot.m_21805_())) {
                        if (!pLevel.isClientSide() && parrot.m_29895_((ServerPlayer) pPlayer)) {
                            return InteractionResult.SUCCESS;
                        }
                    } else {
                        pPlayer.displayClientMessage(I18nUtils.createTooltipComponent("birdcage.not_owner"), true);
                    }
                    return InteractionResult.CONSUME;
                }
                if (!birdcageBlockEntity.isOnCooldown()) {
                    FoodValues foodValues = FoodValuesDefinition.getFoodValues(stackInHand, pLevel);
                    if (foodValues.has(FoodCategory.MEAT)) {
                        if (!pLevel.isClientSide() && birdcageBlockEntity.fedByMeat(pPlayer.getAbilities().instabuild ? stackInHand.copy() : stackInHand, foodValues, parrot)) {
                            return InteractionResult.SUCCESS;
                        }
                        return InteractionResult.CONSUME;
                    }
                    Optional<ParrotFeedingRecipe> optionalParrotFeedingRecipe = pLevel.getRecipeManager().getRecipeFor(CrockPotRecipes.PARROT_FEEDING_RECIPE_TYPE.get(), new SimpleContainer(stackInHand), pLevel);
                    if (optionalParrotFeedingRecipe.isPresent()) {
                        if (!pLevel.isClientSide() && birdcageBlockEntity.fedByRecipe(pPlayer.getAbilities().instabuild ? stackInHand.copy() : stackInHand, (ParrotFeedingRecipe) optionalParrotFeedingRecipe.get(), pLevel.registryAccess(), parrot)) {
                            return InteractionResult.SUCCESS;
                        }
                        return InteractionResult.CONSUME;
                    }
                }
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public void playerWillDestroy(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer) {
        if (!pLevel.isClientSide() && pPlayer.isCreative() && pState.m_61143_(HALF) == DoubleBlockHalf.UPPER) {
            BlockPos lowerPos = pPos.below();
            BlockState lowerState = pLevel.getBlockState(lowerPos);
            if (lowerState.m_60713_(pState.m_60734_()) && lowerState.m_61143_(HALF) == DoubleBlockHalf.LOWER) {
                pLevel.setBlock(lowerPos, Blocks.AIR.defaultBlockState(), 35);
                pLevel.m_5898_(pPlayer, 2001, lowerPos, Block.getId(lowerState));
            }
        }
        super.m_5707_(pLevel, pPos, pState, pPlayer);
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (!pState.m_60713_(pNewState.m_60734_())) {
            if (pState.m_61138_(HALF) && pState.m_61143_(HALF) == DoubleBlockHalf.LOWER) {
                pLevel.m_45976_(Birdcage.class, new AABB((double) pPos.m_123341_(), (double) pPos.m_123342_(), (double) pPos.m_123343_(), (double) pPos.m_123341_() + 1.0, (double) pPos.m_123342_() + 2.0, (double) pPos.m_123343_() + 1.0)).forEach(Entity::m_146870_);
            }
            super.m_6810_(pState, pLevel, pPos, pNewState, pIsMoving);
        }
    }

    @Override
    public BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pNeighborPos) {
        if (pDirection != getConnectedDirection(pState)) {
            if (pDirection.getAxis() == Direction.Axis.Y) {
                boolean canSupport = Block.canSupportCenter(pLevel, pNeighborPos, pDirection.getOpposite());
                boolean lowerBlockHangingValueWithSupport = !canSupport;
                if (pState.m_61143_(HALF) == DoubleBlockHalf.LOWER && (Boolean) pState.m_61143_(HANGING) != lowerBlockHangingValueWithSupport) {
                    return (BlockState) pState.m_61124_(HANGING, lowerBlockHangingValueWithSupport);
                }
                if (pState.m_61143_(HALF) == DoubleBlockHalf.UPPER && (Boolean) pState.m_61143_(HANGING) != canSupport) {
                    return (BlockState) pState.m_61124_(HANGING, canSupport);
                }
            }
            return super.m_7417_(pState, pDirection, pNeighborState, pLevel, pCurrentPos, pNeighborPos);
        } else {
            return pNeighborState.m_60713_(this) && pNeighborState.m_61143_(HALF) != pState.m_61143_(HALF) ? pState : Blocks.AIR.defaultBlockState();
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        Level level = pContext.m_43725_();
        BlockPos clickedPos = pContext.getClickedPos();
        for (Direction direction : pContext.getNearestLookingDirections()) {
            if (direction.getAxis() == Direction.Axis.Y) {
                boolean canSupport = Block.canSupportCenter(level, clickedPos.relative(direction), direction.getOpposite());
                boolean lowerBlockHangingValueWithSupport = !canSupport;
                if (direction == Direction.UP) {
                    if (clickedPos.m_123342_() > level.m_141937_() && level.getBlockState(clickedPos.below()).m_60629_(pContext)) {
                        return (BlockState) ((BlockState) this.m_49966_().m_61124_(HALF, DoubleBlockHalf.UPPER)).m_61124_(HANGING, canSupport);
                    }
                } else if (clickedPos.m_123342_() < level.m_151558_() - 1 && level.getBlockState(clickedPos.above()).m_60629_(pContext)) {
                    return (BlockState) ((BlockState) this.m_49966_().m_61124_(HALF, DoubleBlockHalf.LOWER)).m_61124_(HANGING, lowerBlockHangingValueWithSupport);
                }
            }
        }
        return null;
    }

    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, @Nullable LivingEntity pPlacer, ItemStack pStack) {
        Direction neighbourDirection = getConnectedDirection(pState);
        BlockPos neighbourPos = pPos.relative(neighbourDirection);
        boolean neighbourCanSupport = Block.canSupportCenter(pLevel, neighbourPos.relative(neighbourDirection), neighbourDirection.getOpposite());
        pLevel.setBlockAndUpdate(neighbourPos, (BlockState) ((BlockState) pState.m_61124_(HALF, pState.m_61143_(HALF) == DoubleBlockHalf.LOWER ? DoubleBlockHalf.UPPER : DoubleBlockHalf.LOWER)).m_61124_(HANGING, neighbourDirection == Direction.UP == neighbourCanSupport));
    }

    public static Direction getConnectedDirection(BlockState pState) {
        return pState.m_61143_(HALF) == DoubleBlockHalf.LOWER ? Direction.UP : Direction.DOWN;
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        if ((Boolean) pState.m_61143_(HANGING)) {
            return pState.m_61143_(HALF) == DoubleBlockHalf.UPPER ? HANGING_UPPER_SHAPE : LOWER_SHAPE_WITHOUT_BASE;
        } else {
            return pState.m_61143_(HALF) == DoubleBlockHalf.UPPER ? UPPER_SHAPE : LOWER_SHAPE;
        }
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(HALF, HANGING);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return pState.m_61143_(HALF) == DoubleBlockHalf.LOWER ? new BirdcageBlockEntity(pPos, pState) : null;
    }

    public BlockEntity getBlockEntity(BlockGetter pLevel, BlockPos pPos, BlockState pState) {
        return pLevel.getBlockEntity(pState.m_61143_(HALF) == DoubleBlockHalf.LOWER ? pPos : pPos.below());
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return pLevel.isClientSide() ? null : m_152132_(pBlockEntityType, CrockPotBlockEntities.BIRDCAGE_BLOCK_ENTITY.get(), BirdcageBlockEntity::serverTick);
    }
}