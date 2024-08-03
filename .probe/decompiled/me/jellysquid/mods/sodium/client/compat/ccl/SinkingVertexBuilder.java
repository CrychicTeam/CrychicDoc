package me.jellysquid.mods.sodium.client.compat.ccl;

import com.mojang.blaze3d.vertex.VertexConsumer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import javax.annotation.Nonnull;
import me.jellysquid.mods.sodium.client.model.quad.ModelQuadView;
import me.jellysquid.mods.sodium.client.model.quad.properties.ModelQuadFacing;
import me.jellysquid.mods.sodium.client.render.chunk.compile.buffers.ChunkModelBuilder;
import me.jellysquid.mods.sodium.client.render.chunk.terrain.material.Material;
import me.jellysquid.mods.sodium.client.render.chunk.vertex.builder.ChunkMeshBufferBuilder;
import me.jellysquid.mods.sodium.client.render.chunk.vertex.format.ChunkVertexEncoder;
import me.jellysquid.mods.sodium.client.util.ModelQuadUtil;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.embeddedt.embeddium.render.frapi.SpriteFinderCache;
import org.joml.Vector3fc;

@OnlyIn(Dist.CLIENT)
public final class SinkingVertexBuilder implements VertexConsumer {

    private static final int VERTEX_SIZE_BYTES = 32;

    private static final int INITIAL_CAPACITY = 16384;

    private static final ByteBuffer EMPTY_BUFFER = ByteBuffer.allocate(0);

    private ByteBuffer buffer = EMPTY_BUFFER;

    private final int[] sideCount = new int[ModelQuadFacing.VALUES.length];

    private int currentVertex;

    private float x;

    private float y;

    private float z;

    private float u;

    private float v;

    private int color;

    private int light;

    private int fixedColor;

    private boolean hasFixedColor = false;

    private final ChunkVertexEncoder.Vertex[] sodiumVertexArray = ChunkVertexEncoder.Vertex.uninitializedQuad();

    private final ModelQuadView previousQuad = new ModelQuadView() {

        private int getBaseIndex(int idx, int offset) {
            return (SinkingVertexBuilder.this.currentVertex - 4 + idx) * 32 + offset * 4;
        }

        @Override
        public float getX(int idx) {
            return SinkingVertexBuilder.this.buffer.getFloat(this.getBaseIndex(idx, 1));
        }

        @Override
        public float getY(int idx) {
            return SinkingVertexBuilder.this.buffer.getFloat(this.getBaseIndex(idx, 2));
        }

        @Override
        public float getZ(int idx) {
            return SinkingVertexBuilder.this.buffer.getFloat(this.getBaseIndex(idx, 3));
        }

        @Override
        public int getColor(int idx) {
            return SinkingVertexBuilder.this.buffer.getInt(this.getBaseIndex(idx, 4));
        }

        @Override
        public float getTexU(int idx) {
            return SinkingVertexBuilder.this.buffer.getFloat(this.getBaseIndex(idx, 5));
        }

        @Override
        public float getTexV(int idx) {
            return SinkingVertexBuilder.this.buffer.getFloat(this.getBaseIndex(idx, 6));
        }

        @Override
        public int getLight(int idx) {
            return SinkingVertexBuilder.this.buffer.getInt(this.getBaseIndex(idx, 7));
        }

        @Override
        public int getFlags() {
            return 0;
        }

        @Override
        public int getColorIndex() {
            return 0;
        }

        @Override
        public TextureAtlasSprite getSprite() {
            return null;
        }

        @Override
        public Direction getLightFace() {
            return null;
        }

        @Override
        public int getForgeNormal(int idx) {
            return 0;
        }
    };

    private static ByteBuffer reallocDirect(ByteBuffer old, int capacity) {
        ByteBuffer newBuf = ByteBuffer.allocateDirect(capacity).order(ByteOrder.nativeOrder());
        int oldPos = old.position();
        old.rewind();
        newBuf.put(old);
        newBuf.position(Math.min(capacity, oldPos));
        old.position(oldPos);
        return newBuf;
    }

    @Nonnull
    @Override
    public VertexConsumer vertex(double x, double y, double z) {
        this.x = (float) x;
        this.y = (float) y;
        this.z = (float) z;
        return this;
    }

    @Nonnull
    @Override
    public VertexConsumer color(int r, int g, int b, int a) {
        this.color = (a & 0xFF) << 24 | (b & 0xFF) << 16 | (g & 0xFF) << 8 | r & 0xFF;
        return this;
    }

    @Override
    public void defaultColor(int r, int g, int b, int a) {
        this.fixedColor = (a & 0xFF) << 24 | (b & 0xFF) << 16 | (g & 0xFF) << 8 | r & 0xFF;
        this.hasFixedColor = true;
    }

