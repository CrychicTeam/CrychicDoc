package net.minecraftforge.client.model.lighting;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.Objects;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.IQuadTransformer;
import org.joml.Vector3f;

public abstract class QuadLighter {

    private static final float[] WHITE = new float[] { 1.0F, 1.0F, 1.0F };

    private final BlockColors colors;

    private int currentHash = 0;

    private BlockAndTintGetter level;

    private BlockPos pos;

    private BlockState state;

    private int cachedTintIndex = -1;

    private final float[] cachedTintColor = new float[3];

    private final float[] brightness = new float[4];

    private final int[] lightmap = new int[4];

    private final float[][] positions = new float[4][3];

    private final byte[][] normals = new byte[4][3];

    private final int[] packedLightmaps = new int[4];

    protected QuadLighter(BlockColors colors) {
        this.colors = colors;
    }

    protected abstract void computeLightingAt(BlockAndTintGetter var1, BlockPos var2, BlockState var3);

    protected abstract float calculateBrightness(float[] var1);

    protected abstract int calculateLightmap(float[] var1, byte[] var2);

    public final void setup(BlockAndTintGetter level, BlockPos pos, BlockState state) {
        int hash = Objects.hash(new Object[] { level, pos, state });
        if (this.level == null || this.currentHash != hash) {
            this.currentHash = hash;
            this.level = level;
            this.pos = pos;
            this.state = state;
            this.cachedTintIndex = -1;
            this.computeLightingAt(level, pos, state);
        }
    }

    public final void reset() {
        this.level = null;
    }

    public final void process(VertexConsumer consumer, PoseStack.Pose pose, BakedQuad quad, int overlay) {
        int[] vertices = quad.getVertices();
        for (int i = 0; i < 4; i++) {
            int offset = i * IQuadTransformer.STRIDE;
            this.positions[i][0] = Float.intBitsToFloat(vertices[offset + IQuadTransformer.POSITION]);
            this.positions[i][1] = Float.intBitsToFloat(vertices[offset + IQuadTransformer.POSITION + 1]);
            this.positions[i][2] = Float.intBitsToFloat(vertices[offset + IQuadTransformer.POSITION + 2]);
            int packedNormal = vertices[offset + IQuadTransformer.NORMAL];
            this.normals[i][0] = (byte) (packedNormal & 0xFF);
            this.normals[i][1] = (byte) (packedNormal >> 8 & 0xFF);
            this.normals[i][2] = (byte) (packedNormal >> 16 & 0xFF);
            this.packedLightmaps[i] = vertices[offset + IQuadTransformer.UV2];
        }
        if (this.normals[0][0] == 0 && this.normals[0][1] == 0 && this.normals[0][2] == 0) {
            Vector3f a = new Vector3f(this.positions[0]);
            Vector3f ab = new Vector3f(this.positions[1]);
            Vector3f ac = new Vector3f(this.positions[2]);
            ac.sub(a);
            ab.sub(a);
            ab.cross(ac);
            ab.normalize();
            for (int v = 0; v < 4; v++) {
                this.normals[v][0] = (byte) ((int) (ab.x() * 127.0F));
                this.normals[v][1] = (byte) ((int) (ab.y() * 127.0F));
                this.normals[v][2] = (byte) ((int) (ab.z() * 127.0F));
            }
        }
        for (int i = 0; i < 4; i++) {
            float[] position = this.positions[i];
            byte[] normal = this.normals[i];
            int packedLightmap = this.packedLightmaps[i];
            float[] adjustedPosition = new float[] { position[0] - 0.5F + (float) normal[0] / 127.0F * 0.5F, position[1] - 0.5F + (float) normal[1] / 127.0F * 0.5F, position[2] - 0.5F + (float) normal[2] / 127.0F * 0.5F };
            float shade = this.level.getShade((float) this.normals[i][0] / 127.0F, (float) this.normals[i][1] / 127.0F, (float) this.normals[i][2] / 127.0F, quad.isShade());
            this.brightness[i] = this.calculateBrightness(adjustedPosition) * shade;
            int newLightmap = this.calculateLightmap(adjustedPosition, normal);
            this.lightmap[i] = Math.max(packedLightmap & 65535, newLightmap & 65535) | Math.max(packedLightmap >> 16 & 65535, newLightmap >> 16 & 65535) << 16;
        }
        float[] color = quad.isTinted() ? this.getColorFast(quad.getTintIndex()) : WHITE;
        consumer.putBulkData(pose, quad, this.brightness, color[0], color[1], color[2], this.lightmap, overlay, true);
    }

    private float[] getColorFast(int tintIndex) {
        if (tintIndex != this.cachedTintIndex) {
            int packedColor = this.colors.getColor(this.state, this.level, this.pos, tintIndex);
            this.cachedTintIndex = tintIndex;
            this.cachedTintColor[0] = (float) (packedColor >> 16 & 0xFF) / 255.0F;
            this.cachedTintColor[1] = (float) (packedColor >> 8 & 0xFF) / 255.0F;
            this.cachedTintColor[2] = (float) (packedColor & 0xFF) / 255.0F;
        }
        return this.cachedTintColor;
    }

    public static float calculateShade(float normalX, float normalY, float normalZ, boolean constantAmbientLight) {
        float yFactor = constantAmbientLight ? 0.9F : (3.0F + normalY) / 4.0F;
        return Math.min(normalX * normalX * 0.6F + normalY * normalY * yFactor + normalZ * normalZ * 0.8F, 1.0F);
    }

    @Deprecated(since = "1.20.1")
    protected static int getLightColor(BlockAndTintGetter level, BlockPos pos, BlockState state) {
        if (state.m_60788_(level, pos)) {
            return 15728880;
        } else {
            int skyLight = level.getBrightness(LightLayer.SKY, pos);
            int blockLight = Math.max(level.getBrightness(LightLayer.BLOCK, pos), level.m_8055_(pos).getLightEmission(level, pos));
            return skyLight << 20 | blockLight << 4;
        }
    }
}