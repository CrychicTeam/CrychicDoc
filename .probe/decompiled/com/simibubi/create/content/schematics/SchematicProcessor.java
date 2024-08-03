package com.simibubi.create.content.schematics;

import com.mojang.serialization.Codec;
import com.simibubi.create.AllStructureProcessorTypes;
import com.simibubi.create.foundation.utility.NBTProcessors;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public class SchematicProcessor extends StructureProcessor {

    public static final SchematicProcessor INSTANCE = new SchematicProcessor();

    public static final Codec<SchematicProcessor> CODEC = Codec.unit(() -> INSTANCE);

    @Nullable
    public StructureTemplate.StructureBlockInfo process(LevelReader world, BlockPos pos, BlockPos anotherPos, StructureTemplate.StructureBlockInfo rawInfo, StructureTemplate.StructureBlockInfo info, StructurePlaceSettings settings, @Nullable StructureTemplate template) {
        if (info.nbt() != null && info.state().m_155947_()) {
            BlockEntity be = ((EntityBlock) info.state().m_60734_()).newBlockEntity(info.pos(), info.state());
            if (be != null) {
                CompoundTag nbt = NBTProcessors.process(be, info.nbt(), false);
                if (nbt != info.nbt()) {
                    return new StructureTemplate.StructureBlockInfo(info.pos(), info.state(), nbt);
                }
            }
        }
        return info;
    }

    @Nullable
    public StructureTemplate.StructureEntityInfo processEntity(LevelReader world, BlockPos pos, StructureTemplate.StructureEntityInfo rawInfo, StructureTemplate.StructureEntityInfo info, StructurePlaceSettings settings, StructureTemplate template) {
        return (StructureTemplate.StructureEntityInfo) EntityType.by(info.nbt).flatMap(type -> {
            if (world instanceof Level) {
                Entity e = type.create((Level) world);
                if (e != null && !e.onlyOpCanSetNbt()) {
                    return Optional.of(info);
                }
            }
            return Optional.empty();
        }).orElse(null);
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return AllStructureProcessorTypes.SCHEMATIC.get();
    }
}