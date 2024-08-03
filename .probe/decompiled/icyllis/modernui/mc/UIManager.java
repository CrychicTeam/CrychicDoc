package icyllis.modernui.mc;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.PoseStack;
import icyllis.arc3d.core.MathUtil;
import icyllis.arc3d.core.Matrix4;
import icyllis.arc3d.engine.ResourceCache;
import icyllis.arc3d.opengl.GLCore;
import icyllis.arc3d.opengl.GLDevice;
import icyllis.arc3d.opengl.GLTexture;
import icyllis.modernui.ModernUI;
import icyllis.modernui.animation.LayoutTransition;
import icyllis.modernui.annotation.MainThread;
import icyllis.modernui.annotation.RenderThread;
import icyllis.modernui.annotation.UiThread;
import icyllis.modernui.audio.AudioManager;
import icyllis.modernui.core.Core;
import icyllis.modernui.core.Handler;
import icyllis.modernui.core.Looper;
import icyllis.modernui.core.Message;
import icyllis.modernui.fragment.Fragment;
import icyllis.modernui.fragment.FragmentContainerView;
import icyllis.modernui.fragment.FragmentController;
import icyllis.modernui.fragment.FragmentHostCallback;
import icyllis.modernui.fragment.OnBackPressedDispatcher;
import icyllis.modernui.fragment.OnBackPressedDispatcherOwner;
import icyllis.modernui.graphics.Bitmap;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.GLSurface;
import icyllis.modernui.graphics.GLSurfaceCanvas;
import icyllis.modernui.graphics.ImageStore;
import icyllis.modernui.graphics.Rect;
import icyllis.modernui.graphics.font.GlyphManager;
import icyllis.modernui.graphics.text.LayoutCache;
import icyllis.modernui.lifecycle.Lifecycle;
import icyllis.modernui.lifecycle.LifecycleOwner;
import icyllis.modernui.lifecycle.LifecycleRegistry;
import icyllis.modernui.lifecycle.ViewModelStore;
import icyllis.modernui.lifecycle.ViewModelStoreOwner;
import icyllis.modernui.text.Editable;
import icyllis.modernui.text.Selection;
import icyllis.modernui.text.TextUtils;
import icyllis.modernui.view.KeyEvent;
import icyllis.modernui.view.MotionEvent;
import icyllis.modernui.view.PointerIcon;
import icyllis.modernui.view.View;
import icyllis.modernui.view.ViewRoot;
import icyllis.modernui.view.WindowGroup;
import icyllis.modernui.view.WindowManager;
import icyllis.modernui.view.menu.ContextMenuBuilder;
import icyllis.modernui.view.menu.MenuHelper;
import icyllis.modernui.widget.EditText;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.ByteOrder;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.CrashReport;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.io.output.StringBuilderWriter;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.system.MemoryUtil;

@Internal
public abstract class UIManager implements LifecycleOwner {

    protected static final Marker MARKER = MarkerManager.getMarker("UIManager");

    public static volatile boolean sDingEnabled;

    public static volatile boolean sZoomEnabled;

    protected static volatile UIManager sInstance;

    protected static final int fragment_container = 16908295;

    protected final Minecraft minecraft = Minecraft.getInstance();

    protected final Window mWindow = this.minecraft.getWindow();

    private final PoseStack mEmptyPoseStack = new PoseStack();

    private final Thread mUiThread;

    private volatile Looper mLooper;

    private volatile boolean mRunning;

    protected volatile UIManager.ViewRootImpl mRoot;

    protected WindowGroup mDecor;

    private FragmentContainerView mFragmentContainerView;

    private volatile boolean mDebugLayout = false;

    protected long mElapsedTimeMillis;

    protected long mFrameTimeNanos;

    private GLSurface mSurface;

    protected GLSurfaceCanvas mCanvas;

    protected GLDevice mDevice;

    private final Matrix4 mProjectionMatrix = new Matrix4();

    protected boolean mNoRender = false;

    protected boolean mClearNextMainTarget = false;

    protected boolean mAlwaysClearMainTarget = false;

    private long mLastPurgeNanos;

