package fuzs.puzzleslib.impl.client.core.context;

import fuzs.puzzleslib.api.client.core.v1.context.DynamicBakingCompletedContext;
import java.util.Map;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.resources.ResourceLocation;

public record DynamicBakingCompletedContextForgeImpl(ModelManager modelManager, Map<ResourceLocation, BakedModel> models, ModelBakery modelBakery) implements DynamicBakingCompletedContext {
}