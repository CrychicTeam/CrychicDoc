package com.craisinlord.integrated_api.mixins.forge.structures;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = { StructureTemplatePool.class }, priority = 1200)
public class StructurePoolMixin {

    @WrapOperation(method = { "method_28886", "lambda$static$1(Lcom/mojang/serialization/codecs/RecordCodecBuilder$Instance;)Lcom/mojang/datafixers/kinds/App;", "Lnet/minecraft/world/level/levelgen/structure/pools/StructureTemplatePool;m_254834_(Lcom/mojang/serialization/codecs/RecordCodecBuilder$Instance;)Lcom/mojang/datafixers/kinds/App;" }, at = { @At(value = "INVOKE", target = "Lcom/mojang/serialization/Codec;intRange(II)Lcom/mojang/serialization/Codec;") }, require = 0, remap = false)
    private static Codec<Integer> integratedapi_increaseWeightLimit(int minRange, int maxRange, Operation<Codec<Integer>> original) {
        return (Codec<Integer>) original.call(new Object[] { minRange, 5000 });
    }
}