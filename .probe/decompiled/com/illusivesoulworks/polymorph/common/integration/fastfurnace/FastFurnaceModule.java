package com.illusivesoulworks.polymorph.common.integration.fastfurnace;

import com.illusivesoulworks.polymorph.PolymorphConstants;
import com.illusivesoulworks.polymorph.common.integration.AbstractCompatibilityModule;
import com.illusivesoulworks.polymorph.common.integration.PolymorphIntegrations;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.apache.commons.lang3.reflect.FieldUtils;

public class FastFurnaceModule extends AbstractCompatibilityModule {

    @Override
    public boolean selectRecipe(BlockEntity blockEntity, Recipe<?> recipe) {
        if (recipe instanceof AbstractCookingRecipe && blockEntity instanceof AbstractFurnaceBlockEntity) {
            try {
                FieldUtils.writeField(blockEntity, "curRecipe", recipe, true);
            } catch (IllegalArgumentException | IllegalAccessException var4) {
                PolymorphIntegrations.disable("fastfurnace");
                PolymorphConstants.LOG.error("Polymorph encountered an error with its fastfurnace integration.");
                PolymorphConstants.LOG.error("The integration module for fastfurnace will be disabled.");
                PolymorphConstants.LOG.error("Please report this bug to Polymorph only, do not report this to fastfurnace.");
                var4.printStackTrace();
            }
        }
        return false;
    }
}