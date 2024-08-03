package io.redspace.ironsspellbooks.mixin;

import net.minecraft.world.entity.ai.attributes.Attributes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ Attributes.class })
public class AttributesMixin {

    @Inject(method = { "<clinit>" }, at = { @At("TAIL") })
    private static void makeSynced(CallbackInfo ci) {
        Attributes.ATTACK_DAMAGE.setSyncable(true);
    }
}