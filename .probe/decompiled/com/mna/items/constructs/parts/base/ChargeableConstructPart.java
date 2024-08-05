package com.mna.items.constructs.parts.base;

import com.mna.api.capabilities.IPlayerMagic;
import com.mna.api.entities.construct.ConstructMaterial;
import com.mna.api.entities.construct.ConstructSlot;
import com.mna.api.entities.construct.ItemConstructPart;
import com.mna.api.items.IShowHud;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;

public abstract class ChargeableConstructPart extends ItemConstructPart implements IShowHud {

    public ChargeableConstructPart(ConstructMaterial material, ConstructSlot slot, int modelTypeMutex) {
        super(material, slot, modelTypeMutex);
    }

    public abstract float getChargeCost();

    public int getChargeDuration(ItemStack stack) {
        return 25;
    }

    public abstract void onChargeReleased(ItemStack var1, Level var2, LivingEntity var3);

    private final float getTickChargeCost(ItemStack stack) {
        return this.getChargeCost() / (float) this.getChargeDuration(stack);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack itemStack0) {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 40;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        player.getCapability(PlayerMagicProvider.MAGIC).ifPresent(m -> {
            if (m.getCastingResource().hasEnough(player, this.getChargeCost())) {
                player.m_6672_(hand);
            }
        });
        return InteractionResultHolder.consume(itemstack);
    }

    @Override
    public void onUseTick(Level world, LivingEntity living, ItemStack stack, int ticksRemaining) {
        if (ticksRemaining >= 2 && living instanceof Player) {
            LazyOptional<IPlayerMagic> cap = ((Player) living).getCapability(PlayerMagicProvider.MAGIC);
            if (!cap.isPresent()) {
                living.stopUsingItem();
            } else {
                IPlayerMagic magic = (IPlayerMagic) cap.resolve().get();
                if (!magic.getCastingResource().hasEnough(living, this.getTickChargeCost(stack))) {
                    living.stopUsingItem();
                } else {
                    magic.getCastingResource().consume(living, this.getTickChargeCost(stack));
                }
            }
        } else {
            if (living instanceof Player && !world.isClientSide) {
                this.onChargeReleased(stack, world, living);
                ((Player) living).getCooldowns().addCooldown(this, 20);
            }
            living.stopUsingItem();
        }
    }

    @Override
    public void releaseUsing(ItemStack stack, Level world, LivingEntity living, int ticksRemaining) {
        if (ticksRemaining == 0) {
        }
    }
}