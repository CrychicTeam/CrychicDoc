package com.rekindled.embers.datagen;

import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraftforge.common.data.ExistingFileHelper;

public class EmbersDamageTypeTags extends TagsProvider<DamageType> {

    public static final TagKey<DamageType> HOLY_DAMAGE = TagKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("consecration", "holy"));

    public EmbersDamageTypeTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, Registries.DAMAGE_TYPE, lookupProvider, "embers", existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.m_206424_(DamageTypeTags.BYPASSES_ARMOR).addOptional(EmbersDamageTypes.EMBER_KEY.location());
        this.m_206424_(HOLY_DAMAGE).addOptional(EmbersDamageTypes.EMBER_KEY.location());
        this.m_206424_(DamageTypeTags.WITCH_RESISTANT_TO).addOptional(EmbersDamageTypes.EMBER_KEY.location());
    }
}