package dev.xkmc.l2weaponry.content.item.base;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.TierSortingRegistry;
import org.jetbrains.annotations.NotNull;

public class WeaponItem extends TieredItem {

    public float attackDamage;

    public float attackSpeed;

    private final Multimap<Attribute, AttributeModifier> defaultModifiers;

    private final TagKey<Block> blocks;

    public WeaponItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Item.Properties pProperties, TagKey<Block> blocks) {
        super(pTier, pProperties);
        this.blocks = blocks;
        this.attackDamage = (float) pAttackDamageModifier;
        this.attackSpeed = pAttackSpeedModifier;
        Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        this.addModifiers(builder);
        this.defaultModifiers = builder.build();
    }

    protected void addModifiers(Builder<Attribute, AttributeModifier> builder) {
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(f_41374_, "Weapon modifier", (double) this.attackDamage, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(f_41375_, "Weapon modifier", (double) this.attackSpeed, AttributeModifier.Operation.ADDITION));
    }

    public boolean isSwordLike() {
        return true;
    }

    @Override
    public boolean canAttackBlock(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer) {
        return !this.isSwordLike() || !pPlayer.isCreative();
    }

    @Override
    public float getDestroySpeed(ItemStack pStack, BlockState pState) {
        if (this.isSwordLike() && pState.m_60713_(Blocks.COBWEB)) {
            return 15.0F;
        } else {
            return pState.m_204336_(this.blocks) ? this.m_43314_().getSpeed() : 1.0F;
        }
    }

    @Deprecated
    @Override
    public boolean isCorrectToolForDrops(BlockState pBlock) {
        if (TierSortingRegistry.isTierSorted(this.m_43314_())) {
            return TierSortingRegistry.isCorrectTierForDrops(this.m_43314_(), pBlock) && pBlock.m_204336_(this.blocks);
        } else {
            int i = this.m_43314_().getLevel();
            if (i < 3 && pBlock.m_204336_(BlockTags.NEEDS_DIAMOND_TOOL)) {
                return false;
            } else {
                return i < 2 && pBlock.m_204336_(BlockTags.NEEDS_IRON_TOOL) ? false : (i >= 1 || !pBlock.m_204336_(BlockTags.NEEDS_STONE_TOOL)) && pBlock.m_204336_(this.blocks);
            }
        }
    }

    public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
        return state.m_204336_(this.blocks) && TierSortingRegistry.isCorrectTierForDrops(this.m_43314_(), state);
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        pStack.hurtAndBreak(1, pAttacker, user -> user.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        return true;
    }

    @Override
    public boolean mineBlock(ItemStack pStack, Level pLevel, BlockState pState, BlockPos pPos, LivingEntity pEntityLiving) {
        if (pState.m_60800_(pLevel, pPos) != 0.0F) {
            pStack.hurtAndBreak(1, pEntityLiving, user -> user.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        }
        return true;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot pEquipmentSlot) {
        return pEquipmentSlot == EquipmentSlot.MAINHAND ? this.defaultModifiers : super.m_7167_(pEquipmentSlot);
    }

    public AABB getSweepHitBoxImpl(ItemStack stack, LivingEntity attacker, Entity target) {
        return target.getBoundingBox().inflate(1.0, 0.25, 1.0);
    }

    @NotNull
    public final AABB getSweepHitBox(@NotNull ItemStack stack, @NotNull Player player, @NotNull Entity target) {
        return this.getSweepHitBoxImpl(stack, player, target);
    }
}