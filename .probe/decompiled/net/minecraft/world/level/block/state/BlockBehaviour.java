package net.minecraft.world.level.block.state;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.MapCodec;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.flag.FeatureElement;
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.EmptyBlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.SupportType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public abstract class BlockBehaviour implements FeatureElement {

    protected static final Direction[] UPDATE_SHAPE_ORDER = new Direction[] { Direction.WEST, Direction.EAST, Direction.NORTH, Direction.SOUTH, Direction.DOWN, Direction.UP };

    protected final boolean hasCollision;

    protected final float explosionResistance;

    protected final boolean isRandomlyTicking;

    protected final SoundType soundType;

    protected final float friction;

    protected final float speedFactor;

    protected final float jumpFactor;

    protected final boolean dynamicShape;

    protected final FeatureFlagSet requiredFeatures;

    protected final BlockBehaviour.Properties properties;

    @Nullable
    protected ResourceLocation drops;

    public BlockBehaviour(BlockBehaviour.Properties blockBehaviourProperties0) {
        this.hasCollision = blockBehaviourProperties0.hasCollision;
        this.drops = blockBehaviourProperties0.drops;
        this.explosionResistance = blockBehaviourProperties0.explosionResistance;
        this.isRandomlyTicking = blockBehaviourProperties0.isRandomlyTicking;
        this.soundType = blockBehaviourProperties0.soundType;
        this.friction = blockBehaviourProperties0.friction;
        this.speedFactor = blockBehaviourProperties0.speedFactor;
        this.jumpFactor = blockBehaviourProperties0.jumpFactor;
        this.dynamicShape = blockBehaviourProperties0.dynamicShape;
        this.requiredFeatures = blockBehaviourProperties0.requiredFeatures;
        this.properties = blockBehaviourProperties0;
    }

    @Deprecated
    public void updateIndirectNeighbourShapes(BlockState blockState0, LevelAccessor levelAccessor1, BlockPos blockPos2, int int3, int int4) {
    }

    @Deprecated
    public boolean isPathfindable(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, PathComputationType pathComputationType3) {
        switch(pathComputationType3) {
            case LAND:
                return !blockState0.m_60838_(blockGetter1, blockPos2);
            case WATER:
                return blockGetter1.getFluidState(blockPos2).is(FluidTags.WATER);
            case AIR:
                return !blockState0.m_60838_(blockGetter1, blockPos2);
            default:
                return false;
        }
    }

    @Deprecated
    public BlockState updateShape(BlockState blockState0, Direction direction1, BlockState blockState2, LevelAccessor levelAccessor3, BlockPos blockPos4, BlockPos blockPos5) {
        return blockState0;
    }

    @Deprecated
    public boolean skipRendering(BlockState blockState0, BlockState blockState1, Direction direction2) {
        return false;
    }

    @Deprecated
    public void neighborChanged(BlockState blockState0, Level level1, BlockPos blockPos2, Block block3, BlockPos blockPos4, boolean boolean5) {
        DebugPackets.sendNeighborsUpdatePacket(level1, blockPos2);
    }

    @Deprecated
    public void onPlace(BlockState blockState0, Level level1, BlockPos blockPos2, BlockState blockState3, boolean boolean4) {
    }

    @Deprecated
    public void onRemove(BlockState blockState0, Level level1, BlockPos blockPos2, BlockState blockState3, boolean boolean4) {
        if (blockState0.m_155947_() && !blockState0.m_60713_(blockState3.m_60734_())) {
            level1.removeBlockEntity(blockPos2);
        }
    }

    @Deprecated
    public InteractionResult use(BlockState blockState0, Level level1, BlockPos blockPos2, Player player3, InteractionHand interactionHand4, BlockHitResult blockHitResult5) {
        return InteractionResult.PASS;
    }

    @Deprecated
    public boolean triggerEvent(BlockState blockState0, Level level1, BlockPos blockPos2, int int3, int int4) {
        return false;
    }

    @Deprecated
    public RenderShape getRenderShape(BlockState blockState0) {
        return RenderShape.MODEL;
    }

    @Deprecated
    public boolean useShapeForLightOcclusion(BlockState blockState0) {
        return false;
    }

    @Deprecated
    public boolean isSignalSource(BlockState blockState0) {
        return false;
    }

    @Deprecated
    public FluidState getFluidState(BlockState blockState0) {
        return Fluids.EMPTY.defaultFluidState();
    }

    @Deprecated
    public boolean hasAnalogOutputSignal(BlockState blockState0) {
        return false;
    }

    public float getMaxHorizontalOffset() {
        return 0.25F;
    }

    public float getMaxVerticalOffset() {
        return 0.2F;
    }

    @Override
    public FeatureFlagSet requiredFeatures() {
        return this.requiredFeatures;
    }

    @Deprecated
    public BlockState rotate(BlockState blockState0, Rotation rotation1) {
        return blockState0;
    }

    @Deprecated
    public BlockState mirror(BlockState blockState0, Mirror mirror1) {
        return blockState0;
    }

    @Deprecated
    public boolean canBeReplaced(BlockState blockState0, BlockPlaceContext blockPlaceContext1) {
        return blockState0.m_247087_() && (blockPlaceContext1.m_43722_().isEmpty() || !blockPlaceContext1.m_43722_().is(this.asItem()));
    }

    @Deprecated
    public boolean canBeReplaced(BlockState blockState0, Fluid fluid1) {
        return blockState0.m_247087_() || !blockState0.m_280296_();
    }

    @Deprecated
    public List<ItemStack> getDrops(BlockState blockState0, LootParams.Builder lootParamsBuilder1) {
        ResourceLocation $$2 = this.getLootTable();
        if ($$2 == BuiltInLootTables.EMPTY) {
            return Collections.emptyList();
        } else {
            LootParams $$3 = lootParamsBuilder1.withParameter(LootContextParams.BLOCK_STATE, blockState0).create(LootContextParamSets.BLOCK);
            ServerLevel $$4 = $$3.getLevel();
            LootTable $$5 = $$4.getServer().getLootData().m_278676_($$2);
            return $$5.getRandomItems($$3);
        }
    }

    @Deprecated
    public long getSeed(BlockState blockState0, BlockPos blockPos1) {
        return Mth.getSeed(blockPos1);
    }

    @Deprecated
    public VoxelShape getOcclusionShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2) {
        return blockState0.m_60808_(blockGetter1, blockPos2);
    }

    @Deprecated
    public VoxelShape getBlockSupportShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2) {
        return this.getCollisionShape(blockState0, blockGetter1, blockPos2, CollisionContext.empty());
    }

    @Deprecated
    public VoxelShape getInteractionShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2) {
        return Shapes.empty();
    }

    @Deprecated
    public int getLightBlock(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2) {
        if (blockState0.m_60804_(blockGetter1, blockPos2)) {
            return blockGetter1.getMaxLightLevel();
        } else {
            return blockState0.m_60631_(blockGetter1, blockPos2) ? 0 : 1;
        }
    }

    @Nullable
    @Deprecated
    public MenuProvider getMenuProvider(BlockState blockState0, Level level1, BlockPos blockPos2) {
        return null;
    }

    @Deprecated
    public boolean canSurvive(BlockState blockState0, LevelReader levelReader1, BlockPos blockPos2) {
        return true;
    }

    @Deprecated
    public float getShadeBrightness(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2) {
        return blockState0.m_60838_(blockGetter1, blockPos2) ? 0.2F : 1.0F;
    }

    @Deprecated
    public int getAnalogOutputSignal(BlockState blockState0, Level level1, BlockPos blockPos2) {
        return 0;
    }

    @Deprecated
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return Shapes.block();
    }

    @Deprecated
    public VoxelShape getCollisionShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return this.hasCollision ? blockState0.m_60808_(blockGetter1, blockPos2) : Shapes.empty();
    }

    @Deprecated
    public boolean isCollisionShapeFullBlock(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2) {
        return Block.isShapeFullBlock(blockState0.m_60812_(blockGetter1, blockPos2));
    }

    @Deprecated
    public boolean isOcclusionShapeFullBlock(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2) {
        return Block.isShapeFullBlock(blockState0.m_60768_(blockGetter1, blockPos2));
    }

    @Deprecated
    public VoxelShape getVisualShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return this.getCollisionShape(blockState0, blockGetter1, blockPos2, collisionContext3);
    }

    @Deprecated
    public void randomTick(BlockState blockState0, ServerLevel serverLevel1, BlockPos blockPos2, RandomSource randomSource3) {
        this.tick(blockState0, serverLevel1, blockPos2, randomSource3);
    }

    @Deprecated
    public void tick(BlockState blockState0, ServerLevel serverLevel1, BlockPos blockPos2, RandomSource randomSource3) {
    }

    @Deprecated
    public float getDestroyProgress(BlockState blockState0, Player player1, BlockGetter blockGetter2, BlockPos blockPos3) {
        float $$4 = blockState0.m_60800_(blockGetter2, blockPos3);
        if ($$4 == -1.0F) {
            return 0.0F;
        } else {
            int $$5 = player1.hasCorrectToolForDrops(blockState0) ? 30 : 100;
            return player1.getDestroySpeed(blockState0) / $$4 / (float) $$5;
        }
    }

    @Deprecated
    public void spawnAfterBreak(BlockState blockState0, ServerLevel serverLevel1, BlockPos blockPos2, ItemStack itemStack3, boolean boolean4) {
    }

    @Deprecated
    public void attack(BlockState blockState0, Level level1, BlockPos blockPos2, Player player3) {
    }

    @Deprecated
    public int getSignal(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, Direction direction3) {
        return 0;
    }

    @Deprecated
    public void entityInside(BlockState blockState0, Level level1, BlockPos blockPos2, Entity entity3) {
    }

    @Deprecated
    public int getDirectSignal(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, Direction direction3) {
        return 0;
    }

    public final ResourceLocation getLootTable() {
        if (this.drops == null) {
            ResourceLocation $$0 = BuiltInRegistries.BLOCK.getKey(this.asBlock());
            this.drops = $$0.withPrefix("blocks/");
        }
        return this.drops;
    }

    @Deprecated
    public void onProjectileHit(Level level0, BlockState blockState1, BlockHitResult blockHitResult2, Projectile projectile3) {
    }

    public abstract Item asItem();

    protected abstract Block asBlock();

    public MapColor defaultMapColor() {
        return (MapColor) this.properties.mapColor.apply(this.asBlock().defaultBlockState());
    }

    public float defaultDestroyTime() {
        return this.properties.destroyTime;
    }

    public abstract static class BlockStateBase extends StateHolder<Block, BlockState> {

        private final int lightEmission;

        private final boolean useShapeForLightOcclusion;

        private final boolean isAir;

        private final boolean ignitedByLava;

        @Deprecated
        private final boolean liquid;

        @Deprecated
        private boolean legacySolid;

        private final PushReaction pushReaction;

        private final MapColor mapColor;

        private final float destroySpeed;

        private final boolean requiresCorrectToolForDrops;

        private final boolean canOcclude;

        private final BlockBehaviour.StatePredicate isRedstoneConductor;

        private final BlockBehaviour.StatePredicate isSuffocating;

        private final BlockBehaviour.StatePredicate isViewBlocking;

        private final BlockBehaviour.StatePredicate hasPostProcess;

        private final BlockBehaviour.StatePredicate emissiveRendering;

        private final Optional<BlockBehaviour.OffsetFunction> offsetFunction;

        private final boolean spawnParticlesOnBreak;

        private final NoteBlockInstrument instrument;

        private final boolean replaceable;

        @Nullable
        protected BlockBehaviour.BlockStateBase.Cache cache;

        private FluidState fluidState = Fluids.EMPTY.defaultFluidState();

        private boolean isRandomlyTicking;

        protected BlockStateBase(Block block0, ImmutableMap<Property<?>, Comparable<?>> immutableMapPropertyComparable1, MapCodec<BlockState> mapCodecBlockState2) {
            super(block0, immutableMapPropertyComparable1, mapCodecBlockState2);
            BlockBehaviour.Properties $$3 = block0.f_60439_;
            this.lightEmission = $$3.lightEmission.applyAsInt(this.asState());
            this.useShapeForLightOcclusion = block0.m_7923_(this.asState());
            this.isAir = $$3.isAir;
            this.ignitedByLava = $$3.ignitedByLava;
            this.liquid = $$3.liquid;
            this.pushReaction = $$3.pushReaction;
            this.mapColor = (MapColor) $$3.mapColor.apply(this.asState());
            this.destroySpeed = $$3.destroyTime;
            this.requiresCorrectToolForDrops = $$3.requiresCorrectToolForDrops;
            this.canOcclude = $$3.canOcclude;
            this.isRedstoneConductor = $$3.isRedstoneConductor;
            this.isSuffocating = $$3.isSuffocating;
            this.isViewBlocking = $$3.isViewBlocking;
            this.hasPostProcess = $$3.hasPostProcess;
            this.emissiveRendering = $$3.emissiveRendering;
            this.offsetFunction = $$3.offsetFunction;
            this.spawnParticlesOnBreak = $$3.spawnParticlesOnBreak;
            this.instrument = $$3.instrument;
            this.replaceable = $$3.replaceable;
        }

        private boolean calculateSolid() {
            if (((Block) this.f_61112_).f_60439_.forceSolidOn) {
                return true;
            } else if (((Block) this.f_61112_).f_60439_.forceSolidOff) {
                return false;
            } else if (this.cache == null) {
                return false;
            } else {
                VoxelShape $$0 = this.cache.collisionShape;
                if ($$0.isEmpty()) {
                    return false;
                } else {
                    AABB $$1 = $$0.bounds();
                    return $$1.getSize() >= 0.7291666666666666 ? true : $$1.getYsize() >= 1.0;
                }
            }
        }

        public void initCache() {
            this.fluidState = ((Block) this.f_61112_).m_5888_(this.asState());
            this.isRandomlyTicking = ((Block) this.f_61112_).isRandomlyTicking(this.asState());
            if (!this.getBlock().hasDynamicShape()) {
                this.cache = new BlockBehaviour.BlockStateBase.Cache(this.asState());
            }
            this.legacySolid = this.calculateSolid();
        }

        public Block getBlock() {
            return (Block) this.f_61112_;
        }

        public Holder<Block> getBlockHolder() {
            return ((Block) this.f_61112_).builtInRegistryHolder();
        }

        @Deprecated
        public boolean blocksMotion() {
            Block $$0 = this.getBlock();
            return $$0 != Blocks.COBWEB && $$0 != Blocks.BAMBOO_SAPLING && this.isSolid();
        }

        @Deprecated
        public boolean isSolid() {
            return this.legacySolid;
        }

        public boolean isValidSpawn(BlockGetter blockGetter0, BlockPos blockPos1, EntityType<?> entityType2) {
            return this.getBlock().f_60439_.isValidSpawn.test(this.asState(), blockGetter0, blockPos1, entityType2);
        }

        public boolean propagatesSkylightDown(BlockGetter blockGetter0, BlockPos blockPos1) {
            return this.cache != null ? this.cache.propagatesSkylightDown : this.getBlock().propagatesSkylightDown(this.asState(), blockGetter0, blockPos1);
        }

        public int getLightBlock(BlockGetter blockGetter0, BlockPos blockPos1) {
            return this.cache != null ? this.cache.lightBlock : this.getBlock().m_7753_(this.asState(), blockGetter0, blockPos1);
        }

        public VoxelShape getFaceOcclusionShape(BlockGetter blockGetter0, BlockPos blockPos1, Direction direction2) {
            return this.cache != null && this.cache.occlusionShapes != null ? this.cache.occlusionShapes[direction2.ordinal()] : Shapes.getFaceShape(this.getOcclusionShape(blockGetter0, blockPos1), direction2);
        }

        public VoxelShape getOcclusionShape(BlockGetter blockGetter0, BlockPos blockPos1) {
            return this.getBlock().m_7952_(this.asState(), blockGetter0, blockPos1);
        }

        public boolean hasLargeCollisionShape() {
            return this.cache == null || this.cache.largeCollisionShape;
        }

        public boolean useShapeForLightOcclusion() {
            return this.useShapeForLightOcclusion;
        }

        public int getLightEmission() {
            return this.lightEmission;
        }

        public boolean isAir() {
            return this.isAir;
        }

        public boolean ignitedByLava() {
            return this.ignitedByLava;
        }

        @Deprecated
        public boolean liquid() {
            return this.liquid;
        }

        public MapColor getMapColor(BlockGetter blockGetter0, BlockPos blockPos1) {
            return this.mapColor;
        }

        public BlockState rotate(Rotation rotation0) {
            return this.getBlock().m_6843_(this.asState(), rotation0);
        }

        public BlockState mirror(Mirror mirror0) {
            return this.getBlock().m_6943_(this.asState(), mirror0);
        }

        public RenderShape getRenderShape() {
            return this.getBlock().m_7514_(this.asState());
        }

        public boolean emissiveRendering(BlockGetter blockGetter0, BlockPos blockPos1) {
            return this.emissiveRendering.test(this.asState(), blockGetter0, blockPos1);
        }

        public float getShadeBrightness(BlockGetter blockGetter0, BlockPos blockPos1) {
            return this.getBlock().m_7749_(this.asState(), blockGetter0, blockPos1);
        }

        public boolean isRedstoneConductor(BlockGetter blockGetter0, BlockPos blockPos1) {
            return this.isRedstoneConductor.test(this.asState(), blockGetter0, blockPos1);
        }

        public boolean isSignalSource() {
            return this.getBlock().m_7899_(this.asState());
        }

        public int getSignal(BlockGetter blockGetter0, BlockPos blockPos1, Direction direction2) {
            return this.getBlock().m_6378_(this.asState(), blockGetter0, blockPos1, direction2);
        }

        public boolean hasAnalogOutputSignal() {
            return this.getBlock().m_7278_(this.asState());
        }

        public int getAnalogOutputSignal(Level level0, BlockPos blockPos1) {
            return this.getBlock().m_6782_(this.asState(), level0, blockPos1);
        }

        public float getDestroySpeed(BlockGetter blockGetter0, BlockPos blockPos1) {
            return this.destroySpeed;
        }

        public float getDestroyProgress(Player player0, BlockGetter blockGetter1, BlockPos blockPos2) {
            return this.getBlock().m_5880_(this.asState(), player0, blockGetter1, blockPos2);
        }

        public int getDirectSignal(BlockGetter blockGetter0, BlockPos blockPos1, Direction direction2) {
            return this.getBlock().m_6376_(this.asState(), blockGetter0, blockPos1, direction2);
        }

        public PushReaction getPistonPushReaction() {
            return this.pushReaction;
        }

        public boolean isSolidRender(BlockGetter blockGetter0, BlockPos blockPos1) {
            if (this.cache != null) {
                return this.cache.solidRender;
            } else {
                BlockState $$2 = this.asState();
                return $$2.m_60815_() ? Block.isShapeFullBlock($$2.m_60768_(blockGetter0, blockPos1)) : false;
            }
        }

        public boolean canOcclude() {
            return this.canOcclude;
        }

        public boolean skipRendering(BlockState blockState0, Direction direction1) {
            return this.getBlock().m_6104_(this.asState(), blockState0, direction1);
        }

        public VoxelShape getShape(BlockGetter blockGetter0, BlockPos blockPos1) {
            return this.getShape(blockGetter0, blockPos1, CollisionContext.empty());
        }

        public VoxelShape getShape(BlockGetter blockGetter0, BlockPos blockPos1, CollisionContext collisionContext2) {
            return this.getBlock().m_5940_(this.asState(), blockGetter0, blockPos1, collisionContext2);
        }

        public VoxelShape getCollisionShape(BlockGetter blockGetter0, BlockPos blockPos1) {
            return this.cache != null ? this.cache.collisionShape : this.getCollisionShape(blockGetter0, blockPos1, CollisionContext.empty());
        }

        public VoxelShape getCollisionShape(BlockGetter blockGetter0, BlockPos blockPos1, CollisionContext collisionContext2) {
            return this.getBlock().m_5939_(this.asState(), blockGetter0, blockPos1, collisionContext2);
        }

        public VoxelShape getBlockSupportShape(BlockGetter blockGetter0, BlockPos blockPos1) {
            return this.getBlock().m_7947_(this.asState(), blockGetter0, blockPos1);
        }

        public VoxelShape getVisualShape(BlockGetter blockGetter0, BlockPos blockPos1, CollisionContext collisionContext2) {
            return this.getBlock().m_5909_(this.asState(), blockGetter0, blockPos1, collisionContext2);
        }

        public VoxelShape getInteractionShape(BlockGetter blockGetter0, BlockPos blockPos1) {
            return this.getBlock().m_6079_(this.asState(), blockGetter0, blockPos1);
        }

        public final boolean entityCanStandOn(BlockGetter blockGetter0, BlockPos blockPos1, Entity entity2) {
            return this.entityCanStandOnFace(blockGetter0, blockPos1, entity2, Direction.UP);
        }

        public final boolean entityCanStandOnFace(BlockGetter blockGetter0, BlockPos blockPos1, Entity entity2, Direction direction3) {
            return Block.isFaceFull(this.getCollisionShape(blockGetter0, blockPos1, CollisionContext.of(entity2)), direction3);
        }

        public Vec3 getOffset(BlockGetter blockGetter0, BlockPos blockPos1) {
            return (Vec3) this.offsetFunction.map(p_273089_ -> p_273089_.evaluate(this.asState(), blockGetter0, blockPos1)).orElse(Vec3.ZERO);
        }

        public boolean hasOffsetFunction() {
            return !this.offsetFunction.isEmpty();
        }

        public boolean triggerEvent(Level level0, BlockPos blockPos1, int int2, int int3) {
            return this.getBlock().m_8133_(this.asState(), level0, blockPos1, int2, int3);
        }

        @Deprecated
        public void neighborChanged(Level level0, BlockPos blockPos1, Block block2, BlockPos blockPos3, boolean boolean4) {
            this.getBlock().m_6861_(this.asState(), level0, blockPos1, block2, blockPos3, boolean4);
        }

        public final void updateNeighbourShapes(LevelAccessor levelAccessor0, BlockPos blockPos1, int int2) {
            this.updateNeighbourShapes(levelAccessor0, blockPos1, int2, 512);
        }

        public final void updateNeighbourShapes(LevelAccessor levelAccessor0, BlockPos blockPos1, int int2, int int3) {
            BlockPos.MutableBlockPos $$4 = new BlockPos.MutableBlockPos();
            for (Direction $$5 : BlockBehaviour.UPDATE_SHAPE_ORDER) {
                $$4.setWithOffset(blockPos1, $$5);
                levelAccessor0.neighborShapeChanged($$5.getOpposite(), this.asState(), $$4, blockPos1, int2, int3);
            }
        }

        public final void updateIndirectNeighbourShapes(LevelAccessor levelAccessor0, BlockPos blockPos1, int int2) {
            this.updateIndirectNeighbourShapes(levelAccessor0, blockPos1, int2, 512);
        }

        public void updateIndirectNeighbourShapes(LevelAccessor levelAccessor0, BlockPos blockPos1, int int2, int int3) {
            this.getBlock().m_7742_(this.asState(), levelAccessor0, blockPos1, int2, int3);
        }

        public void onPlace(Level level0, BlockPos blockPos1, BlockState blockState2, boolean boolean3) {
            this.getBlock().m_6807_(this.asState(), level0, blockPos1, blockState2, boolean3);
        }

        public void onRemove(Level level0, BlockPos blockPos1, BlockState blockState2, boolean boolean3) {
            this.getBlock().m_6810_(this.asState(), level0, blockPos1, blockState2, boolean3);
        }

        public void tick(ServerLevel serverLevel0, BlockPos blockPos1, RandomSource randomSource2) {
            this.getBlock().m_213897_(this.asState(), serverLevel0, blockPos1, randomSource2);
        }

        public void randomTick(ServerLevel serverLevel0, BlockPos blockPos1, RandomSource randomSource2) {
            this.getBlock().m_213898_(this.asState(), serverLevel0, blockPos1, randomSource2);
        }

        public void entityInside(Level level0, BlockPos blockPos1, Entity entity2) {
            this.getBlock().m_7892_(this.asState(), level0, blockPos1, entity2);
        }

        public void spawnAfterBreak(ServerLevel serverLevel0, BlockPos blockPos1, ItemStack itemStack2, boolean boolean3) {
            this.getBlock().m_213646_(this.asState(), serverLevel0, blockPos1, itemStack2, boolean3);
        }

        public List<ItemStack> getDrops(LootParams.Builder lootParamsBuilder0) {
            return this.getBlock().m_49635_(this.asState(), lootParamsBuilder0);
        }

        public InteractionResult use(Level level0, Player player1, InteractionHand interactionHand2, BlockHitResult blockHitResult3) {
            return this.getBlock().m_6227_(this.asState(), level0, blockHitResult3.getBlockPos(), player1, interactionHand2, blockHitResult3);
        }

        public void attack(Level level0, BlockPos blockPos1, Player player2) {
            this.getBlock().m_6256_(this.asState(), level0, blockPos1, player2);
        }

        public boolean isSuffocating(BlockGetter blockGetter0, BlockPos blockPos1) {
            return this.isSuffocating.test(this.asState(), blockGetter0, blockPos1);
        }

        public boolean isViewBlocking(BlockGetter blockGetter0, BlockPos blockPos1) {
            return this.isViewBlocking.test(this.asState(), blockGetter0, blockPos1);
        }

        public BlockState updateShape(Direction direction0, BlockState blockState1, LevelAccessor levelAccessor2, BlockPos blockPos3, BlockPos blockPos4) {
            return this.getBlock().m_7417_(this.asState(), direction0, blockState1, levelAccessor2, blockPos3, blockPos4);
        }

        public boolean isPathfindable(BlockGetter blockGetter0, BlockPos blockPos1, PathComputationType pathComputationType2) {
            return this.getBlock().m_7357_(this.asState(), blockGetter0, blockPos1, pathComputationType2);
        }

        public boolean canBeReplaced(BlockPlaceContext blockPlaceContext0) {
            return this.getBlock().m_6864_(this.asState(), blockPlaceContext0);
        }

        public boolean canBeReplaced(Fluid fluid0) {
            return this.getBlock().m_5946_(this.asState(), fluid0);
        }

        public boolean canBeReplaced() {
            return this.replaceable;
        }

        public boolean canSurvive(LevelReader levelReader0, BlockPos blockPos1) {
            return this.getBlock().m_7898_(this.asState(), levelReader0, blockPos1);
        }

        public boolean hasPostProcess(BlockGetter blockGetter0, BlockPos blockPos1) {
            return this.hasPostProcess.test(this.asState(), blockGetter0, blockPos1);
        }

        @Nullable
        public MenuProvider getMenuProvider(Level level0, BlockPos blockPos1) {
            return this.getBlock().m_7246_(this.asState(), level0, blockPos1);
        }

        public boolean is(TagKey<Block> tagKeyBlock0) {
            return this.getBlock().builtInRegistryHolder().is(tagKeyBlock0);
        }

        public boolean is(TagKey<Block> tagKeyBlock0, Predicate<BlockBehaviour.BlockStateBase> predicateBlockBehaviourBlockStateBase1) {
            return this.is(tagKeyBlock0) && predicateBlockBehaviourBlockStateBase1.test(this);
        }

        public boolean is(HolderSet<Block> holderSetBlock0) {
            return holderSetBlock0.contains(this.getBlock().builtInRegistryHolder());
        }

        public Stream<TagKey<Block>> getTags() {
            return this.getBlock().builtInRegistryHolder().tags();
        }

        public boolean hasBlockEntity() {
            return this.getBlock() instanceof EntityBlock;
        }

        @Nullable
        public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level0, BlockEntityType<T> blockEntityTypeT1) {
            return this.getBlock() instanceof EntityBlock ? ((EntityBlock) this.getBlock()).getTicker(level0, this.asState(), blockEntityTypeT1) : null;
        }

        public boolean is(Block block0) {
            return this.getBlock() == block0;
        }

        public FluidState getFluidState() {
            return this.fluidState;
        }

        public boolean isRandomlyTicking() {
            return this.isRandomlyTicking;
        }

        public long getSeed(BlockPos blockPos0) {
            return this.getBlock().m_7799_(this.asState(), blockPos0);
        }

        public SoundType getSoundType() {
            return this.getBlock().getSoundType(this.asState());
        }

        public void onProjectileHit(Level level0, BlockState blockState1, BlockHitResult blockHitResult2, Projectile projectile3) {
            this.getBlock().m_5581_(level0, blockState1, blockHitResult2, projectile3);
        }

        public boolean isFaceSturdy(BlockGetter blockGetter0, BlockPos blockPos1, Direction direction2) {
            return this.isFaceSturdy(blockGetter0, blockPos1, direction2, SupportType.FULL);
        }

        public boolean isFaceSturdy(BlockGetter blockGetter0, BlockPos blockPos1, Direction direction2, SupportType supportType3) {
            return this.cache != null ? this.cache.isFaceSturdy(direction2, supportType3) : supportType3.isSupporting(this.asState(), blockGetter0, blockPos1, direction2);
        }

        public boolean isCollisionShapeFullBlock(BlockGetter blockGetter0, BlockPos blockPos1) {
            return this.cache != null ? this.cache.isCollisionShapeFullBlock : this.getBlock().m_180643_(this.asState(), blockGetter0, blockPos1);
        }

        protected abstract BlockState asState();

        public boolean requiresCorrectToolForDrops() {
            return this.requiresCorrectToolForDrops;
        }

        public boolean shouldSpawnParticlesOnBreak() {
            return this.spawnParticlesOnBreak;
        }

        public NoteBlockInstrument instrument() {
            return this.instrument;
        }

        static final class Cache {

            private static final Direction[] DIRECTIONS = Direction.values();

            private static final int SUPPORT_TYPE_COUNT = SupportType.values().length;

            protected final boolean solidRender;

            final boolean propagatesSkylightDown;

            final int lightBlock;

            @Nullable
            final VoxelShape[] occlusionShapes;

            protected final VoxelShape collisionShape;

            protected final boolean largeCollisionShape;

            private final boolean[] faceSturdy;

            protected final boolean isCollisionShapeFullBlock;

            Cache(BlockState blockState0) {
                Block $$1 = blockState0.m_60734_();
                this.solidRender = blockState0.m_60804_(EmptyBlockGetter.INSTANCE, BlockPos.ZERO);
                this.propagatesSkylightDown = $$1.propagatesSkylightDown(blockState0, EmptyBlockGetter.INSTANCE, BlockPos.ZERO);
                this.lightBlock = $$1.m_7753_(blockState0, EmptyBlockGetter.INSTANCE, BlockPos.ZERO);
                if (!blockState0.m_60815_()) {
                    this.occlusionShapes = null;
                } else {
                    this.occlusionShapes = new VoxelShape[DIRECTIONS.length];
                    VoxelShape $$2 = $$1.m_7952_(blockState0, EmptyBlockGetter.INSTANCE, BlockPos.ZERO);
                    for (Direction $$3 : DIRECTIONS) {
                        this.occlusionShapes[$$3.ordinal()] = Shapes.getFaceShape($$2, $$3);
                    }
                }
                this.collisionShape = $$1.m_5939_(blockState0, EmptyBlockGetter.INSTANCE, BlockPos.ZERO, CollisionContext.empty());
                if (!this.collisionShape.isEmpty() && blockState0.m_271730_()) {
                    throw new IllegalStateException(String.format(Locale.ROOT, "%s has a collision shape and an offset type, but is not marked as dynamicShape in its properties.", BuiltInRegistries.BLOCK.getKey($$1)));
                } else {
                    this.largeCollisionShape = Arrays.stream(Direction.Axis.values()).anyMatch(p_60860_ -> this.collisionShape.min(p_60860_) < 0.0 || this.collisionShape.max(p_60860_) > 1.0);
                    this.faceSturdy = new boolean[DIRECTIONS.length * SUPPORT_TYPE_COUNT];
                    for (Direction $$4 : DIRECTIONS) {
                        for (SupportType $$5 : SupportType.values()) {
                            this.faceSturdy[getFaceSupportIndex($$4, $$5)] = $$5.isSupporting(blockState0, EmptyBlockGetter.INSTANCE, BlockPos.ZERO, $$4);
                        }
                    }
                    this.isCollisionShapeFullBlock = Block.isShapeFullBlock(blockState0.m_60812_(EmptyBlockGetter.INSTANCE, BlockPos.ZERO));
                }
            }

            public boolean isFaceSturdy(Direction direction0, SupportType supportType1) {
                return this.faceSturdy[getFaceSupportIndex(direction0, supportType1)];
            }

            private static int getFaceSupportIndex(Direction direction0, SupportType supportType1) {
                return direction0.ordinal() * SUPPORT_TYPE_COUNT + supportType1.ordinal();
            }
        }
    }

    public interface OffsetFunction {

        Vec3 evaluate(BlockState var1, BlockGetter var2, BlockPos var3);
    }

    public static enum OffsetType {

        NONE, XZ, XYZ
    }

    public static class Properties {

        Function<BlockState, MapColor> mapColor = p_284884_ -> MapColor.NONE;

        boolean hasCollision = true;

        SoundType soundType = SoundType.STONE;

        ToIntFunction<BlockState> lightEmission = p_60929_ -> 0;

        float explosionResistance;

        float destroyTime;

        boolean requiresCorrectToolForDrops;

        boolean isRandomlyTicking;

        float friction = 0.6F;

        float speedFactor = 1.0F;

        float jumpFactor = 1.0F;

        ResourceLocation drops;

        boolean canOcclude = true;

        boolean isAir;

        boolean ignitedByLava;

        @Deprecated
        boolean liquid;

        @Deprecated
        boolean forceSolidOff;

        boolean forceSolidOn;

        PushReaction pushReaction = PushReaction.NORMAL;

        boolean spawnParticlesOnBreak = true;

        NoteBlockInstrument instrument = NoteBlockInstrument.HARP;

        boolean replaceable;

        BlockBehaviour.StateArgumentPredicate<EntityType<?>> isValidSpawn = (p_284893_, p_284894_, p_284895_, p_284896_) -> p_284893_.m_60783_(p_284894_, p_284895_, Direction.UP) && p_284893_.m_60791_() < 14;

        BlockBehaviour.StatePredicate isRedstoneConductor = (p_284888_, p_284889_, p_284890_) -> p_284888_.m_60838_(p_284889_, p_284890_);

        BlockBehaviour.StatePredicate isSuffocating = (p_284885_, p_284886_, p_284887_) -> p_284885_.m_280555_() && p_284885_.m_60838_(p_284886_, p_284887_);

        BlockBehaviour.StatePredicate isViewBlocking = this.isSuffocating;

        BlockBehaviour.StatePredicate hasPostProcess = (p_60963_, p_60964_, p_60965_) -> false;

        BlockBehaviour.StatePredicate emissiveRendering = (p_60931_, p_60932_, p_60933_) -> false;

        boolean dynamicShape;

        FeatureFlagSet requiredFeatures = FeatureFlags.VANILLA_SET;

        Optional<BlockBehaviour.OffsetFunction> offsetFunction = Optional.empty();

        private Properties() {
        }

        public static BlockBehaviour.Properties of() {
            return new BlockBehaviour.Properties();
        }

        public static BlockBehaviour.Properties copy(BlockBehaviour blockBehaviour0) {
            BlockBehaviour.Properties $$1 = new BlockBehaviour.Properties();
            $$1.destroyTime = blockBehaviour0.properties.destroyTime;
            $$1.explosionResistance = blockBehaviour0.properties.explosionResistance;
            $$1.hasCollision = blockBehaviour0.properties.hasCollision;
            $$1.isRandomlyTicking = blockBehaviour0.properties.isRandomlyTicking;
            $$1.lightEmission = blockBehaviour0.properties.lightEmission;
            $$1.mapColor = blockBehaviour0.properties.mapColor;
            $$1.soundType = blockBehaviour0.properties.soundType;
            $$1.friction = blockBehaviour0.properties.friction;
            $$1.speedFactor = blockBehaviour0.properties.speedFactor;
            $$1.dynamicShape = blockBehaviour0.properties.dynamicShape;
            $$1.canOcclude = blockBehaviour0.properties.canOcclude;
            $$1.isAir = blockBehaviour0.properties.isAir;
            $$1.ignitedByLava = blockBehaviour0.properties.ignitedByLava;
            $$1.liquid = blockBehaviour0.properties.liquid;
            $$1.forceSolidOff = blockBehaviour0.properties.forceSolidOff;
            $$1.forceSolidOn = blockBehaviour0.properties.forceSolidOn;
            $$1.pushReaction = blockBehaviour0.properties.pushReaction;
            $$1.requiresCorrectToolForDrops = blockBehaviour0.properties.requiresCorrectToolForDrops;
            $$1.offsetFunction = blockBehaviour0.properties.offsetFunction;
            $$1.spawnParticlesOnBreak = blockBehaviour0.properties.spawnParticlesOnBreak;
            $$1.requiredFeatures = blockBehaviour0.properties.requiredFeatures;
            $$1.emissiveRendering = blockBehaviour0.properties.emissiveRendering;
            $$1.instrument = blockBehaviour0.properties.instrument;
            $$1.replaceable = blockBehaviour0.properties.replaceable;
            return $$1;
        }

        public BlockBehaviour.Properties mapColor(DyeColor dyeColor0) {
            this.mapColor = p_284892_ -> dyeColor0.getMapColor();
            return this;
        }

        public BlockBehaviour.Properties mapColor(MapColor mapColor0) {
            this.mapColor = p_222988_ -> mapColor0;
            return this;
        }

        public BlockBehaviour.Properties mapColor(Function<BlockState, MapColor> functionBlockStateMapColor0) {
            this.mapColor = functionBlockStateMapColor0;
            return this;
        }

        public BlockBehaviour.Properties noCollission() {
            this.hasCollision = false;
            this.canOcclude = false;
            return this;
        }

        public BlockBehaviour.Properties noOcclusion() {
            this.canOcclude = false;
            return this;
        }

        public BlockBehaviour.Properties friction(float float0) {
            this.friction = float0;
            return this;
        }

        public BlockBehaviour.Properties speedFactor(float float0) {
            this.speedFactor = float0;
            return this;
        }

        public BlockBehaviour.Properties jumpFactor(float float0) {
            this.jumpFactor = float0;
            return this;
        }

        public BlockBehaviour.Properties sound(SoundType soundType0) {
            this.soundType = soundType0;
            return this;
        }

        public BlockBehaviour.Properties lightLevel(ToIntFunction<BlockState> toIntFunctionBlockState0) {
            this.lightEmission = toIntFunctionBlockState0;
            return this;
        }

        public BlockBehaviour.Properties strength(float float0, float float1) {
            return this.destroyTime(float0).explosionResistance(float1);
        }

        public BlockBehaviour.Properties instabreak() {
            return this.strength(0.0F);
        }

        public BlockBehaviour.Properties strength(float float0) {
            this.strength(float0, float0);
            return this;
        }

        public BlockBehaviour.Properties randomTicks() {
            this.isRandomlyTicking = true;
            return this;
        }

        public BlockBehaviour.Properties dynamicShape() {
            this.dynamicShape = true;
            return this;
        }

        public BlockBehaviour.Properties noLootTable() {
            this.drops = BuiltInLootTables.EMPTY;
            return this;
        }

        public BlockBehaviour.Properties dropsLike(Block block0) {
            this.drops = block0.m_60589_();
            return this;
        }

        public BlockBehaviour.Properties ignitedByLava() {
            this.ignitedByLava = true;
            return this;
        }

        public BlockBehaviour.Properties liquid() {
            this.liquid = true;
            return this;
        }

        public BlockBehaviour.Properties forceSolidOn() {
            this.forceSolidOn = true;
            return this;
        }

        @Deprecated
        public BlockBehaviour.Properties forceSolidOff() {
            this.forceSolidOff = true;
            return this;
        }

        public BlockBehaviour.Properties pushReaction(PushReaction pushReaction0) {
            this.pushReaction = pushReaction0;
            return this;
        }

        public BlockBehaviour.Properties air() {
            this.isAir = true;
            return this;
        }

        public BlockBehaviour.Properties isValidSpawn(BlockBehaviour.StateArgumentPredicate<EntityType<?>> blockBehaviourStateArgumentPredicateEntityType0) {
            this.isValidSpawn = blockBehaviourStateArgumentPredicateEntityType0;
            return this;
        }

        public BlockBehaviour.Properties isRedstoneConductor(BlockBehaviour.StatePredicate blockBehaviourStatePredicate0) {
            this.isRedstoneConductor = blockBehaviourStatePredicate0;
            return this;
        }

        public BlockBehaviour.Properties isSuffocating(BlockBehaviour.StatePredicate blockBehaviourStatePredicate0) {
            this.isSuffocating = blockBehaviourStatePredicate0;
            return this;
        }

        public BlockBehaviour.Properties isViewBlocking(BlockBehaviour.StatePredicate blockBehaviourStatePredicate0) {
            this.isViewBlocking = blockBehaviourStatePredicate0;
            return this;
        }

        public BlockBehaviour.Properties hasPostProcess(BlockBehaviour.StatePredicate blockBehaviourStatePredicate0) {
            this.hasPostProcess = blockBehaviourStatePredicate0;
            return this;
        }

        public BlockBehaviour.Properties emissiveRendering(BlockBehaviour.StatePredicate blockBehaviourStatePredicate0) {
            this.emissiveRendering = blockBehaviourStatePredicate0;
            return this;
        }

        public BlockBehaviour.Properties requiresCorrectToolForDrops() {
            this.requiresCorrectToolForDrops = true;
            return this;
        }

        public BlockBehaviour.Properties destroyTime(float float0) {
            this.destroyTime = float0;
            return this;
        }

        public BlockBehaviour.Properties explosionResistance(float float0) {
            this.explosionResistance = Math.max(0.0F, float0);
            return this;
        }

        public BlockBehaviour.Properties offsetType(BlockBehaviour.OffsetType blockBehaviourOffsetType0) {
            switch(blockBehaviourOffsetType0) {
                case XYZ:
                    this.offsetFunction = Optional.of((BlockBehaviour.OffsetFunction) (p_272562_, p_272563_, p_272564_) -> {
                        Block $$3 = p_272562_.m_60734_();
                        long $$4 = Mth.getSeed(p_272564_.m_123341_(), 0, p_272564_.m_123343_());
                        double $$5 = ((double) ((float) ($$4 >> 4 & 15L) / 15.0F) - 1.0) * (double) $$3.m_142627_();
                        float $$6 = $$3.m_142740_();
                        double $$7 = Mth.clamp(((double) ((float) ($$4 & 15L) / 15.0F) - 0.5) * 0.5, (double) (-$$6), (double) $$6);
                        double $$8 = Mth.clamp(((double) ((float) ($$4 >> 8 & 15L) / 15.0F) - 0.5) * 0.5, (double) (-$$6), (double) $$6);
                        return new Vec3($$7, $$5, $$8);
                    });
                    break;
                case XZ:
                    this.offsetFunction = Optional.of((BlockBehaviour.OffsetFunction) (p_272565_, p_272566_, p_272567_) -> {
                        Block $$3 = p_272565_.m_60734_();
                        long $$4 = Mth.getSeed(p_272567_.m_123341_(), 0, p_272567_.m_123343_());
                        float $$5 = $$3.m_142740_();
                        double $$6 = Mth.clamp(((double) ((float) ($$4 & 15L) / 15.0F) - 0.5) * 0.5, (double) (-$$5), (double) $$5);
                        double $$7 = Mth.clamp(((double) ((float) ($$4 >> 8 & 15L) / 15.0F) - 0.5) * 0.5, (double) (-$$5), (double) $$5);
                        return new Vec3($$6, 0.0, $$7);
                    });
                    break;
                default:
                    this.offsetFunction = Optional.empty();
            }
            return this;
        }

        public BlockBehaviour.Properties noParticlesOnBreak() {
            this.spawnParticlesOnBreak = false;
            return this;
        }

        public BlockBehaviour.Properties requiredFeatures(FeatureFlag... featureFlag0) {
            this.requiredFeatures = FeatureFlags.REGISTRY.subset(featureFlag0);
            return this;
        }

        public BlockBehaviour.Properties instrument(NoteBlockInstrument noteBlockInstrument0) {
            this.instrument = noteBlockInstrument0;
            return this;
        }

        public BlockBehaviour.Properties replaceable() {
            this.replaceable = true;
            return this;
        }
    }

    public interface StateArgumentPredicate<A> {

        boolean test(BlockState var1, BlockGetter var2, BlockPos var3, A var4);
    }

    public interface StatePredicate {

        boolean test(BlockState var1, BlockGetter var2, BlockPos var3);
    }
}