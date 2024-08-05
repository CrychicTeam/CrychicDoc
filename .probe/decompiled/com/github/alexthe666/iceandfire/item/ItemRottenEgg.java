package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.entity.EntityCockatriceEgg;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class ItemRottenEgg extends Item {

    public ItemRottenEgg() {
        super(new Item.Properties());
    }

    @NotNull
    @Override
    public InteractionResultHolder<ItemStack> use(@NotNull Level worldIn, Player playerIn, @NotNull InteractionHand handIn) {
        ItemStack itemstack = playerIn.m_21120_(handIn);
        if (!playerIn.isCreative()) {
            itemstack.shrink(1);
        }
        worldIn.playSound(null, playerIn.m_20185_(), playerIn.m_20186_(), playerIn.m_20189_(), SoundEvents.EGG_THROW, SoundSource.PLAYERS, 0.5F, 0.4F / (worldIn.random.nextFloat() * 0.4F + 0.8F));
        if (!worldIn.isClientSide) {
            EntityCockatriceEgg entityegg = new EntityCockatriceEgg(IafEntityRegistry.COCKATRICE_EGG.get(), worldIn, playerIn);
            entityegg.m_37251_(playerIn, playerIn.m_146909_(), playerIn.m_146908_(), 0.0F, 1.5F, 1.0F);
            worldIn.m_7967_(entityegg);
        }
        return new InteractionResultHolder<>(InteractionResult.SUCCESS, itemstack);
    }
}