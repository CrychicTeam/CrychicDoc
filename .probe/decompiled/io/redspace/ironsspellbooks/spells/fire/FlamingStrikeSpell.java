package io.redspace.ironsspellbooks.spells.fire;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.magic.SpellSelectionManager;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.AutoSpellConfig;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.SpellAnimations;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.damage.SpellDamageSource;
import io.redspace.ironsspellbooks.entity.spells.flame_strike.FlameStrike;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.List;
import java.util.Optional;
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
public class FlamingStrikeSpell extends AbstractSpell {

    private final ResourceLocation spellId = new ResourceLocation("irons_spellbooks", "flaming_strike");

    private final DefaultConfig defaultConfig = new DefaultConfig().setMinRarity(SpellRarity.COMMON).setSchoolResource(SchoolRegistry.FIRE_RESOURCE).setMaxLevel(5).setCooldownSeconds(15.0).build();

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable("ui.irons_spellbooks.damage", this.getDamageText(spellLevel, caster)));
    }

    public FlamingStrikeSpell() {
        this.manaCostPerLevel = 15;
        this.baseSpellPower = 5;
        this.spellPowerPerLevel = 3;
        this.castTime = 10;
        this.baseManaCost = 30;
    }

    @Override
    public Optional<SoundEvent> getCastStartSound() {
        return Optional.of(SoundRegistry.FLAMING_STRIKE_UPSWING.get());
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.of(SoundRegistry.FLAMING_STRIKE_SWING.get());
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
    public boolean canBeInterrupted(@Nullable Player player) {
        return false;
    }

    @Override
    public int getEffectiveCastTime(int spellLevel, @Nullable LivingEntity entity) {
        return this.getCastTime(spellLevel);
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        float radius = 3.25F;
        float distance = 1.65F;
        Vec3 hitLocation = entity.m_20182_().add(0.0, (double) (entity.m_20206_() * 0.3F), 0.0).add(entity.m_20156_().multiply((double) distance, 0.35F, (double) distance));
        for (Entity targetEntity : level.m_45933_(entity, AABB.ofSize(hitLocation, (double) (radius * 2.0F), (double) radius, (double) (radius * 2.0F)))) {
            if (entity.isPickable() && entity.m_20280_(targetEntity) < (double) (radius * radius) && Utils.hasLineOfSight(level, entity.m_146892_(), targetEntity.getBoundingBox().getCenter(), true) && DamageSources.applyDamage(targetEntity, this.getDamage(spellLevel, entity), this.getDamageSource(entity))) {
                MagicManager.spawnParticles(level, ParticleHelper.EMBERS, targetEntity.getX(), targetEntity.getY() + (double) (targetEntity.getBbHeight() * 0.5F), targetEntity.getZ(), 50, (double) (targetEntity.getBbWidth() * 0.5F), (double) (targetEntity.getBbHeight() * 0.5F), (double) (targetEntity.getBbWidth() * 0.5F), 0.03, false);
                EnchantmentHelper.doPostDamageEffects(entity, targetEntity);
            }
        }
        boolean mirrored = false;
        if (entity instanceof Player player) {
            SpellSelectionManager.SelectionOption selection = new SpellSelectionManager(player).getSelection();
            new SpellSelectionManager(player).getSelection();
            if (selection != null) {
                mirrored = selection.slot.equals(SpellSelectionManager.OFFHAND);
            }
        }
        FlameStrike flameStrike = new FlameStrike(level, mirrored);
        flameStrike.m_20219_(hitLocation);
        flameStrike.m_146922_(entity.m_146908_());
        level.m_7967_(flameStrike);
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    @Override
    public SpellDamageSource getDamageSource(Entity projectile, Entity attacker) {
        return super.getDamageSource(projectile, attacker).setFireTime(3);
    }

    private float getDamage(int spellLevel, LivingEntity entity) {
        return this.getSpellPower(spellLevel, entity) + Utils.getWeaponDamage(entity, MobType.UNDEFINED) + (float) EnchantmentHelper.getFireAspect(entity);
    }

    private String getDamageText(int spellLevel, LivingEntity entity) {
        if (entity != null) {
            float weaponDamage = Utils.getWeaponDamage(entity, MobType.UNDEFINED);
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
        return SpellAnimations.ONE_HANDED_HORIZONTAL_SWING_ANIMATION;
    }

    @Override
    public AnimationHolder getCastFinishAnimation() {
        return AnimationHolder.pass();
    }
}