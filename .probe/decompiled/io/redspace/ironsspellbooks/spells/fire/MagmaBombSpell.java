package io.redspace.ironsspellbooks.spells.fire;

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
import io.redspace.ironsspellbooks.entity.spells.magma_ball.FireBomb;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import java.util.List;
import java.util.Optional;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

@AutoSpellConfig
public class MagmaBombSpell extends AbstractSpell {

    private final ResourceLocation spellId = new ResourceLocation("irons_spellbooks", "magma_bomb");

    private final DefaultConfig defaultConfig = new DefaultConfig().setMinRarity(SpellRarity.UNCOMMON).setSchoolResource(SchoolRegistry.FIRE_RESOURCE).setMaxLevel(8).setCooldownSeconds(12.0).build();

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable("ui.irons_spellbooks.damage", Utils.stringTruncation((double) this.getDamage(spellLevel, caster), 2)), Component.translatable("ui.irons_spellbooks.aoe_damage", Utils.stringTruncation((double) this.getAoeDamage(spellLevel, caster), 1)), Component.translatable("ui.irons_spellbooks.radius", Utils.stringTruncation((double) this.getRadius(spellLevel, caster), 1)));
    }

    public MagmaBombSpell() {
        this.manaCostPerLevel = 5;
        this.baseSpellPower = 8;
        this.spellPowerPerLevel = 3;
        this.castTime = 20;
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
        return Optional.of(SoundRegistry.FIRE_BOMB_CHARGE.get());
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.of(SoundRegistry.FIRE_BOMB_CAST.get());
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        FireBomb orb = new FireBomb(level, entity);
        orb.m_146884_(entity.m_20182_().add(0.0, (double) entity.m_20192_() - orb.m_20191_().getYsize() * 0.5, 0.0).add(entity.m_20156_()));
        orb.shoot(entity.m_20154_());
        orb.m_20256_(orb.m_20184_().add(0.0, 0.2, 0.0));
        orb.setExplosionRadius(this.getRadius(spellLevel, entity));
        orb.setDamage(this.getDamage(spellLevel, entity));
        orb.setAoeDamage(this.getAoeDamage(spellLevel, entity));
        level.m_7967_(orb);
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    public float getRadius(int spellLevel, LivingEntity caster) {
        return 3.0F + this.getEntityPowerMultiplier(caster);
    }

    public float getDamage(int spellLevel, LivingEntity caster) {
        return (float) this.baseSpellPower * this.getEntityPowerMultiplier(caster);
    }

    public float getAoeDamage(int spellLevel, LivingEntity caster) {
        return 1.0F + this.getSpellPower(spellLevel, caster) * 0.1F;
    }

    @Override
    public AnimationHolder getCastStartAnimation() {
        return SpellAnimations.ANIMATION_CHARGED_CAST;
    }
}