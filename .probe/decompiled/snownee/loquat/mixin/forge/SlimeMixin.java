package snownee.loquat.mixin.forge;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Slime;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import snownee.loquat.util.CommonProxy;

@Mixin({ Slime.class })
public class SlimeMixin {

    @Inject(method = { "remove" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z") }, locals = LocalCapture.CAPTURE_FAILEXCEPTION)
    private void onRemove(Entity.RemovalReason reason, CallbackInfo ci, int i, Component component, boolean bl, float f, int j, int k, int l, float g, float h, Slime slime) {
        CommonProxy.onSuccessiveSpawn((Entity) this, slime);
    }
}