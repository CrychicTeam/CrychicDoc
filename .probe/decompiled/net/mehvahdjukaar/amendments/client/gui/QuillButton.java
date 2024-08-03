package net.mehvahdjukaar.amendments.client.gui;

import java.util.Arrays;
import java.util.Locale;
import net.mehvahdjukaar.amendments.Amendments;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class QuillButton extends AbstractWidget {

    protected static final ResourceLocation[] textures = (ResourceLocation[]) Arrays.stream(QuillButton.QuillType.values()).map(t -> Amendments.res("textures/gui/quill/" + t.name().toLowerCase(Locale.ROOT) + ".png")).toArray(ResourceLocation[]::new);

    private int type = 0;

    public QuillButton(Screen screen) {
        super(screen.width / 2 + 70, 20, 48, 144, Component.empty());
        this.refreshTooltip();
    }

    private void refreshTooltip() {
        this.m_257544_(Tooltip.create(Component.translatable("gui.amendments.quill." + this.getType().name().toLowerCase(Locale.ROOT))));
    }

    public QuillButton.QuillType getType() {
        return QuillButton.QuillType.values()[this.type];
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        int length = QuillButton.QuillType.values().length;
        if (Screen.hasShiftDown()) {
            this.type = (length + this.type - 1) % length;
        } else {
            this.type = (this.type + 1) % length;
        }
        this.refreshTooltip();
    }

    public void onClick(double mouseX, double mouseY, int button) {
        this.type += button == 0 ? 1 : -1;
        this.type = this.type % QuillButton.QuillType.values().length;
        this.refreshTooltip();
    }

    @Override
    protected boolean isValidClickButton(int button) {
        return super.isValidClickButton(button) || button == 1;
    }

    @Override
    protected ClientTooltipPositioner createTooltipPositioner() {
        return DefaultTooltipPositioner.INSTANCE;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int i, int j, float f) {
        guiGraphics.blit(textures[this.type], this.m_252754_(), this.m_252907_(), 0.0F, 0.0F, this.f_93618_, this.f_93619_, this.f_93618_, this.f_93619_);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
    }

    public ChatFormatting getChatFormatting() {
        return switch(this.getType()) {
            case BOLD ->
                ChatFormatting.BOLD;
            case STRIKETHROUGH ->
                ChatFormatting.STRIKETHROUGH;
            case ITALIC ->
                ChatFormatting.ITALIC;
            case UNDERLINE ->
                ChatFormatting.UNDERLINE;
            case OBFUSCATED ->
                ChatFormatting.OBFUSCATED;
            default ->
                ChatFormatting.RESET;
        };
    }

    public static enum QuillType {

        DEFAULT,
        ITALIC,
        BOLD,
        UNDERLINE,
        STRIKETHROUGH,
        OBFUSCATED
    }
}