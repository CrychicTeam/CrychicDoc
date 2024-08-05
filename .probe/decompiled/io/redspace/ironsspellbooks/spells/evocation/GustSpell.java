package io.redspace.ironsspellbooks.spells.evocation;

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
import io.redspace.ironsspellbooks.effect.AirborneEffect;
import io.redspace.ironsspellbooks.entity.spells.gust.GustCollider;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import java.util.List;
import java.util.Optional;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;

@AutoSpellConfig
public class GustSpell extends AbstractSpell {

    private final ResourceLocation spellId = new ResourceLocation("irons_spellbooks", "gust");

    private final DefaultConfig defaultConfig = new DefaultConfig().setMinRarity(SpellRarity.UNCOMMON).setSchoolResource(SchoolRegistry.EVOCATION_RESOURCE).setMaxLevel(10).setCooldownSeconds(12.0).build();

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable("ui.irons_spellbooks.strength", String.format("%s%%", (int) (this.getStrength(spellLevel, caster) * 100.0F / this.getStrength(1, null)))), Component.translatable("ui.irons_spellbooks.impact_damage", Utils.stringTruncation((double) AirborneEffect.getDamageFromLevel(spellLevel), 1)));
    }

    public GustSpell() {
        this.manaCostPerLevel = 5;
        this.baseSpellPower = 10;
        this.spellPowerPerLevel = 1;
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
        return Optional.of(SoundRegistry.GUST_CHARGE.get());
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.of(SoundRegistry.GUST_CAST.get());
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        float range = this.getRange(spellLevel, entity);
        float strength = this.getStrength(spellLevel, entity);
        GustCollider gust = new GustCollider(level, entity);
        gust.m_146884_(entity.m_20182_().add(0.0, (double) entity.m_20192_() * 0.7, 0.0).add(entity.m_20156_().normalize().scale(2.0)));
        gust.range = range;
        gust.strength = strength;
        gust.amplifier = spellLevel - 1;
        level.m_7967_(gust);
        gust.setDealDamageActive();
        gust.tick();
        float kickback = (float) entity.m_20191_().getCenter().distanceToSqr(Utils.getTargetBlock(level, entity, ClipContext.Fluid.NONE, 3.5).m_82450_());
        kickback = Mth.clamp(1.0F / (kickback + 1.0F) - 0.11F, 0.0F, 0.95F);
        if (kickback > 0.0F) {
            entity.m_20256_(entity.m_20184_().subtract(entity.m_20154_().scale((double) (kickback * (float) spellLevel * 0.25F))));
            entity.m_183634_();
            entity.f_19864_ = true;
        }
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    public float getRange(int spellLevel, LivingEntity caster) {
        return 8.0F;
    }

    public float getStrength(int spellLevel, LivingEntity caster) {
        return this.getSpellPower(spellLevel, caster) * 0.2F;
    }

    public float getDamage(int spellLevel, LivingEntity caster) {
        return this.getSpellPower(spellLevel, caster);
    }

    @Override
    public AnimationHolder getCastStartAnimation() {
        return SpellAnimations.CHARGE_WAVY_ANIMATION;
    }

    @Override
    public AnimationHolder getCastFinishAnimation() {
        return SpellAnimations.ANIMATION_LONG_CAST_FINISH;
    }

    @Override
    public boolean shouldAIStopCasting(int spellLevel, Mob mob, LivingEntity target) {
        return target.m_20280_(mob) > (double) (this.getRange(spellLevel, mob) * this.getRange(spellLevel, mob)) * 1.25;
    }
}