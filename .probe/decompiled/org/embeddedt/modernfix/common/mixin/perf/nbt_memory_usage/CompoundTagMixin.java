package org.embeddedt.modernfix.common.mixin.perf.nbt_memory_usage;

import java.util.Map;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import org.embeddedt.modernfix.util.CanonizingStringMap;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ CompoundTag.class })
public class CompoundTagMixin {

    @Shadow
    @Final
    private Map<String, Tag> tags;

    @ModifyArg(method = { "<init>()V" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/CompoundTag;<init>(Ljava/util/Map;)V"), index = 0)
    private static Map<String, Tag> useCanonizingStringMap(Map<String, Tag> incoming) {
        CanonizingStringMap<Tag> newMap = new CanonizingStringMap<>();
        if (incoming != null) {
            newMap.putAll(incoming);
        }
        return newMap;
    }

    @Inject(method = { "copy()Lnet/minecraft/nbt/CompoundTag;" }, at = { @At("HEAD") }, cancellable = true)
    public void copyEfficient(CallbackInfoReturnable<Tag> cir) {
        if (this.tags instanceof CanonizingStringMap) {
            cir.setReturnValue(new CompoundTag(CanonizingStringMap.deepCopy((CanonizingStringMap) this.tags, Tag::m_6426_)));
        }
    }
}