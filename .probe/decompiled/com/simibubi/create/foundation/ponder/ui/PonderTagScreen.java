package com.simibubi.create.foundation.ponder.ui;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.gui.ScreenOpener;
import com.simibubi.create.foundation.gui.Theme;
import com.simibubi.create.foundation.gui.UIRenderHelper;
import com.simibubi.create.foundation.gui.element.BoxElement;
import com.simibubi.create.foundation.gui.element.RenderElement;
import com.simibubi.create.foundation.gui.widget.BoxWidget;
import com.simibubi.create.foundation.ponder.PonderChapter;
import com.simibubi.create.foundation.ponder.PonderRegistry;
import com.simibubi.create.foundation.ponder.PonderTag;
import com.simibubi.create.foundation.utility.FontHelper;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.RegisteredObjects;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

public class PonderTagScreen extends NavigatableSimiScreen {

    public static final String ASSOCIATED = "ponder.associated";

    private final PonderTag tag;

    protected final List<Item> items;

    private final double itemXmult = 0.5;

    protected Rect2i itemArea;

    protected final List<PonderChapter> chapters;

    private final double chapterXmult = 0.5;

    private final double chapterYmult = 0.75;

    protected Rect2i chapterArea;

    private final double mainYmult = 0.15;

    private ItemStack hoveredItem = ItemStack.EMPTY;

    public PonderTagScreen(PonderTag tag) {
        this.tag = tag;
        this.items = new ArrayList();
        this.chapters = new ArrayList();
    }

    @Override
    protected void init() {
        super.init();
        this.items.clear();
        PonderRegistry.TAGS.getItems(this.tag).stream().map(key -> {
            Item item = ForgeRegistries.ITEMS.getValue(key);
            if (item == null) {
                Block bx = ForgeRegistries.BLOCKS.getValue(key);
                if (bx != null) {
                    item = bx.asItem();
                }
            }
            return item;
        }).filter(Objects::nonNull).forEach(this.items::add);
        if (!this.tag.getMainItem().isEmpty()) {
            this.items.remove(this.tag.getMainItem().getItem());
        }
        int rowCount = Mth.clamp((int) Math.ceil((double) this.items.size() / 11.0), 1, 3);
        LayoutHelper layout = LayoutHelper.centeredHorizontal(this.items.size(), rowCount, 28, 28, 8);
        this.itemArea = layout.getArea();
        int itemCenterX = (int) ((double) this.f_96543_ * 0.5);
        int itemCenterY = this.getItemsY();
        for (Item i : this.items) {
            PonderButton b = new PonderButton(itemCenterX + layout.getX() + 4, itemCenterY + layout.getY() + 4).showing(new ItemStack(i));
            if (PonderRegistry.ALL.containsKey(RegisteredObjects.getKeyOrThrow(i))) {
                b.withCallback((mouseX, mouseY) -> {
                    this.centerScalingOn(mouseX, mouseY);
                    ScreenOpener.transitionTo(PonderUI.of(new ItemStack(i), this.tag));
                });
            } else if (RegisteredObjects.getKeyOrThrow(i).getNamespace().equals("create")) {
                b.<BoxWidget>withBorderColors(Theme.p(Theme.Key.PONDER_MISSING_CREATE)).animateColors(false);
            } else {
                b.<BoxWidget>withBorderColors(Theme.p(Theme.Key.PONDER_MISSING_VANILLA)).animateColors(false);
            }
            this.m_142416_(b);
            layout.next();
        }
        if (!this.tag.getMainItem().isEmpty()) {
            ResourceLocation registryName = RegisteredObjects.getKeyOrThrow(this.tag.getMainItem().getItem());
            PonderButton b = new PonderButton(itemCenterX - layout.getTotalWidth() / 2 - 48, itemCenterY - 10).showing(this.tag.getMainItem());
            b.withCustomBackground(Theme.c(Theme.Key.PONDER_BACKGROUND_IMPORTANT));
            if (PonderRegistry.ALL.containsKey(registryName)) {
                b.withCallback((mouseX, mouseY) -> {
                    this.centerScalingOn(mouseX, mouseY);
                    ScreenOpener.transitionTo(PonderUI.of(this.tag.getMainItem(), this.tag));
                });
            } else if (registryName.getNamespace().equals("create")) {
                b.<BoxWidget>withBorderColors(Theme.p(Theme.Key.PONDER_MISSING_CREATE)).animateColors(false);
            } else {
                b.<BoxWidget>withBorderColors(Theme.p(Theme.Key.PONDER_MISSING_VANILLA)).animateColors(false);
            }
            this.m_142416_(b);
        }
        this.chapters.clear();
        this.chapters.addAll(PonderRegistry.TAGS.getChapters(this.tag));
        rowCount = Mth.clamp((int) Math.ceil((double) ((float) this.chapters.size() / 3.0F)), 1, 3);
        layout = LayoutHelper.centeredHorizontal(this.chapters.size(), rowCount, 200, 38, 16);
        this.chapterArea = layout.getArea();
        int chapterCenterX = (int) ((double) this.f_96543_ * 0.5);
        int chapterCenterY = (int) ((double) this.f_96544_ * 0.75);
        for (PonderChapter chapter : this.chapters) {
            ChapterLabel label = new ChapterLabel(chapter, chapterCenterX + layout.getX(), chapterCenterY + layout.getY(), (mouseX, mouseY) -> {
                this.centerScalingOn(mouseX, mouseY);
                ScreenOpener.transitionTo(PonderUI.of(chapter));
            });
            this.m_142416_(label);
            layout.next();
        }
    }

    @Override
    protected void initBackTrackIcon(PonderButton backTrack) {
        backTrack.showing(this.tag);
    }

