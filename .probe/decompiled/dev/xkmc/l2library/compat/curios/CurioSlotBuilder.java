package dev.xkmc.l2library.compat.curios;

import java.util.ArrayList;

public record CurioSlotBuilder(int order, String icon, int size, CurioSlotBuilder.Operation operation, boolean add_cosmetic, boolean use_native_gui, boolean render_toggle, boolean replace, ArrayList<SlotCondition> conditions) {

    public CurioSlotBuilder(int order, String icon) {
        this(order, icon, 1, CurioSlotBuilder.Operation.SET);
    }

    public CurioSlotBuilder(int order, String icon, int size, CurioSlotBuilder.Operation operation) {
        this(order, icon, size, operation, false, true, true, false, SlotCondition.of());
    }

    public static enum Operation {

        SET, ADD, REMOVE
    }
}