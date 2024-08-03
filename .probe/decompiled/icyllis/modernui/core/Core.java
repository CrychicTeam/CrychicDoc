package icyllis.modernui.core;

import icyllis.arc3d.core.RefCnt;
import icyllis.arc3d.engine.ContextOptions;
import icyllis.arc3d.engine.DirectContext;
import icyllis.arc3d.engine.RecordingContext;
import icyllis.arc3d.opengl.GLCaps;
import icyllis.arc3d.opengl.GLCore;
import icyllis.modernui.ModernUI;
import icyllis.modernui.annotation.MainThread;
import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.RenderThread;
import icyllis.modernui.annotation.UiThread;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.ref.Cleaner;
import java.lang.ref.Cleaner.Cleanable;
import java.net.URI;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SeekableByteChannel;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import org.apache.logging.log4j.Level;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.AMDDebugOutput;
import org.lwjgl.opengl.ARBDebugOutput;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLCapabilities;
import org.lwjgl.opengl.GLDebugMessageAMDCallback;
import org.lwjgl.opengl.GLDebugMessageARBCallback;
import org.lwjgl.opengl.GLDebugMessageCallback;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.system.Platform;
import org.lwjgl.util.tinyfd.TinyFileDialogs;

public final class Core {

    private static final Cleaner sCleaner = Cleaner.create();

    private static volatile Thread sMainThread;

    private static volatile Thread sRenderThread;

    private static volatile Thread sUiThread;

    private static volatile Handler sMainHandlerAsync;

    private static volatile Handler sUiHandler;

    private static volatile Handler sUiHandlerAsync;

    private static final ConcurrentLinkedQueue<Runnable> sMainCalls = new ConcurrentLinkedQueue();

    private static final ConcurrentLinkedQueue<Runnable> sRenderCalls = new ConcurrentLinkedQueue();

    private static final Executor sMainThreadExecutor = Core::executeOnMainThread;

    private static final Executor sRenderThreadExecutor = Core::executeOnRenderThread;

    private static final Executor sUiThreadExecutor = Core::executeOnUiThread;

    private static volatile DirectContext sDirectContext;

    private static volatile RecordingContext sUiRecordingContext;

    private Core() {
    }

    @NonNull
    public static Cleanable registerCleanup(@NonNull Object target, @NonNull Runnable action) {
        return sCleaner.register(target, action);
    }

    @MainThread
    public static void initialize() {
        synchronized (Core.class) {
            if (sMainThread == null) {
                GLFWErrorCallback cb = GLFW.glfwSetErrorCallback(null);
                if (cb != null) {
                    GLFW.glfwSetErrorCallback(cb);
                } else {
                    ModernUI.LOGGER.info(ModernUI.MARKER, "Backend Library: LWJGL {}", Version.getVersion());
                    GLFW.glfwSetErrorCallback(new GLFWErrorCallback() {

                        public void invoke(int error, long description) {
                            ModernUI.LOGGER.error(ModernUI.MARKER, "GLFW Error: 0x{} {}", Integer.toHexString(error), MemoryUtil.memUTF8Safe(description));
                        }
                    });
                }
                if (!GLFW.glfwInit()) {
                    ((GLFWErrorCallback) Objects.requireNonNull(GLFW.glfwSetErrorCallback(null))).free();
                    throw new UnsupportedOperationException("Failed to initialize GLFW");
                }
                sMainThread = Thread.currentThread();
            } else {
                assert false;
            }
        }
    }

    @MainThread
    public static void terminate() {
        checkMainThread();
        GLFWErrorCallback cb = GLFW.glfwSetErrorCallback(null);
        if (cb != null) {
            cb.close();
        }
        GLFW.glfwTerminate();
        ModernUI.LOGGER.info(ModernUI.MARKER, "Terminated GLFW");
    }

    public static void checkMainThread() {
        if (Thread.currentThread() != sMainThread) {
            throw new IllegalStateException("Not called from the main thread, current " + Thread.currentThread());
        }
    }

    public static Thread getMainThread() {
        return sMainThread;
    }

    public static boolean isOnMainThread() {
        return Thread.currentThread() == sMainThread;
    }

    @NonNull
    private static ContextOptions initContextOptions(@NonNull ContextOptions options) {
        if (options.mErrorWriter == null) {
            options.mErrorWriter = new PrintWriter(new LogWriter(ModernUI.LOGGER, Level.ERROR, ModernUI.MARKER), true);
        }
        return options;
    }

    @RenderThread
    public static boolean initOpenGL() {
        return initOpenGL(new ContextOptions());
    }

