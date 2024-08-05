package io.redspace.ironsspellbooks.spells.holy;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.AutoSpellConfig;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.SpellAnimations;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.CameraShakeData;
import io.redspace.ironsspellbooks.api.util.CameraShakeManager;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.particle.ShockwaveParticleOptions;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

@AutoSpellConfig
public class DivineSmiteSpell extends AbstractSpell {

    private final ResourceLocation spellId = new ResourceLocation("irons_spellbooks", "divine_smite");

    private final DefaultConfig defaultConfig = new DefaultConfig().setMinRarity(SpellRarity.COMMON).setSchoolResource(SchoolRegistry.HOLY_RESOURCE).setMaxLevel(5).setCooldownSeconds(15.0).build();

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable("ui.irons_spellbooks.damage", this.getDamageText(spellLevel, caster)));
    }

    public DivineSmiteSpell() {
        this.manaCostPerLevel = 15;
        this.baseSpellPower = 5;
        this.spellPowerPerLevel = 3;
        this.castTime = 16;
        this.baseManaCost = 30;
    }

    @Override
    public boolean canBeInterrupted(Player player) {
        return false;
    }

    @Override
    public int getEffectiveCastTime(int spellLevel, @Nullable LivingEntity entity) {
        return this.getCastTime(spellLevel);
    }

    @Override
    public CastType getCastType() {
        return CastType.LONG;
    }

    @Override
    public DefaultConfig getDefaultConfig() {
        return this.defaultConfig;
    }

    @Override
    public ResourceLocation getSpellResource() {
        return this.spellId;
    }

    @Override
    public Optional<SoundEvent> getCastStartSound() {
        return Optional.of(SoundRegistry.DIVINE_SMITE_WINDUP.get());
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.of(SoundRegistry.DIVINE_SMITE_CAST.get());
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        float radius = 1.75F;
        Vec3 smiteLocation = Utils.moveToRelativeGroundLevel(level, entity.m_146892_().add(entity.m_20156_().multiply(1.35F, 0.0, 1.35F)), 1);
        MagicManager.spawnParticles(level, new ShockwaveParticleOptions(SchoolRegistry.HOLY.get().getTargetingColor(), radius * 2.0F, true), smiteLocation.x, smiteLocation.y, smiteLocation.z, 1, 0.0, 0.0, 0.0, 0.0, true);
        MagicManager.spawnParticles(level, ParticleTypes.ELECTRIC_SPARK, smiteLocation.x, smiteLocation.y, smiteLocation.z, 50, 0.0, 0.0, 0.0, 1.0, false);
        CameraShakeManager.addCameraShake(new CameraShakeData(10, smiteLocation, 10.0F));
        for (Entity targetEntity : level.m_45933_(entity, AABB.ofSize(smiteLocation, (double) (radius * 2.0F), (double) (radius * 4.0F), (double) (radius * 2.0F)))) {
            if (targetEntity.isAlive() && targetEntity.isPickable() && Utils.hasLineOfSight(level, smiteLocation.add(0.0, 1.0, 0.0), targetEntity.getBoundingBox().getCenter(), true) && DamageSources.applyDamage(targetEntity, this.getDamage(spellLevel, entity), this.getDamageSource(entity))) {
                int i = EnchantmentHelper.getFireAspect(entity);
                if (i > 0) {
                    targetEntity.setSecondsOnFire(i * 4);
                }
                EnchantmentHelper.doPostDamageEffects(entity, targetEntity);
            }
        }
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    private float getDamage(int spellLevel, LivingEntity entity) {
        return this.getSpellPower(spellLevel, entity) + Utils.getWeaponDamage(entity, MobType.UNDEAD);
    }

    private String getDamageText(int spellLevel, LivingEntity entity) {
        if (entity != null) {
            float weaponDamage = Utils.getWeaponDamage(entity, MobType.UNDEAD);
            String plus = "";
            if (weaponDamage > 0.0F) {
                plus = String.format(" (+%s)", Utils.stringTruncation((double) weaponDamage, 1));
            }
            String damage = Utils.stringTruncation((double) this.getDamage(spellLevel, entity), 1);
            return damage + plus;
        } else {
            return this.getSpellPower(spellLevel, entity) + "";
        }
    }

    @Override
    public AnimationHolder getCastStartAnimation() {
        return SpellAnimations.OVERHEAD_MELEE_SWING_ANIMATION;
    }

    @Override
    public AnimationHolder getCastFinishAnimation() {
        return AnimationHolder.pass();
    }
}