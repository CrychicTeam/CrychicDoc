package io.redspace.ironsspellbooks.spells.lightning;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.AutoSpellConfig;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.particle.ZapParticleOption;
import io.redspace.ironsspellbooks.spells.ender.TeleportSpell;
import java.util.List;
import java.util.Optional;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

@AutoSpellConfig
public class ThunderStepSpell extends AbstractSpell {

    private final ResourceLocation spellId = new ResourceLocation("irons_spellbooks", "thunder_step");

    private final DefaultConfig defaultConfig = new DefaultConfig().setMinRarity(SpellRarity.UNCOMMON).setSchoolResource(SchoolRegistry.LIGHTNING_RESOURCE).setMaxLevel(5).setCooldownSeconds(8.0).build();

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable("ui.irons_spellbooks.damage", Utils.stringTruncation((double) this.getSpellPower(spellLevel, caster), 1)));
    }

    public ThunderStepSpell() {
        this.manaCostPerLevel = 15;
        this.baseSpellPower = 10;
        this.spellPowerPerLevel = 2;
        this.castTime = 0;
        this.baseManaCost = 75;
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
    public Optional<SoundEvent> getCastStartSound() {
        return Optional.empty();
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.of(SoundEvents.ILLUSIONER_PREPARE_BLINDNESS);
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        TeleportSpell.TeleportData teleportData = (TeleportSpell.TeleportData) playerMagicData.getAdditionalCastData();
        Vec3 dest = null;
        if (teleportData != null) {
            Vec3 potentialTarget = teleportData.getTeleportTargetPosition();
            if (potentialTarget != null) {
                dest = potentialTarget;
            }
        }
        if (dest == null) {
            dest = TeleportSpell.findTeleportLocation(level, entity, this.getDistance(spellLevel, entity));
        }
        this.zapEntitiesBetween(entity, spellLevel, dest);
        Vec3 travel = dest.subtract(entity.m_20182_());
        for (int i = 0; i < 7; i++) {
            Vec3 random1 = Utils.getRandomVec3(0.5).multiply((double) entity.m_20205_(), (double) entity.m_20206_(), (double) entity.m_20205_());
            Vec3 random2 = Utils.getRandomVec3(0.8F).multiply((double) entity.m_20205_(), (double) entity.m_20206_(), (double) entity.m_20205_());
            float yOffset = (float) i / 7.0F * entity.m_20206_();
            Vec3 midpoint = entity.m_20182_().add(travel.scale(0.5)).add(random2);
            ((ServerLevel) level).sendParticles(new ZapParticleOption(random1.add(entity.m_20185_(), entity.m_20186_() + (double) yOffset, entity.m_20189_())), midpoint.x, midpoint.y, midpoint.z, 1, 0.0, 0.0, 0.0, 0.0);
            ((ServerLevel) level).sendParticles(new ZapParticleOption(random1.scale(-1.0).add(dest.x, dest.y + (double) yOffset, dest.z)), midpoint.x, midpoint.y, midpoint.z, 1, 0.0, 0.0, 0.0, 0.0);
        }
        if (entity.m_20159_()) {
            entity.stopRiding();
        }
        entity.m_6021_(dest.x, dest.y, dest.z);
        entity.m_183634_();
        playerMagicData.resetAdditionalCastData();
        entity.m_5496_((SoundEvent) this.getCastFinishSound().get(), 2.0F, 1.0F);
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    private void zapEntitiesBetween(LivingEntity caster, int spellLevel, Vec3 blockEnd) {
        Vec3 start = caster.m_146892_();
        Vec3 end = blockEnd.add(0.0, (double) caster.m_20192_(), 0.0);
        AABB range = caster.m_20191_().expandTowards(end.subtract(start));
        for (Entity target : caster.f_19853_.m_45933_(caster, range)) {
            Vec3 height = new Vec3(0.0, (double) caster.m_20192_(), 0.0);
            if (Utils.checkEntityIntersecting(target, start, end, 1.0F).getType() != HitResult.Type.MISS || Utils.checkEntityIntersecting(target, start.subtract(height), end.subtract(height), 1.0F).getType() != HitResult.Type.MISS) {
                DamageSources.applyDamage(target, this.getDamage(spellLevel, caster), this.getDamageSource(caster));
            }
        }
    }

    private float getDistance(int spellLevel, LivingEntity sourceEntity) {
        return this.getSpellPower(spellLevel, sourceEntity);
    }

    private float getDamage(int spellLevel, LivingEntity sourceEntity) {
        return this.getSpellPower(spellLevel, sourceEntity);
    }
}