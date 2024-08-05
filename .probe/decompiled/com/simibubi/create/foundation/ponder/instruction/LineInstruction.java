package com.simibubi.create.foundation.ponder.instruction;

import com.simibubi.create.foundation.ponder.PonderPalette;
import com.simibubi.create.foundation.ponder.PonderScene;
import net.minecraft.world.phys.Vec3;

public class LineInstruction extends TickingInstruction {

    private PonderPalette color;

    private Vec3 start;

    private Vec3 end;

    private boolean big;

    public LineInstruction(PonderPalette color, Vec3 start, Vec3 end, int ticks, boolean big) {
        super(false, ticks);
        this.color = color;
        this.start = start;
        this.end = end;
        this.big = big;
    }

    @Override
    public void tick(PonderScene scene) {
        super.tick(scene);
        scene.getOutliner().showLine(this.start, this.start, this.end).lineWidth(this.big ? 0.125F : 0.0625F).colored(this.color.getColor());
    }
}