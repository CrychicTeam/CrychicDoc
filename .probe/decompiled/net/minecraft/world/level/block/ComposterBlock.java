package net.minecraft.world.level.block;

import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.WorldlyContainerHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ComposterBlock extends Block implements WorldlyContainerHolder {

    public static final int READY = 8;

    public static final int MIN_LEVEL = 0;

    public static final int MAX_LEVEL = 7;

    public static final IntegerProperty LEVEL = BlockStateProperties.LEVEL_COMPOSTER;

    public static final Object2FloatMap<ItemLike> COMPOSTABLES = new Object2FloatOpenHashMap();

    private static final int AABB_SIDE_THICKNESS = 2;

    private static final VoxelShape OUTER_SHAPE = Shapes.block();

    private static final VoxelShape[] SHAPES = Util.make(new VoxelShape[9], p_51967_ -> {
        for (int $$1 = 0; $$1 < 8; $$1++) {
            p_51967_[$$1] = Shapes.join(OUTER_SHAPE, Block.box(2.0, (double) Math.max(2, 1 + $$1 * 2), 2.0, 14.0, 16.0, 14.0), BooleanOp.ONLY_FIRST);
        }
        p_51967_[8] = p_51967_[7];
    });

    public static void bootStrap() {
        COMPOSTABLES.defaultReturnValue(-1.0F);
        float $$0 = 0.3F;
        float $$1 = 0.5F;
        float $$2 = 0.65F;
        float $$3 = 0.85F;
        float $$4 = 1.0F;
        add(0.3F, Items.JUNGLE_LEAVES);
        add(0.3F, Items.OAK_LEAVES);
        add(0.3F, Items.SPRUCE_LEAVES);
        add(0.3F, Items.DARK_OAK_LEAVES);
        add(0.3F, Items.ACACIA_LEAVES);
        add(0.3F, Items.CHERRY_LEAVES);
        add(0.3F, Items.BIRCH_LEAVES);
        add(0.3F, Items.AZALEA_LEAVES);
        add(0.3F, Items.MANGROVE_LEAVES);
        add(0.3F, Items.OAK_SAPLING);
        add(0.3F, Items.SPRUCE_SAPLING);
        add(0.3F, Items.BIRCH_SAPLING);
        add(0.3F, Items.JUNGLE_SAPLING);
        add(0.3F, Items.ACACIA_SAPLING);
        add(0.3F, Items.CHERRY_SAPLING);
        add(0.3F, Items.DARK_OAK_SAPLING);
        add(0.3F, Items.MANGROVE_PROPAGULE);
        add(0.3F, Items.BEETROOT_SEEDS);
        add(0.3F, Items.DRIED_KELP);
        add(0.3F, Items.GRASS);
        add(0.3F, Items.KELP);
        add(0.3F, Items.MELON_SEEDS);
        add(0.3F, Items.PUMPKIN_SEEDS);
        add(0.3F, Items.SEAGRASS);
        add(0.3F, Items.SWEET_BERRIES);
        add(0.3F, Items.GLOW_BERRIES);
        add(0.3F, Items.WHEAT_SEEDS);
        add(0.3F, Items.MOSS_CARPET);
        add(0.3F, Items.PINK_PETALS);
        add(0.3F, Items.SMALL_DRIPLEAF);
        add(0.3F, Items.HANGING_ROOTS);
        add(0.3F, Items.MANGROVE_ROOTS);
        add(0.3F, Items.TORCHFLOWER_SEEDS);
        add(0.3F, Items.PITCHER_POD);
        add(0.5F, Items.DRIED_KELP_BLOCK);
        add(0.5F, Items.TALL_GRASS);
        add(0.5F, Items.FLOWERING_AZALEA_LEAVES);
        add(0.5F, Items.CACTUS);
        add(0.5F, Items.SUGAR_CANE);
        add(0.5F, Items.VINE);
        add(0.5F, Items.NETHER_SPROUTS);
        add(0.5F, Items.WEEPING_VINES);
        add(0.5F, Items.TWISTING_VINES);
        add(0.5F, Items.MELON_SLICE);
        add(0.5F, Items.GLOW_LICHEN);
        add(0.65F, Items.SEA_PICKLE);
        add(0.65F, Items.LILY_PAD);
        add(0.65F, Items.PUMPKIN);
        add(0.65F, Items.CARVED_PUMPKIN);
        add(0.65F, Items.MELON);
        add(0.65F, Items.APPLE);
        add(0.65F, Items.BEETROOT);
        add(0.65F, Items.CARROT);
        add(0.65F, Items.COCOA_BEANS);
        add(0.65F, Items.POTATO);
        add(0.65F, Items.WHEAT);
        add(0.65F, Items.BROWN_MUSHROOM);
        add(0.65F, Items.RED_MUSHROOM);
        add(0.65F, Items.MUSHROOM_STEM);
        add(0.65F, Items.CRIMSON_FUNGUS);
        add(0.65F, Items.WARPED_FUNGUS);
        add(0.65F, Items.NETHER_WART);
        add(0.65F, Items.CRIMSON_ROOTS);
        add(0.65F, Items.WARPED_ROOTS);
        add(0.65F, Items.SHROOMLIGHT);
        add(0.65F, Items.DANDELION);
        add(0.65F, Items.POPPY);
        add(0.65F, Items.BLUE_ORCHID);
        add(0.65F, Items.ALLIUM);
        add(0.65F, Items.AZURE_BLUET);
        add(0.65F, Items.RED_TULIP);
        add(0.65F, Items.ORANGE_TULIP);
        add(0.65F, Items.WHITE_TULIP);
        add(0.65F, Items.PINK_TULIP);
        add(0.65F, Items.OXEYE_DAISY);
        add(0.65F, Items.CORNFLOWER);
        add(0.65F, Items.LILY_OF_THE_VALLEY);
        add(0.65F, Items.WITHER_ROSE);
        add(0.65F, Items.FERN);
        add(0.65F, Items.SUNFLOWER);
        add(0.65F, Items.LILAC);
        add(0.65F, Items.ROSE_BUSH);
        add(0.65F, Items.PEONY);
        add(0.65F, Items.LARGE_FERN);
        add(0.65F, Items.SPORE_BLOSSOM);
        add(0.65F, Items.AZALEA);
        add(0.65F, Items.MOSS_BLOCK);
        add(0.65F, Items.BIG_DRIPLEAF);
        add(0.85F, Items.HAY_BLOCK);
        add(0.85F, Items.BROWN_MUSHROOM_BLOCK);
        add(0.85F, Items.RED_MUSHROOM_BLOCK);
        add(0.85F, Items.NETHER_WART_BLOCK);
        add(0.85F, Items.WARPED_WART_BLOCK);
        add(0.85F, Items.FLOWERING_AZALEA);
        add(0.85F, Items.BREAD);
        add(0.85F, Items.BAKED_POTATO);
        add(0.85F, Items.COOKIE);
        add(0.85F, Items.TORCHFLOWER);
        add(0.85F, Items.PITCHER_PLANT);
        add(1.0F, Items.CAKE);
        add(1.0F, Items.PUMPKIN_PIE);
    }

    private static void add(float float0, ItemLike itemLike1) {
        COMPOSTABLES.put(itemLike1.asItem(), float0);
    }

    public ComposterBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
        this.m_49959_((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(LEVEL, 0));
    }

    public static void handleFill(Level level0, BlockPos blockPos1, boolean boolean2) {
        BlockState $$3 = level0.getBlockState(blockPos1);
        level0.playLocalSound(blockPos1, boolean2 ? SoundEvents.COMPOSTER_FILL_SUCCESS : SoundEvents.COMPOSTER_FILL, SoundSource.BLOCKS, 1.0F, 1.0F, false);
        double $$4 = $$3.m_60808_(level0, blockPos1).max(Direction.Axis.Y, 0.5, 0.5) + 0.03125;
        double $$5 = 0.13125F;
        double $$6 = 0.7375F;
        RandomSource $$7 = level0.getRandom();
        for (int $$8 = 0; $$8 < 10; $$8++) {
            double $$9 = $$7.nextGaussian() * 0.02;
            double $$10 = $$7.nextGaussian() * 0.02;
            double $$11 = $$7.nextGaussian() * 0.02;
            level0.addParticle(ParticleTypes.COMPOSTER, (double) blockPos1.m_123341_() + 0.13125F + 0.7375F * (double) $$7.nextFloat(), (double) blockPos1.m_123342_() + $$4 + (double) $$7.nextFloat() * (1.0 - $$4), (double) blockPos1.m_123343_() + 0.13125F + 0.7375F * (double) $$7.nextFloat(), $$9, $$10, $$11);
        }
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return SHAPES[blockState0.m_61143_(LEVEL)];
    }

    @Override
    public VoxelShape getInteractionShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2) {
        return OUTER_SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return SHAPES[0];
    }

    @Override
    public void onPlace(BlockState blockState0, Level level1, BlockPos blockPos2, BlockState blockState3, boolean boolean4) {
        if ((Integer) blockState0.m_61143_(LEVEL) == 7) {
            level1.m_186460_(blockPos2, blockState0.m_60734_(), 20);
        }
    }

    @Override
    public InteractionResult use(BlockState blockState0, Level level1, BlockPos blockPos2, Player player3, InteractionHand interactionHand4, BlockHitResult blockHitResult5) {
        int $$6 = (Integer) blockState0.m_61143_(LEVEL);
        ItemStack $$7 = player3.m_21120_(interactionHand4);
        if ($$6 < 8 && COMPOSTABLES.containsKey($$7.getItem())) {
            if ($$6 < 7 && !level1.isClientSide) {
                BlockState $$8 = addItem(player3, blockState0, level1, blockPos2, $$7);
                level1.m_46796_(1500, blockPos2, blockState0 != $$8 ? 1 : 0);
                player3.awardStat(Stats.ITEM_USED.get($$7.getItem()));
                if (!player3.getAbilities().instabuild) {
                    $$7.shrink(1);
                }
            }
            return InteractionResult.sidedSuccess(level1.isClientSide);
        } else if ($$6 == 8) {
            extractProduce(player3, blockState0, level1, blockPos2);
            return InteractionResult.sidedSuccess(level1.isClientSide);
        } else {
            return InteractionResult.PASS;
        }
    }

    public static BlockState insertItem(Entity entity0, BlockState blockState1, ServerLevel serverLevel2, ItemStack itemStack3, BlockPos blockPos4) {
        int $$5 = (Integer) blockState1.m_61143_(LEVEL);
        if ($$5 < 7 && COMPOSTABLES.containsKey(itemStack3.getItem())) {
            BlockState $$6 = addItem(entity0, blockState1, serverLevel2, blockPos4, itemStack3);
            itemStack3.shrink(1);
            return $$6;
        } else {
            return blockState1;
        }
    }

    public static BlockState extractProduce(Entity entity0, BlockState blockState1, Level level2, BlockPos blockPos3) {
        if (!level2.isClientSide) {
            Vec3 $$4 = Vec3.atLowerCornerWithOffset(blockPos3, 0.5, 1.01, 0.5).offsetRandom(level2.random, 0.7F);
            ItemEntity $$5 = new ItemEntity(level2, $$4.x(), $$4.y(), $$4.z(), new ItemStack(Items.BONE_MEAL));
            $$5.setDefaultPickUpDelay();
            level2.m_7967_($$5);
        }
        BlockState $$6 = empty(entity0, blockState1, level2, blockPos3);
        level2.playSound(null, blockPos3, SoundEvents.COMPOSTER_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
        return $$6;
    }

    static BlockState empty(@Nullable Entity entity0, BlockState blockState1, LevelAccessor levelAccessor2, BlockPos blockPos3) {
        BlockState $$4 = (BlockState) blockState1.m_61124_(LEVEL, 0);
        levelAccessor2.m_7731_(blockPos3, $$4, 3);
        levelAccessor2.gameEvent(GameEvent.BLOCK_CHANGE, blockPos3, GameEvent.Context.of(entity0, $$4));
        return $$4;
    }

    static BlockState addItem(@Nullable Entity entity0, BlockState blockState1, LevelAccessor levelAccessor2, BlockPos blockPos3, ItemStack itemStack4) {
        int $$5 = (Integer) blockState1.m_61143_(LEVEL);
        float $$6 = COMPOSTABLES.getFloat(itemStack4.getItem());
        if (($$5 != 0 || !($$6 > 0.0F)) && !(levelAccessor2.getRandom().nextDouble() < (double) $$6)) {
            return blockState1;
        } else {
            int $$7 = $$5 + 1;
            BlockState $$8 = (BlockState) blockState1.m_61124_(LEVEL, $$7);
            levelAccessor2.m_7731_(blockPos3, $$8, 3);
            levelAccessor2.gameEvent(GameEvent.BLOCK_CHANGE, blockPos3, GameEvent.Context.of(entity0, $$8));
            if ($$7 == 7) {
                levelAccessor2.scheduleTick(blockPos3, blockState1.m_60734_(), 20);
            }
            return $$8;
        }
    }

    @Override
    public void tick(BlockState blockState0, ServerLevel serverLevel1, BlockPos blockPos2, RandomSource randomSource3) {
        if ((Integer) blockState0.m_61143_(LEVEL) == 7) {
            serverLevel1.m_7731_(blockPos2, (BlockState) blockState0.m_61122_(LEVEL), 3);
            serverLevel1.m_5594_(null, blockPos2, SoundEvents.COMPOSTER_READY, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState blockState0) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState0, Level level1, BlockPos blockPos2) {
        return (Integer) blockState0.m_61143_(LEVEL);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(LEVEL);
    }

    @Override
    public boolean isPathfindable(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, PathComputationType pathComputationType3) {
        return false;
    }

    @Override
    public WorldlyContainer getContainer(BlockState blockState0, LevelAccessor levelAccessor1, BlockPos blockPos2) {
        int $$3 = (Integer) blockState0.m_61143_(LEVEL);
        if ($$3 == 8) {
            return new ComposterBlock.OutputContainer(blockState0, levelAccessor1, blockPos2, new ItemStack(Items.BONE_MEAL));
        } else {
            return (WorldlyContainer) ($$3 < 7 ? new ComposterBlock.InputContainer(blockState0, levelAccessor1, blockPos2) : new ComposterBlock.EmptyContainer());
        }
    }

    static class EmptyContainer extends SimpleContainer implements WorldlyContainer {

        public EmptyContainer() {
            super(0);
        }

        @Override
        public int[] getSlotsForFace(Direction direction0) {
            return new int[0];
        }

        @Override
        public boolean canPlaceItemThroughFace(int int0, ItemStack itemStack1, @Nullable Direction direction2) {
            return false;
        }

        @Override
        public boolean canTakeItemThroughFace(int int0, ItemStack itemStack1, Direction direction2) {
            return false;
        }
    }

    static class InputContainer extends SimpleContainer implements WorldlyContainer {

        private final BlockState state;

        private final LevelAccessor level;

        private final BlockPos pos;

        private boolean changed;

        public InputContainer(BlockState blockState0, LevelAccessor levelAccessor1, BlockPos blockPos2) {
            super(1);
            this.state = blockState0;
            this.level = levelAccessor1;
            this.pos = blockPos2;
        }

        @Override
        public int getMaxStackSize() {
            return 1;
        }

        @Override
        public int[] getSlotsForFace(Direction direction0) {
            return direction0 == Direction.UP ? new int[] { 0 } : new int[0];
        }

        @Override
        public boolean canPlaceItemThroughFace(int int0, ItemStack itemStack1, @Nullable Direction direction2) {
            return !this.changed && direction2 == Direction.UP && ComposterBlock.COMPOSTABLES.containsKey(itemStack1.getItem());
        }

        @Override
        public boolean canTakeItemThroughFace(int int0, ItemStack itemStack1, Direction direction2) {
            return false;
        }

        @Override
        public void setChanged() {
            ItemStack $$0 = this.m_8020_(0);
            if (!$$0.isEmpty()) {
                this.changed = true;
                BlockState $$1 = ComposterBlock.addItem(null, this.state, this.level, this.pos, $$0);
                this.level.levelEvent(1500, this.pos, $$1 != this.state ? 1 : 0);
                this.m_8016_(0);
            }
        }
    }

    static class OutputContainer extends SimpleContainer implements WorldlyContainer {

        private final BlockState state;

        private final LevelAccessor level;

        private final BlockPos pos;

        private boolean changed;

        public OutputContainer(BlockState blockState0, LevelAccessor levelAccessor1, BlockPos blockPos2, ItemStack itemStack3) {
            super(itemStack3);
            this.state = blockState0;
            this.level = levelAccessor1;
            this.pos = blockPos2;
        }

        @Override
        public int getMaxStackSize() {
            return 1;
        }

        @Override
        public int[] getSlotsForFace(Direction direction0) {
            return direction0 == Direction.DOWN ? new int[] { 0 } : new int[0];
        }

        @Override
        public boolean canPlaceItemThroughFace(int int0, ItemStack itemStack1, @Nullable Direction direction2) {
            return false;
        }

        @Override
        public boolean canTakeItemThroughFace(int int0, ItemStack itemStack1, Direction direction2) {
            return !this.changed && direction2 == Direction.DOWN && itemStack1.is(Items.BONE_MEAL);
        }

        @Override
        public void setChanged() {
            ComposterBlock.empty(null, this.state, this.level, this.pos);
            this.changed = true;
        }
    }
}