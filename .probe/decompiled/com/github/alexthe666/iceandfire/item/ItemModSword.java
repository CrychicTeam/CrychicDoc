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
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class ItemModSword extends SwordItem implements DragonSteelOverrides<ItemModSword> {

    private Multimap<Attribute, AttributeModifier> dragonsteelModifiers;

    public ItemModSword(Tier toolmaterial) {
        super(toolmaterial, 3, -2.4F, new Item.Properties());
    }

    public int getMaxDamage(ItemStack stack) {
        return this.isDragonsteel(this.m_43314_()) ? IafConfig.dragonsteelBaseDurability : this.m_43314_().getUses();
    }

    @Deprecated
    @NotNull
    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(@NotNull EquipmentSlot equipmentSlot) {
        return equipmentSlot == EquipmentSlot.MAINHAND && this.isDragonsteel(this.m_43314_()) ? this.bakeDragonsteel() : super.getDefaultAttributeModifiers(equipmentSlot);
    }

    @Deprecated
    @Override
    public Multimap<Attribute, AttributeModifier> bakeDragonsteel() {
        if ((double) this.m_43314_().getAttackDamageBonus() == IafConfig.dragonsteelBaseDamage && this.dragonsteelModifiers != null) {
            return this.dragonsteelModifiers;
        } else {
            Builder<Attribute, AttributeModifier> lvt_5_1_ = ImmutableMultimap.builder();
            lvt_5_1_.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(f_41374_, "Weapon modifier", IafConfig.dragonsteelBaseDamage - 1.0, AttributeModifier.Operation.ADDITION));
            lvt_5_1_.put(Attributes.ATTACK_SPEED, new AttributeModifier(f_41375_, "Weapon modifier", -2.4, AttributeModifier.Operation.ADDITION));
            this.dragonsteelModifiers = lvt_5_1_.build();
            return this.dragonsteelModifiers;
        }
    }

    @Override
    public boolean hurtEnemy(@NotNull ItemStack stack, @NotNull LivingEntity target, @NotNull LivingEntity attacker) {
        this.hurtEnemy(this, stack, target, attacker);
        return super.hurtEnemy(stack, target, attacker);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        super.m_7373_(stack, worldIn, tooltip, flagIn);
        this.appendHoverText(this.m_43314_(), stack, worldIn, tooltip, flagIn);
    }
}