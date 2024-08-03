package org.embeddedt.modernfix.forge.mixin.perf.fast_forge_dummies;

import com.mojang.serialization.Lifecycle;
import java.util.Map;
import net.minecraft.core.Holder;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = { "net/minecraftforge/registries/NamespacedWrapper" })
public abstract class NamespacedHolderHelperMixin<T> extends MappedRegistry<T> {

    @Shadow(remap = false)
    private Map<ResourceLocation, Holder.Reference<T>> holdersByName;

    public NamespacedHolderHelperMixin(ResourceKey<? extends Registry<T>> arg, Lifecycle lifecycle) {
        super(arg, lifecycle);
    }

    @Inject(method = { "freeze" }, at = { @At(value = "FIELD", opcode = 180, target = "Lnet/minecraftforge/registries/NamespacedWrapper;holdersByName:Ljava/util/Map;", remap = false) }, cancellable = true)
    private void fastDummyCheck(CallbackInfoReturnable<Registry<T>> cir) {
        for (Holder.Reference<T> ref : this.holdersByName.values()) {
            if (!ref.isBound()) {
                return;
            }
        }
        if (this.f_244282_ != null) {
            for (Holder.Reference<T> refx : this.f_244282_.values()) {
                if (refx.getType() == Holder.Reference.Type.INTRUSIVE && !refx.isBound()) {
                    return;
                }
            }
        }
        cir.setReturnValue(this);
    }
}