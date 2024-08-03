package icyllis.modernui.core;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import icyllis.modernui.text.Editable;
import icyllis.modernui.text.Selection;
import icyllis.modernui.view.KeyEvent;
import icyllis.modernui.view.MotionEvent;
import icyllis.modernui.view.ViewRoot;
import icyllis.modernui.widget.EditText;
import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.MemoryStack;

public final class ActivityWindow extends Window {

    private static volatile ActivityWindow sMainWindow;

    private int mScreenX;

    private int mScreenY;

    private int mScreenWidth;

    private int mScreenHeight;

    private int mWidth;

    private int mHeight;

    private volatile ViewRoot mRoot;

    private int mButtonState;

    private final StringBuilder mCharInputBuffer = new StringBuilder();

    private final Runnable mCommitCharInput = this::commitCharInput;

    ActivityWindow(long handle) {
        super(handle);
        sMainWindow = this;
        GLFW.glfwSetWindowPosCallback(handle, this::onPosCallback);
        GLFW.glfwSetWindowSizeCallback(handle, this::onSizeCallback);
        GLFW.glfwSetWindowRefreshCallback(handle, this::onRefreshCallback);
        GLFW.glfwSetWindowFocusCallback(handle, this::onFocusCallback);
        GLFW.glfwSetWindowIconifyCallback(handle, this::onMinimizeCallback);
        GLFW.glfwSetWindowMaximizeCallback(handle, this::onMaximizeCallback);
        GLFW.glfwSetFramebufferSizeCallback(handle, this::onFramebufferSizeCallback);
        GLFW.glfwSetWindowContentScaleCallback(handle, this::onContentScaleCallback);
        GLFW.glfwSetKeyCallback(handle, this::onKeyCallback);
        GLFW.glfwSetCharCallback(handle, this::onCharCallback);
        GLFW.glfwSetMouseButtonCallback(handle, this::onMouseButtonCallback);
        GLFW.glfwSetCursorPosCallback(handle, this::onCursorPosCallback);
        GLFW.glfwSetScrollCallback(handle, this::onScrollCallback);
        MemoryStack stack = MemoryStack.stackPush();
        try {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            GLFW.glfwGetWindowPos(handle, w, h);
            this.mScreenX = w.get(0);
            this.mScreenY = h.get(0);
            GLFW.glfwGetWindowSize(handle, w, h);
            this.mScreenWidth = w.get(0);
            this.mScreenHeight = h.get(0);
            GLFW.glfwGetFramebufferSize(handle, w, h);
            this.mWidth = w.get(0);
            this.mHeight = h.get(0);
        } catch (Throwable var7) {
            if (stack != null) {
                try {
                    stack.close();
                } catch (Throwable var6) {
                    var7.addSuppressed(var6);
                }
            }
            throw var7;
        }
        if (stack != null) {
            stack.close();
        }
    }

    @NonNull
    public static ActivityWindow createMainWindow(@NonNull String title, int width, int height) {
        return createMainWindow(title, width, height, null);
    }

    @NonNull
    public static ActivityWindow createMainWindow(@NonNull String title, int width, int height, @Nullable Monitor monitor) {
        Core.checkMainThread();
        if (sMainWindow != null) {
            throw new IllegalStateException("Multiple main windows");
        } else {
            long handle = GLFW.glfwCreateWindow(width, height, title, monitor == null ? 0L : monitor.getHandle(), 0L);
            if (handle == 0L) {
                throw new IllegalStateException("Failed to create window");
            } else {
                return new ActivityWindow(handle);
            }
        }
    }

    private void onPosCallback(long w, int xPos, int yPos) {
        this.mScreenX = xPos;
        this.mScreenY = yPos;
    }

    @Override
    public int getScreenX() {
        return this.mScreenX;
    }

    @Override
    public int getScreenY() {
        return this.mScreenY;
    }

    private void onSizeCallback(long w, int width, int height) {
        this.mScreenWidth = width;
        this.mScreenHeight = height;
    }

    @Override
    public int getScreenWidth() {
        return this.mScreenWidth;
    }

    @Override
    public int getScreenHeight() {
        return this.mScreenHeight;
    }

    private void onRefreshCallback(long w) {
    }

    private void onFocusCallback(long w, boolean focused) {
    }

    private void onMinimizeCallback(long w, boolean minimized) {
    }

    private void onMaximizeCallback(long w, boolean maximized) {
    }

    private void onFramebufferSizeCallback(long w, int width, int height) {
        this.mWidth = width;
        this.mHeight = height;
        if (this.mRoot != null) {
            this.mRoot.mHandler.post(() -> this.mRoot.setFrame(this.mWidth, this.mHeight));
        }
    }

    @Override
    public int getWidth() {
        return this.mWidth;
    }

    @Override
    public int getHeight() {
        return this.mHeight;
    }

    private void onContentScaleCallback(long w, float xScale, float yScale) {
    }

    public void center(@NonNull Monitor monitor) {
        VideoMode mode = monitor.getCurrentMode();
        GLFW.glfwSetWindowPos(this.mHandle, (mode.getWidth() - this.mScreenWidth) / 2 + monitor.getXPos(), (mode.getHeight() - this.mScreenHeight) / 2 + monitor.getYPos());
    }