    @RenderThread
    public static boolean initOpenGL(@NonNull ContextOptions options) {
        DirectContext dContext;
        synchronized (Core.class) {
            if (sDirectContext != null) {
                if (sDirectContext.getBackend() != 0) {
                    throw new IllegalStateException();
                }
                return true;
            }
            if (sRenderThread == null) {
                sRenderThread = Thread.currentThread();
            } else if (Thread.currentThread() != sRenderThread) {
                throw new IllegalStateException();
            }
            initContextOptions(options);
            dContext = DirectContext.makeOpenGL(options);
            if (dContext == null) {
                return false;
            }
            sDirectContext = dContext;
        }
        String glVendor = GLCore.glGetString(7936);
        String glRenderer = GLCore.glGetString(7937);
        String glVersion = GLCore.glGetString(7938);
        ModernUI.LOGGER.info(ModernUI.MARKER, "OpenGL vendor: {}", glVendor);
        ModernUI.LOGGER.info(ModernUI.MARKER, "OpenGL renderer: {}", glRenderer);
        ModernUI.LOGGER.info(ModernUI.MARKER, "OpenGL version: {}", glVersion);
        ModernUI.LOGGER.debug(ModernUI.MARKER, "OpenGL caps: {}", dContext.getCaps());
        return true;
    }

    @RenderThread
    public static void glSetupDebugCallback() {
        GLCapabilities caps = GL.getCapabilities();
        if (GLCore.glGetPointer(33348) == 0L) {
            if (caps.OpenGL43 || caps.GL_KHR_debug) {
                ModernUI.LOGGER.debug(ModernUI.MARKER, "Using OpenGL 4.3 for debug logging");
                GLCore.glDebugMessageCallback(Core::glDebugMessage, 0L);
                GLCore.glEnable(37600);
            } else if (caps.GL_ARB_debug_output) {
                ModernUI.LOGGER.debug(ModernUI.MARKER, "Using ARB_debug_output for debug logging");
                GLDebugMessageARBCallback proc = new GLDebugMessageARBCallback() {

                    public void invoke(int source, int type, int id, int severity, int length, long message, long userParam) {
                        ModernUI.LOGGER.info(ModernUI.MARKER, "0x{}[{},{},{}]: {}", Integer.toHexString(id), GLCore.getSourceARB(source), GLCore.getTypeARB(type), GLCore.getSeverityARB(severity), GLDebugMessageARBCallback.getMessage(length, message));
                    }
                };
                ARBDebugOutput.glDebugMessageCallbackARB(proc, 0L);
            } else if (caps.GL_AMD_debug_output) {
                ModernUI.LOGGER.debug(ModernUI.MARKER, "Using AMD_debug_output for debug logging");
                GLDebugMessageAMDCallback proc = new GLDebugMessageAMDCallback() {

                    public void invoke(int id, int category, int severity, int length, long message, long userParam) {
                        ModernUI.LOGGER.info(ModernUI.MARKER, "0x{}[{},{}]: {}", Integer.toHexString(id), GLCore.getCategoryAMD(category), GLCore.getSeverityAMD(severity), GLDebugMessageAMDCallback.getMessage(length, message));
                    }
                };
                AMDDebugOutput.glDebugMessageCallbackAMD(proc, 0L);
            } else {
                ModernUI.LOGGER.debug(ModernUI.MARKER, "No debug callback function was used...");
            }
        } else {
            ModernUI.LOGGER.debug(ModernUI.MARKER, "The debug callback function is already set.");
        }
    }

    public static void glDebugMessage(int source, int type, int id, int severity, int length, long message, long userParam) {
        switch(severity) {
            case 33387:
                ModernUI.LOGGER.debug(ModernUI.MARKER, "({}|{}|0x{}) {}", GLCore.getDebugSource(source), GLCore.getDebugType(type), Integer.toHexString(id), GLDebugMessageCallback.getMessage(length, message));
                break;
            case 37190:
                ModernUI.LOGGER.error(ModernUI.MARKER, "({}|{}|0x{}) {}", GLCore.getDebugSource(source), GLCore.getDebugType(type), Integer.toHexString(id), GLDebugMessageCallback.getMessage(length, message));
                break;
            case 37191:
                ModernUI.LOGGER.warn(ModernUI.MARKER, "({}|{}|0x{}) {}", GLCore.getDebugSource(source), GLCore.getDebugType(type), Integer.toHexString(id), GLDebugMessageCallback.getMessage(length, message));
                break;
            case 37192:
                ModernUI.LOGGER.info(ModernUI.MARKER, "({}|{}|0x{}) {}", GLCore.getDebugSource(source), GLCore.getDebugType(type), Integer.toHexString(id), GLDebugMessageCallback.getMessage(length, message));
        }
    }

