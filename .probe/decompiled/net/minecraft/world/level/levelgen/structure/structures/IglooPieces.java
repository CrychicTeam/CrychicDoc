package net.minecraft.world.level.levelgen.structure.structures;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;

public class IglooPieces {

    public static final int GENERATION_HEIGHT = 90;

    static final ResourceLocation STRUCTURE_LOCATION_IGLOO = new ResourceLocation("igloo/top");

    private static final ResourceLocation STRUCTURE_LOCATION_LADDER = new ResourceLocation("igloo/middle");

    private static final ResourceLocation STRUCTURE_LOCATION_LABORATORY = new ResourceLocation("igloo/bottom");

    static final Map<ResourceLocation, BlockPos> PIVOTS = ImmutableMap.of(STRUCTURE_LOCATION_IGLOO, new BlockPos(3, 5, 5), STRUCTURE_LOCATION_LADDER, new BlockPos(1, 3, 1), STRUCTURE_LOCATION_LABORATORY, new BlockPos(3, 6, 7));

    static final Map<ResourceLocation, BlockPos> OFFSETS = ImmutableMap.of(STRUCTURE_LOCATION_IGLOO, BlockPos.ZERO, STRUCTURE_LOCATION_LADDER, new BlockPos(2, -3, 4), STRUCTURE_LOCATION_LABORATORY, new BlockPos(0, -3, -2));

    public static void addPieces(StructureTemplateManager structureTemplateManager0, BlockPos blockPos1, Rotation rotation2, StructurePieceAccessor structurePieceAccessor3, RandomSource randomSource4) {
        if (randomSource4.nextDouble() < 0.5) {
            int $$5 = randomSource4.nextInt(8) + 4;
            structurePieceAccessor3.addPiece(new IglooPieces.IglooPiece(structureTemplateManager0, STRUCTURE_LOCATION_LABORATORY, blockPos1, rotation2, $$5 * 3));
            for (int $$6 = 0; $$6 < $$5 - 1; $$6++) {
                structurePieceAccessor3.addPiece(new IglooPieces.IglooPiece(structureTemplateManager0, STRUCTURE_LOCATION_LADDER, blockPos1, rotation2, $$6 * 3));
            }
        }
        structurePieceAccessor3.addPiece(new IglooPieces.IglooPiece(structureTemplateManager0, STRUCTURE_LOCATION_IGLOO, blockPos1, rotation2, 0));
    }

    public static class IglooPiece extends TemplateStructurePiece {

        public IglooPiece(StructureTemplateManager structureTemplateManager0, ResourceLocation resourceLocation1, BlockPos blockPos2, Rotation rotation3, int int4) {
            super(StructurePieceType.IGLOO, 0, structureTemplateManager0, resourceLocation1, resourceLocation1.toString(), makeSettings(rotation3, resourceLocation1), makePosition(resourceLocation1, blockPos2, int4));
        }

        public IglooPiece(StructureTemplateManager structureTemplateManager0, CompoundTag compoundTag1) {
            super(StructurePieceType.IGLOO, compoundTag1, structureTemplateManager0, p_227589_ -> makeSettings(Rotation.valueOf(compoundTag1.getString("Rot")), p_227589_));
        }

        private static StructurePlaceSettings makeSettings(Rotation rotation0, ResourceLocation resourceLocation1) {
            return new StructurePlaceSettings().setRotation(rotation0).setMirror(Mirror.NONE).setRotationPivot((BlockPos) IglooPieces.PIVOTS.get(resourceLocation1)).addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
        }

        private static BlockPos makePosition(ResourceLocation resourceLocation0, BlockPos blockPos1, int int2) {
            return blockPos1.offset((Vec3i) IglooPieces.OFFSETS.get(resourceLocation0)).below(int2);
        }

        @Override
        protected void addAdditionalSaveData(StructurePieceSerializationContext structurePieceSerializationContext0, CompoundTag compoundTag1) {
            super.addAdditionalSaveData(structurePieceSerializationContext0, compoundTag1);
            compoundTag1.putString("Rot", this.f_73657_.getRotation().name());
        }

        @Override
        protected void handleDataMarker(String string0, BlockPos blockPos1, ServerLevelAccessor serverLevelAccessor2, RandomSource randomSource3, BoundingBox boundingBox4) {
            if ("chest".equals(string0)) {
                serverLevelAccessor2.m_7731_(blockPos1, Blocks.AIR.defaultBlockState(), 3);
                BlockEntity $$5 = serverLevelAccessor2.m_7702_(blockPos1.below());
                if ($$5 instanceof ChestBlockEntity) {
                    ((ChestBlockEntity) $$5).m_59626_(BuiltInLootTables.IGLOO_CHEST, randomSource3.nextLong());
                }
            }
        }

        @Override
        public void postProcess(WorldGenLevel worldGenLevel0, StructureManager structureManager1, ChunkGenerator chunkGenerator2, RandomSource randomSource3, BoundingBox boundingBox4, ChunkPos chunkPos5, BlockPos blockPos6) {
            ResourceLocation $$7 = new ResourceLocation(this.f_163658_);
            StructurePlaceSettings $$8 = makeSettings(this.f_73657_.getRotation(), $$7);
            BlockPos $$9 = (BlockPos) IglooPieces.OFFSETS.get($$7);
            BlockPos $$10 = this.f_73658_.offset(StructureTemplate.calculateRelativePosition($$8, new BlockPos(3 - $$9.m_123341_(), 0, -$$9.m_123343_())));
            int $$11 = worldGenLevel0.m_6924_(Heightmap.Types.WORLD_SURFACE_WG, $$10.m_123341_(), $$10.m_123343_());
            BlockPos $$12 = this.f_73658_;
            this.f_73658_ = this.f_73658_.offset(0, $$11 - 90 - 1, 0);
            super.postProcess(worldGenLevel0, structureManager1, chunkGenerator2, randomSource3, boundingBox4, chunkPos5, blockPos6);
            if ($$7.equals(IglooPieces.STRUCTURE_LOCATION_IGLOO)) {
                BlockPos $$13 = this.f_73658_.offset(StructureTemplate.calculateRelativePosition($$8, new BlockPos(3, 0, 5)));
                BlockState $$14 = worldGenLevel0.m_8055_($$13.below());
                if (!$$14.m_60795_() && !$$14.m_60713_(Blocks.LADDER)) {
                    worldGenLevel0.m_7731_($$13, Blocks.SNOW_BLOCK.defaultBlockState(), 3);
                }
            }
            this.f_73658_ = $$12;
        }
    }
}