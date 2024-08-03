package fuzs.puzzleslib.api.client.event.v1;

import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.puzzleslib.api.event.v1.core.EventInvoker;
import fuzs.puzzleslib.api.event.v1.core.EventResult;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface RenderBlockOverlayCallback {

    EventInvoker<RenderBlockOverlayCallback> EVENT = EventInvoker.lookup(RenderBlockOverlayCallback.class);

    EventResult onRenderBlockOverlay(LocalPlayer var1, PoseStack var2, @Nullable BlockState var3);
}