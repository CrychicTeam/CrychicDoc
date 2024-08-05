package fuzs.puzzleslib.api.client.event.v1;

import fuzs.puzzleslib.api.event.v1.core.EventInvoker;
import fuzs.puzzleslib.api.event.v1.core.EventResultHolder;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public final class ModelEvents {

    public static final EventInvoker<ModelEvents.ModifyUnbakedModel> MODIFY_UNBAKED_MODEL = EventInvoker.lookup(ModelEvents.ModifyUnbakedModel.class);

    public static final EventInvoker<ModelEvents.ModifyBakedModel> MODIFY_BAKED_MODEL = EventInvoker.lookup(ModelEvents.ModifyBakedModel.class);

    public static final EventInvoker<ModelEvents.AdditionalBakedModel> ADDITIONAL_BAKED_MODEL = EventInvoker.lookup(ModelEvents.AdditionalBakedModel.class);

    public static final EventInvoker<ModelEvents.AfterModelLoading> AFTER_MODEL_LOADING = EventInvoker.lookup(ModelEvents.AfterModelLoading.class);

    @Deprecated(forRemoval = true)
    public static final EventInvoker<ModelEvents.ModifyBakingResult> MODIFY_BAKING_RESULT = EventInvoker.lookup(ModelEvents.ModifyBakingResult.class);

    @Deprecated(forRemoval = true)
    public static final EventInvoker<ModelEvents.BakingCompleted> BAKING_COMPLETED = EventInvoker.lookup(ModelEvents.BakingCompleted.class);

    private ModelEvents() {
    }

    @Deprecated(forRemoval = true)
    public static EventInvoker<ModelEvents.ModifyBakingResult> modifyBakingResult(@Nullable String modId) {
        return EventInvoker.lookup(ModelEvents.ModifyBakingResult.class, modId);
    }

    @Deprecated(forRemoval = true)
    public static EventInvoker<ModelEvents.BakingCompleted> bakingCompleted(@Nullable String modId) {
        return EventInvoker.lookup(ModelEvents.BakingCompleted.class, modId);
    }

    @FunctionalInterface
    public interface AdditionalBakedModel {

        void onAdditionalBakedModel(BiConsumer<ResourceLocation, BakedModel> var1, Function<ResourceLocation, BakedModel> var2, Supplier<ModelBaker> var3);
    }

    @FunctionalInterface
    public interface AfterModelLoading {

        void onAfterModelLoading(Supplier<ModelManager> var1);
    }

    @Deprecated(forRemoval = true)
    @FunctionalInterface
    public interface BakingCompleted {

        void onBakingCompleted(Supplier<ModelManager> var1, Map<ResourceLocation, BakedModel> var2, Supplier<ModelBakery> var3);
    }

    @FunctionalInterface
    public interface ModifyBakedModel {

        EventResultHolder<BakedModel> onModifyBakedModel(ResourceLocation var1, Supplier<BakedModel> var2, Supplier<ModelBaker> var3, Function<ResourceLocation, BakedModel> var4, BiConsumer<ResourceLocation, BakedModel> var5);
    }

    @Deprecated(forRemoval = true)
    @FunctionalInterface
    public interface ModifyBakingResult {

        void onModifyBakingResult(Map<ResourceLocation, BakedModel> var1, Supplier<ModelBakery> var2);
    }

    @FunctionalInterface
    public interface ModifyUnbakedModel {

        EventResultHolder<UnbakedModel> onModifyUnbakedModel(ResourceLocation var1, Supplier<UnbakedModel> var2, Function<ResourceLocation, UnbakedModel> var3, BiConsumer<ResourceLocation, UnbakedModel> var4);
    }
}