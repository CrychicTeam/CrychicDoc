package org.violetmoon.quark.content.tools.item;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import java.util.HashMap;
import java.util.HashSet;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.quark.base.Quark;
import org.violetmoon.quark.base.handler.QuarkSounds;
import org.violetmoon.quark.content.tools.config.PickarangType;
import org.violetmoon.quark.content.tools.entity.rang.AbstractPickarang;
import org.violetmoon.quark.content.tools.module.PickarangModule;
import org.violetmoon.zeta.item.ZetaItem;
import org.violetmoon.zeta.module.ZetaModule;

public class PickarangItem extends ZetaItem {

    public final PickarangType<?> type;

    public PickarangItem(String regname, ZetaModule module, Item.Properties properties, PickarangType<?> type) {
        super(regname, module, properties);
        this.type = type;
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, @NotNull LivingEntity target, @NotNull LivingEntity attacker) {
        stack.hurtAndBreak(2, attacker, player -> player.broadcastBreakEvent(InteractionHand.MAIN_HAND));
        return true;
    }

    @Override
    public boolean isCorrectToolForDrops(@NotNull BlockState blockIn) {
        return switch(this.type.harvestLevel) {
            case 0 ->
                Items.WOODEN_PICKAXE.isCorrectToolForDrops(blockIn) || this.type.canActAsAxe && Items.WOODEN_AXE.isCorrectToolForDrops(blockIn) || this.type.canActAsShovel && Items.WOODEN_SHOVEL.isCorrectToolForDrops(blockIn) || this.type.canActAsHoe && Items.WOODEN_HOE.isCorrectToolForDrops(blockIn);
            case 1 ->
                Items.STONE_PICKAXE.isCorrectToolForDrops(blockIn) || this.type.canActAsAxe && Items.STONE_AXE.isCorrectToolForDrops(blockIn) || this.type.canActAsShovel && Items.STONE_SHOVEL.isCorrectToolForDrops(blockIn) || this.type.canActAsHoe && Items.STONE_HOE.isCorrectToolForDrops(blockIn);
            case 2 ->
                Items.IRON_PICKAXE.isCorrectToolForDrops(blockIn) || this.type.canActAsAxe && Items.IRON_AXE.isCorrectToolForDrops(blockIn) || this.type.canActAsShovel && Items.IRON_SHOVEL.isCorrectToolForDrops(blockIn) || this.type.canActAsHoe && Items.IRON_HOE.isCorrectToolForDrops(blockIn);
            case 3 ->
                Items.DIAMOND_PICKAXE.isCorrectToolForDrops(blockIn) || this.type.canActAsAxe && Items.DIAMOND_AXE.isCorrectToolForDrops(blockIn) || this.type.canActAsShovel && Items.DIAMOND_SHOVEL.isCorrectToolForDrops(blockIn) || this.type.canActAsHoe && Items.DIAMOND_HOE.isCorrectToolForDrops(blockIn);
            default ->
                Items.NETHERITE_PICKAXE.isCorrectToolForDrops(blockIn) || this.type.canActAsAxe && Items.NETHERITE_AXE.isCorrectToolForDrops(blockIn) || this.type.canActAsShovel && Items.NETHERITE_SHOVEL.isCorrectToolForDrops(blockIn) || this.type.canActAsHoe && Items.NETHERITE_HOE.isCorrectToolForDrops(blockIn);
        };
    }

    @Override
    public int getMaxDamageZeta(ItemStack stack) {
        return Math.max(this.type.durability, 0);
    }

    @Override
    public boolean mineBlock(@NotNull ItemStack stack, @NotNull Level worldIn, BlockState state, @NotNull BlockPos pos, @NotNull LivingEntity entityLiving) {
        if (state.m_60800_(worldIn, pos) != 0.0F) {
            stack.hurtAndBreak(1, entityLiving, player -> player.broadcastBreakEvent(InteractionHand.MAIN_HAND));
        }
        return true;
    }

    @NotNull
    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, @NotNull InteractionHand handIn) {
        ItemStack itemstack = playerIn.m_21120_(handIn);
        playerIn.m_21008_(handIn, ItemStack.EMPTY);
        int eff = Quark.ZETA.itemExtensions.get(itemstack).getEnchantmentLevelZeta(itemstack, Enchantments.BLOCK_EFFICIENCY);
        Vec3 pos = playerIn.m_20182_();
        worldIn.playSound(null, pos.x, pos.y, pos.z, QuarkSounds.ENTITY_PICKARANG_THROW, SoundSource.NEUTRAL, 0.5F + (float) eff * 0.14F, 0.4F / (worldIn.random.nextFloat() * 0.4F + 0.8F));
        if (!worldIn.isClientSide) {
            Inventory inventory = playerIn.getInventory();
            int slot = handIn == InteractionHand.OFF_HAND ? inventory.getContainerSize() - 1 : inventory.selected;
            AbstractPickarang<?> entity = this.type.makePickarang(worldIn, playerIn);
            entity.setThrowData(slot, itemstack);
            entity.shoot(playerIn, playerIn.m_146909_(), playerIn.m_146908_(), 0.0F, 1.5F + (float) eff * 0.325F, 0.0F);
            entity.m_5602_(playerIn);
            worldIn.m_7967_(entity);
            if (playerIn instanceof ServerPlayer sp) {
                PickarangModule.throwPickarangTrigger.trigger(sp);
            }
        }
        if (!playerIn.getAbilities().instabuild && this.type.cooldown > 0) {
            int cooldown = this.type.cooldown - eff;
            if (cooldown > 0) {
                playerIn.getCooldowns().addCooldown(this, cooldown);
            }
        }
        playerIn.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResultHolder.sidedSuccess(itemstack, worldIn.isClientSide);
    }

    @NotNull
    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(@NotNull EquipmentSlot slot) {
        Multimap<Attribute, AttributeModifier> multimap = Multimaps.newSetMultimap(new HashMap(), HashSet::new);
        if (slot == EquipmentSlot.MAINHAND) {
            multimap.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(f_41374_, "Weapon modifier", (double) this.type.attackDamage, AttributeModifier.Operation.ADDITION));
            multimap.put(Attributes.ATTACK_SPEED, new AttributeModifier(f_41375_, "Weapon modifier", -2.8, AttributeModifier.Operation.ADDITION));
        }
        return multimap;
    }

    @Override
    public float getDestroySpeed(@NotNull ItemStack stack, @NotNull BlockState state) {
        return 0.0F;
    }

    @Override
    public boolean isRepairableZeta(@NotNull ItemStack stack) {
        return true;
    }

    @Override
    public boolean isValidRepairItem(@NotNull ItemStack toRepair, ItemStack repair) {
        return this.type.repairMaterial != null && repair.getItem() == this.type.repairMaterial;
    }

    @Override
    public int getEnchantmentValueZeta(ItemStack stack) {
        return this.getEnchantmentValue();
    }

    @Override
    public int getEnchantmentValue() {
        return this.type.pickaxeEquivalent != null ? this.type.pickaxeEquivalent.getEnchantmentValue() : 0;
    }

    @Override
    public boolean canApplyAtEnchantingTableZeta(ItemStack stack, Enchantment enchantment) {
        return super.canApplyAtEnchantingTableZeta(stack, enchantment) || ImmutableSet.of(Enchantments.BLOCK_FORTUNE, Enchantments.SILK_TOUCH, Enchantments.BLOCK_EFFICIENCY).contains(enchantment);
    }
}