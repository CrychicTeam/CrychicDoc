package com.almostreliable.summoningrituals.altar;

import com.almostreliable.summoningrituals.util.MathUtils;
import extensions.net.minecraft.world.entity.LivingEntity.LivingEntityExt;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.joml.Vector3f;

public class AltarBlock extends Block implements SimpleWaterloggedBlock, EntityBlock {

    static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    private static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private static final VoxelShape SHAPE = (VoxelShape) Stream.of(Block.box(3.0, 0.0, 3.0, 13.0, 2.0, 13.0), Block.box(5.0, 2.0, 5.0, 11.0, 9.0, 11.0), Block.box(2.0, 9.0, 2.0, 14.0, 13.0, 14.0)).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    public AltarBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.m_49966_().m_61124_(FACING, Direction.NORTH)).m_61124_(ACTIVE, false)).m_61124_(WATERLOGGED, false));
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (hand == InteractionHand.MAIN_HAND && level.getBlockEntity(pos) instanceof AltarBlockEntity altar) {
            if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
                LivingEntityExt.setMainHandItem(serverPlayer, altar.handleInteraction(serverPlayer, serverPlayer.m_21205_()));
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        } else {
            return super.m_6227_(state, level, pos, player, hand, hit);
        }
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState nState, LevelAccessor level, BlockPos pos, BlockPos nPos) {
        if ((Boolean) state.m_61143_(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.m_6718_(level));
        }
        return super.m_7417_(state, direction, nState, level, pos, nPos);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new AltarBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null : (entityLevel, entityState, entityType, entity) -> {
            if (entity instanceof AltarBlockEntity altar) {
                altar.tick();
            }
        };
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        Vector3f[][] particlePos = MathUtils.getHorizontalVectors(new Vector3f(3.5F, 1.5F, 9.5F), new Vector3f(9.5F, 3.5F, 12.5F), new Vector3f(11.5F, 4.5F, 10.5F));
        int x = pos.m_123341_();
        int y = pos.m_123342_() + 1;
        int z = pos.m_123343_();
        Vector3f[] vec = particlePos[((Direction) state.m_61143_(FACING)).ordinal() - 2];
        Boolean active = (Boolean) state.m_61143_(ACTIVE);
        for (int i = 0; i < 3; i++) {
            if (active) {
                this.renderCandleActive(level, x, y, z, vec[i]);
            } else {
                this.renderCandleInactive(level, x, y, z, vec[i]);
            }
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState superState = super.getStateForPlacement(context);
        BlockState state = superState == null ? this.m_49966_() : superState;
        return (BlockState) ((BlockState) ((BlockState) state.m_61124_(FACING, context.m_8125_().getOpposite())).m_61124_(ACTIVE, false)).m_61124_(WATERLOGGED, context.m_43725_().getFluidState(context.getClickedPos()).is(Fluids.WATER));
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide && player instanceof ServerPlayer && level.getBlockEntity(pos) instanceof AltarBlockEntity altar) {
            altar.playerDestroy(player.isCreative());
        }
        super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING).add(ACTIVE).add(WATERLOGGED);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.m_61143_(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.m_5888_(state);
    }

    private void renderCandleActive(Level level, int x, int y, int z, Vector3f vec) {
        level.addParticle(ParticleTypes.SOUL, (double) ((float) x + vec.x() / 16.0F), (double) ((float) y + (vec.y() + 2.0F) / 16.0F), (double) ((float) z + vec.z() / 16.0F), 0.0, 0.0, 0.0);
    }

    private void renderCandleInactive(Level level, int x, int y, int z, Vector3f vec) {
        level.addParticle(ParticleTypes.SMALL_FLAME, (double) ((float) x + vec.x() / 16.0F), (double) ((float) y + vec.y() / 16.0F), (double) ((float) z + vec.z() / 16.0F), 0.0, 0.0, 0.0);
    }
}