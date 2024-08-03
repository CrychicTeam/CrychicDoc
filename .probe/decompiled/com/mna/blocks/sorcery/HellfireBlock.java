package com.mna.blocks.sorcery;

import com.google.common.collect.ImmutableMap;
import com.mna.api.blocks.interfaces.ICutoutBlock;
import com.mna.api.blocks.interfaces.IDontCreateBlockItem;
import com.mna.blocks.BlockInit;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.extensions.IForgeBlock;

public class HellfireBlock extends BaseFireBlock implements ICutoutBlock, IDontCreateBlockItem, IForgeBlock {

    public static final IntegerProperty AGE = BlockStateProperties.AGE_15;

    public static final BooleanProperty NORTH = PipeBlock.NORTH;

    public static final BooleanProperty EAST = PipeBlock.EAST;

    public static final BooleanProperty SOUTH = PipeBlock.SOUTH;

    public static final BooleanProperty WEST = PipeBlock.WEST;

    public static final BooleanProperty UP = PipeBlock.UP;

    private static final Map<Direction, BooleanProperty> FACING_TO_PROPERTY_MAP = (Map<Direction, BooleanProperty>) PipeBlock.PROPERTY_BY_DIRECTION.entrySet().stream().filter(facingProperty -> facingProperty.getKey() != Direction.DOWN).collect(Util.toMap());

    private static final VoxelShape UP_AABB = Block.box(0.0, 15.0, 0.0, 16.0, 16.0, 16.0);

    private static final VoxelShape WEST_AABB = Block.box(0.0, 0.0, 0.0, 1.0, 16.0, 16.0);

    private static final VoxelShape EAST_AABB = Block.box(15.0, 0.0, 0.0, 16.0, 16.0, 16.0);

