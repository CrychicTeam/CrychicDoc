package net.minecraftforge.client.model;

import com.google.common.base.Preconditions;
import com.mojang.math.Transformation;
import java.util.Arrays;
import net.minecraft.Util;
import net.minecraft.client.renderer.LightTexture;
import org.joml.Vector3f;
import org.joml.Vector4f;

public final class QuadTransformers {

    private static final IQuadTransformer EMPTY = quad -> {
    };

    private static final IQuadTransformer[] EMISSIVE_TRANSFORMERS = Util.make(new IQuadTransformer[16], array -> Arrays.setAll(array, i -> applyingLightmap(LightTexture.pack(i, i))));

    public static IQuadTransformer empty() {
        return EMPTY;
    }

    public static IQuadTransformer applying(Transformation transform) {
        return transform.isIdentity() ? empty() : quad -> {
            int[] vertices = quad.getVertices();
            for (int i = 0; i < 4; i++) {
                int offset = i * IQuadTransformer.STRIDE + IQuadTransformer.POSITION;
                float x = Float.intBitsToFloat(vertices[offset]);
                float y = Float.intBitsToFloat(vertices[offset + 1]);
                float z = Float.intBitsToFloat(vertices[offset + 2]);
                Vector4f pos = new Vector4f(x, y, z, 1.0F);
                transform.transformPosition(pos);
                pos.div(pos.w);
                vertices[offset] = Float.floatToRawIntBits(pos.x());
                vertices[offset + 1] = Float.floatToRawIntBits(pos.y());
                vertices[offset + 2] = Float.floatToRawIntBits(pos.z());
            }
            for (int i = 0; i < 4; i++) {
                int offset = i * IQuadTransformer.STRIDE + IQuadTransformer.NORMAL;
                int normalIn = vertices[offset];
                if ((normalIn & 16777215) != 0) {
                    float x = (float) ((byte) (normalIn & 0xFF)) / 127.0F;
                    float y = (float) ((byte) (normalIn >> 8 & 0xFF)) / 127.0F;
                    float z = (float) ((byte) (normalIn >> 16 & 0xFF)) / 127.0F;
                    Vector3f pos = new Vector3f(x, y, z);
                    transform.transformNormal(pos);
                    vertices[offset] = (byte) ((int) (pos.x() * 127.0F)) & 255 | ((byte) ((int) (pos.y() * 127.0F)) & 255) << 8 | ((byte) ((int) (pos.z() * 127.0F)) & 255) << 16 | normalIn & 0xFF000000;
                }
            }
        };
    }

    public static IQuadTransformer applyingLightmap(int packedLight) {
        return quad -> {
            int[] vertices = quad.getVertices();
            for (int i = 0; i < 4; i++) {
                vertices[i * IQuadTransformer.STRIDE + IQuadTransformer.UV2] = packedLight;
            }
        };
    }

    public static IQuadTransformer applyingLightmap(int blockLight, int skyLight) {
        return applyingLightmap(LightTexture.pack(blockLight, skyLight));
    }

    public static IQuadTransformer settingEmissivity(int emissivity) {
        Preconditions.checkArgument(emissivity >= 0 && emissivity < 16, "Emissivity must be between 0 and 15.");
        return EMISSIVE_TRANSFORMERS[emissivity];
    }

    public static IQuadTransformer settingMaxEmissivity() {
        return EMISSIVE_TRANSFORMERS[15];
    }

    public static IQuadTransformer applyingColor(int color) {
        int fixedColor = toABGR(color);
        return quad -> {
            int[] vertices = quad.getVertices();
            for (int i = 0; i < 4; i++) {
                vertices[i * IQuadTransformer.STRIDE + IQuadTransformer.COLOR] = fixedColor;
            }
        };
    }

    public static IQuadTransformer applyingColor(int red, int green, int blue) {
        return applyingColor(255, red, green, blue);
    }

    public static IQuadTransformer applyingColor(int alpha, int red, int green, int blue) {
        return applyingColor(alpha << 24 | red << 16 | green << 8 | blue);
    }

    public static int toABGR(int argb) {
        return argb & -16711936 | argb >> 16 & 0xFF | argb << 16 & 0xFF0000;
    }

    private QuadTransformers() {
    }
}