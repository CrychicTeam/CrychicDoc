package net.minecraft.world.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class SwordItem extends TieredItem implements Vanishable {

    private final float attackDamage;

    private final Multimap<Attribute, AttributeModifier> defaultModifiers;

    public SwordItem(Tier tier0, int int1, float float2, Item.Properties itemProperties3) {
        super(tier0, itemProperties3);
        this.attackDamage = (float) int1 + tier0.getAttackDamageBonus();
        Builder<Attribute, AttributeModifier> $$4 = ImmutableMultimap.builder();
        $$4.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(f_41374_, "Weapon modifier", (double) this.attackDamage, AttributeModifier.Operation.ADDITION));
        $$4.put(Attributes.ATTACK_SPEED, new AttributeModifier(f_41375_, "Weapon modifier", (double) float2, AttributeModifier.Operation.ADDITION));
        this.defaultModifiers = $$4.build();
    }

    public float getDamage() {
        return this.attackDamage;
    }

    @Override
    public boolean canAttackBlock(BlockState blockState0, Level level1, BlockPos blockPos2, Player player3) {
        return !player3.isCreative();
    }

    @Override
    public float getDestroySpeed(ItemStack itemStack0, BlockState blockState1) {
        if (blockState1.m_60713_(Blocks.COBWEB)) {
            return 15.0F;
        } else {
            return blockState1.m_204336_(BlockTags.SWORD_EFFICIENT) ? 1.5F : 1.0F;
        }
    }

    @Override
    public boolean hurtEnemy(ItemStack itemStack0, LivingEntity livingEntity1, LivingEntity livingEntity2) {
        itemStack0.hurtAndBreak(1, livingEntity2, p_43296_ -> p_43296_.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        return true;
    }

    @Override
    public boolean mineBlock(ItemStack itemStack0, Level level1, BlockState blockState2, BlockPos blockPos3, LivingEntity livingEntity4) {
        if (blockState2.m_60800_(level1, blockPos3) != 0.0F) {
            itemStack0.hurtAndBreak(2, livingEntity4, p_43276_ -> p_43276_.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        }
        return true;
    }

    @Override
    public boolean isCorrectToolForDrops(BlockState blockState0) {
        return blockState0.m_60713_(Blocks.COBWEB);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot0) {
        return equipmentSlot0 == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.m_7167_(equipmentSlot0);
    }
}