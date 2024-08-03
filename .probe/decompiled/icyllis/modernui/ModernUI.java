package icyllis.modernui;

import icyllis.arc3d.core.Matrix4;
import icyllis.arc3d.opengl.GLCore;
import icyllis.modernui.annotation.MainThread;
import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import icyllis.modernui.annotation.RenderThread;
import icyllis.modernui.annotation.UiThread;
import icyllis.modernui.app.Activity;
import icyllis.modernui.core.ActivityWindow;
import icyllis.modernui.core.Core;
import icyllis.modernui.core.Handler;
import icyllis.modernui.core.Looper;
import icyllis.modernui.core.Monitor;
import icyllis.modernui.core.VideoMode;
import icyllis.modernui.core.Window;
import icyllis.modernui.fragment.Fragment;
import icyllis.modernui.fragment.FragmentContainerView;
import icyllis.modernui.fragment.FragmentController;
import icyllis.modernui.fragment.FragmentHostCallback;
import icyllis.modernui.fragment.OnBackPressedDispatcher;
import icyllis.modernui.fragment.OnBackPressedDispatcherOwner;
import icyllis.modernui.graphics.Bitmap;
import icyllis.modernui.graphics.BitmapFactory;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.GLSurface;
import icyllis.modernui.graphics.GLSurfaceCanvas;
import icyllis.modernui.graphics.Image;
import icyllis.modernui.graphics.Rect;
import icyllis.modernui.graphics.drawable.Drawable;
import icyllis.modernui.graphics.drawable.ImageDrawable;
import icyllis.modernui.graphics.text.FontFamily;
import icyllis.modernui.lifecycle.Lifecycle;
import icyllis.modernui.lifecycle.LifecycleOwner;
import icyllis.modernui.lifecycle.LifecycleRegistry;
import icyllis.modernui.lifecycle.ViewModelStore;
import icyllis.modernui.lifecycle.ViewModelStoreOwner;
import icyllis.modernui.resources.Resources;
import icyllis.modernui.text.Typeface;
import icyllis.modernui.util.DisplayMetrics;
import icyllis.modernui.view.KeyEvent;
import icyllis.modernui.view.MotionEvent;
import icyllis.modernui.view.PointerIcon;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewRoot;
import icyllis.modernui.view.WindowGroup;
import icyllis.modernui.view.WindowManager;
import icyllis.modernui.view.menu.ContextMenuBuilder;
import icyllis.modernui.view.menu.MenuHelper;
import icyllis.modernui.widget.TextView;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.jetbrains.annotations.ApiStatus.Experimental;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWMonitorCallback;
import org.lwjgl.glfw.GLFWWindowCloseCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.Configuration;

public class ModernUI extends Activity implements AutoCloseable, LifecycleOwner {

    public static final String ID = "modernui";

    public static final String NAME_CPT = "ModernUI";

    public static final Logger LOGGER = LogManager.getLogger("ModernUI");

    public static final Marker MARKER = MarkerManager.getMarker("Core");

    public static final Properties props = new Properties();

    private static volatile ModernUI sInstance;

    private static final int fragment_container = 16908295;

    private volatile ActivityWindow mWindow;

    private volatile GLSurface mSurface;

    private ModernUI.ViewRootImpl mRoot;

    private WindowGroup mDecor;

    private FragmentContainerView mFragmentContainerView;

    private LifecycleRegistry mLifecycleRegistry;

    private OnBackPressedDispatcher mOnBackPressedDispatcher;

    private ViewModelStore mViewModelStore;

    private FragmentController mFragmentController;

    private volatile Typeface mDefaultTypeface;

    private volatile Thread mRenderThread;

    private volatile Looper mRenderLooper;

    private volatile Handler mRenderHandler;

    private Resources mResources = new Resources();

    private Image mBackgroundImage;

    public ModernUI() {
        synchronized (ModernUI.class) {
            if (sInstance == null) {
                sInstance = this;
            } else {
                throw new RuntimeException("Multiple instances");
            }
        }
    }

    public static ModernUI getInstance() {
        return sInstance;
    }

