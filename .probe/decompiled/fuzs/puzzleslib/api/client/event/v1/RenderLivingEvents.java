package fuzs.puzzleslib.api.client.event.v1;

import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.puzzleslib.api.event.v1.core.EventInvoker;
import fuzs.puzzleslib.api.event.v1.core.EventResult;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;

public final class RenderLivingEvents {

    public static final EventInvoker<RenderLivingEvents.Before> BEFORE = EventInvoker.lookup(RenderLivingEvents.Before.class);

    public static final EventInvoker<RenderLivingEvents.After> AFTER = EventInvoker.lookup(RenderLivingEvents.After.class);

    private RenderLivingEvents() {
    }

    @FunctionalInterface
    public interface After {

        <T extends LivingEntity, M extends EntityModel<T>> void onAfterRenderEntity(T var1, LivingEntityRenderer<T, M> var2, float var3, PoseStack var4, MultiBufferSource var5, int var6);
    }

    @FunctionalInterface
    public interface Before {

        <T extends LivingEntity, M extends EntityModel<T>> EventResult onBeforeRenderEntity(T var1, LivingEntityRenderer<T, M> var2, float var3, PoseStack var4, MultiBufferSource var5, int var6);
    }
}