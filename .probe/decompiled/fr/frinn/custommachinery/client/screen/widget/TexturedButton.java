package fr.frinn.custommachinery.client.screen.widget;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class TexturedButton extends Button {

    private final ResourceLocation texture;

    @Nullable
    private final ResourceLocation textureHovered;

    public static TexturedButton.Builder builder(Component message, ResourceLocation texture, Button.OnPress onPress) {
        return new TexturedButton.Builder(message, texture, onPress);
    }

    private TexturedButton(int x, int y, int width, int height, ResourceLocation texture, @Nullable ResourceLocation textureHovered, Button.OnPress onPress, Component message, Button.CreateNarration narration) {
        super(x, y, width, height, message, onPress, narration);
        this.texture = texture;
        this.textureHovered = textureHovered;
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        if (this.m_274382_() && this.textureHovered != null) {
            graphics.blit(this.textureHovered, this.m_252754_(), this.m_252907_(), 0.0F, 0.0F, this.m_5711_(), this.m_93694_(), this.m_5711_(), this.m_93694_());
        } else {
            graphics.blit(this.texture, this.m_252754_(), this.m_252907_(), 0.0F, 0.0F, this.m_5711_(), this.m_93694_(), this.m_5711_(), this.m_93694_());
        }
    }

    @Override
    protected ClientTooltipPositioner createTooltipPositioner() {
        return DefaultTooltipPositioner.INSTANCE;
    }

    public static class Builder {

        private final Component message;

        private final Button.OnPress onPress;

        private final ResourceLocation texture;

        @Nullable
        private Tooltip tooltip;

        private int x;

        private int y;

        private int width = 150;

        private int height = 20;

        @Nullable
        private ResourceLocation textureHovered;

        private Button.CreateNarration createNarration = TexturedButton.f_252438_;

        private Builder(Component message, ResourceLocation texture, Button.OnPress onPress) {
            this.message = message;
            this.texture = texture;
            this.onPress = onPress;
        }

        public TexturedButton.Builder pos(int x, int y) {
            this.x = x;
            this.y = y;
            return this;
        }

        public TexturedButton.Builder width(int width) {
            this.width = width;
            return this;
        }

        public TexturedButton.Builder size(int width, int height) {
            this.width = width;
            this.height = height;
            return this;
        }

        public TexturedButton.Builder bounds(int x, int y, int width, int height) {
            return this.pos(x, y).size(width, height);
        }

        public TexturedButton.Builder hovered(ResourceLocation textureHovered) {
            this.textureHovered = textureHovered;
            return this;
        }

        public TexturedButton.Builder tooltip(@Nullable Tooltip tooltip) {
            this.tooltip = tooltip;
            return this;
        }

        public TexturedButton.Builder createNarration(Button.CreateNarration createNarration) {
            this.createNarration = createNarration;
            return this;
        }

        public TexturedButton build() {
            TexturedButton button = new TexturedButton(this.x, this.y, this.width, this.height, this.texture, this.textureHovered, this.onPress, this.message, this.createNarration);
            button.m_257544_(this.tooltip);
            return button;
        }
    }
}