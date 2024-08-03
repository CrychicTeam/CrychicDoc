package com.simibubi.create.foundation.ponder.ui;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllItems;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.gui.ScreenOpener;
import com.simibubi.create.foundation.gui.Theme;
import com.simibubi.create.foundation.gui.UIRenderHelper;
import com.simibubi.create.foundation.gui.element.BoxElement;
import com.simibubi.create.foundation.gui.element.GuiGameElement;
import com.simibubi.create.foundation.gui.element.RenderElement;
import com.simibubi.create.foundation.gui.widget.ElementWidget;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.ponder.PonderRegistry;
import com.simibubi.create.foundation.ponder.PonderTag;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.FontHelper;
import com.simibubi.create.foundation.utility.Lang;
import java.util.List;
import java.util.Optional;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public class PonderTagIndexScreen extends NavigatableSimiScreen {

    public static final String EXIT = "ponder.exit";

    public static final String TITLE = "ponder.index_title";

    public static final String WELCOME = "ponder.welcome";

    public static final String CATEGORIES = "ponder.categories";

    public static final String DESCRIPTION = "ponder.index_description";

    private final double itemXmult = 0.5;

    protected Rect2i itemArea;

    protected Rect2i chapterArea;

    private final double mainYmult = 0.15;

    private PonderTag hoveredItem = null;

    @Override
    protected void init() {
        super.init();
        List<PonderTag> tags = PonderRegistry.TAGS.getListedTags();
        int rowCount = Mth.clamp((int) Math.ceil((double) tags.size() / 11.0), 1, 3);
        LayoutHelper layout = LayoutHelper.centeredHorizontal(tags.size(), rowCount, 28, 28, 8);
        this.itemArea = layout.getArea();
        int itemCenterX = (int) ((double) this.f_96543_ * 0.5);
        int itemCenterY = this.getItemsY();
        for (PonderTag i : tags) {
            PonderButton b = new PonderButton(itemCenterX + layout.getX() + 4, itemCenterY + layout.getY() + 4).<PonderButton>showingTag(i).withCallback((mouseX, mouseY) -> {
                this.centerScalingOn(mouseX, mouseY);
                ScreenOpener.transitionTo(new PonderTagScreen(i));
            });
            this.m_142416_(b);
            layout.next();
        }
        this.m_142416_(this.backTrack = new PonderButton(31, this.f_96544_ - 31 - 20).<ElementWidget>enableFade(0, 5).<ElementWidget>showing(AllIcons.I_MTD_CLOSE).withCallback(() -> ScreenOpener.openPreviousScreen(this, Optional.empty())));
        this.backTrack.fade(1.0F);
    }

    @Override
    protected void initBackTrackIcon(PonderButton backTrack) {
        backTrack.showing(AllItems.WRENCH.asStack());
    }

    @Override
    public void tick() {
        super.tick();
        PonderUI.ponderTicks++;
        this.hoveredItem = null;
        Window w = this.f_96541_.getWindow();
        double mouseX = this.f_96541_.mouseHandler.xpos() * (double) w.getGuiScaledWidth() / (double) w.getScreenWidth();
        double mouseY = this.f_96541_.mouseHandler.ypos() * (double) w.getGuiScaledHeight() / (double) w.getScreenHeight();
        for (GuiEventListener child : this.m_6702_()) {
            if (child != this.backTrack && child instanceof PonderButton) {
                PonderButton button = (PonderButton) child;
                if (button.m_5953_(mouseX, mouseY)) {
                    this.hoveredItem = button.getTag();
                }
            }
        }
    }

    @Override
    protected String backTrackingLangKey() {
        return "ponder.exit";
    }

    @Override
    protected void renderWindow(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.renderItems(graphics, mouseX, mouseY, partialTicks);
        PoseStack ms = graphics.pose();
        ms.pushPose();
        ms.translate((double) (this.f_96543_ / 2 - 120), (double) this.f_96544_ * 0.15 - 40.0, 0.0);
        ms.pushPose();
        int x = 59;
        int y = 31;
        String title = Lang.translateDirect("ponder.welcome").getString();
        int streakHeight = 35;
        UIRenderHelper.streak(graphics, 0.0F, x - 4, y - 12 + streakHeight / 2, streakHeight, 240);
        new BoxElement().<BoxElement>withBackground(Theme.c(Theme.Key.PONDER_BACKGROUND_FLAT)).<BoxElement>gradientBorder(Theme.p(Theme.Key.PONDER_IDLE)).<RenderElement>at(21.0F, 21.0F, 100.0F).<RenderElement>withBounds(30, 30).render(graphics);
        graphics.drawString(this.f_96547_, title, x + 8, y + 1, Theme.i(Theme.Key.TEXT), false);
        ms.popPose();
        ms.pushPose();
        ms.translate(23.0F, 23.0F, 10.0F);
        ms.scale(1.66F, 1.66F, 1.66F);
        ms.translate(-4.0F, -4.0F, 0.0F);
        ms.scale(1.5F, 1.5F, 1.5F);
        GuiGameElement.of(AllItems.WRENCH.asStack()).render(graphics);
        ms.popPose();
        ms.popPose();
        ms.pushPose();
        int w = (int) ((double) this.f_96543_ * 0.45);
        x = (this.f_96543_ - w) / 2;
        y = this.getItemsY() - 10 + Math.max(this.itemArea.getHeight(), 48);
        String desc = Lang.translateDirect("ponder.index_description").getString();
        int h = this.f_96547_.wordWrapHeight(desc, w);
        new BoxElement().<BoxElement>withBackground(Theme.c(Theme.Key.PONDER_BACKGROUND_FLAT)).<BoxElement>gradientBorder(Theme.p(Theme.Key.PONDER_IDLE)).<RenderElement>at((float) (x - 3), (float) (y - 3), 90.0F).<RenderElement>withBounds(w + 6, h + 6).render(graphics);
        ms.translate(0.0F, 0.0F, 100.0F);
        FontHelper.drawSplitString(ms, this.f_96547_, desc, x, y, w, Theme.i(Theme.Key.TEXT));
        ms.popPose();
    }

    protected void renderItems(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        List<PonderTag> tags = PonderRegistry.TAGS.getListedTags();
        if (!tags.isEmpty()) {
            int x = (int) ((double) this.f_96543_ * 0.5);
            int y = this.getItemsY();
            String relatedTitle = Lang.translateDirect("ponder.categories").getString();
            int stringWidth = this.f_96547_.width(relatedTitle);
            PoseStack ms = graphics.pose();
            ms.pushPose();
            ms.translate((float) x, (float) y, 0.0F);
            new BoxElement().<BoxElement>withBackground(Theme.c(Theme.Key.PONDER_BACKGROUND_FLAT)).<BoxElement>gradientBorder(Theme.p(Theme.Key.PONDER_IDLE)).<RenderElement>at((float) (this.windowWidth - stringWidth) / 2.0F - 5.0F, (float) (this.itemArea.getY() - 21), 100.0F).<RenderElement>withBounds(stringWidth + 10, 10).render(graphics);
            ms.translate(0.0F, 0.0F, 200.0F);
            graphics.drawCenteredString(this.f_96547_, relatedTitle, this.windowWidth / 2, this.itemArea.getY() - 20, Theme.i(Theme.Key.TEXT));
            ms.translate(0.0F, 0.0F, -200.0F);
            UIRenderHelper.streak(graphics, 0.0F, 0, 0, this.itemArea.getHeight() + 10, this.itemArea.getWidth() / 2 + 75);
            UIRenderHelper.streak(graphics, 180.0F, 0, 0, this.itemArea.getHeight() + 10, this.itemArea.getWidth() / 2 + 75);
            ms.popPose();
        }
    }

    public int getItemsY() {
        return (int) (0.15 * (double) this.f_96544_ + 85.0);
    }

    @Override
    protected void renderWindowForeground(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        RenderSystem.disableDepthTest();
        PoseStack ms = graphics.pose();
        ms.pushPose();
        ms.translate(0.0F, 0.0F, 200.0F);
        if (this.hoveredItem != null) {
            List<Component> list = TooltipHelper.cutStringTextComponent(this.hoveredItem.getDescription(), TooltipHelper.Palette.ALL_GRAY);
            list.add(0, Components.literal(this.hoveredItem.getTitle()));
            graphics.renderComponentTooltip(this.f_96547_, list, mouseX, mouseY);
        }
        ms.popPose();
        RenderSystem.enableDepthTest();
    }

    @Override
    protected String getBreadcrumbTitle() {
        return Lang.translateDirect("ponder.index_title").getString();
    }

    @Override
    public boolean isPauseScreen() {
        return true;
    }

    @Override
    public void removed() {
        super.m_7861_();
        this.hoveredItem = null;
    }
}