package com.simibubi.create.content.contraptions.bearing;

import com.jozufozu.flywheel.util.box.GridAlignedBB;
import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.contraptions.render.ContraptionLighter;

public class AnchoredLighter extends ContraptionLighter<Contraption> {

    public AnchoredLighter(Contraption contraption) {
        super(contraption);
    }

    @Override
    public GridAlignedBB getContraptionBounds() {
        GridAlignedBB bb = GridAlignedBB.from(this.contraption.bounds);
        bb.translate(this.contraption.anchor);
        return bb;
    }
}