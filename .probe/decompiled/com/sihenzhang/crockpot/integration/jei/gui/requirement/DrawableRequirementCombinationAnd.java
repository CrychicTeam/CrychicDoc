package com.sihenzhang.crockpot.integration.jei.gui.requirement;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.sihenzhang.crockpot.recipe.cooking.requirement.IRequirement;
import com.sihenzhang.crockpot.recipe.cooking.requirement.RequirementCombinationAnd;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;

public class DrawableRequirementCombinationAnd extends AbstractDrawableRequirement<RequirementCombinationAnd> {

    private final AbstractDrawableRequirement<? extends IRequirement> first;

    private final AbstractDrawableRequirement<? extends IRequirement> second;

    public DrawableRequirementCombinationAnd(RequirementCombinationAnd requirement) {
        super(requirement, null);
        this.first = AbstractDrawableRequirement.createDrawable(requirement.getFirst());
        this.second = AbstractDrawableRequirement.createDrawable(requirement.getSecond());
    }

    @Override
    public int getWidth() {
        return 6 + Math.max(this.first.getWidth(), this.second.getWidth());
    }

    @Override
    public int getHeight() {
        return 7 + this.first.getHeight() + this.second.getHeight();
    }

    @Override
    public void draw(GuiGraphics guiGraphics, int xOffset, int yOffset) {
        super.draw(guiGraphics, xOffset, yOffset);
        this.first.draw(guiGraphics, xOffset + 3, yOffset + 3);
        this.second.draw(guiGraphics, xOffset + 3, yOffset + this.first.getHeight() + 4);
    }

    @Override
    public List<ItemStack> getInvisibleInputs() {
        Builder<ItemStack> builder = ImmutableList.builder();
        builder.addAll(this.first.getInvisibleInputs());
        builder.addAll(this.second.getInvisibleInputs());
        return builder.build();
    }

    @Override
    public List<AbstractDrawableRequirement.GuiItemStacksInfo> getGuiItemStacksInfos(int xOffset, int yOffset) {
        Builder<AbstractDrawableRequirement.GuiItemStacksInfo> builder = ImmutableList.builder();
        builder.addAll(this.first.getGuiItemStacksInfos(xOffset + 3, yOffset + 3));
        builder.addAll(this.second.getGuiItemStacksInfos(xOffset + 3, yOffset + this.first.getHeight() + 4));
        return builder.build();
    }
}