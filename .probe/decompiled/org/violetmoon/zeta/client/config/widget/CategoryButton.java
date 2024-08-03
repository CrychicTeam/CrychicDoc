package org.violetmoon.zeta.client.config.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class CategoryButton extends Button {

    private final ItemStack icon;

    private final Component text;

    public CategoryButton(int x, int y, int w, int h, Component text, ItemStack icon, Button.OnPress onClick) {
        super(new Button.Builder(Component.literal(""), onClick).pos(x, y).size(w, h));
        this.icon = icon;
        this.text = text;
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.m_88315_(guiGraphics, mouseX, mouseY, partialTicks);
        if (!this.f_93623_) {
            this.m_257544_(Tooltip.create(Component.translatable("quark.gui.config.missingaddon")));
        }
        Minecraft mc = Minecraft.getInstance();
        guiGraphics.renderFakeItem(this.icon, this.m_252754_() + 5, this.m_252907_() + 2);
        int iconPad = 10;
        guiGraphics.drawCenteredString(mc.font, this.text, this.m_252754_() + this.f_93618_ / 2 + iconPad, this.m_252907_() + (this.f_93619_ - 8) / 2, this.getFGColor());
    }
}