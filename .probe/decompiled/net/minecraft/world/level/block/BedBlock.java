package net.minecraft.world.level.block;

import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.CollisionGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BedBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.apache.commons.lang3.ArrayUtils;

public class BedBlock extends HorizontalDirectionalBlock implements EntityBlock {

    public static final EnumProperty<BedPart> PART = BlockStateProperties.BED_PART;

    public static final BooleanProperty OCCUPIED = BlockStateProperties.OCCUPIED;

    protected static final int HEIGHT = 9;

    protected static final VoxelShape BASE = Block.box(0.0, 3.0, 0.0, 16.0, 9.0, 16.0);

    private static final int LEG_WIDTH = 3;

    protected static final VoxelShape LEG_NORTH_WEST = Block.box(0.0, 0.0, 0.0, 3.0, 3.0, 3.0);

    protected static final VoxelShape LEG_SOUTH_WEST = Block.box(0.0, 0.0, 13.0, 3.0, 3.0, 16.0);

    protected static final VoxelShape LEG_NORTH_EAST = Block.box(13.0, 0.0, 0.0, 16.0, 3.0, 3.0);

    protected static final VoxelShape LEG_SOUTH_EAST = Block.box(13.0, 0.0, 13.0, 16.0, 3.0, 16.0);

    protected static final VoxelShape NORTH_SHAPE = Shapes.or(BASE, LEG_NORTH_WEST, LEG_NORTH_EAST);

    protected static final VoxelShape SOUTH_SHAPE = Shapes.or(BASE, LEG_SOUTH_WEST, LEG_SOUTH_EAST);

    protected static final VoxelShape WEST_SHAPE = Shapes.or(BASE, LEG_NORTH_WEST, LEG_SOUTH_WEST);

    protected static final VoxelShape EAST_SHAPE = Shapes.or(BASE, LEG_NORTH_EAST, LEG_SOUTH_EAST);

    private final DyeColor color;

