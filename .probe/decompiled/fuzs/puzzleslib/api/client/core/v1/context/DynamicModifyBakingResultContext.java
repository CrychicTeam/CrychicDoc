package fuzs.puzzleslib.api.client.core.v1.context;

import java.util.Map;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.resources.ResourceLocation;

@Deprecated(forRemoval = true)
public interface DynamicModifyBakingResultContext {

    Map<ResourceLocation, BakedModel> models();

    ModelBakery modelBakery();
}