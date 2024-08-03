package io.redspace.ironsspellbooks.spells.blood;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.AutoSpellConfig;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import io.redspace.ironsspellbooks.spells.ender.TeleportSpell;
import java.util.List;
import java.util.Optional;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

@AutoSpellConfig
public class BloodStepSpell extends AbstractSpell {

    private final ResourceLocation spellId = new ResourceLocation("irons_spellbooks", "blood_step");

    private final DefaultConfig defaultConfig = new DefaultConfig().setMinRarity(SpellRarity.UNCOMMON).setSchoolResource(SchoolRegistry.BLOOD_RESOURCE).setMaxLevel(5).setCooldownSeconds(5.0).build();

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable("ui.irons_spellbooks.distance", Utils.stringTruncation((double) this.getDistance(spellLevel, caster), 1)));
    }

    public BloodStepSpell() {
        this.baseSpellPower = 12;
        this.spellPowerPerLevel = 4;
        this.baseManaCost = 30;
        this.manaCostPerLevel = 10;
        this.castTime = 0;
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
        return Optional.of(SoundRegistry.BLOOD_STEP.get());
    }

    @Override
    public void onClientPreCast(Level level, int spellLevel, LivingEntity entity, InteractionHand hand, @Nullable MagicData playerMagicData) {
        super.onClientPreCast(level, spellLevel, entity, hand, playerMagicData);
        Vec3 forward = entity.m_20156_().normalize();
        for (int i = 0; i < 35; i++) {
            Vec3 motion = forward.scale(Utils.random.nextDouble() * 0.25);
            level.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, entity.m_20208_(0.4F), entity.m_20187_(), entity.m_20262_(0.4F), motion.x, motion.y, motion.z);
        }
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        Vec3 dest = null;
        TeleportSpell.TeleportData teleportData = (TeleportSpell.TeleportData) playerMagicData.getAdditionalCastData();
        if (teleportData != null) {
            Vec3 potentialTarget = teleportData.getTeleportTargetPosition();
            if (potentialTarget != null) {
                dest = potentialTarget;
                entity.m_6021_(potentialTarget.x, potentialTarget.y, potentialTarget.z);
            }
        } else {
            HitResult hitResult = Utils.raycastForEntity(level, entity, this.getDistance(spellLevel, entity), true);
            if (entity.m_20159_()) {
                entity.stopRiding();
            }
            if (hitResult.getType() == HitResult.Type.ENTITY && ((EntityHitResult) hitResult).getEntity() instanceof LivingEntity target) {
                for (int i = 0; i < 8; i++) {
                    dest = target.m_20182_().subtract(new Vec3(0.0, 0.0, 1.5).yRot(-(target.m_146908_() + (float) (i * 45)) * (float) (Math.PI / 180.0)));
                    if (level.getBlockState(BlockPos.containing(dest).above()).m_60795_()) {
                        break;
                    }
                }
                entity.m_6021_(dest.x, dest.y + 1.0, dest.z);
                entity.lookAt(EntityAnchorArgument.Anchor.EYES, target.m_146892_().subtract(0.0, 0.15, 0.0));
            } else {
                dest = TeleportSpell.findTeleportLocation(level, entity, this.getDistance(spellLevel, entity));
                entity.m_6021_(dest.x, dest.y, dest.z);
            }
        }
        entity.m_183634_();
        level.playSound(null, dest.x, dest.y, dest.z, (SoundEvent) this.getCastFinishSound().get(), SoundSource.NEUTRAL, 1.0F, 1.0F);
        entity.m_6842_(true);
        entity.addEffect(new MobEffectInstance(MobEffectRegistry.TRUE_INVISIBILITY.get(), 100, 0, false, false, true));
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    private float getDistance(int spellLevel, LivingEntity sourceEntity) {
        return this.getSpellPower(spellLevel, sourceEntity);
    }

    @Override
    public AnimationHolder getCastStartAnimation() {
        return AnimationHolder.none();
    }
}