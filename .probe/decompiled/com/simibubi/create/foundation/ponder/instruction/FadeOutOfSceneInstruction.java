package com.simibubi.create.foundation.ponder.instruction;

import com.simibubi.create.foundation.ponder.ElementLink;
import com.simibubi.create.foundation.ponder.PonderScene;
import com.simibubi.create.foundation.ponder.element.AnimatedSceneElement;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

public class FadeOutOfSceneInstruction<T extends AnimatedSceneElement> extends TickingInstruction {

    private Direction fadeOutTo;

    private ElementLink<T> link;

    private T element;

    public FadeOutOfSceneInstruction(int fadeOutTicks, Direction fadeOutTo, ElementLink<T> link) {
        super(false, fadeOutTicks);
        this.fadeOutTo = fadeOutTo == null ? null : fadeOutTo.getOpposite();
        this.link = link;
    }

    @Override
    protected void firstTick(PonderScene scene) {
        super.firstTick(scene);
        this.element = scene.resolve(this.link);
        if (this.element != null) {
            this.element.setVisible(true);
            this.element.setFade(1.0F);
            this.element.setFadeVec(this.fadeOutTo == null ? Vec3.ZERO : Vec3.atLowerCornerOf(this.fadeOutTo.getNormal()).scale(0.5));
        }
    }

    @Override
    public void tick(PonderScene scene) {
        super.tick(scene);
        if (this.element != null) {
            float fade = (float) this.remainingTicks / (float) this.totalTicks;
            this.element.setFade(1.0F - (1.0F - fade) * (1.0F - fade));
            if (this.remainingTicks == 0) {
                this.element.setVisible(false);
                this.element.setFade(0.0F);
            }
        }
    }
}