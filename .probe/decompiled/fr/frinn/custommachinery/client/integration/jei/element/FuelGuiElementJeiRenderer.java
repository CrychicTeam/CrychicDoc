package fr.frinn.custommachinery.client.integration.jei.element;

import com.google.common.collect.Lists;
import fr.frinn.custommachinery.api.crafting.IMachineRecipe;
import fr.frinn.custommachinery.api.integration.jei.IJEIElementRenderer;
import fr.frinn.custommachinery.common.guielement.FuelGuiElement;
import fr.frinn.custommachinery.common.requirement.FuelRequirement;
import java.util.Collections;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class FuelGuiElementJeiRenderer implements IJEIElementRenderer<FuelGuiElement> {

    public void renderElementInJEI(GuiGraphics graphics, FuelGuiElement element, IMachineRecipe recipe, int mouseX, int mouseY) {
        if (Minecraft.getInstance().level != null) {
            int posX = element.getX();
            int posY = element.getY();
            int width = element.getWidth();
            int height = element.getHeight();
            graphics.blit(element.getEmptyTexture(), posX, posY, 0.0F, 0.0F, width, height, width, height);
            int filledHeight = (int) (Minecraft.getInstance().level.m_46467_() / 2L % (long) height);
            graphics.blit(element.getFilledTexture(), posX, posY + height - filledHeight, 0.0F, (float) (height - filledHeight), width, filledHeight, width, height);
        }
    }

    public List<Component> getJEITooltips(FuelGuiElement element, IMachineRecipe recipe) {
        int amount = (Integer) recipe.getRequirements().stream().filter(requirement -> requirement instanceof FuelRequirement).findFirst().map(requirement -> ((FuelRequirement) requirement).getAmount()).orElse(0);
        return (List<Component>) (amount > 0 ? Lists.newArrayList(new Component[] { Component.translatable("custommachinery.jei.ingredient.fuel.amount", amount) }) : Collections.emptyList());
    }
}