    @Override
    public void unsetDefaultColor() {
        this.hasFixedColor = false;
    }

    @Nonnull
    @Override
    public VertexConsumer uv(float u, float v) {
        this.u = u;
        this.v = v;
        return this;
    }

    @Nonnull
    @Override
    public VertexConsumer overlayCoords(int u, int v) {
        return this;
    }

    @Nonnull
    @Override
    public VertexConsumer uv2(int u, int v) {
        this.light = v << 16 | u;
        return this;
    }

    @Nonnull
    @Override
    public VertexConsumer normal(float x, float y, float z) {
        return this;
    }

    @Override
    public void endVertex() {
        if (this.buffer.capacity() - this.buffer.position() < 32) {
            int newCapacity = this.buffer.capacity() * 2;
            if (newCapacity == 0) {
                newCapacity = 16384;
            }
            this.buffer = reallocDirect(this.buffer, newCapacity);
        }
        ByteBuffer buffer = this.buffer;
        buffer.putInt(-1);
        buffer.putFloat(this.x);
        buffer.putFloat(this.y);
        buffer.putFloat(this.z);
        buffer.putFloat(this.u);
        buffer.putFloat(this.v);
        buffer.putInt(this.hasFixedColor ? this.fixedColor : this.color);
        buffer.putInt(this.light);
        this.resetCurrentVertex();
        this.currentVertex++;
        if (this.currentVertex % 4 == 0) {
            this.recalculateNormals();
        }
    }

    private void recalculateNormals() {
        ModelQuadFacing normal = ModelQuadUtil.findNormalFace(ModelQuadUtil.calculateNormal(this.previousQuad));
        this.buffer.putInt((this.currentVertex - 4) * 32, normal.ordinal());
    }

    public void reset() {
        if (this.currentVertex != 0) {
            this.buffer.rewind();
            this.currentVertex = 0;
            Arrays.fill(this.sideCount, 0);
        }
        this.resetCurrentVertex();
    }

    public boolean isEmpty() {
        return this.currentVertex == 0;
    }

    public boolean flush(@Nonnull ChunkModelBuilder buffers, Material material, Vector3fc origin) {
        return this.flush(buffers, material, origin.x(), origin.y(), origin.z());
    }

    public boolean flush(@Nonnull ChunkModelBuilder buffers, Material material, float oX, float oY, float oZ) {
        if (this.currentVertex == 0) {
            return false;
        } else {
            int numQuads = this.currentVertex >> 2;
            for (int quadIdx = 0; quadIdx < numQuads; quadIdx++) {
                int normal = this.buffer.getInt(quadIdx << 2 << 5);
                this.sideCount[normal]++;
            }
            int byteSize = this.currentVertex << 5;
            byte sideMask = 0;
            this.buffer.rewind();
            while (this.buffer.position() < byteSize) {
                int normal = this.buffer.getInt();
                ModelQuadFacing facing = ModelQuadFacing.VALUES[normal];
                int facingIdx = facing.ordinal();
                ChunkMeshBufferBuilder sink = buffers.getVertexBuffer(facing);
                ChunkVertexEncoder.Vertex[] sodiumQuad = this.sodiumVertexArray;
                float midU = 0.0F;
                float midV = 0.0F;
                for (int i = 0; i < 4; i++) {
                    if (i != 0) {
                        this.buffer.getInt();
                    }
                    ChunkVertexEncoder.Vertex sodiumVertex = sodiumQuad[i];
                    sodiumVertex.x = oX + this.buffer.getFloat();
                    sodiumVertex.y = oY + this.buffer.getFloat();
                    sodiumVertex.z = oZ + this.buffer.getFloat();
                    sodiumVertex.u = this.buffer.getFloat();
                    sodiumVertex.v = this.buffer.getFloat();
                    midU += sodiumVertex.u;
                    midV += sodiumVertex.v;
                    sodiumVertex.color = this.buffer.getInt();
                    sodiumVertex.light = this.buffer.getInt();
                }
                TextureAtlasSprite sprite = SpriteFinderCache.forBlockAtlas().findNearestSprite(midU / 4.0F, midV / 4.0F);
                if (sprite != null) {
                    buffers.addSprite(sprite);
                }
                sink.push(sodiumQuad, material);
                sideMask = (byte) (sideMask | 1 << facingIdx);
            }
            return true;
        }
    }

    private void resetCurrentVertex() {
        this.x = this.y = this.z = 0.0F;
        this.u = this.v = 0.0F;
        this.color = -1;
        this.light = 0;
    }
}