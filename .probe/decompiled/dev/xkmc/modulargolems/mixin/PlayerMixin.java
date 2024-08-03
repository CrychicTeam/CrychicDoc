package dev.xkmc.modulargolems.mixin;

import dev.xkmc.modulargolems.content.entity.dog.DogGolemEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ Player.class })
public abstract class PlayerMixin extends LivingEntity {

    protected PlayerMixin(EntityType<? extends LivingEntity> entityTypeExtendsLivingEntity0, Level level1) {
        super(entityTypeExtendsLivingEntity0, level1);
    }

    @Inject(at = { @At("HEAD") }, method = { "wantsToStopRiding" }, cancellable = true)
    public void modulargolems$wantsToStopRiding$dogOverride(CallbackInfoReturnable<Boolean> cir) {
        if (this.m_20202_() instanceof DogGolemEntity dog && dog.m_20072_()) {
            cir.setReturnValue(false);
        }
    }
}