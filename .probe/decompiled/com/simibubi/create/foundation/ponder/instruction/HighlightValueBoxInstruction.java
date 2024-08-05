package com.simibubi.create.foundation.ponder.instruction;

import com.simibubi.create.foundation.ponder.PonderPalette;
import com.simibubi.create.foundation.ponder.PonderScene;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class HighlightValueBoxInstruction extends TickingInstruction {

    private Vec3 vec;

    private Vec3 expands;

    public HighlightValueBoxInstruction(Vec3 vec, Vec3 expands, int duration) {
        super(false, duration);
        this.vec = vec;
        this.expands = expands;
    }

    @Override
    public void tick(PonderScene scene) {
        super.tick(scene);
        AABB point = new AABB(this.vec, this.vec);
        AABB expanded = point.inflate(this.expands.x, this.expands.y, this.expands.z);
        scene.getOutliner().chaseAABB(this.vec, this.remainingTicks + 1 >= this.totalTicks ? point : expanded).lineWidth(0.06666667F).colored(PonderPalette.WHITE.getColor());
    }
}