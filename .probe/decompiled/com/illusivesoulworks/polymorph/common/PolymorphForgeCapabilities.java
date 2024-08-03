package com.illusivesoulworks.polymorph.common;

import com.illusivesoulworks.polymorph.api.common.capability.IBlockEntityRecipeData;
import com.illusivesoulworks.polymorph.api.common.capability.IPlayerRecipeData;
import com.illusivesoulworks.polymorph.api.common.capability.IStackRecipeData;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public class PolymorphForgeCapabilities {

    public static final Capability<IPlayerRecipeData> PLAYER_RECIPE_DATA = CapabilityManager.get(new CapabilityToken<IPlayerRecipeData>() {
    });

    public static final Capability<IBlockEntityRecipeData> BLOCK_ENTITY_RECIPE_DATA = CapabilityManager.get(new CapabilityToken<IBlockEntityRecipeData>() {
    });

    public static final Capability<IStackRecipeData> STACK_RECIPE_DATA = CapabilityManager.get(new CapabilityToken<IStackRecipeData>() {
    });

    public static final ResourceLocation PLAYER_RECIPE_DATA_ID = new ResourceLocation("polymorph", "player_recipe_data");

    public static final ResourceLocation BLOCK_ENTITY_RECIPE_DATA_ID = new ResourceLocation("polymorph", "block_entity_recipe_data");

    public static final ResourceLocation STACK_RECIPE_DATA_ID = new ResourceLocation("polymorph", "stack_recipe_data");
}