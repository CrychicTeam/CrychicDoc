package com.github.alexmodguy.alexscaves.server.block;

import com.github.alexmodguy.alexscaves.server.block.blockentity.NuclearFurnaceBlockEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACMath;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import com.github.alexmodguy.alexscaves.server.potion.ACEffectRegistry;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.WorldlyContainerHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class NuclearFurnaceComponentBlock extends Block implements WorldlyContainerHolder {

    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    private static final VoxelShape TOP_1_SHAPE = ACMath.buildShape(Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 9.0), Block.box(0.0, 0.0, 0.0, 9.0, 16.0, 16.0));

    private static final VoxelShape TOP_2_SHAPE = ACMath.buildShape(Block.box(0.0, 0.0, 7.0, 16.0, 16.0, 16.0), Block.box(0.0, 0.0, 0.0, 9.0, 16.0, 16.0));

    private static final VoxelShape TOP_3_SHAPE = ACMath.buildShape(Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 9.0), Block.box(7.0, 0.0, 0.0, 16.0, 16.0, 16.0));

    private static final VoxelShape TOP_4_SHAPE = ACMath.buildShape(Block.box(0.0, 0.0, 7.0, 16.0, 16.0, 16.0), Block.box(7.0, 0.0, 0.0, 16.0, 16.0, 16.0));

    public NuclearFurnaceComponentBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).pushReaction(PushReaction.BLOCK).strength(5.0F, 1001.0F).sound(ACSoundTypes.NUCLEAR_BOMB).noOcclusion().randomTicks());
        this.m_49959_((BlockState) this.m_49966_().m_61124_(ACTIVE, false));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        if ((Boolean) state.m_61143_(ACTIVE)) {
            BlockPos corner = getCornerForFurnace(getter, pos, true);
            if (corner != null && corner.m_123342_() == pos.m_123342_() - 1) {
                BlockPos sub = pos.subtract(corner);
                if (sub.m_123341_() == 0 && sub.m_123343_() == 0) {
                    return TOP_1_SHAPE;
                }
                if (sub.m_123341_() == 0 && sub.m_123343_() == 1) {
                    return TOP_2_SHAPE;
                }
                if (sub.m_123341_() == 1 && sub.m_123343_() == 0) {
                    return TOP_3_SHAPE;
                }
                if (sub.m_123341_() == 1 && sub.m_123343_() == 1) {
                    return TOP_4_SHAPE;
                }
            }
        }
        return super.m_5940_(state, getter, pos, context);
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos blockPos, Entity entity) {
        if ((Boolean) state.m_61143_(ACTIVE)) {
            BlockPos corner = getCornerForFurnace(level, blockPos, true);
            if (corner != null && corner.m_123342_() == blockPos.m_123342_() - 1 && level.getBlockEntity(corner) instanceof NuclearFurnaceBlockEntity furnace && furnace.isUndergoingFission() && entity instanceof LivingEntity living && !entity.getType().is(ACTagRegistry.RESISTS_RADIATION)) {
                living.addEffect(new MobEffectInstance(ACEffectRegistry.IRRADIATED.get(), 2000, 3));
            }
        }
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter getter, BlockPos blockPos, PathComputationType pathComputationType) {
        return false;
    }

    @Nullable
    public static BlockPos getCornerForFurnace(BlockGetter levelAccessor, BlockPos componentPos, boolean postConstruction) {
        if (postConstruction) {
            for (BlockPos pos : BlockPos.betweenClosed(componentPos.m_123341_() - 1, componentPos.m_123342_() - 1, componentPos.m_123343_() - 1, componentPos.m_123341_() + 1, componentPos.m_123342_() + 1, componentPos.m_123343_() + 1)) {
                if (levelAccessor.getBlockState(pos).m_60713_(ACBlockRegistry.NUCLEAR_FURNACE.get())) {
                    return pos;
                }
            }
            return null;
        } else {
            BlockPos furthest = componentPos;
            int j = 0;
            int maxDist;
            for (maxDist = 1; canBecomeAComponent(levelAccessor, furthest.west(), false) && j < maxDist; j++) {
                furthest = furthest.west();
            }
            for (int var7 = -1; canBecomeAComponent(levelAccessor, furthest.below(), false) && var7 < maxDist; var7++) {
                furthest = furthest.below();
            }
            for (int var8 = -1; canBecomeAComponent(levelAccessor, furthest.north(), false) && var8 < maxDist; var8++) {
                furthest = furthest.north();
            }
            return canBecomeAComponent(levelAccessor, furthest, false) ? furthest : null;
        }
    }

    public static boolean isCornerForFurnace(LevelReader levelAccessor, BlockPos componentPos, boolean checkMiddle, boolean active) {
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        for (int x = 0; x <= 1; x++) {
            for (int y = 0; y <= 1; y++) {
                for (int z = 0; z <= 1; z++) {
                    mutableBlockPos.set(componentPos.m_123341_() + x, componentPos.m_123342_() + y, componentPos.m_123343_() + z);
                    if (checkMiddle || x != 0 || y != 0 || z != 0) {
                        BlockState state = levelAccessor.m_8055_(mutableBlockPos);
                        if (!state.m_60713_(ACBlockRegistry.NUCLEAR_FURNACE_COMPONENT.get()) || (Boolean) state.m_61143_(ACTIVE) != active) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    public static void activateNeighbors(LevelAccessor levelAccessor, BlockPos cornerPos, boolean active) {
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        for (int x = 0; x <= 1; x++) {
            for (int y = 0; y <= 1; y++) {
                for (int z = 0; z <= 1; z++) {
                    mutableBlockPos.set(cornerPos.m_123341_() + x, cornerPos.m_123342_() + y, cornerPos.m_123343_() + z);
                    BlockState state = levelAccessor.m_8055_(mutableBlockPos);
                    if (state.m_60713_(ACBlockRegistry.NUCLEAR_FURNACE_COMPONENT.get())) {
                        levelAccessor.m_7731_(mutableBlockPos, (BlockState) ACBlockRegistry.NUCLEAR_FURNACE_COMPONENT.get().defaultBlockState().m_61124_(ACTIVE, active), 3);
                    }
                }
            }
        }
    }

    @Override
    public void setPlacedBy(Level level, BlockPos blockPos, BlockState state, @Nullable LivingEntity living, ItemStack itemStack) {
        BlockPos corner = getCornerForFurnace(level, blockPos, false);
        if (corner != null && isCornerForFurnace(level, corner, true, false)) {
            Direction facing = living == null ? Direction.NORTH : living.m_6350_().getOpposite();
            level.setBlockAndUpdate(corner, (BlockState) ACBlockRegistry.NUCLEAR_FURNACE.get().defaultBlockState().m_61124_(NuclearFurnaceBlock.FACING, facing));
            activateNeighbors(level, corner, true);
        }
    }

    public static boolean canBecomeAComponent(BlockGetter levelAccessor, BlockPos componentPos, boolean postConstruction) {
        BlockState state = levelAccessor.getBlockState(componentPos);
        return postConstruction ? state.m_60713_(ACBlockRegistry.NUCLEAR_FURNACE_COMPONENT.get()) || state.m_60713_(ACBlockRegistry.NUCLEAR_FURNACE.get()) : state.m_60713_(ACBlockRegistry.NUCLEAR_FURNACE_COMPONENT.get()) && !(Boolean) state.m_61143_(ACTIVE);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> blockStateBuilder) {
        blockStateBuilder.add(ACTIVE);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        if (!(Boolean) state.m_61143_(ACTIVE)) {
            return true;
        } else {
            BlockPos corner = getCornerForFurnace(level, pos, true);
            return corner != null && level.m_8055_(corner).m_60713_(ACBlockRegistry.NUCLEAR_FURNACE.get()) && isCornerForFurnace(level, corner, false, true);
        }
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState state1, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos1) {
        if (!state.m_60710_(levelAccessor, blockPos)) {
            this.checkCriticalityExplosion(levelAccessor, blockPos);
            return Blocks.AIR.defaultBlockState();
        } else {
            return super.m_7417_(state, direction, state1, levelAccessor, blockPos, blockPos1);
        }
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos blockPos, BlockState state, @Nullable BlockEntity entity, ItemStack itemStack) {
        this.checkCriticalityExplosion(level, blockPos);
        super.playerDestroy(level, player, blockPos, state, entity, itemStack);
    }

    private void checkCriticalityExplosion(LevelReader level, BlockPos pos) {
        BlockState state = level.m_8055_(pos);
        if (state.m_60713_(this) && (Boolean) state.m_61143_(ACTIVE)) {
            BlockPos corner = getCornerForFurnace(level, pos, true);
            if (corner != null && level.m_7702_(corner) instanceof NuclearFurnaceBlockEntity nuclearFurnaceBlockEntity && (float) nuclearFurnaceBlockEntity.getCriticality() >= 2.0F) {
                nuclearFurnaceBlockEntity.destroyWhileCritical(false);
            }
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult result) {
        if ((Boolean) state.m_61143_(ACTIVE) && !player.m_6144_()) {
            BlockPos corner = getCornerForFurnace(level, blockPos, true);
            if (corner != null && level.getBlockState(corner).m_60713_(ACBlockRegistry.NUCLEAR_FURNACE.get()) && isCornerForFurnace(level, corner, false, true) && level.getBlockEntity(corner) instanceof NuclearFurnaceBlockEntity nuclearFurnaceBlockEntity) {
                if (level.isClientSide) {
                    return InteractionResult.SUCCESS;
                }
                if (this.canSurvive(state, level, blockPos)) {
                    player.openMenu(nuclearFurnaceBlockEntity);
                    nuclearFurnaceBlockEntity.onPlayerUse(player);
                    player.awardStat(Stats.INTERACT_WITH_FURNACE);
                    return InteractionResult.CONSUME;
                }
            }
        }
        return super.m_6227_(state, level, blockPos, player, hand, result);
    }

    @Override
    public WorldlyContainer getContainer(BlockState state, LevelAccessor levelAccessor, BlockPos blockPos) {
        if ((Boolean) state.m_61143_(ACTIVE)) {
            BlockPos corner = getCornerForFurnace(levelAccessor, blockPos, true);
            if (corner != null && levelAccessor.m_7702_(corner) instanceof NuclearFurnaceBlockEntity nuclearFurnaceBlockEntity) {
                return nuclearFurnaceBlockEntity.getContainerFor(blockPos.subtract(corner));
            }
        }
        return null;
    }
}