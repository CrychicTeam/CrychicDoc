package snownee.loquat.mixin.forge;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraftforge.event.entity.living.ZombieEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import snownee.loquat.util.CommonProxy;

@Mixin({ Zombie.class })
public class ZombieMixin {

    @Inject(method = { "hurt" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;addFreshEntityWithPassengers(Lnet/minecraft/world/entity/Entity;)V") }, locals = LocalCapture.CAPTURE_FAILSOFT)
    private void onReinforcement(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir, ServerLevel serverLevel, LivingEntity livingEntity, int i, int j, int k, ZombieEvent.SummonAidEvent forgeEvent, Zombie zombie, int l, int m, int n, int o, BlockPos blockPos, EntityType entityType, SpawnPlacements.Type type) {
        CommonProxy.onSuccessiveSpawn((Zombie) this, zombie);
    }
}