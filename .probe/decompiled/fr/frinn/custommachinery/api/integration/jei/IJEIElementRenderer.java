package fr.frinn.custommachinery.api.integration.jei;

import fr.frinn.custommachinery.api.crafting.IMachineRecipe;
import fr.frinn.custommachinery.api.guielement.IGuiElement;
import java.util.Collections;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public interface IJEIElementRenderer<T extends IGuiElement> {

    void renderElementInJEI(GuiGraphics var1, T var2, IMachineRecipe var3, int var4, int var5);

    default boolean isHoveredInJei(T element, int posX, int posY, int mouseX, int mouseY) {
        return mouseX >= posX && mouseX <= posX + element.getWidth() && mouseY >= posY && mouseY <= posY + element.getHeight();
    }

    default List<Component> getJEITooltips(T element, IMachineRecipe recipe) {
        return Collections.emptyList();
    }
}