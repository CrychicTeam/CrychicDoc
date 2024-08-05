package me.jellysquid.mods.sodium.client.platform;

import com.mojang.blaze3d.platform.Window;
import java.nio.ByteBuffer;
import java.util.Objects;
import me.jellysquid.mods.sodium.client.compatibility.environment.OSInfo;
import me.jellysquid.mods.sodium.client.platform.windows.api.User32;
import me.jellysquid.mods.sodium.client.platform.windows.api.msgbox.MsgBoxCallback;
import me.jellysquid.mods.sodium.client.platform.windows.api.msgbox.MsgBoxParamSw;
import net.minecraft.Util;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFWNativeWin32;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

public class MessageBox {

    @Nullable
    private static final MessageBox.MessageBoxImpl IMPL = MessageBox.MessageBoxImpl.chooseImpl();

    public static void showMessageBox(@Nullable Window window, MessageBox.IconType icon, String title, String description, @Nullable String helpUrl) {
        if (IMPL != null) {
            IMPL.showMessageBox(window, icon, title, description, helpUrl);
        }
    }

    public static enum IconType {

        INFO, WARNING, ERROR
    }

    private interface MessageBoxImpl {

        @Nullable
        static MessageBox.MessageBoxImpl chooseImpl() {
            return OSInfo.getOS() == OSInfo.OS.WINDOWS ? new MessageBox.WindowsMessageBoxImpl() : null;
        }

        void showMessageBox(@Nullable Window var1, MessageBox.IconType var2, String var3, String var4, @Nullable String var5);
    }

    private static class WindowsMessageBoxImpl implements MessageBox.MessageBoxImpl {

        @Override
        public void showMessageBox(@Nullable Window window, MessageBox.IconType icon, String title, String description, @Nullable String helpUrl) {
            Objects.requireNonNull(title);
            Objects.requireNonNull(description);
            Objects.requireNonNull(icon);
            MsgBoxCallback msgBoxCallback;
            if (helpUrl != null) {
                msgBoxCallback = MsgBoxCallback.create(lpHelpInfo -> Util.getPlatform().openUri(helpUrl));
            } else {
                msgBoxCallback = null;
            }
            long hWndOwner;
            if (window != null) {
                hWndOwner = GLFWNativeWin32.glfwGetWin32Window(window.getWindow());
            } else {
                hWndOwner = 0L;
            }
            try {
                MemoryStack stack = MemoryStack.stackPush();
                try {
                    ByteBuffer lpText = stack.malloc(MemoryUtil.memLengthUTF16(description, true));
                    MemoryUtil.memUTF16(description, true, lpText);
                    ByteBuffer lpCaption = stack.malloc(MemoryUtil.memLengthUTF16(title, true));
                    MemoryUtil.memUTF16(title, true, lpCaption);
                    MsgBoxParamSw params = MsgBoxParamSw.allocate(stack);
                    params.setCbSize(MsgBoxParamSw.SIZEOF);
                    params.setHWndOwner(hWndOwner);
                    params.setText(lpText);
                    params.setCaption(lpCaption);
                    params.setStyle(getStyle(icon, msgBoxCallback != null));
                    params.setCallback(msgBoxCallback);
                    User32.callMessageBoxIndirectW(params);
                } catch (Throwable var18) {
                    if (stack != null) {
                        try {
                            stack.close();
                        } catch (Throwable var17) {
                            var18.addSuppressed(var17);
                        }
                    }
                    throw var18;
                }
                if (stack != null) {
                    stack.close();
                }
            } finally {
                if (msgBoxCallback != null) {
                    msgBoxCallback.free();
                }
            }
        }

        private static int getStyle(MessageBox.IconType icon, boolean showHelp) {
            int style = switch(icon) {
                case INFO ->
                    64;
                case WARNING ->
                    48;
                case ERROR ->
                    16;
            };
            if (showHelp) {
                style |= 16384;
            }
            return style;
        }
    }
}