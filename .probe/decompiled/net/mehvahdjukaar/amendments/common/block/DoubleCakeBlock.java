package net.mehvahdjukaar.amendments.common.block;

import java.util.Arrays;
import net.mehvahdjukaar.amendments.common.CakeRegistry;
import net.mehvahdjukaar.amendments.configs.CommonConfigs;
import net.mehvahdjukaar.amendments.integration.CompatHandler;
import net.mehvahdjukaar.amendments.integration.SuppCompat;
import net.mehvahdjukaar.amendments.reg.ModRegistry;
import net.mehvahdjukaar.moonlight.api.platform.ForgeHelper;
import net.mehvahdjukaar.moonlight.api.util.math.MthUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class DoubleCakeBlock extends DirectionalCakeBlock {

    protected static final VoxelShape[] SHAPES_NORTH = new VoxelShape[] { Shapes.or(m_49796_(2.0, 8.0, 2.0, 14.0, 16.0, 14.0), m_49796_(1.0, 0.0, 1.0, 15.0, 8.0, 15.0)), Shapes.or(m_49796_(2.0, 8.0, 3.0, 14.0, 16.0, 14.0), m_49796_(1.0, 0.0, 1.0, 15.0, 8.0, 15.0)), Shapes.or(m_49796_(2.0, 8.0, 5.0, 14.0, 16.0, 14.0), m_49796_(1.0, 0.0, 1.0, 15.0, 8.0, 15.0)), Shapes.or(m_49796_(2.0, 8.0, 7.0, 14.0, 16.0, 14.0), m_49796_(1.0, 0.0, 1.0, 15.0, 8.0, 15.0)), Shapes.or(m_49796_(2.0, 8.0, 9.0, 14.0, 16.0, 14.0), m_49796_(1.0, 0.0, 1.0, 15.0, 8.0, 15.0)), Shapes.or(m_49796_(2.0, 8.0, 11.0, 14.0, 16.0, 14.0), m_49796_(1.0, 0.0, 1.0, 15.0, 8.0, 15.0)), Shapes.or(m_49796_(2.0, 8.0, 13.0, 14.0, 16.0, 14.0), m_49796_(1.0, 0.0, 1.0, 15.0, 8.0, 15.0)) };

    protected static final VoxelShape[] SHAPES_WEST = (VoxelShape[]) Arrays.stream(SHAPES_NORTH).map(s -> MthUtils.rotateVoxelShape(s, Direction.WEST)).toArray(VoxelShape[]::new);

    protected static final VoxelShape[] SHAPES_SOUTH = (VoxelShape[]) Arrays.stream(SHAPES_NORTH).map(s -> MthUtils.rotateVoxelShape(s, Direction.SOUTH)).toArray(VoxelShape[]::new);

    protected static final VoxelShape[] SHAPES_EAST = (VoxelShape[]) Arrays.stream(SHAPES_NORTH).map(s -> MthUtils.rotateVoxelShape(s, Direction.EAST)).toArray(VoxelShape[]::new);

    private final BlockState mimic;

    public DoubleCakeBlock(CakeRegistry.CakeType type) {
        super(type);
        this.mimic = type.cake.defaultBlockState();
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return switch((Direction) state.m_61143_(FACING)) {
            case EAST ->
                SHAPES_EAST[state.m_61143_(f_51180_)];
            case SOUTH ->
                SHAPES_SOUTH[state.m_61143_(f_51180_)];
            case NORTH ->
                SHAPES_NORTH[state.m_61143_(f_51180_)];
            default ->
                SHAPES_WEST[state.m_61143_(f_51180_)];
        };
    }

    @Override
    public void removeSlice(BlockState state, BlockPos pos, LevelAccessor level, Direction dir) {
        int i = (Integer) state.m_61143_(f_51180_);
        if (i < 6) {
            if (i == 0 && (Boolean) CommonConfigs.DIRECTIONAL_CAKE.get()) {
                state = (BlockState) state.m_61124_(FACING, dir);
            }
            level.m_7731_(pos, (BlockState) state.m_61124_(f_51180_, i + 1), 3);
        } else if (this.type == CakeRegistry.VANILLA && (Boolean) state.m_61143_(WATERLOGGED) && (Boolean) CommonConfigs.DIRECTIONAL_CAKE.get()) {
            level.m_7731_(pos, (BlockState) ((BlockState) ((Block) ModRegistry.DIRECTIONAL_CAKE.get()).defaultBlockState().m_61124_(FACING, (Direction) state.m_61143_(FACING))).m_61124_(WATERLOGGED, (Boolean) state.m_61143_(WATERLOGGED)), 3);
        } else {
            level.m_7731_(pos, this.type.cake.defaultBlockState(), 3);
        }
    }

    @Override
    public void animateTick(BlockState stateIn, Level level, BlockPos pos, RandomSource rand) {
        if (CompatHandler.SUPPLEMENTARIES) {
            SuppCompat.spawnCakeParticles(level, pos, rand);
        }
        super.animateTick(stateIn, level, pos, rand);
        this.mimic.m_60734_().animateTick(this.mimic, level, pos, rand);
    }

    @Override
    public float getDestroyProgress(BlockState state, Player player, BlockGetter worldIn, BlockPos pos) {
        return Math.min(super.m_5880_(state, player, worldIn, pos), this.mimic.m_60625_(player, worldIn, pos));
    }

    public SoundType getSoundType(BlockState state, LevelReader world, BlockPos pos, Entity entity) {
        return this.mimic.m_60827_();
    }

    @Override
    public SoundType getSoundType(BlockState state) {
        return this.mimic.m_60827_();
    }

    public float getExplosionResistance(BlockState state, BlockGetter level, BlockPos pos, Explosion explosion) {
        return level instanceof Level l ? Math.max(ForgeHelper.getExplosionResistance(this.mimic, l, pos, explosion), state.m_60734_().getExplosionResistance()) : super.m_7325_();
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        return this.mimic.m_60734_().getCloneItemStack(level, pos, state);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (!player.m_21120_(handIn).is(ItemTags.CANDLES)) {
            BlockState newState = this.type.cake.withPropertiesOf(state);
            level.setBlock(pos, newState, 4);
            InteractionResult res = newState.m_60664_(level, player, handIn, hit);
            level.setBlockAndUpdate(pos, state);
            if (res.consumesAction()) {
                if (!level.isClientSide()) {
                    this.removeSlice(state, pos, level, getHitDir(player, hit));
                }
                return res;
            }
        }
        return super.use(state, level, pos, player, handIn, hit);
    }
}