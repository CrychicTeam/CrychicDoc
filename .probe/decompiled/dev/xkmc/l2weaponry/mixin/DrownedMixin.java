package dev.xkmc.l2weaponry.mixin;

import dev.xkmc.l2weaponry.content.entity.BaseThrownWeaponEntity;
import dev.xkmc.l2weaponry.content.item.base.BaseThrowableWeaponItem;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ Drowned.class })
public abstract class DrownedMixin extends Zombie {

    public DrownedMixin(Level level) {
        super(level);
    }

    @Inject(at = { @At("HEAD") }, method = { "performRangedAttack" }, cancellable = true)
    public void l2weaponry$performRangedAttack$throwOtherWeapon(LivingEntity target, float f, CallbackInfo ci) {
        ItemStack stack = this.m_21205_();
        if (stack.getItem() instanceof BaseThrowableWeaponItem item) {
            BaseThrownWeaponEntity<? extends BaseThrownWeaponEntity<?>> ans = (BaseThrownWeaponEntity<? extends BaseThrownWeaponEntity<?>>) item.getProjectile(this.m_9236_(), this, stack, 0);
            ans.m_36781_(this.m_21133_(Attributes.ATTACK_DAMAGE));
            ans.getPersistentData().putInt("DespawnFactor", 20);
            double d0 = target.m_20185_() - this.m_20185_();
            double d1 = target.m_20227_(0.3333333333333333) - ans.m_20186_();
            double d2 = target.m_20189_() - this.m_20189_();
            double d3 = Math.sqrt(d0 * d0 + d2 * d2);
            ans.m_6686_(d0, d1 + d3 * 0.2F, d2, 1.6F, (float) (14 - this.m_9236_().m_46791_().getId() * 4));
            this.m_5496_(SoundEvents.DROWNED_SHOOT, 1.0F, 1.0F / (this.m_217043_().nextFloat() * 0.4F + 0.8F));
            this.m_9236_().m_7967_(ans);
            ci.cancel();
        }
    }
}