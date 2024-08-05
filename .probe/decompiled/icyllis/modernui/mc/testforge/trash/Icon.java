package icyllis.modernui.mc.testforge.trash;

import icyllis.arc3d.opengl.GLTexture;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;

@Deprecated
public class Icon {

    private final ResourceLocation location;

    @Nullable
    private GLTexture texture;

    private final float u1;

    private final float v1;

    private final float u2;

    private final float v2;

    private final boolean aa;

    public Icon(ResourceLocation location, float u1, float v1, float u2, float v2, boolean aa) {
        this.location = location;
        this.u1 = u1;
        this.v1 = v1;
        this.u2 = u2;
        this.v2 = v2;
        this.aa = aa;
    }

    public void bindTexture() {
    }

    public float getLeft() {
        return this.u1;
    }

    public float getTop() {
        return this.v1;
    }

    public float getRight() {
        return this.u2;
    }

    public float getBottom() {
        return this.v2;
    }
}