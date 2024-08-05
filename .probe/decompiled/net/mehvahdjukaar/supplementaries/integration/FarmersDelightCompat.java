package net.mehvahdjukaar.supplementaries.integration;

import com.google.common.base.Suppliers;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import java.util.List;
import java.util.function.Supplier;
import net.mehvahdjukaar.moonlight.api.misc.ModSoundType;
import net.mehvahdjukaar.moonlight.api.platform.ClientHelper;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.mehvahdjukaar.supplementaries.Supplementaries;
import net.mehvahdjukaar.supplementaries.common.block.IRopeConnection;
import net.mehvahdjukaar.supplementaries.common.block.ModBlockProperties;
import net.mehvahdjukaar.supplementaries.common.block.blocks.PlanterBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.RopeBlock;
import net.mehvahdjukaar.supplementaries.common.block.blocks.StickBlock;
import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.mehvahdjukaar.supplementaries.integration.forge.FarmersDelightCompatImpl;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.block.TomatoVineBlock;
import vectorwing.farmersdelight.common.tag.ModTags;

public class FarmersDelightCompat {

    public static final ModSoundType STICK_TOMATO_SOUND = new ModSoundType(1.0F, 1.0F, () -> SoundEvents.CROP_BREAK, () -> SoundEvents.GRASS_STEP, () -> SoundEvents.WOOD_PLACE, () -> SoundEvents.GRASS_HIT, () -> SoundEvents.GRASS_FALL);

