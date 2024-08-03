package org.embeddedt.modernfix.common.mixin.perf.mojang_registry_size;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.core.MappedRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({ MappedRegistry.class })
public class MappedRegistryMixin {

    @Redirect(method = { "registerMapping(ILnet/minecraft/resources/ResourceKey;Ljava/lang/Object;Lcom/mojang/serialization/Lifecycle;)Lnet/minecraft/core/Holder$Reference;" }, at = @At(value = "INVOKE", target = "Lit/unimi/dsi/fastutil/objects/ObjectList;size(I)V", remap = false))
    private void setSizeSmart(ObjectList<?> list, int size) {
        if (list instanceof ObjectArrayList && size > list.size()) {
            int requestedSize = size;
            int p2Size = Integer.highestOneBit(size);
            if (p2Size != size) {
                size = p2Size << 1;
            }
            ((ObjectArrayList) list).ensureCapacity(size);
            while (list.size() < requestedSize) {
                list.add(null);
            }
        } else {
            list.size(size);
        }
    }
}