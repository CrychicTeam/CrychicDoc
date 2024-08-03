package github.pitbox46.fightnbtintegration.mixins;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yesman.epicfight.client.events.engine.RenderEngine;
import yesman.epicfight.client.renderer.patched.item.RenderItemBase;

@Mixin(value = { RenderEngine.class }, remap = false)
public abstract class RenderEngineMixin {

    @Shadow
    public abstract RenderItemBase getItemRenderer(Item var1);

    @Inject(at = { @At("HEAD") }, method = { "getItemRenderer" }, cancellable = true)
    private void afterFindMatchingRendererByClass(Item item, CallbackInfoReturnable<RenderItemBase> cir) {
        Class<? extends Item> clazz = item.getClass();
        if (clazz != null) {
            String var4 = clazz.getName();
            switch(var4) {
                case "se.mickelus.tetra.items.modular.impl.shield.ModularShieldItem":
                    cir.setReturnValue(this.getItemRenderer(Items.SHIELD));
                    return;
                case "se.mickelus.tetra.items.modular.impl.crossbow.ModularCrossbowItem":
                    cir.setReturnValue(this.getItemRenderer(Items.CROSSBOW));
                    return;
                case "se.mickelus.tetra.items.modular.impl.bow.ModularBowItem":
                    cir.setReturnValue(this.getItemRenderer(Items.BOW));
                    return;
            }
        }
    }
}