package de.keksuccino.fancymenu.customization.layout.editor;

import com.mojang.blaze3d.systems.RenderSystem;
import de.keksuccino.fancymenu.customization.animation.AdvancedAnimation;
import de.keksuccino.fancymenu.customization.animation.AnimationHandler;
import de.keksuccino.fancymenu.util.LocalizationUtils;
import de.keksuccino.fancymenu.util.rendering.AspectRatio;
import de.keksuccino.fancymenu.util.rendering.ui.UIBase;
import de.keksuccino.fancymenu.util.rendering.ui.scroll.v1.scrollarea.ScrollArea;
import de.keksuccino.fancymenu.util.rendering.ui.scroll.v1.scrollarea.entry.ScrollAreaEntry;
import de.keksuccino.fancymenu.util.rendering.ui.scroll.v1.scrollarea.entry.TextListScrollAreaEntry;
import de.keksuccino.fancymenu.util.rendering.ui.scroll.v1.scrollarea.entry.TextScrollAreaEntry;
import de.keksuccino.fancymenu.util.rendering.ui.tooltip.Tooltip;
import de.keksuccino.fancymenu.util.rendering.ui.tooltip.TooltipHandler;
import de.keksuccino.fancymenu.util.rendering.ui.widget.button.ExtendedButton;
import de.keksuccino.konkrete.rendering.animation.IAnimationRenderer;
import java.util.function.Consumer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ChooseAnimationScreen extends Screen {

    protected Consumer<String> callback;

    protected String selectedAnimationName = null;

    protected IAnimationRenderer selectedAnimation = null;

    protected ScrollArea animationListScrollArea = new ScrollArea(0, 0, 0, 0);

    protected ExtendedButton doneButton;

    protected ExtendedButton cancelButton;

    public ChooseAnimationScreen(@Nullable String preSelectedAnimation, @NotNull Consumer<String> callback) {
        super(Component.translatable("fancymenu.animation.choose"));
        this.callback = callback;
        this.updateAnimationScrollAreaContent();
        if (preSelectedAnimation != null) {
            for (ScrollAreaEntry e : this.animationListScrollArea.getEntries()) {
                if (e instanceof ChooseAnimationScreen.AnimationScrollEntry a && a.animation.equals(preSelectedAnimation)) {
                    a.setSelected(true);
                    this.setSelectedAnimation(a);
                    break;
                }
            }
        }
    }

    @Override
    protected void init() {
        super.init();
        this.doneButton = new ExtendedButton(0, 0, 150, 20, Component.translatable("fancymenu.guicomponents.done"), button -> this.callback.accept(this.selectedAnimationName)) {

            @Override
            public void renderWidget(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {
                if (ChooseAnimationScreen.this.selectedAnimationName == null) {
                    TooltipHandler.INSTANCE.addWidgetTooltip(this, Tooltip.of(LocalizationUtils.splitLocalizedLines("fancymenu.animation.choose.no_animation_selected")).setDefaultStyle(), false, true);
                    this.f_93623_ = false;
                } else {
                    this.f_93623_ = true;
                }
                super.renderWidget(graphics, mouseX, mouseY, partial);
            }
        };
        this.m_7787_(this.doneButton);
        UIBase.applyDefaultWidgetSkinTo(this.doneButton);
        this.cancelButton = new ExtendedButton(0, 0, 150, 20, Component.translatable("fancymenu.guicomponents.cancel"), button -> this.callback.accept(null));
        this.m_7787_(this.cancelButton);
        UIBase.applyDefaultWidgetSkinTo(this.cancelButton);
    }

    @Override
    public void onClose() {
        this.callback.accept(null);
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {
        RenderSystem.enableBlend();
        graphics.fill(0, 0, this.f_96543_, this.f_96544_, UIBase.getUIColorTheme().screen_background_color.getColorInt());
        Component titleComp = this.f_96539_.copy().withStyle(Style.EMPTY.withBold(true));
        graphics.drawString(this.f_96547_, titleComp, 20, 20, UIBase.getUIColorTheme().generic_text_base_color.getColorInt(), false);
        graphics.drawString(this.f_96547_, Component.translatable("fancymenu.animation.choose.available_animations"), 20, 50, UIBase.getUIColorTheme().generic_text_base_color.getColorInt(), false);
        this.animationListScrollArea.setWidth(this.f_96543_ / 2 - 40, true);
        this.animationListScrollArea.setHeight(this.f_96544_ - 85, true);
        this.animationListScrollArea.setX(20, true);
        this.animationListScrollArea.setY(65, true);
        this.animationListScrollArea.render(graphics, mouseX, mouseY, partial);
        Component previewLabel = Component.translatable("fancymenu.animation.choose.preview");
        int previewLabelWidth = this.f_96547_.width(previewLabel);
        graphics.drawString(this.f_96547_, previewLabel, this.f_96543_ - 20 - previewLabelWidth, 50, UIBase.getUIColorTheme().generic_text_base_color.getColorInt(), false);
        if (this.selectedAnimation != null) {
            int aniW = this.f_96543_ / 2 - 40;
            int aniH = this.f_96544_ / 2;
            AspectRatio ratio = new AspectRatio(this.selectedAnimation.getWidth(), this.selectedAnimation.getHeight());
            int[] size = ratio.getAspectRatioSizeByMaximumSize(aniW, aniH);
            aniW = size[0];
            aniH = size[1];
            int aniX = this.f_96543_ - 20 - aniW;
            int aniY = 65;
            boolean cachedLooped = this.selectedAnimation.isGettingLooped();
            graphics.fill(aniX, aniY, aniX + aniW, aniY + aniH, UIBase.getUIColorTheme().area_background_color.getColorInt());
            this.selectedAnimation.setLooped(false);
            this.selectedAnimation.setPosX(aniX);
            this.selectedAnimation.setPosY(aniY);
            this.selectedAnimation.setWidth(aniW);
            this.selectedAnimation.setHeight(aniH);
            this.selectedAnimation.render(graphics);
            this.selectedAnimation.setLooped(cachedLooped);
            UIBase.renderBorder(graphics, aniX, aniY, aniX + aniW, aniY + aniH, 1, UIBase.getUIColorTheme().element_border_color_normal.getColor(), true, true, true, true);
        }
        this.doneButton.m_252865_(this.f_96543_ - 20 - this.doneButton.m_5711_());
        this.doneButton.m_253211_(this.f_96544_ - 20 - 20);
        this.doneButton.render(graphics, mouseX, mouseY, partial);
        this.cancelButton.m_252865_(this.f_96543_ - 20 - this.cancelButton.m_5711_());
        this.cancelButton.m_253211_(this.doneButton.m_252907_() - 5 - 20);
        this.cancelButton.render(graphics, mouseX, mouseY, partial);
        super.render(graphics, mouseX, mouseY, partial);
    }

    protected void setSelectedAnimation(@Nullable ChooseAnimationScreen.AnimationScrollEntry entry) {
        if (this.selectedAnimation != null) {
            this.selectedAnimation.resetAnimation();
            if (this.selectedAnimation instanceof AdvancedAnimation) {
                ((AdvancedAnimation) this.selectedAnimation).stopAudio();
                ((AdvancedAnimation) this.selectedAnimation).resetAudio();
            }
        }
        if (entry == null) {
            this.selectedAnimation = null;
            this.selectedAnimationName = null;
        } else {
            this.selectedAnimationName = entry.animation;
            this.selectedAnimation = AnimationHandler.getAnimation(entry.animation);
            if (this.selectedAnimation != null) {
                this.selectedAnimation.resetAnimation();
                if (this.selectedAnimation instanceof AdvancedAnimation) {
                    ((AdvancedAnimation) this.selectedAnimation).stopAudio();
                    ((AdvancedAnimation) this.selectedAnimation).resetAudio();
                }
            }
        }
    }

    protected void updateAnimationScrollAreaContent() {
        this.animationListScrollArea.clearEntries();
        for (String s : AnimationHandler.getExternalAnimationNames()) {
            ChooseAnimationScreen.AnimationScrollEntry e = new ChooseAnimationScreen.AnimationScrollEntry(this.animationListScrollArea, s, entry -> this.setSelectedAnimation((ChooseAnimationScreen.AnimationScrollEntry) entry));
            this.animationListScrollArea.addEntry(e);
        }
        if (this.animationListScrollArea.getEntries().isEmpty()) {
            this.animationListScrollArea.addEntry(new TextScrollAreaEntry(this.animationListScrollArea, Component.translatable("fancymenu.animation.choose.no_animations"), entry -> {
            }));
        }
        int totalWidth = this.animationListScrollArea.getTotalEntryWidth();
        for (ScrollAreaEntry e : this.animationListScrollArea.getEntries()) {
            e.setWidth(totalWidth);
        }
    }

    @Override
    public boolean keyPressed(int button, int $$1, int $$2) {
        if (button == 257 && this.selectedAnimationName != null) {
            this.callback.accept(this.selectedAnimationName);
            return true;
        } else {
            return super.keyPressed(button, $$1, $$2);
        }
    }

    public static class AnimationScrollEntry extends TextListScrollAreaEntry {

        public String animation;

        public AnimationScrollEntry(ScrollArea parent, @NotNull String animation, @NotNull Consumer<TextListScrollAreaEntry> onClick) {
            super(parent, Component.literal(animation).setStyle(Style.EMPTY.withColor(UIBase.getUIColorTheme().description_area_text_color.getColorInt())), UIBase.getUIColorTheme().listing_dot_color_1.getColor(), onClick);
            this.animation = animation;
        }
    }
}