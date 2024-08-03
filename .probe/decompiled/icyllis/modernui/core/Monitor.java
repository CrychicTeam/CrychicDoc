package icyllis.modernui.core;

import icyllis.modernui.annotation.MainThread;
import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import it.unimi.dsi.fastutil.longs.Long2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import it.unimi.dsi.fastutil.objects.ObjectCollections;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.jetbrains.annotations.UnmodifiableView;
import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWMonitorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWVidMode.Buffer;
import org.lwjgl.system.MemoryStack;

@MainThread
public final class Monitor {

    private static final Long2ObjectArrayMap<Monitor> sMonitors = new Long2ObjectArrayMap();

    private static final ObjectCollection<Monitor> sMonitorsView = ObjectCollections.unmodifiable(sMonitors.values());

    private static final CopyOnWriteArrayList<Monitor.MonitorEventListener> sListeners = new CopyOnWriteArrayList();

    private final long mHandle;

    private final int mXPos;

    private final int mYPos;

    private final VideoMode[] mVideoModes;

    private static void onMonitorCallback(long monitor, int event) {
        if (event == 262145) {
            Monitor mon = new Monitor(monitor);
            sMonitors.put(monitor, mon);
            for (Monitor.MonitorEventListener listener : sListeners) {
                listener.onMonitorConnected(mon);
            }
        } else if (event == 262146) {
            Monitor mon = (Monitor) sMonitors.remove(monitor);
            if (mon != null) {
                for (Monitor.MonitorEventListener listener : sListeners) {
                    listener.onMonitorDisconnected(mon);
                }
            }
        }
    }

    @Nullable
    public static Monitor get(long handle) {
        return (Monitor) sMonitors.get(handle);
    }

    @Nullable
    public static Monitor getPrimary() {
        return (Monitor) sMonitors.get(GLFW.glfwGetPrimaryMonitor());
    }

    @UnmodifiableView
    public static Collection<Monitor> getAll() {
        return sMonitorsView;
    }

    public static void addMonitorEventListener(@NonNull Monitor.MonitorEventListener listener) {
        if (!sListeners.contains(listener)) {
            sListeners.add(listener);
        }
    }

    public static void removeMonitorEventListener(@NonNull Monitor.MonitorEventListener listener) {
        sListeners.remove(listener);
    }

    private Monitor(long handle) {
        this.mHandle = handle;
        MemoryStack stack = MemoryStack.stackPush();
        try {
            IntBuffer x = stack.mallocInt(1);
            IntBuffer y = stack.mallocInt(1);
            GLFW.glfwGetMonitorPos(handle, x, y);
            this.mXPos = x.get(0);
            this.mYPos = y.get(0);
        } catch (Throwable var8) {
            if (stack != null) {
                try {
                    stack.close();
                } catch (Throwable var7) {
                    var8.addSuppressed(var7);
                }
            }
            throw var8;
        }
        if (stack != null) {
            stack.close();
        }
        List<VideoMode> list = new ArrayList();
        Buffer buffer = GLFW.glfwGetVideoModes(handle);
        if (buffer == null) {
            throw new IllegalStateException("Failed to get video modes");
        } else {
            for (int i = buffer.limit() - 1; i >= 0; i--) {
                buffer.position(i);
                VideoMode m = new VideoMode(buffer);
                if (m.getRedBits() >= 8 && m.getGreenBits() >= 8 && m.getBlueBits() >= 8) {
                    list.add(m);
                }
            }
            this.mVideoModes = (VideoMode[]) list.toArray(new VideoMode[0]);
        }
    }

    public long getHandle() {
        return this.mHandle;
    }

    public int getXPos() {
        return this.mXPos;
    }

    public int getYPos() {
        return this.mYPos;
    }

    @NonNull
    public VideoMode getCurrentMode() {
        GLFWVidMode mode = GLFW.glfwGetVideoMode(this.mHandle);
        if (mode == null) {
            throw new IllegalStateException("Failed to get current video mode");
        } else {
            return new VideoMode(mode);
        }
    }

    @NonNull
    public String getName() {
        String s = GLFW.glfwGetMonitorName(this.mHandle);
        return s == null ? "" : s;
    }

    public int getModeCount() {
        return this.mVideoModes.length;
    }

    @NonNull
    public VideoMode getModeAt(int index) {
        return this.mVideoModes[index];
    }

    @NonNull
    public VideoMode findBestMode(int width, int height) {
        return (VideoMode) Arrays.stream(this.mVideoModes).filter(m -> m.getWidth() <= width && m.getHeight() <= height).sorted((c1, c2) -> c2.getWidth() - c1.getWidth()).sorted((c1, c2) -> c2.getHeight() - c1.getHeight()).max(Comparator.comparingInt(VideoMode::getRefreshRate)).orElse(this.mVideoModes[0]);
    }

    static {
        GLFWMonitorCallback prev = GLFW.glfwSetMonitorCallback(Monitor::onMonitorCallback);
        if (prev != null) {
            prev.close();
        }
        PointerBuffer pointers = GLFW.glfwGetMonitors();
        if (pointers != null) {
            for (int i = 0; i < pointers.limit(); i++) {
                long p = pointers.get(i);
                sMonitors.put(p, new Monitor(p));
            }
        }
    }

    public interface MonitorEventListener {

        void onMonitorConnected(@NonNull Monitor var1);

        void onMonitorDisconnected(@NonNull Monitor var1);
    }
}