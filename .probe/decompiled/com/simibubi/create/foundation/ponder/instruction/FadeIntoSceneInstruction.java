package com.simibubi.create.foundation.ponder.instruction;

import com.simibubi.create.foundation.ponder.ElementLink;
import com.simibubi.create.foundation.ponder.PonderScene;
import com.simibubi.create.foundation.ponder.element.AnimatedSceneElement;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

public abstract class FadeIntoSceneInstruction<T extends AnimatedSceneElement> extends TickingInstruction {

    protected Direction fadeInFrom;

    protected T element;

    private ElementLink<T> elementLink;

    public FadeIntoSceneInstruction(int fadeInTicks, Direction fadeInFrom, T element) {
        super(false, fadeInTicks);
        this.fadeInFrom = fadeInFrom;
        this.element = element;
    }

    @Override
    protected void firstTick(PonderScene scene) {
        super.firstTick(scene);
        scene.addElement(this.element);
        this.element.setVisible(true);
        this.element.setFade(0.0F);
        this.element.setFadeVec(this.fadeInFrom == null ? Vec3.ZERO : Vec3.atLowerCornerOf(this.fadeInFrom.getNormal()).scale(0.5));
        if (this.elementLink != null) {
            scene.linkElement(this.element, this.elementLink);
        }
    }

    @Override
    public void tick(PonderScene scene) {
        super.tick(scene);
        float fade = this.totalTicks == 0 ? 1.0F : (float) this.remainingTicks / (float) this.totalTicks;
        this.element.setFade(1.0F - fade * fade);
        if (this.remainingTicks == 0) {
            if (this.totalTicks == 0) {
                this.element.setFade(1.0F);
            }
            this.element.setFade(1.0F);
        }
    }

    public ElementLink<T> createLink(PonderScene scene) {
        this.elementLink = new ElementLink<>(this.getElementClass());
        scene.linkElement(this.element, this.elementLink);
        return this.elementLink;
    }

    protected abstract Class<T> getElementClass();
}