    @RenderThread
    public static void glShowCapsErrorDialog() {
        checkRenderThread();
        if (!GLCaps.MISSING_EXTENSIONS.isEmpty()) {
            String glRenderer = GLCore.glGetString(7937);
            String glVersion = GLCore.glGetString(7938);
            new Thread(() -> {
                String solution = "Please make sure you have up-to-date GPU drivers. Also make sure Java applications run with the discrete GPU if you have multiple GPUs.";
                String extensions = String.join("\n", GLCaps.MISSING_EXTENSIONS);
                TinyFileDialogs.tinyfd_messageBox("Failed to launch Modern UI", "GPU: " + glRenderer + ", OpenGL: " + glVersion + ". The following ARB extensions are required:\n" + extensions + "\n" + solution, "ok", "error", true);
            }, "GL-Error-Dialog").start();
        }
    }

    @RenderThread
    public static boolean initVulkan() {
        return initVulkan(new ContextOptions());
    }

    @RenderThread
    public static boolean initVulkan(@NonNull ContextOptions options) {
        synchronized (Core.class) {
            if (sDirectContext != null) {
                if (sDirectContext.getBackend() != 1) {
                    throw new IllegalStateException();
                } else {
                    return true;
                }
            } else {
                if (sRenderThread == null) {
                    sRenderThread = Thread.currentThread();
                } else if (Thread.currentThread() != sRenderThread) {
                    throw new IllegalStateException();
                }
                DirectContext dContext;
                try {
                    VulkanManager vkManager = VulkanManager.getInstance();
                    vkManager.initialize();
                    initContextOptions(options);
                    dContext = vkManager.createContext(options);
                    if (dContext == null) {
                        return false;
                    }
                } catch (Exception var5) {
                    var5.printStackTrace();
                    return false;
                }
                sDirectContext = dContext;
                return true;
            }
        }
    }

    public static void checkRenderThread() {
        if (Thread.currentThread() != sRenderThread) {
            synchronized (Core.class) {
                if (sRenderThread == null) {
                    throw new IllegalStateException("The render thread has not been initialized yet.");
                } else {
                    throw new IllegalStateException("Not called from the render thread " + sRenderThread + ", current " + Thread.currentThread());
                }
            }
        }
    }

    public static Thread getRenderThread() {
        return sRenderThread;
    }

    public static boolean isOnRenderThread() {
        return Thread.currentThread() == sRenderThread;
    }

    @NonNull
    @RenderThread
    public static DirectContext requireDirectContext() {
        checkRenderThread();
        return (DirectContext) Objects.requireNonNull(sDirectContext, "Direct context has not been created yet, or creation failed");
    }

    public static DirectContext peekDirectContext() {
        return sDirectContext;
    }

    @NonNull
    public static Handler getMainHandlerAsync() {
        if (sMainHandlerAsync == null) {
            synchronized (Core.class) {
                if (sMainHandlerAsync == null) {
                    if (Looper.getMainLooper() == null) {
                        throw new IllegalStateException("The main event loop does not exist.");
                    }
                    sMainHandlerAsync = Handler.createAsync(Looper.getMainLooper());
                }
            }
        }
        return sMainHandlerAsync;
    }

    public static void postOnMainThread(@NonNull Runnable r) {
        if (Looper.getMainLooper() == null) {
            sMainCalls.offer(r);
        } else {
            getMainHandlerAsync().post(r);
        }
    }

    public static void executeOnMainThread(@NonNull Runnable r) {
        if (isOnMainThread()) {
            r.run();
        } else {
            postOnMainThread(r);
        }
    }

    @NonNull
    public static Executor getMainThreadExecutor() {
        return sMainThreadExecutor;
    }

    public static void postOnRenderThread(@NonNull Runnable r) {
        sRenderCalls.offer(r);
    }

    public static void executeOnRenderThread(@NonNull Runnable r) {
        if (isOnRenderThread()) {
            r.run();
        } else {
            postOnRenderThread(r);
        }
    }

    @NonNull
    public static Executor getRenderThreadExecutor() {
        return sRenderThreadExecutor;
    }

    public static void flushMainCalls() {
        ConcurrentLinkedQueue<Runnable> queue = sMainCalls;
        Runnable r;
        while ((r = (Runnable) queue.poll()) != null) {
            r.run();
        }
    }

    public static void flushRenderCalls() {
        ConcurrentLinkedQueue<Runnable> queue = sRenderCalls;
        Runnable r;
        while ((r = (Runnable) queue.poll()) != null) {
            r.run();
        }
    }

    @NonNull
    @UiThread
    public static Looper initUiThread() {
        synchronized (Core.class) {
            if (sUiThread == null) {
                sUiThread = Thread.currentThread();
                Looper looper;
                if (sUiThread == sMainThread) {
                    looper = Looper.getMainLooper();
                } else {
                    looper = Looper.prepare();
                }
                sUiHandler = new Handler(looper);
                sUiHandlerAsync = Handler.createAsync(looper);
                if (sDirectContext != null) {
                    if (sUiThread == sRenderThread) {
                        sUiRecordingContext = RefCnt.create(sDirectContext);
                    } else {
                        sUiRecordingContext = RecordingContext.makeRecording(sDirectContext.getContextInfo());
                    }
                    Objects.requireNonNull(sUiRecordingContext);
                } else {
                    ModernUI.LOGGER.warn(ModernUI.MARKER, "UI thread initializing without a direct context");
                }
                return looper;
            } else {
                throw new IllegalStateException();
            }
        }
    }

