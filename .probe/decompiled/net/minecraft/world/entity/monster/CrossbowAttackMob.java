package net.minecraft.world.entity.monster;

import javax.annotation.Nullable;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public interface CrossbowAttackMob extends RangedAttackMob {

    void setChargingCrossbow(boolean var1);

    void shootCrossbowProjectile(LivingEntity var1, ItemStack var2, Projectile var3, float var4);

    @Nullable
    LivingEntity getTarget();

    void onCrossbowAttackPerformed();

    default void performCrossbowAttack(LivingEntity livingEntity0, float float1) {
        InteractionHand $$2 = ProjectileUtil.getWeaponHoldingHand(livingEntity0, Items.CROSSBOW);
        ItemStack $$3 = livingEntity0.getItemInHand($$2);
        if (livingEntity0.isHolding(Items.CROSSBOW)) {
            CrossbowItem.performShooting(livingEntity0.m_9236_(), livingEntity0, $$2, $$3, float1, (float) (14 - livingEntity0.m_9236_().m_46791_().getId() * 4));
        }
        this.onCrossbowAttackPerformed();
    }

    default void shootCrossbowProjectile(LivingEntity livingEntity0, LivingEntity livingEntity1, Projectile projectile2, float float3, float float4) {
        double $$5 = livingEntity1.m_20185_() - livingEntity0.m_20185_();
        double $$6 = livingEntity1.m_20189_() - livingEntity0.m_20189_();
        double $$7 = Math.sqrt($$5 * $$5 + $$6 * $$6);
        double $$8 = livingEntity1.m_20227_(0.3333333333333333) - projectile2.m_20186_() + $$7 * 0.2F;
        Vector3f $$9 = this.getProjectileShotVector(livingEntity0, new Vec3($$5, $$8, $$6), float3);
        projectile2.shoot((double) $$9.x(), (double) $$9.y(), (double) $$9.z(), float4, (float) (14 - livingEntity0.m_9236_().m_46791_().getId() * 4));
        livingEntity0.m_5496_(SoundEvents.CROSSBOW_SHOOT, 1.0F, 1.0F / (livingEntity0.getRandom().nextFloat() * 0.4F + 0.8F));
    }

    default Vector3f getProjectileShotVector(LivingEntity livingEntity0, Vec3 vec1, float float2) {
        Vector3f $$3 = vec1.toVector3f().normalize();
        Vector3f $$4 = new Vector3f($$3).cross(new Vector3f(0.0F, 1.0F, 0.0F));
        if ((double) $$4.lengthSquared() <= 1.0E-7) {
            Vec3 $$5 = livingEntity0.m_20289_(1.0F);
            $$4 = new Vector3f($$3).cross($$5.toVector3f());
        }
        Vector3f $$6 = new Vector3f($$3).rotateAxis((float) (Math.PI / 2), $$4.x, $$4.y, $$4.z);
        return new Vector3f($$3).rotateAxis(float2 * (float) (Math.PI / 180.0), $$6.x, $$6.y, $$6.z);
    }
}