package io.redspace.ironsspellbooks.spells.ender;

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
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.entity.spells.comet.Comet;
import io.redspace.ironsspellbooks.entity.spells.target_area.TargetedAreaEntity;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.spells.TargetAreaCastData;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import java.util.List;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

@AutoSpellConfig
public class StarfallSpell extends AbstractSpell {

    private final ResourceLocation spellId = new ResourceLocation("irons_spellbooks", "starfall");

    private final DefaultConfig defaultConfig = new DefaultConfig().setMinRarity(SpellRarity.UNCOMMON).setSchoolResource(SchoolRegistry.ENDER_RESOURCE).setMaxLevel(10).setCooldownSeconds(16.0).build();

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable("ui.irons_spellbooks.damage", Utils.stringTruncation((double) this.getDamage(spellLevel, caster), 2)), Component.translatable("ui.irons_spellbooks.radius", Utils.stringTruncation((double) this.getRadius(caster), 1)));
    }

    public StarfallSpell() {
        this.manaCostPerLevel = 1;
        this.baseSpellPower = 8;
        this.spellPowerPerLevel = 1;
        this.castTime = 160;
        this.baseManaCost = 5;
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

    @Override
    public Optional<SoundEvent> getCastStartSound() {
        return Optional.of(SoundRegistry.ENDER_CAST.get());
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.empty();
    }

    @Override
    public void onCast(Level world, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        if (!(playerMagicData.getAdditionalCastData() instanceof TargetAreaCastData)) {
            Vec3 targetArea = Utils.moveToRelativeGroundLevel(world, Utils.raycastForEntity(world, entity, 40.0F, true).getLocation(), 12);
            playerMagicData.setAdditionalCastData(new TargetAreaCastData(targetArea, TargetedAreaEntity.createTargetAreaEntity(world, targetArea, this.getRadius(entity), 6291596)));
        }
        super.onCast(world, spellLevel, entity, castSource, playerMagicData);
    }

    @Override
    public void onServerCastTick(Level level, int spellLevel, LivingEntity entity, @Nullable MagicData playerMagicData) {
        if (playerMagicData != null && (playerMagicData.getCastDurationRemaining() + 1) % 4 == 0 && playerMagicData.getAdditionalCastData() instanceof TargetAreaCastData targetAreaCastData) {
            for (int i = 0; i < 2; i++) {
                Vec3 center = targetAreaCastData.getCenter();
                float radius = this.getRadius(entity);
                Vec3 spawn = center.add(new Vec3(0.0, 0.0, (double) (entity.getRandom().nextFloat() * radius)).yRot((float) entity.getRandom().nextInt(360)));
                spawn = this.raiseWithCollision(spawn, 12, level);
                this.shootComet(level, spellLevel, entity, spawn);
                MagicManager.spawnParticles(level, ParticleHelper.COMET_FOG, spawn.x, spawn.y, spawn.z, 1, 1.0, 1.0, 1.0, 1.0, false);
                MagicManager.spawnParticles(level, ParticleHelper.COMET_FOG, spawn.x, spawn.y, spawn.z, 1, 1.0, 1.0, 1.0, 1.0, true);
            }
        }
    }

    private Vec3 raiseWithCollision(Vec3 start, int blocks, Level level) {
        for (int i = 0; i < blocks; i++) {
            Vec3 raised = start.add(0.0, 1.0, 0.0);
            if (!level.getBlockState(BlockPos.containing(raised)).m_60795_()) {
                break;
            }
            start = raised;
        }
        return start;
    }

    private float getDamage(int spellLevel, LivingEntity caster) {
        return this.getSpellPower(spellLevel, caster) * 0.5F;
    }

    private float getRadius(LivingEntity caster) {
        return 6.0F;
    }

    public void shootComet(Level world, int spellLevel, LivingEntity entity, Vec3 spawn) {
        Comet fireball = new Comet(world, entity);
        fireball.m_146884_(spawn.add(-1.0, 0.0, 0.0));
        fireball.shoot(new Vec3(0.15F, -0.85F, 0.0), 0.075F);
        fireball.setDamage(this.getDamage(spellLevel, entity));
        fireball.setExplosionRadius(2.0F);
        world.m_7967_(fireball);
        world.playSound(null, spawn.x, spawn.y, spawn.z, SoundEvents.FIREWORK_ROCKET_LAUNCH, SoundSource.PLAYERS, 3.0F, 0.7F + Utils.random.nextFloat() * 0.3F);
    }

    @Override
    public AnimationHolder getCastStartAnimation() {
        return SpellAnimations.ANIMATION_CONTINUOUS_OVERHEAD;
    }
}