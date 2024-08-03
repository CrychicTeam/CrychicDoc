package net.minecraft.world.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class DiggerItem extends TieredItem implements Vanishable {

    private final TagKey<Block> blocks;

    protected final float speed;

    private final float attackDamageBaseline;

    private final Multimap<Attribute, AttributeModifier> defaultModifiers;

    protected DiggerItem(float float0, float float1, Tier tier2, TagKey<Block> tagKeyBlock3, Item.Properties itemProperties4) {
        super(tier2, itemProperties4);
        this.blocks = tagKeyBlock3;
        this.speed = tier2.getSpeed();
        this.attackDamageBaseline = float0 + tier2.getAttackDamageBonus();
        Builder<Attribute, AttributeModifier> $$5 = ImmutableMultimap.builder();
        $$5.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(f_41374_, "Tool modifier", (double) this.attackDamageBaseline, AttributeModifier.Operation.ADDITION));
        $$5.put(Attributes.ATTACK_SPEED, new AttributeModifier(f_41375_, "Tool modifier", (double) float1, AttributeModifier.Operation.ADDITION));
        this.defaultModifiers = $$5.build();
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

    public float getAttackDamage() {
        return this.attackDamageBaseline;
    }

    @Override
    public boolean isCorrectToolForDrops(BlockState blockState0) {
        int $$1 = this.m_43314_().getLevel();
        if ($$1 < 3 && blockState0.m_204336_(BlockTags.NEEDS_DIAMOND_TOOL)) {
            return false;
        } else if ($$1 < 2 && blockState0.m_204336_(BlockTags.NEEDS_IRON_TOOL)) {
            return false;
        } else {
            return $$1 < 1 && blockState0.m_204336_(BlockTags.NEEDS_STONE_TOOL) ? false : blockState0.m_204336_(this.blocks);
        }
    }
}