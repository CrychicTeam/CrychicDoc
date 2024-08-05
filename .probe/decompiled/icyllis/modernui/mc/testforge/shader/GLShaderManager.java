package icyllis.modernui.mc.testforge.shader;

import icyllis.arc3d.opengl.GLCore;
import icyllis.modernui.ModernUI;
import icyllis.modernui.annotation.RenderThread;
import icyllis.modernui.core.Core;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nonnull;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

@Deprecated
public class GLShaderManager {

    private static final GLShaderManager INSTANCE = new GLShaderManager();

    private final Set<GLShaderManager.Listener> mListeners = new HashSet();

    private Map<String, Object2IntMap<String>> mShaders = new HashMap();

    private GLShaderManager() {
    }

    public static GLShaderManager getInstance() {
        return INSTANCE;
    }

    public void addListener(@Nonnull GLShaderManager.Listener listener) {
        this.mListeners.add(listener);
    }

    public void removeListener(@Nonnull GLShaderManager.Listener listener) {
        this.mListeners.remove(listener);
    }

    public void reload() {
        Core.checkRenderThread();
        for (Object2IntMap<String> map : this.mShaders.values()) {
            IntIterator var3 = map.values().iterator();
            while (var3.hasNext()) {
                int stage = (Integer) var3.next();
                GLCore.glDeleteShader(stage);
            }
        }
        this.mShaders.clear();
        for (GLShaderManager.Listener l : this.mListeners) {
            l.onReload(this);
        }
        for (Object2IntMap<String> map : this.mShaders.values()) {
            IntIterator var9 = map.values().iterator();
            while (var9.hasNext()) {
                int stage = (Integer) var9.next();
                GLCore.glDeleteShader(stage);
            }
        }
        this.mShaders.clear();
        this.mShaders = new HashMap();
    }

    public int getStage(@Nonnull String namespace, @Nonnull String entry) {
        return this.getStage(namespace, "shaders/" + entry, 0);
    }

    public int getStage(@Nonnull String namespace, @Nonnull String path, int type) {
        Core.checkRenderThread();
        int shader = ((Object2IntMap) this.mShaders.computeIfAbsent(namespace, n -> {
            Object2IntMap<String> r = new Object2IntOpenHashMap();
            r.defaultReturnValue(-1);
            return r;
        })).getInt(path);
        if (shader != -1) {
            return shader;
        } else {
            if (type == 0) {
                if (path.endsWith(".vert")) {
                    type = 35633;
                } else if (path.endsWith(".frag")) {
                    type = 35632;
                } else {
                    if (!path.endsWith(".geom")) {
                        ModernUI.LOGGER.warn(ModernUI.MARKER, "Unknown type identifier for shader source {}:{}", namespace, path);
                        return 0;
                    }
                    type = 36313;
                }
            }
            ByteBuffer source = null;
            label160: {
                byte var11;
                try {
                    InputStream stream;
                    label178: {
                        stream = ModernUI.getInstance().getResourceStream(namespace, path);
                        int log;
                        try {
                            label170: {
                                MemoryStack stack = MemoryStack.stackPush();
                                label148: {
                                    try {
                                        source = Core.readIntoNativeBuffer(stream);
                                        shader = GLCore.glCreateShader(type);
                                        IntBuffer pLength = stack.mallocInt(1);
                                        pLength.put(0, source.position());
                                        PointerBuffer pString = stack.mallocPointer(1);
                                        pString.put(0, MemoryUtil.memAddress0(source));
                                        GLCore.glShaderSource(shader, pString, pLength);
                                        GLCore.glCompileShader(shader);
                                        if (GLCore.glGetShaderi(shader, 35713) == 0) {
                                            String logx = GLCore.glGetShaderInfoLog(shader, 8192).trim();
                                            ModernUI.LOGGER.error(ModernUI.MARKER, "Failed to compile shader {}:{}\n{}", namespace, path, logx);
                                            GLCore.glDeleteShader(shader);
                                            ((Object2IntMap) this.mShaders.get(namespace)).putIfAbsent(path, 0);
                                            var11 = 0;
                                            break label148;
                                        }
                                        ((Object2IntMap) this.mShaders.get(namespace)).putIfAbsent(path, shader);
                                        log = shader;
                                    } catch (Throwable var21) {
                                        if (stack != null) {
                                            try {
                                                stack.close();
                                            } catch (Throwable var20) {
                                                var21.addSuppressed(var20);
                                            }
                                        }
                                        throw var21;
                                    }
                                    if (stack != null) {
                                        stack.close();
                                    }
                                    break label170;
                                }
                                if (stack != null) {
                                    stack.close();
                                }
                                break label178;
                            }
                        } catch (Throwable var22) {
                            if (stream != null) {
                                try {
                                    stream.close();
                                } catch (Throwable var19) {
                                    var22.addSuppressed(var19);
                                }
                            }
                            throw var22;
                        }
                        if (stream != null) {
                            stream.close();
                        }
                        return log;
                    }
                    if (stream != null) {
                        stream.close();
                    }
                } catch (IOException var23) {
                    ModernUI.LOGGER.error(ModernUI.MARKER, "Failed to get shader source {}:{}\n", namespace, path, var23);
                    break label160;
                } finally {
                    MemoryUtil.memFree(source);
                }
                return var11;
            }
            ((Object2IntMap) this.mShaders.get(namespace)).putIfAbsent(path, 0);
            return 0;
        }
    }

    public boolean create(GLProgram t, int... stages) {
        Core.checkRenderThread();
        int program;
        if (t.mProgram != 0) {
            program = t.mProgram;
        } else {
            program = GLCore.glCreateProgram();
        }
        for (int s : stages) {
            GLCore.glAttachShader(program, s);
        }
        GLCore.glLinkProgram(program);
        if (GLCore.glGetProgrami(program, 35714) == 0) {
            String log = GLCore.glGetProgramInfoLog(program, 8192);
            ModernUI.LOGGER.error(ModernUI.MARKER, "Failed to link shader program\n{}", log);
            GLCore.glDeleteProgram(program);
            program = 0;
        } else {
            for (int s : stages) {
                GLCore.glDetachShader(program, s);
            }
        }
        t.mProgram = program;
        return program != 0;
    }

    @FunctionalInterface
    public interface Listener {

        @RenderThread
        void onReload(@Nonnull GLShaderManager var1);
    }
}