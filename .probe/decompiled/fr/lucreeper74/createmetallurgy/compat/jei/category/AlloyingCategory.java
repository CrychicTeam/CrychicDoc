package fr.lucreeper74.createmetallurgy.compat.jei.category;

import com.simibubi.create.compat.jei.category.BasinCategory;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.compat.jei.category.animations.AnimatedBlazeBurner;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import fr.lucreeper74.createmetallurgy.compat.jei.category.animations.AnimatedFoundryMixer;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import net.minecraft.client.gui.GuiGraphics;

public class AlloyingCategory extends BasinCategory {

    private final AnimatedFoundryMixer mixer = new AnimatedFoundryMixer();

    private final AnimatedBlazeBurner heater = new AnimatedBlazeBurner();

    public AlloyingCategory(CreateRecipeCategory.Info<BasinRecipe> info) {
        super(info, true);
    }

    @Override
    public void draw(BasinRecipe recipe, IRecipeSlotsView iRecipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        super.draw(recipe, iRecipeSlotsView, graphics, mouseX, mouseY);
        HeatCondition requiredHeat = recipe.getRequiredHeat();
        if (requiredHeat != HeatCondition.NONE) {
            this.heater.withHeat(requiredHeat.visualizeAsBlazeBurner()).draw(graphics, this.getBackground().getWidth() / 2 + 3, 55);
        }
        this.mixer.draw(graphics, this.getBackground().getWidth() / 2 + 3, 34);
    }
}