package com.mna.items.artifice;

import com.mna.api.faction.IFaction;
import com.mna.api.items.IFactionSpecific;
import com.mna.factions.Factions;
import com.mna.items.base.IHellfireItem;
import com.mna.items.sorcery.ItemStaff;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ItemHellfireStaff extends ItemStaff implements IHellfireItem, IFactionSpecific {

    public ItemHellfireStaff() {
        super(7.0F);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        InteractionResultHolder<ItemStack> superClick = super.use(worldIn, playerIn, handIn);
        if (!worldIn.isClientSide && superClick.getResult() == InteractionResult.SUCCESS) {
            this.usedByPlayer(playerIn);
        }
        return superClick;
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        target.m_20254_(4);
        return false;
    }

    @Override
    public IFaction getFaction() {
        return Factions.DEMONS;
    }

    @Override
    public int getEnchantmentValue(ItemStack stack) {
        return 0;
    }
}