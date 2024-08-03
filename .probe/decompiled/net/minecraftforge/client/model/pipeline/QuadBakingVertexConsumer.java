package net.minecraftforge.client.model.pipeline;

import com.google.common.base.Preconditions;
import com.google.common.collect.UnmodifiableIterator;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Consumer;
import net.minecraft.Util;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraftforge.client.model.IQuadTransformer;
import net.minecraftforge.client.textures.UnitTextureAtlasSprite;

public class QuadBakingVertexConsumer implements VertexConsumer {

    private final Map<VertexFormatElement, Integer> ELEMENT_OFFSETS = Util.make(new IdentityHashMap(), map -> {
        int i = 0;
        UnmodifiableIterator var2 = DefaultVertexFormat.BLOCK.getElements().iterator();
        while (var2.hasNext()) {
            VertexFormatElement element = (VertexFormatElement) var2.next();
            map.put(element, DefaultVertexFormat.BLOCK.getOffset(i++) / 4);
        }
    });

    private static final int QUAD_DATA_SIZE = IQuadTransformer.STRIDE * 4;

    private final Consumer<BakedQuad> quadConsumer;

    int vertexIndex = 0;

    private int[] quadData = new int[QUAD_DATA_SIZE];

    private int tintIndex;

    private Direction direction = Direction.DOWN;

    private TextureAtlasSprite sprite = UnitTextureAtlasSprite.INSTANCE;

    private boolean shade;

    private boolean hasAmbientOcclusion;

    public QuadBakingVertexConsumer(Consumer<BakedQuad> quadConsumer) {
        this.quadConsumer = quadConsumer;
    }

    @Override
    public VertexConsumer vertex(double x, double y, double z) {
        int offset = this.vertexIndex * IQuadTransformer.STRIDE + IQuadTransformer.POSITION;
        this.quadData[offset] = Float.floatToRawIntBits((float) x);
        this.quadData[offset + 1] = Float.floatToRawIntBits((float) y);
        this.quadData[offset + 2] = Float.floatToRawIntBits((float) z);
        return this;
    }

    @Override
    public VertexConsumer normal(float x, float y, float z) {
        int offset = this.vertexIndex * IQuadTransformer.STRIDE + IQuadTransformer.NORMAL;
        this.quadData[offset] = (int) (x * 127.0F) & 0xFF | ((int) (y * 127.0F) & 0xFF) << 8 | ((int) (z * 127.0F) & 0xFF) << 16;
        return this;
    }

    @Override
    public VertexConsumer color(int r, int g, int b, int a) {
        int offset = this.vertexIndex * IQuadTransformer.STRIDE + IQuadTransformer.COLOR;
        this.quadData[offset] = (a & 0xFF) << 24 | (b & 0xFF) << 16 | (g & 0xFF) << 8 | r & 0xFF;
        return this;
    }

    @Override
    public VertexConsumer uv(float u, float v) {
        int offset = this.vertexIndex * IQuadTransformer.STRIDE + IQuadTransformer.UV0;
        this.quadData[offset] = Float.floatToRawIntBits(u);
        this.quadData[offset + 1] = Float.floatToRawIntBits(v);
        return this;
    }

    @Override
    public VertexConsumer overlayCoords(int u, int v) {
        if (IQuadTransformer.UV1 >= 0) {
            int offset = this.vertexIndex * IQuadTransformer.STRIDE + IQuadTransformer.UV1;
            this.quadData[offset] = u & 65535 | (v & 65535) << 16;
        }
        return this;
    }

    @Override
    public VertexConsumer uv2(int u, int v) {
        int offset = this.vertexIndex * IQuadTransformer.STRIDE + IQuadTransformer.UV2;
        this.quadData[offset] = u & 65535 | (v & 65535) << 16;
        return this;
    }

    public VertexConsumer misc(VertexFormatElement element, int... rawData) {
        Integer baseOffset = (Integer) this.ELEMENT_OFFSETS.get(element);
        if (baseOffset != null) {
            int offset = this.vertexIndex * IQuadTransformer.STRIDE + baseOffset;
            System.arraycopy(rawData, 0, this.quadData, offset, rawData.length);
        }
        return this;
    }

    @Override
    public void endVertex() {
        if (++this.vertexIndex == 4) {
            this.quadConsumer.accept(new BakedQuad(this.quadData, this.tintIndex, this.direction, this.sprite, this.shade, this.hasAmbientOcclusion));
            this.vertexIndex = 0;
            this.quadData = new int[QUAD_DATA_SIZE];
        }
    }

    @Override
    public void defaultColor(int r, int g, int b, int a) {
    }

    @Override
    public void unsetDefaultColor() {
    }

    public void setTintIndex(int tintIndex) {
        this.tintIndex = tintIndex;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void setSprite(TextureAtlasSprite sprite) {
        this.sprite = sprite;
    }

    public void setShade(boolean shade) {
        this.shade = shade;
    }

    public void setHasAmbientOcclusion(boolean hasAmbientOcclusion) {
        this.hasAmbientOcclusion = hasAmbientOcclusion;
    }

    public static class Buffered extends QuadBakingVertexConsumer {

        private final BakedQuad[] output;

        public Buffered() {
            this(new BakedQuad[1]);
        }

        private Buffered(BakedQuad[] output) {
            super(q -> output[0] = q);
            this.output = output;
        }

        public BakedQuad getQuad() {
            BakedQuad quad = (BakedQuad) Preconditions.checkNotNull(this.output[0], "No quad has been emitted. Vertices in buffer: " + this.vertexIndex);
            this.output[0] = null;
            return quad;
        }
    }
}