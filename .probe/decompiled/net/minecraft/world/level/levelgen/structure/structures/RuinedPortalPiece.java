package net.minecraft.world.level.levelgen.structure.structures;

import com.google.common.collect.Lists;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.templatesystem.AlwaysTrueTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlackstoneReplaceProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockAgeProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.LavaSubmergedBlockProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.ProcessorRule;
import net.minecraft.world.level.levelgen.structure.templatesystem.ProtectedBlockProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.RandomBlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.slf4j.Logger;

public class RuinedPortalPiece extends TemplateStructurePiece {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final float PROBABILITY_OF_GOLD_GONE = 0.3F;

    private static final float PROBABILITY_OF_MAGMA_INSTEAD_OF_NETHERRACK = 0.07F;

    private static final float PROBABILITY_OF_MAGMA_INSTEAD_OF_LAVA = 0.2F;

    private final RuinedPortalPiece.VerticalPlacement verticalPlacement;

    private final RuinedPortalPiece.Properties properties;

    public RuinedPortalPiece(StructureTemplateManager structureTemplateManager0, BlockPos blockPos1, RuinedPortalPiece.VerticalPlacement ruinedPortalPieceVerticalPlacement2, RuinedPortalPiece.Properties ruinedPortalPieceProperties3, ResourceLocation resourceLocation4, StructureTemplate structureTemplate5, Rotation rotation6, Mirror mirror7, BlockPos blockPos8) {
        super(StructurePieceType.RUINED_PORTAL, 0, structureTemplateManager0, resourceLocation4, resourceLocation4.toString(), makeSettings(mirror7, rotation6, ruinedPortalPieceVerticalPlacement2, blockPos8, ruinedPortalPieceProperties3), blockPos1);
        this.verticalPlacement = ruinedPortalPieceVerticalPlacement2;
        this.properties = ruinedPortalPieceProperties3;
    }

    public RuinedPortalPiece(StructureTemplateManager structureTemplateManager0, CompoundTag compoundTag1) {
        super(StructurePieceType.RUINED_PORTAL, compoundTag1, structureTemplateManager0, p_229188_ -> makeSettings(structureTemplateManager0, compoundTag1, p_229188_));
        this.verticalPlacement = RuinedPortalPiece.VerticalPlacement.byName(compoundTag1.getString("VerticalPlacement"));
        this.properties = (RuinedPortalPiece.Properties) RuinedPortalPiece.Properties.CODEC.parse(new Dynamic(NbtOps.INSTANCE, compoundTag1.get("Properties"))).getOrThrow(true, LOGGER::error);
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext structurePieceSerializationContext0, CompoundTag compoundTag1) {
        super.addAdditionalSaveData(structurePieceSerializationContext0, compoundTag1);
        compoundTag1.putString("Rotation", this.f_73657_.getRotation().name());
        compoundTag1.putString("Mirror", this.f_73657_.getMirror().name());
        compoundTag1.putString("VerticalPlacement", this.verticalPlacement.getName());
        RuinedPortalPiece.Properties.CODEC.encodeStart(NbtOps.INSTANCE, this.properties).resultOrPartial(LOGGER::error).ifPresent(p_229177_ -> compoundTag1.put("Properties", p_229177_));
    }

    private static StructurePlaceSettings makeSettings(StructureTemplateManager structureTemplateManager0, CompoundTag compoundTag1, ResourceLocation resourceLocation2) {
        StructureTemplate $$3 = structureTemplateManager0.getOrCreate(resourceLocation2);
        BlockPos $$4 = new BlockPos($$3.getSize().getX() / 2, 0, $$3.getSize().getZ() / 2);
        return makeSettings(Mirror.valueOf(compoundTag1.getString("Mirror")), Rotation.valueOf(compoundTag1.getString("Rotation")), RuinedPortalPiece.VerticalPlacement.byName(compoundTag1.getString("VerticalPlacement")), $$4, (RuinedPortalPiece.Properties) RuinedPortalPiece.Properties.CODEC.parse(new Dynamic(NbtOps.INSTANCE, compoundTag1.get("Properties"))).getOrThrow(true, LOGGER::error));
    }

