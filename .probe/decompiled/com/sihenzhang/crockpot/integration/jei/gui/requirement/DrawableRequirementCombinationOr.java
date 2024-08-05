package com.sihenzhang.crockpot.integration.jei.gui.requirement;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.sihenzhang.crockpot.recipe.cooking.requirement.IRequirement;
import com.sihenzhang.crockpot.recipe.cooking.requirement.RequirementCombinationOr;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class DrawableRequirementCombinationOr extends AbstractDrawableRequirement<RequirementCombinationOr> {

    private final AbstractDrawableRequirement<? extends IRequirement> first;

    private final AbstractDrawableRequirement<? extends IRequirement> second;

    public DrawableRequirementCombinationOr(RequirementCombinationOr requirement) {
        super(requirement, Component.translatable("integration.crockpot.jei.crock_pot_cooking.requirement.or"));
        this.first = AbstractDrawableRequirement.createDrawable(requirement.getFirst());
        this.second = AbstractDrawableRequirement.createDrawable(requirement.getSecond());
    }

    @Override
    public int getWidth() {
        return 8 + this.first.getWidth() + this.second.getWidth() + Minecraft.getInstance().font.width(this.description);
    }

    @Override
    public int getHeight() {
        return 6 + Math.max(this.first.getHeight(), this.second.getHeight());
    }

    @Override
    public void draw(GuiGraphics guiGraphics, int xOffset, int yOffset) {
        super.draw(guiGraphics, xOffset, yOffset);
        this.first.draw(guiGraphics, xOffset + 3, yOffset + this.getHeight() / 2 - this.first.getHeight() / 2);
        guiGraphics.drawString(Minecraft.getInstance().font, this.description, xOffset + this.first.getWidth() + 4, yOffset + this.getHeight() / 2 - 4, 0, false);
        this.second.draw(guiGraphics, xOffset + this.getWidth() - this.second.getWidth() - 3, yOffset + this.getHeight() / 2 - this.second.getHeight() / 2);
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
        builder.addAll(this.first.getGuiItemStacksInfos(xOffset + 3, yOffset + this.getHeight() / 2 - this.first.getHeight() / 2));
        builder.addAll(this.second.getGuiItemStacksInfos(xOffset + this.getWidth() - this.second.getWidth() - 3, yOffset + this.getHeight() / 2 - this.second.getHeight() / 2));
        return builder.build();
    }
}