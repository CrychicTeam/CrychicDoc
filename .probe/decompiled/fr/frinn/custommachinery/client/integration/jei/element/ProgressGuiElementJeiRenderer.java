package fr.frinn.custommachinery.client.integration.jei.element;

import fr.frinn.custommachinery.api.crafting.IMachineRecipe;
import fr.frinn.custommachinery.api.integration.jei.IJEIElementRenderer;
import fr.frinn.custommachinery.client.element.ProgressGuiElementWidget;
import fr.frinn.custommachinery.common.guielement.ProgressBarGuiElement;
import fr.frinn.custommachinery.common.integration.config.CMConfig;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public class ProgressGuiElementJeiRenderer implements IJEIElementRenderer<ProgressBarGuiElement> {

    public void renderElementInJEI(GuiGraphics graphics, ProgressBarGuiElement element, IMachineRecipe recipe, int mouseX, int mouseY) {
        int posX = element.getX();
        int posY = element.getY();
        int width = element.getWidth();
        int height = element.getHeight();
        if (Minecraft.getInstance().level != null) {
            int filledWidth = 0;
            int filledHeight = 0;
            if (recipe.getRecipeTime() > 0) {
                filledWidth = (int) ((double) width * Mth.clamp((double) Mth.map((float) (Minecraft.getInstance().level.m_46467_() % (long) recipe.getRecipeTime()) / (float) recipe.getRecipeTime(), element.getStart(), element.getEnd(), 0.0F, 1.0F), 0.0, 1.0));
                filledHeight = (int) ((double) height * Mth.clamp((double) Mth.map((float) (Minecraft.getInstance().level.m_46467_() % (long) recipe.getRecipeTime()) / (float) recipe.getRecipeTime(), element.getStart(), element.getEnd(), 0.0F, 1.0F), 0.0, 1.0));
            }
            if (element.getEmptyTexture().equals(ProgressBarGuiElement.BASE_EMPTY_TEXTURE) && element.getFilledTexture().equals(ProgressBarGuiElement.BASE_FILLED_TEXTURE)) {
                graphics.pose().pushPose();
                ProgressGuiElementWidget.rotate(graphics.pose(), element.getDirection(), posX, posY, width, height);
                graphics.blit(element.getEmptyTexture(), 0, 0, 0.0F, 0.0F, width, height, width, height);
                graphics.blit(element.getFilledTexture(), 0, 0, 0.0F, 0.0F, filledWidth, height, width, height);
                graphics.pose().popPose();
            } else {
                graphics.blit(element.getEmptyTexture(), posX, posY, 0.0F, 0.0F, width, height, width, height);
                switch(element.getDirection()) {
                    case RIGHT:
                        graphics.blit(element.getFilledTexture(), posX, posY, 0.0F, 0.0F, filledWidth, height, width, height);
                        break;
                    case LEFT:
                        graphics.blit(element.getFilledTexture(), posX + width - filledWidth, posY, (float) (width - filledWidth), 0.0F, filledWidth, height, width, height);
                        break;
                    case TOP:
                        graphics.blit(element.getFilledTexture(), posX, posY, 0.0F, 0.0F, width, filledHeight, width, height);
                        break;
                    case BOTTOM:
                        graphics.blit(element.getFilledTexture(), posX, posY + height - filledHeight, 0.0F, (float) (height - filledHeight), width, filledHeight, width, height);
                }
            }
        }
    }

    public boolean isHoveredInJei(ProgressBarGuiElement element, int posX, int posY, int mouseX, int mouseY) {
        boolean invertAxis = element.getEmptyTexture().equals(ProgressBarGuiElement.BASE_EMPTY_TEXTURE) && element.getFilledTexture().equals(ProgressBarGuiElement.BASE_FILLED_TEXTURE) && element.getDirection() != ProgressBarGuiElement.Orientation.RIGHT && element.getDirection() != ProgressBarGuiElement.Orientation.LEFT;
        int width = invertAxis ? element.getHeight() : element.getWidth();
        int height = invertAxis ? element.getWidth() : element.getHeight();
        return mouseX >= posX && mouseX <= posX + width && mouseY >= posY && mouseY <= posY + height;
    }

    public List<Component> getJEITooltips(ProgressBarGuiElement element, IMachineRecipe recipe) {
        List<Component> tooltips = new ArrayList();
        if (recipe.getRecipeTime() > 0) {
            tooltips.add(Component.translatable("custommachinery.jei.recipe.time", recipe.getRecipeTime()));
        } else {
            tooltips.add(Component.translatable("custommachinery.jei.recipe.instant"));
        }
        if (!CMConfig.get().needAdvancedInfoForRecipeID || Minecraft.getInstance().options.advancedItemTooltips) {
            tooltips.add(Component.translatable("custommachinery.jei.recipe.id", recipe.getRecipeId().toString()).withStyle(ChatFormatting.DARK_GRAY));
        }
        return tooltips;
    }
}