package de.keksuccino.fancymenu.customization.layout.editor;

import com.mojang.blaze3d.systems.RenderSystem;
import de.keksuccino.fancymenu.customization.panorama.PanoramaHandler;
import de.keksuccino.fancymenu.util.LocalizationUtils;
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

public class ChoosePanoramaScreen extends Screen {

    protected Consumer<String> callback;

    protected String selectedPanoramaName = null;

    protected ScrollArea panoramaListScrollArea = new ScrollArea(0, 0, 0, 0);

    protected ExtendedButton doneButton;

    protected ExtendedButton cancelButton;

    public ChoosePanoramaScreen(@Nullable String preSelectedPanorama, @NotNull Consumer<String> callback) {
        super(Component.translatable("fancymenu.panorama.choose"));
        this.callback = callback;
        this.updatePanoramaScrollAreaContent();
        if (preSelectedPanorama != null) {
            for (ScrollAreaEntry e : this.panoramaListScrollArea.getEntries()) {
                if (e instanceof ChoosePanoramaScreen.PanoramaScrollEntry a && a.panorama.equals(preSelectedPanorama)) {
                    a.setSelected(true);
                    this.setSelectedPanorama(a);
                    break;
                }
            }
        }
    }

    @Override
    protected void init() {
        super.init();
        this.doneButton = new ExtendedButton(0, 0, 150, 20, Component.translatable("fancymenu.guicomponents.done"), button -> this.callback.accept(this.selectedPanoramaName)) {

            @Override
            public void renderWidget(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {
                if (ChoosePanoramaScreen.this.selectedPanoramaName == null) {
                    TooltipHandler.INSTANCE.addWidgetTooltip(this, Tooltip.of(LocalizationUtils.splitLocalizedLines("fancymenu.panorama.choose.no_panorama_selected")).setDefaultStyle(), false, true);
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
        graphics.drawString(this.f_96547_, Component.translatable("fancymenu.panorama.choose.available_panoramas"), 20, 50, UIBase.getUIColorTheme().generic_text_base_color.getColorInt(), false);
        this.panoramaListScrollArea.setWidth(this.f_96543_ / 2 - 40, true);
        this.panoramaListScrollArea.setHeight(this.f_96544_ - 85, true);
        this.panoramaListScrollArea.setX(20, true);
        this.panoramaListScrollArea.setY(65, true);
        this.panoramaListScrollArea.render(graphics, mouseX, mouseY, partial);
        this.doneButton.m_252865_(this.f_96543_ - 20 - this.doneButton.m_5711_());
        this.doneButton.m_253211_(this.f_96544_ - 20 - 20);
        this.doneButton.render(graphics, mouseX, mouseY, partial);
        this.cancelButton.m_252865_(this.f_96543_ - 20 - this.cancelButton.m_5711_());
        this.cancelButton.m_253211_(this.doneButton.m_252907_() - 5 - 20);
        this.cancelButton.render(graphics, mouseX, mouseY, partial);
        super.render(graphics, mouseX, mouseY, partial);
    }

    protected void setSelectedPanorama(@Nullable ChoosePanoramaScreen.PanoramaScrollEntry entry) {
        if (entry == null) {
            this.selectedPanoramaName = null;
        } else {
            this.selectedPanoramaName = entry.panorama;
        }
    }

    protected void updatePanoramaScrollAreaContent() {
        this.panoramaListScrollArea.clearEntries();
        for (String s : PanoramaHandler.getPanoramaNames()) {
            ChoosePanoramaScreen.PanoramaScrollEntry e = new ChoosePanoramaScreen.PanoramaScrollEntry(this.panoramaListScrollArea, s, entry -> this.setSelectedPanorama((ChoosePanoramaScreen.PanoramaScrollEntry) entry));
            this.panoramaListScrollArea.addEntry(e);
        }
        if (this.panoramaListScrollArea.getEntries().isEmpty()) {
            this.panoramaListScrollArea.addEntry(new TextScrollAreaEntry(this.panoramaListScrollArea, Component.translatable("fancymenu.panorama.choose.no_panoramas"), entry -> {
            }));
        }
        int totalWidth = this.panoramaListScrollArea.getTotalEntryWidth();
        for (ScrollAreaEntry e : this.panoramaListScrollArea.getEntries()) {
            e.setWidth(totalWidth);
        }
    }

    @Override
    public boolean keyPressed(int button, int $$1, int $$2) {
        if (button == 257 && this.selectedPanoramaName != null) {
            this.callback.accept(this.selectedPanoramaName);
            return true;
        } else {
            return super.keyPressed(button, $$1, $$2);
        }
    }

    public static class PanoramaScrollEntry extends TextListScrollAreaEntry {

        public String panorama;

        public PanoramaScrollEntry(ScrollArea parent, @NotNull String panorama, @NotNull Consumer<TextListScrollAreaEntry> onClick) {
            super(parent, Component.literal(panorama).setStyle(Style.EMPTY.withColor(UIBase.getUIColorTheme().description_area_text_color.getColorInt())), UIBase.getUIColorTheme().listing_dot_color_1.getColor(), onClick);
            this.panorama = panorama;
        }
    }
}