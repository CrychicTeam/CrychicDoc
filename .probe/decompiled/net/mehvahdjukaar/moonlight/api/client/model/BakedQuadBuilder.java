package net.mehvahdjukaar.moonlight.api.client.model;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Transformation;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import java.util.function.Consumer;
import net.mehvahdjukaar.moonlight.api.client.model.forge.BakedQuadBuilderImpl;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public interface BakedQuadBuilder extends VertexConsumer {

    static BakedQuadBuilder create(TextureAtlasSprite sprite) {
        return create(sprite, (Matrix4f) null);
    }

    static BakedQuadBuilder create(TextureAtlasSprite sprite, @Nullable Transformation transformation) {
        return create(sprite, transformation == null ? null : new Matrix4f().translate(0.5F, 0.5F, 0.5F).mul(transformation.getMatrix()).translate(-0.5F, -0.5F, -0.5F));
    }

    @ExpectPlatform
    @Transformed
    static BakedQuadBuilder create(TextureAtlasSprite sprite, @Nullable Matrix4f transformation) {
        return BakedQuadBuilderImpl.create(sprite, transformation);
    }

    BakedQuadBuilder setAutoDirection();

    BakedQuadBuilder setDirection(Direction var1);

    BakedQuadBuilder setAmbientOcclusion(boolean var1);

    BakedQuadBuilder setShade(boolean var1);

    BakedQuadBuilder lightEmission(int var1);

    @Deprecated(forRemoval = true)
    BakedQuadBuilder fromVanilla(BakedQuad var1);

    BakedQuadBuilder setTint(int var1);

    BakedQuad build();

    BakedQuadBuilder setAutoBuild(Consumer<BakedQuad> var1);

    default BakedQuadBuilder vertex(Matrix4f matrix, float x, float y, float z) {
        VertexConsumer.super.vertex(matrix, x, y, z);
        return this;
    }

    default BakedQuadBuilder normal(Matrix3f matrix, float x, float y, float z) {
        VertexConsumer.super.normal(matrix, x, y, z);
        return this;
    }
}