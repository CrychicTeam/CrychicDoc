package com.mna.gui.item;

import com.google.common.collect.Lists;
import com.mna.gui.GuiTextures;
import com.mna.gui.base.GuiJEIDisable;
import com.mna.gui.containers.item.ContainerSpellRecipe;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

public class GuiSpellRecipe extends GuiJEIDisable<ContainerSpellRecipe> {

    private static final int ITEMSTACK_WIDTH = 16;

    private static final int CYCLE_TICKS = 40;

    public GuiSpellRecipe(ContainerSpellRecipe screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
        this.f_97726_ = 213;
        this.f_97727_ = 256;
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        this.m_280273_(pGuiGraphics);
        int i = this.f_97735_;
        int j = this.f_97736_;
        pGuiGraphics.blit(GuiTextures.Items.SPELL_RECIPE, i, j, 0.0F, 0.0F, this.f_97726_, this.f_97727_, 256, 256);
        int x = this.f_97735_ + 6;
        int y = this.f_97736_ + 24 + 9;
        int column_width = 24;
        for (List<ItemStack> stacks : ((ContainerSpellRecipe) this.f_97732_).getReagents()) {
            this.renderItemStack(pGuiGraphics, stacks, x, y, pMouseX, pMouseY, 1.0F);
            x += 16 + column_width;
            if (x >= this.f_97735_ + this.f_97726_ - 16 - column_width) {
                y += 19;
                x = this.f_97735_ + 6;
            }
        }
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
        int y = 10;
        String rawText = Component.translatable("item.mna.enchanted_vellum.subtitle").getString();
        for (FormattedText itp : this.f_96547_.getSplitter().splitLines(rawText, this.f_97726_ - 10, Style.EMPTY)) {
            pGuiGraphics.drawString(this.f_96547_, itp.getString(), 8, y, FastColor.ARGB32.color(255, 71, 71, 71), false);
            y += 9;
        }
    }

    protected final int renderItemStack(GuiGraphics pGuiGraphics, List<ItemStack> stackToRender, int x, int y, int mouseX, int mouseY, float scale) {
        if (stackToRender != null && stackToRender.size() != 0) {
            ItemStack stack = this.getCurrentIndex(stackToRender);
            pGuiGraphics.renderItem(stack, x, y);
            StringBuilder sb = new StringBuilder();
            sb.append(" x ");
            sb.append(stack.getCount());
            pGuiGraphics.drawString(this.f_96547_, sb.toString(), x + 16, y + 3, FastColor.ARGB32.color(255, 71, 71, 71), false);
            int adjustedX = (int) ((float) x * scale + 16.0F * scale);
            int adjustedY = (int) ((float) y * scale + 16.0F * scale);
            if (adjustedX >= mouseX && (float) adjustedX <= (float) mouseX + 16.0F * scale && adjustedY >= mouseY && (float) adjustedY <= (float) mouseY + 16.0F * scale) {
                List<Component> toolTip = stack.getTooltipLines(null, TooltipFlag.Default.f_256752_);
                if (toolTip != null) {
                    pGuiGraphics.renderTooltip(this.f_96547_, Lists.transform(toolTip, Component::m_7532_), (int) ((float) mouseX / scale), (int) ((float) mouseY / scale));
                }
            }
            return x + 16 + this.f_96547_.width(sb.toString());
        } else {
            return 0;
        }
    }

    private ItemStack getCurrentIndex(List<ItemStack> stacks) {
        return stacks.size() == 1 ? (ItemStack) stacks.get(0) : (ItemStack) stacks.get((int) (this.f_96541_.level.m_46467_() / 40L % (long) stacks.size()));
    }
}