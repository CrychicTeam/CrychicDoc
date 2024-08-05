package com.mna.items.ritual;

import com.mna.items.filters.AllItemFilter;
import com.mna.items.filters.BottledWeaveItemFilter;
import com.mna.items.filters.ItemFilter;
import com.mna.items.filters.MarkingRuneFilter;
import com.mna.items.filters.MoteItemFilter;
import com.mna.items.filters.RuneItemFilter;
import java.util.function.Supplier;
import javax.annotation.Nullable;

public enum PractitionersPouchPatches {

    COLLECTION,
    CONVEYANCE(1, 77, 1, 1, 96, 96, 96, 77, () -> new MarkingRuneFilter()),
    DEPTH(2),
    GLYPH(1, 58, 4, 4, 0, 0, 96, 96, () -> new RuneItemFilter()),
    MOTE(1, 77, 3, 4, 0, 96, 96, 77, () -> new MoteItemFilter()),
    RIFT,
    SPEED(3),
    WEAVE(1, 77, 3, 4, 0, 174, 96, 77, () -> new BottledWeaveItemFilter()),
    VOID(2, 58, 4, 4, 0, 0, 96, 96, () -> new AllItemFilter());

    private final int levels;

    private final boolean hasInventory;

    private final int inventoryRows;

    private final int inventoryCols;

    private final int gui_u;

    private final int gui_v;

    private final int gui_w;

    private final int gui_h;

    private final int gui_sheet;

    private final Supplier<ItemFilter> slotFilterSupplier;

    private final int guiRowStart;

    private PractitionersPouchPatches() {
        this.levels = 1;
        this.hasInventory = false;
        this.inventoryRows = 0;
        this.inventoryCols = 0;
        this.gui_u = 0;
        this.gui_v = 0;
        this.gui_w = 0;
        this.gui_h = 0;
        this.gui_sheet = 0;
        this.guiRowStart = 0;
        this.slotFilterSupplier = null;
    }

    private PractitionersPouchPatches(int levels) {
        this.levels = levels;
        this.hasInventory = false;
        this.inventoryRows = 0;
        this.inventoryCols = 0;
        this.gui_u = 0;
        this.gui_v = 0;
        this.gui_w = 0;
        this.gui_h = 0;
        this.gui_sheet = 0;
        this.guiRowStart = 0;
        this.slotFilterSupplier = null;
    }

    private PractitionersPouchPatches(int guiSheetIndex, int guiRowStart, int invRows, int invCols, int u, int v, int w, int h, Supplier<ItemFilter> filter) {
        this.levels = 1;
        this.hasInventory = true;
        this.inventoryRows = invRows;
        this.inventoryCols = invCols;
        this.gui_u = u;
        this.gui_v = v;
        this.gui_w = w;
        this.gui_h = h;
        this.gui_sheet = guiSheetIndex;
        this.guiRowStart = guiRowStart;
        this.slotFilterSupplier = filter;
    }

    public int getLevels() {
        return this.levels;
    }

    public int getInventoryRows() {
        return this.inventoryRows;
    }

    public int getInventoryCols() {
        return this.inventoryCols;
    }

    public boolean hasInventory() {
        return this.hasInventory;
    }

    public int guiU() {
        return this.gui_u;
    }

    public int guiV() {
        return this.gui_v;
    }

    public int guiW() {
        return this.gui_w;
    }

    public int guiH() {
        return this.gui_h;
    }

    public int guiSheet() {
        return this.gui_sheet;
    }

    @Nullable
    public ItemFilter getSlotFilter() {
        return this.slotFilterSupplier != null ? (ItemFilter) this.slotFilterSupplier.get() : null;
    }

    public int getRowStart() {
        return this.guiRowStart;
    }
}