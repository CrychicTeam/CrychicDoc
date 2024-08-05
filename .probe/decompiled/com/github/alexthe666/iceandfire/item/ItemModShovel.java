package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.IafConfig;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class ItemModShovel extends ShovelItem implements DragonSteelOverrides<ItemModShovel> {

    private Multimap<Attribute, AttributeModifier> dragonsteelModifiers;

    public ItemModShovel(Tier toolmaterial) {
        super(toolmaterial, 1.5F, -3.0F, new Item.Properties());
    }

    @Deprecated
    @NotNull
    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(@NotNull EquipmentSlot equipmentSlot) {
        return equipmentSlot == EquipmentSlot.MAINHAND && this.isDragonsteel(this.m_43314_()) ? this.bakeDragonsteel() : super.m_7167_(equipmentSlot);
    }

    @Deprecated
    @Override
    public Multimap<Attribute, AttributeModifier> bakeDragonsteel() {
        if ((double) this.m_43314_().getAttackDamageBonus() == IafConfig.dragonsteelBaseDamage && this.dragonsteelModifiers != null) {
            return this.dragonsteelModifiers;
        } else {
            Builder<Attribute, AttributeModifier> lvt_5_1_ = ImmutableMultimap.builder();
            lvt_5_1_.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(f_41374_, "Weapon modifier", IafConfig.dragonsteelBaseDamage - 1.0 + 1.5, AttributeModifier.Operation.ADDITION));
            lvt_5_1_.put(Attributes.ATTACK_SPEED, new AttributeModifier(f_41375_, "Weapon modifier", -3.0, AttributeModifier.Operation.ADDITION));
            this.dragonsteelModifiers = lvt_5_1_.build();
            return this.dragonsteelModifiers;
        }
    }

    public int getMaxDamage(ItemStack stack) {
        return this.isDragonsteel(this.m_43314_()) ? IafConfig.dragonsteelBaseDurability : this.m_43314_().getUses();
    }

    @Override
    public boolean hurtEnemy(@NotNull ItemStack stack, @NotNull LivingEntity target, @NotNull LivingEntity attacker) {
        this.hurtEnemy(this, stack, target, attacker);
        return super.m_7579_(stack, target, attacker);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        this.appendHoverText(this.m_43314_(), stack, worldIn, tooltip, flagIn);
    }
}