    public static void checkUiThread() {
        if (Thread.currentThread() != sUiThread) {
            synchronized (Core.class) {
                if (sUiThread == null) {
                    throw new IllegalStateException("The UI thread has not been initialized yet.");
                } else {
                    throw new IllegalStateException("Not called from the UI thread " + sRenderThread + ", current " + Thread.currentThread());
                }
            }
        }
    }

    public static Thread getUiThread() {
        return sUiThread;
    }

    public static boolean isOnUiThread() {
        return Thread.currentThread() == sUiThread;
    }

    @NonNull
    @UiThread
    public static RecordingContext requireUiRecordingContext() {
        checkUiThread();
        return (RecordingContext) Objects.requireNonNull(sUiRecordingContext, "UI recording context has not been created yet, or creation failed");
    }

    public static RecordingContext peekUiRecordingContext() {
        return sUiRecordingContext;
    }

    public static Handler getUiHandler() {
        return sUiHandler;
    }

    public static Handler getUiHandlerAsync() {
        return sUiHandlerAsync;
    }

    public static void postOnUiThread(@NonNull Runnable r) {
        getUiHandlerAsync().post(r);
    }

    public static void executeOnUiThread(@NonNull Runnable r) {
        if (isOnUiThread()) {
            r.run();
        } else {
            postOnUiThread(r);
        }
    }

    @NonNull
    public static Executor getUiThreadExecutor() {
        return sUiThreadExecutor;
    }

    public static long timeNanos() {
        return (long) (GLFW.glfwGetTime() * 1.0E9);
    }

    public static long timeMillis() {
        return (long) (GLFW.glfwGetTime() * 1000.0);
    }

    @NonNull
    public static ByteBuffer readIntoNativeBuffer(@NonNull ReadableByteChannel channel) throws IOException {
        ByteBuffer p = null;
        try {
            if (channel instanceof SeekableByteChannel ch) {
                long rem = ch.size() - ch.position() + 1L;
                p = MemoryUtil.memAlloc((int) Math.min(rem, 2147483647L));
                while (ch.read(p) > 0) {
                }
            } else {
                p = MemoryUtil.memAlloc(4096);
                while (channel.read(p) != -1) {
                    if (!p.hasRemaining()) {
                        long cap = (long) p.capacity();
                        if (cap == 2147483647L) {
                            break;
                        }
                        p = MemoryUtil.memRealloc(p, (int) Math.min(cap + (cap >> 1), 2147483647L));
                    }
                }
            }
            return p;
        } catch (Throwable var5) {
            MemoryUtil.memFree(p);
            throw var5;
        }
    }

    @NonNull
    public static ByteBuffer readIntoNativeBuffer(@NonNull InputStream stream) throws IOException {
        return readIntoNativeBuffer(Channels.newChannel(stream));
    }

    public static boolean openURL(@NonNull URL url) {
        try {
            String s = url.toString();
            String[] cmd = switch(Platform.get()) {
                case WINDOWS ->
                    new String[] { "rundll32", "url.dll,FileProtocolHandler", s };
                case MACOSX ->
                    new String[] { "open", s };
                default ->
                    new String[] { "xdg-open", url.getProtocol().equals("file") ? s.replace("file:", "file://") : s };
            };
            Process proc = Runtime.getRuntime().exec(cmd);
            BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
            try {
                reader.lines().forEach(line -> ModernUI.LOGGER.error(ModernUI.MARKER, line));
            } catch (Throwable var8) {
                try {
                    reader.close();
                } catch (Throwable var7) {
                    var8.addSuppressed(var7);
                }
                throw var8;
            }
            reader.close();
            return true;
        } catch (IOException var9) {
            ModernUI.LOGGER.error(ModernUI.MARKER, "Failed to open URL: {}", url, var9);
            return false;
        }
    }

    public static boolean openURI(@NonNull URI uri) {
        try {
            return openURL(uri.toURL());
        } catch (Exception var2) {
            ModernUI.LOGGER.error(ModernUI.MARKER, "Failed to open URI: {}", uri, var2);
            return false;
        }
    }

    public static boolean openURI(@NonNull String uri) {
        try {
            return openURI(URI.create(uri));
        } catch (Exception var2) {
            ModernUI.LOGGER.error(ModernUI.MARKER, "Failed to open URI: {}", uri, var2);
            return false;
        }
    }
}