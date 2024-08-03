package team.lodestar.lodestone.systems.item;

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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;

public class ModCombatItem extends TieredItem {

    private final float attackDamage;

    private final float attackSpeed;

    private Multimap<Attribute, AttributeModifier> attributes;

    public ModCombatItem(Tier tier, float attackDamage, float attackSpeed, Item.Properties builderIn) {
        super(tier, builderIn);
        this.attackDamage = attackDamage;
        this.attackSpeed = attackSpeed;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
        if (this.attributes == null) {
            Builder<Attribute, AttributeModifier> attributeBuilder = new Builder();
            attributeBuilder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(f_41374_, "Weapon modifier", (double) this.attackDamage, AttributeModifier.Operation.ADDITION));
            attributeBuilder.put(Attributes.ATTACK_SPEED, new AttributeModifier(f_41375_, "Weapon modifier", (double) this.attackSpeed, AttributeModifier.Operation.ADDITION));
            attributeBuilder.putAll(this.createExtraAttributes().build());
            this.attributes = attributeBuilder.build();
        }
        return equipmentSlot == EquipmentSlot.MAINHAND ? this.attributes : super.m_7167_(equipmentSlot);
    }

    public Builder<Attribute, AttributeModifier> createExtraAttributes() {
        return new Builder();
    }

    public float getDamage() {
        return this.attackDamage;
    }

    @Override
    public boolean canAttackBlock(BlockState blockState0, Level level1, BlockPos blockPos2, Player player3) {
        return !player3.isCreative();
    }

    @Override
    public float getDestroySpeed(ItemStack pStack, BlockState pState) {
        if (pState.m_60713_(Blocks.COBWEB)) {
            return 15.0F;
        } else {
            return pState.m_204336_(BlockTags.SWORD_EFFICIENT) ? 1.5F : 1.0F;
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

    public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
        return toolAction.equals(ToolActions.SWORD_DIG);
    }
}