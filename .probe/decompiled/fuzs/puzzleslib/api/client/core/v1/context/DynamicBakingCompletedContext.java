package fuzs.puzzleslib.api.client.core.v1.context;

import fuzs.puzzleslib.api.client.core.v1.ClientAbstractions;
import java.util.Map;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.resources.ResourceLocation;

@Deprecated(forRemoval = true)
public interface DynamicBakingCompletedContext {

    ModelManager modelManager();

    Map<ResourceLocation, BakedModel> models();

    ModelBakery modelBakery();

    default BakedModel getModel(ResourceLocation identifier) {
        return ClientAbstractions.INSTANCE.getBakedModel(identifier);
    }
}