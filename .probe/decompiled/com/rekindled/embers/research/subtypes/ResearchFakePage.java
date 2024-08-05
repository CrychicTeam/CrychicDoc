package com.rekindled.embers.research.subtypes;

import com.rekindled.embers.datagen.EmbersSounds;
import com.rekindled.embers.gui.GuiCodex;
import com.rekindled.embers.research.ResearchBase;
import com.rekindled.embers.util.Vec2i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class ResearchFakePage extends ResearchBase {

    ResearchBase targetPage;

    public ResearchFakePage(ResearchBase page, double x, double y) {
        super(page.name, ItemStack.EMPTY, x, y);
        this.targetPage = page;
    }

    public ResearchFakePage(ResearchBase page, Vec2i pos) {
        this(page, (double) pos.x, (double) pos.y);
    }

    @Override
    public String getName() {
        return this.targetPage.getName();
    }

    @Override
    public String getTitle() {
        return this.targetPage.getTitle();
    }

    @Override
    public ItemStack getIcon() {
        return this.targetPage.getIcon();
    }

    @Override
    public ResourceLocation getIconBackground() {
        return this.targetPage.getIconBackground();
    }

    @Override
    public double getIconBackgroundU() {
        return this.targetPage.getIconBackgroundU();
    }

    @Override
    public double getIconBackgroundV() {
        return this.targetPage.getIconBackgroundV();
    }

    @Override
    public boolean onOpen(GuiCodex gui) {
        gui.researchPage = this.targetPage;
        gui.playSound(EmbersSounds.CODEX_PAGE_OPEN.get());
        return false;
    }
}