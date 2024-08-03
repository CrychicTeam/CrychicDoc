package icyllis.modernui.mc.testforge.shader.program;

import icyllis.modernui.mc.testforge.shader.GLProgram;
import javax.annotation.Nonnull;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.opengl.GL43C;

@Deprecated
public class CircleProgram extends GLProgram {

    private static CircleProgram.Fill sFill;

    private static CircleProgram.Stroke sStroke;

    private CircleProgram(@Nonnull ResourceLocation vert, @Nonnull ResourceLocation frag) {
    }

    public static void createPrograms() {
        if (sFill == null) {
            sFill = new CircleProgram.Fill();
            sStroke = new CircleProgram.Stroke();
        }
    }

    public static CircleProgram.Fill fill() {
        return sFill;
    }

    public static CircleProgram.Stroke stroke() {
        return sStroke;
    }

    public void setCenter(float x, float y) {
        GL43C.glUniform2f(1, x, y);
    }

    public static class Fill extends CircleProgram {

        private Fill() {
            super(RectProgram.VERT, new ResourceLocation("modernui", "shaders/circle_fill.frag"));
        }

        public void setRadius(float radius, float feather) {
            GL43C.glUniform2f(0, radius, feather);
        }
    }

    public static class Stroke extends CircleProgram {

        private Stroke() {
            super(RectProgram.VERT, new ResourceLocation("modernui", "shaders/circle_stroke.frag"));
        }

        public void setRadius(float inner, float radius, float feather) {
            GL43C.glUniform3f(0, inner, radius, feather);
        }
    }
}