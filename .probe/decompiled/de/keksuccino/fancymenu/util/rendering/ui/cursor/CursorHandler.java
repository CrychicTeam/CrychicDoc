package de.keksuccino.fancymenu.util.rendering.ui.cursor;

import com.mojang.blaze3d.platform.TextureUtil;
import de.keksuccino.fancymenu.events.ticking.ClientTickEvent;
import de.keksuccino.fancymenu.util.CloseableUtils;
import de.keksuccino.fancymenu.util.event.acara.EventHandler;
import de.keksuccino.fancymenu.util.event.acara.EventListener;
import de.keksuccino.fancymenu.util.resource.resources.texture.SimpleTexture;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

public class CursorHandler {

    private static final Logger LOGGER = LogManager.getLogger();

    public static final long CURSOR_RESIZE_HORIZONTAL = GLFW.glfwCreateStandardCursor(221189);

    public static final long CURSOR_RESIZE_VERTICAL = GLFW.glfwCreateStandardCursor(221190);

    public static final long CURSOR_RESIZE_ALL = GLFW.glfwCreateStandardCursor(221193);

    public static final long CURSOR_WRITING = GLFW.glfwCreateStandardCursor(221186);

    public static final long CURSOR_POINTING_HAND = GLFW.glfwCreateStandardCursor(221188);

    private static final long CURSOR_NORMAL = GLFW.glfwCreateStandardCursor(221185);

    private static final Map<String, CursorHandler.CustomCursor> CUSTOM_CURSORS = new HashMap();

    private static long clientTickCursor = -2L;

    private static boolean initialized = false;

    public static void init() {
        if (!initialized) {
            initialized = true;
            EventHandler.INSTANCE.registerListenersOf(new CursorHandler());
        }
    }

    public static void registerCustomCursor(@NotNull String uniqueCursorName, @NotNull CursorHandler.CustomCursor cursor) {
        if (!initialized) {
            throw new RuntimeException("[FANCYMENU] CursorHandler accessed too early!");
        } else {
            LOGGER.info("[FANCYMENU] Registering GLFW custom cursor: NAME: " + uniqueCursorName + " | TEXTURE CONTEXT: " + cursor.textureName);
            CUSTOM_CURSORS.put((String) Objects.requireNonNull(uniqueCursorName), (CursorHandler.CustomCursor) Objects.requireNonNull(cursor));
        }
    }

    public static void unregisterCustomCursor(@NotNull String cursorName) {
        if (!initialized) {
            throw new RuntimeException("[FANCYMENU] CursorHandler accessed too early!");
        } else {
            CursorHandler.CustomCursor c = (CursorHandler.CustomCursor) CUSTOM_CURSORS.get(cursorName);
            if (c != null) {
                c.destroy();
            }
            CUSTOM_CURSORS.remove(cursorName);
        }
    }

    @Nullable
    public static CursorHandler.CustomCursor getCustomCursor(@NotNull String cursorName) {
        if (!initialized) {
            throw new RuntimeException("[FANCYMENU] CursorHandler accessed too early!");
        } else {
            return (CursorHandler.CustomCursor) CUSTOM_CURSORS.get(cursorName);
        }
    }

    public static void setClientTickCursor(long cursor) {
        if (!initialized) {
            throw new RuntimeException("[FANCYMENU] CursorHandler accessed too early!");
        } else {
            clientTickCursor = cursor;
        }
    }

    public static void setClientTickCursor(@NotNull String customCursorName) {
        if (!initialized) {
            throw new RuntimeException("[FANCYMENU] CursorHandler accessed too early!");
        } else {
            CursorHandler.CustomCursor c = getCustomCursor(customCursorName);
            if (c != null) {
                setClientTickCursor(c.id_long);
            }
        }
    }

    private static void setCursor(long cursor) {
        if (!initialized) {
            throw new RuntimeException("[FANCYMENU] CursorHandler accessed too early!");
        } else {
            GLFW.glfwSetCursor(Minecraft.getInstance().getWindow().getWindow(), cursor);
        }
    }

    @EventListener
    public void onClientTickPre(ClientTickEvent.Pre e) {
        if (clientTickCursor != -1L && clientTickCursor != -2L) {
            setCursor(clientTickCursor);
            clientTickCursor = -1L;
        } else if (clientTickCursor == -1L) {
            setCursor(CURSOR_NORMAL);
            clientTickCursor = -2L;
        }
    }

    public static class CustomCursor {

        public final long id_long;

        public final int hotspotX;

        public final int hotspotY;

        @NotNull
        public final SimpleTexture texture;

        @NotNull
        public final String textureName;

        @Nullable
        public static CursorHandler.CustomCursor create(@NotNull SimpleTexture texture, int hotspotX, int hotspotY, @NotNull String textureName) {
            CursorHandler.CustomCursor customCursor = null;
            InputStream in = null;
            MemoryStack memStack = null;
            ByteBuffer texResourceBuffer = null;
            ByteBuffer stbBuffer = null;
            try {
                Objects.requireNonNull(texture);
                texture.waitForReady(5000L);
                if (texture.isReady()) {
                    in = (InputStream) Objects.requireNonNull(texture.open());
                    texResourceBuffer = TextureUtil.readResource(in);
                    texResourceBuffer.rewind();
                    if (MemoryUtil.memAddress(texResourceBuffer) == 0L) {
                        throw new IllegalArgumentException("Invalid buffer! Memory address was NULL!");
                    }
                    memStack = MemoryStack.stackPush();
                    IntBuffer width = memStack.mallocInt(1);
                    IntBuffer height = memStack.mallocInt(1);
                    IntBuffer components = memStack.mallocInt(1);
                    stbBuffer = STBImage.stbi_load_from_memory(texResourceBuffer, width, height, components, 0);
                    if (stbBuffer == null) {
                        throw new IOException("Could not load image: " + STBImage.stbi_failure_reason());
                    }
                    GLFWImage image = GLFWImage.create();
                    image = image.set(texture.getWidth(), texture.getHeight(), stbBuffer);
                    long lid = GLFW.glfwCreateCursor(image, hotspotX, hotspotY);
                    if (lid == 0L) {
                        throw new IllegalArgumentException("Failed to create custom cursor! Cursor handle was NULL!");
                    }
                    customCursor = new CursorHandler.CustomCursor(lid, hotspotX, hotspotY, texture, textureName);
                }
            } catch (Exception var17) {
                var17.printStackTrace();
            }
            if (texResourceBuffer != null) {
                try {
                    MemoryUtil.memFree(texResourceBuffer);
                } catch (Exception var16) {
                    var16.printStackTrace();
                }
            }
            if (stbBuffer != null) {
                try {
                    STBImage.stbi_image_free(stbBuffer);
                } catch (Exception var15) {
                    var15.printStackTrace();
                }
            }
            CloseableUtils.closeQuietly(in);
            CloseableUtils.closeQuietly(memStack);
            return customCursor;
        }

        protected CustomCursor(long id_long, int hotspotX, int hotspotY, @NotNull SimpleTexture texture, @NotNull String textureName) {
            this.id_long = id_long;
            this.hotspotX = hotspotX;
            this.hotspotY = hotspotY;
            this.texture = texture;
            this.textureName = textureName;
        }

        public void destroy() {
        }
    }
}