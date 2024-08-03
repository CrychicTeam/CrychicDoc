package com.rekindled.embers.datagen;

import com.rekindled.embers.RegistryManager;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.FluidTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

public class EmbersFluidTags extends FluidTagsProvider {

    public static final TagKey<Fluid> MOLTEN_IRON = FluidTags.create(new ResourceLocation("forge", "molten_iron"));

    public static final TagKey<Fluid> MOLTEN_GOLD = FluidTags.create(new ResourceLocation("forge", "molten_gold"));

    public static final TagKey<Fluid> MOLTEN_COPPER = FluidTags.create(new ResourceLocation("forge", "molten_copper"));

    public static final TagKey<Fluid> MOLTEN_LEAD = FluidTags.create(new ResourceLocation("forge", "molten_lead"));

    public static final TagKey<Fluid> MOLTEN_SILVER = FluidTags.create(new ResourceLocation("forge", "molten_silver"));

    public static final TagKey<Fluid> MOLTEN_DAWNSTONE = FluidTags.create(new ResourceLocation("forge", "molten_dawnstone"));

    public static final TagKey<Fluid> STEAM = FluidTags.create(new ResourceLocation("forge", "steam"));

    public static final TagKey<Fluid> WATERY = FluidTags.create(new ResourceLocation("embers", "watery"));

    public static final TagKey<Fluid> MOLTEN_NICKEL = FluidTags.create(new ResourceLocation("forge", "molten_nickel"));

    public static final TagKey<Fluid> MOLTEN_TIN = FluidTags.create(new ResourceLocation("forge", "molten_tin"));

    public static final TagKey<Fluid> MOLTEN_ALUMINUM = FluidTags.create(new ResourceLocation("forge", "molten_aluminum"));

    public static final TagKey<Fluid> MOLTEN_ZINC = FluidTags.create(new ResourceLocation("forge", "molten_zinc"));

    public static final TagKey<Fluid> MOLTEN_PLATINUM = FluidTags.create(new ResourceLocation("forge", "molten_platinum"));

    public static final TagKey<Fluid> MOLTEN_URANIUM = FluidTags.create(new ResourceLocation("forge", "molten_uranium"));

    public static final TagKey<Fluid> MOLTEN_BRONZE = FluidTags.create(new ResourceLocation("forge", "molten_bronze"));

    public static final TagKey<Fluid> MOLTEN_ELECTRUM = FluidTags.create(new ResourceLocation("forge", "molten_electrum"));

    public static final TagKey<Fluid> MOLTEN_BRASS = FluidTags.create(new ResourceLocation("forge", "molten_brass"));

    public static final TagKey<Fluid> MOLTEN_CONSTANTAN = FluidTags.create(new ResourceLocation("forge", "molten_constantan"));

    public static final TagKey<Fluid> MOLTEN_INVAR = FluidTags.create(new ResourceLocation("forge", "molten_invar"));

    public static final TagKey<Fluid> INGOT_TOOLTIP = FluidTags.create(new ResourceLocation("embers", "ingot_tooltip"));

    public static final TagKey<Fluid> METAL_TOOLTIPS = FluidTags.create(new ResourceLocation("tconstruct", "tooltips/metal"));

