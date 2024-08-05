package de.keksuccino.fancymenu.customization.layout.editor;

import com.mojang.blaze3d.systems.RenderSystem;
import de.keksuccino.fancymenu.customization.slideshow.ExternalTextureSlideshowRenderer;
import de.keksuccino.fancymenu.customization.slideshow.SlideshowHandler;
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
import java.util.function.Consumer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ChooseSlideshowScreen extends Screen {

    protected Consumer<String> callback;

    protected String selectedSlideshowName = null;

    protected ExternalTextureSlideshowRenderer selectedSlideshow = null;

    protected ScrollArea slideshowListScrollArea = new ScrollArea(0, 0, 0, 0);

    protected ExtendedButton doneButton;

    protected ExtendedButton cancelButton;

    public ChooseSlideshowScreen(@Nullable String preSelectedSlideshow, @NotNull Consumer<String> callback) {
        super(Component.translatable("fancymenu.slideshow.choose"));
        this.callback = callback;
        this.updateSlideshowScrollAreaContent();
        if (preSelectedSlideshow != null) {
            for (ScrollAreaEntry e : this.slideshowListScrollArea.getEntries()) {
                if (e instanceof ChooseSlideshowScreen.SlideshowScrollEntry a && a.slideshow.equals(preSelectedSlideshow)) {
                    a.setSelected(true);
                    this.setSelectedSlideshow(a);
                    break;
                }
            }
        }
    }

    @Override
    protected void init() {
        super.init();
        this.doneButton = new ExtendedButton(0, 0, 150, 20, Component.translatable("fancymenu.guicomponents.done"), button -> this.callback.accept(this.selectedSlideshowName)) {

            @Override
            public void renderWidget(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {
                if (ChooseSlideshowScreen.this.selectedSlideshowName == null) {
                    TooltipHandler.INSTANCE.addWidgetTooltip(this, Tooltip.of(LocalizationUtils.splitLocalizedLines("fancymenu.slideshow.choose.no_slideshow_selected")).setDefaultStyle(), false, true);
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
        graphics.drawString(this.f_96547_, Component.translatable("fancymenu.slideshow.choose.available_slideshows"), 20, 50, UIBase.getUIColorTheme().generic_text_base_color.getColorInt(), false);
        this.slideshowListScrollArea.setWidth(this.f_96543_ / 2 - 40, true);
        this.slideshowListScrollArea.setHeight(this.f_96544_ - 85, true);
        this.slideshowListScrollArea.setX(20, true);
        this.slideshowListScrollArea.setY(65, true);
        this.slideshowListScrollArea.render(graphics, mouseX, mouseY, partial);
        Component previewLabel = Component.translatable("fancymenu.slideshow.choose.preview");
        int previewLabelWidth = this.f_96547_.width(previewLabel);
        graphics.drawString(this.f_96547_, previewLabel, this.f_96543_ - 20 - previewLabelWidth, 50, UIBase.getUIColorTheme().generic_text_base_color.getColorInt(), false);
        if (this.selectedSlideshow != null) {
            int slideW = this.f_96543_ / 2 - 40;
            int slideH = this.f_96544_ / 2;
            AspectRatio ratio = new AspectRatio(this.selectedSlideshow.getImageWidth(), this.selectedSlideshow.getImageHeight());
            int[] size = ratio.getAspectRatioSizeByMaximumSize(slideW, slideH);
            slideW = size[0];
            slideH = size[1];
            int slideX = this.f_96543_ - 20 - slideW;
            int slideY = 65;
            graphics.fill(slideX, slideY, slideX + slideW, slideY + slideH, UIBase.getUIColorTheme().area_background_color.getColorInt());
            this.selectedSlideshow.x = slideX;
            this.selectedSlideshow.y = slideY;
            this.selectedSlideshow.width = slideW;
            this.selectedSlideshow.height = slideH;
            this.selectedSlideshow.render(graphics);
            UIBase.renderBorder(graphics, slideX, slideY, slideX + slideW, slideY + slideH, 1, UIBase.getUIColorTheme().element_border_color_normal.getColor(), true, true, true, true);
        }
        this.doneButton.m_252865_(this.f_96543_ - 20 - this.doneButton.m_5711_());
        this.doneButton.m_253211_(this.f_96544_ - 20 - 20);
        this.doneButton.render(graphics, mouseX, mouseY, partial);
        this.cancelButton.m_252865_(this.f_96543_ - 20 - this.cancelButton.m_5711_());
        this.cancelButton.m_253211_(this.doneButton.m_252907_() - 5 - 20);
        this.cancelButton.render(graphics, mouseX, mouseY, partial);
        super.render(graphics, mouseX, mouseY, partial);
    }

    protected void setSelectedSlideshow(@Nullable ChooseSlideshowScreen.SlideshowScrollEntry entry) {
        if (entry == null) {
            this.selectedSlideshow = null;
            this.selectedSlideshowName = null;
        } else {
            this.selectedSlideshowName = entry.slideshow;
            this.selectedSlideshow = SlideshowHandler.getSlideshow(entry.slideshow);
            if (this.selectedSlideshow != null && !this.selectedSlideshow.isReady()) {
                this.selectedSlideshow.prepareSlideshow();
            }
        }
    }

    protected void updateSlideshowScrollAreaContent() {
        this.slideshowListScrollArea.clearEntries();
        for (String s : SlideshowHandler.getSlideshowNames()) {
            ChooseSlideshowScreen.SlideshowScrollEntry e = new ChooseSlideshowScreen.SlideshowScrollEntry(this.slideshowListScrollArea, s, entry -> this.setSelectedSlideshow((ChooseSlideshowScreen.SlideshowScrollEntry) entry));
            this.slideshowListScrollArea.addEntry(e);
        }
        if (this.slideshowListScrollArea.getEntries().isEmpty()) {
            this.slideshowListScrollArea.addEntry(new TextScrollAreaEntry(this.slideshowListScrollArea, Component.translatable("fancymenu.slideshow.choose.no_slideshows"), entry -> {
            }));
        }
        int totalWidth = this.slideshowListScrollArea.getTotalEntryWidth();
        for (ScrollAreaEntry e : this.slideshowListScrollArea.getEntries()) {
            e.setWidth(totalWidth);
        }
    }

    @Override
    public boolean keyPressed(int button, int $$1, int $$2) {
        if (button == 257 && this.selectedSlideshowName != null) {
            this.callback.accept(this.selectedSlideshowName);
            return true;
        } else {
            return super.keyPressed(button, $$1, $$2);
        }
    }

    public static class SlideshowScrollEntry extends TextListScrollAreaEntry {

        public String slideshow;

        public SlideshowScrollEntry(ScrollArea parent, @NotNull String slideshow, @NotNull Consumer<TextListScrollAreaEntry> onClick) {
            super(parent, Component.literal(slideshow).setStyle(Style.EMPTY.withColor(UIBase.getUIColorTheme().description_area_text_color.getColorInt())), UIBase.getUIColorTheme().listing_dot_color_1.getColor(), onClick);
            this.slideshow = slideshow;
        }
    }
}