package fuzs.puzzleslib.api.client.event.v1;

import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.puzzleslib.api.event.v1.core.EventInvoker;
import fuzs.puzzleslib.api.event.v1.core.EventResult;
import fuzs.puzzleslib.api.event.v1.data.DefaultedValue;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;

public interface RenderNameTagCallback {

    EventInvoker<RenderNameTagCallback> EVENT = EventInvoker.lookup(RenderNameTagCallback.class);

    EventResult onRenderNameTag(Entity var1, DefaultedValue<Component> var2, EntityRenderer<?> var3, PoseStack var4, MultiBufferSource var5, int var6, float var7);
}