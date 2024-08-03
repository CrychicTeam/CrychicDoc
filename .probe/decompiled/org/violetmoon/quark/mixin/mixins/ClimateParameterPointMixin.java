package org.violetmoon.quark.mixin.mixins;

import com.google.common.collect.ImmutableList;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.level.biome.Climate;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.violetmoon.quark.content.experimental.module.ClimateControlRemoverModule;

@Mixin({ Climate.ParameterPoint.class })
public class ClimateParameterPointMixin {

    @Shadow
    @Final
    private Climate.Parameter temperature;

    @Shadow
    @Final
    private Climate.Parameter humidity;

    @Shadow
    @Final
    private Climate.Parameter continentalness;

    @Shadow
    @Final
    private Climate.Parameter erosion;

    @Shadow
    @Final
    private Climate.Parameter depth;

    @Shadow
    @Final
    private Climate.Parameter weirdness;

    @Shadow
    @Final
    private long offset;

    @WrapOperation(method = { "fitness" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/world/level/biome/Climate$Parameter;distance(J)J") })
    public long giveMinimumDistanceForDisabledParameters(Climate.Parameter parameter, long targetValue, Operation<Long> original) {
        return !ClimateControlRemoverModule.staticEnabled || (parameter != this.temperature || !ClimateControlRemoverModule.disableTemperature) && (parameter != this.humidity || !ClimateControlRemoverModule.disableHumidity) && (parameter != this.continentalness || !ClimateControlRemoverModule.disableContinentalness) && (parameter != this.erosion || !ClimateControlRemoverModule.disableErosion) && (parameter != this.depth || !ClimateControlRemoverModule.disableDepth) && (parameter != this.weirdness || !ClimateControlRemoverModule.disableWeirdness) ? (Long) original.call(new Object[] { parameter, targetValue }) : 0L;
    }

    @ModifyExpressionValue(method = { "fitness" }, at = { @At(value = "FIELD", target = "Lnet/minecraft/world/level/biome/Climate$ParameterPoint;offset:J", opcode = 180) })
    public long giveMinimumOffsetIfDisabled(long originalOffset) {
        return ClimateControlRemoverModule.staticEnabled && ClimateControlRemoverModule.disableOffset ? 0L : originalOffset;
    }

    @ModifyReturnValue(method = { "parameterSpace" }, at = { @At("RETURN") })
    public List<Climate.Parameter> dummyOutDisabledParameters(List<Climate.Parameter> original) {
        if (!ClimateControlRemoverModule.staticEnabled) {
            return original;
        } else {
            Climate.Parameter dummyParameter = new Climate.Parameter(0L, 0L);
            List<Climate.Parameter> newParameterSpace = new ArrayList(original.size());
            for (Climate.Parameter parameter : original) {
                if (parameter == this.humidity) {
                    if (ClimateControlRemoverModule.disableHumidity) {
                        newParameterSpace.add(dummyParameter);
                    } else {
                        newParameterSpace.add(parameter);
                    }
                } else if (parameter == this.temperature) {
                    if (ClimateControlRemoverModule.disableTemperature) {
                        newParameterSpace.add(dummyParameter);
                    } else {
                        newParameterSpace.add(parameter);
                    }
                } else if (parameter == this.continentalness) {
                    if (ClimateControlRemoverModule.disableContinentalness) {
                        newParameterSpace.add(dummyParameter);
                    } else {
                        newParameterSpace.add(parameter);
                    }
                } else if (parameter == this.erosion) {
                    if (ClimateControlRemoverModule.disableErosion) {
                        newParameterSpace.add(dummyParameter);
                    } else {
                        newParameterSpace.add(parameter);
                    }
                } else if (parameter == this.depth) {
                    if (ClimateControlRemoverModule.disableDepth) {
                        newParameterSpace.add(dummyParameter);
                    } else {
                        newParameterSpace.add(parameter);
                    }
                } else if (parameter == this.weirdness) {
                    if (ClimateControlRemoverModule.disableWeirdness) {
                        newParameterSpace.add(dummyParameter);
                    } else {
                        newParameterSpace.add(parameter);
                    }
                } else if (parameter.min() == parameter.max() && parameter.min() == this.offset && ClimateControlRemoverModule.disableOffset) {
                    newParameterSpace.add(dummyParameter);
                } else {
                    newParameterSpace.add(parameter);
                }
            }
            return ImmutableList.copyOf(newParameterSpace);
        }
    }
}