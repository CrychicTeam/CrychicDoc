package io.redspace.ironsspellbooks.spells.ice;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.AutoSpellConfig;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.capabilities.magic.TargetEntityCastData;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.icicle.IcicleProjectile;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

@AutoSpellConfig
public class FrostbiteSpell extends AbstractSpell {

    private final ResourceLocation spellId = new ResourceLocation("irons_spellbooks", "frostbite");

    private final DefaultConfig defaultConfig = new DefaultConfig().setMinRarity(SpellRarity.COMMON).setSchoolResource(SchoolRegistry.ICE_RESOURCE).setMaxLevel(5).setCooldownSeconds(0.0).build();

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable("ui.irons_spellbooks.percent_damage", Utils.stringTruncation((double) (this.getPercentDamage(spellLevel, caster) * 100.0F), 1)));
    }

    public FrostbiteSpell() {
        this.manaCostPerLevel = 50;
        this.baseSpellPower = 75;
        this.spellPowerPerLevel = 15;
        this.castTime = 40;
        this.baseManaCost = 100;
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
    public boolean checkPreCastConditions(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData) {
        return Utils.preCastTargetHelper(level, entity, playerMagicData, this, 48, 0.15F);
    }

    @Override
    public void onServerCastTick(Level level, int spellLevel, LivingEntity entity, @Nullable MagicData playerMagicData) {
        if (playerMagicData != null && playerMagicData.getAdditionalCastData() instanceof TargetEntityCastData targetingData) {
            LivingEntity target = targetingData.getTarget((ServerLevel) level);
            if (target != null) {
                float i = playerMagicData.getCastCompletionPercent();
                float f = (float) entity.f_19797_;
                float distance = entity.m_20270_(target);
                float density = 2.0F;
                Vec3 start = target.m_20182_().add(0.0, (double) (entity.m_20206_() * 0.5F), 0.0);
                Vec3 delta = entity.m_20182_().subtract(target.m_20182_()).normalize();
                start = start.add(delta.scale((double) (i * distance)));
                MagicManager.spawnParticles(level, ParticleHelper.SNOWFLAKE, start.x + (double) (this.beamNoise((f + i + 50.0F) * 1.25F) * 0.25F), start.y + (double) (this.beamNoise((f + i) * 1.5F) * 0.25F), start.z + (double) (this.beamNoise((f + i + 84.0F) * 1.25F) * 0.25F), 1, 0.0, 0.0, 0.0, 0.0, false);
            }
        }
        super.onServerCastTick(level, spellLevel, entity, playerMagicData);
    }

    private float beamNoise(float f) {
        f %= 360.0F;
        return (Mth.cos(f * 0.25F) * 2.0F + Mth.sin(f) + Mth.cos(2.0F * f) * 0.25F) * 0.4F;
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        if (playerMagicData.getAdditionalCastData() instanceof TargetEntityCastData targetingData) {
            LivingEntity target = targetingData.getTarget((ServerLevel) level);
            if (target != null) {
                float damage = this.getPercentDamage(spellLevel, entity) * (float) target.m_146888_() / 20.0F;
                DamageSources.applyDamage(target, damage, this.getDamageSource(entity));
                target.m_146917_(0);
                MagicManager.spawnParticles(level, ParticleHelper.SNOWFLAKE, target.m_20185_(), target.m_20186_() + (double) (target.m_20206_() * 0.5F), target.m_20189_(), 35, (double) (target.m_20205_() * 0.5F), (double) (target.m_20206_() * 0.5F), (double) (target.m_20205_() * 0.5F), 0.03, false);
                if (target.isDeadOrDying()) {
                    this.spawnIcicleShards(target.m_20191_().getCenter(), damage, entity);
                    target.m_146870_();
                }
            }
        }
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    private void spawnIcicleShards(Vec3 origin, float damage, LivingEntity owner) {
        int count = 8;
        int offset = 360 / count;
        for (int i = 0; i < count; i++) {
            Vec3 motion = new Vec3(0.0, 0.0, 0.55);
            motion = motion.xRot((float) (Math.PI / 6));
            motion = motion.yRot((float) (offset * i) * (float) (Math.PI / 180.0));
            IcicleProjectile shard = new IcicleProjectile(owner.f_19853_, owner);
            shard.setDamage(damage / (float) count);
            shard.m_20256_(motion);
            Vec3 spawn = origin.add(motion.multiply(1.0, 0.0, 1.0).normalize().scale(0.5));
            Vec2 angle = Utils.rotationFromDirection(motion);
            shard.m_7678_(spawn.x, spawn.y - shard.m_20191_().getYsize() / 2.0, spawn.z, angle.y, angle.x);
            owner.f_19853_.m_7967_(shard);
        }
    }

    public float getPercentDamage(int spellLevel, LivingEntity caster) {
        return this.getSpellPower(spellLevel, caster) * 0.01F;
    }
}