package dev.xkmc.l2hostility.mixin;

import dev.xkmc.l2hostility.init.registrate.LHEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ Player.class })
public abstract class PlayerMixin extends LivingEntity {

    protected PlayerMixin(EntityType<? extends LivingEntity> type, Level level) {
        super(type, level);
    }

    @Inject(at = { @At("HEAD") }, method = { "blockActionRestricted" }, cancellable = true)
    public void l2hostility$blockActionRestricted$antibuild(Level level, BlockPos pos, GameType type, CallbackInfoReturnable<Boolean> cir) {
        if (type == GameType.SURVIVAL && this.m_21023_((MobEffect) LHEffects.ANTIBUILD.get())) {
            cir.setReturnValue(true);
        }
    }
}