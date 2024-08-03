package com.simibubi.create.foundation.ponder.instruction;

import com.simibubi.create.foundation.ponder.ElementLink;
import com.simibubi.create.foundation.ponder.PonderScene;
import com.simibubi.create.foundation.ponder.element.PonderSceneElement;
import java.util.function.BiConsumer;
import java.util.function.Function;
import net.minecraft.world.phys.Vec3;

public class AnimateElementInstruction<T extends PonderSceneElement> extends TickingInstruction {

    protected Vec3 deltaPerTick;

    protected Vec3 totalDelta;

    protected Vec3 target;

    protected ElementLink<T> link;

    protected T element;

    private BiConsumer<T, Vec3> setter;

    private Function<T, Vec3> getter;

    protected AnimateElementInstruction(ElementLink<T> link, Vec3 totalDelta, int ticks, BiConsumer<T, Vec3> setter, Function<T, Vec3> getter) {
        super(false, ticks);
        this.link = link;
        this.setter = setter;
        this.getter = getter;
        this.deltaPerTick = totalDelta.scale(1.0 / (double) ticks);
        this.totalDelta = totalDelta;
        this.target = totalDelta;
    }

    @Override
    protected final void firstTick(PonderScene scene) {
        super.firstTick(scene);
        this.element = scene.resolve(this.link);
        if (this.element != null) {
            this.target = ((Vec3) this.getter.apply(this.element)).add(this.totalDelta);
        }
    }

    @Override
    public void tick(PonderScene scene) {
        super.tick(scene);
        if (this.element != null) {
            if (this.remainingTicks == 0) {
                this.setter.accept(this.element, this.target);
                this.setter.accept(this.element, this.target);
            } else {
                this.setter.accept(this.element, ((Vec3) this.getter.apply(this.element)).add(this.deltaPerTick));
            }
        }
    }
}