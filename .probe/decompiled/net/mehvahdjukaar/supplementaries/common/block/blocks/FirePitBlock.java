package net.mehvahdjukaar.supplementaries.common.block.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class FirePitBlock extends LightUpWaterBlock {

    public static final BooleanProperty HANGING = BlockStateProperties.HANGING;

    protected static final VoxelShape SHAPE = Shapes.or(Block.box(1.0, 4.0, 1.0, 15.0, 9.0, 15.0), Block.box(2.0, 0.0, 2.0, 14.0, 4.0, 14.0));

    private final float fireDamage;

    public FirePitBlock(float fireDamage, BlockBehaviour.Properties properties) {
        super(properties);
        this.fireDamage = fireDamage;
        this.m_49959_((BlockState) this.m_49966_().m_61124_(HANGING, false));
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if ((Boolean) state.m_61143_(LIT) && entity instanceof LivingEntity && !EnchantmentHelper.hasFrostWalker((LivingEntity) entity)) {
            entity.hurt(level.damageSources().inFire(), this.fireDamage);
        }
        super.m_7892_(state, level, pos, entity);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        for (Direction direction : context.getNearestLookingDirections()) {
            if (direction.getAxis() == Direction.Axis.Y) {
                BlockState blockstate = (BlockState) this.m_49966_().m_61124_(HANGING, direction == Direction.UP);
                if (blockstate.m_60710_(context.m_43725_(), context.getClickedPos())) {
                    return super.getStateForPlacement(context);
                }
            }
        }
        return null;
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        super.updateShape(stateIn, facing, facingState, level, currentPos, facingPos);
        return getDir(stateIn).getOpposite() == facing && !stateIn.m_60710_(level, currentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(stateIn, facing, facingState, level, currentPos, facingPos);
    }

    private static Direction getDir(BlockState state) {
        return state.m_61143_(HANGING) ? Direction.DOWN : Direction.UP;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (random.nextInt(10) == 0) {
            level.playLocalSound((double) pos.m_123341_() + 0.5, (double) pos.m_123342_() + 0.5, (double) pos.m_123343_() + 0.5, SoundEvents.FIRE_AMBIENT, SoundSource.BLOCKS, 1.0F + random.nextFloat(), random.nextFloat() * 0.7F + 0.6F, false);
        }
    }

    public static void spawnSmokeParticles(Level level, BlockPos pos) {
        RandomSource random = level.getRandom();
        level.addParticle(ParticleTypes.SMOKE, (double) pos.m_123341_() + 0.5 + random.nextDouble() / 2.0 * (double) (random.nextBoolean() ? 1 : -1), (double) pos.m_123342_() + 0.4, (double) pos.m_123343_() + 0.5 + random.nextDouble() / 2.0 * (double) (random.nextBoolean() ? 1 : -1), 0.0, 0.005, 0.0);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        Direction direction = getDir(state).getOpposite();
        return Block.canSupportCenter(level, pos.relative(direction), direction.getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(HANGING);
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos pos, PathComputationType type) {
        return false;
    }

    @Nullable
    public BlockPathTypes getBlockPathType(BlockState state, BlockGetter level, BlockPos pos, @Nullable Mob entity) {
        return this.isLitUp(state, (LevelAccessor) level, pos) ? BlockPathTypes.DAMAGE_FIRE : null;
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        return state.m_61143_(LIT) ? 15 : 0;
    }
}