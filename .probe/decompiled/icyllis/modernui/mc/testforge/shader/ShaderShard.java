package icyllis.modernui.mc.testforge.shader;

import icyllis.modernui.ModernUI;
import icyllis.modernui.annotation.RenderThread;
import it.unimi.dsi.fastutil.objects.Object2ObjectAVLTreeMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import javax.annotation.Nonnull;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import org.lwjgl.opengl.GL43C;

@Deprecated
public final class ShaderShard {

    private static final Object2ObjectMap<ResourceLocation, ShaderShard> SHADERS = new Object2ObjectAVLTreeMap();

    private final ResourceLocation mLocation;

    private final int mId;

    private int mAttachCount = 0;

    private boolean mDeleted = false;

    private ShaderShard(ResourceLocation location, int id) {
        this.mLocation = location;
        this.mId = id;
    }

    public void attach(@Nonnull GLProgram program) {
        if (this.mAttachCount == Integer.MIN_VALUE) {
            throw new IllegalStateException(this + " has been deleted.");
        } else {
            this.mAttachCount++;
            GL43C.glAttachShader(program.get(), this.mId);
            if (this.mDeleted) {
                ModernUI.LOGGER.warn(ModernUI.MARKER, "{} is marked as deleted, but the shader is still trying to attach to program {}", this, program);
            }
        }
    }

    public void detach(@Nonnull GLProgram program) {
        if (this.mAttachCount > 0) {
            this.mAttachCount--;
            GL43C.glDetachShader(program.get(), this.mId);
            if (this.mAttachCount == 0 && this.mDeleted) {
                SHADERS.remove(this.mLocation);
                this.mAttachCount = Integer.MIN_VALUE;
            }
        } else {
            ModernUI.LOGGER.warn(ModernUI.MARKER, "Try to detach {} from {}, but the shader is not attached to any program", this, program);
        }
    }

    public int getAttachCount() {
        return this.mAttachCount;
    }

    public void delete() {
        if (!this.mDeleted) {
            GL43C.glDeleteShader(this.mId);
            if (this.mAttachCount <= 0) {
                SHADERS.remove(this.mLocation);
                this.mAttachCount = Integer.MIN_VALUE;
            }
            this.mDeleted = true;
        }
    }

    @RenderThread
    public static ShaderShard getOrCreate(ResourceManager manager, ResourceLocation location, ShaderShard.Type type) throws IOException {
        ShaderShard shader = (ShaderShard) SHADERS.get(location);
        if (shader != null) {
            return shader;
        } else {
            InputStream stream = manager.m_215595_(location);
            try {
                String src = null;
                if (src == null) {
                    throw new IOException("Failed to read shader source (" + location + ")");
                }
                int id = GL43C.glCreateShader(type.type);
                GL43C.glShaderSource(id, src);
                GL43C.glCompileShader(id);
                if (GL43C.glGetShaderi(id, 35713) != 1) {
                    String y = GL43C.glGetShaderInfoLog(id, 32768).trim();
                    throw new IOException("Failed to compile " + type.getName() + " shader (" + location + ") : " + y);
                }
                SHADERS.put(location, shader = new ShaderShard(location, id));
            } catch (Throwable var9) {
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (Throwable var8) {
                        var9.addSuppressed(var8);
                    }
                }
                throw var9;
            }
            if (stream != null) {
                stream.close();
            }
            return shader;
        }
    }

    public static void deleteAll() {
        SHADERS.values().forEach(ShaderShard::delete);
        if (!SHADERS.isEmpty()) {
            throw new IllegalStateException("There are still " + SHADERS.size() + " shaders attaching to some programs.");
        }
    }

    public static enum Type {

        VERTEX(35633),
        FRAGMENT(35632),
        GEOMETRY(36313),
        TESS_CONTROL(36488),
        TESS_EVALUATION(36487),
        COMPUTE(37305);

        private final int type;

        private Type(int type) {
            this.type = type;
        }

        @Nonnull
        private String getName() {
            return this.name().toLowerCase(Locale.ROOT).replace('_', ' ');
        }
    }
}