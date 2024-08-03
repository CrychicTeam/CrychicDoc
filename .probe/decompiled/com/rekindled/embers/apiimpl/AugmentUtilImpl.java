package com.rekindled.embers.apiimpl;

import com.google.common.collect.Lists;
import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.api.augment.IAugment;
import com.rekindled.embers.api.augment.IAugmentUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class AugmentUtilImpl implements IAugmentUtil {

    @Override
    public Collection<IAugment> getAllAugments() {
        return RegistryManager.augmentRegistry.values();
    }

    @Override
    public IAugment getAugment(ResourceLocation name) {
        return (IAugment) RegistryManager.augmentRegistry.get(name);
    }

    @Override
    public List<IAugment> getAugments(ItemStack stack) {
        if (this.hasHeat(stack)) {
            ListTag tagAugments = stack.getOrCreateTag().getCompound("embers:heat_tag").getList("augments", 10);
            if (tagAugments.size() > 0) {
                List<IAugment> results = new ArrayList();
                for (int i = 0; i < tagAugments.size(); i++) {
                    CompoundTag tagAugment = tagAugments.getCompound(i);
                    IAugment augment = this.getAugment(new ResourceLocation(tagAugment.getString("name")));
                    if (augment != null) {
                        results.add(augment);
                    }
                }
                return results;
            }
        }
        return Lists.newArrayList();
    }

    @Override
    public int getTotalAugmentLevel(ItemStack stack) {
        int total = 0;
        if (this.hasHeat(stack)) {
            ListTag list = stack.getOrCreateTag().getCompound("embers:heat_tag").getList("augments", 10);
            for (int i = 0; i < list.size(); i++) {
                CompoundTag compound = list.getCompound(i);
                IAugment augment = this.getAugment(new ResourceLocation(compound.getString("name")));
                if (augment.countTowardsTotalLevel()) {
                    total += compound.getInt("level");
                }
            }
        }
        return total;
    }

    @Override
    public boolean hasAugment(ItemStack stack, IAugment augment) {
        if (this.hasHeat(stack)) {
            ListTag list = stack.getOrCreateTag().getCompound("embers:heat_tag").getList("augments", 10);
            for (int i = 0; i < list.size(); i++) {
                CompoundTag compound = list.getCompound(i);
                if (compound.contains("name") && compound.getString("name").compareTo(augment.getName().toString()) == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void addAugment(ItemStack stack, ItemStack augmentStack, IAugment augment) {
        checkForTag(stack);
        ListTag list = stack.getOrCreateTag().getCompound("embers:heat_tag").getList("augments", 10);
        int level = this.getAugmentLevel(stack, augment);
        if (level == 0) {
            CompoundTag augmentCompound = new CompoundTag();
            augmentCompound.putString("name", augment.getName().toString());
            ListTag items = new ListTag();
            augmentCompound.put("items", items);
            items.add(augmentStack.save(new CompoundTag()));
            augmentCompound.putInt("level", 1);
            list.add(augmentCompound);
        } else {
            for (int i = 0; i < list.size(); i++) {
                CompoundTag augmentCompound = list.getCompound(i);
                if (augmentCompound.contains("name") && augmentCompound.getString("name").compareTo(augment.getName().toString()) == 0) {
                    ListTag items = augmentCompound.getList("items", 10);
                    items.add(augmentStack.save(new CompoundTag()));
                    augmentCompound.putInt("level", level + 1);
                }
            }
        }
        augment.onApply(stack);
    }

    @Override
    public List<ItemStack> removeAllAugments(ItemStack stack) {
        if (this.hasHeat(stack)) {
            CompoundTag tagCompound = stack.getOrCreateTag();
            ListTag tagAugments = tagCompound.getCompound("embers:heat_tag").getList("augments", 10);
            if (tagAugments.size() > 0) {
                List<ItemStack> results = new ArrayList();
                ListTag remainingAugments = new ListTag();
                List<IAugment> removedAugments = new ArrayList();
                for (int i = 0; i < tagAugments.size(); i++) {
                    CompoundTag tagAugment = tagAugments.getCompound(i);
                    IAugment augment = this.getAugment(new ResourceLocation(tagAugment.getString("name")));
                    if (augment != null) {
                        if (!augment.canRemove()) {
                            remainingAugments.add(tagAugment);
                        } else {
                            for (int j = 0; j < tagAugment.getInt("level"); j++) {
                                removedAugments.add(augment);
                            }
                            if (tagAugment.contains("items")) {
                                ListTag items = tagAugment.getList("items", 10);
                                for (int j = 0; j < items.size(); j++) {
                                    results.add(ItemStack.of(items.getCompound(j)));
                                }
                            }
                        }
                    }
                }
                tagCompound.getCompound("embers:heat_tag").put("augments", remainingAugments);
                for (IAugment augment : removedAugments) {
                    augment.onRemove(stack);
                }
                return results;
            }
        }
        return Lists.newArrayList();
    }

    @Override
    public void addAugmentLevel(ItemStack stack, IAugment augment, int levels) {
        this.setAugmentLevel(stack, augment, this.getAugmentLevel(stack, augment) + levels);
    }

    @Override
    public void setAugmentLevel(ItemStack stack, IAugment augment, int level) {
        checkForTag(stack);
        ListTag list = stack.getOrCreateTag().getCompound("embers:heat_tag").getList("augments", 10);
        for (int i = 0; i < list.size(); i++) {
            CompoundTag compound = list.getCompound(i);
            if (compound.contains("name") && compound.getString("name").compareTo(augment.getName().toString()) == 0) {
                compound.putInt("level", level);
            }
        }
    }

    @Override
    public int getAugmentLevel(ItemStack stack, IAugment augment) {
        if (this.hasHeat(stack)) {
            ListTag list = stack.getOrCreateTag().getCompound("embers:heat_tag").getList("augments", 10);
            for (int i = 0; i < list.size(); i++) {
                CompoundTag compound = list.getCompound(i);
                if (compound.contains("name") && compound.getString("name").compareTo(augment.getName().toString()) == 0) {
                    return compound.getInt("level");
                }
            }
        }
        return 0;
    }

    @Override
    public boolean hasHeat(ItemStack stack) {
        return !stack.isEmpty() && stack.hasTag() && stack.getTag().contains("embers:heat_tag");
    }

    @Override
    public void addHeat(ItemStack stack, float heat) {
        checkForTag(stack);
        stack.getTag().getCompound("embers:heat_tag").putFloat("heat", Math.min(this.getMaxHeat(stack), this.getHeat(stack) + heat));
    }

    @Override
    public void setHeat(ItemStack stack, float heat) {
        checkForTag(stack);
        stack.getTag().getCompound("embers:heat_tag").putFloat("heat", heat);
    }

    @Override
    public float getHeat(ItemStack stack) {
        return this.hasHeat(stack) ? stack.getTag().getCompound("embers:heat_tag").getFloat("heat") : 0.0F;
    }

    @Override
    public float getMaxHeat(ItemStack stack) {
        return this.hasHeat(stack) ? 500.0F + 250.0F * stack.getTag().getCompound("embers:heat_tag").getFloat("heat_level") : 0.0F;
    }

    @Override
    public int getLevel(ItemStack stack) {
        return this.hasHeat(stack) ? stack.getTag().getCompound("embers:heat_tag").getInt("heat_level") : 0;
    }

    @Override
    public void setLevel(ItemStack stack, int level) {
        checkForTag(stack);
        stack.getTag().getCompound("embers:heat_tag").putInt("heat_level", level);
    }

    @Override
    public int getArmorAugmentLevel(LivingEntity entity, IAugment augment) {
        int maxLevel = 0;
        if (this.hasHeat(entity.getItemBySlot(EquipmentSlot.HEAD))) {
            int l = this.getAugmentLevel(entity.getItemBySlot(EquipmentSlot.HEAD), augment);
            if (l > maxLevel) {
                maxLevel = l;
            }
        }
        if (this.hasHeat(entity.getItemBySlot(EquipmentSlot.CHEST))) {
            int l = this.getAugmentLevel(entity.getItemBySlot(EquipmentSlot.CHEST), augment);
            if (l > maxLevel) {
                maxLevel = l;
            }
        }
        if (this.hasHeat(entity.getItemBySlot(EquipmentSlot.LEGS))) {
            int l = this.getAugmentLevel(entity.getItemBySlot(EquipmentSlot.LEGS), augment);
            if (l > maxLevel) {
                maxLevel = l;
            }
        }
        if (this.hasHeat(entity.getItemBySlot(EquipmentSlot.FEET))) {
            int l = this.getAugmentLevel(entity.getItemBySlot(EquipmentSlot.FEET), augment);
            if (l > maxLevel) {
                maxLevel = l;
            }
        }
        return maxLevel;
    }

    @Override
    public IAugment registerAugment(IAugment augment) {
        RegistryManager.augmentRegistry.put(augment.getName(), augment);
        return augment;
    }

    public static void checkForTag(ItemStack stack) {
        if (!stack.getOrCreateTag().contains("embers:heat_tag")) {
            stack.getTag().put("embers:heat_tag", new CompoundTag());
            stack.getTag().getCompound("embers:heat_tag").putInt("heat_level", 0);
            stack.getTag().getCompound("embers:heat_tag").putFloat("heat", 0.0F);
            stack.getTag().getCompound("embers:heat_tag").put("augments", new ListTag());
        }
    }
}