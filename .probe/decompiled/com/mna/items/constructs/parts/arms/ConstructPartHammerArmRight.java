package com.mna.items.constructs.parts.arms;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.mna.api.entities.construct.ConstructCapability;
import com.mna.api.entities.construct.ConstructMaterial;
import com.mna.api.entities.construct.ConstructSlot;
import com.mna.api.entities.construct.ItemConstructPart;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.TierSortingRegistry;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;

public class ConstructPartHammerArmRight extends ItemConstructPart {

    private final TagKey<Block> blocks = BlockTags.MINEABLE_WITH_PICKAXE;

    protected final float speed;

    private final float attackDamageBaseline;

    private final Multimap<Attribute, AttributeModifier> defaultModifiers;

    public ConstructPartHammerArmRight(ConstructMaterial material, float damage, float speed) {
        super(material, ConstructSlot.RIGHT_ARM, 2);
        this.speed = material.getEquivalentTier().getSpeed();
        this.attackDamageBaseline = damage + material.getEquivalentTier().getAttackDamageBonus();
        Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(f_41374_, "Tool modifier", (double) this.attackDamageBaseline, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(f_41375_, "Tool modifier", (double) speed, AttributeModifier.Operation.ADDITION));
        this.defaultModifiers = builder.build();
    }

    @Override
    public ConstructCapability[] getEnabledCapabilities() {
        return new ConstructCapability[] { ConstructCapability.MELEE_ATTACK, ConstructCapability.SMITH, ConstructCapability.MINE };
    }

    @Override
    public float getAttackDamage() {
        return this.getMaterial().getDamageBonus();
    }

    @Override
    public int getAttackSpeedModifier() {
        return 15;
    }

    @Override
    public float getKnockbackBonus() {
        return 2.0F;
    }

    public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
        return ToolActions.DEFAULT_PICKAXE_ACTIONS.contains(toolAction);
    }

    @Override
    public float getDestroySpeed(ItemStack itemStack0, BlockState blockState1) {
        return blockState1.m_204336_(this.blocks) ? this.speed : 1.0F;
    }

    @Override
    public boolean hurtEnemy(ItemStack itemStack0, LivingEntity livingEntity1, LivingEntity livingEntity2) {
        itemStack0.hurtAndBreak(2, livingEntity2, p_41007_ -> p_41007_.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        return true;
    }

    @Override
    public boolean mineBlock(ItemStack itemStack0, Level level1, BlockState blockState2, BlockPos blockPos3, LivingEntity livingEntity4) {
        if (!level1.isClientSide && blockState2.m_60800_(level1, blockPos3) != 0.0F) {
            itemStack0.hurtAndBreak(1, livingEntity4, p_40992_ -> p_40992_.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        }
        return true;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot0) {
        return equipmentSlot0 == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.m_7167_(equipmentSlot0);
    }

    @Deprecated
    @Override
    public boolean isCorrectToolForDrops(BlockState blockState0) {
        if (TierSortingRegistry.isTierSorted(this.getMaterial().getEquivalentTier())) {
            return TierSortingRegistry.isCorrectTierForDrops(this.getMaterial().getEquivalentTier(), blockState0) && blockState0.m_204336_(this.blocks);
        } else {
            int i = this.getMaterial().getEquivalentTier().getLevel();
            if (i < 3 && blockState0.m_204336_(BlockTags.NEEDS_DIAMOND_TOOL)) {
                return false;
            } else if (i < 2 && blockState0.m_204336_(BlockTags.NEEDS_IRON_TOOL)) {
                return false;
            } else {
                return i < 1 && blockState0.m_204336_(BlockTags.NEEDS_STONE_TOOL) ? false : blockState0.m_204336_(this.blocks);
            }
        }
    }

    public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
        return state.m_204336_(this.blocks) && TierSortingRegistry.isCorrectTierForDrops(this.getMaterial().getEquivalentTier(), state);
    }
}