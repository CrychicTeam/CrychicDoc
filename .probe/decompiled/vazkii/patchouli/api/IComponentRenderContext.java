package vazkii.patchouli.api;

import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public interface IComponentRenderContext {

    Screen getGui();

    Style getFont();

    void renderItemStack(GuiGraphics var1, int var2, int var3, int var4, int var5, ItemStack var6);

    void renderIngredient(GuiGraphics var1, int var2, int var3, int var4, int var5, Ingredient var6);

    boolean isAreaHovered(int var1, int var2, int var3, int var4, int var5, int var6);

    boolean navigateToEntry(ResourceLocation var1, int var2, boolean var3);

    @Deprecated
    void setHoverTooltip(List<String> var1);

    void setHoverTooltipComponents(List<Component> var1);

    @Deprecated(forRemoval = true)
    void registerButton(Button var1, int var2, Runnable var3);

    void addWidget(AbstractWidget var1, int var2);

    ResourceLocation getBookTexture();

    ResourceLocation getCraftingTexture();

    int getTextColor();

    int getHeaderColor();

    int getTicksInBook();
}