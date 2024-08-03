package io.redspace.ironsspellbooks.spells.lightning;

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
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

@AutoSpellConfig
public class ChargeSpell extends AbstractSpell {

    private final ResourceLocation spellId = new ResourceLocation("irons_spellbooks", "charge");

    private final DefaultConfig defaultConfig = new DefaultConfig().setMinRarity(SpellRarity.RARE).setSchoolResource(SchoolRegistry.LIGHTNING_RESOURCE).setMaxLevel(3).setCooldownSeconds(40.0).build();

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable("ui.irons_spellbooks.effect_length", Utils.timeFromTicks(this.getSpellPower(spellLevel, caster) * 20.0F, 1)), Component.translatable("attribute.modifier.plus.1", Utils.stringTruncation((double) this.getPercentSpeed(spellLevel, caster), 0), Component.translatable("attribute.name.generic.movement_speed")), Component.translatable("attribute.modifier.plus.1", Utils.stringTruncation((double) this.getPercentAttackDamage(spellLevel, caster), 0), Component.translatable("attribute.name.generic.attack_damage")), Component.translatable("attribute.modifier.plus.1", Utils.stringTruncation((double) this.getPercentSpellPower(spellLevel, caster), 0), Component.translatable("attribute.irons_spellbooks.spell_power")));
    }

    public ChargeSpell() {
        this.manaCostPerLevel = 25;
        this.baseSpellPower = 30;
        this.spellPowerPerLevel = 8;
        this.castTime = 0;
        this.baseManaCost = 50;
    }

    @Override
    public CastType getCastType() {
        return CastType.INSTANT;
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
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        entity.addEffect(new MobEffectInstance(MobEffectRegistry.CHARGED.get(), (int) (this.getSpellPower(spellLevel, entity) * 20.0F), spellLevel - 1, false, false, true));
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    private float getPercentAttackDamage(int spellLevel, LivingEntity entity) {
        return (float) spellLevel * 0.1F * 100.0F;
    }

    private float getPercentSpeed(int spellLevel, LivingEntity entity) {
        return (float) spellLevel * 0.2F * 100.0F;
    }

    private float getPercentSpellPower(int spellLevel, LivingEntity entity) {
        return (float) spellLevel * 0.05F * 100.0F;
    }

    @Override
    public AnimationHolder getCastStartAnimation() {
        return SpellAnimations.SELF_CAST_ANIMATION;
    }
}