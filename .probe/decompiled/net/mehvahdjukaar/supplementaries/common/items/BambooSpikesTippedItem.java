package net.mehvahdjukaar.supplementaries.common.items;

import java.util.List;
import net.mehvahdjukaar.moonlight.api.item.WoodBasedBlockItem;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.mehvahdjukaar.supplementaries.reg.ModTags;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import org.jetbrains.annotations.Nullable;

public class BambooSpikesTippedItem extends WoodBasedBlockItem implements SimpleWaterloggedBlock {

    public BambooSpikesTippedItem(Block blockIn, Item.Properties builder) {
        super(blockIn, builder, 150);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.m_7373_(stack, worldIn, tooltip, flagIn);
        PotionUtils.addPotionTooltip(stack, tooltip, 0.1F);
    }

    @Override
    public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
        return false;
    }

    public boolean isRepairable(ItemStack stack) {
        return false;
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return PotionUtils.getColor(stack);
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return !(Boolean) CommonConfigs.Functional.ONLY_ALLOW_HARMFUL.get();
    }

    public static boolean isPotionValid(Potion potion) {
        List<MobEffectInstance> effects = potion.getEffects();
        if ((Boolean) CommonConfigs.Functional.ONLY_ALLOW_HARMFUL.get()) {
            for (MobEffectInstance e : effects) {
                if (e.getEffect().isBeneficial()) {
                    return false;
                }
            }
        }
        return !Utils.isTagged(potion, BuiltInRegistries.POTION, ModTags.TIPPED_SPIKES_POTION_BLACKLIST);
    }

    @Override
    public int getEnchantmentValue() {
        return 0;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }

    @Override
    public String getDescriptionId(ItemStack stack) {
        return "item.supplementaries.bamboo_spikes_tipped";
    }

    @Override
    public Component getName(ItemStack stack) {
        Potion p = PotionUtils.getPotion(stack);
        Component arrowName = Component.translatable(p.getName("item.minecraft.tipped_arrow.effect."));
        String s = arrowName.getString();
        return s.contains("Arrow of ") ? Component.translatable("item.supplementaries.bamboo_spikes_tipped_effect", s.replace("Arrow of ", "")) : Component.translatable(this.getDescriptionId(stack));
    }

    @Override
    public ItemStack getDefaultInstance() {
        return makeSpikeItem(Potions.POISON);
    }

    public static ItemStack makeSpikeItem(Potion potion) {
        ItemStack stack = new ItemStack((ItemLike) ModRegistry.BAMBOO_SPIKES_TIPPED_ITEM.get());
        PotionUtils.setPotion(stack, potion);
        return stack;
    }
}