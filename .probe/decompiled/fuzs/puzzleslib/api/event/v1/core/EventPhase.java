package fuzs.puzzleslib.api.event.v1.core;

import fuzs.puzzleslib.impl.PuzzlesLib;
import fuzs.puzzleslib.impl.event.core.EventPhaseImpl;
import java.util.function.BiConsumer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus.NonExtendable;

@NonExtendable
public interface EventPhase {

    EventPhase DEFAULT = new EventPhaseImpl(new ResourceLocation("fabric", "default"), null, null);

    EventPhase BEFORE = new EventPhaseImpl(PuzzlesLib.id("before"), DEFAULT, EventPhaseImpl.Ordering.BEFORE);

    EventPhase AFTER = new EventPhaseImpl(PuzzlesLib.id("after"), DEFAULT, EventPhaseImpl.Ordering.AFTER);

    EventPhase FIRST = new EventPhaseImpl(PuzzlesLib.id("first"), BEFORE, EventPhaseImpl.Ordering.BEFORE);

    EventPhase LAST = new EventPhaseImpl(PuzzlesLib.id("last"), AFTER, EventPhaseImpl.Ordering.AFTER);

    ResourceLocation identifier();

    EventPhase parent();

    void applyOrdering(BiConsumer<ResourceLocation, ResourceLocation> var1);

    static EventPhase early(EventPhase eventPhase) {
        return new EventPhaseImpl(PuzzlesLib.id("early_" + eventPhase.identifier().getPath()), eventPhase, EventPhaseImpl.Ordering.BEFORE);
    }

    static EventPhase late(EventPhase eventPhase) {
        return new EventPhaseImpl(PuzzlesLib.id("late_" + eventPhase.identifier().getPath()), eventPhase, EventPhaseImpl.Ordering.AFTER);
    }
}