package com.rekindled.embers.item;

import com.rekindled.embers.api.item.IInflictorGem;
import com.rekindled.embers.api.item.IInflictorGemHolder;
import java.util.function.Supplier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class AshenArmorGemItem extends AshenArmorItem implements IInflictorGemHolder {

    public Supplier<Integer> gemSlots;

    public AshenArmorGemItem(ArmorMaterial material, ArmorItem.Type type, Item.Properties properties, Supplier<Integer> gemSlots) {
        super(material, type, properties);
        this.gemSlots = gemSlots;
    }

    @Override
    public int getGemSlots(ItemStack holder) {
        return (Integer) this.gemSlots.get();
    }

    @Override
    public boolean canAttachGem(ItemStack holder, ItemStack gem) {
        return gem.getItem() instanceof IInflictorGem;
    }

    @Override
    public void attachGem(ItemStack holder, ItemStack gem, int slot) {
        holder.getOrCreateTag().put("gem" + slot, gem.serializeNBT());
    }

    @Override
    public ItemStack detachGem(ItemStack holder, int slot) {
        if (holder.getOrCreateTag().contains("gem" + slot)) {
            ItemStack gem = ItemStack.of(holder.getOrCreateTag().getCompound("gem" + slot));
            holder.getOrCreateTag().remove("gem" + slot);
            return gem;
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public void clearGems(ItemStack holder) {
        CompoundTag tagCompound = holder.getOrCreateTag();
        if (tagCompound != null) {
            for (int i = 0; i < this.getGemSlots(holder); i++) {
                if (tagCompound.contains("gem" + i)) {
                    tagCompound.remove("gem" + i);
                }
            }
        }
    }

    @Override
    public ItemStack[] getAttachedGems(ItemStack holder) {
        ItemStack[] stacks = new ItemStack[this.getGemSlots(holder)];
        for (int i = 0; i < stacks.length; i++) {
            if (holder.getOrCreateTag().contains("gem" + i)) {
                stacks[i] = ItemStack.of(holder.getOrCreateTag().getCompound("gem" + i));
            } else {
                stacks[i] = ItemStack.EMPTY;
            }
        }
        return stacks;
    }

    @Override
    public float getTotalDamageResistance(LivingEntity entity, DamageSource source, ItemStack holder) {
        float reduction = 0.0F;
        if (!this.isBroken(holder)) {
            for (ItemStack stack : this.getAttachedGems(holder)) {
                Item item = stack.getItem();
                if (item instanceof IInflictorGem) {
                    IInflictorGem gem = (IInflictorGem) item;
                    if (gem.getAttunedSource(stack).equals(source.type().msgId())) {
                        reduction += gem.getDamageResistance(stack, reduction);
                    }
                }
            }
        }
        return reduction;
    }
}