    public EmbersFluidTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, "embers", existingFileHelper);
    }

    @Override
    public void addTags(HolderLookup.Provider provider) {
        this.m_206424_(MOLTEN_IRON).add(RegistryManager.MOLTEN_IRON.FLUID.get(), RegistryManager.MOLTEN_IRON.FLUID_FLOW.get());
        this.m_206424_(MOLTEN_GOLD).add(RegistryManager.MOLTEN_GOLD.FLUID.get(), RegistryManager.MOLTEN_GOLD.FLUID_FLOW.get());
        this.m_206424_(MOLTEN_COPPER).add(RegistryManager.MOLTEN_COPPER.FLUID.get(), RegistryManager.MOLTEN_COPPER.FLUID_FLOW.get());
        this.m_206424_(MOLTEN_LEAD).add(RegistryManager.MOLTEN_LEAD.FLUID.get(), RegistryManager.MOLTEN_LEAD.FLUID_FLOW.get());
        this.m_206424_(MOLTEN_SILVER).add(RegistryManager.MOLTEN_SILVER.FLUID.get(), RegistryManager.MOLTEN_SILVER.FLUID_FLOW.get());
        this.m_206424_(MOLTEN_DAWNSTONE).add(RegistryManager.MOLTEN_DAWNSTONE.FLUID.get(), RegistryManager.MOLTEN_DAWNSTONE.FLUID_FLOW.get());
        this.m_206424_(STEAM).add(RegistryManager.STEAM.FLUID.get(), RegistryManager.STEAM.FLUID_FLOW.get());
        this.m_206424_(Tags.Fluids.GASEOUS).add(RegistryManager.STEAM.FLUID.get(), RegistryManager.STEAM.FLUID_FLOW.get()).add(RegistryManager.DWARVEN_GAS.FLUID.get(), RegistryManager.DWARVEN_GAS.FLUID_FLOW.get());
        this.m_206424_(WATERY).addTag(FluidTags.WATER);
        this.m_206424_(MOLTEN_NICKEL).add(RegistryManager.MOLTEN_NICKEL.FLUID.get(), RegistryManager.MOLTEN_NICKEL.FLUID_FLOW.get());
        this.m_206424_(MOLTEN_TIN).add(RegistryManager.MOLTEN_TIN.FLUID.get(), RegistryManager.MOLTEN_TIN.FLUID_FLOW.get());
        this.m_206424_(MOLTEN_ALUMINUM).add(RegistryManager.MOLTEN_ALUMINUM.FLUID.get(), RegistryManager.MOLTEN_ALUMINUM.FLUID_FLOW.get());
        this.m_206424_(MOLTEN_ZINC).add(RegistryManager.MOLTEN_ZINC.FLUID.get(), RegistryManager.MOLTEN_ZINC.FLUID_FLOW.get());
        this.m_206424_(MOLTEN_PLATINUM).add(RegistryManager.MOLTEN_PLATINUM.FLUID.get(), RegistryManager.MOLTEN_PLATINUM.FLUID_FLOW.get());
        this.m_206424_(MOLTEN_URANIUM).add(RegistryManager.MOLTEN_URANIUM.FLUID.get(), RegistryManager.MOLTEN_URANIUM.FLUID_FLOW.get());
        this.m_206424_(MOLTEN_BRONZE).add(RegistryManager.MOLTEN_BRONZE.FLUID.get(), RegistryManager.MOLTEN_BRONZE.FLUID_FLOW.get());
        this.m_206424_(MOLTEN_ELECTRUM).add(RegistryManager.MOLTEN_ELECTRUM.FLUID.get(), RegistryManager.MOLTEN_ELECTRUM.FLUID_FLOW.get());
        this.m_206424_(MOLTEN_BRASS).add(RegistryManager.MOLTEN_BRASS.FLUID.get(), RegistryManager.MOLTEN_BRASS.FLUID_FLOW.get());
        this.m_206424_(MOLTEN_CONSTANTAN).add(RegistryManager.MOLTEN_CONSTANTAN.FLUID.get(), RegistryManager.MOLTEN_CONSTANTAN.FLUID_FLOW.get());
        this.m_206424_(MOLTEN_INVAR).add(RegistryManager.MOLTEN_INVAR.FLUID.get(), RegistryManager.MOLTEN_INVAR.FLUID_FLOW.get());
        this.m_206424_(INGOT_TOOLTIP).addTag(MOLTEN_IRON).addTag(MOLTEN_GOLD).addTag(MOLTEN_COPPER).addTag(MOLTEN_LEAD).addTag(MOLTEN_SILVER).addTag(MOLTEN_DAWNSTONE).addTag(MOLTEN_NICKEL).addTag(MOLTEN_TIN).addTag(MOLTEN_ALUMINUM).addTag(MOLTEN_ZINC).addTag(MOLTEN_PLATINUM).addTag(MOLTEN_URANIUM).addTag(MOLTEN_BRONZE).addTag(MOLTEN_ELECTRUM).addTag(MOLTEN_BRASS).addTag(MOLTEN_CONSTANTAN).addTag(MOLTEN_INVAR).m_176841_(METAL_TOOLTIPS.location());
        this.m_206424_(METAL_TOOLTIPS).addTag(INGOT_TOOLTIP);
    }
}