    public BedBlock(DyeColor dyeColor0, BlockBehaviour.Properties blockBehaviourProperties1) {
        super(blockBehaviourProperties1);
        this.color = dyeColor0;
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(PART, BedPart.FOOT)).m_61124_(OCCUPIED, false));
    }

    @Nullable
    public static Direction getBedOrientation(BlockGetter blockGetter0, BlockPos blockPos1) {
        BlockState $$2 = blockGetter0.getBlockState(blockPos1);
        return $$2.m_60734_() instanceof BedBlock ? (Direction) $$2.m_61143_(f_54117_) : null;
    }

    @Override
    public InteractionResult use(BlockState blockState0, Level level1, BlockPos blockPos2, Player player3, InteractionHand interactionHand4, BlockHitResult blockHitResult5) {
        if (level1.isClientSide) {
            return InteractionResult.CONSUME;
        } else {
            if (blockState0.m_61143_(PART) != BedPart.HEAD) {
                blockPos2 = blockPos2.relative((Direction) blockState0.m_61143_(f_54117_));
                blockState0 = level1.getBlockState(blockPos2);
                if (!blockState0.m_60713_(this)) {
                    return InteractionResult.CONSUME;
                }
            }
            if (!canSetSpawn(level1)) {
                level1.removeBlock(blockPos2, false);
                BlockPos $$6 = blockPos2.relative(((Direction) blockState0.m_61143_(f_54117_)).getOpposite());
                if (level1.getBlockState($$6).m_60713_(this)) {
                    level1.removeBlock($$6, false);
                }
                Vec3 $$7 = blockPos2.getCenter();
                level1.explode(null, level1.damageSources().badRespawnPointExplosion($$7), null, $$7, 5.0F, true, Level.ExplosionInteraction.BLOCK);
                return InteractionResult.SUCCESS;
            } else if ((Boolean) blockState0.m_61143_(OCCUPIED)) {
                if (!this.kickVillagerOutOfBed(level1, blockPos2)) {
                    player3.displayClientMessage(Component.translatable("block.minecraft.bed.occupied"), true);
                }
                return InteractionResult.SUCCESS;
            } else {
                player3.startSleepInBed(blockPos2).ifLeft(p_49477_ -> {
                    if (p_49477_.getMessage() != null) {
                        player3.displayClientMessage(p_49477_.getMessage(), true);
                    }
                });
                return InteractionResult.SUCCESS;
            }
        }
    }

    public static boolean canSetSpawn(Level level0) {
        return level0.dimensionType().bedWorks();
    }

    private boolean kickVillagerOutOfBed(Level level0, BlockPos blockPos1) {
        List<Villager> $$2 = level0.m_6443_(Villager.class, new AABB(blockPos1), LivingEntity::m_5803_);
        if ($$2.isEmpty()) {
            return false;
        } else {
            ((Villager) $$2.get(0)).stopSleeping();
            return true;
        }
    }

    @Override
    public void fallOn(Level level0, BlockState blockState1, BlockPos blockPos2, Entity entity3, float float4) {
        super.m_142072_(level0, blockState1, blockPos2, entity3, float4 * 0.5F);
    }

    @Override
    public void updateEntityAfterFallOn(BlockGetter blockGetter0, Entity entity1) {
        if (entity1.isSuppressingBounce()) {
            super.m_5548_(blockGetter0, entity1);
        } else {
            this.bounceUp(entity1);
        }
    }

    private void bounceUp(Entity entity0) {
        Vec3 $$1 = entity0.getDeltaMovement();
        if ($$1.y < 0.0) {
            double $$2 = entity0 instanceof LivingEntity ? 1.0 : 0.8;
            entity0.setDeltaMovement($$1.x, -$$1.y * 0.66F * $$2, $$1.z);
        }
    }

    @Override
    public BlockState updateShape(BlockState blockState0, Direction direction1, BlockState blockState2, LevelAccessor levelAccessor3, BlockPos blockPos4, BlockPos blockPos5) {
        if (direction1 == getNeighbourDirection((BedPart) blockState0.m_61143_(PART), (Direction) blockState0.m_61143_(f_54117_))) {
            return blockState2.m_60713_(this) && blockState2.m_61143_(PART) != blockState0.m_61143_(PART) ? (BlockState) blockState0.m_61124_(OCCUPIED, (Boolean) blockState2.m_61143_(OCCUPIED)) : Blocks.AIR.defaultBlockState();
        } else {
            return super.m_7417_(blockState0, direction1, blockState2, levelAccessor3, blockPos4, blockPos5);
        }
    }

    private static Direction getNeighbourDirection(BedPart bedPart0, Direction direction1) {
        return bedPart0 == BedPart.FOOT ? direction1 : direction1.getOpposite();
    }

    @Override
    public void playerWillDestroy(Level level0, BlockPos blockPos1, BlockState blockState2, Player player3) {
        if (!level0.isClientSide && player3.isCreative()) {
            BedPart $$4 = (BedPart) blockState2.m_61143_(PART);
            if ($$4 == BedPart.FOOT) {
                BlockPos $$5 = blockPos1.relative(getNeighbourDirection($$4, (Direction) blockState2.m_61143_(f_54117_)));
                BlockState $$6 = level0.getBlockState($$5);
                if ($$6.m_60713_(this) && $$6.m_61143_(PART) == BedPart.HEAD) {
                    level0.setBlock($$5, Blocks.AIR.defaultBlockState(), 35);
                    level0.m_5898_(player3, 2001, $$5, Block.getId($$6));
                }
            }
        }
        super.m_5707_(level0, blockPos1, blockState2, player3);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext0) {
        Direction $$1 = blockPlaceContext0.m_8125_();
        BlockPos $$2 = blockPlaceContext0.getClickedPos();
        BlockPos $$3 = $$2.relative($$1);
        Level $$4 = blockPlaceContext0.m_43725_();
        return $$4.getBlockState($$3).m_60629_(blockPlaceContext0) && $$4.getWorldBorder().isWithinBounds($$3) ? (BlockState) this.m_49966_().m_61124_(f_54117_, $$1) : null;
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        Direction $$4 = getConnectedDirection(blockState0).getOpposite();
        switch($$4) {
            case NORTH:
                return NORTH_SHAPE;
            case SOUTH:
                return SOUTH_SHAPE;
            case WEST:
                return WEST_SHAPE;
            default:
                return EAST_SHAPE;
        }
    }

    public static Direction getConnectedDirection(BlockState blockState0) {
        Direction $$1 = (Direction) blockState0.m_61143_(f_54117_);
        return blockState0.m_61143_(PART) == BedPart.HEAD ? $$1.getOpposite() : $$1;
    }

    public static DoubleBlockCombiner.BlockType getBlockType(BlockState blockState0) {
        BedPart $$1 = (BedPart) blockState0.m_61143_(PART);
        return $$1 == BedPart.HEAD ? DoubleBlockCombiner.BlockType.FIRST : DoubleBlockCombiner.BlockType.SECOND;
    }

    private static boolean isBunkBed(BlockGetter blockGetter0, BlockPos blockPos1) {
        return blockGetter0.getBlockState(blockPos1.below()).m_60734_() instanceof BedBlock;
    }

    public static Optional<Vec3> findStandUpPosition(EntityType<?> entityType0, CollisionGetter collisionGetter1, BlockPos blockPos2, Direction direction3, float float4) {
        Direction $$5 = direction3.getClockWise();
        Direction $$6 = $$5.isFacingAngle(float4) ? $$5.getOpposite() : $$5;
        if (isBunkBed(collisionGetter1, blockPos2)) {
            return findBunkBedStandUpPosition(entityType0, collisionGetter1, blockPos2, direction3, $$6);
        } else {
            int[][] $$7 = bedStandUpOffsets(direction3, $$6);
            Optional<Vec3> $$8 = findStandUpPositionAtOffset(entityType0, collisionGetter1, blockPos2, $$7, true);
            return $$8.isPresent() ? $$8 : findStandUpPositionAtOffset(entityType0, collisionGetter1, blockPos2, $$7, false);
        }
    }

    private static Optional<Vec3> findBunkBedStandUpPosition(EntityType<?> entityType0, CollisionGetter collisionGetter1, BlockPos blockPos2, Direction direction3, Direction direction4) {
        int[][] $$5 = bedSurroundStandUpOffsets(direction3, direction4);
        Optional<Vec3> $$6 = findStandUpPositionAtOffset(entityType0, collisionGetter1, blockPos2, $$5, true);
        if ($$6.isPresent()) {
            return $$6;
        } else {
            BlockPos $$7 = blockPos2.below();
            Optional<Vec3> $$8 = findStandUpPositionAtOffset(entityType0, collisionGetter1, $$7, $$5, true);
            if ($$8.isPresent()) {
                return $$8;
            } else {
                int[][] $$9 = bedAboveStandUpOffsets(direction3);
                Optional<Vec3> $$10 = findStandUpPositionAtOffset(entityType0, collisionGetter1, blockPos2, $$9, true);
                if ($$10.isPresent()) {
                    return $$10;
                } else {
                    Optional<Vec3> $$11 = findStandUpPositionAtOffset(entityType0, collisionGetter1, blockPos2, $$5, false);
                    if ($$11.isPresent()) {
                        return $$11;
                    } else {
                        Optional<Vec3> $$12 = findStandUpPositionAtOffset(entityType0, collisionGetter1, $$7, $$5, false);
                        return $$12.isPresent() ? $$12 : findStandUpPositionAtOffset(entityType0, collisionGetter1, blockPos2, $$9, false);
                    }
                }
            }
        }
    }

    private static Optional<Vec3> findStandUpPositionAtOffset(EntityType<?> entityType0, CollisionGetter collisionGetter1, BlockPos blockPos2, int[][] int3, boolean boolean4) {
        BlockPos.MutableBlockPos $$5 = new BlockPos.MutableBlockPos();
        for (int[] $$6 : int3) {
            $$5.set(blockPos2.m_123341_() + $$6[0], blockPos2.m_123342_(), blockPos2.m_123343_() + $$6[1]);
            Vec3 $$7 = DismountHelper.findSafeDismountLocation(entityType0, collisionGetter1, $$5, boolean4);
            if ($$7 != null) {
                return Optional.of($$7);
            }
        }
        return Optional.empty();
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState0) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(f_54117_, PART, OCCUPIED);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos0, BlockState blockState1) {
        return new BedBlockEntity(blockPos0, blockState1, this.color);
    }

    @Override
    public void setPlacedBy(Level level0, BlockPos blockPos1, BlockState blockState2, @Nullable LivingEntity livingEntity3, ItemStack itemStack4) {
        super.m_6402_(level0, blockPos1, blockState2, livingEntity3, itemStack4);
        if (!level0.isClientSide) {
            BlockPos $$5 = blockPos1.relative((Direction) blockState2.m_61143_(f_54117_));
            level0.setBlock($$5, (BlockState) blockState2.m_61124_(PART, BedPart.HEAD), 3);
            level0.m_6289_(blockPos1, Blocks.AIR);
            blockState2.m_60701_(level0, blockPos1, 3);
        }
    }

    public DyeColor getColor() {
        return this.color;
    }

    @Override
    public long getSeed(BlockState blockState0, BlockPos blockPos1) {
        BlockPos $$2 = blockPos1.relative((Direction) blockState0.m_61143_(f_54117_), blockState0.m_61143_(PART) == BedPart.HEAD ? 0 : 1);
        return Mth.getSeed($$2.m_123341_(), blockPos1.m_123342_(), $$2.m_123343_());
    }

    @Override
    public boolean isPathfindable(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, PathComputationType pathComputationType3) {
        return false;
    }

    private static int[][] bedStandUpOffsets(Direction direction0, Direction direction1) {
        return (int[][]) ArrayUtils.addAll(bedSurroundStandUpOffsets(direction0, direction1), bedAboveStandUpOffsets(direction0));
    }

    private static int[][] bedSurroundStandUpOffsets(Direction direction0, Direction direction1) {
        return new int[][] { { direction1.getStepX(), direction1.getStepZ() }, { direction1.getStepX() - direction0.getStepX(), direction1.getStepZ() - direction0.getStepZ() }, { direction1.getStepX() - direction0.getStepX() * 2, direction1.getStepZ() - direction0.getStepZ() * 2 }, { -direction0.getStepX() * 2, -direction0.getStepZ() * 2 }, { -direction1.getStepX() - direction0.getStepX() * 2, -direction1.getStepZ() - direction0.getStepZ() * 2 }, { -direction1.getStepX() - direction0.getStepX(), -direction1.getStepZ() - direction0.getStepZ() }, { -direction1.getStepX(), -direction1.getStepZ() }, { -direction1.getStepX() + direction0.getStepX(), -direction1.getStepZ() + direction0.getStepZ() }, { direction0.getStepX(), direction0.getStepZ() }, { direction1.getStepX() + direction0.getStepX(), direction1.getStepZ() + direction0.getStepZ() } };
    }

    private static int[][] bedAboveStandUpOffsets(Direction direction0) {
        return new int[][] { { 0, 0 }, { -direction0.getStepX(), -direction0.getStepZ() } };
    }
}