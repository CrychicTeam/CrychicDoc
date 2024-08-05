package com.github.alexthe666.iceandfire.world.gen.processor;

import com.github.alexthe666.iceandfire.world.IafProcessors;
import com.mojang.serialization.Codec;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.NotNull;

public class VillageHouseProcessor extends StructureProcessor {

    public static final ResourceLocation LOOT = new ResourceLocation("iceandfire", "chest/village_scribe");

    public static final VillageHouseProcessor INSTANCE = new VillageHouseProcessor();

    public static final Codec<VillageHouseProcessor> CODEC = Codec.unit(() -> INSTANCE);

    public StructureTemplate.StructureBlockInfo process(@NotNull LevelReader worldReader, @NotNull BlockPos pos, @NotNull BlockPos pos2, @NotNull StructureTemplate.StructureBlockInfo infoIn1, StructureTemplate.StructureBlockInfo infoIn2, StructurePlaceSettings settings, @Nullable StructureTemplate template) {
        RandomSource random = settings.getRandom(infoIn2.pos());
        if (infoIn2.state().m_60734_() == Blocks.CHEST) {
            CompoundTag tag = new CompoundTag();
            tag.putString("LootTable", LOOT.toString());
            tag.putLong("LootTableSeed", random.nextLong());
            return new StructureTemplate.StructureBlockInfo(infoIn2.pos(), infoIn2.state(), tag);
        } else {
            return infoIn2;
        }
    }

    @NotNull
    @Override
    protected StructureProcessorType getType() {
        return IafProcessors.VILLAGEHOUSEPROCESSOR.get();
    }
}