    @MainThread
    public void run(@NonNull Fragment fragment) {
        Thread.currentThread().setName("Main-Thread");
        Core.initialize();
        LOGGER.debug(MARKER, "Preparing main thread");
        Looper.prepareMainLooper();
        CompletableFuture<Void> loadTypeface = CompletableFuture.runAsync(this::loadDefaultTypeface);
        LOGGER.debug(MARKER, "Initializing window system");
        Monitor monitor = Monitor.getPrimary();
        String name = (String) Configuration.OPENGL_LIBRARY_NAME.get();
        if (name != null) {
            LOGGER.debug(MARKER, "OpenGL library: {}", name);
            Objects.requireNonNull(GL.getFunctionProvider(), "Implicit OpenGL loading is required");
        }
        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(139265, 196609);
        GLFW.glfwWindowHint(139275, 221185);
        GLFW.glfwWindowHint(139272, 204801);
        GLFW.glfwWindowHint(139266, 3);
        GLFW.glfwWindowHint(139267, 3);
        GLFW.glfwWindowHintString(147457, "ModernUI");
        GLFW.glfwWindowHintString(147458, "ModernUI");
        GLFW.glfwWindowHint(131076, 0);
        if (monitor == null) {
            LOGGER.info(MARKER, "No monitor connected");
            this.mWindow = ActivityWindow.createMainWindow("Modern UI", 1280, 720);
        } else {
            VideoMode mode = monitor.getCurrentMode();
            this.mWindow = ActivityWindow.createMainWindow("Modern UI", (int) ((double) mode.getWidth() * 0.75), (int) ((double) mode.getHeight() * 0.75));
        }
        CountDownLatch latch = new CountDownLatch(1);
        LOGGER.debug(MARKER, "Preparing render thread");
        this.mRenderThread = new Thread(() -> this.runRender(latch), "Render-Thread");
        this.mRenderThread.start();
        CompletableFuture.supplyAsync(() -> {
            try {
                Bitmap i16 = BitmapFactory.decodeStream(this.getResourceStream("modernui", "AppLogo16x.png"));
                Bitmap i32 = BitmapFactory.decodeStream(this.getResourceStream("modernui", "AppLogo32x.png"));
                Bitmap i48 = BitmapFactory.decodeStream(this.getResourceStream("modernui", "AppLogo48x.png"));
                return new Bitmap[] { i16, i32, i48 };
            } catch (IOException var4x) {
                LOGGER.info(MARKER, "Failed to load window icons", var4x);
                return null;
            }
        }).thenAcceptAsync(icons -> this.mWindow.setIcon(icons), Core.getMainThreadExecutor());
        if (monitor != null) {
            this.mWindow.center(monitor);
            int[] physw = new int[] { 0 };
            int[] physh = new int[] { 0 };
            GLFW.glfwGetMonitorPhysicalSize(monitor.getHandle(), physw, physh);
            float[] xscale = new float[] { 0.0F };
            float[] yscale = new float[] { 0.0F };
            GLFW.glfwGetMonitorContentScale(monitor.getHandle(), xscale, yscale);
            DisplayMetrics metrics = new DisplayMetrics();
            metrics.setToDefaults();
            metrics.widthPixels = this.mWindow.getWidth();
            metrics.heightPixels = this.mWindow.getHeight();
            VideoMode mode = monitor.getCurrentMode();
            metrics.xdpi = 25.4F * (float) mode.getWidth() / (float) physw[0];
            metrics.ydpi = 25.4F * (float) mode.getHeight() / (float) physh[0];
            LOGGER.info(MARKER, "Primary monitor physical size: {}x{} mm, xScale: {}, yScale: {}", physw[0], physh[0], xscale[0], yscale[0]);
            int density = Math.round(metrics.xdpi * xscale[0] / 12.0F) * 12;
            metrics.density = (float) density * 0.013888889F;
            metrics.densityDpi = density;
            metrics.scaledDensity = metrics.density;
            LOGGER.info(MARKER, "Display metrics: {}", metrics);
            this.mResources.updateMetrics(metrics);
        }
        GLFW.glfwSetWindowCloseCallback(this.mWindow.getHandle(), new GLFWWindowCloseCallback() {

            public void invoke(long window) {
                ModernUI.LOGGER.debug(ModernUI.MARKER, "Window closed from callback");
                ModernUI.this.stop();
            }
        });
        try {
            latch.await();
        } catch (InterruptedException var13) {
            throw new RuntimeException(var13);
        }
        LOGGER.debug(MARKER, "Initializing UI system");
        Core.initUiThread();
        this.mRoot = new ModernUI.ViewRootImpl();
        this.mRoot.loadSystemProperties(() -> Boolean.getBoolean("icyllis.modernui.display.debug.layout"));
        this.mDecor = new WindowGroup(this);
        this.mDecor.setWillNotDraw(true);
        this.mDecor.setId(16908290);
        CompletableFuture.supplyAsync(() -> {
            Path p = Path.of("assets/modernui/raw/eromanga.png").toAbsolutePath();
            try {
                FileChannel channel = FileChannel.open(p, StandardOpenOption.READ);
                Bitmap var2x;
                try {
                    var2x = BitmapFactory.decodeChannel(channel);
                } catch (Throwable var5x) {
                    if (channel != null) {
                        try {
                            channel.close();
                        } catch (Throwable var4x) {
                            var5x.addSuppressed(var4x);
                        }
                    }
                    throw var5x;
                }
                if (channel != null) {
                    channel.close();
                }
                return var2x;
            } catch (IOException var6x) {
                LOGGER.info(MARKER, "Failed to load background image", var6x);
                return null;
            }
        }).thenAcceptAsync(bitmap -> {
            Bitmap var2x = bitmap;
            try {
                Image image = Image.createTextureFromBitmap(bitmap);
                if (image != null) {
                    Drawable drawable = new ImageDrawable(image);
                    drawable.setTint(-8355712);
                    this.mDecor.setBackground(drawable);
                    synchronized (Core.class) {
                        this.mBackgroundImage = image;
                    }
                }
            } catch (Throwable var9x) {
                if (bitmap != null) {
                    try {
                        var2x.close();
                    } catch (Throwable var7x) {
                        var9x.addSuppressed(var7x);
                    }
                }
                throw var9x;
            }
            if (bitmap != null) {
                bitmap.close();
            }
        }, Core.getUiThreadExecutor());
        this.mFragmentContainerView = new FragmentContainerView(this);
        this.mFragmentContainerView.setLayoutParams(new WindowManager.LayoutParams());
        this.mFragmentContainerView.setWillNotDraw(true);
        this.mFragmentContainerView.setId(16908295);
        this.mDecor.addView(this.mFragmentContainerView);
        this.mDecor.setLayoutDirection(3);
        this.mDecor.setIsRootNamespace(true);
        this.mRoot.setView(this.mDecor);
        LOGGER.debug(MARKER, "Installing view protocol");
        this.mWindow.install(this.mRoot);
        this.mLifecycleRegistry = new LifecycleRegistry(this);
        this.mOnBackPressedDispatcher = new OnBackPressedDispatcher(() -> {
            this.mWindow.setShouldClose(true);
            this.stop();
        });
        this.mViewModelStore = new ViewModelStore();
        this.mFragmentController = FragmentController.createController(new ModernUI.HostCallbacks());
        this.mFragmentController.attachHost(null);
        this.mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
        this.mFragmentController.dispatchCreate();
        this.mFragmentController.dispatchActivityCreated();
        this.mFragmentController.execPendingActions();
        this.mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START);
        this.mFragmentController.dispatchStart();
        this.mFragmentController.getFragmentManager().beginTransaction().add(16908295, fragment, "main").setTransition(4097).addToBackStack("main").commit();
        this.mWindow.show();
        loadTypeface.join();
        LOGGER.info(MARKER, "Looping main thread");
        Looper.loop();
        Core.requireUiRecordingContext().unref();
        LOGGER.info(MARKER, "Quited main thread");
    }

    @RenderThread
    private void runRender(CountDownLatch latch) {
        Window window = this.mWindow;
        window.makeCurrent();
        try {
            if (!Core.initOpenGL()) {
                Core.glShowCapsErrorDialog();
                throw new IllegalStateException("Failed to initialize OpenGL");
            }
            this.mRenderLooper = Looper.prepare();
            this.mRenderHandler = new Handler(this.mRenderLooper);
            Core.glSetupDebugCallback();
            GLSurfaceCanvas.initialize();
        } finally {
            latch.countDown();
        }
        GLCore.glDisable(2884);
        GLCore.glEnable(3042);
        GLCore.glBlendFunc(1, 771);
        GLCore.glDisable(2929);
        GLCore.glEnable(2960);
        GLCore.glDisable(32925);
        this.mSurface = new GLSurface();
        window.swapInterval(1);
        LOGGER.info(MARKER, "Looping render thread");
        Looper.loop();
        this.mSurface.close();
        synchronized (Core.class) {
            if (this.mBackgroundImage != null) {
                this.mBackgroundImage.close();
                this.mBackgroundImage = null;
            }
        }
        GLSurfaceCanvas.getInstance().destroy();
        Core.requireDirectContext().unref();
        LOGGER.info(MARKER, "Quited render thread");
    }

    private void loadDefaultTypeface() {
        Set<FontFamily> set = new LinkedHashSet();
        try {
            InputStream stream = new FileInputStream("E:/Free Fonts/biliw.otf");
            try {
                set.add(FontFamily.createFamily(stream, true));
            } catch (Throwable var8) {
                try {
                    stream.close();
                } catch (Throwable var7) {
                    var8.addSuppressed(var7);
                }
                throw var8;
            }
            stream.close();
        } catch (Exception var9) {
        }
        for (String name : new String[] { "Microsoft YaHei UI", "Calibri", "STHeiti", "Segoe UI", "SimHei" }) {
            FontFamily family = FontFamily.getSystemFontWithAlias(name);
            if (family != null) {
                set.add(family);
            }
        }
        this.mDefaultTypeface = Typeface.createTypeface((FontFamily[]) set.toArray(new FontFamily[0]));
    }

    private void stop() {
        this.mFragmentController.dispatchStop();
        this.mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP);
        this.mFragmentController.dispatchDestroy();
        this.mLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY);
        Looper.getMainLooper().quitSafely();
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return this.mLifecycleRegistry;
    }

    @Override
    public Resources getResources() {
        return this.mResources;
    }

    protected Locale onGetSelectedLocale() {
        return Locale.getDefault();
    }

    @NonNull
    public static Locale getSelectedLocale() {
        return sInstance == null ? Locale.getDefault() : sInstance.onGetSelectedLocale();
    }

    @NonNull
    protected Typeface onGetSelectedTypeface() {
        return (Typeface) Objects.requireNonNullElse(this.mDefaultTypeface, Typeface.SANS_SERIF);
    }

    @NonNull
    public static Typeface getSelectedTypeface() {
        return sInstance == null ? Typeface.SANS_SERIF : sInstance.onGetSelectedTypeface();
    }

    @Experimental
    public boolean hasRtlSupport() {
        return true;
    }

    @Experimental
    @NonNull
    public InputStream getResourceStream(@NonNull String namespace, @NonNull String path) throws IOException {
        InputStream stream = ModernUI.class.getResourceAsStream("/assets/" + namespace + "/" + path);
        if (stream == null) {
            throw new FileNotFoundException();
        } else {
            return stream;
        }
    }

    @Experimental
    @NonNull
    public ReadableByteChannel getResourceChannel(@NonNull String namespace, @NonNull String path) throws IOException {
        return Channels.newChannel(this.getResourceStream(namespace, path));
    }

    @Internal
    @Override
    public WindowManager getWindowManager() {
        return this.mDecor;
    }

    public void close() {
        try {
            this.mRenderLooper.quit();
            if (this.mRenderThread != null && this.mRenderThread.isAlive()) {
                try {
                    this.mRenderThread.join(1000L);
                } catch (InterruptedException var5) {
                    Thread.currentThread().interrupt();
                }
            }
            if (this.mWindow != null) {
                this.mWindow.close();
                LOGGER.debug(MARKER, "Closed main window");
            }
            GLFWMonitorCallback cb = GLFW.glfwSetMonitorCallback(null);
            if (cb != null) {
                cb.free();
            }
        } finally {
            Core.terminate();
        }
    }

    static {
        if (Runtime.version().feature() < 17) {
            throw new RuntimeException("JRE 17 or above is required");
        }
    }

    @UiThread
    class HostCallbacks extends FragmentHostCallback<Object> implements ViewModelStoreOwner, OnBackPressedDispatcherOwner {

        HostCallbacks() {
            super(ModernUI.this, new Handler(Looper.myLooper()));
            assert Core.isOnUiThread();
        }

        @Nullable
        @Override
        public Object onGetHost() {
            return null;
        }

        @Nullable
        @Override
        public View onFindViewById(int id) {
            return ModernUI.this.mDecor.findViewById(id);
        }

        @NonNull
        @Override
        public ViewModelStore getViewModelStore() {
            return ModernUI.this.mViewModelStore;
        }

        @NonNull
        @Override
        public OnBackPressedDispatcher getOnBackPressedDispatcher() {
            return ModernUI.this.mOnBackPressedDispatcher;
        }

        @NonNull
        @Override
        public Lifecycle getLifecycle() {
            return ModernUI.this.mLifecycleRegistry;
        }
    }

    @UiThread
    class ViewRootImpl extends ViewRoot {

        private final Rect mGlobalRect = new Rect();

        ContextMenuBuilder mContextMenu;

        MenuHelper mContextMenuHelper;

        @Override
        protected boolean dispatchTouchEvent(MotionEvent event) {
            if (event.getAction() == 0) {
                View v = this.mView.findFocus();
                if (v instanceof TextView tv && tv.getMovementMethod() != null) {
                    v.getGlobalVisibleRect(this.mGlobalRect);
                    if (!this.mGlobalRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                        v.clearFocus();
                    }
                }
            }
            return super.dispatchTouchEvent(event);
        }

        @Override
        protected void onKeyEvent(KeyEvent event) {
            if (event.getAction() == 0 && event.getKeyCode() == 256) {
                if (this.mView.findFocus() instanceof TextView tv && tv.getMovementMethod() != null) {
                    this.mView.requestFocus();
                    return;
                }
                ModernUI.this.mOnBackPressedDispatcher.onBackPressed();
            }
        }

        @NonNull
        @Override
        protected Canvas beginDrawLocked(int width, int height) {
            GLSurfaceCanvas canvas = GLSurfaceCanvas.getInstance();
            canvas.reset(width, height);
            return canvas;
        }

        @Override
        protected void endDrawLocked(@NonNull Canvas canvas) {
            ModernUI.this.mRenderHandler.post(this::render);
            try {
                this.mRenderLock.wait();
            } catch (InterruptedException var3) {
                Thread.currentThread().interrupt();
            }
        }

        @RenderThread
        private void render() {
            GLSurfaceCanvas canvas = GLSurfaceCanvas.getInstance();
            Window window = ModernUI.this.mWindow;
            GLSurface framebuffer = ModernUI.this.mSurface;
            int width = window.getWidth();
            int height = window.getHeight();
            GLCore.glViewport(0, 0, width, height);
            synchronized (this.mRenderLock) {
                Matrix4 projection = new Matrix4();
                canvas.setProjection(projection.setOrthographic((float) width, (float) height, 0.0F, 5999.0F, true));
                canvas.executeRenderPass(framebuffer);
                this.mRenderLock.notifyAll();
            }
            if (framebuffer.getBackingWidth() > 0) {
                framebuffer.bindRead();
                GLCore.glBindFramebuffer(36009, 0);
                GLCore.glBlitFramebuffer(0, 0, width, height, 0, 0, width, height, 16384, 9728);
                window.swapBuffers();
            }
        }

        @Override
        public void playSoundEffect(int effectId) {
        }

        @Override
        public boolean performHapticFeedback(int effectId, boolean always) {
            return false;
        }

        @Override
        protected void applyPointerIcon(int pointerType) {
            Core.executeOnMainThread(() -> GLFW.glfwSetCursor(ModernUI.this.mWindow.getHandle(), PointerIcon.getSystemIcon(pointerType).getHandle()));
        }

        @Override
        public boolean showContextMenuForChild(View originalView, float x, float y) {
            if (this.mContextMenuHelper != null) {
                this.mContextMenuHelper.dismiss();
                this.mContextMenuHelper = null;
            }
            if (this.mContextMenu == null) {
                this.mContextMenu = new ContextMenuBuilder(ModernUI.this);
            } else {
                this.mContextMenu.clearAll();
            }
            boolean isPopup = !Float.isNaN(x) && !Float.isNaN(y);
            MenuHelper helper;
            if (isPopup) {
                helper = this.mContextMenu.showPopup(ModernUI.this, originalView, x, y);
            } else {
                helper = this.mContextMenu.showPopup(ModernUI.this, originalView, 0.0F, 0.0F);
            }
            if (helper != null) {
            }
            this.mContextMenuHelper = helper;
            return helper != null;
        }
    }
}