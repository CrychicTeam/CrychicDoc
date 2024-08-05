package net.mehvahdjukaar.moonlight.api.client.model.forge;

import com.mojang.math.Transformation;
import java.util.Arrays;
import java.util.function.IntUnaryOperator;
import java.util.function.UnaryOperator;
import net.mehvahdjukaar.moonlight.api.client.model.BakedQuadsTransformer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraftforge.client.model.IQuadTransformer;
import net.minecraftforge.client.model.QuadTransformers;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class BakedQuadsTransformerImpl implements BakedQuadsTransformer {

    private IQuadTransformer inner = QuadTransformers.empty();

    private Boolean ambientOcclusion = null;

    private Boolean shade = null;

    private Integer tintIndex = null;

    private UnaryOperator<Direction> directionRemap = UnaryOperator.identity();

    private TextureAtlasSprite sprite = null;

    private TextureAtlasSprite lastSpriteHack = null;

    public static BakedQuadsTransformer create() {
        return new BakedQuadsTransformerImpl();
    }

    @Override
    public BakedQuadsTransformer applyingColor(IntUnaryOperator indexToABGR) {
        this.inner = this.inner.andThen(applyingColorInplace(indexToABGR));
        return this;
    }

    @Override
    public BakedQuadsTransformer applyingLightMap(int packedLight) {
        this.inner = this.inner.andThen(QuadTransformers.applyingLightmap(packedLight));
        return this;
    }

    @Override
    public BakedQuadsTransformer applyingTransform(Matrix4f transform) {
        Matrix4f m = new Matrix4f();
        m.translate(0.5F, 0.5F, 0.5F);
        m.mul(transform);
        m.translate(-0.5F, -0.5F, -0.5F);
        this.inner = this.inner.andThen(QuadTransformers.applying(new Transformation(m)));
        this.directionRemap = d -> Direction.rotate(new Matrix4f(new Matrix3f(transform)), d);
        return this;
    }

    @Override
    public BakedQuadsTransformer applyingAmbientOcclusion(boolean ambientOcclusion) {
        this.ambientOcclusion = ambientOcclusion;
        return this;
    }

    @Override
    public BakedQuadsTransformer applyingShade(boolean shade) {
        this.shade = shade;
        return this;
    }

    @Override
    public BakedQuadsTransformer applyingTintIndex(int tintIndex) {
        this.tintIndex = tintIndex;
        return this;
    }

    @Override
    public BakedQuadsTransformer applyingEmissivity(int emissivity) {
        this.inner = this.inner.andThen(QuadTransformers.settingEmissivity(emissivity));
        return this;
    }

    @Override
    public BakedQuadsTransformer applyingSprite(TextureAtlasSprite sprite) {
        this.inner = this.inner.andThen(this.applyingSpriteInplace(sprite));
        this.sprite = sprite;
        return this;
    }

    @Override
    public BakedQuad transform(BakedQuad quad) {
        int[] v = Arrays.copyOf(quad.getVertices(), quad.getVertices().length);
        int tint = this.tintIndex == null ? quad.getTintIndex() : this.tintIndex;
        boolean shade = this.shade == null ? quad.isShade() : this.shade;
        boolean ambientOcclusion = this.ambientOcclusion == null ? quad.hasAmbientOcclusion() : this.ambientOcclusion;
        this.lastSpriteHack = quad.getSprite();
        TextureAtlasSprite sprite = this.sprite == null ? quad.getSprite() : this.sprite;
        BakedQuad newQuad = new BakedQuad(v, tint, (Direction) this.directionRemap.apply(quad.getDirection()), sprite, shade, ambientOcclusion);
        this.inner.processInPlace(newQuad);
        this.lastSpriteHack = null;
        return newQuad;
    }

    private IQuadTransformer applyingSpriteInplace(TextureAtlasSprite sprite) {
        return q -> {
            TextureAtlasSprite oldSprite = this.lastSpriteHack;
            int stride = IQuadTransformer.STRIDE;
            int[] v = q.getVertices();
            float segmentWScale = (float) sprite.contents().width() / (float) oldSprite.contents().width();
            float segmentHScale = (float) sprite.contents().height() / (float) oldSprite.contents().height();
            for (int i = 0; i < 4; i++) {
                int offset = i * stride + IQuadTransformer.UV0;
                float originalU = Float.intBitsToFloat(v[offset]);
                float originalV = Float.intBitsToFloat(v[offset + 1]);
                float u1 = (originalU - oldSprite.getU0()) * segmentWScale;
                v[offset] = Float.floatToRawIntBits(u1 + sprite.getU0());
                float v1 = (originalV - oldSprite.getV0()) * segmentHScale;
                v[offset + 1] = Float.floatToRawIntBits(v1 + sprite.getV0());
            }
        };
    }

    private static IQuadTransformer applyingColorInplace(IntUnaryOperator indexToABGR) {
        return quad -> {
            int[] v = quad.getVertices();
            int stride = IQuadTransformer.STRIDE;
            for (int i = 0; i < 4; i++) {
                int i1 = indexToABGR.applyAsInt(i);
                v[i * stride + IQuadTransformer.COLOR] = i1;
            }
        };
    }
}