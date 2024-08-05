package icyllis.modernui.core;

import icyllis.modernui.annotation.MainThread;
import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import icyllis.modernui.text.TextUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;

@MainThread
public final class Clipboard {

    @Nullable
    public static String getText() {
        GLFWErrorCallback callback = GLFW.glfwSetErrorCallback(null);
        String text = GLFW.glfwGetClipboardString(0L);
        GLFW.glfwSetErrorCallback(callback);
        return text == null ? null : TextUtils.validateSurrogatePairs(text);
    }

    public static void setText(@NonNull CharSequence text) {
        GLFW.glfwSetClipboardString(0L, text);
    }
}