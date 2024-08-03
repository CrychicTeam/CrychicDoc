package com.github.alexthe666.iceandfire.world.gen;

import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.datagen.tags.IafBlockTags;
import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import com.mojang.serialization.Codec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class WorldGenFireDragonCave extends WorldGenDragonCave {

    public static ResourceLocation FIRE_DRAGON_CHEST = new ResourceLocation("iceandfire", "chest/fire_dragon_female_cave");

    public static ResourceLocation FIRE_DRAGON_CHEST_MALE = new ResourceLocation("iceandfire", "chest/fire_dragon_male_cave");

    public WorldGenFireDragonCave(Codec<NoneFeatureConfiguration> configuration) {
        super(configuration);
        this.DRAGON_CHEST = FIRE_DRAGON_CHEST;
        this.DRAGON_MALE_CHEST = FIRE_DRAGON_CHEST_MALE;
        this.CEILING_DECO = new WorldGenCaveStalactites(IafBlockRegistry.CHARRED_STONE.get(), 3);
        this.PALETTE_BLOCK1 = IafBlockRegistry.CHARRED_STONE.get().defaultBlockState();
        this.PALETTE_BLOCK2 = IafBlockRegistry.CHARRED_COBBLESTONE.get().defaultBlockState();
        this.TREASURE_PILE = IafBlockRegistry.GOLD_PILE.get().defaultBlockState();
        this.dragonTypeOreTag = IafBlockTags.FIRE_DRAGON_CAVE_ORES;
    }

    @Override
    public EntityType<? extends EntityDragonBase> getDragonType() {
        return IafEntityRegistry.FIRE_DRAGON.get();
    }
}