package dev.shadowsoffire.attributeslib.mixin;

import com.mojang.authlib.GameProfile;
import dev.shadowsoffire.attributeslib.util.IEntityOwned;
import dev.shadowsoffire.attributeslib.util.IFlying;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ Player.class })
public class PlayerMixin implements IFlying {

    @Shadow
    private Abilities abilities;

    @Nullable
    private boolean apoth_flying;

    @Inject(at = { @At("TAIL") }, method = { "<init>(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;FLcom/mojang/authlib/GameProfile;)V" }, require = 1, remap = false)
    public void apoth_ownedAbilities(Level level, BlockPos pos, float yRot, GameProfile profile, CallbackInfo ci) {
        ((IEntityOwned) this.abilities).setOwner((LivingEntity) this);
    }

    @Inject(at = { @At("TAIL") }, method = { "readAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V" }, require = 1)
    public void apoth_cacheFlying(CompoundTag tag, CallbackInfo ci) {
        if (this.abilities.flying) {
            this.markFlying();
        }
    }

    @Override
    public boolean getAndDestroyFlyingCache() {
        boolean value = this.apoth_flying;
        this.apoth_flying = false;
        return value;
    }

    @Override
    public void markFlying() {
        this.apoth_flying = true;
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z", ordinal = 0), method = { "attack(Lnet/minecraft/world/entity/Entity;)V" })
    private boolean apoth_handleKilledByAuxDmg(LivingEntity target, DamageSource src, float dmg) {
        boolean res = target.hurt(src, dmg);
        return res || target.getPersistentData().getBoolean("apoth.killed_by_aux_dmg");
    }
}