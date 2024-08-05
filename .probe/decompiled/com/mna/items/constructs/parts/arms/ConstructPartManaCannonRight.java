package com.mna.items.constructs.parts.arms;

import com.mna.api.affinity.Affinity;
import com.mna.api.entities.construct.ConstructCapability;
import com.mna.api.entities.construct.ConstructMaterial;
import com.mna.api.entities.construct.ConstructSlot;
import com.mna.api.sound.SFX;
import com.mna.entities.sorcery.targeting.SpellProjectile;
import com.mna.items.constructs.parts.base.ChargeableConstructPart;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ConstructPartManaCannonRight extends ChargeableConstructPart {

    public ConstructPartManaCannonRight(ConstructMaterial material) {
        super(material, ConstructSlot.RIGHT_ARM, 16);
    }

    @Override
    public ConstructCapability[] getEnabledCapabilities() {
        return new ConstructCapability[] { ConstructCapability.RANGED_ATTACK };
    }

    @Override
    public float getAttackDamage() {
        return 0.0F;
    }

    @Override
    public int getAttackSpeedModifier() {
        return 10;
    }

    @Override
    public float getRangedAttackDamage() {
        return this.getMaterial().getRangedDamageBonus();
    }

    @Override
    public float getChargeCost() {
        return this.getMaterial().getRangedManaCost();
    }

    @Override
    public void onChargeReleased(ItemStack stack, Level world, LivingEntity living) {
        SpellProjectile esp = new SpellProjectile(living, world);
        esp.setForcedDamageAffinityAndTarget(Affinity.ARCANE, this.getRangedAttackDamage(), null);
        esp.shoot(living, living.m_20156_(), 1.0F, 0.0F);
        world.m_7967_(esp);
        world.playSound(null, living.m_20185_(), living.m_20186_(), living.m_20189_(), SFX.Spell.Cast.ForAffinity(Affinity.ARCANE), SoundSource.NEUTRAL, 0.25F, (float) (0.9 + Math.random() * 0.1));
    }
}