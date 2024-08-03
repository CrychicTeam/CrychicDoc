package com.mna.gui.widgets.lodestar;

import com.mna.ManaAndArtifice;
import com.mna.api.entities.construct.ai.parameter.ConstructAITaskParameter;
import com.mna.api.entities.construct.ai.parameter.ConstructTaskFilterParameter;
import com.mna.api.items.DynamicItemFilter;
import com.mna.inventory.ItemInventoryBase;
import com.mna.items.ItemInit;
import com.mna.items.artifice.FilterItem;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class FilterParameterInput extends LodestarParameter<DynamicItemFilter> {

    public static final int V = 44;

    private ItemStack whitelistFilter = new ItemStack(ItemInit.FILTER_ITEM.get());

    private ItemStack blacklistFilter = new ItemStack(ItemInit.FILTER_ITEM.get());

    public FilterParameterInput(boolean lowTier, int x, int y, Button.OnPress pressHandler, Component tooltip) {
        super(lowTier, x, y, 44, new DynamicItemFilter(), pressHandler, tooltip);
    }

    public FilterParameterInput(boolean lowTier, int x, int y, DynamicItemFilter defaultValue, Button.OnPress pressHandler, Component tooltip) {
        super(lowTier, x, y, 44, defaultValue, pressHandler, tooltip);
    }

    @Override
    public void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.renderWidget(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        if (!this.value.isWhitelistEmpty()) {
            pGuiGraphics.renderItem(this.whitelistFilter, this.m_252754_() + 19, this.m_252907_() + 3);
        }
        if (!this.value.isBlacklistEmpty()) {
            pGuiGraphics.renderItem(this.blacklistFilter, this.m_252754_() + 37, this.m_252907_() + 3);
        }
    }

    @Override
    public void saveTo(ConstructAITaskParameter param) {
        if (param instanceof ConstructTaskFilterParameter && this.value != null) {
            ((ConstructTaskFilterParameter) param).getValue().copyFrom(this.value);
        }
    }

    @Override
    public void loadFrom(ConstructAITaskParameter param) {
        this.value.copyFrom(((ConstructTaskFilterParameter) param).getValue());
        ItemInit.FILTER_ITEM.get().setItems(this.whitelistFilter, this.value.getWhiteList(), this.value.getWhitelistMatchDurability(), this.value.getWhitelistMatchTag());
        ItemInit.FILTER_ITEM.get().setItems(this.blacklistFilter, this.value.getBlackList(), this.value.getBlacklistMatchDurability(), this.value.getBlacklistMatchTag());
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        ItemStack carried = this.getCursorHeldItem();
        int slot = this.getClickSlot(mouseX, mouseY);
        if (slot != -1) {
            boolean isWhitelist = slot == 0;
            boolean matchDurability = false;
            boolean matchTag = false;
            NonNullList<ItemStack> items;
            if (!carried.isEmpty() && carried.getItem() instanceof FilterItem) {
                ItemInventoryBase inv = new ItemInventoryBase(carried);
                items = inv.getAllItems();
                matchDurability = ((FilterItem) carried.getItem()).getMatchDurability(carried);
                matchTag = ((FilterItem) carried.getItem()).getMatchTag(carried);
            } else {
                items = NonNullList.create();
            }
            if (isWhitelist) {
                this.value.setWhitelist(items, matchDurability, matchTag);
                ItemInit.FILTER_ITEM.get().setItems(this.whitelistFilter, this.value.getWhiteList(), this.value.getWhitelistMatchDurability(), this.value.getWhitelistMatchTag());
            } else {
                this.value.setBlacklist(items, matchDurability, matchTag);
                ItemInit.FILTER_ITEM.get().setItems(this.blacklistFilter, this.value.getBlackList(), this.value.getBlacklistMatchDurability(), this.value.getBlacklistMatchTag());
            }
            super.m_5716_(mouseX, mouseY);
        }
    }

    private int getClickSlot(double mouseX, double mouseY) {
        int padding = 16;
        if (mouseX < (double) (this.m_252754_() + padding)) {
            return -1;
        } else {
            return mouseX < (double) (this.m_252754_() + padding + (this.f_93618_ - padding) / 2) ? 0 : 1;
        }
    }

    @Override
    public List<Component> getTooltipItems() {
        List<Component> baseTT = super.getTooltipItems();
        baseTT.add(Component.literal(" "));
        baseTT.add(Component.translatable("gui.mna.filter.whitelist"));
        ItemInit.FILTER_ITEM.get().addContentsDescription(this.whitelistFilter, ManaAndArtifice.instance.proxy.getClientWorld(), baseTT);
        baseTT.add(Component.literal("-------"));
        baseTT.add(Component.translatable("gui.mna.filter.blacklist"));
        ItemInit.FILTER_ITEM.get().addContentsDescription(this.blacklistFilter, ManaAndArtifice.instance.proxy.getClientWorld(), baseTT);
        return baseTT;
    }
}