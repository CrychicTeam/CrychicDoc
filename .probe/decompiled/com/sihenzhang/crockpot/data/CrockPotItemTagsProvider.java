package com.sihenzhang.crockpot.data;

import com.sihenzhang.crockpot.item.CrockPotItems;
import com.sihenzhang.crockpot.tag.CrockPotBlockTags;
import com.sihenzhang.crockpot.tag.CrockPotItemTags;
import java.util.concurrent.CompletableFuture;
import javax.annotation.Nullable;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

public class CrockPotItemTagsProvider extends ItemTagsProvider {

    public CrockPotItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> providerFuture, CompletableFuture<TagsProvider.TagLookup<Block>> blockTagsProviderFuture, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, providerFuture, blockTagsProviderFuture, "crockpot", existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.m_206421_(CrockPotBlockTags.CROCK_POTS, CrockPotItemTags.CROCK_POTS);
        Item[] milkmadeHats = new Item[] { CrockPotItems.MILKMADE_HAT.get(), CrockPotItems.CREATIVE_MILKMADE_HAT.get() };
        this.m_206424_(CrockPotItemTags.MILKMADE_HATS).add(milkmadeHats);
        CrockPotItems.PARROT_EGGS.forEach((variant, egg) -> this.m_206424_(CrockPotItemTags.PARROT_EGGS).add((Item) egg.get()));
        this.m_206424_(Tags.Items.EGGS).addTag(CrockPotItemTags.PARROT_EGGS);
        this.m_206424_(CrockPotItemTags.CROPS_ASPARAGUS).add(CrockPotItems.ASPARAGUS.get());
        this.m_206424_(CrockPotItemTags.CROPS_CORN).add(CrockPotItems.CORN.get());
        this.m_206424_(CrockPotItemTags.CROPS_EGGPLANT).add(CrockPotItems.EGGPLANT.get());
        this.m_206424_(CrockPotItemTags.CROPS_GARLIC).add(CrockPotItems.GARLIC.get());
        this.m_206424_(CrockPotItemTags.CROPS_ONION).add(CrockPotItems.ONION.get());
        this.m_206424_(CrockPotItemTags.CROPS_PEPPER).add(CrockPotItems.PEPPER.get());
        this.m_206424_(CrockPotItemTags.CROPS_TOMATO).add(CrockPotItems.TOMATO.get());
        this.m_206424_(Tags.Items.CROPS).addTags(new TagKey[] { CrockPotItemTags.CROPS_ASPARAGUS, CrockPotItemTags.CROPS_CORN, CrockPotItemTags.CROPS_EGGPLANT, CrockPotItemTags.CROPS_GARLIC, CrockPotItemTags.CROPS_ONION, CrockPotItemTags.CROPS_PEPPER, CrockPotItemTags.CROPS_TOMATO });
        this.m_206424_(CrockPotItemTags.SEEDS_ASPARAGUS).add(CrockPotItems.ASPARAGUS_SEEDS.get());
        this.m_206424_(CrockPotItemTags.SEEDS_CORN).add(CrockPotItems.CORN_SEEDS.get());
        this.m_206424_(CrockPotItemTags.SEEDS_EGGPLANT).add(CrockPotItems.EGGPLANT_SEEDS.get());
        this.m_206424_(CrockPotItemTags.SEEDS_GARLIC).add(CrockPotItems.GARLIC_SEEDS.get());
        this.m_206424_(CrockPotItemTags.SEEDS_ONION).add(CrockPotItems.ONION_SEEDS.get());
        this.m_206424_(CrockPotItemTags.SEEDS_PEPPER).add(CrockPotItems.PEPPER_SEEDS.get());
        this.m_206424_(CrockPotItemTags.SEEDS_TOMATO).add(CrockPotItems.TOMATO_SEEDS.get());
        this.m_206424_(Tags.Items.SEEDS).addTags(new TagKey[] { CrockPotItemTags.SEEDS_ASPARAGUS, CrockPotItemTags.SEEDS_CORN, CrockPotItemTags.SEEDS_EGGPLANT, CrockPotItemTags.SEEDS_GARLIC, CrockPotItemTags.SEEDS_ONION, CrockPotItemTags.SEEDS_PEPPER, CrockPotItemTags.SEEDS_TOMATO });
        this.m_206424_(CrockPotItemTags.VEGETABLES_BEETROOT).add(Items.BEETROOT);
        this.m_206424_(CrockPotItemTags.VEGETABLES_CARROT).add(Items.CARROT);
        this.m_206424_(CrockPotItemTags.VEGETABLES_POTATO).add(Items.POTATO);
        this.m_206424_(CrockPotItemTags.VEGETABLES_PUMPKIN).add(Items.PUMPKIN);
        this.m_206424_(CrockPotItemTags.VEGETABLES_ASPARAGUS).add(CrockPotItems.ASPARAGUS.get());
        this.m_206424_(CrockPotItemTags.VEGETABLES_CORN).add(CrockPotItems.CORN.get());
        this.m_206424_(CrockPotItemTags.VEGETABLES_EGGPLANT).add(CrockPotItems.EGGPLANT.get());
        this.m_206424_(CrockPotItemTags.VEGETABLES_GARLIC).add(CrockPotItems.GARLIC.get());
        this.m_206424_(CrockPotItemTags.VEGETABLES_ONION).add(CrockPotItems.ONION.get());
        this.m_206424_(CrockPotItemTags.VEGETABLES_PEPPER).add(CrockPotItems.PEPPER.get());
        this.m_206424_(CrockPotItemTags.VEGETABLES_TOMATO).add(CrockPotItems.TOMATO.get());
        this.m_206424_(CrockPotItemTags.VEGETABLES).addTags(new TagKey[] { CrockPotItemTags.VEGETABLES_BEETROOT, CrockPotItemTags.VEGETABLES_CARROT, CrockPotItemTags.VEGETABLES_POTATO, CrockPotItemTags.VEGETABLES_PUMPKIN, CrockPotItemTags.VEGETABLES_ASPARAGUS, CrockPotItemTags.VEGETABLES_CORN, CrockPotItemTags.VEGETABLES_EGGPLANT, CrockPotItemTags.VEGETABLES_GARLIC, CrockPotItemTags.VEGETABLES_ONION, CrockPotItemTags.VEGETABLES_PEPPER, CrockPotItemTags.VEGETABLES_TOMATO });
        this.m_206424_(CrockPotItemTags.FRUITS_APPLE).add(Items.APPLE);
        this.m_206424_(CrockPotItemTags.FRUITS).addTag(CrockPotItemTags.FRUITS_APPLE);
        this.m_206424_(CrockPotItemTags.RAW_BEEF).add(Items.BEEF);
        this.m_206424_(CrockPotItemTags.RAW_CHICKEN).add(Items.CHICKEN);
        this.m_206424_(CrockPotItemTags.RAW_MUTTON).add(Items.MUTTON);
        this.m_206424_(CrockPotItemTags.RAW_PORK).add(Items.PORKCHOP);
        this.m_206424_(CrockPotItemTags.RAW_RABBIT).add(Items.RABBIT);
        this.m_206424_(CrockPotItemTags.COOKED_BEEF).add(Items.COOKED_BEEF);
        this.m_206424_(CrockPotItemTags.COOKED_CHICKEN).add(Items.COOKED_CHICKEN);
        this.m_206424_(CrockPotItemTags.COOKED_MUTTON).add(Items.COOKED_MUTTON);
        this.m_206424_(CrockPotItemTags.COOKED_PORK).add(Items.COOKED_PORKCHOP);
        this.m_206424_(CrockPotItemTags.COOKED_RABBIT).add(Items.COOKED_RABBIT);
        this.m_206424_(CrockPotItemTags.RAW_FISHES_COD).add(Items.COD);
        this.m_206424_(CrockPotItemTags.RAW_FISHES_SALMON).add(Items.SALMON);
        this.m_206424_(CrockPotItemTags.RAW_FISHES_TROPICAL_FISH).add(Items.TROPICAL_FISH);
        this.m_206424_(CrockPotItemTags.RAW_FISHES).addTags(new TagKey[] { CrockPotItemTags.RAW_FISHES_COD, CrockPotItemTags.RAW_FISHES_SALMON, CrockPotItemTags.RAW_FISHES_TROPICAL_FISH });
        this.m_206424_(CrockPotItemTags.COOKED_FISHES_COD).add(Items.COOKED_COD);
        this.m_206424_(CrockPotItemTags.COOKED_FISHES_SALMON).add(Items.COOKED_SALMON);
        this.m_206424_(CrockPotItemTags.COOKED_FISHES).addTags(new TagKey[] { CrockPotItemTags.COOKED_FISHES_COD, CrockPotItemTags.COOKED_FISHES_SALMON });
        this.m_206424_(CrockPotItemTags.RAW_FROGS).add(CrockPotItems.FROG_LEGS.get());
        this.m_206424_(CrockPotItemTags.COOKED_FROGS).add(CrockPotItems.COOKED_FROG_LEGS.get());
        this.m_206424_(CrockPotItemTags.CURIO).add(CrockPotItems.GNAWS_COIN.get());
        this.m_206424_(CrockPotItemTags.HEAD).add(milkmadeHats);
    }

    @Override
    public String getName() {
        return "CrockPot Item Tags";
    }
}