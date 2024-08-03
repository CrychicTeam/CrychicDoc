package net.minecraft.world.item;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.Level;

public class EnchantedBookItem extends Item {

    public static final String TAG_STORED_ENCHANTMENTS = "StoredEnchantments";

    public EnchantedBookItem(Item.Properties itemProperties0) {
        super(itemProperties0);
    }

    @Override
    public boolean isFoil(ItemStack itemStack0) {
        return true;
    }

    @Override
    public boolean isEnchantable(ItemStack itemStack0) {
        return false;
    }

    public static ListTag getEnchantments(ItemStack itemStack0) {
        CompoundTag $$1 = itemStack0.getTag();
        return $$1 != null ? $$1.getList("StoredEnchantments", 10) : new ListTag();
    }

    @Override
    public void appendHoverText(ItemStack itemStack0, @Nullable Level level1, List<Component> listComponent2, TooltipFlag tooltipFlag3) {
        super.appendHoverText(itemStack0, level1, listComponent2, tooltipFlag3);
        ItemStack.appendEnchantmentNames(listComponent2, getEnchantments(itemStack0));
    }

    public static void addEnchantment(ItemStack itemStack0, EnchantmentInstance enchantmentInstance1) {
        ListTag $$2 = getEnchantments(itemStack0);
        boolean $$3 = true;
        ResourceLocation $$4 = EnchantmentHelper.getEnchantmentId(enchantmentInstance1.enchantment);
        for (int $$5 = 0; $$5 < $$2.size(); $$5++) {
            CompoundTag $$6 = $$2.getCompound($$5);
            ResourceLocation $$7 = EnchantmentHelper.getEnchantmentId($$6);
            if ($$7 != null && $$7.equals($$4)) {
                if (EnchantmentHelper.getEnchantmentLevel($$6) < enchantmentInstance1.level) {
                    EnchantmentHelper.setEnchantmentLevel($$6, enchantmentInstance1.level);
                }
                $$3 = false;
                break;
            }
        }
        if ($$3) {
            $$2.add(EnchantmentHelper.storeEnchantment($$4, enchantmentInstance1.level));
        }
        itemStack0.getOrCreateTag().put("StoredEnchantments", $$2);
    }

    public static ItemStack createForEnchantment(EnchantmentInstance enchantmentInstance0) {
        ItemStack $$1 = new ItemStack(Items.ENCHANTED_BOOK);
        addEnchantment($$1, enchantmentInstance0);
        return $$1;
    }
}