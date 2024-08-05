package fr.frinn.custommachinery.client.integration.jei.energy;

import fr.frinn.custommachinery.impl.integration.jei.CustomIngredientTypes;
import fr.frinn.custommachinery.impl.integration.jei.Energy;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class EnergyIngredientHelper implements IIngredientHelper<Energy> {

    @Override
    public IIngredientType<Energy> getIngredientType() {
        return CustomIngredientTypes.ENERGY;
    }

    public String getDisplayName(Energy energy) {
        return Component.translatable("custommachinery.jei.ingredient.energy", energy.getAmount()).getString();
    }

    public String getUniqueId(Energy energy, UidContext context) {
        return "" + energy.getAmount() + energy.getChance() + energy.isPerTick();
    }

    public Energy copyIngredient(Energy energy) {
        return new Energy(energy.getAmount(), energy.getChance(), energy.isPerTick());
    }

    public String getErrorInfo(@Nullable Energy energy) {
        return "";
    }

    public ResourceLocation getResourceLocation(Energy ingredient) {
        return new ResourceLocation("custommachinery", "energy");
    }
}