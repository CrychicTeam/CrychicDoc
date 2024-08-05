package net.minecraft.world.level.block;

import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CampfireBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {

    protected static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 7.0, 16.0);

    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    public static final BooleanProperty SIGNAL_FIRE = BlockStateProperties.SIGNAL_FIRE;

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    private static final VoxelShape VIRTUAL_FENCE_POST = Block.box(6.0, 0.0, 6.0, 10.0, 16.0, 10.0);

    private static final int SMOKE_DISTANCE = 5;

    private final boolean spawnParticles;

    private final int fireDamage;

    public CampfireBlock(boolean boolean0, int int1, BlockBehaviour.Properties blockBehaviourProperties2) {
        super(blockBehaviourProperties2);
        this.spawnParticles = boolean0;
        this.fireDamage = int1;
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(LIT, true)).m_61124_(SIGNAL_FIRE, false)).m_61124_(WATERLOGGED, false)).m_61124_(FACING, Direction.NORTH));
    }

    @Override
    public InteractionResult use(BlockState blockState0, Level level1, BlockPos blockPos2, Player player3, InteractionHand interactionHand4, BlockHitResult blockHitResult5) {
        if (level1.getBlockEntity(blockPos2) instanceof CampfireBlockEntity $$7) {
            ItemStack $$8 = player3.m_21120_(interactionHand4);
            Optional<CampfireCookingRecipe> $$9 = $$7.getCookableRecipe($$8);
            if ($$9.isPresent()) {
                if (!level1.isClientSide && $$7.placeFood(player3, player3.getAbilities().instabuild ? $$8.copy() : $$8, ((CampfireCookingRecipe) $$9.get()).m_43753_())) {
                    player3.awardStat(Stats.INTERACT_WITH_CAMPFIRE);
                    return InteractionResult.SUCCESS;
                }
                return InteractionResult.CONSUME;
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public void entityInside(BlockState blockState0, Level level1, BlockPos blockPos2, Entity entity3) {
        if ((Boolean) blockState0.m_61143_(LIT) && entity3 instanceof LivingEntity && !EnchantmentHelper.hasFrostWalker((LivingEntity) entity3)) {
            entity3.hurt(level1.damageSources().inFire(), (float) this.fireDamage);
        }
        super.m_7892_(blockState0, level1, blockPos2, entity3);
    }

    @Override
    public void onRemove(BlockState blockState0, Level level1, BlockPos blockPos2, BlockState blockState3, boolean boolean4) {
        if (!blockState0.m_60713_(blockState3.m_60734_())) {
            BlockEntity $$5 = level1.getBlockEntity(blockPos2);
            if ($$5 instanceof CampfireBlockEntity) {
                Containers.dropContents(level1, blockPos2, ((CampfireBlockEntity) $$5).getItems());
            }
            super.m_6810_(blockState0, level1, blockPos2, blockState3, boolean4);
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext0) {
        LevelAccessor $$1 = blockPlaceContext0.m_43725_();
        BlockPos $$2 = blockPlaceContext0.getClickedPos();
        boolean $$3 = $$1.m_6425_($$2).getType() == Fluids.WATER;
        return (BlockState) ((BlockState) ((BlockState) ((BlockState) this.m_49966_().m_61124_(WATERLOGGED, $$3)).m_61124_(SIGNAL_FIRE, this.isSmokeSource($$1.m_8055_($$2.below())))).m_61124_(LIT, !$$3)).m_61124_(FACING, blockPlaceContext0.m_8125_());
    }

    @Override
    public BlockState updateShape(BlockState blockState0, Direction direction1, BlockState blockState2, LevelAccessor levelAccessor3, BlockPos blockPos4, BlockPos blockPos5) {
        if ((Boolean) blockState0.m_61143_(WATERLOGGED)) {
            levelAccessor3.scheduleTick(blockPos4, Fluids.WATER, Fluids.WATER.m_6718_(levelAccessor3));
        }
        return direction1 == Direction.DOWN ? (BlockState) blockState0.m_61124_(SIGNAL_FIRE, this.isSmokeSource(blockState2)) : super.m_7417_(blockState0, direction1, blockState2, levelAccessor3, blockPos4, blockPos5);
    }

    private boolean isSmokeSource(BlockState blockState0) {
        return blockState0.m_60713_(Blocks.HAY_BLOCK);
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return SHAPE;
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState0) {
        return RenderShape.MODEL;
    }

    @Override
    public void animateTick(BlockState blockState0, Level level1, BlockPos blockPos2, RandomSource randomSource3) {
        if ((Boolean) blockState0.m_61143_(LIT)) {
            if (randomSource3.nextInt(10) == 0) {
                level1.playLocalSound((double) blockPos2.m_123341_() + 0.5, (double) blockPos2.m_123342_() + 0.5, (double) blockPos2.m_123343_() + 0.5, SoundEvents.CAMPFIRE_CRACKLE, SoundSource.BLOCKS, 0.5F + randomSource3.nextFloat(), randomSource3.nextFloat() * 0.7F + 0.6F, false);
            }
            if (this.spawnParticles && randomSource3.nextInt(5) == 0) {
                for (int $$4 = 0; $$4 < randomSource3.nextInt(1) + 1; $$4++) {
                    level1.addParticle(ParticleTypes.LAVA, (double) blockPos2.m_123341_() + 0.5, (double) blockPos2.m_123342_() + 0.5, (double) blockPos2.m_123343_() + 0.5, (double) (randomSource3.nextFloat() / 2.0F), 5.0E-5, (double) (randomSource3.nextFloat() / 2.0F));
                }
            }
        }
    }

    public static void dowse(@Nullable Entity entity0, LevelAccessor levelAccessor1, BlockPos blockPos2, BlockState blockState3) {
        if (levelAccessor1.m_5776_()) {
            for (int $$4 = 0; $$4 < 20; $$4++) {
                makeParticles((Level) levelAccessor1, blockPos2, (Boolean) blockState3.m_61143_(SIGNAL_FIRE), true);
            }
        }
        BlockEntity $$5 = levelAccessor1.m_7702_(blockPos2);
        if ($$5 instanceof CampfireBlockEntity) {
            ((CampfireBlockEntity) $$5).dowse();
        }
        levelAccessor1.gameEvent(entity0, GameEvent.BLOCK_CHANGE, blockPos2);
    }

    @Override
    public boolean placeLiquid(LevelAccessor levelAccessor0, BlockPos blockPos1, BlockState blockState2, FluidState fluidState3) {
        if (!(Boolean) blockState2.m_61143_(BlockStateProperties.WATERLOGGED) && fluidState3.getType() == Fluids.WATER) {
            boolean $$4 = (Boolean) blockState2.m_61143_(LIT);
            if ($$4) {
                if (!levelAccessor0.m_5776_()) {
                    levelAccessor0.playSound(null, blockPos1, SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.BLOCKS, 1.0F, 1.0F);
                }
                dowse(null, levelAccessor0, blockPos1, blockState2);
            }
            levelAccessor0.m_7731_(blockPos1, (BlockState) ((BlockState) blockState2.m_61124_(WATERLOGGED, true)).m_61124_(LIT, false), 3);
            levelAccessor0.scheduleTick(blockPos1, fluidState3.getType(), fluidState3.getType().getTickDelay(levelAccessor0));
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onProjectileHit(Level level0, BlockState blockState1, BlockHitResult blockHitResult2, Projectile projectile3) {
        BlockPos $$4 = blockHitResult2.getBlockPos();
        if (!level0.isClientSide && projectile3.m_6060_() && projectile3.mayInteract(level0, $$4) && !(Boolean) blockState1.m_61143_(LIT) && !(Boolean) blockState1.m_61143_(WATERLOGGED)) {
            level0.setBlock($$4, (BlockState) blockState1.m_61124_(BlockStateProperties.LIT, true), 11);
        }
    }

    public static void makeParticles(Level level0, BlockPos blockPos1, boolean boolean2, boolean boolean3) {
        RandomSource $$4 = level0.getRandom();
        SimpleParticleType $$5 = boolean2 ? ParticleTypes.CAMPFIRE_SIGNAL_SMOKE : ParticleTypes.CAMPFIRE_COSY_SMOKE;
        level0.addAlwaysVisibleParticle($$5, true, (double) blockPos1.m_123341_() + 0.5 + $$4.nextDouble() / 3.0 * (double) ($$4.nextBoolean() ? 1 : -1), (double) blockPos1.m_123342_() + $$4.nextDouble() + $$4.nextDouble(), (double) blockPos1.m_123343_() + 0.5 + $$4.nextDouble() / 3.0 * (double) ($$4.nextBoolean() ? 1 : -1), 0.0, 0.07, 0.0);
        if (boolean3) {
            level0.addParticle(ParticleTypes.SMOKE, (double) blockPos1.m_123341_() + 0.5 + $$4.nextDouble() / 4.0 * (double) ($$4.nextBoolean() ? 1 : -1), (double) blockPos1.m_123342_() + 0.4, (double) blockPos1.m_123343_() + 0.5 + $$4.nextDouble() / 4.0 * (double) ($$4.nextBoolean() ? 1 : -1), 0.0, 0.005, 0.0);
        }
    }

    public static boolean isSmokeyPos(Level level0, BlockPos blockPos1) {
        for (int $$2 = 1; $$2 <= 5; $$2++) {
            BlockPos $$3 = blockPos1.below($$2);
            BlockState $$4 = level0.getBlockState($$3);
            if (isLitCampfire($$4)) {
                return true;
            }
            boolean $$5 = Shapes.joinIsNotEmpty(VIRTUAL_FENCE_POST, $$4.m_60742_(level0, blockPos1, CollisionContext.empty()), BooleanOp.AND);
            if ($$5) {
                BlockState $$6 = level0.getBlockState($$3.below());
                return isLitCampfire($$6);
            }
        }
        return false;
    }

    public static boolean isLitCampfire(BlockState blockState0) {
        return blockState0.m_61138_(LIT) && blockState0.m_204336_(BlockTags.CAMPFIRES) && (Boolean) blockState0.m_61143_(LIT);
    }

    @Override
    public FluidState getFluidState(BlockState blockState0) {
        return blockState0.m_61143_(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.m_5888_(blockState0);
    }

    @Override
    public BlockState rotate(BlockState blockState0, Rotation rotation1) {
        return (BlockState) blockState0.m_61124_(FACING, rotation1.rotate((Direction) blockState0.m_61143_(FACING)));
    }

    @Override
    public BlockState mirror(BlockState blockState0, Mirror mirror1) {
        return blockState0.m_60717_(mirror1.getRotation((Direction) blockState0.m_61143_(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(LIT, SIGNAL_FIRE, WATERLOGGED, FACING);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos0, BlockState blockState1) {
        return new CampfireBlockEntity(blockPos0, blockState1);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level0, BlockState blockState1, BlockEntityType<T> blockEntityTypeT2) {
        if (level0.isClientSide) {
            return blockState1.m_61143_(LIT) ? m_152132_(blockEntityTypeT2, BlockEntityType.CAMPFIRE, CampfireBlockEntity::m_155318_) : null;
        } else {
            return blockState1.m_61143_(LIT) ? m_152132_(blockEntityTypeT2, BlockEntityType.CAMPFIRE, CampfireBlockEntity::m_155306_) : m_152132_(blockEntityTypeT2, BlockEntityType.CAMPFIRE, CampfireBlockEntity::m_155313_);
        }
    }

    @Override
    public boolean isPathfindable(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, PathComputationType pathComputationType3) {
        return false;
    }

    public static boolean canLight(BlockState blockState0) {
        return blockState0.m_204338_(BlockTags.CAMPFIRES, p_51262_ -> p_51262_.m_61138_(WATERLOGGED) && p_51262_.m_61138_(LIT)) && !(Boolean) blockState0.m_61143_(WATERLOGGED) && !(Boolean) blockState0.m_61143_(LIT);
    }
}