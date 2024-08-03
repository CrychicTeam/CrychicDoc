package io.github.lightman314.lightmanscurrency.client.gui.widget.slot_machine;

import com.google.common.collect.ImmutableList;
import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.client.gui.easy.EasyScreenHelper;
import io.github.lightman314.lightmanscurrency.client.gui.easy.WidgetAddon;
import io.github.lightman314.lightmanscurrency.client.gui.easy.interfaces.ITooltipSource;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyWidget;
import io.github.lightman314.lightmanscurrency.client.util.ScreenPosition;
import io.github.lightman314.lightmanscurrency.common.traders.slot_machine.SlotMachineEntry;
import io.github.lightman314.lightmanscurrency.common.traders.slot_machine.SlotMachineTraderData;
import java.util.List;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class SlotMachineEntryDisplayWidget extends EasyWidget implements ITooltipSource {

    public static final int WIDTH = 80;

    public static final int HEIGHT = 46;

    public final Supplier<SlotMachineTraderData> trader;

    public final Supplier<Integer> index;

    private static final int ITEM_POSY = 22;

    public SlotMachineEntryDisplayWidget(ScreenPosition pos, Supplier<SlotMachineTraderData> trader, Supplier<Integer> index) {
        this(pos.x, pos.y, trader, index);
    }

    public SlotMachineEntryDisplayWidget(int x, int y, Supplier<SlotMachineTraderData> trader, Supplier<Integer> index) {
        super(x, y, 80, 46);
        this.trader = trader;
        this.index = index;
    }

    public SlotMachineEntryDisplayWidget withAddons(WidgetAddon... addons) {
        this.withAddonsInternal(addons);
        return this;
    }

    @Nullable
    private SlotMachineEntry getEntry() {
        SlotMachineTraderData trader = (SlotMachineTraderData) this.trader.get();
        if (trader != null) {
            int index = (Integer) this.index.get();
            List<SlotMachineEntry> entries = trader.getValidEntries();
            if (index >= 0 && index < entries.size()) {
                return (SlotMachineEntry) entries.get(index);
            }
        }
        return null;
    }

    @Override
    public void renderWidget(@Nonnull EasyGuiGraphics gui) {
        SlotMachineEntry entry = this.getEntry();
        SlotMachineTraderData trader = (SlotMachineTraderData) this.trader.get();
        if (trader != null && entry != null) {
            gui.drawString(LCText.GUI_TRADER_SLOT_MACHINE_ENTRY_LABEL.get((Integer) this.index.get() + 1), 0, 0, 4210752);
            gui.drawString(LCText.GUI_TRADER_SLOT_MACHINE_ODDS_LABEL.get(trader.getOdds(entry.getWeight())), 0, 12, 4210752);
            for (int i = 0; i < 4; i++) {
                if (i < entry.items.size() && !((ItemStack) entry.items.get(i)).isEmpty()) {
                    gui.renderItem((ItemStack) entry.items.get(i), 18 * i, 22);
                }
            }
        }
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
    public List<Component> getTooltipText(int mouseX, int mouseY) {
        if (!this.isVisible()) {
            return null;
        } else {
            SlotMachineEntry entry = this.getEntry();
            if (entry != null && mouseY >= this.m_252907_() + 22 && mouseY < this.m_252907_() + 22 + 16) {
                int itemIndex = this.getItemSlotIndex((double) mouseX);
                if (itemIndex >= 0 && itemIndex < entry.items.size()) {
                    if (entry.isMoney()) {
                        return ImmutableList.of(LCText.TOOLTIP_SLOT_MACHINE_MONEY.get(entry.getMoneyValue().getText()));
                    }
                    ItemStack item = (ItemStack) entry.items.get(itemIndex);
                    if (!item.isEmpty()) {
                        return EasyScreenHelper.getTooltipFromItem(item);
                    }
                }
            }
            return null;
        }
    }
}