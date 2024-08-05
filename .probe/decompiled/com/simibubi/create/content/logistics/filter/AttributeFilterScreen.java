package com.simibubi.create.content.logistics.filter;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllPackets;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.gui.widget.AbstractSimiWidget;
import com.simibubi.create.foundation.gui.widget.IconButton;
import com.simibubi.create.foundation.gui.widget.Indicator;
import com.simibubi.create.foundation.gui.widget.Label;
import com.simibubi.create.foundation.gui.widget.SelectionScrollInput;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.Pair;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class AttributeFilterScreen extends AbstractFilterScreen<AttributeFilterMenu> {

    private static final String PREFIX = "gui.attribute_filter.";

    private Component addDESC = Lang.translateDirect("gui.attribute_filter.add_attribute");

    private Component addInvertedDESC = Lang.translateDirect("gui.attribute_filter.add_inverted_attribute");

    private Component allowDisN = Lang.translateDirect("gui.attribute_filter.allow_list_disjunctive");

    private Component allowDisDESC = Lang.translateDirect("gui.attribute_filter.allow_list_disjunctive.description");

    private Component allowConN = Lang.translateDirect("gui.attribute_filter.allow_list_conjunctive");

    private Component allowConDESC = Lang.translateDirect("gui.attribute_filter.allow_list_conjunctive.description");

    private Component denyN = Lang.translateDirect("gui.attribute_filter.deny_list");

    private Component denyDESC = Lang.translateDirect("gui.attribute_filter.deny_list.description");

    private Component referenceH = Lang.translateDirect("gui.attribute_filter.add_reference_item");

    private Component noSelectedT = Lang.translateDirect("gui.attribute_filter.no_selected_attributes");

    private Component selectedT = Lang.translateDirect("gui.attribute_filter.selected_attributes");

    private IconButton whitelistDis;

    private IconButton whitelistCon;

    private IconButton blacklist;

    private Indicator whitelistDisIndicator;

    private Indicator whitelistConIndicator;

    private Indicator blacklistIndicator;

    private IconButton add;

    private IconButton addInverted;

    private ItemStack lastItemScanned = ItemStack.EMPTY;

    private List<ItemAttribute> attributesOfItem = new ArrayList();

    private List<Component> selectedAttributes = new ArrayList();

    private SelectionScrollInput attributeSelector;

    private Label attributeSelectorLabel;

    public AttributeFilterScreen(AttributeFilterMenu menu, Inventory inv, Component title) {
        super(menu, inv, title, AllGuiTextures.ATTRIBUTE_FILTER);
    }

    @Override
    protected void init() {
        this.setWindowOffset(-11, 7);
        super.init();
        int x = this.f_97735_;
        int y = this.f_97736_;
        this.whitelistDis = new IconButton(x + 47, y + 61, AllIcons.I_WHITELIST_OR);
        this.whitelistDis.withCallback(() -> {
            ((AttributeFilterMenu) this.f_97732_).whitelistMode = AttributeFilterMenu.WhitelistMode.WHITELIST_DISJ;
            this.sendOptionUpdate(FilterScreenPacket.Option.WHITELIST);
        });
        this.whitelistDis.setToolTip(this.allowDisN);
        this.whitelistCon = new IconButton(x + 65, y + 61, AllIcons.I_WHITELIST_AND);
        this.whitelistCon.withCallback(() -> {
            ((AttributeFilterMenu) this.f_97732_).whitelistMode = AttributeFilterMenu.WhitelistMode.WHITELIST_CONJ;
            this.sendOptionUpdate(FilterScreenPacket.Option.WHITELIST2);
        });
        this.whitelistCon.setToolTip(this.allowConN);
        this.blacklist = new IconButton(x + 83, y + 61, AllIcons.I_WHITELIST_NOT);
        this.blacklist.withCallback(() -> {
            ((AttributeFilterMenu) this.f_97732_).whitelistMode = AttributeFilterMenu.WhitelistMode.BLACKLIST;
            this.sendOptionUpdate(FilterScreenPacket.Option.BLACKLIST);
        });
        this.blacklist.setToolTip(this.denyN);
        this.whitelistDisIndicator = new Indicator(x + 47, y + 55, Components.immutableEmpty());
        this.whitelistConIndicator = new Indicator(x + 65, y + 55, Components.immutableEmpty());
        this.blacklistIndicator = new Indicator(x + 83, y + 55, Components.immutableEmpty());
        this.addRenderableWidgets(new AbstractSimiWidget[] { this.blacklist, this.whitelistCon, this.whitelistDis, this.blacklistIndicator, this.whitelistConIndicator, this.whitelistDisIndicator });
        this.m_142416_(this.add = new IconButton(x + 182, y + 23, AllIcons.I_ADD));
        this.m_142416_(this.addInverted = new IconButton(x + 200, y + 23, AllIcons.I_ADD_INVERTED_ATTRIBUTE));
        this.add.withCallback(() -> this.handleAddedAttibute(false));
        this.add.setToolTip(this.addDESC);
        this.addInverted.withCallback(() -> this.handleAddedAttibute(true));
        this.addInverted.setToolTip(this.addInvertedDESC);
        this.handleIndicators();
        this.attributeSelectorLabel = new Label(x + 43, y + 28, Components.immutableEmpty()).colored(15985630).withShadow();
        this.attributeSelector = new SelectionScrollInput(x + 39, y + 23, 137, 18);
        this.attributeSelector.forOptions(Arrays.asList(Components.immutableEmpty()));
        this.attributeSelector.removeCallback();
        this.referenceItemChanged(((AttributeFilterMenu) this.f_97732_).ghostInventory.getStackInSlot(0));
        this.m_142416_(this.attributeSelector);
        this.m_142416_(this.attributeSelectorLabel);
        this.selectedAttributes.clear();
        this.selectedAttributes.add((((AttributeFilterMenu) this.f_97732_).selectedAttributes.isEmpty() ? this.noSelectedT : this.selectedT).plainCopy().withStyle(ChatFormatting.YELLOW));
        ((AttributeFilterMenu) this.f_97732_).selectedAttributes.forEach(at -> this.selectedAttributes.add(Components.literal("- ").append(((ItemAttribute) at.getFirst()).format((Boolean) at.getSecond())).withStyle(ChatFormatting.GRAY)));
    }

    private void referenceItemChanged(ItemStack stack) {
        this.lastItemScanned = stack;
        if (stack.isEmpty()) {
            this.attributeSelector.f_93623_ = false;
            this.attributeSelector.f_93624_ = false;
            this.attributeSelectorLabel.text = this.referenceH.plainCopy().withStyle(ChatFormatting.ITALIC);
            this.add.f_93623_ = false;
            this.addInverted.f_93623_ = false;
            this.attributeSelector.calling(s -> {
            });
        } else {
            this.add.f_93623_ = true;
            this.addInverted.f_93623_ = true;
            this.attributeSelector.titled(stack.getHoverName().plainCopy().append("..."));
            this.attributesOfItem.clear();
            for (ItemAttribute itemAttribute : ItemAttribute.types) {
                this.attributesOfItem.addAll(itemAttribute.listAttributesOf(stack, this.f_96541_.level));
            }
            List<Component> options = (List<Component>) this.attributesOfItem.stream().map(a -> a.format(false)).collect(Collectors.toList());
            this.attributeSelector.forOptions(options);
            this.attributeSelector.f_93623_ = true;
            this.attributeSelector.f_93624_ = true;
            this.attributeSelector.setState(0);
            this.attributeSelector.calling(i -> {
                this.attributeSelectorLabel.setTextAndTrim((Component) options.get(i), true, 112);
                ItemAttribute selected = (ItemAttribute) this.attributesOfItem.get(i);
                for (Pair<ItemAttribute, Boolean> existing : ((AttributeFilterMenu) this.f_97732_).selectedAttributes) {
                    CompoundTag testTag = new CompoundTag();
                    CompoundTag testTag2 = new CompoundTag();
                    existing.getFirst().serializeNBT(testTag);
                    selected.serializeNBT(testTag2);
                    if (testTag.equals(testTag2)) {
                        this.add.f_93623_ = false;
                        this.addInverted.f_93623_ = false;
                        return;
                    }
                }
                this.add.f_93623_ = true;
                this.addInverted.f_93623_ = true;
            });
            this.attributeSelector.onChanged();
        }
    }

    @Override
    public void renderForeground(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        ItemStack stack = ((AttributeFilterMenu) this.f_97732_).ghostInventory.getStackInSlot(1);
        PoseStack matrixStack = graphics.pose();
        matrixStack.pushPose();
        matrixStack.translate(0.0F, 0.0F, 150.0F);
        graphics.renderItemDecorations(this.f_96547_, stack, this.f_97735_ + 22, this.f_97736_ + 59, String.valueOf(this.selectedAttributes.size() - 1));
        matrixStack.popPose();
        super.renderForeground(graphics, mouseX, mouseY, partialTicks);
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        ItemStack stackInSlot = ((AttributeFilterMenu) this.f_97732_).ghostInventory.getStackInSlot(0);
        if (!stackInSlot.equals(this.lastItemScanned, false)) {
            this.referenceItemChanged(stackInSlot);
        }
    }

    @Override
    protected void renderTooltip(GuiGraphics graphics, int mouseX, int mouseY) {
        if (((AttributeFilterMenu) this.f_97732_).m_142621_().isEmpty() && this.f_97734_ != null && this.f_97734_.hasItem()) {
            if (this.f_97734_.index == 37) {
                graphics.renderComponentTooltip(this.f_96547_, this.selectedAttributes, mouseX, mouseY);
                return;
            }
            graphics.renderTooltip(this.f_96547_, this.f_97734_.getItem(), mouseX, mouseY);
        }
        super.m_280072_(graphics, mouseX, mouseY);
    }

    @Override
    protected List<IconButton> getTooltipButtons() {
        return Arrays.asList(this.blacklist, this.whitelistCon, this.whitelistDis);
    }

    @Override
    protected List<MutableComponent> getTooltipDescriptions() {
        return Arrays.asList(this.denyDESC.plainCopy(), this.allowConDESC.plainCopy(), this.allowDisDESC.plainCopy());
    }

    @Override
    protected List<Indicator> getIndicators() {
        return Arrays.asList(this.blacklistIndicator, this.whitelistConIndicator, this.whitelistDisIndicator);
    }

    protected boolean handleAddedAttibute(boolean inverted) {
        int index = this.attributeSelector.getState();
        if (index >= this.attributesOfItem.size()) {
            return false;
        } else {
            this.add.f_93623_ = false;
            this.addInverted.f_93623_ = false;
            CompoundTag tag = new CompoundTag();
            ItemAttribute itemAttribute = (ItemAttribute) this.attributesOfItem.get(index);
            itemAttribute.serializeNBT(tag);
            AllPackets.getChannel().sendToServer(new FilterScreenPacket(inverted ? FilterScreenPacket.Option.ADD_INVERTED_TAG : FilterScreenPacket.Option.ADD_TAG, tag));
            ((AttributeFilterMenu) this.f_97732_).appendSelectedAttribute(itemAttribute, inverted);
            if (((AttributeFilterMenu) this.f_97732_).selectedAttributes.size() == 1) {
                this.selectedAttributes.set(0, this.selectedT.plainCopy().withStyle(ChatFormatting.YELLOW));
            }
            this.selectedAttributes.add(Components.literal("- ").append(itemAttribute.format(inverted)).withStyle(ChatFormatting.GRAY));
            return true;
        }
    }

    @Override
    protected void contentsCleared() {
        this.selectedAttributes.clear();
        this.selectedAttributes.add(this.noSelectedT.plainCopy().withStyle(ChatFormatting.YELLOW));
        if (!this.lastItemScanned.isEmpty()) {
            this.add.f_93623_ = true;
            this.addInverted.f_93623_ = true;
        }
    }

    @Override
    protected boolean isButtonEnabled(IconButton button) {
        if (button == this.blacklist) {
            return ((AttributeFilterMenu) this.f_97732_).whitelistMode != AttributeFilterMenu.WhitelistMode.BLACKLIST;
        } else if (button == this.whitelistCon) {
            return ((AttributeFilterMenu) this.f_97732_).whitelistMode != AttributeFilterMenu.WhitelistMode.WHITELIST_CONJ;
        } else {
            return button == this.whitelistDis ? ((AttributeFilterMenu) this.f_97732_).whitelistMode != AttributeFilterMenu.WhitelistMode.WHITELIST_DISJ : true;
        }
    }

    @Override
    protected boolean isIndicatorOn(Indicator indicator) {
        if (indicator == this.blacklistIndicator) {
            return ((AttributeFilterMenu) this.f_97732_).whitelistMode == AttributeFilterMenu.WhitelistMode.BLACKLIST;
        } else if (indicator == this.whitelistConIndicator) {
            return ((AttributeFilterMenu) this.f_97732_).whitelistMode == AttributeFilterMenu.WhitelistMode.WHITELIST_CONJ;
        } else {
            return indicator == this.whitelistDisIndicator ? ((AttributeFilterMenu) this.f_97732_).whitelistMode == AttributeFilterMenu.WhitelistMode.WHITELIST_DISJ : false;
        }
    }
}