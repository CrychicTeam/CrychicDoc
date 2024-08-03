package fuzs.puzzleslib.api.client.init.v1;

import fuzs.puzzleslib.impl.client.init.ModelLayerFactoryImpl;
import net.minecraft.client.model.geom.ModelLayerLocation;

public interface ModelLayerFactory {

    static ModelLayerFactory from(String namespace) {
        return new ModelLayerFactoryImpl(namespace);
    }

    ModelLayerLocation register(String var1);

    ModelLayerLocation register(String var1, String var2);

    ModelLayerLocation registerInnerArmor(String var1);

    ModelLayerLocation registerOuterArmor(String var1);
}