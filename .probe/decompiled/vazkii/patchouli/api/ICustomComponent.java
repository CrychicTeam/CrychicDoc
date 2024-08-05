package vazkii.patchouli.api;

import net.minecraft.client.gui.GuiGraphics;

public interface ICustomComponent extends IVariablesAvailableCallback {

    void build(int var1, int var2, int var3);

    void render(GuiGraphics var1, IComponentRenderContext var2, float var3, int var4, int var5);

    default void onDisplayed(IComponentRenderContext context) {
    }

    default boolean mouseClicked(IComponentRenderContext context, double mouseX, double mouseY, int mouseButton) {
        return false;
    }
}