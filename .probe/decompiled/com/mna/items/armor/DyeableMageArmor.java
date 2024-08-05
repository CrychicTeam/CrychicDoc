package com.mna.items.armor;

import com.mna.api.items.ITieredItem;
import com.mna.api.tools.RLoc;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.items.base.IManaRepairable;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.DyeableArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class DyeableMageArmor extends DyeableArmorItem implements ISetItem, ITieredItem<DyeableMageArmor>, IBrokenArmorReplaceable<DyeableMageArmor>, IManaRepairable {

    private static final ResourceLocation mage_armor_set_bonus = RLoc.create("mage_armor_set_bonus");

    private static final String mage_armor_set_bonus_key = "mage_armor_set_bonus";

    private final float manaPerRepairTick;

    private final float repairPerTick;

    private int _tier = -1;

    public DyeableMageArmor(ArmorMaterial armorMaterial, ArmorItem.Type slot, Item.Properties properties, float manaPerRepairTick, float repairPerTick) {
        super(armorMaterial, slot, properties);
        this.manaPerRepairTick = manaPerRepairTick;
        this.repairPerTick = repairPerTick;
    }

    @Override
    public float manaPerRepairTick() {
        return this.manaPerRepairTick;
    }

    @Override
    public float repairPerTick() {
        return this.repairPerTick;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        IManaRepairable.super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
    }

    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
        return IBrokenArmorReplaceable.super.damageItem(stack, amount * 3, entity, onBroken);
    }

    @Override
    public int getColor(ItemStack stack) {
        CompoundTag compoundnbt = stack.getTagElement("display");
        return compoundnbt != null && compoundnbt.contains("color", 99) ? compoundnbt.getInt("color") : -13495757;
    }

    @Override
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        ISetItem.super.addSetTooltip(tooltip);
        super.m_7373_(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public ResourceLocation getSetIdentifier() {
        return mage_armor_set_bonus;
    }

    @Override
    public void applySetBonus(LivingEntity living, EquipmentSlot... setSlots) {
        if (living instanceof Player) {
            ((Player) living).getCapability(PlayerMagicProvider.MAGIC).ifPresent(m -> {
                m.getCastingResource().addModifier("mage_armor_set_bonus", 200.0F);
                m.getCastingResource().addRegenerationModifier("mage_armor_set_bonus", -0.25F);
            });
        }
    }

    @Override
    public void removeSetBonus(LivingEntity living, EquipmentSlot... setSlots) {
        if (living instanceof Player) {
            ((Player) living).getCapability(PlayerMagicProvider.MAGIC).ifPresent(m -> {
                m.getCastingResource().removeModifier("mage_armor_set_bonus");
                m.getCastingResource().removeRegenerationModifier("mage_armor_set_bonus");
            });
        }
    }

    @Override
    public int itemsForSetBonus() {
        return 3;
    }

    @Override
    public int getCachedTier() {
        return this._tier;
    }

    @Override
    public void setCachedTier(int tier) {
        this._tier = tier;
    }
}