package icyllis.modernui.mc.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.LevelLoadingScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.progress.StoringChunkProgressListener;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Deprecated
@Mixin({ LevelLoadingScreen.class })
public class MixinLevelLoadingScreen extends Screen {

    @Shadow
    @Final
    private StoringChunkProgressListener progressListener;

    private float mSweep;

    private float mTime;

    protected MixinLevelLoadingScreen(Component titleIn) {
        super(titleIn);
    }

    @Inject(method = { "render" }, at = { @At("TAIL") })
    private void render(PoseStack matrixStack, int scaledMouseX, int scaledMouseY, float deltaTick, CallbackInfo ci) {
    }
}