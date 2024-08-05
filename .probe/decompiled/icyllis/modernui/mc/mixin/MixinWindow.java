package icyllis.modernui.mc.mixin;

import com.mojang.blaze3d.platform.Monitor;
import com.mojang.blaze3d.platform.VideoMode;
import com.mojang.blaze3d.platform.Window;
import icyllis.modernui.ModernUI;
import icyllis.modernui.graphics.MathUtil;
import icyllis.modernui.mc.ModernUIClient;
import icyllis.modernui.mc.MuiModApi;
import icyllis.modernui.util.DisplayMetrics;
import javax.annotation.Nullable;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ Window.class })
public abstract class MixinWindow {

    @Shadow
    private double guiScale;

    @Shadow
    public abstract int getWidth();

    @Shadow
    public abstract int getHeight();

    @Shadow
    @Nullable
    public abstract Monitor findBestMonitor();

    @Inject(method = { "calculateScale" }, at = { @At("HEAD") }, cancellable = true)
    public void onCalculateScale(int guiScaleIn, boolean forceUnicode, CallbackInfoReturnable<Integer> ci) {
        int r = MuiModApi.calcGuiScales((Window) this);
        ci.setReturnValue(guiScaleIn > 0 ? MathUtil.clamp(guiScaleIn, r >> 8 & 15, r & 15) : r >> 4 & 15);
    }

    @Inject(method = { "setGuiScale" }, at = { @At("HEAD") })
    private void onSetGuiScale(double scaleFactor, CallbackInfo ci) {
        int oldScale = (int) this.guiScale;
        int newScale = (int) scaleFactor;
        if ((double) newScale != scaleFactor) {
            ModernUI.LOGGER.warn(ModernUI.MARKER, "Gui scale {} should be an integer, some mods break this", scaleFactor);
        }
        DisplayMetrics metrics = new DisplayMetrics();
        metrics.setToDefaults();
        metrics.widthPixels = this.getWidth();
        metrics.heightPixels = this.getHeight();
        metrics.density = (float) newScale * 0.5F;
        metrics.densityDpi = (int) (metrics.density * 72.0F);
        metrics.scaledDensity = ModernUIClient.sFontScale * metrics.density;
        Monitor monitor = this.findBestMonitor();
        if (monitor != null) {
            int[] w = new int[] { 0 };
            int[] h = new int[] { 0 };
            GLFW.glfwGetMonitorPhysicalSize(monitor.getMonitor(), w, h);
            VideoMode mode = monitor.getCurrentMode();
            metrics.xdpi = 25.4F * (float) mode.getWidth() / (float) w[0];
            metrics.ydpi = 25.4F * (float) mode.getHeight() / (float) h[0];
        }
        ModernUI ctx = ModernUI.getInstance();
        if (ctx != null) {
            ctx.getResources().updateMetrics(metrics);
        }
        MuiModApi.dispatchOnWindowResize(this.getWidth(), this.getHeight(), newScale, oldScale);
    }

    @Redirect(method = { "<init>" }, at = @At(value = "INVOKE", target = "Lorg/lwjgl/glfw/GLFW;glfwWindowHint(II)V", ordinal = 5), remap = false)
    private void onInit(int x, int y) {
        if (!MuiModApi.get().isGLVersionPromoted()) {
            GLFWErrorCallback callback = GLFW.glfwSetErrorCallback(null);
            GLFW.glfwWindowHint(131076, 0);
            GLFW.glfwWindowHint(139272, 204801);
            GLFW.glfwWindowHint(139270, 1);
            int[][] versions = new int[][] { { 4, 6 }, { 4, 5 }, { 4, 1 }, { 3, 3 } };
            long window = 0L;
            try {
                for (int[] version : versions) {
                    GLFW.glfwWindowHint(139266, version[0]);
                    GLFW.glfwWindowHint(139267, version[1]);
                    ModernUI.LOGGER.debug(ModernUI.MARKER, "Trying OpenGL {}.{}", version[0], version[1]);
                    window = GLFW.glfwCreateWindow(640, 480, "System Testing", 0L, 0L);
                    if (window != 0L) {
                        ModernUI.LOGGER.info(ModernUI.MARKER, "Promoted to OpenGL {}.{} Core Profile", version[0], version[1]);
                        return;
                    }
                }
                GLFW.glfwWindowHint(139266, 3);
                GLFW.glfwWindowHint(139267, 2);
                ModernUI.LOGGER.warn(ModernUI.MARKER, "Fallback to OpenGL 3.2 Core Profile");
            } catch (Exception var14) {
                GLFW.glfwWindowHint(139266, 3);
                GLFW.glfwWindowHint(139267, 2);
                ModernUI.LOGGER.warn(ModernUI.MARKER, "Fallback to OpenGL 3.2 Core Profile", var14);
            } finally {
                if (window != 0L) {
                    GLFW.glfwDestroyWindow(window);
                }
                GLFW.glfwWindowHint(131076, 1);
                GLFW.glfwSetErrorCallback(callback);
            }
        }
    }
}