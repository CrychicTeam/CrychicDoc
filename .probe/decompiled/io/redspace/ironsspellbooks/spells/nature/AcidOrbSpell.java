package io.redspace.ironsspellbooks.spells.nature;

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
import io.redspace.ironsspellbooks.entity.spells.acid_orb.AcidOrb;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import java.util.List;
import java.util.Optional;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

@AutoSpellConfig
public class AcidOrbSpell extends AbstractSpell {

    private final ResourceLocation spellId = new ResourceLocation("irons_spellbooks", "acid_orb");

    private final DefaultConfig defaultConfig = new DefaultConfig().setMinRarity(SpellRarity.COMMON).setSchoolResource(SchoolRegistry.NATURE_RESOURCE).setMaxLevel(10).setCooldownSeconds(15.0).build();

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable("ui.irons_spellbooks.radius", Utils.stringTruncation((double) this.getRadius(spellLevel, caster), 1)), Component.translatable("ui.irons_spellbooks.rend", Utils.stringTruncation((double) ((this.getRendAmplifier(spellLevel, caster) + 1) * 5), 1)), Component.translatable("ui.irons_spellbooks.effect_length", Utils.timeFromTicks((float) this.getRendDuration(spellLevel, caster), 1)));
    }

    public AcidOrbSpell() {
        this.manaCostPerLevel = 3;
        this.baseSpellPower = 1;
        this.spellPowerPerLevel = 0;
        this.castTime = 15;
        this.baseManaCost = 30;
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
        return Optional.of(SoundRegistry.ACID_ORB_CHARGE.get());
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.of(SoundRegistry.ACID_ORB_CAST.get());
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        AcidOrb orb = new AcidOrb(level, entity);
        orb.m_146884_(entity.m_20182_().add(0.0, (double) entity.m_20192_() - orb.m_20191_().getYsize() * 0.5, 0.0).add(entity.m_20156_()));
        orb.shoot(entity.m_20154_());
        orb.m_20256_(orb.m_20184_().add(0.0, 0.2, 0.0));
        orb.setExplosionRadius(this.getRadius(spellLevel, entity));
        orb.setRendLevel(this.getRendAmplifier(spellLevel, entity));
        orb.setRendDuration(this.getRendDuration(spellLevel, entity));
        level.m_7967_(orb);
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    public float getRadius(int spellLevel, LivingEntity caster) {
        return this.getSpellPower(spellLevel, caster) * 3.0F;
    }

    public int getRendAmplifier(int spellLevel, LivingEntity caster) {
        return (int) (this.getSpellPower(spellLevel, caster) * (float) spellLevel - 1.0F);
    }

    public int getRendDuration(int spellLevel, LivingEntity caster) {
        return (int) (this.getSpellPower(spellLevel, caster) * 20.0F * 15.0F);
    }

    @Override
    public AnimationHolder getCastStartAnimation() {
        return SpellAnimations.CHARGE_SPIT_ANIMATION;
    }

    @Override
    public AnimationHolder getCastFinishAnimation() {
        return SpellAnimations.SPIT_FINISH_ANIMATION;
    }

    @Override
    public boolean shouldAIStopCasting(int spellLevel, Mob mob, LivingEntity target) {
        return target.getAttributeValue(Attributes.ARMOR) < 4.0;
    }
}