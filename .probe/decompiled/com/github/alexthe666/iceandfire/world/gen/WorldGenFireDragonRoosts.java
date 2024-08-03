package com.github.alexthe666.iceandfire.world.gen;

import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraftforge.common.Tags;
import org.jetbrains.annotations.NotNull;

public class WorldGenFireDragonRoosts extends WorldGenDragonRoosts {

    private static final ResourceLocation DRAGON_CHEST = new ResourceLocation("iceandfire", "chest/fire_dragon_roost");

    public WorldGenFireDragonRoosts(Codec<NoneFeatureConfiguration> configuration) {
        super(configuration, IafBlockRegistry.GOLD_PILE.get());
    }

    @Override
    protected EntityType<? extends EntityDragonBase> getDragonType() {
        return IafEntityRegistry.FIRE_DRAGON.get();
    }

    @Override
    protected ResourceLocation getRoostLootTable() {
        return DRAGON_CHEST;
    }

    @Override
    protected BlockState transform(BlockState state) {
        Block block = null;
        if (state.m_60713_(Blocks.GRASS_BLOCK)) {
            block = IafBlockRegistry.CHARRED_GRASS.get();
        } else if (state.m_60713_(Blocks.DIRT_PATH)) {
            block = IafBlockRegistry.CHARRED_DIRT_PATH.get();
        } else if (state.m_204336_(Tags.Blocks.GRAVEL)) {
            block = IafBlockRegistry.CHARRED_GRAVEL.get();
        } else if (state.m_204336_(BlockTags.DIRT)) {
            block = IafBlockRegistry.CHARRED_DIRT.get();
        } else if (state.m_204336_(Tags.Blocks.STONE)) {
            block = IafBlockRegistry.CHARRED_STONE.get();
        } else if (state.m_204336_(Tags.Blocks.COBBLESTONE)) {
            block = IafBlockRegistry.CHARRED_COBBLESTONE.get();
        } else if (state.m_204336_(BlockTags.LOGS) || state.m_204336_(BlockTags.PLANKS)) {
            block = IafBlockRegistry.ASH.get();
        } else if (state.m_60713_(Blocks.GRASS) || state.m_204336_(BlockTags.LEAVES) || state.m_204336_(BlockTags.FLOWERS) || state.m_204336_(BlockTags.CROPS)) {
            block = Blocks.AIR;
        }
        return block != null ? block.defaultBlockState() : state;
    }

    @Override
    protected void handleCustomGeneration(@NotNull FeaturePlaceContext<NoneFeatureConfiguration> context, BlockPos position, double distance) {
        if (context.random().nextInt(1000) == 0) {
            this.generateRoostPile(context.level(), context.random(), this.getSurfacePosition(context.level(), position), IafBlockRegistry.ASH.get());
        }
    }
}