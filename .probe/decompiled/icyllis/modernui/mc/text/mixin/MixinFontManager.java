package icyllis.modernui.mc.text.mixin;

import icyllis.modernui.mc.text.TextLayoutEngine;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import javax.annotation.Nonnull;
import net.minecraft.client.gui.font.FontManager;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin({ FontManager.class })
public class MixinFontManager {

    @Overwrite
    public CompletableFuture<Void> reload(@Nonnull PreparableReloadListener.PreparationBarrier preparationBarrier, @Nonnull ResourceManager resourceManager, @Nonnull ProfilerFiller preparationProfiler, @Nonnull ProfilerFiller reloadProfiler, @Nonnull Executor preparationExecutor, @Nonnull Executor reloadExecutor) {
        return TextLayoutEngine.getInstance().injectFontManager((FontManager) this).reload(preparationBarrier, resourceManager, preparationProfiler, reloadProfiler, preparationExecutor, reloadExecutor);
    }
}