    protected final TooltipRenderer mTooltipRenderer = new TooltipRenderer();

    @Nullable
    protected volatile MuiScreen mScreen;

    protected boolean mFirstScreenOpened = false;

    protected boolean mZoomMode = false;

    protected boolean mZoomSmoothCamera;

    protected LifecycleRegistry mFragmentLifecycleRegistry;

    private final OnBackPressedDispatcher mOnBackPressedDispatcher = new OnBackPressedDispatcher(() -> this.minecraft.m_6937_(this::onBackPressed));

    private ViewModelStore mViewModelStore;

    protected volatile FragmentController mFragmentController;

    protected int mButtonState;

    private final StringBuilder mCharInputBuffer = new StringBuilder();

    private final Runnable mCommitCharInput = this::commitCharInput;

    private final Runnable mResizeRunnable = () -> this.mRoot.setFrame(this.mWindow.getWidth(), this.mWindow.getHeight());

    protected UIManager() {
        MuiModApi.addOnScrollListener(this::onScroll);
        MuiModApi.addOnScreenChangeListener(this::onScreenChange);
        MuiModApi.addOnWindowResizeListener((width, height, guiScale, oldGuiScale) -> this.resize());
        this.mUiThread = new Thread(this::run, "UI thread");
        this.mUiThread.start();
        AudioManager.getInstance().initialize(true);
        this.mRunning = true;
    }

    @RenderThread
    public static void initializeRenderer() {
        Core.checkRenderThread();
        if (ModernUIMod.isDeveloperMode()) {
            Core.glSetupDebugCallback();
        }
        Objects.requireNonNull(sInstance);
        sInstance.mCanvas = GLSurfaceCanvas.initialize();
        sInstance.mDevice = (GLDevice) Core.requireDirectContext().getDevice();
        sInstance.mDevice.getContext().getResourceCache().setCacheLimit(268435456L);
        sInstance.mSurface = new GLSurface();
        BufferUploader.invalidate();
        ModernUI.LOGGER.info(MARKER, "UI renderer initialized");
    }

    @Nonnull
    public static UIManager getInstance() {
        if (sInstance == null) {
            throw new IllegalStateException("UI manager was never initialized. Please check whether mod loader threw an exception before.");
        } else {
            return sInstance;
        }
    }

    @UiThread
    private void run() {
        try {
            this.init();
        } catch (Throwable var2) {
            ModernUI.LOGGER.fatal(MARKER, "UI manager failed to initialize");
            return;
        }
        while (this.mRunning) {
            try {
                Looper.loop();
                break;
            } catch (Throwable var3) {
                ModernUI.LOGGER.error(MARKER, "An error occurred on UI thread", var3);
                if (!this.mRunning || !ModernUIMod.isDeveloperMode()) {
                    this.minecraft.m_6937_(this::dump);
                    this.minecraft.m_6937_(() -> Minecraft.crash(CrashReport.forThrowable(var3, "Exception on UI thread")));
                    break;
                }
            }
        }
        Core.requireUiRecordingContext().unref();
        ModernUI.LOGGER.debug(MARKER, "Quited UI thread");
    }

    @MainThread
    protected abstract void open(@Nonnull Fragment var1);

    @MainThread
    void onBackPressed() {
        MuiScreen screen = this.mScreen;
        if (screen != null) {
            if (screen.getCallback() == null || screen.getCallback().shouldClose()) {
                if (screen.isMenuScreen()) {
                    if (this.minecraft.player != null) {
                        this.minecraft.player.closeContainer();
                    }
                } else {
                    this.minecraft.setScreen(screen.getPreviousScreen());
                }
            }
        }
    }

    static long getElapsedTime() {
        return sInstance == null ? Core.timeMillis() : sInstance.mElapsedTimeMillis;
    }

    static long getFrameTimeNanos() {
        return sInstance == null ? Core.timeNanos() : sInstance.mFrameTimeNanos;
    }

    public WindowGroup getDecorView() {
        return this.mDecor;
    }

    public FragmentController getFragmentController() {
        return this.mFragmentController;
    }