    public static final Supplier<Block> ROPE_TOMATO = RegHelper.registerBlock(Supplementaries.res("rope_tomatoes"), () -> new FarmersDelightCompat.TomatoRopeBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT).forceSolidOff()));

    public static final Supplier<Block> STICK_TOMATOES = RegHelper.registerBlock(Supplementaries.res("stick_tomatoes"), () -> new FarmersDelightCompat.TomatoStickBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT).forceSolidOff().sound(STICK_TOMATO_SOUND)));

    public static void init() {
    }

    public static boolean tryTomatoLogging(ServerLevel level, BlockPos pos) {
        BlockState state = level.m_8055_(pos);
        if (isTomatoVineClimbingConfigOn()) {
            if (state.m_60713_((Block) ModRegistry.ROPE.get())) {
                BlockState toPlace = ((Block) ROPE_TOMATO.get()).defaultBlockState();
                toPlace = Block.updateFromNeighbourShapes(toPlace, level, pos);
                level.m_7731_(pos, toPlace, 3);
                return true;
            }
            if (state.m_60713_((Block) ModRegistry.STICK_BLOCK.get())) {
                BlockState toPlace = ((Block) STICK_TOMATOES.get()).defaultBlockState();
                level.m_7731_(pos, toPlace, 3);
                return true;
            }
        }
        return false;
    }

    @ExpectPlatform
    @Transformed
    public static boolean isTomatoVineClimbingConfigOn() {
        return FarmersDelightCompatImpl.isTomatoVineClimbingConfigOn();
    }

    public static Block getStickTomato() {
        return (Block) STICK_TOMATOES.get();
    }

    public static void setupClient() {
        ClientHelper.registerRenderType((Block) ROPE_TOMATO.get(), RenderType.cutout());
        ClientHelper.registerRenderType((Block) STICK_TOMATOES.get(), RenderType.cutout());
    }

    public static boolean canAddStickToTomato(BlockState blockstate, BooleanProperty axis) {
        return blockstate.m_60734_() == getStickTomato() ? !(Boolean) blockstate.m_61143_(axis) : false;
    }

    public static PlanterBlock makePlanterRich() {
        return new FarmersDelightCompat.PlanterRichBlock(BlockBehaviour.Properties.copy(Blocks.RED_TERRACOTTA).strength(2.0F, 6.0F).requiresCorrectToolForDrops().randomTicks(), CompatObjects.RICH_SOIL);
    }

    public static class PlanterRichBlock extends PlanterBlock {

        private final Supplier<BlockState> richSoilDelegate;

        public PlanterRichBlock(BlockBehaviour.Properties properties, Supplier<Block> mimic) {
            super(properties);
            this.richSoilDelegate = Suppliers.memoize(() -> ((Block) mimic.get()).defaultBlockState());
            this.m_49959_((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(WATERLOGGED, false)).m_61124_(EXTENDED, false));
        }

        @Override
        public List<ItemStack> getDrops(BlockState blockState, LootParams.Builder builder) {
            return super.m_49635_(blockState, builder);
        }

        @Override
        public void randomTick(BlockState state, ServerLevel worldIn, BlockPos pos, RandomSource rand) {
            if ((Boolean) CommonConfigs.Building.FD_PLANTER.get()) {
                ((BlockState) this.richSoilDelegate.get()).m_222972_(worldIn, pos, rand);
            }
        }
    }

    private abstract static class TomatoLoggedBlock extends TomatoVineBlock {

        public TomatoLoggedBlock(BlockBehaviour.Properties properties) {
            super(properties);
        }

        public void attemptRopeClimb(ServerLevel level, BlockPos pos, RandomSource random) {
            if (random.nextFloat() < 0.3F) {
                BlockPos posAbove = pos.above();
                BlockState stateAbove = level.m_8055_(posAbove);
                boolean canClimb = stateAbove.m_204336_(ModTags.ROPES);
                if (canClimb) {
                    int vineHeight = 1;
                    while (level.m_8055_(pos.below(vineHeight)).m_60734_() instanceof TomatoVineBlock) {
                        vineHeight++;
                    }
                    if (vineHeight < 3) {
                        BlockState toPlace;
                        if (stateAbove.m_60713_((Block) ModRegistry.ROPE.get())) {
                            toPlace = ((Block) FarmersDelightCompat.ROPE_TOMATO.get()).withPropertiesOf(stateAbove);
                        } else if (stateAbove.m_60713_((Block) ModRegistry.STICK_BLOCK.get())) {
                            toPlace = ((Block) FarmersDelightCompat.STICK_TOMATOES.get()).withPropertiesOf(stateAbove);
                        } else {
                            toPlace = (BlockState) ((Block) CompatObjects.TOMATO_CROP.get()).defaultBlockState().m_61124_(TomatoVineBlock.ROPELOGGED, true);
                        }
                        level.m_46597_(posAbove, toPlace);
                    }
                }
            }
        }

        public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
            BlockPos belowPos = pos.below();
            BlockState belowState = level.m_8055_(belowPos);
            return (belowState.m_60734_() instanceof TomatoVineBlock || super.m_7898_((BlockState) state.m_61124_(TomatoVineBlock.ROPELOGGED, false), level, pos)) && this.hasGoodCropConditions(level, pos);
        }

        public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack stack) {
            super.m_6240_(level, player, pos, (BlockState) state.m_61124_(TomatoVineBlock.ROPELOGGED, false), blockEntity, stack);
        }

        public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
            this.m_5707_(level, pos, state, player);
            return level.setBlock(pos, this.getInnerBlock().withPropertiesOf(state), level.isClientSide ? 11 : 3);
        }

        public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
            if (!state.m_60710_(level, pos)) {
                level.m_46796_(2001, pos, Block.getId(state));
                Block.dropResources(state, level, pos, null, null, ItemStack.EMPTY);
                level.m_46597_(pos, this.getInnerBlock().withPropertiesOf(state));
            }
        }

        public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
            if (!state.m_60710_(level, currentPos)) {
                level.scheduleTick(currentPos, this, 1);
            }
            return state;
        }

        public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
            state = ((Block) CompatObjects.TOMATO_CROP.get()).withPropertiesOf(state);
            return state.m_287290_(builder);
        }

        public abstract Block getInnerBlock();
    }

    private static class TomatoRopeBlock extends FarmersDelightCompat.TomatoLoggedBlock implements IRopeConnection {

        public static final BooleanProperty NORTH = BlockStateProperties.NORTH;

        public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;

        public static final BooleanProperty WEST = BlockStateProperties.WEST;

        public static final BooleanProperty EAST = BlockStateProperties.EAST;

        public static final BooleanProperty KNOT = ModBlockProperties.KNOT;

        public TomatoRopeBlock(BlockBehaviour.Properties properties) {
            super(properties);
            this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) this.m_49966_().m_61124_(TomatoVineBlock.ROPELOGGED, true)).m_61124_(KNOT, false)).m_61124_(EAST, false)).m_61124_(WEST, false)).m_61124_(NORTH, false)).m_61124_(SOUTH, false));
        }

        @Override
        public Block getInnerBlock() {
            return (Block) ModRegistry.ROPE.get();
        }

        protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
            builder.add(NORTH, SOUTH, EAST, WEST, KNOT);
            super.m_7926_(builder);
        }

        @Override
        public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
            super.updateShape(state, facing, facingState, level, currentPos, facingPos);
            if (facing.getAxis() == Direction.Axis.Y) {
                return state;
            } else {
                BlockState newState = (BlockState) state.m_61124_((Property) RopeBlock.FACING_TO_PROPERTY_MAP.get(facing), this.shouldConnectToFace(state, facingState, facingPos, facing, level));
                boolean hasKnot = (Boolean) newState.m_61143_(SOUTH) || (Boolean) newState.m_61143_(EAST) || (Boolean) newState.m_61143_(NORTH) || (Boolean) newState.m_61143_(WEST);
                return (BlockState) newState.m_61124_(KNOT, hasKnot);
            }
        }

        @Override
        public boolean canSideAcceptConnection(BlockState state, Direction direction) {
            return true;
        }
    }

    private static class TomatoStickBlock extends FarmersDelightCompat.TomatoLoggedBlock {

        public static final BooleanProperty AXIS_X = ModBlockProperties.AXIS_X;

        public static final BooleanProperty AXIS_Z = ModBlockProperties.AXIS_Z;

        public TomatoStickBlock(BlockBehaviour.Properties properties) {
            super(properties);
            this.m_49959_((BlockState) ((BlockState) ((BlockState) this.m_49966_().m_61124_(TomatoVineBlock.ROPELOGGED, true)).m_61124_(AXIS_X, false)).m_61124_(AXIS_Z, false));
        }

        public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
            return StickBlock.getStickShape((Boolean) state.m_61143_(AXIS_X), true, (Boolean) state.m_61143_(AXIS_Z));
        }

        @Override
        public Block getInnerBlock() {
            return (Block) ModRegistry.STICK_BLOCK.get();
        }

        protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
            builder.add(AXIS_X, AXIS_Z);
            super.m_7926_(builder);
        }

        public boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
            if (!context.m_7078_() && context.m_43722_().is(Items.STICK)) {
                return switch(context.m_43719_().getAxis()) {
                    case Z ->
                        !state.m_61143_(AXIS_Z);
                    case X ->
                        !state.m_61143_(AXIS_X);
                    default ->
                        false;
                };
            } else {
                return super.m_6864_(state, context);
            }
        }
    }
}