package com.xiaohunao.createheatjs.mixin;

import com.simibubi.create.compat.jei.category.BasinCategory;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.compat.jei.category.MixingCategory;
import com.simibubi.create.compat.jei.category.animations.AnimatedMixer;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.xiaohunao.createheatjs.util.BlockHelper;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = { MixingCategory.class }, remap = false)
public abstract class MixingCategoryMixin extends BasinCategory {

    @Shadow
    @Final
    private AnimatedMixer mixer;

    public MixingCategoryMixin(CreateRecipeCategory.Info<BasinRecipe> info, boolean needsHeating) {
        super(info, needsHeating);
    }

    @Inject(method = { "draw(Lcom/simibubi/create/content/processing/basin/BasinRecipe;Lmezz/jei/api/gui/ingredient/IRecipeSlotsView;Lnet/minecraft/client/gui/GuiGraphics;DD)V" }, at = { @At(value = "INVOKE", target = "Lcom/simibubi/create/content/processing/basin/BasinRecipe;getRequiredHeat()Lcom/simibubi/create/content/processing/recipe/HeatCondition;") }, cancellable = true, remap = false)
    private void drawMixin(BasinRecipe recipe, IRecipeSlotsView iRecipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY, CallbackInfo ci) {
        HeatCondition requiredHeat = recipe.getRequiredHeat();
        if (BlockHelper.hetaSourceRender(graphics, this.getBackground(), requiredHeat)) {
            this.mixer.draw(graphics, this.getBackground().getWidth() / 2 + 3, 34);
            ci.cancel();
        }
    }
}