    private static StructurePlaceSettings makeSettings(Mirror mirror0, Rotation rotation1, RuinedPortalPiece.VerticalPlacement ruinedPortalPieceVerticalPlacement2, BlockPos blockPos3, RuinedPortalPiece.Properties ruinedPortalPieceProperties4) {
        BlockIgnoreProcessor $$5 = ruinedPortalPieceProperties4.airPocket ? BlockIgnoreProcessor.STRUCTURE_BLOCK : BlockIgnoreProcessor.STRUCTURE_AND_AIR;
        List<ProcessorRule> $$6 = Lists.newArrayList();
        $$6.add(getBlockReplaceRule(Blocks.GOLD_BLOCK, 0.3F, Blocks.AIR));
        $$6.add(getLavaProcessorRule(ruinedPortalPieceVerticalPlacement2, ruinedPortalPieceProperties4));
        if (!ruinedPortalPieceProperties4.cold) {
            $$6.add(getBlockReplaceRule(Blocks.NETHERRACK, 0.07F, Blocks.MAGMA_BLOCK));
        }
        StructurePlaceSettings $$7 = new StructurePlaceSettings().setRotation(rotation1).setMirror(mirror0).setRotationPivot(blockPos3).addProcessor($$5).addProcessor(new RuleProcessor($$6)).addProcessor(new BlockAgeProcessor(ruinedPortalPieceProperties4.mossiness)).addProcessor(new ProtectedBlockProcessor(BlockTags.FEATURES_CANNOT_REPLACE)).addProcessor(new LavaSubmergedBlockProcessor());
        if (ruinedPortalPieceProperties4.replaceWithBlackstone) {
            $$7.addProcessor(BlackstoneReplaceProcessor.INSTANCE);
        }
        return $$7;
    }

    private static ProcessorRule getLavaProcessorRule(RuinedPortalPiece.VerticalPlacement ruinedPortalPieceVerticalPlacement0, RuinedPortalPiece.Properties ruinedPortalPieceProperties1) {
        if (ruinedPortalPieceVerticalPlacement0 == RuinedPortalPiece.VerticalPlacement.ON_OCEAN_FLOOR) {
            return getBlockReplaceRule(Blocks.LAVA, Blocks.MAGMA_BLOCK);
        } else {
            return ruinedPortalPieceProperties1.cold ? getBlockReplaceRule(Blocks.LAVA, Blocks.NETHERRACK) : getBlockReplaceRule(Blocks.LAVA, 0.2F, Blocks.MAGMA_BLOCK);
        }
    }

    @Override
    public void postProcess(WorldGenLevel worldGenLevel0, StructureManager structureManager1, ChunkGenerator chunkGenerator2, RandomSource randomSource3, BoundingBox boundingBox4, ChunkPos chunkPos5, BlockPos blockPos6) {
        BoundingBox $$7 = this.f_73656_.getBoundingBox(this.f_73657_, this.f_73658_);
        if (boundingBox4.isInside($$7.getCenter())) {
            boundingBox4.encapsulate($$7);
            super.postProcess(worldGenLevel0, structureManager1, chunkGenerator2, randomSource3, boundingBox4, chunkPos5, blockPos6);
            this.spreadNetherrack(randomSource3, worldGenLevel0);
            this.addNetherrackDripColumnsBelowPortal(randomSource3, worldGenLevel0);
            if (this.properties.vines || this.properties.overgrown) {
                BlockPos.betweenClosedStream(this.m_73547_()).forEach(p_229127_ -> {
                    if (this.properties.vines) {
                        this.maybeAddVines(randomSource3, worldGenLevel0, p_229127_);
                    }
                    if (this.properties.overgrown) {
                        this.maybeAddLeavesAbove(randomSource3, worldGenLevel0, p_229127_);
                    }
                });
            }
        }
    }

    @Override
    protected void handleDataMarker(String string0, BlockPos blockPos1, ServerLevelAccessor serverLevelAccessor2, RandomSource randomSource3, BoundingBox boundingBox4) {
    }

    private void maybeAddVines(RandomSource randomSource0, LevelAccessor levelAccessor1, BlockPos blockPos2) {
        BlockState $$3 = levelAccessor1.m_8055_(blockPos2);
        if (!$$3.m_60795_() && !$$3.m_60713_(Blocks.VINE)) {
            Direction $$4 = m_226760_(randomSource0);
            BlockPos $$5 = blockPos2.relative($$4);
            BlockState $$6 = levelAccessor1.m_8055_($$5);
            if ($$6.m_60795_()) {
                if (Block.isFaceFull($$3.m_60812_(levelAccessor1, blockPos2), $$4)) {
                    BooleanProperty $$7 = VineBlock.getPropertyForFace($$4.getOpposite());
                    levelAccessor1.m_7731_($$5, (BlockState) Blocks.VINE.defaultBlockState().m_61124_($$7, true), 3);
                }
            }
        }
    }

