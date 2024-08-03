package com.rekindled.embers.util;

import com.rekindled.embers.datagen.EmbersBlockTags;
import com.rekindled.embers.datagen.EmbersItemTags;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.TierSortingRegistry;

public class EmbersTiers {

    public static final Tier LEAD = new ForgeTier(2, 168, 6.0F, 2.0F, 4, EmbersBlockTags.NEEDS_LEAD_TOOL, () -> Ingredient.of(EmbersItemTags.LEAD_INGOT));

    public static final Tier TYRFING = new ForgeTier(2, 512, 7.5F, 0.0F, 24, EmbersBlockTags.NEEDS_TYRFING, () -> Ingredient.of(EmbersItemTags.ASH_DUST));

    public static final Tier SILVER = new ForgeTier(2, 202, 7.6F, 2.0F, 20, EmbersBlockTags.NEEDS_SILVER_TOOL, () -> Ingredient.of(EmbersItemTags.SILVER_INGOT));

    public static final Tier DAWNSTONE = new ForgeTier(2, 644, 7.5F, 2.5F, 18, EmbersBlockTags.NEEDS_DAWNSTONE_TOOL, () -> Ingredient.of(EmbersItemTags.DAWNSTONE_INGOT));

    public static final Tier CLOCKWORK_PICK = new ForgeTier(3, -1, 16.0F, 4.0F, 18, EmbersBlockTags.NEEDS_CLOCKWORK_TOOL, () -> Ingredient.EMPTY);

    public static final Tier CLOCKWORK_AXE = new ForgeTier(3, -1, 16.0F, 5.0F, 18, EmbersBlockTags.NEEDS_CLOCKWORK_TOOL, () -> Ingredient.EMPTY);

    public static final Tier CLOCKWORK_HAMMER = new ForgeTier(1, -1, 6.0F, 6.0F, 18, EmbersBlockTags.NEEDS_CLOCKWORK_HAMMER, () -> Ingredient.EMPTY);

    static {
        TierSortingRegistry.registerTier(LEAD, new ResourceLocation("embers", "lead"), List.of(Tiers.IRON), List.of(Tiers.DIAMOND, "embers:silver"));
        TierSortingRegistry.registerTier(TYRFING, new ResourceLocation("embers", "tyrfing"), List.of(Tiers.IRON, LEAD), List.of(Tiers.DIAMOND, "embers:silver"));
        TierSortingRegistry.registerTier(SILVER, new ResourceLocation("embers", "silver"), List.of(Tiers.IRON, LEAD), List.of(Tiers.DIAMOND, "embers:dawnstone"));
        TierSortingRegistry.registerTier(DAWNSTONE, new ResourceLocation("embers", "dawnstone"), List.of(Tiers.IRON, SILVER), List.of(Tiers.DIAMOND, "embers:clockwork"));
        TierSortingRegistry.registerTier(CLOCKWORK_PICK, new ResourceLocation("embers", "clockwork_pickaxe"), List.of(Tiers.DIAMOND, DAWNSTONE), List.of(Tiers.NETHERITE));
        TierSortingRegistry.registerTier(CLOCKWORK_AXE, new ResourceLocation("embers", "clockwork_axe"), List.of(Tiers.DIAMOND, DAWNSTONE), List.of(Tiers.NETHERITE));
        TierSortingRegistry.registerTier(CLOCKWORK_HAMMER, new ResourceLocation("embers", "clockwork_hammer"), List.of(Tiers.STONE), List.of(Tiers.IRON));
    }
}