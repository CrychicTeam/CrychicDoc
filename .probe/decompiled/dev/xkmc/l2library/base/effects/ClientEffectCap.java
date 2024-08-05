package dev.xkmc.l2library.base.effects;

import dev.xkmc.l2library.capability.entity.GeneralCapabilityHolder;
import dev.xkmc.l2library.capability.entity.GeneralCapabilityTemplate;
import dev.xkmc.l2serial.serialization.SerialClass;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

@SerialClass
public class ClientEffectCap extends GeneralCapabilityTemplate<LivingEntity, ClientEffectCap> {

    public static final Capability<ClientEffectCap> CAPABILITY = CapabilityManager.get(new CapabilityToken<ClientEffectCap>() {
    });

    public static final GeneralCapabilityHolder<LivingEntity, ClientEffectCap> HOLDER = new GeneralCapabilityHolder(new ResourceLocation("l2library", "effects"), CAPABILITY, ClientEffectCap.class, ClientEffectCap::new, LivingEntity.class, e -> e.m_9236_().isClientSide());

    public final Map<MobEffect, Integer> map = new TreeMap(Comparator.comparing(MobEffect::m_19481_));

    public static void register() {
    }
}