    public void setShowingLayoutBounds(boolean debugLayout) {
        this.mDebugLayout = debugLayout;
        this.mRoot.loadSystemProperties(() -> this.mDebugLayout);
    }

    public boolean isShowingLayoutBounds() {
        return this.mDebugLayout;
    }

    public OnBackPressedDispatcher getOnBackPressedDispatcher() {
        return this.mOnBackPressedDispatcher;
    }

    @Nonnull
    @Override
    public Lifecycle getLifecycle() {
        return this.mFragmentLifecycleRegistry;
    }

    @MainThread
    public void initScreen(@Nonnull MuiScreen screen) {
        if (this.mScreen != screen) {
            if (this.mScreen != null) {
                ModernUI.LOGGER.warn(MARKER, "You cannot set multiple screens.");
                this.removed();
            }
            this.mRoot.mHandler.post(this::suppressLayoutTransition);
            this.mFragmentController.getFragmentManager().beginTransaction().add(16908295, screen.getFragment(), "main").setTransition(4097).commit();
            this.mRoot.mHandler.post(this::restoreLayoutTransition);
        }
        this.mScreen = screen;
        this.resize();
    }

    @UiThread
    void suppressLayoutTransition() {
        LayoutTransition transition = this.mDecor.getLayoutTransition();
        transition.disableTransitionType(2);
        transition.disableTransitionType(3);
    }

    @UiThread
    void restoreLayoutTransition() {
        LayoutTransition transition = this.mDecor.getLayoutTransition();
        transition.enableTransitionType(2);
        transition.enableTransitionType(3);
    }

    protected void onScreenChange(@Nullable Screen oldScreen, @Nullable Screen newScreen) {
        BlurHandler.INSTANCE.blur(newScreen);
        if (newScreen == null) {
            this.removed();
        }
    }

