package noppes.npcs.mixin;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import noppes.npcs.client.renderer.RenderCustomNpc;
import noppes.npcs.entity.EntityCustomNpc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ LivingEntityRenderer.class })
public class LivingRendererMixin<T extends EntityCustomNpc, M extends HumanoidModel<T>> {

    @Inject(at = { @At("HEAD") }, method = { "addLayer" })
    private void spawnOriginalMobs(RenderLayer<T, M> layer, CallbackInfoReturnable<Boolean> cir) {
        LivingEntityRenderer renderer = (LivingEntityRenderer) this;
        if (renderer instanceof RenderCustomNpc) {
            ((RenderCustomNpc) renderer).npclayers.add(layer);
        }
    }
}