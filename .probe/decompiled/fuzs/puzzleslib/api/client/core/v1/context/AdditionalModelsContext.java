package fuzs.puzzleslib.api.client.core.v1.context;

import net.minecraft.resources.ResourceLocation;

@FunctionalInterface
public interface AdditionalModelsContext {

    void registerAdditionalModel(ResourceLocation... var1);
}