package com.simibubi.create.foundation.ponder.instruction;

import com.simibubi.create.content.contraptions.glue.SuperGlueItem;
import com.simibubi.create.foundation.ponder.PonderScene;
import com.simibubi.create.foundation.ponder.Selection;
import com.simibubi.create.foundation.ponder.element.WorldSectionElement;
import java.util.Optional;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public class DisplayWorldSectionInstruction extends FadeIntoSceneInstruction<WorldSectionElement> {

    private Selection initialSelection;

    private Optional<Supplier<WorldSectionElement>> mergeOnto;

    private BlockPos glue;

    public DisplayWorldSectionInstruction(int fadeInTicks, Direction fadeInFrom, Selection selection, Optional<Supplier<WorldSectionElement>> mergeOnto) {
        this(fadeInTicks, fadeInFrom, selection, mergeOnto, null);
    }

    public DisplayWorldSectionInstruction(int fadeInTicks, Direction fadeInFrom, Selection selection, Optional<Supplier<WorldSectionElement>> mergeOnto, @Nullable BlockPos glue) {
        super(fadeInTicks, fadeInFrom, new WorldSectionElement(selection));
        this.initialSelection = selection;
        this.mergeOnto = mergeOnto;
        this.glue = glue;
    }

    @Override
    protected void firstTick(PonderScene scene) {
        super.firstTick(scene);
        this.mergeOnto.ifPresent(wse -> this.element.setAnimatedOffset(((WorldSectionElement) wse.get()).getAnimatedOffset(), true));
        this.element.set(this.initialSelection);
        this.element.setVisible(true);
    }

    @Override
    public void tick(PonderScene scene) {
        super.tick(scene);
        if (this.remainingTicks <= 0) {
            this.mergeOnto.ifPresent(c -> this.element.mergeOnto((WorldSectionElement) c.get()));
            if (this.glue != null) {
                SuperGlueItem.spawnParticles(scene.getWorld(), this.glue, this.fadeInFrom, true);
            }
        }
    }

    @Override
    protected Class<WorldSectionElement> getElementClass() {
        return WorldSectionElement.class;
    }
}