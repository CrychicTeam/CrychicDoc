package org.embeddedt.modernfix.common.mixin.perf.dynamic_dfu;

import com.mojang.datafixers.DSL.TypeReference;
import com.mojang.datafixers.types.Type;
import net.minecraft.world.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({ EntityType.Builder.class })
public class EntityTypeBuilderMixin {

    @Redirect(method = { "build" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/Util;fetchChoiceType(Lcom/mojang/datafixers/DSL$TypeReference;Ljava/lang/String;)Lcom/mojang/datafixers/types/Type;"))
    private Type<?> skipSchemaCheck(TypeReference ref, String s) {
        return null;
    }
}