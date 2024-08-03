package com.mna.items.constructs.parts.arms;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.mna.api.entities.construct.ConstructCapability;
import com.mna.api.entities.construct.ConstructMaterial;
import com.mna.api.entities.construct.ConstructSlot;
import com.mna.api.entities.construct.ItemConstructPart;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.TierSortingRegistry;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;

public class ConstructPartAxeArmRight extends ItemConstructPart {

    protected static final Map<Block, Block> STRIPPABLES = new com.google.common.collect.ImmutableMap.Builder().put(Blocks.OAK_WOOD, Blocks.STRIPPED_OAK_WOOD).put(Blocks.OAK_LOG, Blocks.STRIPPED_OAK_LOG).put(Blocks.DARK_OAK_WOOD, Blocks.STRIPPED_DARK_OAK_WOOD).put(Blocks.DARK_OAK_LOG, Blocks.STRIPPED_DARK_OAK_LOG).put(Blocks.ACACIA_WOOD, Blocks.STRIPPED_ACACIA_WOOD).put(Blocks.ACACIA_LOG, Blocks.STRIPPED_ACACIA_LOG).put(Blocks.BIRCH_WOOD, Blocks.STRIPPED_BIRCH_WOOD).put(Blocks.BIRCH_LOG, Blocks.STRIPPED_BIRCH_LOG).put(Blocks.JUNGLE_WOOD, Blocks.STRIPPED_JUNGLE_WOOD).put(Blocks.JUNGLE_LOG, Blocks.STRIPPED_JUNGLE_LOG).put(Blocks.SPRUCE_WOOD, Blocks.STRIPPED_SPRUCE_WOOD).put(Blocks.SPRUCE_LOG, Blocks.STRIPPED_SPRUCE_LOG).put(Blocks.WARPED_STEM, Blocks.STRIPPED_WARPED_STEM).put(Blocks.WARPED_HYPHAE, Blocks.STRIPPED_WARPED_HYPHAE).put(Blocks.CRIMSON_STEM, Blocks.STRIPPED_CRIMSON_STEM).put(Blocks.CRIMSON_HYPHAE, Blocks.STRIPPED_CRIMSON_HYPHAE).build();

    private final TagKey<Block> blocks = BlockTags.MINEABLE_WITH_AXE;

    protected final float speed;

    private final float attackDamageBaseline;

    private final Multimap<Attribute, AttributeModifier> defaultModifiers;

    public ConstructPartAxeArmRight(ConstructMaterial material, float damage, float speed) {
        super(material, ConstructSlot.RIGHT_ARM, 8);
        this.speed = material.getEquivalentTier().getSpeed();
        this.attackDamageBaseline = damage + material.getEquivalentTier().getAttackDamageBonus();
        Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(f_41374_, "Tool modifier", (double) this.attackDamageBaseline, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(f_41375_, "Tool modifier", (double) speed, AttributeModifier.Operation.ADDITION));
        this.defaultModifiers = builder.build();
    }

    @Override
    public ConstructCapability[] getEnabledCapabilities() {
        return new ConstructCapability[] { ConstructCapability.MELEE_ATTACK, ConstructCapability.CHOP_WOOD };
    }

    @Override
    public float getAttackDamage() {
        return this.getMaterial().getDamageBonus() * 1.25F;
    }

    @Override
    public int getAttackSpeedModifier() {
        return 10;
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        Level level = ctx.getLevel();
        BlockPos blockpos = ctx.getClickedPos();
        Player player = ctx.getPlayer();
        BlockState blockstate = level.getBlockState(blockpos);
        Optional<BlockState> optional = Optional.ofNullable(blockstate.getToolModifiedState(ctx, ToolActions.AXE_STRIP, false));
        Optional<BlockState> optional1 = Optional.ofNullable(blockstate.getToolModifiedState(ctx, ToolActions.AXE_SCRAPE, false));
        Optional<BlockState> optional2 = Optional.ofNullable(blockstate.getToolModifiedState(ctx, ToolActions.AXE_WAX_OFF, false));
        ItemStack itemstack = ctx.getItemInHand();
        Optional<BlockState> optional3 = Optional.empty();
        if (optional.isPresent()) {
            level.playSound(player, blockpos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0F, 1.0F);
            optional3 = optional;
        } else if (optional1.isPresent()) {
            level.playSound(player, blockpos, SoundEvents.AXE_SCRAPE, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.m_5898_(player, 3005, blockpos, 0);
            optional3 = optional1;
        } else if (optional2.isPresent()) {
            level.playSound(player, blockpos, SoundEvents.AXE_WAX_OFF, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.m_5898_(player, 3004, blockpos, 0);
            optional3 = optional2;
        }
        if (optional3.isPresent()) {
            if (player instanceof ServerPlayer) {
                CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer) player, blockpos, itemstack);
            }
            level.setBlock(blockpos, (BlockState) optional3.get(), 11);
            if (player != null) {
                itemstack.hurtAndBreak(1, player, p_150686_ -> p_150686_.m_21190_(ctx.getHand()));
            }
            return InteractionResult.sidedSuccess(level.isClientSide());
        } else {
            return InteractionResult.PASS;
        }
    }

    @Nullable
    public static BlockState getAxeStrippingState(BlockState originalState) {
        Block block = (Block) STRIPPABLES.get(originalState.m_60734_());
        return block != null ? (BlockState) block.defaultBlockState().m_61124_(RotatedPillarBlock.AXIS, (Direction.Axis) originalState.m_61143_(RotatedPillarBlock.AXIS)) : null;
    }

    public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
        return ToolActions.DEFAULT_AXE_ACTIONS.contains(toolAction);
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