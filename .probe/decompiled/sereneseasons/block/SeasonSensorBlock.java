package sereneseasons.block;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import sereneseasons.api.SSBlockEntities;
import sereneseasons.api.season.SeasonHelper;
import sereneseasons.config.ServerConfig;
import sereneseasons.season.SeasonTime;
import sereneseasons.tileentity.SeasonSensorBlockEntity;

public class SeasonSensorBlock extends BaseEntityBlock {

    public static final IntegerProperty POWER = BlockStateProperties.POWER;

    protected static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 6.0, 16.0);

    public static final IntegerProperty SEASON = IntegerProperty.create("season", 0, 3);

    public SeasonSensorBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(POWER, 0)).m_61124_(SEASON, 0));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext selectionContext) {
        return SHAPE;
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState state) {
        return true;
    }

    @Override
    public int getSignal(BlockState state, BlockGetter reader, BlockPos pos, Direction direction) {
        return (Integer) state.m_61143_(POWER);
    }

    public void updatePower(Level world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        if (ServerConfig.isDimensionWhitelisted(world.dimension())) {
            BlockState currentState = world.getBlockState(pos);
            int power = 0;
            int startTicks = (Integer) currentState.m_61143_(SEASON) * SeasonTime.ZERO.getSeasonDuration();
            int endTicks = ((Integer) currentState.m_61143_(SEASON) + 1) * SeasonTime.ZERO.getSeasonDuration();
            int currentTicks = SeasonHelper.getSeasonState(world).getSeasonCycleTicks();
            if (currentTicks >= startTicks && currentTicks <= endTicks) {
                float delta = (float) (currentTicks - startTicks) / (float) SeasonTime.ZERO.getSeasonDuration();
                power = (int) Math.min(delta * 15.0F + 1.0F, 15.0F);
            }
            if ((Integer) currentState.m_61143_(POWER) != power) {
                world.setBlock(pos, (BlockState) currentState.m_61124_(POWER, power), 3);
            }
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult rayTraceResult) {
        if (player.mayBuild()) {
            if (world.isClientSide) {
                return InteractionResult.SUCCESS;
            } else {
                BlockState blockstate = (BlockState) state.m_61122_(SEASON);
                world.setBlock(pos, blockstate, 4);
                this.updatePower(world, pos);
                return InteractionResult.SUCCESS;
            }
        } else {
            return super.m_6227_(state, world, pos, player, hand, rayTraceResult);
        }
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public boolean isSignalSource(BlockState state) {
        return true;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SeasonSensorBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return !level.isClientSide && level.dimensionType().hasSkyLight() ? m_152132_(type, SSBlockEntities.SEASON_SENSOR.get(), SeasonSensorBlock::tickEntity) : null;
    }

    private static void tickEntity(Level level, BlockPos pos, BlockState state, SeasonSensorBlockEntity entity) {
        if (level != null && !level.isClientSide && (long) SeasonHelper.getSeasonState(level).getSeasonCycleTicks() % 20L == 0L) {
            Block block = state.m_60734_();
            if (block instanceof SeasonSensorBlock) {
                ((SeasonSensorBlock) block).updatePower(level, pos);
            }
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POWER, SEASON);
    }
}