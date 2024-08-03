package net.mehvahdjukaar.supplementaries.common.block.blocks;

import java.util.Arrays;
import net.mehvahdjukaar.moonlight.api.events.IFireConsumeBlockEvent;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.supplementaries.common.entities.FallingAshEntity;
import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.mehvahdjukaar.supplementaries.reg.ModParticles;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class AshLayerBlock extends FallingBlock {

    private static final int MAX_LAYERS = 8;

    public static final IntegerProperty LAYERS = BlockStateProperties.LAYERS;

    protected static final VoxelShape[] SHAPE_BY_LAYER = new VoxelShape[9];

    public static final ThreadLocal<Boolean> RECURSION_HACK = ThreadLocal.withInitial(() -> false);

    public static final int GRASS_SPREAD_WIDTH = 3;

    public AshLayerBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.m_49959_((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(LAYERS, 1));
    }

    @Override
    public void onProjectileHit(Level level, BlockState state, BlockHitResult pHit, Projectile projectile) {
        BlockPos pos = pHit.getBlockPos();
        if (projectile instanceof ThrownPotion potion && PotionUtils.getPotion(potion.m_7846_()) == Potions.WATER) {
            Entity entity = projectile.getOwner();
            boolean flag = entity == null || entity instanceof Player || PlatHelper.isMobGriefingOn(level, entity);
            if (flag) {
                this.removeOneLayer(state, pos, level);
            }
        }
    }

    @Override
    public int getDustColor(BlockState state, BlockGetter reader, BlockPos pos) {
        return 10129552;
    }

    @Override
    public void onPlace(BlockState state, Level worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (state.m_60734_() != oldState.m_60734_()) {
            worldIn.m_186460_(pos, this, this.m_7198_());
        }
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE_BY_LAYER[pState.m_61143_(LAYERS)];
    }

    @Override
    public VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        if (pContext instanceof EntityCollisionContext c) {
            Entity e = c.getEntity();
            if (e instanceof LivingEntity) {
                return SHAPE_BY_LAYER[pState.m_61143_(LAYERS) - 1];
            }
        }
        return this.getShape(pState, pLevel, pPos, pContext);
    }

    @Override
    public VoxelShape getBlockSupportShape(BlockState pState, BlockGetter pReader, BlockPos pPos) {
        return SHAPE_BY_LAYER[pState.m_61143_(LAYERS)];
    }

    @Override
    public VoxelShape getVisualShape(BlockState pState, BlockGetter pReader, BlockPos pPos, CollisionContext pContext) {
        return SHAPE_BY_LAYER[pState.m_61143_(LAYERS)];
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter blockGetter, BlockPos pos, PathComputationType pathType) {
        return pathType == PathComputationType.LAND ? (Integer) state.m_61143_(LAYERS) <= 4 : false;
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState state) {
        return true;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState facingState, LevelAccessor world, BlockPos currentPos, BlockPos otherPos) {
        if (world instanceof ServerLevel serverLevel) {
            BlockPos pos = currentPos.above();
            for (BlockState state1 = world.m_8055_(pos); state1.m_60713_(this); state1 = serverLevel.m_8055_(pos)) {
                serverLevel.m_186460_(pos, this, this.m_7198_());
                pos = pos.above();
            }
            updateBasaltBelow(currentPos, serverLevel);
        }
        return super.updateShape(state, direction, facingState, world, currentPos, otherPos);
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource pRand) {
        BlockState below = level.m_8055_(pos.below());
        if ((FallingAshEntity.isFree(below) || this.hasIncompleteAshPileBelow(below)) && pos.m_123342_() >= level.m_141937_()) {
            while (state.m_60713_(this)) {
                FallingBlockEntity fallingblockentity = FallingAshEntity.fall(level, pos, state);
                this.m_6788_(fallingblockentity);
                pos = pos.above();
                state = level.m_8055_(pos);
            }
        }
    }

    private boolean hasIncompleteAshPileBelow(BlockState state) {
        return state.m_60713_(this) && (Integer) state.m_61143_(LAYERS) != 8;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState blockstate = context.m_43725_().getBlockState(context.getClickedPos());
        if (blockstate.m_60713_(this)) {
            int i = (Integer) blockstate.m_61143_(LAYERS);
            return (BlockState) blockstate.m_61124_(LAYERS, Math.min(8, i + 1));
        } else {
            return super.m_5573_(context);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.m_7926_(builder);
        builder.add(LAYERS);
    }

    @Override
    public void randomTick(BlockState pState, ServerLevel level, BlockPos pPos, RandomSource pRandom) {
        if ((Boolean) CommonConfigs.Building.ASH_RAIN.get() && level.m_46758_(pPos.above()) && level.f_46441_.nextInt(4) == 0) {
            this.removeOneLayer(pState, pPos, level);
        }
    }

    @Override
    public void handlePrecipitation(BlockState pState, Level level, BlockPos pPos, Biome.Precipitation pPrecipitation) {
        super.m_141997_(pState, level, pPos, pPrecipitation);
        if ((Boolean) CommonConfigs.Building.ASH_RAIN.get() && level.random.nextInt(2) == 0) {
            this.removeOneLayer(pState, pPos, level);
        }
    }

    private void removeOneLayer(BlockState state, BlockPos pos, Level level) {
        int levels = (Integer) state.m_61143_(LAYERS);
        if (levels > 1) {
            level.setBlockAndUpdate(pos, (BlockState) state.m_61124_(LAYERS, levels - 1));
        } else {
            level.removeBlock(pos, false);
        }
    }

    @Override
    public boolean canBeReplaced(BlockState pState, BlockPlaceContext useContext) {
        int i = (Integer) pState.m_61143_(LAYERS);
        if (useContext.m_43722_().is(this.m_5456_()) && i < 8) {
            boolean ret = true;
            if ((Boolean) RECURSION_HACK.get()) {
                return true;
            } else {
                RECURSION_HACK.set(true);
                if (useContext.replacingClickedOnBlock()) {
                    ret = useContext.m_43719_() == Direction.UP;
                }
                RECURSION_HACK.set(false);
                return ret;
            }
        } else {
            return i <= 3;
        }
    }

    @Override
    public boolean canBeReplaced(BlockState state, Fluid fluid) {
        return true;
    }

    public static void tryConvertToAsh(IFireConsumeBlockEvent event) {
        double chance = (Double) CommonConfigs.Building.ASH_BURN_CHANCE.get();
        if (chance != 0.0) {
            BlockState state = event.getState();
            LevelAccessor level = event.getLevel();
            BlockPos pos = event.getPos();
            if (event.getFinalState() != null) {
                return;
            }
            Item i = state.m_60734_().asItem();
            int count = PlatHelper.getBurnTime(i.getDefaultInstance()) / 100;
            if (i.builtInRegistryHolder().is(ItemTags.LOGS_THAT_BURN)) {
                count += 2;
            }
            if (count > 0) {
                int layers = Mth.clamp(level.getRandom().nextInt(count), 1, 8);
                if (layers != 0) {
                    ((ServerLevel) level).sendParticles((SimpleParticleType) ModParticles.ASH_PARTICLE.get(), (double) pos.m_123341_() + 0.5, (double) pos.m_123342_() + 0.5, (double) pos.m_123343_() + 0.5, 10 + layers, 0.5, 0.5, 0.5, 0.0);
                    event.setFinalState((BlockState) ((Block) ModRegistry.ASH_BLOCK.get()).defaultBlockState().m_61124_(LAYERS, layers));
                }
            }
        }
    }

    private void addParticle(Entity entity, BlockPos pos, Level level, int layers, float upSpeed) {
        level.addParticle((ParticleOptions) ModParticles.ASH_PARTICLE.get(), entity.getX(), (double) ((float) pos.m_123342_() + (float) layers * 0.125F), entity.getZ(), (double) (Mth.randomBetween(level.random, -1.0F, 1.0F) * 0.083333336F), (double) upSpeed, (double) (Mth.randomBetween(level.random, -1.0F, 1.0F) * 0.083333336F));
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (level.isClientSide && (!(entity instanceof LivingEntity) || entity.getFeetBlockState().m_60713_(this))) {
            boolean bl = entity.xOld != entity.getX() || entity.zOld != entity.getZ();
            if (bl && level.random.nextInt(2) == 0) {
                this.addParticle(entity, pos, level, (Integer) state.m_61143_(LAYERS), 0.05F);
            }
        }
        super.m_7892_(state, level, pos, entity);
    }

    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float height) {
        int layers = (Integer) state.m_61143_(LAYERS);
        entity.causeFallDamage(height, layers > 2 ? 0.3F : 1.0F, level.damageSources().fall());
        if (level.isClientSide) {
            for (int i = 0; (double) i < Math.min(12.0, (double) height * 1.4); i++) {
                this.addParticle(entity, pos, level, layers, 0.12F);
            }
        }
    }

    public static boolean applyBonemeal(ItemStack stack, Level level, BlockPos pos, Player player) {
        BlockState blockstate = level.getBlockState(pos);
        if (blockstate.m_60734_() instanceof BonemealableBlock bonemealableblock && bonemealableblock.isValidBonemealTarget(level, pos, blockstate, level.isClientSide)) {
            if (level instanceof ServerLevel serverLevel) {
                if (bonemealableblock.isBonemealSuccess(level, level.random, pos, blockstate)) {
                    bonemealableblock.performBonemeal(serverLevel, level.random, pos, blockstate);
                }
                stack.shrink(1);
            }
            return true;
        }
        return false;
    }

    public static boolean updateBasaltBelow(BlockPos selfPos, Level level) {
        if (level.getBlockState(selfPos.below()) == Blocks.BASALT.defaultBlockState()) {
            level.setBlock(selfPos.below(), ((Block) ModRegistry.ASHEN_BASALT.get()).defaultBlockState(), 2);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.m_6402_(level, pos, state, placer, stack);
        updateBasaltBelow(pos, level);
    }

    @Override
    public void onLand(Level level, BlockPos pos, BlockState state, BlockState replaceableState, FallingBlockEntity fallingBlock) {
        super.m_48792_(level, pos, state, replaceableState, fallingBlock);
        updateBasaltBelow(pos, level);
    }

    static {
        Arrays.setAll(SHAPE_BY_LAYER, l -> Block.box(0.0, 0.0, 0.0, 16.0, (double) l * 2.0, 16.0));
    }
}