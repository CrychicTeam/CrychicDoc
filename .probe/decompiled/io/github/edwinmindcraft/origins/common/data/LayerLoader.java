package io.github.edwinmindcraft.origins.common.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.DataResult;
import io.github.apace100.origins.Origins;
import io.github.edwinmindcraft.calio.api.registry.DynamicEntryFactory;
import io.github.edwinmindcraft.calio.api.registry.DynamicEntryValidator;
import io.github.edwinmindcraft.calio.api.registry.ICalioDynamicRegistryManager;
import io.github.edwinmindcraft.origins.api.data.PartialGuiTitle;
import io.github.edwinmindcraft.origins.api.data.PartialLayer;
import io.github.edwinmindcraft.origins.api.origin.ConditionedOrigin;
import io.github.edwinmindcraft.origins.api.origin.OriginLayer;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public enum LayerLoader implements DynamicEntryValidator<OriginLayer>, DynamicEntryFactory<OriginLayer> {

    INSTANCE;

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(PartialLayer.class, PartialLayer.Serializer.INSTANCE).registerTypeAdapter(PartialGuiTitle.class, PartialGuiTitle.Serializer.INSTANCE).registerTypeAdapter(ConditionedOrigin.class, ConditionedOrigin.Serializer.INSTANCE).create();

    public OriginLayer accept(@NotNull ResourceLocation resourceLocation, @NotNull List<JsonElement> list) {
        Optional<PartialLayer> reduce = list.stream().flatMap(x -> {
            try {
                return Stream.of((PartialLayer) GSON.fromJson(x, PartialLayer.class));
            } catch (Exception var3x) {
                Origins.LOGGER.error("There was a problem reading Origin layer file {} (skipping): {}: {}", resourceLocation, var3x.getClass(), var3x.getMessage(), var3x);
                return Stream.empty();
            }
        }).sorted(PartialLayer.LOADING_COMPARATOR).reduce(PartialLayer::merge);
        if (reduce.isPresent()) {
            return ((PartialLayer) reduce.get()).create(resourceLocation);
        } else {
            Origins.LOGGER.error("All instances of layer {} failed to load. Skipped", resourceLocation);
            return null;
        }
    }

    @NotNull
    public DataResult<OriginLayer> validate(@NotNull ResourceLocation resourceLocation, @NotNull OriginLayer originLayer, @NotNull ICalioDynamicRegistryManager manager) {
        return DataResult.success(originLayer.cleanup(manager));
    }
}