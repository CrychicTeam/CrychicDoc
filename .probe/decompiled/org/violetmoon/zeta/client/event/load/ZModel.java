package org.violetmoon.zeta.client.event.load;

import java.util.Map;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.geometry.IGeometryLoader;
import org.violetmoon.zeta.event.bus.IZetaLoadEvent;

public interface ZModel extends IZetaLoadEvent {

    public interface BakingCompleted extends ZModel {

        ModelManager getModelManager();

        Map<ResourceLocation, BakedModel> getModels();

        ModelBakery getModelBakery();
    }

    public interface ModifyBakingResult extends ZModel {

        Map<ResourceLocation, BakedModel> getModels();

        ModelBakery getModelBakery();
    }

    public interface RegisterAdditional extends ZModel {

        void register(ResourceLocation var1);
    }

    public interface RegisterGeometryLoaders extends ZModel {

        void register(String var1, IGeometryLoader<?> var2);
    }
}