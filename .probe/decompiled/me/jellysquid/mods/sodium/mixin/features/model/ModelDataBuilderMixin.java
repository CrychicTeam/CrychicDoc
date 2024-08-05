package me.jellysquid.mods.sodium.mixin.features.model;

import it.unimi.dsi.fastutil.objects.Reference2ReferenceArrayMap;
import it.unimi.dsi.fastutil.objects.Reference2ReferenceOpenHashMap;
import java.util.Map;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = { ModelData.Builder.class }, remap = false)
public class ModelDataBuilderMixin {

    @Redirect(method = { "build" }, at = @At(value = "INVOKE", target = "Ljava/util/Collections;unmodifiableMap(Ljava/util/Map;)Ljava/util/Map;"))
    private Map<ModelProperty<?>, Object> useEfficientMap(Map<ModelProperty<?>, Object> properties) {
        int size = properties.size();
        if (size >= 4) {
            return new Reference2ReferenceOpenHashMap(properties);
        } else {
            return size > 0 ? new Reference2ReferenceArrayMap(properties) : new Reference2ReferenceArrayMap();
        }
    }
}