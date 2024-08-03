package net.mehvahdjukaar.moonlight.api.client.model.forge;

import com.google.common.base.Preconditions;
import java.util.Arrays;
import java.util.function.Consumer;
import net.mehvahdjukaar.moonlight.api.client.model.BakedQuadBuilder;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraftforge.client.model.QuadTransformers;
import net.minecraftforge.client.model.pipeline.QuadBakingVertexConsumer;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class BakedQuadBuilderImpl implements BakedQuadBuilder {

    private final QuadBakingVertexConsumer inner;

    private final TextureAtlasSprite sprite;

    private final Matrix4f globalTransform;

    private final Matrix3f normalTransf;

    private int emissivity = 0;

    private BakedQuad output;

    private boolean autoDirection = false;

    private Consumer<BakedQuad> quadConsumer = s -> this.output = s;

    public static BakedQuadBuilder create(TextureAtlasSprite sprite, @Nullable Matrix4f transformation) {
        return new BakedQuadBuilderImpl(sprite, transformation);
    }

    private BakedQuadBuilderImpl(TextureAtlasSprite sprite, @Nullable Matrix4f transformation) {
        this.inner = new QuadBakingVertexConsumer(s -> this.quadConsumer.accept(s));
        this.globalTransform = transformation;
        this.sprite = sprite;
        this.inner.setShade(true);
        this.inner.setHasAmbientOcclusion(true);
        this.inner.setSprite(sprite);
        this.normalTransf = transformation == null ? null : new Matrix3f(transformation).invert().transpose();
    }

    @Override
    public BakedQuad build() {
        Preconditions.checkNotNull(this.output, "vertex data has not been fully filled");
        if (this.emissivity != 0) {
            QuadTransformers.settingEmissivity(this.emissivity).processInPlace(this.output);
        }
        return this.output;
    }

    public BakedQuadBuilderImpl setAutoBuild(Consumer<BakedQuad> quadConsumer) {
        this.quadConsumer = quadConsumer;
        return this;
    }

    public BakedQuadBuilderImpl vertex(double x, double y, double z) {
        if (this.globalTransform != null) {
            this.inner.m_252986_(new Matrix4f(this.globalTransform), (float) x, (float) y, (float) z);
        } else {
            this.inner.vertex(x, y, z);
        }
        return this;
    }

    public BakedQuadBuilderImpl color(int red, int green, int blue, int alpha) {
        this.inner.color(red, green, blue, alpha);
        return this;
    }

    public BakedQuadBuilderImpl uv(float u, float v) {
        this.inner.uv(this.sprite.getU((double) (u * 16.0F)), this.sprite.getV((double) (v * 16.0F)));
        return this;
    }

    public BakedQuadBuilderImpl overlayCoords(int u, int v) {
        this.inner.overlayCoords(u, v);
        return this;
    }

    public BakedQuadBuilderImpl uv2(int u, int v) {
        this.inner.uv2(u, v);
        return this;
    }

    public BakedQuadBuilderImpl normal(float x, float y, float z) {
        if (this.globalTransform != null) {
            Vector3f normal = this.normalTransf.transform(new Vector3f(x, y, z));
            normal.normalize();
            this.inner.normal(normal.x, normal.y, normal.z);
        } else {
            this.inner.normal(x, y, z);
        }
        if (this.autoDirection) {
            this.setDirection(Direction.getNearest(x, y, z));
        }
        return this;
    }

    @Override
    public void endVertex() {
        this.inner.endVertex();
    }

    @Override
    public void defaultColor(int defaultR, int defaultG, int defaultB, int defaultA) {
        this.inner.defaultColor(defaultR, defaultG, defaultB, defaultA);
    }

    @Override
    public void unsetDefaultColor() {
        this.inner.unsetDefaultColor();
    }

    @Override
    public BakedQuadBuilder setDirection(Direction direction) {
        if (this.globalTransform != null) {
            direction = Direction.rotate(this.globalTransform, direction);
        }
        this.inner.setDirection(direction);
        return this;
    }

    @Override
    public BakedQuadBuilder setAmbientOcclusion(boolean ambientOcclusion) {
        this.inner.setHasAmbientOcclusion(ambientOcclusion);
        return this;
    }

    @Override
    public BakedQuadBuilder setTint(int tintIndex) {
        this.inner.setTintIndex(tintIndex);
        return this;
    }

    @Override
    public BakedQuadBuilder setShade(boolean shade) {
        this.inner.setShade(shade);
        return this;
    }

    @Override
    public BakedQuadBuilder lightEmission(int light) {
        this.emissivity = light;
        return this;
    }

    @Override
    public BakedQuadBuilder fromVanilla(BakedQuad q) {
        int[] v = Arrays.copyOf(q.getVertices(), q.getVertices().length);
        this.output = new BakedQuad(v, q.getTintIndex(), q.getDirection(), q.getSprite(), q.isShade(), q.hasAmbientOcclusion());
        return this;
    }

    @Override
    public BakedQuadBuilder setAutoDirection() {
        this.autoDirection = true;
        return this;
    }
}