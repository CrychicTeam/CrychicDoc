package org.embeddedt.modernfix.common.mixin.perf.dynamic_dfu;

import com.mojang.datafixers.DSL.TypeReference;
import com.mojang.datafixers.types.Type;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({ BlockEntityType.class })
public class BlockEntityTypeMixin {

    @Redirect(method = { "register" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/Util;fetchChoiceType(Lcom/mojang/datafixers/DSL$TypeReference;Ljava/lang/String;)Lcom/mojang/datafixers/types/Type;"))
    private static Type<?> skipSchemaCheck(TypeReference ref, String s) {
        return null;
    }
}