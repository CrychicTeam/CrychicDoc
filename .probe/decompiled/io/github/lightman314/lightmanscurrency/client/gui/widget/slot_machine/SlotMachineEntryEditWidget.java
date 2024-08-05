package io.github.lightman314.lightmanscurrency.client.gui.widget.slot_machine;

import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.api.misc.EasyText;
import io.github.lightman314.lightmanscurrency.api.misc.IEasyTickable;
import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.client.gui.easy.EasyScreenHelper;
import io.github.lightman314.lightmanscurrency.client.gui.easy.WidgetAddon;
import io.github.lightman314.lightmanscurrency.client.gui.easy.interfaces.ITooltipSource;
import io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.traderstorage.slot_machine.SlotMachineEntryClientTab;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.PlainButton;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyButton;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyWidgetWithChildren;
import io.github.lightman314.lightmanscurrency.client.util.IconAndButtonUtil;
import io.github.lightman314.lightmanscurrency.client.util.ScreenPosition;
import io.github.lightman314.lightmanscurrency.client.util.TextInputUtil;
import io.github.lightman314.lightmanscurrency.common.menus.slots.easy.EasySlot;
import io.github.lightman314.lightmanscurrency.common.traders.slot_machine.SlotMachineEntry;
import io.github.lightman314.lightmanscurrency.common.traders.slot_machine.SlotMachineTraderData;
import io.github.lightman314.lightmanscurrency.util.InventoryUtil;
import java.util.List;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class SlotMachineEntryEditWidget extends EasyWidgetWithChildren implements IEasyTickable, ITooltipSource {

    public static final int WIDTH = 80;

    public static final int HEIGHT = 46;

    public final SlotMachineEntryClientTab tab;

    public final Supplier<Integer> entryIndex;

    private EditBox weightEdit;

    private PlainButton removeEntryButton;

    private int previousIndex = -1;

    private static final int ITEM_POSY = 22;

    public SlotMachineEntryEditWidget(ScreenPosition pos, SlotMachineEntryClientTab tab, Supplier<Integer> entryIndex) {
        this(pos.x, pos.y, tab, entryIndex);
    }

    public SlotMachineEntryEditWidget(int x, int y, SlotMachineEntryClientTab tab, Supplier<Integer> entryIndex) {
        super(x, y, 80, 46);
        this.tab = tab;
        this.entryIndex = entryIndex;
    }

    public SlotMachineEntryEditWidget withAddons(WidgetAddon... addons) {
        this.withAddonsInternal(addons);
        return this;
    }

    @Override
    public void addChildren() {
        this.weightEdit = this.addChild(new EditBox(this.tab.getFont(), this.m_252754_() + this.tab.getFont().width(LCText.GUI_TRADER_SLOT_MACHINE_WEIGHT_LABEL.get()), this.m_252907_() + 10, 36, 10, EasyText.empty()));
        this.weightEdit.setMaxLength(4);
        this.removeEntryButton = this.addChild(IconAndButtonUtil.minusButton(this.m_252754_(), this.m_252907_(), this::Remove));
    }

    private SlotMachineEntry getEntry() {
        return this.tab.getEntry((Integer) this.entryIndex.get());
    }

    private void Remove(EasyButton button) {
        this.tab.commonTab.RemoveEntry((Integer) this.entryIndex.get());
    }

    @Override
    public void renderWidget(@Nonnull EasyGuiGraphics gui) {
        SlotMachineEntry entry = this.getEntry();
        if (entry != null) {
            gui.drawString(LCText.GUI_TRADER_SLOT_MACHINE_ENTRY_LABEL.get((Integer) this.entryIndex.get() + 1), 12, 0, 4210752);
            gui.drawString(LCText.GUI_TRADER_SLOT_MACHINE_WEIGHT_LABEL.get(), 0, 12, 4210752);
            for (int i = 0; i < 4; i++) {
                if (i < entry.items.size() && !((ItemStack) entry.items.get(i)).isEmpty()) {
                    gui.renderItem((ItemStack) entry.items.get(i), 18 * i, 22);
                } else {
                    gui.renderSlotBackground(EasySlot.BACKGROUND, 18 * i, 22);
                }
            }
        }
    }

    @Override
    protected boolean isValidClickButton(int button) {
        return button == 0 || button == 1;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.m_93680_(mouseX, mouseY) && this.isValidClickButton(button)) {
            boolean rightClick = button == 1;
            SlotMachineEntry entry = this.getEntry();
            if (entry != null) {
                int entryIndex = (Integer) this.entryIndex.get();
                ItemStack heldItem = this.tab.menu.getHeldItem();
                if (mouseY >= (double) (this.m_252907_() + 22) && mouseY < (double) (this.m_252907_() + 22 + 16)) {
                    int itemIndex = this.getItemSlotIndex(mouseX);
                    if (itemIndex >= 0) {
                        if (itemIndex >= entry.items.size()) {
                            if (!heldItem.isEmpty()) {
                                if (rightClick) {
                                    this.tab.commonTab.AddEntryItem(entryIndex, heldItem.copyWithCount(1));
                                } else {
                                    this.tab.commonTab.AddEntryItem(entryIndex, heldItem);
                                }
                                return true;
                            }
                        } else {
                            if (heldItem.isEmpty()) {
                                if (rightClick) {
                                    ItemStack newStack = ((ItemStack) entry.items.get(itemIndex)).copy();
                                    newStack.shrink(1);
                                    if (newStack.isEmpty()) {
                                        this.tab.commonTab.RemoveEntryItem(entryIndex, itemIndex);
                                    } else {
                                        this.tab.commonTab.EditEntryItem(entryIndex, itemIndex, newStack);
                                    }
                                } else {
                                    this.tab.commonTab.RemoveEntryItem(entryIndex, itemIndex);
                                }
                                return true;
                            }
                            if (rightClick) {
                                ItemStack oldStack = (ItemStack) entry.items.get(itemIndex);
                                if (InventoryUtil.ItemMatches(heldItem, oldStack)) {
                                    ItemStack newStack = ((ItemStack) entry.items.get(itemIndex)).copy();
                                    if (newStack.getCount() >= newStack.getMaxStackSize()) {
                                        return false;
                                    }
                                    newStack.grow(1);
                                    this.tab.commonTab.EditEntryItem(entryIndex, itemIndex, newStack);
                                } else {
                                    this.tab.commonTab.EditEntryItem(entryIndex, itemIndex, heldItem.copyWithCount(1));
                                }
                                return true;
                            }
                            this.tab.commonTab.EditEntryItem(entryIndex, itemIndex, heldItem);
                        }
                    }
                }
            }
        }
        return false;
    }

    private int getItemSlotIndex(double mouseX) {
        int x = (int) mouseX - this.m_252754_();
        if (x < 0) {
            return -1;
        } else {
            int result = x / 18;
            return result >= 4 ? -1 : result;
        }
    }

    @Override
    public void tick() {
        SlotMachineEntry entry = this.getEntry();
        if (entry != null && this.tab.menu.getTrader() instanceof SlotMachineTraderData trader) {
            this.weightEdit.f_93624_ = true;
            boolean hasPerms = this.tab.menu.hasPermission("editTrades");
            this.removeEntryButton.f_93624_ = hasPerms;
            this.weightEdit.setEditable(hasPerms);
            if (trader.areEntriesChanged()) {
                this.weightEdit.setValue(Integer.toString(entry.getWeight()));
                return;
            }
            int thisIndex = (Integer) this.entryIndex.get();
            if (thisIndex != this.previousIndex) {
                this.weightEdit.setValue(Integer.toString(entry.getWeight()));
            }
            int newWeight = TextInputUtil.getIntegerValue(this.weightEdit, 1);
            if (newWeight != entry.getWeight()) {
                this.tab.commonTab.ChangeEntryWeight(thisIndex, newWeight);
            }
            this.previousIndex = thisIndex;
        } else {
            this.weightEdit.f_93624_ = this.removeEntryButton.f_93624_ = false;
        }
        TextInputUtil.whitelistInteger(this.weightEdit, 1L, 1000L);
    }

    @Override
    public List<Component> getTooltipText(int mouseX, int mouseY) {
        SlotMachineEntry entry = this.getEntry();
        if (entry != null && mouseY >= this.m_252907_() + 22 && mouseY < this.m_252907_() + 22 + 16) {
            int itemIndex = this.getItemSlotIndex((double) mouseX);
            if (itemIndex >= 0 && itemIndex < entry.items.size()) {
                ItemStack item = (ItemStack) entry.items.get(itemIndex);
                if (!item.isEmpty()) {
                    return EasyScreenHelper.getTooltipFromItem(item);
                }
            }
        }
        return null;
    }
}