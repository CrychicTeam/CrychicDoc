package io.redspace.ironsspellbooks.spells.nature;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.AutoSpellConfig;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.TargetEntityCastData;
import io.redspace.ironsspellbooks.entity.spells.firefly_swarm.FireflySwarmProjectile;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import java.util.List;
import java.util.Optional;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

@AutoSpellConfig
public class FireflySwarmSpell extends AbstractSpell {

    private final ResourceLocation spellId = new ResourceLocation("irons_spellbooks", "firefly_swarm");

    private final DefaultConfig defaultConfig = new DefaultConfig().setMinRarity(SpellRarity.UNCOMMON).setSchoolResource(SchoolRegistry.NATURE_RESOURCE).setMaxLevel(10).setCooldownSeconds(20.0).build();

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable("ui.irons_spellbooks.aoe_damage", Utils.stringTruncation((double) this.getDamage(spellLevel, caster), 1)), Component.translatable("ui.irons_spellbooks.radius", 2.0F));
    }

    public FireflySwarmSpell() {
        this.manaCostPerLevel = 10;
        this.baseSpellPower = 6;
        this.spellPowerPerLevel = 1;
        this.castTime = 30;
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
        return Optional.of(SoundRegistry.FIREFLY_SPELL_PREPARE.get());
    }

    @Override
    public boolean checkPreCastConditions(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData) {
        return Utils.preCastTargetHelper(level, entity, playerMagicData, this, 32, 0.35F);
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        Vec3 spawn = null;
        Entity target = null;
        if (playerMagicData.getAdditionalCastData() instanceof TargetEntityCastData castTargetingData) {
            spawn = castTargetingData.getTargetPosition((ServerLevel) level);
            target = castTargetingData.getTarget((ServerLevel) level);
        }
        if (spawn == null) {
            HitResult raycast = Utils.raycastForEntity(level, entity, 32.0F, true);
            if (raycast.getType() == HitResult.Type.ENTITY) {
                target = ((EntityHitResult) raycast).getEntity();
                spawn = target.position();
            } else {
                spawn = Utils.moveToRelativeGroundLevel(level, raycast.getLocation().subtract(entity.m_20156_().normalize()).add(0.0, 2.0, 0.0), 5);
            }
        }
        FireflySwarmProjectile fireflies = new FireflySwarmProjectile(level, entity, target, this.getDamage(spellLevel, entity));
        fireflies.m_20219_(spawn.add(0.0, 0.5, 0.0));
        level.m_7967_(fireflies);
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    private float getDamage(int spellLevel, LivingEntity entity) {
        return this.getSpellPower(spellLevel, entity) / 3.0F;
    }
}