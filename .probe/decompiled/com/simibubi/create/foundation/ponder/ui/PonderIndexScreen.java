package com.simibubi.create.foundation.ponder.ui;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.kinetics.crank.ValveHandleBlock;
import com.simibubi.create.foundation.gui.ScreenOpener;
import com.simibubi.create.foundation.gui.Theme;
import com.simibubi.create.foundation.gui.UIRenderHelper;
import com.simibubi.create.foundation.ponder.PonderChapter;
import com.simibubi.create.foundation.ponder.PonderRegistry;
import com.simibubi.create.foundation.utility.RegisteredObjects;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.util.Mth;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

public class PonderIndexScreen extends NavigatableSimiScreen {

    protected final List<PonderChapter> chapters;

    private final double chapterXmult = 0.5;

    private final double chapterYmult = 0.3;

    protected Rect2i chapterArea;

    protected final List<Item> items;

    private final double itemXmult = 0.5;

    private double itemYmult = 0.75;

    protected Rect2i itemArea;

    private ItemStack hoveredItem = ItemStack.EMPTY;

    public PonderIndexScreen() {
        this.chapters = new ArrayList();
        this.items = new ArrayList();
    }

    @Override
    protected void init() {
        super.init();
        this.chapters.clear();
        this.items.clear();
        PonderRegistry.ALL.keySet().stream().map(key -> {
            Item itemx = ForgeRegistries.ITEMS.getValue(key);
            if (itemx == null) {
                Block bx = ForgeRegistries.BLOCKS.getValue(key);
                if (bx != null) {
                    itemx = bx.asItem();
                }
            }
            return itemx;
        }).filter(Objects::nonNull).filter(PonderIndexScreen::exclusions).forEach(this.items::add);
        boolean hasChapters = !this.chapters.isEmpty();
        LayoutHelper layout = LayoutHelper.centeredHorizontal(this.chapters.size(), Mth.clamp((int) Math.ceil((double) ((float) this.chapters.size() / 4.0F)), 1, 4), 200, 38, 16);
        this.chapterArea = layout.getArea();
        int chapterCenterX = (int) ((double) this.f_96543_ * 0.5);
        int chapterCenterY = (int) ((double) this.f_96544_ * 0.3);
        for (PonderChapter chapter : this.chapters) {
            ChapterLabel label = new ChapterLabel(chapter, chapterCenterX + layout.getX(), chapterCenterY + layout.getY(), (mouseX, mouseY) -> {
                this.centerScalingOn(mouseX, mouseY);
                ScreenOpener.transitionTo(PonderUI.of(chapter));
            });
            this.m_142416_(label);
            layout.next();
        }
        if (!hasChapters) {
            this.itemYmult = 0.5;
        }
        int maxItemRows = hasChapters ? 4 : 7;
        layout = LayoutHelper.centeredHorizontal(this.items.size(), Mth.clamp((int) Math.ceil((double) ((float) this.items.size() / 11.0F)), 1, maxItemRows), 28, 28, 8);
        this.itemArea = layout.getArea();
        int itemCenterX = (int) ((double) this.f_96543_ * 0.5);
        int itemCenterY = (int) ((double) this.f_96544_ * this.itemYmult);
        for (Item item : this.items) {
            PonderButton b = new PonderButton(itemCenterX + layout.getX() + 4, itemCenterY + layout.getY() + 4).<PonderButton>showing(new ItemStack(item)).withCallback((x, y) -> {
                if (PonderRegistry.ALL.containsKey(RegisteredObjects.getKeyOrThrow(item))) {
                    this.centerScalingOn(x, y);
                    ScreenOpener.transitionTo(PonderUI.of(new ItemStack(item)));
                }
            });
            this.m_142416_(b);
            layout.next();
        }
    }

    @Override
    protected void initBackTrackIcon(PonderButton backTrack) {
        backTrack.showing(AllItems.WRENCH.asStack());
    }

    private static boolean exclusions(Item item) {
        if (item instanceof BlockItem) {
            Block block = ((BlockItem) item).getBlock();
            if (block instanceof ValveHandleBlock && !AllBlocks.COPPER_VALVE_HANDLE.is(item)) {
                return false;
            }
        }
        return true;
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
            if (child instanceof PonderButton) {
                PonderButton button = (PonderButton) child;
                if (button.m_5953_(mouseX, mouseY)) {
                    this.hoveredItem = button.getItem();
                }
            }
        }
    }

    @Override
    protected void renderWindow(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        int x = (int) ((double) this.f_96543_ * 0.5);
        int y = (int) ((double) this.f_96544_ * 0.3);
        PoseStack ms = graphics.pose();
        if (!this.chapters.isEmpty()) {
            ms.pushPose();
            ms.translate((float) x, (float) y, 0.0F);
            UIRenderHelper.streak(graphics, 0.0F, this.chapterArea.getX() - 10, this.chapterArea.getY() - 20, 20, 220);
            graphics.drawString(this.f_96547_, "Topics to Ponder about", this.chapterArea.getX() - 5, this.chapterArea.getY() - 25, Theme.i(Theme.Key.TEXT), false);
            ms.popPose();
        }
        x = (int) ((double) this.f_96543_ * 0.5);
        y = (int) ((double) this.f_96544_ * this.itemYmult);
        ms.pushPose();
        ms.translate((float) x, (float) y, 0.0F);
        UIRenderHelper.streak(graphics, 0.0F, this.itemArea.getX() - 10, this.itemArea.getY() - 20, 20, 220);
        graphics.drawString(this.f_96547_, "Items to inspect", this.itemArea.getX() - 5, this.itemArea.getY() - 25, Theme.i(Theme.Key.TEXT), false);
        ms.popPose();
    }

    @Override
    protected void renderWindowForeground(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        if (!this.hoveredItem.isEmpty()) {
            PoseStack ms = graphics.pose();
            ms.pushPose();
            ms.translate(0.0F, 0.0F, 200.0F);
            graphics.renderTooltip(this.f_96547_, this.hoveredItem, mouseX, mouseY);
            ms.popPose();
        }
    }

    @Override
    public boolean isEquivalentTo(NavigatableSimiScreen other) {
        return other instanceof PonderIndexScreen;
    }

    public ItemStack getHoveredTooltipItem() {
        return this.hoveredItem;
    }

    @Override
    public boolean isPauseScreen() {
        return true;
    }
}