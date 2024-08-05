package io.redspace.ironsspellbooks.spells.fire;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.AutoSpellConfig;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.ICastData;
import io.redspace.ironsspellbooks.api.spells.ICastDataSerializable;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.ImpulseCastData;
import io.redspace.ironsspellbooks.damage.SpellDamageSource;
import io.redspace.ironsspellbooks.player.SpinAttackType;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.List;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

@AutoSpellConfig
public class BurningDashSpell extends AbstractSpell {

    private final ResourceLocation spellId = new ResourceLocation("irons_spellbooks", "burning_dash");

    private final DefaultConfig defaultConfig = new DefaultConfig().setMinRarity(SpellRarity.COMMON).setSchoolResource(SchoolRegistry.FIRE_RESOURCE).setMaxLevel(10).setCooldownSeconds(10.0).build();

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable("ui.irons_spellbooks.damage", this.getDamage(spellLevel, caster)));
    }

    public BurningDashSpell() {
        this.manaCostPerLevel = 2;
        this.baseSpellPower = 1;
        this.spellPowerPerLevel = 1;
        this.castTime = 0;
        this.baseManaCost = 20;
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
    public void onClientCast(Level level, int spellLevel, LivingEntity entity, ICastData castData) {
        if (castData instanceof ImpulseCastData bdcd) {
            entity.f_19812_ = bdcd.hasImpulse;
            entity.m_20256_(entity.m_20184_().add((double) bdcd.x, (double) bdcd.y, (double) bdcd.z));
        }
        super.onClientCast(level, spellLevel, entity, castData);
    }

    @Override
    public ICastDataSerializable getEmptyCastData() {
        return new ImpulseCastData();
    }

    @Override
    public void onCast(Level world, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        entity.f_19812_ = true;
        float multiplier = (15.0F + this.getSpellPower(spellLevel, entity)) / 12.0F;
        Vec3 forward = entity.m_20154_();
        if (playerMagicData.getAdditionalCastData() instanceof BurningDashSpell.BurningDashDirectionOverrideCastData) {
            if (Utils.random.nextBoolean()) {
                forward = forward.yRot(90.0F);
            } else {
                forward = forward.yRot(-90.0F);
            }
        }
        Vec3 vec = forward.multiply(3.0, 1.0, 3.0).normalize().add(0.0, 0.25, 0.0).scale((double) multiplier);
        if (entity.m_20096_()) {
            entity.m_146884_(entity.m_20182_().add(0.0, 1.5, 0.0));
            vec.add(0.0, 0.25, 0.0);
        }
        playerMagicData.setAdditionalCastData(new ImpulseCastData((float) vec.x, (float) vec.y, (float) vec.z, true));
        entity.m_20256_(new Vec3(Mth.lerp(0.75, entity.m_20184_().x, vec.x), Mth.lerp(0.75, entity.m_20184_().y, vec.y), Mth.lerp(0.75, entity.m_20184_().z, vec.z)));
        entity.addEffect(new MobEffectInstance(MobEffectRegistry.BURNING_DASH.get(), 15, this.getDamage(spellLevel, entity), false, false, false));
        entity.f_19802_ = 20;
        playerMagicData.getSyncedData().setSpinAttackType(SpinAttackType.FIRE);
        super.onCast(world, spellLevel, entity, castSource, playerMagicData);
    }

    @Override
    public SpellDamageSource getDamageSource(@Nullable Entity projectile, Entity attacker) {
        return super.getDamageSource(projectile, attacker).setFireTime(4);
    }

    private int getDamage(int spellLevel, LivingEntity caster) {
        return (int) (5.0F + this.getSpellPower(spellLevel, caster));
    }

    public static void ambientParticles(ClientLevel level, LivingEntity entity) {
        for (int i = 0; i < 2; i++) {
            Vec3 random = Utils.getRandomVec3(0.2);
            level.addParticle(ParticleHelper.FIRE, entity.m_20208_(0.75), entity.m_20186_() + Utils.getRandomScaled(0.75), entity.m_20262_(0.75), random.x, random.y, random.z);
        }
        for (int i = 0; i < 6; i++) {
            Vec3 random = Utils.getRandomVec3(0.2);
            level.addParticle(ParticleHelper.EMBERS, entity.m_20208_(0.75), entity.m_20186_() + Utils.getRandomScaled(0.75), entity.m_20262_(0.75), random.x, random.y, random.z);
        }
    }

    public static class BurningDashDirectionOverrideCastData implements ICastData {

        @Override
        public void reset() {
        }
    }
}