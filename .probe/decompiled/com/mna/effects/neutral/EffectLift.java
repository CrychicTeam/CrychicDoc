package com.mna.effects.neutral;

import com.mna.ManaAndArtifice;
import com.mna.api.capabilities.IPlayerMagic;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.effects.interfaces.IInputDisable;
import com.mna.effects.interfaces.INoCreeperLingering;
import com.mna.network.ServerMessageDispatcher;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class EffectLift extends MobEffect implements IInputDisable, INoCreeperLingering {

    public EffectLift() {
        super(MobEffectCategory.NEUTRAL, 0);
    }

    public List<ItemStack> getCurativeItems() {
        return new ArrayList();
    }

    @Override
    public void addAttributeModifiers(LivingEntity entityLivingBaseIn, AttributeMap attributeMapIn, int amplifier) {
        entityLivingBaseIn.m_20242_(true);
        if (entityLivingBaseIn instanceof Player) {
            IPlayerMagic magic = (IPlayerMagic) ((Player) entityLivingBaseIn).getCapability(PlayerMagicProvider.MAGIC).orElse(null);
            Vec3 vec = entityLivingBaseIn.m_20182_();
            magic.setLiftPosition(vec);
            magic.setDidAllowFlying(((Player) entityLivingBaseIn).getAbilities().mayfly);
            ManaAndArtifice.instance.proxy.setFlightEnabled((Player) entityLivingBaseIn, true);
            if (!entityLivingBaseIn.m_9236_().isClientSide()) {
                ServerMessageDispatcher.sendSetLiftPosition(vec.x, vec.y, vec.z, (ServerPlayer) entityLivingBaseIn);
            }
        }
    }

    @Override
    public void removeAttributeModifiers(LivingEntity entityLivingBaseIn, AttributeMap attributeMapIn, int amplifier) {
        entityLivingBaseIn.m_20242_(false);
        if (entityLivingBaseIn instanceof Player) {
            IPlayerMagic magic = (IPlayerMagic) ((Player) entityLivingBaseIn).getCapability(PlayerMagicProvider.MAGIC).orElse(null);
            ManaAndArtifice.instance.proxy.setFlightEnabled((Player) entityLivingBaseIn, magic.didAllowFlying());
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public void applyEffectTick(LivingEntity entityLivingBaseIn, int amplifier) {
        Vec3 forcePos = entityLivingBaseIn.m_20182_();
        entityLivingBaseIn.m_20334_(0.0, 0.0, 0.0);
        entityLivingBaseIn.f_19789_ = 0.0F;
        entityLivingBaseIn.f_19812_ = true;
        if (amplifier == 1) {
            float liftSpeed = 0.0175F;
            if (entityLivingBaseIn.getPersistentData().contains("lift_speed")) {
                liftSpeed = entityLivingBaseIn.getPersistentData().getFloat("lift_speed");
            }
            if (entityLivingBaseIn instanceof Player) {
                IPlayerMagic magic = (IPlayerMagic) ((Player) entityLivingBaseIn).getCapability(PlayerMagicProvider.MAGIC).orElse(null);
                forcePos = magic.getLiftPosition();
                if (forcePos == null) {
                    forcePos = entityLivingBaseIn.m_20182_();
                }
                forcePos = forcePos.add(0.0, (double) liftSpeed, 0.0);
                magic.setLiftPosition(forcePos);
                ManaAndArtifice.instance.proxy.setFlightEnabled((Player) entityLivingBaseIn, true);
            } else {
                forcePos = forcePos.add(0.0, (double) liftSpeed, 0.0);
            }
        }
        entityLivingBaseIn.m_6034_(forcePos.x, forcePos.y, forcePos.z);
    }

    @Override
    public EnumSet<IInputDisable.InputMask> getDisabledFlags() {
        return EnumSet.of(IInputDisable.InputMask.LEFT_CLICK, IInputDisable.InputMask.RIGHT_CLICK, IInputDisable.InputMask.MOVEMENT);
    }
}