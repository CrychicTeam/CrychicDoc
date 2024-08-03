package com.sihenzhang.crockpot.data;

import com.sihenzhang.crockpot.block.CrockPotBlocks;
import com.sihenzhang.crockpot.tag.CrockPotBlockTags;
import java.util.concurrent.CompletableFuture;
import javax.annotation.Nullable;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class CrockPotBlockTagsProvider extends BlockTagsProvider {

    public CrockPotBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> providerFuture, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, providerFuture, "crockpot", existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        Block[] pots = new Block[] { CrockPotBlocks.CROCK_POT.get(), CrockPotBlocks.PORTABLE_CROCK_POT.get() };
        this.m_206424_(CrockPotBlockTags.CROCK_POTS).add(pots);
        this.m_206424_(BlockTags.MINEABLE_WITH_PICKAXE).add(pots);
        this.m_206424_(BlockTags.MINEABLE_WITH_PICKAXE).add(CrockPotBlocks.BIRDCAGE.get());
        this.m_206424_(BlockTags.NEEDS_IRON_TOOL).add(CrockPotBlocks.BIRDCAGE.get());
        Block[] crops = new Block[] { CrockPotBlocks.ASPARAGUS.get(), CrockPotBlocks.CORN.get(), CrockPotBlocks.EGGPLANT.get(), CrockPotBlocks.GARLIC.get(), CrockPotBlocks.ONION.get(), CrockPotBlocks.PEPPER.get(), CrockPotBlocks.TOMATO.get() };
        this.m_206424_(CrockPotBlockTags.UNKNOWN_CROPS).add(crops);
        this.m_206424_(BlockTags.CROPS).add(CrockPotBlocks.UNKNOWN_CROPS.get()).add(crops);
        this.m_206424_(CrockPotBlockTags.VOLT_GOATS_SPAWNABLE_ON).add(Blocks.TERRACOTTA, Blocks.WHITE_TERRACOTTA, Blocks.LIGHT_GRAY_TERRACOTTA, Blocks.BROWN_TERRACOTTA, Blocks.RED_TERRACOTTA, Blocks.ORANGE_TERRACOTTA, Blocks.YELLOW_TERRACOTTA, Blocks.RED_SAND);
    }

    @Override
    public String getName() {
        return "CrockPot Block Tags";
    }
}