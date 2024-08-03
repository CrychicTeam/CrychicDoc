package net.mehvahdjukaar.moonlight.api.client.model;

import java.util.Objects;
import org.jetbrains.annotations.ApiStatus.Internal;

public interface IExtraModelDataProvider {

    @Deprecated(forRemoval = true)
    @Internal
    default ExtraModelData getExtraModelData() {
        ExtraModelData.Builder builder = ExtraModelData.builder();
        this.addExtraModelData(builder);
        return builder.build();
    }

    default void addExtraModelData(ExtraModelData.Builder builder) {
    }

    default void requestModelReload() {
    }

    default void afterDataPacket(ExtraModelData oldData) {
        if (!Objects.equals(oldData, this.getExtraModelData())) {
            this.requestModelReload();
        }
    }
}