package com.simibubi.create.foundation.ponder.instruction;

import com.simibubi.create.foundation.ponder.PonderScene;
import com.simibubi.create.foundation.ponder.Selection;
import com.simibubi.create.foundation.ponder.element.WorldSectionElement;

public abstract class WorldModifyInstruction extends PonderInstruction {

    private Selection selection;

    public WorldModifyInstruction(Selection selection) {
        this.selection = selection;
    }

    @Override
    public boolean isComplete() {
        return true;
    }

    @Override
    public void tick(PonderScene scene) {
        this.runModification(this.selection, scene);
        if (this.needsRedraw()) {
            scene.forEach(WorldSectionElement.class, WorldSectionElement::queueRedraw);
        }
    }

    protected abstract void runModification(Selection var1, PonderScene var2);

    protected abstract boolean needsRedraw();
}