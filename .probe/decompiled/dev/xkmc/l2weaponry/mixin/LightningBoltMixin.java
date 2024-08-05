package dev.xkmc.l2weaponry.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ LightningBolt.class })
public abstract class LightningBoltMixin extends Entity {

    public LightningBoltMixin(EntityType<?> entityType0, Level level1) {
        super(entityType0, level1);
    }

    @Inject(at = { @At("HEAD") }, method = { "spawnFire" }, cancellable = true)
    public void l2weaponry$spawnFire$cancelFire(int i, CallbackInfo ci) {
        if (this.m_19880_().contains("l2weaponry:lightning")) {
            ci.cancel();
        }
    }
}