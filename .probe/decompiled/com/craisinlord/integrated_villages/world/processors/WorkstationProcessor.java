package com.craisinlord.integrated_villages.world.processors;

import com.craisinlord.integrated_api.utils.PlatformHooks;
import com.craisinlord.integrated_villages.IntegratedVilagesProcessors;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.ArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public class WorkstationProcessor extends StructureProcessor {

    public static final Codec<WorkstationProcessor> CODEC = RecordCodecBuilder.create(instance -> instance.group(BuiltInRegistries.BLOCK.m_194605_().fieldOf("input_block").forGetter(config -> config.inputBlock), Codec.STRING.fieldOf("workstation_type").forGetter(config -> config.workstationType), Codec.BOOL.fieldOf("enable_integration").orElse(Boolean.TRUE).forGetter(config -> config.enableIntegration)).apply(instance, instance.stable(WorkstationProcessor::new)));

    private final Block inputBlock;

    private final String workstationType;

    private ArrayList<String> outputBlocksString = new ArrayList();

    private boolean enableIntegration;

    public WorkstationProcessor(Block inputBlock, String workstationType, boolean enableIntegration) {
        this.inputBlock = inputBlock;
        this.workstationType = workstationType;
        this.enableIntegration = enableIntegration;
    }

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader worldView, BlockPos pos, BlockPos blockPos, StructureTemplate.StructureBlockInfo structureBlockInfoLocal, StructureTemplate.StructureBlockInfo structureBlockInfoWorld, StructurePlaceSettings structurePlacementData) {
        if (structureBlockInfoWorld.state().m_60734_() != this.inputBlock) {
            return structureBlockInfoWorld;
        } else {
            RandomSource random = RandomSource.create();
            if (this.workstationType.equals("generic")) {
                this.outputBlocksString.add("minecraft:composter");
                if (this.enableIntegration) {
                    if (PlatformHooks.isModLoaded("domestic_innovation")) {
                        this.outputBlocksString.add("domestic_innovation:pet_bed_white");
                    }
                    if (PlatformHooks.isModLoaded("simplycats")) {
                        this.outputBlocksString.add("simplycats:shelter_book");
                    }
                    if (PlatformHooks.isModLoaded("morevillagers")) {
                        this.outputBlocksString.add("morevillagers:woodworking_table");
                        this.outputBlocksString.add("morevillagers:gardening_table");
                    }
                    if (PlatformHooks.isModLoaded("sawmill")) {
                        this.outputBlocksString.add("sawmill:sawmill");
                    }
                    if (PlatformHooks.isModLoaded("brewery")) {
                        this.outputBlocksString.add("brewery:bar_counter");
                    }
                    if (PlatformHooks.isModLoaded("cloudstorage")) {
                        this.outputBlocksString.add("cloudstorage:balloon_stand");
                    }
                    if (PlatformHooks.isModLoaded("betterarcheology")) {
                        this.outputBlocksString.add("betterarcheology:archeology_table");
                    }
                    if (PlatformHooks.isModLoaded("new_villagers")) {
                        this.outputBlocksString.add("new_villagers:archeology_table");
                        this.outputBlocksString.add("new_villagers:lumberjack_woodwork_table");
                    }
                    if (PlatformHooks.isModLoaded("recruits")) {
                        this.outputBlocksString.add("recruits:recruit_block");
                        this.outputBlocksString.add("recruits:recruit_shield_block");
                    }
                    if (PlatformHooks.isModLoaded("wares")) {
                        this.outputBlocksString.add("wares:delivery_table");
                    }
                    if (this.outputBlocksString.size() >= 2) {
                        this.outputBlocksString.remove("minecraft:composter");
                    }
                }
            } else if (this.workstationType.equals("shepard")) {
                this.outputBlocksString.add("minecraft:loom");
            } else if (this.workstationType.equals("butcher")) {
                this.outputBlocksString.add("minecraft:smoker");
            } else if (this.workstationType.equals("mason")) {
                this.outputBlocksString.add("minecraft:stonecutter");
            } else if (this.workstationType.equals("cleric")) {
                this.outputBlocksString.add("minecraft:brewing_stand");
                if (this.enableIntegration) {
                    if (PlatformHooks.isModLoaded("morevillagers")) {
                        this.outputBlocksString.add("morevillagers:blueprint_table");
                    }
                    if (PlatformHooks.isModLoaded("villagersplus")) {
                        this.outputBlocksString.add("villagersplus:occultist_table");
                        this.outputBlocksString.add("villagersplus:alchemist_table");
                    }
                    if (PlatformHooks.isModLoaded("mna")) {
                        this.outputBlocksString.add("mna:runescribing_table");
                        this.outputBlocksString.add("mna:manaweaving_altar");
                        this.outputBlocksString.add("mna:runeforge");
                    }
                }
            } else if (this.workstationType.equals("cartographer")) {
                this.outputBlocksString.add("minecraft:cartography_table");
                if (this.enableIntegration && PlatformHooks.isModLoaded("morevillagers")) {
                    this.outputBlocksString.add("morevillagers:hunting_post");
                    this.outputBlocksString.add("morevillagers:oceanography_table");
                }
            } else if (this.workstationType.equals("blacksmith")) {
                this.outputBlocksString.add("minecraft:smithing_table");
                this.outputBlocksString.add("minecraft:grindstone");
                this.outputBlocksString.add("minecraft:blast_furnace");
                if (this.enableIntegration) {
                    if (PlatformHooks.isModLoaded("morevillagers")) {
                        this.outputBlocksString.add("morevillagers:mining_bench");
                        this.outputBlocksString.add("morevillagers:blueprint_table");
                    }
                    if (PlatformHooks.isModLoaded("new_villagers")) {
                        this.outputBlocksString.add("new_villagers:graze_bench");
                    }
                    if (PlatformHooks.isModLoaded("immersiveengineering")) {
                        this.outputBlocksString.add("immersiveengineering:craftingtable");
                    }
                }
            } else if (this.workstationType.equals("librarian")) {
                this.outputBlocksString.add("minecraft:lectern");
                if (this.enableIntegration && PlatformHooks.isModLoaded("iceandfire")) {
                    this.outputBlocksString.add("iceandfire:lectern");
                }
            } else if (this.workstationType.equals("fletcher")) {
                this.outputBlocksString.add("minecraft:fletching_table");
                if (PlatformHooks.isModLoaded("recruits")) {
                    this.outputBlocksString.add("recruits:bowman_block");
                }
            } else if (this.workstationType.equals("leatherworker")) {
                this.outputBlocksString.add("minecraft:cauldron");
            } else if (this.workstationType.equals("beekeeper")) {
                this.outputBlocksString.add("minecraft:beehive");
                if (this.enableIntegration && PlatformHooks.isModLoaded("new_villagers")) {
                    this.outputBlocksString.add("new_villagers:bee_station");
                }
            } else {
                try {
                    throw new Exception("Integrated Villages Error: Found invalid workstation type: " + this.workstationType);
                } catch (Exception var10) {
                    var10.printStackTrace();
                }
            }
            String newBlockString = (String) this.outputBlocksString.get(random.nextInt(this.outputBlocksString.size()));
            BlockState newBlockState = BuiltInRegistries.BLOCK.get(new ResourceLocation(newBlockString)).defaultBlockState();
            return new StructureTemplate.StructureBlockInfo(structureBlockInfoWorld.pos(), newBlockState, structureBlockInfoWorld.nbt());
        }
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return IntegratedVilagesProcessors.WORKSTATION_PROCESSOR.get();
    }
}