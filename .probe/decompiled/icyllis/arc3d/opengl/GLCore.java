package icyllis.arc3d.opengl;

import icyllis.arc3d.engine.PipelineStateCache;
import java.io.PrintWriter;
import java.lang.ref.Reference;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nonnull;
import org.lwjgl.PointerBuffer;
import org.lwjgl.opengl.GL45C;
import org.lwjgl.opengl.GL46C;
import org.lwjgl.system.APIUtil;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.system.NativeType;

public final class GLCore extends GL45C {

    public static final int INVALID_ID = -1;

    public static final int DEFAULT_FRAMEBUFFER = 0;

    public static final int DEFAULT_VERTEX_ARRAY = 0;

    public static final int DEFAULT_TEXTURE = 0;

    public static final int GL_VENDOR_OTHER = 0;

    public static final int GL_VENDOR_NVIDIA = 1;

    public static final int GL_VENDOR_ATI = 2;

    public static final int GL_VENDOR_INTEL = 3;

    public static final int GL_VENDOR_QUALCOMM = 4;

    public static final int GL_DRIVER_OTHER = 0;

    public static final int GL_DRIVER_NVIDIA = 1;

    public static final int GL_DRIVER_AMD = 2;

    public static final int GL_DRIVER_INTEL = 3;

    public static final int GL_DRIVER_FREEDRENO = 4;

    public static final int GL_DRIVER_MESA = 5;

    public static final int LAST_COLOR_FORMAT_INDEX = 16;

    public static final int LAST_FORMAT_INDEX = 19;

    private GLCore() {
        throw new UnsupportedOperationException();
    }

    public static void glClearErrors() {
        while (glGetError() != 0) {
        }
    }

    public static int find_vendor(String vendorString) {
        Objects.requireNonNull(vendorString);
        if (vendorString.equals("NVIDIA Corporation")) {
            return 1;
        } else if (vendorString.equals("ATI Technologies Inc.")) {
            return 2;
        } else if (vendorString.startsWith("Intel ") || vendorString.equals("Intel")) {
            return 3;
        } else {
            return !vendorString.equals("Qualcomm") && !vendorString.equals("freedreno") ? 0 : 4;
        }
    }

    public static int find_driver(int vendor, String vendorString, String versionString) {
        Objects.requireNonNull(vendorString);
        if (vendorString.equals("freedreno")) {
            return 4;
        } else if (vendor == 1) {
            return 1;
        } else {
            Matcher matcher = Pattern.compile("\\d+\\.\\d+( \\(Core Profile\\))? Mesa \\d+\\.\\d+").matcher(versionString);
            if (matcher.find()) {
                return 5;
            } else if (vendor == 2) {
                return 2;
            } else {
                return vendor == 3 ? 3 : 0;
            }
        }
    }

    public static int glFormatToIndex(@NativeType("GLenum") int format) {
        return switch(format) {
            case 32849 ->
                6;
            case 32856 ->
                1;
            case 32857 ->
                8;
            case 32859 ->
                15;
            case 33321 ->
                2;
            case 33322 ->
                13;
            case 33323 ->
                7;
            case 33324 ->
                14;
            case 33325 ->
                5;
            case 33327 ->
                16;
            case 33776 ->
                11;
            case 33777 ->
                12;
            case 34842 ->
                4;
            case 35056 ->
                19;
            case 35907 ->
                9;
            case 36168 ->
                17;
            case 36169 ->
                18;
            case 36194 ->
                3;
            case 37492 ->
                10;
            default ->
                0;
        };
    }

    @NativeType("GLenum")
    public static int glIndexToFormat(int index) {
        return switch(index) {
            case 0 ->
                '\u0000';
            case 1 ->
                '聘';
            case 2 ->
                '舩';
            case 3 ->
                '赢';
            case 4 ->
                '蠚';
            case 5 ->
                '舭';
            case 6 ->
                '聑';
            case 7 ->
                '舫';
            case 8 ->
                '聙';
            case 9 ->
                '豃';
            case 10 ->
                '鉴';
            case 11 ->
                '菰';
            case 12 ->
                '菱';
            case 13 ->
                '航';
            case 14 ->
                '般';
            case 15 ->
                '聛';
            case 16 ->
                '舯';
            case 17 ->
                '赈';
            case 18 ->
                '赉';
            case 19 ->
                '裰';
            default ->
                {
                }
        };
    }

    public static int glFormatChannels(@NativeType("GLenum") int format) {
        return switch(format) {
            case 32849, 33776, 36194, 37492 ->
                7;
            case 32856, 32857, 32859, 33777, 34842, 35907 ->
                15;
            case 33321, 33322, 33325 ->
                1;
            case 33323, 33324, 33327 ->
                3;
            default ->
                0;
        };
    }

