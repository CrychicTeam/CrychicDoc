package icyllis.modernui.mc.testforge.shader.program;

import icyllis.modernui.annotation.RenderThread;
import icyllis.modernui.mc.testforge.shader.GLProgram;
import javax.annotation.Nonnull;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.opengl.GL43C;

@Deprecated
public class RoundRectProgram extends GLProgram {

    private static RoundRectProgram.Fill sFill;

    private static RoundRectProgram.FillTex sFillTex;

    private static RoundRectProgram.Stroke sStroke;

    private RoundRectProgram(@Nonnull ResourceLocation vert, @Nonnull ResourceLocation frag) {
    }

    @RenderThread
    public static void createPrograms() {
        if (sFill == null) {
            sFill = new RoundRectProgram.Fill();
            sFillTex = new RoundRectProgram.FillTex();
            sStroke = new RoundRectProgram.Stroke();
        }
    }

    public static RoundRectProgram.Fill fill() {
        return sFill;
    }

    public static RoundRectProgram.FillTex fillTex() {
        return sFillTex;
    }

    public static RoundRectProgram.Stroke stroke() {
        return sStroke;
    }

    public void setInnerRect(float left, float top, float right, float bottom) {
        GL43C.glUniform4f(1, left, top, right, bottom);
    }

    public static class Fill extends RoundRectProgram {

        private Fill() {
            super(RectProgram.VERT, new ResourceLocation("modernui", "shaders/round_rect_fill.frag"));
        }

        private Fill(@Nonnull ResourceLocation vert, @Nonnull ResourceLocation frag) {
            super(vert, frag);
        }

        public void setRadius(float radius, float feather) {
            GL43C.glUniform2f(0, radius, feather);
        }
    }

    public static class FillTex extends RoundRectProgram.Fill {

        private FillTex() {
            super(RectProgram.VERT_TEX, new ResourceLocation("modernui", "shaders/round_rect_tex.frag"));
        }
    }

    public static class Stroke extends RoundRectProgram {

        private Stroke() {
            super(RectProgram.VERT, new ResourceLocation("modernui", "shaders/round_rect_stroke.frag"));
        }

        public void setRadius(float radius, float feather, float thickness) {
            GL43C.glUniform3f(0, radius, feather, thickness);
        }
    }
}