package fuzs.puzzleslib.api.client.event.v1;

import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.puzzleslib.api.event.v1.core.EventInvoker;
import fuzs.puzzleslib.api.event.v1.core.EventResult;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

@FunctionalInterface
public interface RenderHandCallback {

    EventInvoker<RenderHandCallback> EVENT = EventInvoker.lookup(RenderHandCallback.class);

    EventResult onRenderHand(Player var1, InteractionHand var2, ItemStack var3, PoseStack var4, MultiBufferSource var5, int var6, float var7, float var8, float var9, float var10);
}