    public static boolean glFormatIsSupported(@NativeType("GLenum") int format) {
        return switch(format) {
            case 32849, 32856, 32857, 32859, 33321, 33322, 33323, 33324, 33325, 33327, 33776, 33777, 34842, 35056, 35907, 36168, 36169, 36194, 37492 ->
                true;
            default ->
                false;
        };
    }

    public static int glFormatCompressionType(@NativeType("GLenum") int format) {
        return switch(format) {
            case 33776 ->
                2;
            case 33777 ->
                3;
            case 37492 ->
                1;
            default ->
                0;
        };
    }

    public static int glFormatBytesPerBlock(@NativeType("GLenum") int format) {
        return switch(format) {
            case 32849, 32856, 32857, 33324, 33327, 35056, 35907 ->
                4;
            case 32859, 33776, 33777, 34842, 37492 ->
                8;
            case 33321, 36168 ->
                1;
            case 33322, 33323, 33325, 36169, 36194 ->
                2;
            default ->
                0;
        };
    }

    public static int glFormatStencilBits(@NativeType("GLenum") int format) {
        return switch(format) {
            case 35056, 36168 ->
                8;
            case 36169 ->
                16;
            default ->
                0;
        };
    }

    public static boolean glFormatIsPackedDepthStencil(@NativeType("GLenum") int format) {
        return format == 35056;
    }

    public static boolean glFormatIsSRGB(@NativeType("GLenum") int format) {
        return format == 35907;
    }

    public static boolean glFormatIsCompressed(@NativeType("GLenum") int format) {
        return switch(format) {
            case 33776, 33777, 37492 ->
                true;
            default ->
                false;
        };
    }

    public static String glFormatName(@NativeType("GLenum") int format) {
        return switch(format) {
            case 32849 ->
                "RGB8";
            case 32856 ->
                "RGBA8";
            case 32857 ->
                "RGB10_A2";
            case 32859 ->
                "RGBA16";
            case 33321 ->
                "R8";
            case 33322 ->
                "R16";
            case 33323 ->
                "RG8";
            case 33324 ->
                "RG16";
            case 33325 ->
                "R16F";
            case 33327 ->
                "RG16F";
            case 33776 ->
                "RGB8_BC1";
            case 33777 ->
                "RGBA8_BC1";
            case 34836 ->
                "RGBA32F";
            case 34842 ->
                "RGBA16F";
            case 35056 ->
                "DEPTH24_STENCIL8";
            case 35907 ->
                "SRGB8_ALPHA8";
            case 36168 ->
                "STENCIL_INDEX8";
            case 36169 ->
                "STENCIL_INDEX16";
            case 36194 ->
                "RGB565";
            case 37492 ->
                "ETC2";
            default ->
                APIUtil.apiUnknownToken(format);
        };
    }

    @NativeType("GLuint")
    public static int glCompileShader(@NativeType("GLenum") int shaderType, @NativeType("GLchar const *") ByteBuffer source, PipelineStateCache.Stats stats, PrintWriter pw) {
        int shader = glCreateShader(shaderType);
        if (shader == 0) {
            return 0;
        } else {
            try {
                MemoryStack stack = MemoryStack.stackPush();
                try {
                    PointerBuffer string = stack.pointers(source);
                    IntBuffer length = stack.ints(source.remaining());
                    glShaderSource(shader, string, length);
                } catch (Throwable var13) {
                    if (stack != null) {
                        try {
                            stack.close();
                        } catch (Throwable var12) {
                            var13.addSuppressed(var12);
                        }
                    }
                    throw var13;
                }
                if (stack != null) {
                    stack.close();
                }
            } finally {
                Reference.reachabilityFence(source);
            }
            glCompileShader(shader);
            stats.incShaderCompilations();
            if (glGetShaderi(shader, 35713) == 0) {
                String log = glGetShaderInfoLog(shader);
                glDeleteShader(shader);
                handleCompileError(pw, MemoryUtil.memUTF8(source), log);
                return 0;
            } else {
                return shader;
            }
        }
    }

    public static int glCompileAndAttachShader(int program, int shaderType, String source, PipelineStateCache.Stats stats, PrintWriter pw) {
        int shader = glCreateShader(shaderType);
        if (shader == 0) {
            return 0;
        } else {
            glShaderSource(shader, source);
            glCompileShader(shader);
            stats.incShaderCompilations();
            if (glGetShaderi(shader, 35713) == 0) {
                String log = glGetShaderInfoLog(shader);
                glDeleteShader(shader);
                handleCompileError(pw, source, log);
                return 0;
            } else {
                glAttachShader(program, shader);
                return shader;
            }
        }
    }

