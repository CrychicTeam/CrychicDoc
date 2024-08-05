package yesman.epicfight.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.player.PlayerPatch;
import yesman.epicfight.world.capabilities.projectile.ThrownTridentPatch;

@Mixin({ ThrownTrident.class })
public abstract class MixinThrownTrident extends AbstractArrow {

    protected MixinThrownTrident(EntityType<? extends AbstractArrow> entityTypeExtendsAbstractArrow0, Level level1) {
        super(entityTypeExtendsAbstractArrow0, level1);
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/ThrownTrident;setPosRaw(DDD)V"), method = { "tick()V" })
    private void epicfight_setPosRawInTick(ThrownTrident entity, double x, double y, double z) {
        ThrownTridentPatch tridentPatch = EpicFightCapabilities.getEntityPatch(entity, ThrownTridentPatch.class);
        if (tridentPatch == null || !tridentPatch.isInnateActivated()) {
            entity.m_20343_(x, y, z);
        }
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/ThrownTrident;setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V"), method = { "tick()V" })
    private void epicfight_setDeltaMovementInTick(ThrownTrident entity, Vec3 vec3) {
        ThrownTridentPatch tridentPatch = EpicFightCapabilities.getEntityPatch(entity, ThrownTridentPatch.class);
        if (tridentPatch == null || !tridentPatch.isInnateActivated()) {
            entity.m_20256_(vec3);
        }
    }

    @Inject(at = { @At("TAIL") }, method = { "tick()V" }, cancellable = false)
    private void epicfight_tickEnd(CallbackInfo info) {
        ThrownTridentPatch tridentPatch = EpicFightCapabilities.getEntityPatch((ThrownTrident) this, ThrownTridentPatch.class);
        if (tridentPatch != null) {
            tridentPatch.tickEnd();
        }
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/AbstractArrow;playerTouch(Lnet/minecraft/world/entity/player/Player;)V"), method = { "playerTouch(Lnet/minecraft/world/entity/player/Player;)V" })
    private void epicfight_playerTouch(AbstractArrow entity, Player player) {
        ThrownTridentPatch tridentPatch = EpicFightCapabilities.getEntityPatch(entity, ThrownTridentPatch.class);
        if (tridentPatch != null && tridentPatch.isInnateActivated()) {
            PlayerPatch<?> playerpatch = EpicFightCapabilities.getEntityPatch(player, PlayerPatch.class);
            if (playerpatch != null && !playerpatch.isLogicalClient()) {
                tridentPatch.catchByPlayer(playerpatch);
            }
        }
        super.playerTouch(player);
    }
}