package com.simibubi.create.content.contraptions.render;

import com.jozufozu.flywheel.light.TickingLightListener;
import com.jozufozu.flywheel.util.box.GridAlignedBB;
import com.jozufozu.flywheel.util.box.ImmutableBox;
import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.infrastructure.config.AllConfigs;

public class NonStationaryLighter<C extends Contraption> extends ContraptionLighter<C> implements TickingLightListener {

    public NonStationaryLighter(C contraption) {
        super(contraption);
    }

    public boolean tickLightListener() {
        if (this.getVolume().volume() > AllConfigs.client().maxContraptionLightVolume.get()) {
            return false;
        } else {
            ImmutableBox contraptionBounds = this.getContraptionBounds();
            if (this.bounds.sameAs(contraptionBounds, 2)) {
                return false;
            } else {
                this.bounds.assign(contraptionBounds);
                growBoundsForEdgeData(this.bounds);
                this.lightVolume.move(this.bounds);
                return true;
            }
        }
    }

    @Override
    public GridAlignedBB getContraptionBounds() {
        GridAlignedBB bb = GridAlignedBB.from(this.contraption.bounds);
        bb.translate(this.contraption.entity.m_20183_());
        return bb;
    }
}