package com.mna.items.armor;

import com.mna.api.items.ITieredItem;
import com.mna.items.ItemInit;
import com.mna.items.base.INoCreativeTab;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.DyeableArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

public class BrokenMageArmor extends DyeableArmorItem implements ITieredItem<BrokenMageArmor>, INoCreativeTab {

    private static final String NBT_RESTORE = "previous_item";

    private static final String NBT_OTHER_ID = "previous_item_id";

    private int _tier = -1;

    public BrokenMageArmor(ArmorItem.Type slot) {
        super(MAArmorMaterial.BROKEN, slot, new Item.Properties().stacksTo(1).setNoRepair());
    }

    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
        return 0;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }

    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return false;
    }

    @Override
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(Component.translatable("item.mna.broken_repair_prompt").withStyle(ChatFormatting.AQUA));
    }

    public static BrokenMageArmor getBySlot(EquipmentSlot slot) {
        if (slot == null) {
            return null;
        } else {
            switch(slot) {
                case CHEST:
                    return ItemInit.BROKEN_ROBES.get();
                case FEET:
                    return ItemInit.BROKEN_BOOTS.get();
                case HEAD:
                    return ItemInit.BROKEN_HOOD.get();
                case LEGS:
                    return ItemInit.BROKEN_LEGGINGS.get();
                case MAINHAND:
                case OFFHAND:
                default:
                    return null;
            }
        }
    }

    public ItemStack convertFrom(ItemStack other) {
        if (other.getItem() instanceof ArmorItem && ((ArmorItem) other.getItem()).getType() == this.m_266204_()) {
            ItemStack outputStack = new ItemStack(this);
            if (other.getItem() instanceof DyeableArmorItem) {
                ((DyeableArmorItem) other.getItem()).m_41115_(outputStack, ((DyeableArmorItem) other.getItem()).m_41121_(other));
            }
            CompoundTag tag = outputStack.getOrCreateTag();
            if (other.hasTag()) {
                tag.put("previous_item", other.getTag());
            }
            tag.putString("previous_item_id", ForgeRegistries.ITEMS.getKey(other.getItem()).toString());
            return outputStack;
        } else {
            return ItemStack.EMPTY;
        }
    }

    public ItemStack restore(ItemStack me) {
        CompoundTag tag = me.getTag();
        if (tag == null) {
            return ItemStack.EMPTY;
        } else {
            ResourceLocation restore = new ResourceLocation(tag.getString("previous_item_id"));
            Item item = ForgeRegistries.ITEMS.getValue(restore);
            if (item == null) {
                return ItemStack.EMPTY;
            } else {
                ItemStack outputStack = new ItemStack(item);
                if (tag.contains("previous_item")) {
                    outputStack.setTag((CompoundTag) tag.get("previous_item"));
                }
                return outputStack;
            }
        }
    }

    public static boolean hasRestore(ItemStack stack) {
        if (stack.getItem() instanceof BrokenMageArmor && stack.hasTag() && stack.getTag().contains("previous_item") && stack.getTag().contains("previous_item_id")) {
            ResourceLocation restore = new ResourceLocation(stack.getTag().getString("previous_item_id"));
            Item item = ForgeRegistries.ITEMS.getValue(restore);
            if (item == null) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void setCachedTier(int tier) {
        this._tier = tier;
    }

    @Override
    public int getCachedTier() {
        return this._tier;
    }
}