    private void maybeAddLeavesAbove(RandomSource randomSource0, LevelAccessor levelAccessor1, BlockPos blockPos2) {
        if (randomSource0.nextFloat() < 0.5F && levelAccessor1.m_8055_(blockPos2).m_60713_(Blocks.NETHERRACK) && levelAccessor1.m_8055_(blockPos2.above()).m_60795_()) {
            levelAccessor1.m_7731_(blockPos2.above(), (BlockState) Blocks.JUNGLE_LEAVES.defaultBlockState().m_61124_(LeavesBlock.PERSISTENT, true), 3);
        }
    }

    private void addNetherrackDripColumnsBelowPortal(RandomSource randomSource0, LevelAccessor levelAccessor1) {
        for (int $$2 = this.f_73383_.minX() + 1; $$2 < this.f_73383_.maxX(); $$2++) {
            for (int $$3 = this.f_73383_.minZ() + 1; $$3 < this.f_73383_.maxZ(); $$3++) {
                BlockPos $$4 = new BlockPos($$2, this.f_73383_.minY(), $$3);
                if (levelAccessor1.m_8055_($$4).m_60713_(Blocks.NETHERRACK)) {
                    this.addNetherrackDripColumn(randomSource0, levelAccessor1, $$4.below());
                }
            }
        }
    }

    private void addNetherrackDripColumn(RandomSource randomSource0, LevelAccessor levelAccessor1, BlockPos blockPos2) {
        BlockPos.MutableBlockPos $$3 = blockPos2.mutable();
        this.placeNetherrackOrMagma(randomSource0, levelAccessor1, $$3);
        int $$4 = 8;
        while ($$4 > 0 && randomSource0.nextFloat() < 0.5F) {
            $$3.move(Direction.DOWN);
            $$4--;
            this.placeNetherrackOrMagma(randomSource0, levelAccessor1, $$3);
        }
    }

    private void spreadNetherrack(RandomSource randomSource0, LevelAccessor levelAccessor1) {
        boolean $$2 = this.verticalPlacement == RuinedPortalPiece.VerticalPlacement.ON_LAND_SURFACE || this.verticalPlacement == RuinedPortalPiece.VerticalPlacement.ON_OCEAN_FLOOR;
        BlockPos $$3 = this.f_73383_.getCenter();
        int $$4 = $$3.m_123341_();
        int $$5 = $$3.m_123343_();
        float[] $$6 = new float[] { 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.9F, 0.9F, 0.8F, 0.7F, 0.6F, 0.4F, 0.2F };
        int $$7 = $$6.length;
        int $$8 = (this.f_73383_.getXSpan() + this.f_73383_.getZSpan()) / 2;
        int $$9 = randomSource0.nextInt(Math.max(1, 8 - $$8 / 2));
        int $$10 = 3;
        BlockPos.MutableBlockPos $$11 = BlockPos.ZERO.mutable();
        for (int $$12 = $$4 - $$7; $$12 <= $$4 + $$7; $$12++) {
            for (int $$13 = $$5 - $$7; $$13 <= $$5 + $$7; $$13++) {
                int $$14 = Math.abs($$12 - $$4) + Math.abs($$13 - $$5);
                int $$15 = Math.max(0, $$14 + $$9);
                if ($$15 < $$7) {
                    float $$16 = $$6[$$15];
                    if (randomSource0.nextDouble() < (double) $$16) {
                        int $$17 = getSurfaceY(levelAccessor1, $$12, $$13, this.verticalPlacement);
                        int $$18 = $$2 ? $$17 : Math.min(this.f_73383_.minY(), $$17);
                        $$11.set($$12, $$18, $$13);
                        if (Math.abs($$18 - this.f_73383_.minY()) <= 3 && this.canBlockBeReplacedByNetherrackOrMagma(levelAccessor1, $$11)) {
                            this.placeNetherrackOrMagma(randomSource0, levelAccessor1, $$11);
                            if (this.properties.overgrown) {
                                this.maybeAddLeavesAbove(randomSource0, levelAccessor1, $$11);
                            }
                            this.addNetherrackDripColumn(randomSource0, levelAccessor1, $$11.m_7495_());
                        }
                    }
                }
            }
        }
    }