    public void install(@NonNull ViewRoot root) {
        this.mRoot = root;
        root.setFrame(this.mWidth, this.mHeight);
    }

    private void onKeyCallback(long w, int key, int scancode, int action, int mods) {
        if (this.mRoot != null) {
            KeyEvent keyEvent = KeyEvent.obtain(Core.timeNanos(), action == 0 ? 1 : 0, key, 0, mods, scancode, 0);
            this.mRoot.enqueueInputEvent(keyEvent);
        }
    }

    private void onCharCallback(long w, int codepoint) {
        if (this.mRoot != null) {
            if (codepoint != 0 && codepoint != 127) {
                this.mCharInputBuffer.appendCodePoint(codepoint);
                Core.postOnMainThread(this.mCommitCharInput);
            }
        }
    }

    private void commitCharInput() {
        if (!this.mCharInputBuffer.isEmpty()) {
            String input = this.mCharInputBuffer.toString();
            this.mCharInputBuffer.setLength(0);
            Message msg = Message.obtain(this.mRoot.mHandler, () -> {
                if (this.mRoot != null && this.mRoot.getView().findFocus() instanceof EditText text) {
                    Editable content = text.getText();
                    int selStart = text.getSelectionStart();
                    int selEnd = text.getSelectionEnd();
                    if (selStart >= 0 && selEnd >= 0) {
                        Selection.setSelection(content, Math.max(selStart, selEnd));
                        content.replace(Math.min(selStart, selEnd), Math.max(selStart, selEnd), input);
                    }
                }
            });
            msg.setAsynchronous(true);
            msg.sendToTarget();
        }
    }

    private void onMouseButtonCallback(long w, int button, int action, int mods) {
        if (this.mRoot != null) {
            MemoryStack stack = MemoryStack.stackPush();
            double cursorX;
            double cursorY;
            try {
                DoubleBuffer x = stack.mallocDouble(1);
                DoubleBuffer y = stack.mallocDouble(1);
                GLFW.glfwGetCursorPos(w, x, y);
                cursorX = x.get(0);
                cursorY = y.get(0);
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
            long now = Core.timeNanos();
            float x = (float) (cursorX * (double) this.mWidth / (double) this.mScreenWidth);
            float y = (float) (cursorY * (double) this.mHeight / (double) this.mScreenHeight);
            int buttonState = 0;
            for (int i = 0; i < 5; i++) {
                if (GLFW.glfwGetMouseButton(w, i) == 1) {
                    buttonState |= 1 << i;
                }
            }
            this.mButtonState = buttonState;
            int actionButton = 1 << button;
            action = action == 1 ? 0 : 1;
            if (action == 0 && (buttonState ^ actionButton) == 0 || action == 1 && buttonState == 0) {
                MotionEvent ev = MotionEvent.obtain(now, action, actionButton, x, y, mods, buttonState, 0);
                this.mRoot.enqueueInputEvent(ev);
            }
        }
    }

    private void onCursorPosCallback(long w, double cursorX, double cursorY) {
        if (this.mRoot != null) {
            long now = Core.timeNanos();
            float x = (float) (cursorX * (double) this.mWidth / (double) this.mScreenWidth);
            float y = (float) (cursorY * (double) this.mHeight / (double) this.mScreenHeight);
            MotionEvent event = MotionEvent.obtain(now, 7, x, y, 0);
            this.mRoot.enqueueInputEvent(event);
            if (this.mButtonState > 0) {
                event = MotionEvent.obtain(now, 2, 0, x, y, 0, this.mButtonState, 0);
                this.mRoot.enqueueInputEvent(event);
            }
        }
    }

    private void onScrollCallback(long w, double deltaX, double deltaY) {
        if (this.mRoot != null) {
            MemoryStack stack = MemoryStack.stackPush();
            double cursorX;
            double cursorY;
            try {
                DoubleBuffer x = stack.mallocDouble(1);
                DoubleBuffer y = stack.mallocDouble(1);
                GLFW.glfwGetCursorPos(w, x, y);
                cursorX = x.get(0);
                cursorY = y.get(0);
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
            int mods = 0;
            if (GLFW.glfwGetKey(w, 341) == 1 || GLFW.glfwGetKey(w, 345) == 1) {
                mods |= KeyEvent.META_CTRL_ON;
            }
            if (GLFW.glfwGetKey(w, 340) == 1 || GLFW.glfwGetKey(w, 344) == 1) {
                mods |= 1;
            }
            long now = Core.timeNanos();
            float x = (float) (cursorX * (double) this.mWidth / (double) this.mScreenWidth);
            float y = (float) (cursorY * (double) this.mHeight / (double) this.mScreenHeight);
            MotionEvent event = MotionEvent.obtain(now, 8, x, y, mods);
            event.setAxisValue(10, (float) deltaX);
            event.setAxisValue(9, (float) deltaY);
            this.mRoot.enqueueInputEvent(event);
        }
    }
}