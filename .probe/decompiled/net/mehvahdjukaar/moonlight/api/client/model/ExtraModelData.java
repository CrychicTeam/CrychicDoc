package net.mehvahdjukaar.moonlight.api.client.model;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import net.mehvahdjukaar.moonlight.api.client.model.forge.ExtraModelDataImpl;
import org.jetbrains.annotations.Nullable;

public interface ExtraModelData {

    ExtraModelData EMPTY = builder().build();

    @ExpectPlatform
    @Transformed
    static ExtraModelData.Builder builder() {
        return ExtraModelDataImpl.builder();
    }

    @Nullable
    <T> T get(ModelDataKey<T> var1);

    default boolean isEmpty() {
        return this == EMPTY;
    }

    public interface Builder {

        <A> ExtraModelData.Builder with(ModelDataKey<A> var1, A var2);

        ExtraModelData build();
    }
}