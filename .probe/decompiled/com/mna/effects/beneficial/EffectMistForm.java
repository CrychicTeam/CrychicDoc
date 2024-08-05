package com.mna.effects.beneficial;

import com.mna.ManaAndArtifice;
import com.mna.api.capabilities.IPlayerProgression;
import com.mna.capabilities.entity.MAPFX;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.effects.interfaces.IDoubleTapEndEarly;
import com.mna.effects.interfaces.IInputDisable;
import com.mna.effects.interfaces.INoCreeperLingering;
import com.mna.effects.particles.EffectWithCustomClientParticles;
import com.mna.factions.Factions;
import com.mna.tools.EntityUtil;
import java.util.EnumSet;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class EffectMistForm extends EffectWithCustomClientParticles implements IInputDisable, IDoubleTapEndEarly, INoCreeperLingering {

    public EffectMistForm() {
        super(MobEffectCategory.BENEFICIAL, 0, MAPFX.Flag.MIST_FORM, MAPFX.Flag.CANCEL_RENDER);
    }

    @Override
    public void addAttributeModifiers(LivingEntity pLivingEntity, AttributeMap pAttributeMap, int pAmplifier) {
        super.m_6385_(pLivingEntity, pAttributeMap, pAmplifier);
        if (pLivingEntity instanceof Player p) {
            IPlayerProgression pr = (IPlayerProgression) pLivingEntity.getCapability(PlayerProgressionProvider.PROGRESSION).orElse(null);
            if (pr != null && pr.getAlliedFaction() == Factions.UNDEAD && pLivingEntity.getPersistentData().getBoolean("bone_armor_set_bonus")) {
                p.getPersistentData().putBoolean("mist_form_speed_remove", true);
                p.getAbilities().setFlyingSpeed(p.getAbilities().getFlyingSpeed() + 0.025F);
                p.onUpdateAbilities();
            }
        }
    }

    @Override
    public void removeAttributeModifiers(LivingEntity pLivingEntity, AttributeMap pAttributeMap, int pAmplifier) {
        super.m_6386_(pLivingEntity, pAttributeMap, pAmplifier);
        if (pLivingEntity instanceof Player p && p.getPersistentData().getBoolean("mist_form_speed_remove")) {
            p.getPersistentData().remove("mist_form_speed_remove");
            p.getAbilities().setFlyingSpeed(p.getAbilities().getFlyingSpeed() - 0.025F);
            p.onUpdateAbilities();
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public void applyEffectTick(LivingEntity entityLivingBaseIn, int amplifier) {
        if (entityLivingBaseIn instanceof Player player) {
            MobEffectInstance activeEffect = player.m_21124_(this);
            if (activeEffect.getDuration() <= 5 && !activeEffect.isInfiniteDuration()) {
                ManaAndArtifice.instance.proxy.setFlightEnabled(player, false);
                player.f_19794_ = false;
                EntityUtil.removeInvisibility(entityLivingBaseIn);
            } else {
                ManaAndArtifice.instance.proxy.setFlightEnabled(player, true);
                player.getAbilities().flying = true;
                boolean shouldNoClip = false;
                BlockHitResult brtr = player.m_9236_().m_45547_(new ClipContext(player.m_20299_(1.0F), player.m_20299_(1.0F).add(player.m_20184_().scale(10.0)), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));
                if (!player.f_19862_ && !player.f_19863_) {
                    shouldNoClip = !player.m_146900_().m_60815_();
                } else if (brtr.getType() == HitResult.Type.BLOCK) {
                    shouldNoClip = !player.m_9236_().getBlockState(brtr.getBlockPos()).m_60815_();
                }
                if (shouldNoClip) {
                    Direction offset = player.m_6374_();
                    Vec3i offsetVec = offset.getNormal();
                    for (int i = 0; i < 1; i++) {
                        shouldNoClip &= !player.m_9236_().getBlockState(brtr.getBlockPos().offset(offsetVec.relative(offset, i))).m_60815_();
                    }
                }
                player.f_19794_ = shouldNoClip;
            }
            entityLivingBaseIn.m_6842_(true);
        }
    }

    @Override
    public EnumSet<IInputDisable.InputMask> getDisabledFlags() {
        return EnumSet.of(IInputDisable.InputMask.LEFT_CLICK, IInputDisable.InputMask.RIGHT_CLICK);
    }

    @Override
    public void onRemoved(Player player, MobEffectInstance effect) {
        player.getAbilities().mayfly = player.isCreative() || player.isSpectator();
        player.getAbilities().flying = false;
        player.f_19794_ = false;
        player.m_6842_(false);
        player.onUpdateAbilities();
    }
}