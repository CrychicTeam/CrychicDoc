package icyllis.modernui.mc.text;

import com.mojang.blaze3d.vertex.VertexConsumer;
import javax.annotation.Nonnull;
import org.joml.Matrix4f;

public class TextRenderEffect {

    private static final float UNDERLINE_OFFSET = 0.6666667F;

    private static final float STRIKETHROUGH_OFFSET = -3.5F;

    private static final float UNDERLINE_THICKNESS = 0.75F;

    private static final float STRIKETHROUGH_THICKNESS = 0.75F;

    public static final float EFFECT_DEPTH = 0.01F;

    private TextRenderEffect() {
    }

    public static void drawUnderline(@Nonnull VertexConsumer builder, float start, float end, float baseline, int r, int g, int b, int a) {
        baseline += 0.6666667F;
        builder.vertex((double) start, (double) (baseline + 0.75F), 0.01F).color(r, g, b, a).endVertex();
        builder.vertex((double) end, (double) (baseline + 0.75F), 0.01F).color(r, g, b, a).endVertex();
        builder.vertex((double) end, (double) baseline, 0.01F).color(r, g, b, a).endVertex();
        builder.vertex((double) start, (double) baseline, 0.01F).color(r, g, b, a).endVertex();
    }

    public static void drawUnderline(@Nonnull Matrix4f matrix, @Nonnull VertexConsumer builder, float start, float end, float baseline, int r, int g, int b, int a, int light) {
        baseline += 0.6666667F;
        builder.vertex(matrix, start, baseline + 0.75F, 0.01F).color(r, g, b, a).uv(0.0F, 1.0F).uv2(light).endVertex();
        builder.vertex(matrix, end, baseline + 0.75F, 0.01F).color(r, g, b, a).uv(1.0F, 1.0F).uv2(light).endVertex();
        builder.vertex(matrix, end, baseline, 0.01F).color(r, g, b, a).uv(1.0F, 0.0F).uv2(light).endVertex();
        builder.vertex(matrix, start, baseline, 0.01F).color(r, g, b, a).uv(0.0F, 0.0F).uv2(light).endVertex();
    }

    public static void drawStrikethrough(@Nonnull VertexConsumer builder, float start, float end, float baseline, int r, int g, int b, int a) {
        baseline += -3.5F;
        builder.vertex((double) start, (double) (baseline + 0.75F), 0.01F).color(r, g, b, a).endVertex();
        builder.vertex((double) end, (double) (baseline + 0.75F), 0.01F).color(r, g, b, a).endVertex();
        builder.vertex((double) end, (double) baseline, 0.01F).color(r, g, b, a).endVertex();
        builder.vertex((double) start, (double) baseline, 0.01F).color(r, g, b, a).endVertex();
    }

    public static void drawStrikethrough(@Nonnull Matrix4f matrix, @Nonnull VertexConsumer builder, float start, float end, float baseline, int r, int g, int b, int a, int light) {
        baseline += -3.5F;
        builder.vertex(matrix, start, baseline + 0.75F, 0.01F).color(r, g, b, a).uv(0.0F, 1.0F).uv2(light).endVertex();
        builder.vertex(matrix, end, baseline + 0.75F, 0.01F).color(r, g, b, a).uv(1.0F, 1.0F).uv2(light).endVertex();
        builder.vertex(matrix, end, baseline, 0.01F).color(r, g, b, a).uv(1.0F, 0.0F).uv2(light).endVertex();
        builder.vertex(matrix, start, baseline, 0.01F).color(r, g, b, a).uv(0.0F, 0.0F).uv2(light).endVertex();
    }
}