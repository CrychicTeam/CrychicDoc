package icyllis.modernui.mc.testforge.shader.program;

import icyllis.modernui.mc.testforge.shader.GLProgram;
import javax.annotation.Nonnull;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.opengl.GL43C;

@Deprecated
public class RectProgram extends GLProgram {

    public static final ResourceLocation VERT = new ResourceLocation("modernui", "shaders/pos_color.vert");

    public static final ResourceLocation VERT_TEX = new ResourceLocation("modernui", "shaders/pos_color_tex.vert");

    private static RectProgram sFill;

    private static RectProgram.FillTex sFillTex;

    private static RectProgram.Feathered sFeathered;

    private RectProgram(@Nonnull ResourceLocation vert, @Nonnull ResourceLocation frag) {
    }

    public static void createPrograms() {
        if (sFill == null) {
            sFill = new RectProgram(VERT, new ResourceLocation("modernui", "shaders/color_fill.frag"));
            sFillTex = new RectProgram.FillTex();
            sFeathered = new RectProgram.Feathered();
        }
    }

    public static RectProgram fill() {
        return sFill;
    }

    public static RectProgram fillTex() {
        return sFillTex;
    }

    public static RectProgram.Feathered feathered() {
        return sFeathered;
    }

    public static class Feathered extends RectProgram {

        private Feathered() {
            super(VERT, new ResourceLocation("modernui", "shaders/rect_fill_v.frag"));
        }

        public void setThickness(float thickness) {
            GL43C.glUniform1f(0, thickness);
        }

        public void setInnerRect(float left, float top, float right, float bottom) {
            GL43C.glUniform4f(1, left, top, right, bottom);
        }
    }

    private static class FillTex extends RectProgram {

        private FillTex() {
            super(VERT_TEX, new ResourceLocation("modernui", "shaders/color_tex.frag"));
        }
    }
}