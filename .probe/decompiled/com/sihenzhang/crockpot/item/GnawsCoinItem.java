package com.sihenzhang.crockpot.item;

import com.sihenzhang.crockpot.effect.CrockPotEffects;
import com.sihenzhang.crockpot.integration.curios.GnawsCoinCuriosCapabilityProvider;
import com.sihenzhang.crockpot.util.I18nUtils;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.ModList;

public class GnawsCoinItem extends CrockPotBaseItem {

    public GnawsCoinItem() {
        super(new Item.Properties().stacksTo(1).fireResistant().rarity(Rarity.EPIC));
    }

    @Override
    public boolean isFoil(ItemStack pStack) {
        return true;
    }

    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }

    public int getEntityLifespan(ItemStack itemStack, Level level) {
        return Integer.MAX_VALUE;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(I18nUtils.createTooltipComponent("gnaws_coin").withStyle(ChatFormatting.AQUA));
        super.m_7373_(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        if (!pLevel.isClientSide && pEntity instanceof Player player && player.f_19797_ % 19 == 0) {
            player.m_7292_(new MobEffectInstance(CrockPotEffects.GNAWS_GIFT.get(), 20, 0, true, true));
        }
    }

    @Nullable
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return (ICapabilityProvider) (ModList.get().isLoaded("curios") ? new GnawsCoinCuriosCapabilityProvider(stack, nbt) : super.initCapabilities(stack, nbt));
    }
}