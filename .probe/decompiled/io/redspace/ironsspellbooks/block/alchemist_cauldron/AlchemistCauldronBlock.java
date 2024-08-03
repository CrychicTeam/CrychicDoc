package io.redspace.ironsspellbooks.block.alchemist_cauldron;

import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.damage.ISSDamageTypes;
import io.redspace.ironsspellbooks.registries.BlockRegistry;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
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
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class AlchemistCauldronBlock extends BaseEntityBlock {

    private static final VoxelShape SHAPE = Shapes.or(Shapes.or(m_49796_(0.0, 0.0, 4.0, 16.0, 2.0, 6.0), m_49796_(0.0, 0.0, 10.0, 16.0, 2.0, 12.0), m_49796_(4.0, 0.0, 0.0, 6.0, 2.0, 16.0), m_49796_(10.0, 0.0, 0.0, 12.0, 2.0, 16.0)), Shapes.join(Shapes.or(Shapes.join(m_49796_(0.0, 2.0, 0.0, 16.0, 16.0, 16.0), m_49796_(0.0, 12.0, 0.0, 16.0, 14.0, 16.0), BooleanOp.ONLY_FIRST), m_49796_(1.0, 12.0, 1.0, 15.0, 14.0, 15.0)), m_49796_(2.0, 4.0, 2.0, 14.0, 16.0, 14.0), BooleanOp.ONLY_FIRST));

    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    public static final int MAX_LEVELS = 4;

    public static final IntegerProperty LEVEL = IntegerProperty.create("level", 0, 4);

    public AlchemistCauldronBlock() {
        super(BlockBehaviour.Properties.copy(Blocks.CAULDRON).lightLevel(blockState -> 3));
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(LIT, false)).m_61124_(LEVEL, 0));
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return createTicker(pLevel, pBlockEntityType, BlockRegistry.ALCHEMIST_CAULDRON_TILE.get());
    }

    @Nullable
    protected static <T extends BlockEntity> BlockEntityTicker<T> createTicker(Level pLevel, BlockEntityType<T> pServerType, BlockEntityType<? extends AlchemistCauldronTile> pClientType) {
        return pLevel.isClientSide ? null : m_152132_(pServerType, pClientType, AlchemistCauldronTile::serverTick);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT, LEVEL);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult blockHit) {
        return level.getBlockEntity(pos) instanceof AlchemistCauldronTile tile ? tile.handleUse(blockState, level, pos, player, hand) : super.m_6227_(blockState, level, pos, player, hand, blockHit);
    }

    @Override
    public void entityInside(BlockState blockState, Level level, BlockPos pos, Entity entity) {
        if (entity.tickCount % 20 == 0 && isBoiling(blockState) && entity instanceof LivingEntity livingEntity && livingEntity.hurt(DamageSources.get(level, ISSDamageTypes.CAULDRON), 2.0F)) {
            MagicManager.spawnParticles(level, ParticleHelper.BLOOD, entity.getX(), entity.getY() + (double) (entity.getBbHeight() / 2.0F), entity.getZ(), 20, 0.05, 0.05, 0.05, 0.1, false);
            if (level.getBlockEntity(pos) instanceof AlchemistCauldronTile cauldronTile) {
                AlchemistCauldronTile.appendItem(cauldronTile.resultItems, new ItemStack(ItemRegistry.BLOOD_VIAL.get()));
                cauldronTile.setChanged();
            }
        }
        super.m_7892_(blockState, level, pos, entity);
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new AlchemistCauldronTile(pos, state);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborstate, LevelAccessor level, BlockPos pos, BlockPos pNeighborPos) {
        if (direction.equals(Direction.DOWN)) {
            level.m_7731_(pos, (BlockState) state.m_61124_(LIT, this.isFireSource(neighborstate)), 11);
        }
        return super.m_7417_(state, direction, neighborstate, level, pos, pNeighborPos);
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        LevelAccessor levelaccessor = pContext.m_43725_();
        BlockPos blockpos = pContext.getClickedPos().below();
        boolean flag = this.isFireSource(levelaccessor.m_8055_(blockpos));
        return (BlockState) this.m_49966_().m_61124_(LIT, flag);
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (pState.m_60734_() != pNewState.m_60734_() && pLevel.getBlockEntity(pPos) instanceof AlchemistCauldronTile cauldronTile) {
            cauldronTile.drops();
        }
        super.m_6810_(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    @NotNull
    @Override
    public RenderShape getRenderShape(@NotNull BlockState blockState) {
        return RenderShape.MODEL;
    }

    public boolean isFireSource(BlockState blockState) {
        return true;
    }

    public static boolean isLit(BlockState blockState) {
        return blockState.m_61138_(LIT) && (Boolean) blockState.m_61143_(LIT);
    }

    public static int getLevel(BlockState blockState) {
        return blockState.m_61138_(LEVEL) ? (Integer) blockState.m_61143_(LEVEL) : 0;
    }

    public static boolean isBoiling(BlockState blockState) {
        return isLit(blockState) && getLevel(blockState) > 0;
    }
}