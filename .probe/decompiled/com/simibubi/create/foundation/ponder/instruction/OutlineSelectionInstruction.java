package com.simibubi.create.foundation.ponder.instruction;

import com.simibubi.create.foundation.ponder.PonderPalette;
import com.simibubi.create.foundation.ponder.PonderScene;
import com.simibubi.create.foundation.ponder.Selection;

public class OutlineSelectionInstruction extends TickingInstruction {

    private PonderPalette color;

    private Object slot;

    private Selection selection;

    public OutlineSelectionInstruction(PonderPalette color, Object slot, Selection selection, int ticks) {
        super(false, ticks);
        this.color = color;
        this.slot = slot;
        this.selection = selection;
    }

    @Override
    public void tick(PonderScene scene) {
        super.tick(scene);
        this.selection.makeOutline(scene.getOutliner(), this.slot).lineWidth(0.0625F).colored(this.color.getColor());
    }
}