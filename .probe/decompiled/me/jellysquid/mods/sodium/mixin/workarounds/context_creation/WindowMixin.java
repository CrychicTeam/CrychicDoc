package me.jellysquid.mods.sodium.mixin.workarounds.context_creation;

import com.mojang.blaze3d.platform.Window;
import java.util.Objects;
import java.util.function.IntSupplier;
import java.util.function.LongSupplier;
import java.util.function.Supplier;
import me.jellysquid.mods.sodium.client.SodiumClientMod;
import me.jellysquid.mods.sodium.client.compatibility.checks.LateDriverScanner;
import me.jellysquid.mods.sodium.client.compatibility.checks.ModuleScanner;
import me.jellysquid.mods.sodium.client.compatibility.workarounds.Workarounds;
import me.jellysquid.mods.sodium.client.compatibility.workarounds.nvidia.NvidiaWorkarounds;
import net.minecraft.Util;
import net.minecraftforge.fml.loading.FMLConfig;
import net.minecraftforge.fml.loading.ImmediateWindowHandler;
import net.minecraftforge.fml.loading.FMLConfig.ConfigValue;
import org.embeddedt.embeddium.bootstrap.EmbeddiumEarlyWindowHacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLCapabilities;
import org.lwjgl.opengl.WGL;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ Window.class })
public class WindowMixin {

    @Shadow
    @Final
    private static Logger LOGGER;

    @Unique
    private long wglPrevContext = 0L;

    @Redirect(method = { "<init>" }, at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fml/loading/ImmediateWindowHandler;setupMinecraftWindow(Ljava/util/function/IntSupplier;Ljava/util/function/IntSupplier;Ljava/util/function/Supplier;Ljava/util/function/LongSupplier;)J", remap = false))
    private long wrapGlfwCreateWindow(IntSupplier width, IntSupplier height, Supplier<String> title, LongSupplier monitor) {
        boolean applyNvidiaWorkarounds = Workarounds.isWorkaroundEnabled(Workarounds.Reference.NVIDIA_THREADED_OPTIMIZATIONS);
        if (applyNvidiaWorkarounds) {
            NvidiaWorkarounds.install();
        }
        if (SodiumClientMod.options().performance.useNoErrorGLContext && !Workarounds.isWorkaroundEnabled(Workarounds.Reference.NO_ERROR_CONTEXT_UNSUPPORTED)) {
            GLFW.glfwWindowHint(139274, 1);
        }
        if (FMLConfig.getBoolConfigValue(ConfigValue.EARLY_WINDOW_CONTROL) && Objects.equals(FMLConfig.getConfigValue(ConfigValue.EARLY_WINDOW_PROVIDER), "embeddium")) {
            EmbeddiumEarlyWindowHacks.createEarlyLaunchWindow(width, height);
        }
        long var6;
        try {
            var6 = ImmediateWindowHandler.setupMinecraftWindow(width, height, title, monitor);
        } finally {
            if (applyNvidiaWorkarounds) {
                NvidiaWorkarounds.uninstall();
            }
        }
        return var6;
    }

    @Redirect(method = { "<init>" }, at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL;createCapabilities()Lorg/lwjgl/opengl/GLCapabilities;", remap = false))
    private GLCapabilities postWindowCreated() {
        GLCapabilities caps = GL.createCapabilities();
        if (Util.getPlatform() == Util.OS.WINDOWS) {
            this.wglPrevContext = WGL.wglGetCurrentContext();
        } else {
            this.wglPrevContext = 0L;
        }
        LateDriverScanner.onContextInitialized();
        ModuleScanner.checkModules();
        return caps;
    }

    @Inject(method = { "updateDisplay" }, at = { @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;flipFrame(J)V", shift = Shift.AFTER) })
    private void preSwapBuffers(CallbackInfo ci) {
        if (this.wglPrevContext != 0L) {
            long context = WGL.wglGetCurrentContext();
            if (this.wglPrevContext != context) {
                LOGGER.warn("The OpenGL context appears to have been suddenly replaced! Something has likely just injected into the game process.");
                ModuleScanner.checkModules();
                this.wglPrevContext = context;
            }
        }
    }
}