    public static int glSpecializeAndAttachShader(int program, int shaderType, ByteBuffer spirv, PipelineStateCache.Stats stats, PrintWriter pw) {
        int shader = glCreateShader(shaderType);
        if (shader == 0) {
            return 0;
        } else {
            MemoryStack stack = MemoryStack.stackPush();
            try {
                IntBuffer shaders = stack.ints(shader);
                glShaderBinary(shaders, 38225, spirv);
            } catch (Throwable var10) {
                if (stack != null) {
                    try {
                        stack.close();
                    } catch (Throwable var9) {
                        var10.addSuppressed(var9);
                    }
                }
                throw var10;
            }
            if (stack != null) {
                stack.close();
            }
            GL46C.glSpecializeShader(shader, "main", (IntBuffer) null, null);
            stats.incShaderCompilations();
            if (glGetShaderi(shader, 35713) == 0) {
                String log = glGetShaderInfoLog(shader);
                glDeleteShader(shader);
                pw.println("Shader specialization error");
                pw.println("Errors:");
                pw.println(log);
                return 0;
            } else {
                glAttachShader(program, shader);
                return shader;
            }
        }
    }

    public static void handleCompileError(PrintWriter pw, String source, String errors) {
        pw.println("Shader compilation error");
        pw.println("------------------------");
        String[] lines = source.split("\n");
        for (int i = 0; i < lines.length; i++) {
            pw.printf(Locale.ROOT, "%4s\t%s\n", i + 1, lines[i]);
        }
        pw.println("Errors:");
        pw.println(errors);
    }

    public static void handleLinkError(PrintWriter pw, String[] headers, String[] sources, String errors) {
        pw.println("Program linking error");
        pw.println("------------------------");
        for (int i = 0; i < headers.length; i++) {
            pw.println(headers[i]);
            String[] lines = sources[i].split("\n");
            for (int j = 0; j < lines.length; j++) {
                pw.printf(Locale.ROOT, "%4s\t%s\n", j + 1, lines[j]);
            }
        }
        pw.println("Errors:");
        pw.println(errors);
    }

    @Nonnull
    public static String getDebugSource(int source) {
        return switch(source) {
            case 33350 ->
                "API";
            case 33351 ->
                "Window System";
            case 33352 ->
                "Shader Compiler";
            case 33353 ->
                "Third Party";
            case 33354 ->
                "Application";
            case 33355 ->
                "Other";
            default ->
                APIUtil.apiUnknownToken(source);
        };
    }

    @Nonnull
    public static String getDebugType(int type) {
        return switch(type) {
            case 33356 ->
                "Error";
            case 33357 ->
                "Deprecated Behavior";
            case 33358 ->
                "Undefined Behavior";
            case 33359 ->
                "Portability";
            case 33360 ->
                "Performance";
            case 33361 ->
                "Other";
            case 33384 ->
                "Marker";
            default ->
                APIUtil.apiUnknownToken(type);
        };
    }

    @Nonnull
    public static String getDebugSeverity(int severity) {
        return switch(severity) {
            case 33387 ->
                "Notification";
            case 37190 ->
                "High";
            case 37191 ->
                "Medium";
            case 37192 ->
                "Low";
            default ->
                APIUtil.apiUnknownToken(severity);
        };
    }

    @Nonnull
    public static String getSourceARB(int source) {
        return switch(source) {
            case 33350 ->
                "API";
            case 33351 ->
                "Window System";
            case 33352 ->
                "Shader Compiler";
            case 33353 ->
                "Third Party";
            case 33354 ->
                "Application";
            case 33355 ->
                "Other";
            default ->
                APIUtil.apiUnknownToken(source);
        };
    }

    @Nonnull
    public static String getTypeARB(int type) {
        return switch(type) {
            case 33356 ->
                "Error";
            case 33357 ->
                "Deprecated Behavior";
            case 33358 ->
                "Undefined Behavior";
            case 33359 ->
                "Portability";
            case 33360 ->
                "Performance";
            case 33361 ->
                "Other";
            default ->
                APIUtil.apiUnknownToken(type);
        };
    }

    @Nonnull
    public static String getSeverityARB(int severity) {
        return switch(severity) {
            case 37190 ->
                "High";
            case 37191 ->
                "Medium";
            case 37192 ->
                "Low";
            default ->
                APIUtil.apiUnknownToken(severity);
        };
    }

    @Nonnull
    public static String getCategoryAMD(int category) {
        return switch(category) {
            case 37193 ->
                "API Error";
            case 37194 ->
                "Window System";
            case 37195 ->
                "Deprecation";
            case 37196 ->
                "Undefined Behavior";
            case 37197 ->
                "Performance";
            case 37198 ->
                "Shader Compiler";
            case 37199 ->
                "Application";
            case 37200 ->
                "Other";
            default ->
                APIUtil.apiUnknownToken(category);
        };
    }

    @Nonnull
    public static String getSeverityAMD(int severity) {
        return switch(severity) {
            case 37190 ->
                "High";
            case 37191 ->
                "Medium";
            case 37192 ->
                "Low";
            default ->
                APIUtil.apiUnknownToken(severity);
        };
    }
}