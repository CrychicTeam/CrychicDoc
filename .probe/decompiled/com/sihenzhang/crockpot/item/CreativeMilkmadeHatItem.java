package com.sihenzhang.crockpot.item;

import com.sihenzhang.crockpot.integration.curios.MilkmadeHatCuriosCapabilityProvider;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.ModList;

public class CreativeMilkmadeHatItem extends MilkmadeHatItem {

    public CreativeMilkmadeHatItem() {
        super(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC));
    }

    @Override
    public boolean isFoil(ItemStack pStack) {
        return true;
    }

    @Override
    public void onArmorTick(ItemStack stack, Level level, Player player) {
        if (!level.isClientSide && player.getFoodData().needsFood() && !player.getCooldowns().isOnCooldown(this)) {
            player.getFoodData().eat(1, 0.05F);
            player.getCooldowns().addCooldown(this, 20);
        }
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return (ICapabilityProvider) (ModList.get().isLoaded("curios") ? new MilkmadeHatCuriosCapabilityProvider(stack, nbt, true) : super.initCapabilities(stack, nbt));
    }
}