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

public class WorldGenIceDragonRoosts extends WorldGenDragonRoosts {

    private static final ResourceLocation DRAGON_CHEST = new ResourceLocation("iceandfire", "chest/ice_dragon_roost");

    public WorldGenIceDragonRoosts(Codec<NoneFeatureConfiguration> configuration) {
        super(configuration, IafBlockRegistry.SILVER_PILE.get());
    }

    @Override
    protected EntityType<? extends EntityDragonBase> getDragonType() {
        return IafEntityRegistry.ICE_DRAGON.get();
    }

    @Override
    protected ResourceLocation getRoostLootTable() {
        return DRAGON_CHEST;
    }

    @Override
    protected BlockState transform(BlockState state) {
        Block block = null;
        if (state.m_60713_(Blocks.GRASS_BLOCK)) {
            block = IafBlockRegistry.FROZEN_GRASS.get();
        } else if (state.m_60713_(Blocks.DIRT_PATH)) {
            block = IafBlockRegistry.FROZEN_DIRT_PATH.get();
        } else if (state.m_204336_(Tags.Blocks.GRAVEL)) {
            block = IafBlockRegistry.FROZEN_GRAVEL.get();
        } else if (state.m_204336_(BlockTags.DIRT)) {
            block = IafBlockRegistry.FROZEN_DIRT.get();
        } else if (state.m_204336_(Tags.Blocks.STONE)) {
            block = IafBlockRegistry.FROZEN_STONE.get();
        } else if (state.m_204336_(Tags.Blocks.COBBLESTONE)) {
            block = IafBlockRegistry.FROZEN_COBBLESTONE.get();
        } else if (state.m_204336_(BlockTags.LOGS) || state.m_204336_(BlockTags.PLANKS)) {
            block = IafBlockRegistry.FROZEN_SPLINTERS.get();
        } else if (state.m_60713_(Blocks.GRASS) || state.m_204336_(BlockTags.LEAVES) || state.m_204336_(BlockTags.FLOWERS) || state.m_204336_(BlockTags.CROPS)) {
            block = Blocks.AIR;
        }
        return block != null ? block.defaultBlockState() : state;
    }

    @Override
    protected void handleCustomGeneration(@NotNull FeaturePlaceContext<NoneFeatureConfiguration> context, BlockPos position, double distance) {
        if (context.random().nextInt(1000) == 0) {
            this.generateRoostPile(context.level(), context.random(), this.getSurfacePosition(context.level(), position), IafBlockRegistry.DRAGON_ICE.get());
        }
    }
}