package com.mrcrayfish.configured.client.screen.widget;

import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;

public class ConfiguredButton extends Button {

    private Tooltip tooltip;

    private Predicate<Button> tooltipPredicate = button -> true;

    protected ConfiguredButton(int x, int y, int width, int height, Component message, Button.OnPress onPress, Button.CreateNarration narration) {
        super(x, y, width, height, message, onPress, narration);
    }

    public void setTooltip(@Nullable Tooltip tooltip, Predicate<Button> predicate) {
        this.m_257544_(tooltip);
        this.tooltipPredicate = predicate;
        this.tooltip = tooltip;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        if (this.f_93624_) {
            this.m_257544_(this.tooltipPredicate.test(this) ? this.tooltip : null);
        }
        super.m_88315_(graphics, mouseX, mouseY, partialTick);
    }
}