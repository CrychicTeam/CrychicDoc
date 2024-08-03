package me.jellysquid.mods.sodium.client.gl.shader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.io.IOUtils;

public class ShaderLoader {

    public static GlShader loadShader(ShaderType type, ResourceLocation name, ShaderConstants constants) {
        return new GlShader(type, name, ShaderParser.parseShader(getShaderSource(name), constants));
    }

    public static String getShaderSource(ResourceLocation name) {
        String path = String.format("/assets/%s/shaders/%s", name.getNamespace(), name.getPath());
        try {
            InputStream in = ShaderLoader.class.getClassLoader().getResourceAsStream(path);
            String var3;
            try {
                if (in == null) {
                    throw new RuntimeException("Shader not found: " + path);
                }
                var3 = IOUtils.toString(in, StandardCharsets.UTF_8);
            } catch (Throwable var6) {
                if (in != null) {
                    try {
                        in.close();
                    } catch (Throwable var5) {
                        var6.addSuppressed(var5);
                    }
                }
                throw var6;
            }
            if (in != null) {
                in.close();
            }
            return var3;
        } catch (IOException var7) {
            throw new RuntimeException("Failed to read shader source for " + path, var7);
        }
    }
}