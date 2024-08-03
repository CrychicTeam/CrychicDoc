package io.redspace.ironsspellbooks.spells.lightning;

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
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

@AutoSpellConfig
public class AscensionSpell extends AbstractSpell {

    private final ResourceLocation spellId = new ResourceLocation("irons_spellbooks", "ascension");

    private final DefaultConfig defaultConfig = new DefaultConfig().setMinRarity(SpellRarity.RARE).setSchoolResource(SchoolRegistry.LIGHTNING_RESOURCE).setMaxLevel(10).setCooldownSeconds(15.0).build();

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable("ui.irons_spellbooks.damage", Utils.stringTruncation((double) this.getSpellPower(spellLevel, caster), 1)));
    }

    public AscensionSpell() {
        this.manaCostPerLevel = 1;
        this.baseSpellPower = 5;
        this.spellPowerPerLevel = 1;
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
    public ICastDataSerializable getEmptyCastData() {
        return new ImpulseCastData();
    }

    @Override
    public void onClientCast(Level level, int spellLevel, LivingEntity entity, ICastData castData) {
        if (castData instanceof ImpulseCastData data) {
            entity.f_19812_ = data.hasImpulse;
            double y = Math.max(entity.m_20184_().y, (double) data.y);
            entity.m_20334_((double) data.x, y, (double) data.z);
        }
        super.onClientCast(level, spellLevel, entity, castData);
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        entity.addEffect(new MobEffectInstance(MobEffectRegistry.ASCENSION.get(), 80, 0, false, false, true));
        Vec3 vec = entity.m_20182_();
        for (int i = 0; i < 32 && level.getBlockState(BlockPos.containing(vec).below()).m_60795_(); i++) {
            vec = vec.subtract(0.0, 1.0, 0.0);
        }
        Vec3 strikePos = vec;
        LightningBolt lightningBolt = EntityType.LIGHTNING_BOLT.create(level);
        lightningBolt.setVisualOnly(true);
        lightningBolt.setDamage(0.0F);
        lightningBolt.m_146884_(strikePos);
        level.m_7967_(lightningBolt);
        float radius = 5.0F;
        level.m_45933_(entity, entity.m_20191_().inflate((double) radius)).forEach(target -> {
            double distance = target.distanceToSqr(strikePos);
            if (distance < (double) (radius * radius)) {
                float finalDamage = (float) ((double) this.getDamage(spellLevel, entity) * (1.0 - distance / (double) (radius * radius)));
                DamageSources.applyDamage(target, finalDamage, this.getDamageSource(lightningBolt, entity));
                if (target instanceof Creeper creeper) {
                    creeper.thunderHit((ServerLevel) level, lightningBolt);
                }
            }
        });
        Vec3 motion = entity.m_20154_().multiply(1.0, 0.0, 1.0).normalize().add(0.0, 5.0, 0.0).scale(0.125);
        playerMagicData.setAdditionalCastData(new ImpulseCastData((float) motion.x, (float) motion.y, (float) motion.z, true));
        entity.m_20256_(entity.m_20184_().add(motion));
        entity.f_19812_ = true;
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    private int getDamage(int spellLevel, LivingEntity caster) {
        return (int) this.getSpellPower(spellLevel, caster);
    }
}