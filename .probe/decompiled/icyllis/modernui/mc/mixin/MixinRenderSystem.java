package icyllis.modernui.mc.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import icyllis.arc3d.engine.ContextOptions;
import icyllis.modernui.ModernUI;
import icyllis.modernui.core.Core;
import icyllis.modernui.mc.ModernUIClient;
import java.util.Objects;
import net.minecraft.util.TimeSource;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.Configuration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ RenderSystem.class })
public class MixinRenderSystem {

    @Inject(method = { "initBackendSystem" }, at = { @At("HEAD") }, remap = false)
    private static void onInitBackendSystem(CallbackInfoReturnable<TimeSource.NanoTimeSource> ci) {
        RenderSystem.assertInInitPhase();
        String name = (String) Configuration.OPENGL_LIBRARY_NAME.get();
        if (name != null) {
            ModernUI.LOGGER.info(ModernUI.MARKER, "OpenGL library: {}", name);
            Objects.requireNonNull(GL.getFunctionProvider(), "Implicit OpenGL loading is required");
        }
    }

    @Inject(method = { "initRenderer" }, at = { @At("TAIL") }, remap = false)
    private static void onInitRenderer(int debugLevel, boolean debugSync, CallbackInfo ci) {
        Core.initialize();
        ContextOptions options = new ContextOptions();
        options.mDriverBugWorkarounds = ModernUIClient.getGpuDriverBugWorkarounds();
        if (!Core.initOpenGL(options)) {
            Core.glShowCapsErrorDialog();
        }
    }

    @Overwrite(remap = false)
    public static void assertOnRenderThread() {
    }
}