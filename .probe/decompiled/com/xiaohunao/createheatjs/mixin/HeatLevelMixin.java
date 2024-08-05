package com.xiaohunao.createheatjs.mixin;

import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.xiaohunao.createheatjs.CreateHeatJS;
import java.util.ArrayList;
import java.util.Arrays;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = { BlazeBurnerBlock.HeatLevel.class }, remap = false)
public abstract class HeatLevelMixin {

    @Shadow
    @Final
    @Mutable
    private static BlazeBurnerBlock.HeatLevel[] $VALUES;

    @Invoker("<init>")
    public static BlazeBurnerBlock.HeatLevel heatExpansion$invokeInit(String internalName, int internalId) {
        throw new AssertionError();
    }

    @Inject(method = { "<clinit>" }, at = { @At("TAIL") })
    private static void clinit(CallbackInfo ci) {
        CreateHeatJS.heatDataMap.forEach((condition, heatData) -> {
            BlazeBurnerBlock.HeatLevel heatLevel = heatExpansion$addVariant(condition);
            heatData.setHeatLevel(heatLevel);
        });
    }

    private static BlazeBurnerBlock.HeatLevel heatExpansion$addVariant(String internalName) {
        ArrayList<BlazeBurnerBlock.HeatLevel> variants = new ArrayList(Arrays.asList($VALUES));
        BlazeBurnerBlock.HeatLevel heat = heatExpansion$invokeInit(internalName, ((BlazeBurnerBlock.HeatLevel) variants.get(variants.size() - 1)).ordinal() + 1);
        variants.add(heat);
        $VALUES = (BlazeBurnerBlock.HeatLevel[]) variants.toArray(new BlazeBurnerBlock.HeatLevel[0]);
        return heat;
    }
}