    @UiThread
    private void init() {
        long startTime = System.nanoTime();
        this.mLooper = Core.initUiThread();
        this.mRoot = new UIManager.ViewRootImpl();
        this.mDecor = new WindowGroup(ModernUI.getInstance());
        this.mDecor.setWillNotDraw(true);
        this.mDecor.setId(16908290);
        this.updateLayoutDir(false);
        this.mFragmentContainerView = new FragmentContainerView(ModernUI.getInstance());
        this.mFragmentContainerView.setLayoutParams(new WindowManager.LayoutParams());
        this.mFragmentContainerView.setWillNotDraw(true);
        this.mFragmentContainerView.setId(16908295);
        this.mDecor.addView(this.mFragmentContainerView);
        this.mDecor.setLayoutTransition(new LayoutTransition());
        this.mRoot.setView(this.mDecor);
        this.resize();
        this.mDecor.getViewTreeObserver().addOnScrollChangedListener(() -> this.onHoverMove(false));
        this.mFragmentLifecycleRegistry = new LifecycleRegistry(this);
        this.mViewModelStore = new ViewModelStore();
        this.mFragmentController = FragmentController.createController(new UIManager.HostCallbacks());
        this.mFragmentController.attachHost(null);
        this.mFragmentLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
        this.mFragmentController.dispatchCreate();
        this.mFragmentController.dispatchActivityCreated();
        this.mFragmentController.execPendingActions();
        this.mFragmentLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START);
        this.mFragmentController.dispatchStart();
        ModernUI.LOGGER.info(MARKER, "UI thread initialized in {}ms", (System.nanoTime() - startTime) / 1000000L);
    }

    @UiThread
    private void finish() {
        ModernUI.LOGGER.debug(MARKER, "Quiting UI thread");
        this.mFragmentController.dispatchStop();
        this.mFragmentLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP);
        this.mFragmentController.dispatchDestroy();
        this.mFragmentLifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY);
        this.mRoot.mHandler.postDelayed(this.mLooper::quitSafely, 60L);
    }

    @MainThread
    public void onHoverMove(boolean natural) {
        long now = Core.timeNanos();
        float x = (float) (this.minecraft.mouseHandler.xpos() * (double) this.mWindow.getWidth() / (double) this.mWindow.getScreenWidth());
        float y = (float) (this.minecraft.mouseHandler.ypos() * (double) this.mWindow.getHeight() / (double) this.mWindow.getScreenHeight());
        MotionEvent event = MotionEvent.obtain(now, 7, x, y, 0);
        this.mRoot.enqueueInputEvent(event);
        if (natural && this.mButtonState > 0) {
            event = MotionEvent.obtain(now, 2, 0, x, y, 0, this.mButtonState, 0);
            this.mRoot.enqueueInputEvent(event);
        }
    }

    private void onScroll(double scrollX, double scrollY) {
        if (this.mScreen != null) {
            long now = Core.timeNanos();
            Window window = this.mWindow;
            MouseHandler mouseHandler = this.minecraft.mouseHandler;
            float x = (float) (mouseHandler.xpos() * (double) window.getWidth() / (double) window.getScreenWidth());
            float y = (float) (mouseHandler.ypos() * (double) window.getHeight() / (double) window.getScreenHeight());
            int mods = 0;
            if (Screen.hasControlDown()) {
                mods |= KeyEvent.META_CTRL_ON;
            }
            if (Screen.hasShiftDown()) {
                mods |= 1;
            }
            MotionEvent event = MotionEvent.obtain(now, 8, x, y, mods);
            event.setAxisValue(10, (float) scrollX);
            event.setAxisValue(9, (float) scrollY);
            this.mRoot.enqueueInputEvent(event);
        }
    }

    public void onPostMouseInput(int button, int action, int mods) {
        if (this.minecraft.getOverlay() == null && this.mScreen != null) {
            long now = Core.timeNanos();
            float x = (float) (this.minecraft.mouseHandler.xpos() * (double) this.mWindow.getWidth() / (double) this.mWindow.getScreenWidth());
            float y = (float) (this.minecraft.mouseHandler.ypos() * (double) this.mWindow.getHeight() / (double) this.mWindow.getScreenHeight());
            int buttonState = 0;
            for (int i = 0; i < 5; i++) {
                if (GLFW.glfwGetMouseButton(this.mWindow.getWindow(), i) == 1) {
                    buttonState |= 1 << i;
                }
            }
            this.mButtonState = buttonState;
            int hoverAction = action == 1 ? 11 : 12;
            int touchAction = action == 1 ? 0 : 1;
            int actionButton = 1 << button;
            MotionEvent ev = MotionEvent.obtain(now, hoverAction, actionButton, x, y, mods, buttonState, 0);
            this.mRoot.enqueueInputEvent(ev);
            if (touchAction == 0 && (buttonState ^ actionButton) == 0 || touchAction == 1 && buttonState == 0) {
                ev = MotionEvent.obtain(now, touchAction, actionButton, x, y, mods, buttonState, 0);
                this.mRoot.enqueueInputEvent(ev);
            }
        }
    }

    public void onPostKeyInput(int key, int scanCode, int action, int mods) {
    }

    public void takeScreenshot() {
        this.mSurface.bindRead();
        int width = this.mSurface.getBackingWidth();
        int height = this.mSurface.getBackingHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Format.RGBA_8888);
        GLCore.glPixelStorei(3330, 0);
        GLCore.glPixelStorei(3331, 0);
        GLCore.glPixelStorei(3332, 0);
        GLCore.glPixelStorei(3333, 1);
        GLCore.glReadPixels(0, 0, width, height, 6408, 5121, bitmap.getAddress());
        Util.ioPool().execute(() -> {
            try {
                Bitmap e = bitmap;
                try {
                    Bitmap.flipVertically(bitmap);
                    unpremulAlpha(bitmap);
                    bitmap.saveDialog(Bitmap.SaveFormat.PNG, 0, null);
                } catch (Throwable var5) {
                    if (bitmap != null) {
                        try {
                            e.close();
                        } catch (Throwable var4) {
                            var5.addSuppressed(var4);
                        }
                    }
                    throw var5;
                }
                if (bitmap != null) {
                    bitmap.close();
                }
            } catch (IOException var6) {
                ModernUI.LOGGER.warn(MARKER, "Failed to save UI screenshot", var6);
            }
        });
    }

    static void unpremulAlpha(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int rowStride = bitmap.getRowStride();
        long addr = bitmap.getAddress();
        boolean big = ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                long base = addr + (long) (j << 2);
                int col = MemoryUtil.memGetInt(base);
                if (big) {
                    col = Integer.reverseBytes(col);
                }
                int alpha = col >>> 24;
                if (alpha != 0) {
                    float a = (float) alpha / 255.0F;
                    int r = MathUtil.clamp((int) ((float) (col & 0xFF) / a + 0.5F), 0, 255);
                    int g = MathUtil.clamp((int) ((float) (col >> 8 & 0xFF) / a + 0.5F), 0, 255);
                    int b = MathUtil.clamp((int) ((float) (col >> 16 & 0xFF) / a + 0.5F), 0, 255);
                    col = r | g << 8 | b << 16 | col & 0xFF000000;
                    if (big) {
                        col = Integer.reverseBytes(col);
                    }
                    MemoryUtil.memPutInt(base, col);
                }
            }
            addr += (long) rowStride;
        }
    }

    protected void changeRadialBlur() {
        if (this.minecraft.gameRenderer.currentEffect() == null) {
            ModernUI.LOGGER.info(MARKER, "Load post-processing effect");
            ResourceLocation effect;
            if (InputConstants.isKeyDown(this.mWindow.getWindow(), 344)) {
                effect = new ResourceLocation("shaders/post/grayscale.json");
            } else {
                effect = new ResourceLocation("shaders/post/radial_blur.json");
            }
            MuiModApi.get().loadEffect(this.minecraft.gameRenderer, effect);
        } else {
            ModernUI.LOGGER.info(MARKER, "Stop post-processing effect");
            this.minecraft.gameRenderer.shutdownEffect();
        }
    }

    public void dump() {
        StringBuilder builder = new StringBuilder();
        PrintWriter w = new PrintWriter(new StringBuilderWriter(builder));
        try {
            this.dump(w, true);
        } catch (Throwable var6) {
            try {
                w.close();
            } catch (Throwable var5) {
                var6.addSuppressed(var5);
            }
            throw var6;
        }
        w.close();
        String var7 = builder.toString();
        if (this.minecraft.level != null) {
            this.minecraft.gui.getChat().addMessage(Component.literal(var7).withStyle(ChatFormatting.GRAY));
        }
        ModernUI.LOGGER.info(MARKER, var7);
    }

    public void dump(@Nonnull PrintWriter pw, boolean fragments) {
        pw.println(">>> Modern UI dump data <<<");
        pw.print("Container Menu: ");
        LocalPlayer player = this.minecraft.player;
        AbstractContainerMenu menu = null;
        if (player != null) {
            menu = player.f_36096_;
        }
        if (menu != null) {
            pw.println(menu.getClass().getSimpleName());
            try {
                ResourceLocation name = BuiltInRegistries.MENU.getKey(menu.getType());
                pw.print("  Registry Name: ");
                pw.println(name);
            } catch (Exception var8) {
            }
        } else {
            pw.println(null);
        }
        Screen screen = this.minecraft.screen;
        if (screen != null) {
            pw.print("Screen: ");
            pw.println(screen.getClass());
        }
        if (fragments && this.mFragmentController != null) {
            this.mFragmentController.getFragmentManager().dump("", null, pw);
        }
        int coreN = LayoutCache.getSize();
        int coreMem = LayoutCache.getMemoryUsage();
        pw.printf("LayoutCore: Count=%d, Size=%s (%d bytes)\n", coreN, TextUtils.binaryCompact((long) coreMem), coreMem);
        GlyphManager.getInstance().dumpInfo(pw);
        MuiModApi.dispatchOnDebugDump(pw);
    }

    @MainThread
    public boolean onCharTyped(char ch) {
        if (ch != 0 && ch != 127) {
            this.mCharInputBuffer.append(ch);
            Core.postOnMainThread(this.mCommitCharInput);
            return true;
        } else {
            return false;
        }
    }

    private void commitCharInput() {
        if (!this.mCharInputBuffer.isEmpty()) {
            String input = this.mCharInputBuffer.toString();
            this.mCharInputBuffer.setLength(0);
            Message msg = Message.obtain(this.mRoot.mHandler, () -> {
                if (this.mDecor.findFocus() instanceof EditText text) {
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

    @RenderThread
    public void render() {
        if (this.mCanvas != null && !this.mNoRender) {
            RenderSystem.disableCull();
            RenderSystem.enableBlend();
            RenderSystem.activeTexture(33984);
            RenderSystem.disableDepthTest();
            RenderSystem.blendFuncSeparate(1, 771, 1, 771);
            GLCore.glBlendFuncSeparate(1, 771, 1, 771);
            int oldVertexArray = GLCore.glGetInteger(34229);
            int oldProgram = GLCore.glGetInteger(35725);
            int width = this.mWindow.getWidth();
            int height = this.mWindow.getHeight();
            this.mDevice.markContextDirty(2);
            this.mCanvas.setProjection(this.mProjectionMatrix.setOrthographic((float) width, (float) height, 0.0F, 5999.0F, true));
            this.mRoot.flushDrawCommands(this.mCanvas, this.mSurface, width, height);
            ResourceCache resourceCache = this.mDevice.getContext().getResourceCache();
            resourceCache.cleanup();
            if (this.mFrameTimeNanos - this.mLastPurgeNanos >= 120000000000L) {
                this.mLastPurgeNanos = this.mFrameTimeNanos;
                resourceCache.purgeFreeResourcesOlderThan(System.currentTimeMillis() - 120000L, true);
                GlyphManager.getInstance().compact();
            }
            GLCore.glBindVertexArray(oldVertexArray);
            GLCore.glUseProgram(oldProgram);
            RenderSystem.defaultBlendFunc();
            RenderSystem.disableBlend();
            RenderSystem.bindTexture(0);
        }
    }

    @MainThread
    void resize() {
        if (this.mRoot != null) {
            this.mRoot.mHandler.post(this.mResizeRunnable);
        }
    }

    @UiThread
    public void updateLayoutDir(boolean forceRTL) {
        if (this.mDecor != null) {
            boolean layoutRtl = forceRTL || TextUtils.getLayoutDirectionFromLocale(ModernUI.getSelectedLocale()) == 1;
            this.mDecor.setLayoutDirection(layoutRtl ? 1 : 3);
            this.mDecor.requestLayout();
            this.mTooltipRenderer.mLayoutRTL = layoutRtl;
        }
    }

    @MainThread
    public void removed() {
        MuiScreen screen = this.mScreen;
        if (screen != null) {
            this.mRoot.mHandler.post(this::suppressLayoutTransition);
            this.mFragmentController.getFragmentManager().beginTransaction().remove(screen.getFragment()).runOnCommit(() -> this.mFragmentContainerView.removeAllViews()).commit();
            this.mRoot.mHandler.post(this::restoreLayoutTransition);
            this.mScreen = null;
            GLFW.glfwSetCursor(this.mWindow.getWindow(), 0L);
        }
    }

    public void drawExtTooltip(ItemStack itemStack, GuiGraphics graphics, List<ClientTooltipComponent> components, int x, int y, Font font, int screenWidth, int screenHeight, ClientTooltipPositioner positioner) {
        Window window = this.mWindow;
        MouseHandler mouseHandler = this.minecraft.mouseHandler;
        double cursorX = mouseHandler.xpos() * (double) window.getGuiScaledWidth() / (double) window.getScreenWidth();
        double cursorY = mouseHandler.ypos() * (double) window.getGuiScaledHeight() / (double) window.getScreenHeight();
        int mouseX = (int) cursorX;
        int mouseY = (int) cursorY;
        if (TooltipRenderer.sExactPositioning && positioner instanceof DefaultTooltipPositioner) {
            positioner = null;
        }
        float partialX;
        float partialY;
        if (x == mouseX && y == mouseY && positioner == null && isIdentity(graphics.pose().last().pose())) {
            partialX = (float) (cursorX - (double) mouseX);
            partialY = (float) (cursorY - (double) mouseY);
        } else {
            partialX = 0.0F;
            partialY = 0.0F;
        }
        this.mTooltipRenderer.drawTooltip(itemStack, graphics, components, x, y, font, screenWidth, screenHeight, partialX, partialY, positioner);
    }

    private static boolean isIdentity(Matrix4f ctm) {
        return (ctm.properties() & 4) != 0 ? true : MathUtil.isApproxEqual(ctm.m00(), 1.0F) && MathUtil.isApproxZero(ctm.m01()) && MathUtil.isApproxZero(ctm.m02()) && MathUtil.isApproxZero(ctm.m03()) && MathUtil.isApproxZero(ctm.m10()) && MathUtil.isApproxEqual(ctm.m11(), 1.0F) && MathUtil.isApproxZero(ctm.m12()) && MathUtil.isApproxZero(ctm.m13()) && MathUtil.isApproxZero(ctm.m20()) && MathUtil.isApproxZero(ctm.m21()) && MathUtil.isApproxEqual(ctm.m22(), 1.0F) && MathUtil.isApproxZero(ctm.m23()) && MathUtil.isApproxZero(ctm.m30()) && MathUtil.isApproxZero(ctm.m31()) && MathUtil.isApproxZero(ctm.m32()) && MathUtil.isApproxEqual(ctm.m33(), 1.0F);
    }

    protected void onRenderTick(boolean isEnd) {
        if (!isEnd) {
            long lastFrameTime = this.mFrameTimeNanos;
            this.mFrameTimeNanos = Core.timeNanos();
            long deltaMillis = (this.mFrameTimeNanos - lastFrameTime) / 1000000L;
            this.mElapsedTimeMillis += deltaMillis;
            if (this.mRunning) {
                BlurHandler.INSTANCE.onRenderTick(this.mElapsedTimeMillis);
                if (TooltipRenderer.sTooltip) {
                    this.mTooltipRenderer.update(deltaMillis, this.mFrameTimeNanos / 1000000L);
                }
            }
        } else if (!this.minecraft.isRunning() && this.mRunning) {
            this.mRunning = false;
            this.mRoot.mHandler.post(this::finish);
        } else if (this.minecraft.isRunning() && this.mRunning && this.mScreen == null && this.minecraft.getOverlay() == null) {
            this.render();
        }
    }

    protected void onClientTick(boolean isEnd) {
        if (isEnd) {
            BlurHandler.INSTANCE.onClientTick();
        }
    }

    public static void destroy() {
        ModernUI.LOGGER.debug(MARKER, "Quiting Modern UI");
        if (sInstance != null && sInstance.mCanvas != null) {
            sInstance.mCanvas.destroy();
        }
        FontResourceManager.getInstance().close();
        ImageStore.getInstance().clear();
        Core.requireDirectContext().unref();
        if (sInstance != null) {
            AudioManager.getInstance().close();
            try {
                sInstance.mUiThread.join(1000L);
            } catch (InterruptedException var1) {
                var1.printStackTrace();
            }
        }
        ModernUI.LOGGER.debug(MARKER, "Quited Modern UI");
    }

    @UiThread
    protected class HostCallbacks extends FragmentHostCallback<Object> implements ViewModelStoreOwner, OnBackPressedDispatcherOwner {

        HostCallbacks() {
            super(ModernUI.getInstance(), new Handler(Looper.myLooper()));
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
            return UIManager.this.mDecor.findViewById(id);
        }

        @Nonnull
        @Override
        public ViewModelStore getViewModelStore() {
            return UIManager.this.mViewModelStore;
        }

        @Nonnull
        @Override
        public OnBackPressedDispatcher getOnBackPressedDispatcher() {
            return UIManager.this.mOnBackPressedDispatcher;
        }

        @Nonnull
        @Override
        public Lifecycle getLifecycle() {
            return UIManager.this.mFragmentLifecycleRegistry;
        }
    }

    @UiThread
    protected class ViewRootImpl extends ViewRoot {

        private final Rect mGlobalRect = new Rect();

        ContextMenuBuilder mContextMenu;

        MenuHelper mContextMenuHelper;

        private volatile boolean mPendingDraw = false;

        private boolean mBlit;

        @Override
        protected boolean dispatchTouchEvent(MotionEvent event) {
            if (UIManager.this.mScreen != null && event.getAction() == 0) {
                View v = UIManager.this.mDecor.findFocus();
                if (v instanceof EditText) {
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
            MuiScreen screen = UIManager.this.mScreen;
            if (screen != null && event.getAction() == 0) {
                boolean back;
                if (screen.getCallback() != null) {
                    back = screen.getCallback().isBackKey(event.getKeyCode(), event);
                } else if (screen.isMenuScreen()) {
                    if (event.getKeyCode() == 256) {
                        back = true;
                    } else {
                        InputConstants.Key key = InputConstants.getKey(event.getKeyCode(), event.getScanCode());
                        back = MuiModApi.get().isKeyBindingMatches(UIManager.this.minecraft.options.keyInventory, key);
                    }
                } else {
                    back = event.getKeyCode() == 256;
                }
                if (back) {
                    View v = UIManager.this.mDecor.findFocus();
                    if (v instanceof EditText) {
                        if (event.getKeyCode() == 256) {
                            v.clearFocus();
                        }
                    } else {
                        UIManager.this.mOnBackPressedDispatcher.onBackPressed();
                    }
                }
            }
        }

        @Override
        protected Canvas beginDrawLocked(int width, int height) {
            if (UIManager.this.mCanvas != null) {
                UIManager.this.mCanvas.reset(width, height);
            }
            return UIManager.this.mCanvas;
        }

        @Override
        protected void endDrawLocked(@Nonnull Canvas canvas) {
            this.mPendingDraw = true;
            try {
                this.mRenderLock.wait();
            } catch (InterruptedException var3) {
                Thread.currentThread().interrupt();
            }
        }

        @RenderThread
        private void flushDrawCommands(GLSurfaceCanvas canvas, GLSurface surface, int width, int height) {
            synchronized (this.mRenderLock) {
                if (this.mPendingDraw) {
                    GLCore.glEnable(2960);
                    try {
                        this.mBlit = canvas.executeRenderPass(surface);
                    } catch (Throwable var12) {
                        ModernUI.LOGGER.fatal(MARKER, "Failed to invoke rendering callbacks, please report the issue to related mods", var12);
                        UIManager.this.dump();
                        throw var12;
                    } finally {
                        GLCore.glDisable(2960);
                        this.mPendingDraw = false;
                        this.mRenderLock.notifyAll();
                    }
                }
                if (this.mBlit && surface.getBackingWidth() > 0) {
                    GLTexture layer = surface.getAttachedTexture(36064);
                    GLCore.glBindFramebuffer(36009, UIManager.this.minecraft.getMainRenderTarget().frameBufferId);
                    canvas.drawLayer(layer, (float) width, (float) height, 1.0F, true);
                    canvas.executeRenderPass(null);
                }
            }
        }

        @Override
        public void playSoundEffect(int effectId) {
        }

        @Override
        public boolean performHapticFeedback(int effectId, boolean always) {
            return false;
        }

        @MainThread
        @Override
        protected void applyPointerIcon(int pointerType) {
            UIManager.this.minecraft.m_6937_(() -> GLFW.glfwSetCursor(UIManager.this.mWindow.getWindow(), PointerIcon.getSystemIcon(pointerType).getHandle()));
        }

        @Override
        public boolean showContextMenuForChild(View originalView, float x, float y) {
            if (this.mContextMenuHelper != null) {
                this.mContextMenuHelper.dismiss();
                this.mContextMenuHelper = null;
            }
            if (this.mContextMenu == null) {
                this.mContextMenu = new ContextMenuBuilder(ModernUI.getInstance());
            } else {
                this.mContextMenu.clearAll();
            }
            boolean isPopup = !Float.isNaN(x) && !Float.isNaN(y);
            MenuHelper helper;
            if (isPopup) {
                helper = this.mContextMenu.showPopup(ModernUI.getInstance(), originalView, x, y);
            } else {
                helper = this.mContextMenu.showPopup(ModernUI.getInstance(), originalView, 0.0F, 0.0F);
            }
            this.mContextMenuHelper = helper;
            return helper != null;
        }
    }
}