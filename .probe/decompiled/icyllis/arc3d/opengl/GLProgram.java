package icyllis.arc3d.opengl;

import icyllis.arc3d.engine.ManagedResource;
import javax.annotation.Nonnull;
import org.lwjgl.opengl.GL20C;

public final class GLProgram extends ManagedResource {

    private int mProgram;

    public GLProgram(@Nonnull GLDevice device, int program) {
        super(device);
        assert program != 0;
        this.mProgram = program;
    }

    @Override
    protected void deallocate() {
        if (this.mProgram != 0) {
            GL20C.glDeleteProgram(this.mProgram);
        }
        this.discard();
    }

    public void discard() {
        this.mProgram = 0;
    }

    public int getProgram() {
        return this.mProgram;
    }
}