package io.redspace.ironsspellbooks.spells.eldritch;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.AutoSpellConfig;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.SpellAnimations;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.AnimationHolder;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.TargetEntityCastData;
import io.redspace.ironsspellbooks.capabilities.magic.TelekinesisData;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import java.util.List;
import java.util.Optional;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

@AutoSpellConfig
public class TelekinesisSpell extends AbstractEldritchSpell {

    private final ResourceLocation spellId = new ResourceLocation("irons_spellbooks", "telekinesis");

    private final DefaultConfig defaultConfig = new DefaultConfig().setMinRarity(SpellRarity.LEGENDARY).setSchoolResource(SchoolRegistry.ELDRITCH_RESOURCE).setMaxLevel(5).setCooldownSeconds(35.0).build();

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable("ui.irons_spellbooks.distance", Utils.stringTruncation((double) this.getRange(spellLevel, caster), 1)));
    }

    public TelekinesisSpell() {
        this.manaCostPerLevel = 0;
        this.baseSpellPower = 8;
        this.spellPowerPerLevel = 4;
        this.castTime = 140;
        this.baseManaCost = 25;
    }

    @Override
    public int getCastTime(int spellLevel) {
        return this.castTime + 20 * (spellLevel - 1);
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
        return Optional.of(SoundRegistry.TELEKINESIS_CAST.get());
    }

    @Override
    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.of(SoundRegistry.TELEKINESIS_LOOP.get());
    }

    @Override
    public boolean checkPreCastConditions(Level level, int spellLevel, LivingEntity entity, MagicData playerMagicData) {
        if (Utils.preCastTargetHelper(level, entity, playerMagicData, this, this.getRange(spellLevel, entity), 0.15F)) {
            LivingEntity target = ((TargetEntityCastData) playerMagicData.getAdditionalCastData()).getTarget((ServerLevel) level);
            if (target == null) {
                return false;
            } else {
                playerMagicData.setAdditionalCastData(new TelekinesisData(entity.m_20270_(target), target, 6));
                return true;
            }
        } else {
            return false;
        }
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    @Override
    public void onServerCastTick(Level level, int spellLevel, LivingEntity entity, @Nullable MagicData playerMagicData) {
        super.onServerCastTick(level, spellLevel, entity, playerMagicData);
        if (playerMagicData != null && playerMagicData.getCastDurationRemaining() % 2 == 0) {
            this.handleTelekinesis((ServerLevel) level, entity, playerMagicData, 0.3F);
        }
    }

    private void handleTelekinesis(ServerLevel world, LivingEntity entity, MagicData playerMagicData, float strength) {
        if (playerMagicData.getAdditionalCastData() instanceof TelekinesisData targetData) {
            LivingEntity targetEntity = targetData.getTarget(world);
            if (targetEntity != null) {
                if ((targetEntity.m_213877_() || targetEntity.isDeadOrDying()) && entity instanceof ServerPlayer serverPlayer) {
                    Utils.serverSideCancelCast(serverPlayer);
                    return;
                }
                float resistance = Mth.clamp(1.0F - (float) targetEntity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE), 0.2F, 1.0F);
                float lockedDistance = targetData.getDistance();
                float actualDistance = entity.m_20270_(targetEntity);
                float distance = Mth.lerp(actualDistance > lockedDistance ? 0.25F : 0.1F, lockedDistance, actualDistance);
                targetData.setDistance(distance);
                Vec3 force = entity.m_20156_().normalize().scale((double) targetData.getDistance()).add(entity.m_20182_()).subtract(targetEntity.m_20182_()).scale((double) (0.15F * resistance * strength));
                Vec3 travel = new Vec3(targetEntity.m_20185_() - targetEntity.f_19790_, targetEntity.m_20186_() - targetEntity.f_19791_, targetEntity.m_20189_() - targetEntity.f_19792_);
                if (force.y > 0.0) {
                    targetEntity.m_183634_();
                }
                if (playerMagicData.getCastDurationRemaining() % 10 == 0) {
                    int airborne = (int) (travel.x * travel.x + travel.z * travel.z) / 2;
                    targetEntity.addEffect(new MobEffectInstance(MobEffectRegistry.AIRBORNE.get(), 31, airborne));
                    targetEntity.addEffect(new MobEffectInstance(MobEffectRegistry.ANTIGRAVITY.get(), 11, 0));
                }
                targetEntity.m_20256_(targetEntity.m_20184_().add(force));
                targetEntity.f_19864_ = true;
            }
        }
    }

    @Override
    public Vector3f getTargetingColor() {
        return new Vector3f(1.0F, 0.24F, 0.95F);
    }

    private int getRange(int spellLevel, LivingEntity caster) {
        return 12 + (spellLevel - 1) * 2;
    }

    @Override
    public AnimationHolder getCastStartAnimation() {
        return SpellAnimations.SELF_CAST_ANIMATION;
    }
}