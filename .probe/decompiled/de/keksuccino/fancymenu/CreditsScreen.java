package de.keksuccino.fancymenu;

import com.mojang.blaze3d.systems.RenderSystem;
import de.keksuccino.fancymenu.util.rendering.RenderingUtils;
import de.keksuccino.fancymenu.util.rendering.text.markdown.ScrollableMarkdownRenderer;
import de.keksuccino.fancymenu.util.rendering.ui.UIBase;
import de.keksuccino.fancymenu.util.rendering.ui.widget.button.ExtendedButton;
import de.keksuccino.fancymenu.util.resource.ResourceSource;
import de.keksuccino.fancymenu.util.resource.ResourceSourceType;
import de.keksuccino.fancymenu.util.resource.ResourceSupplier;
import de.keksuccino.fancymenu.util.resource.resources.text.IText;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class CreditsScreen extends Screen {

    private static final ResourceSource CREDITS_SOURCE = ResourceSource.of("fancymenu:credits_and_copyright.md", ResourceSourceType.LOCATION);

    protected ScrollableMarkdownRenderer markdownRenderer;

    protected int headerHeight = 20;

    protected int footerHeight = 40;

    protected int border = 40;

    protected Screen parent;

    protected boolean textSet = false;

    protected ResourceSupplier<IText> creditsTextSupplier = ResourceSupplier.text(CREDITS_SOURCE.getSerializationSource());

    public CreditsScreen(@NotNull Screen parent) {
        super(Component.empty());
        this.parent = parent;
    }

    @Override
    protected void init() {
        int centerX = this.f_96543_ / 2;
        int scrollWidth = this.f_96543_ - this.border * 2;
        int scrollHeight = this.f_96544_ - this.headerHeight - this.footerHeight;
        if (this.markdownRenderer == null) {
            this.markdownRenderer = new ScrollableMarkdownRenderer((float) (centerX - scrollWidth / 2), (float) this.headerHeight, (float) scrollWidth, (float) scrollHeight);
        } else {
            this.markdownRenderer.rebuild((float) (centerX - scrollWidth / 2), (float) this.headerHeight, (float) scrollWidth, (float) scrollHeight);
        }
        this.markdownRenderer.getMarkdownRenderer().setHeadlineLineColor(UIBase.getUIColorTheme().screen_background_color_darker);
        this.markdownRenderer.getMarkdownRenderer().setTextBaseColor(UIBase.getUIColorTheme().generic_text_base_color);
        this.markdownRenderer.getMarkdownRenderer().setTextShadow(false);
        this.m_142416_(this.markdownRenderer);
        UIBase.applyDefaultWidgetSkinTo((ExtendedButton) this.m_142416_(new ExtendedButton(centerX - 100, this.f_96544_ - this.footerHeight / 2 - 10, 200, 20, Component.translatable("fancymenu.common.close"), var1x -> this.onClose())));
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {
        if (!this.textSet) {
            IText text = this.creditsTextSupplier.get();
            if (text != null) {
                List<String> lines = text.getTextLines();
                if (lines != null) {
                    StringBuilder lineString = new StringBuilder();
                    for (String s : lines) {
                        lineString.append(s).append("\n");
                    }
                    this.markdownRenderer.setText(lineString.toString());
                    this.textSet = true;
                }
            }
        }
        RenderSystem.enableBlend();
        graphics.fill(0, 0, this.f_96543_, this.f_96544_, UIBase.getUIColorTheme().screen_background_color_darker.getColorInt());
        RenderingUtils.resetShaderColor(graphics);
        graphics.fill(0, this.f_96544_ - this.footerHeight, this.f_96543_, this.f_96544_, UIBase.getUIColorTheme().area_background_color.getColorInt());
        RenderingUtils.resetShaderColor(graphics);
        super.render(graphics, mouseX, mouseY, partial);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollDelta) {
        return this.markdownRenderer.mouseScrolled(mouseX, mouseY, scrollDelta);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return this.markdownRenderer.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public void onClose() {
        Minecraft.getInstance().setScreen(this.parent);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }
}