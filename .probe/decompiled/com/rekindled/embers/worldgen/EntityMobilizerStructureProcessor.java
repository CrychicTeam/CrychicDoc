package com.rekindled.embers.worldgen;

import com.mojang.serialization.Codec;
import com.rekindled.embers.RegistryManager;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public class EntityMobilizerStructureProcessor extends StructureProcessor {

    public static final Codec<EntityMobilizerStructureProcessor> CODEC = Codec.unit(() -> EntityMobilizerStructureProcessor.INSTANCE);

    public static final EntityMobilizerStructureProcessor INSTANCE = new EntityMobilizerStructureProcessor();

    public StructureTemplate.StructureEntityInfo processEntity(LevelReader world, BlockPos seedPos, StructureTemplate.StructureEntityInfo rawEntityInfo, StructureTemplate.StructureEntityInfo entityInfo, StructurePlaceSettings placementSettings, StructureTemplate template) {
        CompoundTag nbt = entityInfo.nbt;
        if (nbt != null) {
            nbt = nbt.copy();
            nbt.putBoolean("NoAI", false);
            nbt.putBoolean("PersistenceRequired", true);
        }
        return new StructureTemplate.StructureEntityInfo(entityInfo.pos, entityInfo.blockPos, nbt);
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return RegistryManager.ENTITY_MOBILIZER_PROCESSOR.get();
    }
}