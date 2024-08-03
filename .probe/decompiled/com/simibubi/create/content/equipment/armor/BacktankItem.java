package com.simibubi.create.content.equipment.armor;

import com.simibubi.create.foundation.item.LayeredArmorItem;
import java.util.Locale;
import java.util.function.Supplier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

public class BacktankItem extends BaseArmorItem implements CapacityEnchantment.ICapacityEnchantable {

    public static final EquipmentSlot SLOT = EquipmentSlot.CHEST;

    public static final ArmorItem.Type TYPE = ArmorItem.Type.CHESTPLATE;

    public static final int BAR_COLOR = 15724527;

    private final Supplier<BacktankItem.BacktankBlockItem> blockItem;

    public BacktankItem(ArmorMaterial material, Item.Properties properties, ResourceLocation textureLoc, Supplier<BacktankItem.BacktankBlockItem> placeable) {
        super(material, TYPE, properties, textureLoc);
        this.blockItem = placeable;
    }

    @Nullable
    public static BacktankItem getWornBy(Entity entity) {
        if (entity instanceof LivingEntity livingEntity) {
            Item var3 = livingEntity.getItemBySlot(SLOT).getItem();
            return var3 instanceof BacktankItem ? (BacktankItem) var3 : null;
        } else {
            return null;
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        return ((BacktankItem.BacktankBlockItem) this.blockItem.get()).m_6225_(ctx);
    }

    @Override
    public boolean canBeDepleted() {
        return false;
    }

    @Override
    public boolean isEnchantable(ItemStack p_77616_1_) {
        return true;
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return Math.round(13.0F * Mth.clamp((float) getRemainingAir(stack) / (float) BacktankUtil.maxAir(stack), 0.0F, 1.0F));
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return 15724527;
    }

    public Block getBlock() {
        return ((BacktankItem.BacktankBlockItem) this.blockItem.get()).m_40614_();
    }

    public static int getRemainingAir(ItemStack stack) {
        CompoundTag orCreateTag = stack.getOrCreateTag();
        return orCreateTag.getInt("Air");
    }

    public static class BacktankBlockItem extends BlockItem {

        private final Supplier<Item> actualItem;

        public BacktankBlockItem(Block block, Supplier<Item> actualItem, Item.Properties properties) {
            super(block, properties);
            this.actualItem = actualItem;
        }

        @Override
        public String getDescriptionId() {
            return this.m_41467_();
        }

        public Item getActualItem() {
            return (Item) this.actualItem.get();
        }
    }

    public static class Layered extends BacktankItem implements LayeredArmorItem {

        public Layered(ArmorMaterial material, Item.Properties properties, ResourceLocation textureLoc, Supplier<BacktankItem.BacktankBlockItem> placeable) {
            super(material, properties, textureLoc, placeable);
        }

        @Override
        public String getArmorTextureLocation(LivingEntity entity, EquipmentSlot slot, ItemStack stack, int layer) {
            return String.format(Locale.ROOT, "%s:textures/models/armor/%s_layer_%d.png", this.textureLoc.getNamespace(), this.textureLoc.getPath(), layer);
        }
    }
}