package com.simibubi.create.foundation.ponder.instruction;

import com.simibubi.create.foundation.ponder.PonderScene;
import com.simibubi.create.foundation.ponder.element.AnimatedOverlayElement;
import com.simibubi.create.foundation.ponder.element.AnimatedSceneElement;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

public class HideAllInstruction extends TickingInstruction {

    private Direction fadeOutTo;

    public HideAllInstruction(int fadeOutTicks, Direction fadeOutTo) {
        super(false, fadeOutTicks);
        this.fadeOutTo = fadeOutTo;
    }

    @Override
    protected void firstTick(PonderScene scene) {
        super.firstTick(scene);
        scene.getElements().forEach(element -> {
            if (element instanceof AnimatedSceneElement animatedSceneElement) {
                animatedSceneElement.setFade(1.0F);
                animatedSceneElement.setFadeVec(this.fadeOutTo == null ? null : Vec3.atLowerCornerOf(this.fadeOutTo.getNormal()).scale(0.5));
            } else if (element instanceof AnimatedOverlayElement animatedSceneElement) {
                animatedSceneElement.setFade(1.0F);
            } else {
                element.setVisible(false);
            }
        });
    }

    @Override
    public void tick(PonderScene scene) {
        super.tick(scene);
        float fade = (float) this.remainingTicks / (float) this.totalTicks;
        scene.forEach(AnimatedSceneElement.class, ase -> {
            ase.setFade(fade * fade);
            if (this.remainingTicks == 0) {
                ase.setFade(0.0F);
            }
        });
        scene.forEach(AnimatedOverlayElement.class, aoe -> {
            aoe.setFade(fade * fade);
            if (this.remainingTicks == 0) {
                aoe.setFade(0.0F);
            }
        });
    }
}