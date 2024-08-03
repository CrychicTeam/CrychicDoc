package com.sihenzhang.crockpot.data;

import com.sihenzhang.crockpot.block.AbstractCrockPotCropBlock;
import com.sihenzhang.crockpot.block.CornBlock;
import com.sihenzhang.crockpot.block.CrockPotBlock;
import com.sihenzhang.crockpot.block.CrockPotBlocks;
import com.sihenzhang.crockpot.block.food.AbstractStackableFoodBlock;
import com.sihenzhang.crockpot.util.RLUtils;
import java.util.List;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

public class CrockPotBlockStateProvider extends BlockStateProvider {

    public CrockPotBlockStateProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, "crockpot", existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        this.crockPotBlock(CrockPotBlocks.CROCK_POT.get());
        this.crockPotBlock(CrockPotBlocks.PORTABLE_CROCK_POT.get());
        this.simpleBlock(CrockPotBlocks.UNKNOWN_CROPS.get(), this.models().crop("unknown_crops", RLUtils.createRL("block/unknown_crops")).renderType(RLUtils.createVanillaRL("cutout")));
        this.customStageCropBlock(CrockPotBlocks.ASPARAGUS.get(), AbstractCrockPotCropBlock.f_52244_, List.of(0, 0, 1, 1, 2, 2, 2, 3));
        this.customStageCropBlock(CrockPotBlocks.CORN.get(), CornBlock.f_52244_, List.of());
        this.customStageCropBlock(CrockPotBlocks.EGGPLANT.get(), AbstractCrockPotCropBlock.f_52244_, List.of(0, 0, 1, 1, 2, 2, 2, 3));
        this.customStageCropBlock(CrockPotBlocks.GARLIC.get(), AbstractCrockPotCropBlock.f_52244_, List.of(0, 0, 1, 1, 2, 2, 2, 3));
        this.customStageCropBlock(CrockPotBlocks.ONION.get(), AbstractCrockPotCropBlock.f_52244_, List.of(0, 0, 1, 1, 2, 2, 2, 3));
        this.customStageCropBlock(CrockPotBlocks.PEPPER.get(), AbstractCrockPotCropBlock.f_52244_, List.of(0, 0, 1, 1, 2, 2, 2, 3));
        this.customStageCrossBlock(CrockPotBlocks.TOMATO.get(), AbstractCrockPotCropBlock.f_52244_, List.of(0, 0, 1, 1, 2, 2, 2, 3));
        ((List) CrockPotBlocks.FOODS.get()).forEach(this::foodBlock);
    }

    public void crockPotBlock(Block block) {
        String blockName = getBlockName(block);
        this.getVariantBuilder(block).forAllStates(state -> {
            StringBuilder sb = new StringBuilder(blockName);
            if ((Boolean) state.m_61143_(CrockPotBlock.OPEN)) {
                sb.append("_open");
            }
            if ((Boolean) state.m_61143_(CrockPotBlock.LIT)) {
                sb.append("_lit");
            }
            return ConfiguredModel.builder().modelFile(this.models().getExistingFile(RLUtils.createRL(sb.toString()))).rotationY(((int) ((Direction) state.m_61143_(BlockStateProperties.HORIZONTAL_FACING)).toYRot() + 180) % 360).build();
        });
    }

    public void customStageCropBlock(Block block, IntegerProperty ageProperty, List<Integer> ageSuffixes, Property<?>... ignored) {
        this.getVariantBuilder(block).forAllStatesExcept(state -> {
            Integer age = (Integer) state.m_61143_(ageProperty);
            String stageName = getBlockName(block) + "_stage" + (ageSuffixes.isEmpty() ? age : (Integer) ageSuffixes.get(Math.min(ageSuffixes.size(), age)));
            return ConfiguredModel.builder().modelFile(this.models().crop(stageName, RLUtils.createRL("block/" + stageName)).renderType(RLUtils.createVanillaRL("cutout"))).build();
        }, ignored);
    }

    public void customStageCrossBlock(Block block, IntegerProperty ageProperty, List<Integer> ageSuffixes, Property<?>... ignored) {
        this.getVariantBuilder(block).forAllStatesExcept(state -> {
            Integer age = (Integer) state.m_61143_(ageProperty);
            String stageName = getBlockName(block) + "_stage" + (ageSuffixes.isEmpty() ? age : (Integer) ageSuffixes.get(Math.min(ageSuffixes.size(), age)));
            return ConfiguredModel.builder().modelFile(this.models().cross(stageName, RLUtils.createRL("block/" + stageName)).renderType(RLUtils.createVanillaRL("cutout"))).build();
        }, ignored);
    }

    public void foodBlock(Block block) {
        String blockName = getBlockName(block);
        this.getVariantBuilder(block).forAllStates(state -> {
            StringBuilder sb = new StringBuilder(blockName);
            if (state.m_60734_() instanceof AbstractStackableFoodBlock stackableBlock) {
                int stackCount = (Integer) state.m_61143_(stackableBlock.getStacksProperty());
                if (stackCount != 1) {
                    sb.append("_").append(stackCount - 1);
                }
            }
            return ConfiguredModel.builder().modelFile(this.models().getExistingFile(RLUtils.createRL(sb.toString()))).rotationY(((int) ((Direction) state.m_61143_(BlockStateProperties.HORIZONTAL_FACING)).toYRot() + 180) % 360).build();
        });
    }

    protected static String getBlockName(Block block) {
        return ForgeRegistries.BLOCKS.getKey(block).getPath();
    }
}