    private boolean canBlockBeReplacedByNetherrackOrMagma(LevelAccessor levelAccessor0, BlockPos blockPos1) {
        BlockState $$2 = levelAccessor0.m_8055_(blockPos1);
        return !$$2.m_60713_(Blocks.AIR) && !$$2.m_60713_(Blocks.OBSIDIAN) && !$$2.m_204336_(BlockTags.FEATURES_CANNOT_REPLACE) && (this.verticalPlacement == RuinedPortalPiece.VerticalPlacement.IN_NETHER || !$$2.m_60713_(Blocks.LAVA));
    }

    private void placeNetherrackOrMagma(RandomSource randomSource0, LevelAccessor levelAccessor1, BlockPos blockPos2) {
        if (!this.properties.cold && randomSource0.nextFloat() < 0.07F) {
            levelAccessor1.m_7731_(blockPos2, Blocks.MAGMA_BLOCK.defaultBlockState(), 3);
        } else {
            levelAccessor1.m_7731_(blockPos2, Blocks.NETHERRACK.defaultBlockState(), 3);
        }
    }

    private static int getSurfaceY(LevelAccessor levelAccessor0, int int1, int int2, RuinedPortalPiece.VerticalPlacement ruinedPortalPieceVerticalPlacement3) {
        return levelAccessor0.m_6924_(getHeightMapType(ruinedPortalPieceVerticalPlacement3), int1, int2) - 1;
    }

    public static Heightmap.Types getHeightMapType(RuinedPortalPiece.VerticalPlacement ruinedPortalPieceVerticalPlacement0) {
        return ruinedPortalPieceVerticalPlacement0 == RuinedPortalPiece.VerticalPlacement.ON_OCEAN_FLOOR ? Heightmap.Types.OCEAN_FLOOR_WG : Heightmap.Types.WORLD_SURFACE_WG;
    }

    private static ProcessorRule getBlockReplaceRule(Block block0, float float1, Block block2) {
        return new ProcessorRule(new RandomBlockMatchTest(block0, float1), AlwaysTrueTest.INSTANCE, block2.defaultBlockState());
    }

    private static ProcessorRule getBlockReplaceRule(Block block0, Block block1) {
        return new ProcessorRule(new BlockMatchTest(block0), AlwaysTrueTest.INSTANCE, block1.defaultBlockState());
    }

    public static class Properties {

        public static final Codec<RuinedPortalPiece.Properties> CODEC = RecordCodecBuilder.create(p_229214_ -> p_229214_.group(Codec.BOOL.fieldOf("cold").forGetter(p_229226_ -> p_229226_.cold), Codec.FLOAT.fieldOf("mossiness").forGetter(p_229224_ -> p_229224_.mossiness), Codec.BOOL.fieldOf("air_pocket").forGetter(p_229222_ -> p_229222_.airPocket), Codec.BOOL.fieldOf("overgrown").forGetter(p_229220_ -> p_229220_.overgrown), Codec.BOOL.fieldOf("vines").forGetter(p_229218_ -> p_229218_.vines), Codec.BOOL.fieldOf("replace_with_blackstone").forGetter(p_229216_ -> p_229216_.replaceWithBlackstone)).apply(p_229214_, RuinedPortalPiece.Properties::new));

        public boolean cold;

        public float mossiness;

        public boolean airPocket;

        public boolean overgrown;

        public boolean vines;

        public boolean replaceWithBlackstone;

        public Properties() {
        }

        public Properties(boolean boolean0, float float1, boolean boolean2, boolean boolean3, boolean boolean4, boolean boolean5) {
            this.cold = boolean0;
            this.mossiness = float1;
            this.airPocket = boolean2;
            this.overgrown = boolean3;
            this.vines = boolean4;
            this.replaceWithBlackstone = boolean5;
        }
    }

    public static enum VerticalPlacement implements StringRepresentable {

        ON_LAND_SURFACE("on_land_surface"),
        PARTLY_BURIED("partly_buried"),
        ON_OCEAN_FLOOR("on_ocean_floor"),
        IN_MOUNTAIN("in_mountain"),
        UNDERGROUND("underground"),
        IN_NETHER("in_nether");

        public static final StringRepresentable.EnumCodec<RuinedPortalPiece.VerticalPlacement> CODEC = StringRepresentable.fromEnum(RuinedPortalPiece.VerticalPlacement::values);

        private final String name;

        private VerticalPlacement(String p_229240_) {
            this.name = p_229240_;
        }

        public String getName() {
            return this.name;
        }

        public static RuinedPortalPiece.VerticalPlacement byName(String p_229243_) {
            return (RuinedPortalPiece.VerticalPlacement) CODEC.byName(p_229243_);
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }
    }
}