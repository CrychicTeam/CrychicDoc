package com.simibubi.create.content.contraptions.pulley;

import com.jozufozu.flywheel.util.box.GridAlignedBB;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.contraptions.render.ContraptionLighter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class PulleyLighter extends ContraptionLighter<PulleyContraption> {

    public PulleyLighter(PulleyContraption contraption) {
        super(contraption);
    }

    @Override
    public GridAlignedBB getContraptionBounds() {
        GridAlignedBB bounds = GridAlignedBB.from(this.contraption.bounds);
        Level world = this.contraption.entity.m_9236_();
        BlockPos.MutableBlockPos pos = this.contraption.anchor.mutable();
        while (!AllBlocks.ROPE_PULLEY.has(world.getBlockState(pos)) && pos.m_123342_() < world.m_151558_()) {
            pos.move(0, 1, 0);
        }
        bounds.translate(pos);
        bounds.setMinY(world.m_141937_());
        return bounds;
    }
}