package io.redspace.ironsspellbooks.spells.holy;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.events.SpellHealEvent;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.AutoSpellConfig;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.network.spell.ClientboundHealParticles;
import io.redspace.ironsspellbooks.network.spell.ClientboundRegenCloudParticles;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.setup.Messages;
import java.util.List;
import java.util.Optional;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;

@AutoSpellConfig
public class CloudOfRegenerationSpell extends AbstractSpell {

    private final ResourceLocation spellId = new ResourceLocation("irons_spellbooks", "cloud_of_regeneration");

    public static final float radius = 5.0F;

    private final DefaultConfig defaultConfig = new DefaultConfig().setMinRarity(SpellRarity.COMMON).setSchoolResource(SchoolRegistry.HOLY_RESOURCE).setMaxLevel(5).setCooldownSeconds(35.0).setDeprecated(true).build();

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable("ui.irons_spellbooks.healing", Utils.stringTruncation((double) this.getHealing(spellLevel, caster), 1)), Component.translatable("ui.irons_spellbooks.radius", Utils.stringTruncation(5.0, 1)));
    }

    public CloudOfRegenerationSpell() {
        this.manaCostPerLevel = 3;
        this.baseSpellPower = 2;
        this.spellPowerPerLevel = 1;
        this.castTime = 200;
        this.baseManaCost = 10;
    }

    @Override
    public CastType getCastType() {
        return CastType.CONTINUOUS;
    }

    @Override
    public DefaultConfig getDefaultConfig() {
        return this.defaultConfig;
    }

    @Override
    public ResourceLocation getSpellResource() {
        return this.spellId;
    }

    private float getHealing(int spellLevel, LivingEntity caster) {
        return this.getSpellPower(spellLevel, caster) * 0.5F;
    }

    @Override
    public Optional<SoundEvent> getCastStartSound() {
        return Optional.of(SoundRegistry.HOLY_CAST.get());
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.of(SoundRegistry.CLOUD_OF_REGEN_LOOP.get());
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        level.m_45976_(LivingEntity.class, entity.m_20191_().inflate(5.0)).forEach(target -> {
            if (target.m_20238_(entity.m_20182_()) < 25.0 && Utils.shouldHealEntity(entity, target)) {
                float healAmount = this.getHealing(spellLevel, entity);
                MinecraftForge.EVENT_BUS.post(new SpellHealEvent(entity, target, healAmount, this.getSchoolType()));
                target.heal(healAmount);
                Messages.sendToPlayersTrackingEntity(new ClientboundHealParticles(target.m_20182_()), entity, true);
            }
        });
        Messages.sendToPlayersTrackingEntity(new ClientboundRegenCloudParticles(entity.m_20182_()), entity, true);
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }
}