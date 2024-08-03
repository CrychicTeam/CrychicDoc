package snownee.lychee.compat.jei.category;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import snownee.lychee.client.gui.ScreenElement;
import snownee.lychee.core.ItemShapelessContext;
import snownee.lychee.item_inside.ItemInsideRecipe;
import snownee.lychee.item_inside.ItemInsideRecipeType;
import snownee.lychee.util.ClientProxy;

public class ItemInsideRecipeCategory extends ItemAndBlockBaseCategory<ItemShapelessContext, ItemInsideRecipe> {

    public ItemInsideRecipeCategory(ItemInsideRecipeType recipeType, ScreenElement mainIcon) {
        super(List.of(recipeType), mainIcon);
        this.infoRect.setPosition(0, 25);
        this.inputBlockRect.setX(80);
        this.methodRect.setX(77);
    }

    public void drawExtra(ItemInsideRecipe recipe, GuiGraphics graphics, double mouseX, double mouseY, int centerX) {
        super.drawExtra(recipe, graphics, mouseX, mouseY, centerX);
        if (recipe.getTime() > 0) {
            Component component = ClientProxy.format("tip.lychee.sec", recipe.getTime());
            Font font = Minecraft.getInstance().font;
            graphics.drawCenteredString(font, component, this.methodRect.getX() + 10, this.methodRect.getY() - 8, 6710886);
        }
    }

    @Override
    public int getWidth() {
        return 169;
    }
}