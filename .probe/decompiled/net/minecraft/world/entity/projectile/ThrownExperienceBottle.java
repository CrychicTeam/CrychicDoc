package net.minecraft.world.entity.projectile;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

public class ThrownExperienceBottle extends ThrowableItemProjectile {

    public ThrownExperienceBottle(EntityType<? extends ThrownExperienceBottle> entityTypeExtendsThrownExperienceBottle0, Level level1) {
        super(entityTypeExtendsThrownExperienceBottle0, level1);
    }

    public ThrownExperienceBottle(Level level0, LivingEntity livingEntity1) {
        super(EntityType.EXPERIENCE_BOTTLE, livingEntity1, level0);
    }

    public ThrownExperienceBottle(Level level0, double double1, double double2, double double3) {
        super(EntityType.EXPERIENCE_BOTTLE, double1, double2, double3, level0);
    }

    @Override
    protected Item getDefaultItem() {
        return Items.EXPERIENCE_BOTTLE;
    }

    @Override
    protected float getGravity() {
        return 0.07F;
    }

    @Override
    protected void onHit(HitResult hitResult0) {
        super.m_6532_(hitResult0);
        if (this.m_9236_() instanceof ServerLevel) {
            this.m_9236_().m_46796_(2002, this.m_20183_(), PotionUtils.getColor(Potions.WATER));
            int $$1 = 3 + this.m_9236_().random.nextInt(5) + this.m_9236_().random.nextInt(5);
            ExperienceOrb.award((ServerLevel) this.m_9236_(), this.m_20182_(), $$1);
            this.m_146870_();
        }
    }
}