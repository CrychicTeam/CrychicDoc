package com.sihenzhang.crockpot.integration.curios;

import com.sihenzhang.crockpot.effect.CrockPotEffects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;

public class GnawsCoinCuriosCapabilityProvider implements ICapabilityProvider {

    private final LazyOptional<ICurio> curioOptional;

    public GnawsCoinCuriosCapabilityProvider(ItemStack stack, @Nullable CompoundTag nbt) {
        this.curioOptional = LazyOptional.of(() -> new ICurio() {

            @Override
            public ItemStack getStack() {
                return stack;
            }

            @Override
            public void curioTick(SlotContext slotContext) {
                LivingEntity entity = slotContext.entity();
                if (!entity.m_9236_().isClientSide && entity instanceof Player && entity.f_19797_ % 19 == 0) {
                    entity.addEffect(new MobEffectInstance(CrockPotEffects.GNAWS_GIFT.get(), 20, 0, true, true));
                }
            }

            @Override
            public boolean canEquipFromUse(SlotContext slotContext) {
                return true;
            }

            @Nonnull
            @Override
            public ICurio.DropRule getDropRule(SlotContext slotContext, DamageSource source, int lootingLevel, boolean recentlyHit) {
                return ICurio.DropRule.ALWAYS_KEEP;
            }
        });
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return CuriosCapability.ITEM.orEmpty(cap, this.curioOptional);
    }
}