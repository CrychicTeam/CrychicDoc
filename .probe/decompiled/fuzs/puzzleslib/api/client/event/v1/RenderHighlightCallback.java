package fuzs.puzzleslib.api.client.event.v1;

import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.puzzleslib.api.event.v1.core.EventInvoker;
import fuzs.puzzleslib.api.event.v1.core.EventResult;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.phys.HitResult;

@FunctionalInterface
public interface RenderHighlightCallback {

    EventInvoker<RenderHighlightCallback> EVENT = EventInvoker.lookup(RenderHighlightCallback.class);

    EventResult onRenderHighlight(LevelRenderer var1, Camera var2, GameRenderer var3, HitResult var4, float var5, PoseStack var6, MultiBufferSource var7, ClientLevel var8);
}