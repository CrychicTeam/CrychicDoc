package com.simibubi.create.foundation.block.connected;

import com.simibubi.create.foundation.block.render.SpriteShiftEntry;

public class CTSpriteShiftEntry extends SpriteShiftEntry {

    protected final CTType type;

    public CTSpriteShiftEntry(CTType type) {
        this.type = type;
    }

    public CTType getType() {
        return this.type;
    }

    public float getTargetU(float localU, int index) {
        float uOffset = (float) (index % this.type.getSheetSize());
        return this.getTarget().getU((double) ((getUnInterpolatedU(this.getOriginal(), localU) + uOffset * 16.0F) / (float) this.type.getSheetSize()));
    }

    public float getTargetV(float localV, int index) {
        float vOffset = (float) (index / this.type.getSheetSize());
        return this.getTarget().getV((double) ((getUnInterpolatedV(this.getOriginal(), localV) + vOffset * 16.0F) / (float) this.type.getSheetSize()));
    }
}