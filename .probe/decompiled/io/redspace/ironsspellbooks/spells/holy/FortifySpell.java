package io.redspace.ironsspellbooks.spells.holy;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.AutoSpellConfig;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.spells.target_area.TargetedAreaEntity;
import io.redspace.ironsspellbooks.network.spell.ClientboundAborptionParticles;
import io.redspace.ironsspellbooks.network.spell.ClientboundFortifyAreaParticles;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.setup.Messages;
import io.redspace.ironsspellbooks.spells.TargetAreaCastData;
import java.util.List;
import java.util.Optional;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

@AutoSpellConfig
public class FortifySpell extends AbstractSpell {

    private final ResourceLocation spellId = new ResourceLocation("irons_spellbooks", "fortify");

    public static final float radius = 16.0F;

    private final DefaultConfig defaultConfig = new DefaultConfig().setMinRarity(SpellRarity.COMMON).setSchoolResource(SchoolRegistry.HOLY_RESOURCE).setMaxLevel(10).setCooldownSeconds(35.0).build();

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable("ui.irons_spellbooks.absorption", Utils.stringTruncation((double) this.getSpellPower(spellLevel, caster), 0)), Component.translatable("ui.irons_spellbooks.radius", Utils.stringTruncation(16.0, 1)));
    }

    public FortifySpell() {
        this.manaCostPerLevel = 5;
        this.baseSpellPower = 6;
        this.spellPowerPerLevel = 1;
        this.castTime = 40;
        this.baseManaCost = 40;
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
        return Optional.of(SoundRegistry.CLOUD_OF_REGEN_LOOP.get());
    }

    @Override
    public void onServerPreCast(Level level, int spellLevel, LivingEntity entity, @Nullable MagicData playerMagicData) {
        super.onServerPreCast(level, spellLevel, entity, playerMagicData);
        if (playerMagicData != null) {
            TargetedAreaEntity targetedAreaEntity = TargetedAreaEntity.createTargetAreaEntity(level, entity.m_20182_(), 16.0F, 16239960, entity);
            playerMagicData.setAdditionalCastData(new TargetAreaCastData(entity.m_20182_(), targetedAreaEntity));
        }
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        level.m_45976_(LivingEntity.class, new AABB(entity.m_20182_().subtract(16.0, 16.0, 16.0), entity.m_20182_().add(16.0, 16.0, 16.0))).forEach(target -> {
            if (Utils.shouldHealEntity(entity, target) && entity.m_20270_(target) <= 16.0F) {
                target.addEffect(new MobEffectInstance(MobEffectRegistry.FORTIFY.get(), 2400, (int) this.getSpellPower(spellLevel, entity) - 1, false, false, true));
                Messages.sendToPlayersTrackingEntity(new ClientboundAborptionParticles(target.m_20182_()), entity, true);
            }
        });
        Messages.sendToPlayersTrackingEntity(new ClientboundFortifyAreaParticles(entity.m_20182_()), entity, true);
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }
}