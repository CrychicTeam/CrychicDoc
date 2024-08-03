package team.lodestar.lodestone.systems.postprocess;

import net.minecraft.client.renderer.EffectInstance;
import org.lwjgl.opengl.GL31;

public class ShaderDataBuffer {

    private int tbo = 0;

    private int tex = 0;

    public void generate(long size) {
        this.destroy();
        this.tbo = GL31.glGenBuffers();
        GL31.glBindBuffer(35882, this.tbo);
        GL31.glBufferData(35882, size * 4L, 35044);
        this.tex = GL31.glGenTextures();
        GL31.glBindTexture(35882, this.tex);
        GL31.glTexBuffer(35882, 33326, this.tbo);
        GL31.glBindBuffer(35882, 0);
        GL31.glBindTexture(35882, 0);
    }

    public void destroy() {
        if (this.tbo != 0) {
            GL31.glDeleteBuffers(this.tbo);
        }
        if (this.tex != 0) {
            GL31.glDeleteTextures(this.tex);
        }
        this.tbo = 0;
        this.tex = 0;
    }

    public void upload(float[] data) {
        GL31.glBindBuffer(35882, this.tbo);
        GL31.glBufferSubData(35882, 0L, data);
        GL31.glBindBuffer(35882, 0);
    }

    public void apply(EffectInstance effect, String uniform) {
        GL31.glBindBuffer(35882, this.tbo);
        int unit = effect.samplerMap.size();
        GL31.glActiveTexture(33984 + unit);
        GL31.glBindTexture(35882, this.tex);
        effect.safeGetUniform(uniform).set(unit);
        GL31.glActiveTexture(33984);
    }
}