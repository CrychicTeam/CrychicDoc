package io.github.lightman314.lightmanscurrency.client.gui.screen.inventory.traderstorage.slot_machine;

import io.github.lightman314.lightmanscurrency.LCText;
import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.api.traders.menu.storage.TraderStorageClientTab;
import io.github.lightman314.lightmanscurrency.client.gui.widget.ScrollListener;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.icon.IconData;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyButton;
import io.github.lightman314.lightmanscurrency.client.gui.widget.scroll.IScrollable;
import io.github.lightman314.lightmanscurrency.client.gui.widget.scroll.ScrollBarWidget;
import io.github.lightman314.lightmanscurrency.client.gui.widget.slot_machine.SlotMachineEntryEditWidget;
import io.github.lightman314.lightmanscurrency.client.util.IconAndButtonUtil;
import io.github.lightman314.lightmanscurrency.client.util.ScreenArea;
import io.github.lightman314.lightmanscurrency.common.menus.traderstorage.slot_machine.SlotMachineEntryTab;
import io.github.lightman314.lightmanscurrency.common.traders.slot_machine.SlotMachineEntry;
import io.github.lightman314.lightmanscurrency.common.traders.slot_machine.SlotMachineTraderData;
import io.github.lightman314.lightmanscurrency.util.MathUtil;
import java.util.List;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.network.chat.MutableComponent;

public class SlotMachineEntryClientTab extends TraderStorageClientTab<SlotMachineEntryTab> implements IScrollable {

    public static final int ENTRY_ROWS = 3;

    public static final int ENTRY_COLUMNS = 2;

    public static final int ENTRIES_PER_PAGE = 6;

    private int scroll = 0;

    private EasyButton buttonAddEntry;

    public SlotMachineEntryClientTab(Object screen, SlotMachineEntryTab commonTab) {
        super(screen, commonTab);
    }

    @Nonnull
    @Override
    public IconData getIcon() {
        return IconAndButtonUtil.ICON_TRADER_ALT;
    }

    public MutableComponent getTooltip() {
        return LCText.TOOLTIP_TRADER_SLOT_MACHINE_EDIT_ENTRIES.get();
    }

    @Override
    public void initialize(ScreenArea screenArea, boolean firstOpen) {
        this.addChild(new ScrollListener(screenArea.pos, screenArea.width, 145, this));
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 2; x++) {
                this.addChild(new SlotMachineEntryEditWidget(screenArea.pos.offset(19 + x * 80, 10 + y * 46), this, this.supplierForIndex(y * 2 + x)));
            }
        }
        this.addChild(new ScrollBarWidget(screenArea.pos.offset(179, 10), 138, this));
        this.buttonAddEntry = this.addChild(IconAndButtonUtil.plusButton(screenArea.pos.offset(screenArea.width - 14, 4), this::AddEntry));
        this.tick();
        this.menu.SetCoinSlotsActive(false);
    }

    @Override
    public void closeAction() {
        this.menu.SetCoinSlotsActive(true);
    }

    @Nullable
    public SlotMachineEntry getEntry(int entryIndex) {
        if (this.menu.getTrader() instanceof SlotMachineTraderData trader) {
            List<SlotMachineEntry> entries = trader.getAllEntries();
            return entryIndex >= 0 && entryIndex < entries.size() ? (SlotMachineEntry) entries.get(entryIndex) : null;
        } else {
            return null;
        }
    }

    private Supplier<Integer> supplierForIndex(int index) {
        return () -> this.scroll * 2 + index;
    }

    @Override
    public void tick() {
        if (this.menu.getTrader() instanceof SlotMachineTraderData trader) {
            trader.clearEntriesChangedCache();
        }
        this.validateScroll();
        this.buttonAddEntry.f_93624_ = this.menu.hasPermission("editTrades");
    }

    @Override
    public void renderBG(@Nonnull EasyGuiGraphics gui) {
    }

    private void AddEntry(EasyButton button) {
        this.commonTab.AddEntry();
    }

    @Override
    public int currentScroll() {
        return this.scroll;
    }

    @Override
    public void setScroll(int newScroll) {
        this.scroll = MathUtil.clamp(newScroll, 0, this.getMaxScroll());
    }

    private int getEntryCount() {
        return this.menu.getTrader() instanceof SlotMachineTraderData trader ? trader.getAllEntries().size() : 0;
    }

    @Override
    public int getMaxScroll() {
        return IScrollable.calculateMaxScroll(6, 2, this.getEntryCount());
    }
}