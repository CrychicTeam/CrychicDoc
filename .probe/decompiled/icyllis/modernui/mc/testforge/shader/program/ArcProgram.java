package icyllis.modernui.mc.testforge.shader.program;

import icyllis.modernui.mc.testforge.shader.GLProgram;
import javax.annotation.Nonnull;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.opengl.GL43C;

@Deprecated
public class ArcProgram extends GLProgram {

    private static ArcProgram.Fill sFill;

    private static ArcProgram.Stroke sStroke;

    private ArcProgram(@Nonnull ResourceLocation vert, @Nonnull ResourceLocation frag) {
    }

    public static void createPrograms() {
        if (sFill == null) {
            sFill = new ArcProgram.Fill();
            sStroke = new ArcProgram.Stroke();
        }
    }

    public static ArcProgram.Fill fill() {
        return sFill;
    }

    public static ArcProgram.Stroke stroke() {
        return sStroke;
    }

    public void setCenter(float x, float y) {
        GL43C.glUniform2f(1, x, y);
    }

    public static class Fill extends ArcProgram {

        private Fill() {
            super(RectProgram.VERT, new ResourceLocation("modernui", "shaders/arc_fill.frag"));
        }

        public void setRadius(float radius, float feather) {
            GL43C.glUniform2f(0, radius, feather);
        }

        public void setAngle(float middle, float flare) {
            GL43C.glUniform2f(2, middle, flare);
        }
    }

    public static class Stroke extends ArcProgram {

        private Stroke() {
            super(RectProgram.VERT, new ResourceLocation("modernui", "shaders/arc_stroke.frag"));
        }

        public void setRadius(float radius, float feather, float thickness) {
            GL43C.glUniform3f(0, radius, feather, thickness);
        }

        public void setAngle(float middle, float flare) {
            GL43C.glUniform2f(2, middle, flare);
        }
    }
}