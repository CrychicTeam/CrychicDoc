package com.sihenzhang.crockpot.data;

import com.sihenzhang.crockpot.entity.CrockPotEntities;
import java.util.concurrent.CompletableFuture;
import javax.annotation.Nullable;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.tags.EntityTypeTags;
import net.minecraftforge.common.data.ExistingFileHelper;

public class CrockPotEntityTypeTagsProvider extends EntityTypeTagsProvider {

    public CrockPotEntityTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> providerFuture, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, providerFuture, "crockpot", existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.m_206424_(EntityTypeTags.IMPACT_PROJECTILES).add(CrockPotEntities.PARROT_EGG.get());
    }

    @Override
    public String getName() {
        return "CrockPot Entity Type Tags";
    }
}