package icyllis.arc3d.core;

import java.util.Locale;

public class ShaderUtils {

    public static String buildShaderErrorMessage(String shader, String errors) {
        StringBuilder b = new StringBuilder("Shader compilation error\n------------------------\n");
        String[] lines = shader.split("\n");
        for (int i = 0; i < lines.length; i++) {
            b.append(String.format(Locale.ROOT, "%4s\t%s\n", i + 1, lines[i]));
        }
        b.append("Errors:\n");
        b.append(errors);
        return b.toString();
    }
}