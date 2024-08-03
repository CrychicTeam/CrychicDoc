package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.entity.EntityDreadLichSkull;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class ItemLichStaff extends Item {

    public ItemLichStaff() {
        super(new Item.Properties().durability(100));
    }

    @Override
    public boolean isValidRepairItem(@NotNull ItemStack toRepair, ItemStack repair) {
        return repair.getItem() == IafItemRegistry.DREAD_SHARD.get() || super.isValidRepairItem(toRepair, repair);
    }

    @NotNull
    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, @NotNull InteractionHand hand) {
        ItemStack itemStackIn = playerIn.m_21120_(hand);
        if (!worldIn.isClientSide) {
            playerIn.m_6672_(hand);
            playerIn.m_6674_(hand);
            double d2 = playerIn.m_20154_().x;
            double d3 = playerIn.m_20154_().y;
            double d4 = playerIn.m_20154_().z;
            float inaccuracy = 1.0F;
            d2 += playerIn.m_217043_().nextGaussian() * 0.0075F * (double) inaccuracy;
            d3 += playerIn.m_217043_().nextGaussian() * 0.0075F * (double) inaccuracy;
            d4 += playerIn.m_217043_().nextGaussian() * 0.0075F * (double) inaccuracy;
            EntityDreadLichSkull charge = new EntityDreadLichSkull(IafEntityRegistry.DREAD_LICH_SKULL.get(), worldIn, playerIn, 6.0);
            charge.m_6686_((double) playerIn.m_146909_(), (double) playerIn.m_146908_(), 0.0, 7.0F, 1.0F);
            charge.m_6034_(playerIn.m_20185_(), playerIn.m_20186_() + 1.0, playerIn.m_20189_());
            worldIn.m_7967_(charge);
            charge.m_6686_(d2, d3, d4, 1.0F, 1.0F);
            playerIn.playSound(SoundEvents.ZOMBIE_INFECT, 1.0F, 0.75F + 0.5F * playerIn.m_217043_().nextFloat());
            itemStackIn.hurtAndBreak(1, playerIn, player -> player.m_21190_(hand));
            playerIn.getCooldowns().addCooldown(this, 4);
        }
        return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemStackIn);
    }
}