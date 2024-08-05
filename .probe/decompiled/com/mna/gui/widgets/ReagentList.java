package com.mna.gui.widgets;

import com.mna.ManaAndArtifice;
import com.mna.api.tools.MATags;
import com.mna.gui.GuiTextures;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ReagentList extends ObjectSelectionList<ReagentList.ReagentListEntry> {

    private boolean _scrolling = false;

    private boolean fullTooltip = false;

    private final Font font;

    private final Consumer<Component> tooltipFunction;

    public ReagentList(int screenLeft, int screenTop, int x, int y, int width, int height, @Nullable Consumer<Component> tooltipFunction) {
        super(Minecraft.getInstance(), width, height, screenTop + y, screenTop + y + height, 16);
        this.m_93471_(false);
        this.m_93473_(false, 24);
        this.f_93393_ = screenLeft + x;
        this.f_93392_ = screenLeft + x + width;
        Minecraft mc = Minecraft.getInstance();
        this.font = mc.font;
        this.tooltipFunction = tooltipFunction;
        this.clear();
    }

    public void setFullTooltip(boolean fullTooltip) {
        this.fullTooltip = fullTooltip;
    }

    public void clear() {
        this.m_93516_();
    }

    public void reInit(Collection<ItemStack> stacks) {
        this.clear();
        if (stacks != null) {
            this.addIconsForAll((Collection<List<ItemStack>>) stacks.stream().map(is -> {
                ArrayList<ItemStack> mappedCollection = new ArrayList();
                mappedCollection.add(is);
                return mappedCollection;
            }).collect(Collectors.toList()));
        }
    }

    public void reInit(HashMap<ResourceLocation, Integer> stacks) {
        this.clear();
        if (stacks != null) {
            Collection<List<ItemStack>> collections = new ArrayList();
            stacks.entrySet().forEach(e -> {
                ArrayList<ItemStack> collectedStacks = new ArrayList();
                List<Item> items = MATags.smartLookupItem((ResourceLocation) e.getKey());
                if (items != null && items.size() != 0) {
                    items.forEach(i -> collectedStacks.add(new ItemStack(i, (Integer) e.getValue())));
                    collections.add(collectedStacks);
                }
            });
            this.addIconsForAll(collections);
        }
    }

    private void addIconsForAll(Collection<List<ItemStack>> stacks) {
        ArrayList<List<ItemStack>> segment = new ArrayList();
        for (List<ItemStack> part : stacks) {
            segment.add(part);
            int segmentWidth = (segment.size() + 1) * 8 + 15;
            if (segmentWidth >= this.f_93388_) {
                this.m_7085_(new ReagentList.ReagentListEntry(segment));
                segment.clear();
            }
        }
        if (segment.size() > 0) {
            this.m_7085_(new ReagentList.ReagentListEntry(segment));
        }
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int mouseX, int mouseY, float partialTicks) {
        int scrollBarStartX = this.getScrollbarPosition();
        pGuiGraphics.enableScissor(this.f_93393_, this.f_93390_, this.f_93392_, this.f_93391_);
        this.m_239227_(pGuiGraphics, mouseX, mouseY, partialTicks);
        if (this.getMaxScroll() > 0) {
            int scrollBarHeight = 20;
            int top = (int) this.m_93517_() * (this.f_93391_ - this.f_93390_ - scrollBarHeight) / this.getMaxScroll() + this.f_93390_;
            if (top < this.f_93390_) {
                top = this.f_93390_;
            }
            pGuiGraphics.blit(GuiTextures.WizardLab.INSCRIPTION_TABLE_WIDGETS, scrollBarStartX, top, 25.0F, 20.0F, 4, scrollBarHeight, 128, 128);
        }
        pGuiGraphics.disableScissor();
    }

    @Nullable
    protected final ReagentList.ReagentListEntry getEntryAtPos(double mouseX, double mouseY) {
        int lowerXBound = this.getRowLeft();
        int upperXBound = lowerXBound + this.getRowWidth();
        int adjustedY = Mth.floor(mouseY - (double) this.f_93390_) + (int) this.m_93517_();
        int index = adjustedY / this.f_93387_;
        return index >= 0 && adjustedY >= 0 && index < this.m_5773_() && mouseX < (double) this.getScrollbarPosition() && mouseX >= (double) lowerXBound && mouseX <= (double) upperXBound ? (ReagentList.ReagentListEntry) this.m_6702_().get(index) : null;
    }

    @Override
    protected int getScrollbarPosition() {
        return this.getRowLeft() + this.getRowWidth() - 4;
    }

    @Override
    protected int getRowTop(int p_getRowTop_1_) {
        return this.f_93390_ - (int) this.m_93517_() + p_getRowTop_1_ * this.f_93387_ - 4;
    }

    @Override
    public int getRowLeft() {
        return this.f_93393_;
    }

    @Override
    public int getRowWidth() {
        return this.f_93388_;
    }

    @Override
    protected void updateScrollingState(double p_updateScrollingState_1_, double p_updateScrollingState_3_, int p_updateScrollingState_5_) {
        super.m_93481_(p_updateScrollingState_1_, p_updateScrollingState_3_, p_updateScrollingState_5_);
        this._scrolling = p_updateScrollingState_5_ == 0 && p_updateScrollingState_1_ >= (double) this.getScrollbarPosition() && p_updateScrollingState_1_ < (double) (this.getScrollbarPosition() + 6);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.updateScrollingState(mouseX, mouseY, button);
        if (!this.m_5953_(mouseX, mouseY)) {
            return false;
        } else if (button == 0) {
            this.m_7897_(true);
            return true;
        } else {
            return this._scrolling;
        }
    }

    @Override
    public int getMaxScroll() {
        return Math.max(0, this.m_5775_() - (this.f_93391_ - this.f_93390_ - 4));
    }

    public void setSelected(@Nullable ReagentList.ReagentListEntry selected) {
        super.m_6987_(selected);
    }

    public boolean isScrolling() {
        return this._scrolling;
    }

    public class ReagentListEntry extends ObjectSelectionList.Entry<ReagentList.ReagentListEntry> {

        private Collection<List<ItemStack>> stacks;

        private int spacing = 10;

        public ReagentListEntry(Collection<List<ItemStack>> parts) {
            this.stacks = new ArrayList(parts);
        }

        @Override
        public void render(GuiGraphics pGuiGraphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean isHovered, float p_render_9_) {
            int i = 0;
            float scale = 0.5F;
            pGuiGraphics.pose().pushPose();
            pGuiGraphics.pose().scale(scale, scale, scale);
            for (List<ItemStack> stackList : this.stacks) {
                if (stackList != null && stackList.size() != 0) {
                    int stackIndex = (int) (ManaAndArtifice.instance.proxy.getClientWorld().getGameTime() / 20L) % Math.max(stackList.size(), 1);
                    ItemStack stack = (ItemStack) stackList.get(stackIndex);
                    if (!stack.isEmpty()) {
                        int x = (int) ((float) (5 + left + i++ * this.spacing) / scale);
                        int y = (int) ((float) (top + 8) / scale);
                        pGuiGraphics.renderItem(stack, x, y);
                        String str = String.format("%d", stack.getCount());
                        pGuiGraphics.pose().pushPose();
                        pGuiGraphics.pose().translate(0.0F, 0.0F, 500.0F);
                        pGuiGraphics.drawString(ReagentList.this.font, str, x + 14 - ReagentList.this.font.width(str) / 2, y + 8, 16777215, false);
                        pGuiGraphics.pose().popPose();
                        boolean isOverMe = ReagentList.this.getEntryAtPos((double) mouseX, (double) mouseY) == this;
                        if (this.m_5953_((double) mouseX, (double) mouseY) && isOverMe && (float) mouseX >= (float) x * scale && (float) mouseX <= (float) (x + this.spacing + 5) * scale && ReagentList.this.tooltipFunction != null) {
                            if (ReagentList.this.fullTooltip) {
                                Minecraft mc = Minecraft.getInstance();
                                stack.getTooltipLines(mc.player, TooltipFlag.Default.f_256752_).forEach(ReagentList.this.tooltipFunction);
                            } else {
                                ReagentList.this.tooltipFunction.accept(Component.translatable(stack.getDescriptionId()));
                            }
                        }
                    }
                }
            }
            pGuiGraphics.pose().popPose();
        }

        @Override
        public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
            return true;
        }

        @Override
        public Component getNarration() {
            return Component.translatable("narrator.select", "");
        }
    }
}