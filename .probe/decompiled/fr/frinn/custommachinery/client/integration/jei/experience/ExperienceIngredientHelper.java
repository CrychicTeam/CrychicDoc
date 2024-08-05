package fr.frinn.custommachinery.client.integration.jei.experience;

import fr.frinn.custommachinery.impl.integration.jei.CustomIngredientTypes;
import fr.frinn.custommachinery.impl.integration.jei.Experience;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class ExperienceIngredientHelper implements IIngredientHelper<Experience> {

    @Override
    public IIngredientType<Experience> getIngredientType() {
        return CustomIngredientTypes.EXPERIENCE;
    }

    public String getDisplayName(Experience xp) {
        return Component.translatable("custommachinery.jei.ingredient.xp", xp.getXp()).getString();
    }

    public String getUniqueId(Experience xp, UidContext context) {
        return "" + xp.getXp() + xp.getChance() + xp.isPerTick() + xp.getForm();
    }

    public Experience copyIngredient(Experience xp) {
        return new Experience(xp.getXp(), xp.getChance(), xp.isPerTick(), xp.getForm());
    }

    public String getErrorInfo(@Nullable Experience energy) {
        return "";
    }

    public ResourceLocation getResourceLocation(Experience ingredient) {
        return new ResourceLocation("custommachinery", "experience");
    }
}