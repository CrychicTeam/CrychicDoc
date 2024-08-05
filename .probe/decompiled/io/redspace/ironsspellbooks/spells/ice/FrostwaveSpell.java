package io.redspace.ironsspellbooks.spells.ice;

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
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.network.spell.ClientboundParticleShockwave;
import io.redspace.ironsspellbooks.particle.BlastwaveParticleOptions;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import io.redspace.ironsspellbooks.registries.ParticleRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.setup.Messages;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.List;
import java.util.Optional;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

@AutoSpellConfig
public class FrostwaveSpell extends AbstractSpell {

    private final ResourceLocation spellId = new ResourceLocation("irons_spellbooks", "frostwave");

    private final DefaultConfig defaultConfig = new DefaultConfig().setMinRarity(SpellRarity.COMMON).setSchoolResource(SchoolRegistry.ICE_RESOURCE).setMaxLevel(8).setCooldownSeconds(45.0).build();

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable("ui.irons_spellbooks.effect_length", Utils.timeFromTicks((float) this.getDuration(spellLevel, caster), 2)), Component.translatable("ui.irons_spellbooks.radius", Utils.stringTruncation((double) this.getRadius(spellLevel, caster), 2)));
    }

    public FrostwaveSpell() {
        this.manaCostPerLevel = 5;
        this.baseSpellPower = 10;
        this.spellPowerPerLevel = 3;
        this.castTime = 20;
        this.baseManaCost = 50;
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
        return Optional.of(SoundRegistry.FROSTWAVE_PREPARE.get());
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        float radius = this.getRadius(spellLevel, entity);
        MagicManager.spawnParticles(level, new BlastwaveParticleOptions(SchoolRegistry.ICE.get().getTargetingColor(), radius), entity.m_20185_(), entity.m_20186_() + 0.165F, entity.m_20189_(), 1, 0.0, 0.0, 0.0, 0.0, true);
        Messages.sendToPlayersTrackingEntity(new ClientboundParticleShockwave(new Vec3(entity.m_20185_(), entity.m_20186_() + 0.165F, entity.m_20189_()), radius, ParticleRegistry.SNOWFLAKE_PARTICLE.get()), entity, true);
        level.getEntities(entity, entity.m_20191_().inflate((double) radius, 4.0, (double) radius), target -> !DamageSources.isFriendlyFireBetween(target, entity) && Utils.hasLineOfSight(level, entity, target, true)).forEach(target -> {
            if (target instanceof LivingEntity livingEntity && livingEntity.m_20280_(entity) < (double) (radius * radius)) {
                livingEntity.addEffect(new MobEffectInstance(MobEffectRegistry.CHILLED.get(), this.getDuration(spellLevel, entity)));
                MagicManager.spawnParticles(level, ParticleHelper.SNOWFLAKE, livingEntity.m_20185_(), livingEntity.m_20186_() + (double) (livingEntity.m_20206_() * 0.5F), livingEntity.m_20189_(), 50, (double) (livingEntity.m_20205_() * 0.5F), (double) (livingEntity.m_20206_() * 0.5F), (double) (livingEntity.m_20205_() * 0.5F), 0.03, false);
            }
        });
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    public float getRadius(int spellLevel, LivingEntity caster) {
        return 6.0F + (float) spellLevel * 0.75F;
    }

    public int getDuration(int spellLevel, LivingEntity caster) {
        return (int) (this.getSpellPower(spellLevel, caster) * 20.0F);
    }

    @Override
    public AnimationHolder getCastStartAnimation() {
        return SpellAnimations.CHARGE_RAISED_HAND;
    }

    @Override
    public AnimationHolder getCastFinishAnimation() {
        return SpellAnimations.TOUCH_GROUND_ANIMATION;
    }
}