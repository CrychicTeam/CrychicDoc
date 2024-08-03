package net.mehvahdjukaar.moonlight.api.client.model;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import java.util.ArrayList;
import java.util.List;
import java.util.function.IntUnaryOperator;
import net.mehvahdjukaar.moonlight.api.client.model.forge.BakedQuadsTransformerImpl;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.joml.Matrix4f;

public interface BakedQuadsTransformer {

    @ExpectPlatform
    @Transformed
    static BakedQuadsTransformer create() {
        return BakedQuadsTransformerImpl.create();
    }

    default List<BakedQuad> transformAll(List<BakedQuad> quads) {
        List<BakedQuad> list = new ArrayList();
        quads.forEach(q -> list.add(this.transform(q)));
        return list;
    }

    BakedQuad transform(BakedQuad var1);

    BakedQuadsTransformer applyingAmbientOcclusion(boolean var1);

    BakedQuadsTransformer applyingEmissivity(int var1);

    BakedQuadsTransformer applyingLightMap(int var1);

    BakedQuadsTransformer applyingShade(boolean var1);

    BakedQuadsTransformer applyingTintIndex(int var1);

    BakedQuadsTransformer applyingTransform(Matrix4f var1);

    default BakedQuadsTransformer applyingColor(int ABGRcolor) {
        this.applyingColor(i -> ABGRcolor);
        return this;
    }

    BakedQuadsTransformer applyingColor(IntUnaryOperator var1);

    BakedQuadsTransformer applyingSprite(TextureAtlasSprite var1);
}