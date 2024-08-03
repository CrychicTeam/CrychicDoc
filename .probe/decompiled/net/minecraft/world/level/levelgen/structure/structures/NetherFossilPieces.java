package net.minecraft.world.level.levelgen.structure.structures;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

public class NetherFossilPieces {

    private static final ResourceLocation[] FOSSILS = new ResourceLocation[] { new ResourceLocation("nether_fossils/fossil_1"), new ResourceLocation("nether_fossils/fossil_2"), new ResourceLocation("nether_fossils/fossil_3"), new ResourceLocation("nether_fossils/fossil_4"), new ResourceLocation("nether_fossils/fossil_5"), new ResourceLocation("nether_fossils/fossil_6"), new ResourceLocation("nether_fossils/fossil_7"), new ResourceLocation("nether_fossils/fossil_8"), new ResourceLocation("nether_fossils/fossil_9"), new ResourceLocation("nether_fossils/fossil_10"), new ResourceLocation("nether_fossils/fossil_11"), new ResourceLocation("nether_fossils/fossil_12"), new ResourceLocation("nether_fossils/fossil_13"), new ResourceLocation("nether_fossils/fossil_14") };

    public static void addPieces(StructureTemplateManager structureTemplateManager0, StructurePieceAccessor structurePieceAccessor1, RandomSource randomSource2, BlockPos blockPos3) {
        Rotation $$4 = Rotation.getRandom(randomSource2);
        structurePieceAccessor1.addPiece(new NetherFossilPieces.NetherFossilPiece(structureTemplateManager0, Util.getRandom(FOSSILS, randomSource2), blockPos3, $$4));
    }

    public static class NetherFossilPiece extends TemplateStructurePiece {

        public NetherFossilPiece(StructureTemplateManager structureTemplateManager0, ResourceLocation resourceLocation1, BlockPos blockPos2, Rotation rotation3) {
            super(StructurePieceType.NETHER_FOSSIL, 0, structureTemplateManager0, resourceLocation1, resourceLocation1.toString(), makeSettings(rotation3), blockPos2);
        }

        public NetherFossilPiece(StructureTemplateManager structureTemplateManager0, CompoundTag compoundTag1) {
            super(StructurePieceType.NETHER_FOSSIL, compoundTag1, structureTemplateManager0, p_228568_ -> makeSettings(Rotation.valueOf(compoundTag1.getString("Rot"))));
        }

        private static StructurePlaceSettings makeSettings(Rotation rotation0) {
            return new StructurePlaceSettings().setRotation(rotation0).setMirror(Mirror.NONE).addProcessor(BlockIgnoreProcessor.STRUCTURE_AND_AIR);
        }

        @Override
        protected void addAdditionalSaveData(StructurePieceSerializationContext structurePieceSerializationContext0, CompoundTag compoundTag1) {
            super.addAdditionalSaveData(structurePieceSerializationContext0, compoundTag1);
            compoundTag1.putString("Rot", this.f_73657_.getRotation().name());
        }

        @Override
        protected void handleDataMarker(String string0, BlockPos blockPos1, ServerLevelAccessor serverLevelAccessor2, RandomSource randomSource3, BoundingBox boundingBox4) {
        }

        @Override
        public void postProcess(WorldGenLevel worldGenLevel0, StructureManager structureManager1, ChunkGenerator chunkGenerator2, RandomSource randomSource3, BoundingBox boundingBox4, ChunkPos chunkPos5, BlockPos blockPos6) {
            boundingBox4.encapsulate(this.f_73656_.getBoundingBox(this.f_73657_, this.f_73658_));
            super.postProcess(worldGenLevel0, structureManager1, chunkGenerator2, randomSource3, boundingBox4, chunkPos5, blockPos6);
        }
    }
}