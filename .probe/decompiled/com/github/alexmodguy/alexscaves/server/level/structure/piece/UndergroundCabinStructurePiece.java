package com.github.alexmodguy.alexscaves.server.level.structure.piece;

import com.github.alexmodguy.alexscaves.server.config.BiomeGenerationConfig;
import com.github.alexmodguy.alexscaves.server.level.biome.ACBiomeRegistry;
import com.github.alexmodguy.alexscaves.server.level.structure.processor.UndergroundCabinProcessor;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;

public class UndergroundCabinStructurePiece extends TemplateStructurePiece {

    private ResourceKey<Biome> pickedBiome = null;

    public UndergroundCabinStructurePiece(StructureTemplateManager manager, ResourceLocation resourceLocation, BlockPos pos, Rotation rotation) {
        super(ACStructurePieceRegistry.UNDERGROUND_CABIN.get(), 0, manager, resourceLocation, resourceLocation.toString(), makeSettings(rotation), pos);
    }

    public UndergroundCabinStructurePiece(StructureTemplateManager manager, CompoundTag tag) {
        super(ACStructurePieceRegistry.UNDERGROUND_CABIN.get(), tag, manager, x -> makeSettings(Rotation.valueOf(tag.getString("Rotation"))));
    }

    public UndergroundCabinStructurePiece(StructurePieceSerializationContext context, CompoundTag tag) {
        this(context.structureTemplateManager(), tag);
    }

    private static StructurePlaceSettings makeSettings(Rotation rotation) {
        return new StructurePlaceSettings().setRotation(rotation).setIgnoreEntities(true).setKeepLiquids(false);
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
        super.addAdditionalSaveData(context, tag);
        tag.putString("Rotation", this.f_73657_.getRotation().name());
    }

    @Override
    public void postProcess(WorldGenLevel worldGenLevel, StructureManager structureManager, ChunkGenerator chunkGenerator, RandomSource randomSource, BoundingBox boundingBox, ChunkPos chunkPos, BlockPos pos) {
        this.pickedBiome = this.pickBiome(randomSource);
        this.f_73657_.clearProcessors().addProcessor(new UndergroundCabinProcessor());
        BlockPos structureCenter = StructureTemplate.transform(new BlockPos(this.f_73656_.getSize().getX() / 2, 0, this.f_73656_.getSize().getZ() / 2), Mirror.NONE, this.f_73657_.getRotation(), BlockPos.ZERO).offset(this.f_73658_);
        BlockPos cavePos = this.getCaveHeight(structureCenter, worldGenLevel, randomSource);
        this.f_73658_ = new BlockPos(this.f_73658_.m_123341_(), cavePos.m_123342_(), this.f_73658_.m_123343_());
        BlockPos genPos = new BlockPos(pos.m_123341_(), cavePos.m_123342_(), pos.m_123343_());
        super.postProcess(worldGenLevel, structureManager, chunkGenerator, randomSource, boundingBox, chunkPos, genPos);
    }

    private ResourceKey<Biome> pickBiome(RandomSource randomSource) {
        int attempts = 0;
        List<ResourceKey<Biome>> biomeList = new ArrayList(ACBiomeRegistry.ALEXS_CAVES_BIOMES);
        biomeList.removeIf(BiomeGenerationConfig::isBiomeDisabledCompletely);
        return biomeList.isEmpty() ? null : Util.getRandom(biomeList, randomSource);
    }

    private BlockPos getCaveHeight(BlockPos currentStructureCenter, BlockGetter level, RandomSource randomSource) {
        List<BlockPos> genPos = new ArrayList();
        int j = 0;
        for (BlockPos.MutableBlockPos chunkCenter = new BlockPos.MutableBlockPos(currentStructureCenter.m_123341_(), level.m_141937_() + 3, currentStructureCenter.m_123343_()); chunkCenter.m_123342_() < currentStructureCenter.m_123342_(); j++) {
            BlockState currentState = level.getBlockState(chunkCenter);
            chunkCenter.move(0, 1, 0);
            BlockState nextState = level.getBlockState(chunkCenter);
            if (!this.canReplace(currentState, j) && this.canReplace(nextState, j + 1)) {
                genPos.add(chunkCenter.immutable().below());
            }
        }
        if (genPos.isEmpty()) {
            return currentStructureCenter;
        } else {
            return genPos.size() <= 1 ? (BlockPos) genPos.get(0) : (BlockPos) genPos.get(randomSource.nextInt(genPos.size() - 1));
        }
    }

    protected boolean canReplace(BlockState state, int already) {
        return state.m_60795_() || state.m_247087_();
    }

    @Override
    protected void handleDataMarker(String string, BlockPos pos, ServerLevelAccessor accessor, RandomSource random, BoundingBox box) {
        accessor.m_7731_(pos, Blocks.CAVE_AIR.defaultBlockState(), 0);
        byte var7 = -1;
        switch(string.hashCode()) {
            case 56455850:
                if (string.equals("loot_chest")) {
                    var7 = 0;
                }
            default:
                switch(var7) {
                    case 0:
                        ResourceLocation chestLoot = this.pickedBiome == null ? BuiltInLootTables.SIMPLE_DUNGEON : new ResourceLocation("alexscaves:chests/underground_cabin_" + this.pickedBiome.location().getPath());
                        RandomizableContainerBlockEntity.setLootTable(accessor, random, pos.below(), chestLoot);
                }
        }
    }
}