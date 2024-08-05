package io.redspace.ironsspellbooks.spells.ender;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.entity.IMagicEntity;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.AutoSpellConfig;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.capabilities.magic.RecastResult;
import io.redspace.ironsspellbooks.effect.MagicMobEffect;
import io.redspace.ironsspellbooks.entity.mobs.AntiMagicSusceptible;
import io.redspace.ironsspellbooks.entity.mobs.MagicSummon;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

@AutoSpellConfig
public class CounterspellSpell extends AbstractSpell {

    private final ResourceLocation spellId = new ResourceLocation("irons_spellbooks", "counterspell");

    private final DefaultConfig defaultConfig = new DefaultConfig().setMinRarity(SpellRarity.RARE).setSchoolResource(SchoolRegistry.ENDER_RESOURCE).setMaxLevel(1).setCooldownSeconds(10.0).build();

    public CounterspellSpell() {
        this.manaCostPerLevel = 1;
        this.baseSpellPower = 1;
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
    public void onCast(Level world, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        Vec3 start = entity.m_146892_();
        Vec3 end = start.add(entity.m_20156_().normalize().scale(80.0));
        HitResult hitResult = Utils.raycastForEntity(entity.f_19853_, entity, start, end, true, 0.35F, Utils::validAntiMagicTarget);
        Vec3 forward = entity.m_20156_().normalize();
        if (hitResult instanceof EntityHitResult entityHitResult) {
            Entity hitEntity = entityHitResult.getEntity();
            double distance = (double) entity.m_20270_(hitEntity);
            for (float i = 1.0F; (double) i < distance; i += 0.5F) {
                Vec3 pos = entity.m_146892_().add(forward.scale((double) i));
                MagicManager.spawnParticles(world, ParticleTypes.ENCHANT, pos.x, pos.y, pos.z, 1, 0.0, 0.0, 0.0, 0.0, false);
            }
            if (hitEntity instanceof AntiMagicSusceptible antiMagicSusceptible) {
                if (antiMagicSusceptible instanceof MagicSummon summon) {
                    if (summon.getSummoner() == entity) {
                        if (summon instanceof Mob mob && mob.getTarget() == null) {
                            antiMagicSusceptible.onAntiMagic(playerMagicData);
                        }
                    } else {
                        antiMagicSusceptible.onAntiMagic(playerMagicData);
                    }
                } else {
                    antiMagicSusceptible.onAntiMagic(playerMagicData);
                }
            } else if (hitEntity instanceof ServerPlayer serverPlayer) {
                Utils.serverSideCancelCast(serverPlayer, true);
                MagicData.getPlayerMagicData(serverPlayer).getPlayerRecasts().removeAll(RecastResult.COUNTERSPELL);
            } else if (hitEntity instanceof IMagicEntity abstractSpellCastingMob) {
                abstractSpellCastingMob.cancelCast();
            }
            if (hitEntity instanceof LivingEntity livingEntity) {
                for (MobEffect mobEffect : livingEntity.getActiveEffectsMap().keySet().stream().toList()) {
                    if (mobEffect instanceof MagicMobEffect magicMobEffect) {
                        livingEntity.removeEffect(magicMobEffect);
                    }
                }
            }
        } else {
            for (float i = 1.0F; i < 40.0F; i += 0.5F) {
                Vec3 pos = entity.m_146892_().add(forward.scale((double) i));
                MagicManager.spawnParticles(world, ParticleTypes.ENCHANT, pos.x, pos.y, pos.z, 1, 0.0, 0.0, 0.0, 0.0, false);
                if (!world.getBlockState(BlockPos.containing(pos)).m_60795_()) {
                    break;
                }
            }
        }
        super.onCast(world, spellLevel, entity, castSource, playerMagicData);
    }
}