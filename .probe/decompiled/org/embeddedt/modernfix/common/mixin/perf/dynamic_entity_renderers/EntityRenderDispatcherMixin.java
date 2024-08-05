package org.embeddedt.modernfix.common.mixin.perf.dynamic_entity_renderers;

import java.util.Map;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import org.embeddedt.modernfix.annotation.ClientOnlyMixin;
import org.embeddedt.modernfix.entity.EntityRendererMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ EntityRenderDispatcher.class })
@ClientOnlyMixin
public class EntityRenderDispatcherMixin {

    @Shadow
    private Map<EntityType<?>, EntityRenderer<?>> renderers;

    private EntityRendererMap mfix$dynamicRenderers;

    @Inject(method = { "getRenderer" }, at = { @At("RETURN") }, cancellable = true)
    private <T extends Entity> void checkNullness(T entity, CallbackInfoReturnable<EntityRenderer<? super T>> cir) {
        if (cir.getReturnValue() == null) {
            cir.setReturnValue(this.mfix$dynamicRenderers.get(entity.getType()));
        }
    }

    @Redirect(method = { "onResourceManagerReload" }, at = @At(value = "FIELD", opcode = 181, target = "Lnet/minecraft/client/renderer/entity/EntityRenderDispatcher;renderers:Ljava/util/Map;"))
    private void setRendererField(EntityRenderDispatcher instance, Map<EntityType<?>, EntityRenderer<?>> incomingMap) {
        this.renderers = incomingMap;
        this.mfix$dynamicRenderers = (EntityRendererMap) incomingMap;
    }
}