package fuzs.puzzleslib.impl.event.core;

import fuzs.puzzleslib.api.event.v1.core.EventPhase;
import java.util.function.BiConsumer;
import net.minecraft.resources.ResourceLocation;

public record EventPhaseImpl(ResourceLocation identifier, EventPhase parent, EventPhaseImpl.Ordering ordering) implements EventPhase {

    @Override
    public void applyOrdering(BiConsumer<ResourceLocation, ResourceLocation> consumer) {
        this.ordering().apply(consumer, this.identifier(), this.parent().identifier());
    }

    public interface Ordering {

        EventPhaseImpl.Ordering BEFORE = (consumer, first, second) -> consumer.accept(first, second);

        EventPhaseImpl.Ordering AFTER = (consumer, first, second) -> consumer.accept(second, first);

        void apply(BiConsumer<ResourceLocation, ResourceLocation> var1, ResourceLocation var2, ResourceLocation var3);
    }
}