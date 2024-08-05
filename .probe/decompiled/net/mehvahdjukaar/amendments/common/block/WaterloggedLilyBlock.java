package net.mehvahdjukaar.amendments.common.block;

import java.util.List;
import net.mehvahdjukaar.amendments.common.tile.WaterloggedLilyBlockTile;
import net.mehvahdjukaar.moonlight.api.block.IBlockHolder;
import net.mehvahdjukaar.moonlight.api.platform.ForgeHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.WaterlilyBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class WaterloggedLilyBlock extends WaterlilyBlock implements LiquidBlockContainer, EntityBlock {

    protected static final VoxelShape AABB = Block.box(1.0, 15.0, 1.0, 15.0, 16.0, 15.0);

    protected static final VoxelShape AABB_EXTENDED = Block.box(1.0, 15.0, 1.0, 15.0, 17.5, 15.0);

    protected static final VoxelShape AABB_FAKE = Block.box(1.0, 16.0, 1.0, 15.0, 17.5, 15.0);

    protected static final VoxelShape AABB_SUPPORT = Block.box(0.0, 15.0, 0.0, 16.0, 16.0, 16.0);

    public static final BooleanProperty EXTENDED = BlockStateProperties.EXTENDED;

    public WaterloggedLilyBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.m_49959_((BlockState) this.m_49966_().m_61124_(EXTENDED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.m_7926_(builder);
        builder.add(EXTENDED);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext collisionContext) {
        return state.m_61143_(EXTENDED) ? AABB_FAKE : AABB;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.m_61143_(EXTENDED) ? AABB_EXTENDED : AABB;
    }

    @Override
    public VoxelShape getBlockSupportShape(BlockState state, BlockGetter reader, BlockPos pos) {
        return AABB_SUPPORT;
    }

    @Override
    public VoxelShape getVisualShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return Fluids.WATER.getSource(false);
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        worldIn.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.m_6718_(worldIn));
        return stateIn;
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block neighborBlock, BlockPos fromPos, boolean moving) {
        super.m_6861_(state, world, pos, neighborBlock, fromPos, moving);
        if (pos.above().equals(fromPos)) {
            this.maybeConvertToVanilla(state, world, pos);
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel serverLevel, BlockPos pos, RandomSource random) {
        if (!this.maybeConvertToVanilla(state, serverLevel, pos)) {
        }
        super.m_213897_(state, serverLevel, pos, random);
    }

    private boolean maybeConvertToVanilla(BlockState state, LevelAccessor serverLevel, BlockPos pos) {
        if (serverLevel.m_8055_(pos.above()).m_60795_() && serverLevel.m_7702_(pos) instanceof WaterloggedLilyBlockTile te) {
            serverLevel.m_7731_(pos, Blocks.WATER.defaultBlockState(), 3);
            serverLevel.m_7731_(pos.above(), te.getHeldBlock(), 3);
            for (Entity e : serverLevel.m_45976_(Entity.class, AABB_SUPPORT.bounds().move(pos).move(0.0, 0.0625, 0.0))) {
                if (e.getPistonPushReaction() != PushReaction.IGNORE) {
                    e.move(MoverType.SHULKER_BOX, new Vec3(0.0, 0.09375, 0.0));
                }
            }
            return true;
        } else {
            if ((Boolean) state.m_61143_(EXTENDED)) {
                serverLevel.m_7731_(pos, (BlockState) state.m_61124_(EXTENDED, false), 3);
            }
            return false;
        }
    }

    @Override
    public boolean canPlaceLiquid(BlockGetter level, BlockPos pos, BlockState state, Fluid fluid) {
        return false;
    }

    @Override
    public boolean placeLiquid(LevelAccessor level, BlockPos pos, BlockState state, FluidState fluidState) {
        return false;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        return InteractionResult.PASS;
    }

    @Override
    public long getSeed(BlockState pState, BlockPos pPos) {
        return Mth.getSeed(pPos.above());
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new WaterloggedLilyBlockTile(pos, state);
    }

    @Override
    public float getDestroyProgress(BlockState state, Player player, BlockGetter worldIn, BlockPos pos) {
        if (worldIn.getBlockEntity(pos) instanceof IBlockHolder tile) {
            BlockState mimicState = tile.getHeldBlock();
            if (!mimicState.m_60795_() && !(mimicState.m_60734_() instanceof WaterloggedLilyBlock)) {
                return Math.min(super.m_5880_(state, player, worldIn, pos), mimicState.m_60625_(player, worldIn, pos));
            }
        }
        return super.m_5880_(state, player, worldIn, pos);
    }

    public SoundType getSoundType(BlockState state, LevelReader world, BlockPos pos, Entity entity) {
        if (world.m_7702_(pos) instanceof IBlockHolder tile) {
            BlockState mimicState = tile.getHeldBlock();
            if (!mimicState.m_60795_()) {
                return mimicState.m_60827_();
            }
        }
        return super.m_49962_(state);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        List<ItemStack> drops = super.m_49635_(state, builder);
        if (builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY) instanceof IBlockHolder tile) {
            BlockState heldState = tile.getHeldBlock();
            if (builder.getOptionalParameter(LootContextParams.THIS_ENTITY) instanceof ServerPlayer player && !ForgeHelper.canHarvestBlock(heldState, builder.getLevel(), BlockPos.containing(builder.getParameter(LootContextParams.ORIGIN)), player)) {
                return drops;
            }
            List<ItemStack> newDrops = heldState.m_287290_(builder);
            drops.addAll(newDrops);
        }
        return drops;
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter worldIn, BlockPos pos, PathComputationType type) {
        return false;
    }

    public float getExplosionResistance(BlockState state, BlockGetter world, BlockPos pos, Explosion explosion) {
        if (world.getBlockEntity(pos) instanceof IBlockHolder tile) {
            BlockState mimicState = tile.getHeldBlock();
            if (!mimicState.m_60795_() && !(mimicState.m_60734_() instanceof WaterloggedLilyBlock)) {
                return Math.max(ForgeHelper.getExplosionResistance(mimicState, (Level) world, pos, explosion), state.m_60734_().getExplosionResistance());
            }
        }
        return 2.0F;
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        if (level.getBlockEntity(pos) instanceof IBlockHolder tile) {
            BlockState mimic = tile.getHeldBlock();
            return mimic.m_60734_().getCloneItemStack(level, pos, state);
        } else {
            return super.m_7397_(level, pos, state);
        }
    }

    @Override
    public String getDescriptionId() {
        return Blocks.LILY_PAD.getDescriptionId();
    }
}