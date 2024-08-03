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
import java.util.List;
import java.util.Optional;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

@AutoSpellConfig
public class LightningBoltSpell extends AbstractSpell {

    private final ResourceLocation spellId = new ResourceLocation("irons_spellbooks", "lightning_bolt");

    private final DefaultConfig defaultConfig = new DefaultConfig().setMinRarity(SpellRarity.EPIC).setSchoolResource(SchoolRegistry.LIGHTNING_RESOURCE).setMaxLevel(10).setCooldownSeconds(25.0).build();

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable("ui.irons_spellbooks.damage", Utils.stringTruncation((double) this.getSpellPower(spellLevel, caster), 1)));
    }

    public LightningBoltSpell() {
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
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.of(SoundEvents.ILLUSIONER_PREPARE_BLINDNESS);
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        HitResult result = Utils.raycastForEntity(level, entity, 64.0F, true, 1.0F);
        Vec3 pos = result.getLocation();
        if (result.getType() == HitResult.Type.ENTITY) {
            pos = ((EntityHitResult) result).getEntity().position();
        } else {
            pos = Utils.moveToRelativeGroundLevel(level, pos, 10);
        }
        LightningBolt lightningBolt = EntityType.LIGHTNING_BOLT.create(level);
        lightningBolt.setVisualOnly(true);
        lightningBolt.setDamage(0.0F);
        lightningBolt.m_146884_(pos);
        level.m_7967_(lightningBolt);
        float radius = 4.0F;
        float damage = this.getSpellPower(spellLevel, entity);
        Vec3 finalpos = pos;
        level.getEntities(entity, AABB.ofSize(finalpos, (double) (radius * 2.0F), (double) (radius * 2.0F), (double) (radius * 2.0F)), target -> this.canHit(entity, target)).forEach(target -> {
            double distance = target.distanceToSqr(finalpos);
            if (distance < (double) (radius * radius) && Utils.hasLineOfSight(level, finalpos.add(0.0, 2.0, 0.0), target.getBoundingBox().getCenter(), true)) {
                float finalDamage = (float) ((double) damage * (1.0 - distance / (double) (radius * radius)));
                DamageSources.applyDamage(target, finalDamage, this.getDamageSource(lightningBolt, entity));
                if (target instanceof Creeper creeper) {
                    creeper.thunderHit((ServerLevel) level, lightningBolt);
                }
            }
        });
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    private boolean canHit(Entity owner, Entity target) {
        return target != owner && target.isAlive() && target.isPickable() && !target.isSpectator();
    }
}