    @Override
    public void tick() {
        super.tick();
        PonderUI.ponderTicks++;
        this.hoveredItem = ItemStack.EMPTY;
        Window w = this.f_96541_.getWindow();
        double mouseX = this.f_96541_.mouseHandler.xpos() * (double) w.getGuiScaledWidth() / (double) w.getScreenWidth();
        double mouseY = this.f_96541_.mouseHandler.ypos() * (double) w.getGuiScaledHeight() / (double) w.getScreenHeight();
        for (GuiEventListener child : this.m_6702_()) {
            if (child != this.backTrack && child instanceof PonderButton) {
                PonderButton button = (PonderButton) child;
                if (button.m_5953_(mouseX, mouseY)) {
                    this.hoveredItem = button.getItem();
                }
            }
        }
    }

    @Override
    protected void renderWindow(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.renderItems(graphics, mouseX, mouseY, partialTicks);
        this.renderChapters(graphics, mouseX, mouseY, partialTicks);
        PoseStack ms = graphics.pose();
        ms.pushPose();
        ms.translate((double) (this.f_96543_ / 2 - 120), (double) this.f_96544_ * 0.15 - 40.0, 0.0);
        ms.pushPose();
        int x = 59;
        int y = 31;
        String title = this.tag.getTitle();
        int streakHeight = 35;
        UIRenderHelper.streak(graphics, 0.0F, x - 4, y - 12 + streakHeight / 2, streakHeight, 240);
        new BoxElement().<BoxElement>withBackground(Theme.c(Theme.Key.PONDER_BACKGROUND_FLAT)).<BoxElement>gradientBorder(Theme.p(Theme.Key.PONDER_IDLE)).<RenderElement>at(21.0F, 21.0F, 100.0F).<RenderElement>withBounds(30, 30).render(graphics);
        graphics.drawString(this.f_96547_, Lang.translateDirect("ponder.pondering"), x, y - 6, Theme.i(Theme.Key.TEXT_DARKER), false);
        y += 8;
        x += 0;
        ms.translate((float) x, (float) y, 0.0F);
        ms.translate(0.0F, 0.0F, 5.0F);
        graphics.drawString(this.f_96547_, title, 0, 0, Theme.i(Theme.Key.TEXT), false);
        ms.popPose();
        ms.pushPose();
        ms.translate(23.0F, 23.0F, 10.0F);
        ms.scale(1.66F, 1.66F, 1.66F);
        this.tag.render(graphics, 0, 0);
        ms.popPose();
        ms.popPose();
        ms.pushPose();
        int w = (int) ((double) this.f_96543_ * 0.45);
        x = (this.f_96543_ - w) / 2;
        y = this.getItemsY() - 10 + Math.max(this.itemArea.getHeight(), 48);
        String desc = this.tag.getDescription();
        int h = this.f_96547_.wordWrapHeight(desc, w);
        new BoxElement().<BoxElement>withBackground(Theme.c(Theme.Key.PONDER_BACKGROUND_FLAT)).<BoxElement>gradientBorder(Theme.p(Theme.Key.PONDER_IDLE)).<RenderElement>at((float) (x - 3), (float) (y - 3), 90.0F).<RenderElement>withBounds(w + 6, h + 6).render(graphics);
        ms.translate(0.0F, 0.0F, 100.0F);
        FontHelper.drawSplitString(ms, this.f_96547_, desc, x, y, w, Theme.i(Theme.Key.TEXT));
        ms.popPose();
    }

    protected void renderItems(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        if (!this.items.isEmpty()) {
            int x = (int) ((double) this.f_96543_ * 0.5);
            int y = this.getItemsY();
            String relatedTitle = Lang.translateDirect("ponder.associated").getString();
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

    protected void renderChapters(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        if (!this.chapters.isEmpty()) {
            int chapterX = (int) ((double) this.f_96543_ * 0.5);
            int chapterY = (int) ((double) this.f_96544_ * 0.75);
            PoseStack ms = graphics.pose();
            ms.pushPose();
            ms.translate((float) chapterX, (float) chapterY, 0.0F);
            UIRenderHelper.streak(graphics, 0.0F, this.chapterArea.getX() - 10, this.chapterArea.getY() - 20, 20, 220);
            graphics.drawString(this.f_96547_, "More Topics to Ponder about", this.chapterArea.getX() - 5, this.chapterArea.getY() - 25, Theme.i(Theme.Key.TEXT_ACCENT_SLIGHT), false);
            ms.popPose();
        }
    }

    @Override
    protected void renderWindowForeground(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        RenderSystem.disableDepthTest();
        PoseStack ms = graphics.pose();
        ms.pushPose();
        ms.translate(0.0F, 0.0F, 200.0F);
        if (!this.hoveredItem.isEmpty()) {
            graphics.renderTooltip(this.f_96547_, this.hoveredItem, mouseX, mouseY);
        }
        ms.popPose();
        RenderSystem.enableDepthTest();
    }

    @Override
    protected String getBreadcrumbTitle() {
        return this.tag.getTitle();
    }

    public ItemStack getHoveredTooltipItem() {
        return this.hoveredItem;
    }

    @Override
    public boolean isEquivalentTo(NavigatableSimiScreen other) {
        return other instanceof PonderTagScreen ? this.tag == ((PonderTagScreen) other).tag : super.isEquivalentTo(other);
    }

    @Override
    public boolean isPauseScreen() {
        return true;
    }

    public PonderTag getTag() {
        return this.tag;
    }

    @Override
    public void removed() {
        super.m_7861_();
        this.hoveredItem = ItemStack.EMPTY;
    }
}