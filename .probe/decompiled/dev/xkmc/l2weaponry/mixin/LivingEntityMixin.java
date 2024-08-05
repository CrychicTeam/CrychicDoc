package dev.xkmc.l2weaponry.mixin;

import dev.xkmc.l2serial.util.Wrappers;
import dev.xkmc.l2weaponry.content.item.base.BaseClawItem;
import dev.xkmc.l2weaponry.content.item.legendary.LegendaryWeapon;
import dev.xkmc.l2weaponry.content.item.types.ClawItem;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ LivingEntity.class })
public abstract class LivingEntityMixin extends Entity {

    public LivingEntityMixin(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Shadow
    public abstract ItemStack getMainHandItem();

    @Shadow
    public abstract ItemStack getOffhandItem();

    @Inject(at = { @At("HEAD") }, method = { "canFreeze" }, cancellable = true)
    public void l2weaponry_canFreeze_cancelFreeze(CallbackInfoReturnable<Boolean> cir) {
        if (this.getMainHandItem().getItem() instanceof LegendaryWeapon weapon && weapon.cancelFreeze()) {
            cir.setReturnValue(false);
        }
    }

    @Inject(at = { @At("HEAD") }, method = { "isDamageSourceBlocked" }, cancellable = true)
    public void l2weaponry_isDamageSourceBlocked_clawBlock(DamageSource source, CallbackInfoReturnable<Boolean> cir) {
        ItemStack stack = this.getMainHandItem();
        if (stack.getItem() instanceof ClawItem claw) {
            long gameTime = this.m_9236_().getGameTime();
            if ((float) gameTime > (float) BaseClawItem.getLastTime(stack) + claw.getBlockTime((LivingEntity) Wrappers.cast(this))) {
                return;
            }
            Entity entity = source.getDirectEntity();
            boolean flag = false;
            if (entity instanceof AbstractArrow abstractarrow && abstractarrow.getPierceLevel() > 0) {
                flag = true;
            }
            if (!source.is(DamageTypeTags.BYPASSES_ARMOR) && !flag) {
                if (this.getOffhandItem().getItem() == stack.getItem()) {
                    cir.setReturnValue(true);
                    return;
                }
                Vec3 vec32 = source.getSourcePosition();
                if (vec32 != null) {
                    Vec3 vec3 = this.m_20252_(1.0F);
                    Vec3 vec31 = vec32.vectorTo(this.m_20182_()).normalize();
                    vec31 = new Vec3(vec31.x, 0.0, vec31.z);
                    if (vec31.dot(vec3) < 0.0) {
                        cir.setReturnValue(true);
                    }
                }
            }
        }
    }
}