package com.mojang.blaze3d.shaders;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.preprocessor.GlslPreprocessor;
import com.mojang.blaze3d.systems.RenderSystem;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

public class Program {

    private static final int MAX_LOG_LENGTH = 32768;

    private final Program.Type type;

    private final String name;

    private int id;

    protected Program(Program.Type programType0, int int1, String string2) {
        this.type = programType0;
        this.id = int1;
        this.name = string2;
    }

    public void attachToShader(Shader shader0) {
        RenderSystem.assertOnRenderThread();
        GlStateManager.glAttachShader(shader0.getId(), this.getId());
    }

    public void close() {
        if (this.id != -1) {
            RenderSystem.assertOnRenderThread();
            GlStateManager.glDeleteShader(this.id);
            this.id = -1;
            this.type.getPrograms().remove(this.name);
        }
    }

    public String getName() {
        return this.name;
    }

    public static Program compileShader(Program.Type programType0, String string1, InputStream inputStream2, String string3, GlslPreprocessor glslPreprocessor4) throws IOException {
        RenderSystem.assertOnRenderThread();
        int $$5 = compileShaderInternal(programType0, string1, inputStream2, string3, glslPreprocessor4);
        Program $$6 = new Program(programType0, $$5, string1);
        programType0.getPrograms().put(string1, $$6);
        return $$6;
    }

    protected static int compileShaderInternal(Program.Type programType0, String string1, InputStream inputStream2, String string3, GlslPreprocessor glslPreprocessor4) throws IOException {
        String $$5 = IOUtils.toString(inputStream2, StandardCharsets.UTF_8);
        if ($$5 == null) {
            throw new IOException("Could not load program " + programType0.getName());
        } else {
            int $$6 = GlStateManager.glCreateShader(programType0.getGlType());
            GlStateManager.glShaderSource($$6, glslPreprocessor4.process($$5));
            GlStateManager.glCompileShader($$6);
            if (GlStateManager.glGetShaderi($$6, 35713) == 0) {
                String $$7 = StringUtils.trim(GlStateManager.glGetShaderInfoLog($$6, 32768));
                throw new IOException("Couldn't compile " + programType0.getName() + " program (" + string3 + ", " + string1 + ") : " + $$7);
            } else {
                return $$6;
            }
        }
    }

    protected int getId() {
        return this.id;
    }

    public static enum Type {

        VERTEX("vertex", ".vsh", 35633), FRAGMENT("fragment", ".fsh", 35632);

        private final String name;

        private final String extension;

        private final int glType;

        private final Map<String, Program> programs = Maps.newHashMap();

        private Type(String p_85563_, String p_85564_, int p_85565_) {
            this.name = p_85563_;
            this.extension = p_85564_;
            this.glType = p_85565_;
        }

        public String getName() {
            return this.name;
        }

        public String getExtension() {
            return this.extension;
        }

        int getGlType() {
            return this.glType;
        }

        public Map<String, Program> getPrograms() {
            return this.programs;
        }
    }
}