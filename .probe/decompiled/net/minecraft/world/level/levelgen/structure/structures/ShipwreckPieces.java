package net.minecraft.world.level.levelgen.structure.structures;

import java.util.Map;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;

public class ShipwreckPieces {

    static final BlockPos PIVOT = new BlockPos(4, 0, 15);

    private static final ResourceLocation[] STRUCTURE_LOCATION_BEACHED = new ResourceLocation[] { new ResourceLocation("shipwreck/with_mast"), new ResourceLocation("shipwreck/sideways_full"), new ResourceLocation("shipwreck/sideways_fronthalf"), new ResourceLocation("shipwreck/sideways_backhalf"), new ResourceLocation("shipwreck/rightsideup_full"), new ResourceLocation("shipwreck/rightsideup_fronthalf"), new ResourceLocation("shipwreck/rightsideup_backhalf"), new ResourceLocation("shipwreck/with_mast_degraded"), new ResourceLocation("shipwreck/rightsideup_full_degraded"), new ResourceLocation("shipwreck/rightsideup_fronthalf_degraded"), new ResourceLocation("shipwreck/rightsideup_backhalf_degraded") };

    private static final ResourceLocation[] STRUCTURE_LOCATION_OCEAN = new ResourceLocation[] { new ResourceLocation("shipwreck/with_mast"), new ResourceLocation("shipwreck/upsidedown_full"), new ResourceLocation("shipwreck/upsidedown_fronthalf"), new ResourceLocation("shipwreck/upsidedown_backhalf"), new ResourceLocation("shipwreck/sideways_full"), new ResourceLocation("shipwreck/sideways_fronthalf"), new ResourceLocation("shipwreck/sideways_backhalf"), new ResourceLocation("shipwreck/rightsideup_full"), new ResourceLocation("shipwreck/rightsideup_fronthalf"), new ResourceLocation("shipwreck/rightsideup_backhalf"), new ResourceLocation("shipwreck/with_mast_degraded"), new ResourceLocation("shipwreck/upsidedown_full_degraded"), new ResourceLocation("shipwreck/upsidedown_fronthalf_degraded"), new ResourceLocation("shipwreck/upsidedown_backhalf_degraded"), new ResourceLocation("shipwreck/sideways_full_degraded"), new ResourceLocation("shipwreck/sideways_fronthalf_degraded"), new ResourceLocation("shipwreck/sideways_backhalf_degraded"), new ResourceLocation("shipwreck/rightsideup_full_degraded"), new ResourceLocation("shipwreck/rightsideup_fronthalf_degraded"), new ResourceLocation("shipwreck/rightsideup_backhalf_degraded") };

    static final Map<String, ResourceLocation> MARKERS_TO_LOOT = Map.of("map_chest", BuiltInLootTables.SHIPWRECK_MAP, "treasure_chest", BuiltInLootTables.SHIPWRECK_TREASURE, "supply_chest", BuiltInLootTables.SHIPWRECK_SUPPLY);

    public static void addPieces(StructureTemplateManager structureTemplateManager0, BlockPos blockPos1, Rotation rotation2, StructurePieceAccessor structurePieceAccessor3, RandomSource randomSource4, boolean boolean5) {
        ResourceLocation $$6 = Util.getRandom(boolean5 ? STRUCTURE_LOCATION_BEACHED : STRUCTURE_LOCATION_OCEAN, randomSource4);
        structurePieceAccessor3.addPiece(new ShipwreckPieces.ShipwreckPiece(structureTemplateManager0, $$6, blockPos1, rotation2, boolean5));
    }

    public static class ShipwreckPiece extends TemplateStructurePiece {

        private final boolean isBeached;

        public ShipwreckPiece(StructureTemplateManager structureTemplateManager0, ResourceLocation resourceLocation1, BlockPos blockPos2, Rotation rotation3, boolean boolean4) {
            super(StructurePieceType.SHIPWRECK_PIECE, 0, structureTemplateManager0, resourceLocation1, resourceLocation1.toString(), makeSettings(rotation3), blockPos2);
            this.isBeached = boolean4;
        }

        public ShipwreckPiece(StructureTemplateManager structureTemplateManager0, CompoundTag compoundTag1) {
            super(StructurePieceType.SHIPWRECK_PIECE, compoundTag1, structureTemplateManager0, p_229383_ -> makeSettings(Rotation.valueOf(compoundTag1.getString("Rot"))));
            this.isBeached = compoundTag1.getBoolean("isBeached");
        }

        @Override
        protected void addAdditionalSaveData(StructurePieceSerializationContext structurePieceSerializationContext0, CompoundTag compoundTag1) {
            super.addAdditionalSaveData(structurePieceSerializationContext0, compoundTag1);
            compoundTag1.putBoolean("isBeached", this.isBeached);
            compoundTag1.putString("Rot", this.f_73657_.getRotation().name());
        }

        private static StructurePlaceSettings makeSettings(Rotation rotation0) {
            return new StructurePlaceSettings().setRotation(rotation0).setMirror(Mirror.NONE).setRotationPivot(ShipwreckPieces.PIVOT).addProcessor(BlockIgnoreProcessor.STRUCTURE_AND_AIR);
        }

        @Override
        protected void handleDataMarker(String string0, BlockPos blockPos1, ServerLevelAccessor serverLevelAccessor2, RandomSource randomSource3, BoundingBox boundingBox4) {
            ResourceLocation $$5 = (ResourceLocation) ShipwreckPieces.MARKERS_TO_LOOT.get(string0);
            if ($$5 != null) {
                RandomizableContainerBlockEntity.setLootTable(serverLevelAccessor2, randomSource3, blockPos1.below(), $$5);
            }
        }

        @Override
        public void postProcess(WorldGenLevel worldGenLevel0, StructureManager structureManager1, ChunkGenerator chunkGenerator2, RandomSource randomSource3, BoundingBox boundingBox4, ChunkPos chunkPos5, BlockPos blockPos6) {
            int $$7 = worldGenLevel0.m_151558_();
            int $$8 = 0;
            Vec3i $$9 = this.f_73656_.getSize();
            Heightmap.Types $$10 = this.isBeached ? Heightmap.Types.WORLD_SURFACE_WG : Heightmap.Types.OCEAN_FLOOR_WG;
            int $$11 = $$9.getX() * $$9.getZ();
            if ($$11 == 0) {
                $$8 = worldGenLevel0.m_6924_($$10, this.f_73658_.m_123341_(), this.f_73658_.m_123343_());
            } else {
                BlockPos $$12 = this.f_73658_.offset($$9.getX() - 1, 0, $$9.getZ() - 1);
                for (BlockPos $$13 : BlockPos.betweenClosed(this.f_73658_, $$12)) {
                    int $$14 = worldGenLevel0.m_6924_($$10, $$13.m_123341_(), $$13.m_123343_());
                    $$8 += $$14;
                    $$7 = Math.min($$7, $$14);
                }
                $$8 /= $$11;
            }
            int $$15 = this.isBeached ? $$7 - $$9.getY() / 2 - randomSource3.nextInt(3) : $$8;
            this.f_73658_ = new BlockPos(this.f_73658_.m_123341_(), $$15, this.f_73658_.m_123343_());
            super.postProcess(worldGenLevel0, structureManager1, chunkGenerator2, randomSource3, boundingBox4, chunkPos5, blockPos6);
        }
    }
}