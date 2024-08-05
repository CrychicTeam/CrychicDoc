package dev.xkmc.l2hostility.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.xkmc.l2hostility.events.MiscHandlers;
import dev.xkmc.l2serial.util.Wrappers;
import net.minecraft.world.entity.monster.Slime;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({ Slime.class })
public class SlimeMixin {

    @WrapOperation(at = { @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/Slime;setNoAi(Z)V") }, method = { "remove" })
    public void l2hostility$remove$inheritCap(Slime sub, boolean noAI, Operation<Void> op) {
        Slime self = (Slime) Wrappers.cast(this);
        MiscHandlers.copyCap(self, sub);
        op.call(new Object[] { sub, noAI });
    }
}