    private static final VoxelShape NORTH_AABB = Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 1.0);

    private static final VoxelShape SOUTH_AABB = Block.box(0.0, 0.0, 15.0, 16.0, 16.0, 16.0);

    private final Map<BlockState, VoxelShape> shapesCache;

    private final Object2IntMap<Block> encouragements = new Object2IntOpenHashMap();

    private final Object2IntMap<Block> flammabilities = new Object2IntOpenHashMap();

    public HellfireBlock() {
        super(BlockBehaviour.Properties.of().mapColor(DyeColor.LIME).noOcclusion().noCollission().instabreak().lightLevel(state -> 15).sound(SoundType.WOOL), 3.0F);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(AGE, 0)).m_61124_(NORTH, false)).m_61124_(EAST, false)).m_61124_(SOUTH, false)).m_61124_(WEST, false)).m_61124_(UP, false));
        this.shapesCache = ImmutableMap.copyOf((Map) this.f_49792_.getPossibleStates().stream().filter(p_242674_0_ -> (Integer) p_242674_0_.m_61143_(AGE) == 0).collect(Collectors.toMap(Function.identity(), HellfireBlock::calculateShape)));
    }

    private static VoxelShape calculateShape(BlockState p_242673_0_) {
        VoxelShape voxelshape = Shapes.empty();
        if ((Boolean) p_242673_0_.m_61143_(UP)) {
            voxelshape = UP_AABB;
        }
        if ((Boolean) p_242673_0_.m_61143_(NORTH)) {
            voxelshape = Shapes.or(voxelshape, NORTH_AABB);
        }
        if ((Boolean) p_242673_0_.m_61143_(SOUTH)) {
            voxelshape = Shapes.or(voxelshape, SOUTH_AABB);
        }
        if ((Boolean) p_242673_0_.m_61143_(EAST)) {
            voxelshape = Shapes.or(voxelshape, EAST_AABB);
        }
        if ((Boolean) p_242673_0_.m_61143_(WEST)) {
            voxelshape = Shapes.or(voxelshape, WEST_AABB);
        }
        return voxelshape.isEmpty() ? f_49237_ : voxelshape;
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        return this.canSurvive(stateIn, worldIn, currentPos) ? this.getFireWithAge(worldIn, currentPos, (Integer) stateIn.m_61143_(AGE)) : Blocks.AIR.defaultBlockState();
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return (VoxelShape) this.shapesCache.get(state.m_61124_(AGE, 0));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.getStateForPlacement(context.m_43725_(), context.getClickedPos());
    }

    protected BlockState getStateForPlacement(BlockGetter blockReader, BlockPos pos) {
        BlockPos blockpos = pos.below();
        BlockState blockstate = blockReader.getBlockState(blockpos);
        if (!this.canCatchFire(blockReader, pos, Direction.UP) && !blockstate.m_60783_(blockReader, blockpos, Direction.UP)) {
            BlockState blockstate1 = this.m_49966_();
            for (Direction direction : Direction.values()) {
                BooleanProperty booleanproperty = (BooleanProperty) FACING_TO_PROPERTY_MAP.get(direction);
                if (booleanproperty != null) {
                    blockstate1 = (BlockState) blockstate1.m_61124_(booleanproperty, this.canCatchFire(blockReader, pos.relative(direction), direction.getOpposite()));
                }
            }
            return blockstate1;
        } else {
            return this.m_49966_();
        }
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
        BlockPos blockpos = pos.below();
        return worldIn.m_8055_(blockpos).m_60783_(worldIn, blockpos, Direction.UP) || this.areNeighborsFlammable(worldIn, pos);
    }

    @Override
    public void tick(BlockState state, ServerLevel worldIn, BlockPos pos, RandomSource rand) {
        worldIn.m_186460_(pos, this, getTickCooldown(worldIn.f_46441_));
        if (worldIn.m_46469_().getBoolean(GameRules.RULE_DOFIRETICK)) {
            if (!state.m_60710_(worldIn, pos)) {
                worldIn.m_7471_(pos, false);
            }
            BlockState blockstate = worldIn.m_8055_(pos.below());
            boolean flag = blockstate.isFireSource(worldIn, pos, Direction.UP);
            int i = (Integer) state.m_61143_(AGE);
            if (!flag && worldIn.m_46471_() && this.canDie(worldIn, pos) && rand.nextFloat() < 0.2F + (float) i * 0.03F) {
                worldIn.m_7471_(pos, false);
            } else {
                int j = Math.min(15, i + rand.nextInt(3) / 2);
                if (i != j) {
                    state = (BlockState) state.m_61124_(AGE, j);
                    worldIn.m_7731_(pos, state, 4);
                }
                if (!flag) {
                    if (!this.areNeighborsFlammable(worldIn, pos)) {
                        BlockPos blockpos = pos.below();
                        if (!worldIn.m_8055_(blockpos).m_60783_(worldIn, blockpos, Direction.UP) || i > 3) {
                            worldIn.m_7471_(pos, false);
                        }
                        return;
                    }
                    if (i == 15 && rand.nextInt(4) == 0 && !this.canCatchFire(worldIn, pos.below(), Direction.UP)) {
                        worldIn.m_7471_(pos, false);
                        return;
                    }
                }
                boolean flag1 = worldIn.m_204166_(pos).is(BiomeTags.INCREASED_FIRE_BURNOUT);
                int k = flag1 ? -50 : 0;
                this.tryCatchFire(worldIn, pos.east(), 300 + k, rand, i, Direction.WEST);
                this.tryCatchFire(worldIn, pos.west(), 300 + k, rand, i, Direction.EAST);
                this.tryCatchFire(worldIn, pos.below(), 250 + k, rand, i, Direction.UP);
                this.tryCatchFire(worldIn, pos.above(), 250 + k, rand, i, Direction.DOWN);
                this.tryCatchFire(worldIn, pos.north(), 300 + k, rand, i, Direction.SOUTH);
                this.tryCatchFire(worldIn, pos.south(), 300 + k, rand, i, Direction.NORTH);
                BlockPos.MutableBlockPos blockpos$mutable = new BlockPos.MutableBlockPos();
                for (int l = -1; l <= 1; l++) {
                    for (int i1 = -1; i1 <= 1; i1++) {
                        for (int j1 = -1; j1 <= 4; j1++) {
                            if (l != 0 || j1 != 0 || i1 != 0) {
                                int k1 = 100;
                                if (j1 > 1) {
                                    k1 += (j1 - 1) * 100;
                                }
                                blockpos$mutable.setWithOffset(pos, l, j1, i1);
                                int l1 = this.getNeighborEncouragement(worldIn, blockpos$mutable);
                                if (l1 > 0) {
                                    int i2 = (l1 + 40 + worldIn.m_46791_().getId() * 7) / (i + 30);
                                    if (flag1) {
                                        i2 /= 2;
                                    }
                                    if (i2 > 0 && rand.nextInt(k1) <= i2 && (!worldIn.m_46471_() || !this.canDie(worldIn, blockpos$mutable))) {
                                        int j2 = Math.min(15, i + rand.nextInt(5) / 4);
                                        worldIn.m_7731_(blockpos$mutable, this.getFireWithAge(worldIn, blockpos$mutable, j2), 3);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    protected boolean canDie(Level worldIn, BlockPos pos) {
        return false;
    }

    @Deprecated
    public int getFlammability(BlockState state) {
        return state.m_61138_(BlockStateProperties.WATERLOGGED) && state.m_61143_(BlockStateProperties.WATERLOGGED) ? 0 : this.flammabilities.getInt(state.m_60734_());
    }

    @Deprecated
    public int getFireSpreadSpeed(BlockState state) {
        return state.m_61138_(BlockStateProperties.WATERLOGGED) && state.m_61143_(BlockStateProperties.WATERLOGGED) ? 0 : this.encouragements.getInt(state.m_60734_()) * 2;
    }

    private void tryCatchFire(Level worldIn, BlockPos pos, int chance, RandomSource random, int age, Direction face) {
        int i = worldIn.getBlockState(pos).getFlammability(worldIn, pos, face);
        if (random.nextInt(chance) < i) {
            BlockState blockstate = worldIn.getBlockState(pos);
            if (random.nextInt(age + 10) < 5) {
                int j = Math.min(age + random.nextInt(5) / 4, 15);
                worldIn.setBlock(pos, this.getFireWithAge(worldIn, pos, j), 3);
            } else {
                worldIn.removeBlock(pos, false);
            }
            blockstate.onCaughtFire(worldIn, pos, face, null);
        }
    }

    private BlockState getFireWithAge(LevelAccessor world, BlockPos pos, int age) {
        return (BlockState) this.getStateForPlacement(world, pos).m_61124_(AGE, age);
    }

    private boolean areNeighborsFlammable(BlockGetter worldIn, BlockPos pos) {
        for (Direction direction : Direction.values()) {
            if (this.canCatchFire(worldIn, pos.relative(direction), direction.getOpposite())) {
                return true;
            }
        }
        return false;
    }

    private int getNeighborEncouragement(LevelReader worldIn, BlockPos pos) {
        if (!worldIn.isEmptyBlock(pos)) {
            return 0;
        } else {
            int i = 0;
            for (Direction direction : Direction.values()) {
                BlockState blockstate = worldIn.m_8055_(pos.relative(direction));
                i = Math.max(blockstate.getFireSpreadSpeed(worldIn, pos.relative(direction), direction.getOpposite()), i);
            }
            return i;
        }
    }

    @Deprecated
    @Override
    protected boolean canBurn(BlockState state) {
        return this.getFireSpreadSpeed(state) > 0;
    }

    @Override
    public void onPlace(BlockState state, Level worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, worldIn, pos, oldState, isMoving);
        worldIn.m_186460_(pos, this, getTickCooldown(worldIn.random));
    }

    private static int getTickCooldown(RandomSource rand) {
        return 5 + rand.nextInt(10);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE, NORTH, EAST, SOUTH, WEST, UP);
    }

    private void setFireInfo(Block blockIn, int encouragement, int flammability) {
        if (blockIn == Blocks.AIR) {
            throw new IllegalArgumentException("Tried to set air on fire... This is bad.");
        } else {
            this.encouragements.put(blockIn, encouragement);
            this.flammabilities.put(blockIn, flammability);
        }
    }

    public boolean canCatchFire(BlockGetter world, BlockPos pos, Direction face) {
        return world.getBlockState(pos).isFlammable(world, pos, face);
    }

    public static void init() {
        HellfireBlock fireblock = (HellfireBlock) BlockInit.HELLFIRE.get();
        fireblock.setFireInfo(Blocks.OAK_PLANKS, 5, 20);
        fireblock.setFireInfo(Blocks.SPRUCE_PLANKS, 5, 20);
        fireblock.setFireInfo(Blocks.BIRCH_PLANKS, 5, 20);
        fireblock.setFireInfo(Blocks.JUNGLE_PLANKS, 5, 20);
        fireblock.setFireInfo(Blocks.ACACIA_PLANKS, 5, 20);
        fireblock.setFireInfo(Blocks.DARK_OAK_PLANKS, 5, 20);
        fireblock.setFireInfo(Blocks.OAK_SLAB, 5, 20);
        fireblock.setFireInfo(Blocks.SPRUCE_SLAB, 5, 20);
        fireblock.setFireInfo(Blocks.BIRCH_SLAB, 5, 20);
        fireblock.setFireInfo(Blocks.JUNGLE_SLAB, 5, 20);
        fireblock.setFireInfo(Blocks.ACACIA_SLAB, 5, 20);
        fireblock.setFireInfo(Blocks.DARK_OAK_SLAB, 5, 20);
        fireblock.setFireInfo(Blocks.OAK_FENCE_GATE, 5, 20);
        fireblock.setFireInfo(Blocks.SPRUCE_FENCE_GATE, 5, 20);
        fireblock.setFireInfo(Blocks.BIRCH_FENCE_GATE, 5, 20);
        fireblock.setFireInfo(Blocks.JUNGLE_FENCE_GATE, 5, 20);
        fireblock.setFireInfo(Blocks.DARK_OAK_FENCE_GATE, 5, 20);
        fireblock.setFireInfo(Blocks.ACACIA_FENCE_GATE, 5, 20);
        fireblock.setFireInfo(Blocks.OAK_FENCE, 5, 20);
        fireblock.setFireInfo(Blocks.SPRUCE_FENCE, 5, 20);
        fireblock.setFireInfo(Blocks.BIRCH_FENCE, 5, 20);
        fireblock.setFireInfo(Blocks.JUNGLE_FENCE, 5, 20);
        fireblock.setFireInfo(Blocks.DARK_OAK_FENCE, 5, 20);
        fireblock.setFireInfo(Blocks.ACACIA_FENCE, 5, 20);
        fireblock.setFireInfo(Blocks.OAK_STAIRS, 5, 20);
        fireblock.setFireInfo(Blocks.BIRCH_STAIRS, 5, 20);
        fireblock.setFireInfo(Blocks.SPRUCE_STAIRS, 5, 20);
        fireblock.setFireInfo(Blocks.JUNGLE_STAIRS, 5, 20);
        fireblock.setFireInfo(Blocks.ACACIA_STAIRS, 5, 20);
        fireblock.setFireInfo(Blocks.DARK_OAK_STAIRS, 5, 20);
        fireblock.setFireInfo(Blocks.OAK_LOG, 5, 5);
        fireblock.setFireInfo(Blocks.SPRUCE_LOG, 5, 5);
        fireblock.setFireInfo(Blocks.BIRCH_LOG, 5, 5);
        fireblock.setFireInfo(Blocks.JUNGLE_LOG, 5, 5);
        fireblock.setFireInfo(Blocks.ACACIA_LOG, 5, 5);
        fireblock.setFireInfo(Blocks.DARK_OAK_LOG, 5, 5);
        fireblock.setFireInfo(Blocks.STRIPPED_OAK_LOG, 5, 5);
        fireblock.setFireInfo(Blocks.STRIPPED_SPRUCE_LOG, 5, 5);
        fireblock.setFireInfo(Blocks.STRIPPED_BIRCH_LOG, 5, 5);
        fireblock.setFireInfo(Blocks.STRIPPED_JUNGLE_LOG, 5, 5);
        fireblock.setFireInfo(Blocks.STRIPPED_ACACIA_LOG, 5, 5);
        fireblock.setFireInfo(Blocks.STRIPPED_DARK_OAK_LOG, 5, 5);
        fireblock.setFireInfo(Blocks.STRIPPED_OAK_WOOD, 5, 5);
        fireblock.setFireInfo(Blocks.STRIPPED_SPRUCE_WOOD, 5, 5);
        fireblock.setFireInfo(Blocks.STRIPPED_BIRCH_WOOD, 5, 5);
        fireblock.setFireInfo(Blocks.STRIPPED_JUNGLE_WOOD, 5, 5);
        fireblock.setFireInfo(Blocks.STRIPPED_ACACIA_WOOD, 5, 5);
        fireblock.setFireInfo(Blocks.STRIPPED_DARK_OAK_WOOD, 5, 5);
        fireblock.setFireInfo(Blocks.OAK_WOOD, 5, 5);
        fireblock.setFireInfo(Blocks.SPRUCE_WOOD, 5, 5);
        fireblock.setFireInfo(Blocks.BIRCH_WOOD, 5, 5);
        fireblock.setFireInfo(Blocks.JUNGLE_WOOD, 5, 5);
        fireblock.setFireInfo(Blocks.ACACIA_WOOD, 5, 5);
        fireblock.setFireInfo(Blocks.DARK_OAK_WOOD, 5, 5);
        fireblock.setFireInfo(Blocks.OAK_LEAVES, 30, 60);
        fireblock.setFireInfo(Blocks.SPRUCE_LEAVES, 30, 60);
        fireblock.setFireInfo(Blocks.BIRCH_LEAVES, 30, 60);
        fireblock.setFireInfo(Blocks.JUNGLE_LEAVES, 30, 60);
        fireblock.setFireInfo(Blocks.ACACIA_LEAVES, 30, 60);
        fireblock.setFireInfo(Blocks.DARK_OAK_LEAVES, 30, 60);
        fireblock.setFireInfo(Blocks.BOOKSHELF, 30, 20);
        fireblock.setFireInfo(Blocks.TNT, 15, 100);
        fireblock.setFireInfo(Blocks.GRASS, 60, 100);
        fireblock.setFireInfo(Blocks.FERN, 60, 100);
        fireblock.setFireInfo(Blocks.DEAD_BUSH, 60, 100);
        fireblock.setFireInfo(Blocks.SUNFLOWER, 60, 100);
        fireblock.setFireInfo(Blocks.LILAC, 60, 100);
        fireblock.setFireInfo(Blocks.ROSE_BUSH, 60, 100);
        fireblock.setFireInfo(Blocks.PEONY, 60, 100);
        fireblock.setFireInfo(Blocks.TALL_GRASS, 60, 100);
        fireblock.setFireInfo(Blocks.LARGE_FERN, 60, 100);
        fireblock.setFireInfo(Blocks.DANDELION, 60, 100);
        fireblock.setFireInfo(Blocks.POPPY, 60, 100);
        fireblock.setFireInfo(Blocks.BLUE_ORCHID, 60, 100);
        fireblock.setFireInfo(Blocks.ALLIUM, 60, 100);
        fireblock.setFireInfo(Blocks.AZURE_BLUET, 60, 100);
        fireblock.setFireInfo(Blocks.RED_TULIP, 60, 100);
        fireblock.setFireInfo(Blocks.ORANGE_TULIP, 60, 100);
        fireblock.setFireInfo(Blocks.WHITE_TULIP, 60, 100);
        fireblock.setFireInfo(Blocks.PINK_TULIP, 60, 100);
        fireblock.setFireInfo(Blocks.OXEYE_DAISY, 60, 100);
        fireblock.setFireInfo(Blocks.CORNFLOWER, 60, 100);
        fireblock.setFireInfo(Blocks.LILY_OF_THE_VALLEY, 60, 100);
        fireblock.setFireInfo(Blocks.WITHER_ROSE, 60, 100);
        fireblock.setFireInfo(Blocks.WHITE_WOOL, 30, 60);
        fireblock.setFireInfo(Blocks.ORANGE_WOOL, 30, 60);
        fireblock.setFireInfo(Blocks.MAGENTA_WOOL, 30, 60);
        fireblock.setFireInfo(Blocks.LIGHT_BLUE_WOOL, 30, 60);
        fireblock.setFireInfo(Blocks.YELLOW_WOOL, 30, 60);
        fireblock.setFireInfo(Blocks.LIME_WOOL, 30, 60);
        fireblock.setFireInfo(Blocks.PINK_WOOL, 30, 60);
        fireblock.setFireInfo(Blocks.GRAY_WOOL, 30, 60);
        fireblock.setFireInfo(Blocks.LIGHT_GRAY_WOOL, 30, 60);
        fireblock.setFireInfo(Blocks.CYAN_WOOL, 30, 60);
        fireblock.setFireInfo(Blocks.PURPLE_WOOL, 30, 60);
        fireblock.setFireInfo(Blocks.BLUE_WOOL, 30, 60);
        fireblock.setFireInfo(Blocks.BROWN_WOOL, 30, 60);
        fireblock.setFireInfo(Blocks.GREEN_WOOL, 30, 60);
        fireblock.setFireInfo(Blocks.RED_WOOL, 30, 60);
        fireblock.setFireInfo(Blocks.BLACK_WOOL, 30, 60);
        fireblock.setFireInfo(Blocks.VINE, 15, 100);
        fireblock.setFireInfo(Blocks.COAL_BLOCK, 5, 5);
        fireblock.setFireInfo(Blocks.HAY_BLOCK, 60, 20);
        fireblock.setFireInfo(Blocks.TARGET, 15, 20);
        fireblock.setFireInfo(Blocks.WHITE_CARPET, 60, 20);
        fireblock.setFireInfo(Blocks.ORANGE_CARPET, 60, 20);
        fireblock.setFireInfo(Blocks.MAGENTA_CARPET, 60, 20);
        fireblock.setFireInfo(Blocks.LIGHT_BLUE_CARPET, 60, 20);
        fireblock.setFireInfo(Blocks.YELLOW_CARPET, 60, 20);
        fireblock.setFireInfo(Blocks.LIME_CARPET, 60, 20);
        fireblock.setFireInfo(Blocks.PINK_CARPET, 60, 20);
        fireblock.setFireInfo(Blocks.GRAY_CARPET, 60, 20);
        fireblock.setFireInfo(Blocks.LIGHT_GRAY_CARPET, 60, 20);
        fireblock.setFireInfo(Blocks.CYAN_CARPET, 60, 20);
        fireblock.setFireInfo(Blocks.PURPLE_CARPET, 60, 20);
        fireblock.setFireInfo(Blocks.BLUE_CARPET, 60, 20);
        fireblock.setFireInfo(Blocks.BROWN_CARPET, 60, 20);
        fireblock.setFireInfo(Blocks.GREEN_CARPET, 60, 20);
        fireblock.setFireInfo(Blocks.RED_CARPET, 60, 20);
        fireblock.setFireInfo(Blocks.BLACK_CARPET, 60, 20);
        fireblock.setFireInfo(Blocks.DRIED_KELP_BLOCK, 30, 60);
        fireblock.setFireInfo(Blocks.BAMBOO, 60, 60);
        fireblock.setFireInfo(Blocks.SCAFFOLDING, 60, 60);
        fireblock.setFireInfo(Blocks.LECTERN, 30, 20);
        fireblock.setFireInfo(Blocks.COMPOSTER, 5, 20);
        fireblock.setFireInfo(Blocks.SWEET_BERRY_BUSH, 60, 100);
        fireblock.setFireInfo(Blocks.BEEHIVE, 5, 20);
        fireblock.setFireInfo(Blocks.BEE_NEST, 30, 20);
    }
}