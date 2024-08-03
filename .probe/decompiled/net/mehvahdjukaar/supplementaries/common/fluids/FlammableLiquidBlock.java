package net.mehvahdjukaar.supplementaries.common.fluids;

import java.util.function.Supplier;
import java.util.stream.IntStream;
import net.mehvahdjukaar.moonlight.api.block.ILightable;
import net.mehvahdjukaar.supplementaries.common.block.blocks.GunpowderBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class FlammableLiquidBlock extends FiniteLiquidBlock implements ILightable {

    public static final IntegerProperty AGE = BlockStateProperties.AGE_15;

    public final VoxelShape[] interactionShapes = (VoxelShape[]) IntStream.range(0, 16).mapToObj(i -> m_49796_(0.0, 0.0, 0.0, 16.0, (double) Math.max(0.0F, 15.0F * (1.0F - (float) i / (float) this.maxLevel)), 16.0)).toArray(VoxelShape[]::new);

    public FlammableLiquidBlock(Supplier<? extends FiniteFluid> supplier, BlockBehaviour.Properties arg) {
        super(supplier, arg.lightLevel(state -> state.m_61143_(AGE) > 0 ? 15 : 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(AGE);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        return 60;
    }

    public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        return 300;
    }

    public void onCaughtFire(BlockState state, Level world, BlockPos pos, @Nullable Direction face, @Nullable LivingEntity igniter) {
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos currentPos, BlockPos neighborPos) {
        BlockState newState = super.updateShape(state, direction, neighborState, level, currentPos, neighborPos);
        if (GunpowderBlock.isFireSource(neighborState)) {
            newState = (BlockState) newState.m_61124_(AGE, 1);
        }
        return newState;
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        super.neighborChanged(state, level, pos, block, fromPos, isMoving);
        if (!level.isClientSide) {
            level.m_186460_(pos, this, this.getReactToFireDelay());
        }
    }

    @Override
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean moving) {
        super.onPlace(state, world, pos, oldState, moving);
        if (!oldState.m_60713_(state.m_60734_()) && !world.isClientSide) {
            world.m_186460_(pos, this, this.getReactToFireDelay());
        }
    }

    private int getReactToFireDelay() {
        return 2;
    }

    private int getFireTickDelay(RandomSource random) {
        return 30 + random.nextInt(10);
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        return this.interactWithPlayer(state, worldIn, pos, player, handIn);
    }

    @Override
    public boolean lightUp(@Nullable Entity player, BlockState state, BlockPos pos, LevelAccessor world, ILightable.FireSourceType fireSourceType) {
        return state.m_61143_(LEVEL) >= 10 && !world.m_6425_(pos.above()).getType().isSame(state.m_60819_().getType()) ? ILightable.super.lightUp(player, state, pos, world, fireSourceType) : false;
    }

    @Override
    public boolean isLitUp(BlockState state, BlockGetter level, BlockPos pos) {
        return isOnFire(state);
    }

    public static boolean isOnFire(BlockState state) {
        return (Integer) state.m_61143_(AGE) > 0;
    }

    @Override
    public void setLitUp(BlockState state, LevelAccessor world, BlockPos pos, boolean lit) {
        world.m_7731_(pos, (BlockState) state.m_61124_(AGE, lit ? 1 : 0), 3);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return isOnFire(state) ? this.interactionShapes[state.m_61143_(LEVEL)] : super.getShape(state, level, pos, context);
    }

    @Override
    public VoxelShape getInteractionShape(BlockState state, BlockGetter level, BlockPos pos) {
        return this.interactionShapes[state.m_61143_(LEVEL)];
    }

    @Override
    public void onProjectileHit(Level level, BlockState state, BlockHitResult pHit, Projectile projectile) {
        BlockPos pos = pHit.getBlockPos();
        this.interactWithProjectile(level, state, projectile, pos);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = this.m_49966_();
        BlockPos pos = context.getClickedPos();
        Level level = context.m_43725_();
        boolean shouldBeOnFire = false;
        for (Direction direction : context.getNearestLookingDirections()) {
            if (GunpowderBlock.isFireSource(level, pos.relative(direction))) {
                shouldBeOnFire = true;
                break;
            }
        }
        return (BlockState) state.m_61124_(AGE, shouldBeOnFire ? 1 : 0);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (isOnFire(state)) {
            if (random.nextInt(24) == 0) {
                level.playLocalSound((double) pos.m_123341_() + 0.5, (double) pos.m_123342_() + 0.5, (double) pos.m_123343_() + 0.5, SoundEvents.FIRE_AMBIENT, SoundSource.BLOCKS, 1.0F + random.nextFloat(), random.nextFloat() * 0.7F + 0.3F, false);
            }
            for (int i = 0; i < 3; i++) {
                double d = (double) pos.m_123341_() + random.nextDouble();
                double e = (double) pos.m_123342_() + random.nextDouble() * 0.5 + 0.5;
                double f = (double) pos.m_123343_() + random.nextDouble();
                level.addParticle(ParticleTypes.SMOKE, d, e, f, 0.0, 0.0, 0.0);
            }
        }
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (entity instanceof Projectile projectile) {
            this.interactWithProjectile(level, state, projectile, pos);
        }
        if (isOnFire(state)) {
            if (!entity.fireImmune()) {
                entity.setRemainingFireTicks(entity.getRemainingFireTicks() + 1);
                if (entity.getRemainingFireTicks() == 0) {
                    entity.setSecondsOnFire(8);
                }
            }
            entity.hurt(level.damageSources().inFire(), 1.0F);
        }
        super.m_7892_(state, level, pos, entity);
    }

    @Override
    protected void spawnDestroyParticles(Level level, Player player, BlockPos pos, BlockState state) {
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide() && isOnFire(state)) {
            level.m_5898_(null, 1009, pos, 0);
        }
        super.m_5707_(level, pos, state, player);
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        level.m_186460_(pos, this, this.getFireTickDelay(level.f_46441_));
        if (level.m_46469_().getBoolean(GameRules.RULE_DOFIRETICK) && isOnFire(state)) {
            int age = (Integer) state.m_61143_(AGE);
            int layers = (Integer) state.m_61143_(LEVEL);
            int ageAdd = random.nextInt(3) / 2;
            int ageIncrease = Math.min(15, age + ageAdd);
            if (age != ageIncrease) {
                state = (BlockState) state.m_61124_(AGE, ageIncrease);
                level.m_7731_(pos, state, 4);
            }
            boolean burnout = level.m_204166_(pos).is(BiomeTags.INCREASED_FIRE_BURNOUT);
            int k = burnout ? -50 : 0;
            new BlockPos.MutableBlockPos();
        }
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        super.randomTick(state, level, pos, random);
        if (isOnFire(state)) {
            Blocks.LAVA.m_213898_(Blocks.LAVA.defaultBlockState(), level, pos, random);
        }
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return true;
    }
}