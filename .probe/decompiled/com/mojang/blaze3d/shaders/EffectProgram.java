package com.mojang.blaze3d.shaders;

import com.mojang.blaze3d.preprocessor.GlslPreprocessor;
import com.mojang.blaze3d.systems.RenderSystem;
import java.io.IOException;
import java.io.InputStream;

public class EffectProgram extends Program {

    private static final GlslPreprocessor PREPROCESSOR = new GlslPreprocessor() {

        @Override
        public String applyImport(boolean p_166595_, String p_166596_) {
            return "#error Import statement not supported";
        }
    };

    private int references;

    private EffectProgram(Program.Type programType0, int int1, String string2) {
        super(programType0, int1, string2);
    }

    public void attachToEffect(Effect effect0) {
        RenderSystem.assertOnRenderThread();
        this.references++;
        this.m_166610_(effect0);
    }

    @Override
    public void close() {
        RenderSystem.assertOnRenderThread();
        this.references--;
        if (this.references <= 0) {
            super.close();
        }
    }

    public static EffectProgram compileShader(Program.Type programType0, String string1, InputStream inputStream2, String string3) throws IOException {
        RenderSystem.assertOnRenderThread();
        int $$4 = m_166612_(programType0, string1, inputStream2, string3, PREPROCESSOR);
        EffectProgram $$5 = new EffectProgram(programType0, $$4, string1);
        programType0.getPrograms().put(string1, $$5);
        return $$5;
    }
}