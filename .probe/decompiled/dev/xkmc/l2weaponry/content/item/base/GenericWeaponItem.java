package dev.xkmc.l2weaponry.content.item.base;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import dev.xkmc.l2damagetracker.contents.materials.generic.ExtraToolConfig;
import dev.xkmc.l2weaponry.init.data.LWConfig;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import org.jetbrains.annotations.Nullable;

public class GenericWeaponItem extends WeaponItem implements LWTieredItem {

    private final ExtraToolConfig config;

    public GenericWeaponItem(Tier tier, int damage, float speed, Item.Properties prop, ExtraToolConfig config, TagKey<Block> blocks) {
        super(tier, damage, speed, prop, blocks);
        this.config = config;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        this.config.inventoryTick(stack, level, entity, slot, selected);
        super.m_6883_(stack, level, entity, slot, selected);
    }

    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
        return this.config.damageItem(stack, amount, entity);
    }

    @Override
    public boolean canBeDepleted() {
        return this.config.canBeDepleted;
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity user) {
        this.config.onHit(stack, target, user);
        if (this.config.sword_hit > 0) {
            stack.hurtAndBreak(this.config.sword_hit, user, level -> level.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        }
        return true;
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity entity) {
        if (this.config.tool_mine > 0 && state.m_60800_(level, pos) != 0.0F) {
            stack.hurtAndBreak(this.config.tool_mine, entity, l -> l.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        }
        return true;
    }

    @Override
    public boolean isSharp() {
        return true;
    }

    protected boolean canSweep() {
        return false;
    }

    public ExtraToolConfig getExtraConfig() {
        return this.config;
    }

    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> parent = super.getAttributeModifiers(slot, stack);
        if (slot != EquipmentSlot.MAINHAND) {
            return parent;
        } else {
            Multimap<Attribute, AttributeModifier> cur = HashMultimap.create();
            cur.putAll(parent);
            return this.config.modify(cur, slot, stack);
        }
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        float old = super.getDestroySpeed(stack, state);
        return this.config.getDestroySpeed(stack, state, old);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        this.config.addTooltip(pStack, pTooltipComponents);
    }

    public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
        if (toolAction == ToolActions.SWORD_DIG) {
            return true;
        } else {
            return toolAction == ToolActions.SWORD_SWEEP ? this.canSweep() : false;
        }
    }

    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        if (enchantment == Enchantments.SWEEPING_EDGE) {
            return this.canSweep();
        } else if (enchantment == Enchantments.SHARPNESS) {
            return this.isSharp();
        } else if (enchantment.category == EnchantmentCategory.WEAPON) {
            return true;
        } else if (LWConfig.COMMON.diggerEnchantmentOnWeapon.get() && enchantment.category == EnchantmentCategory.DIGGER) {
            return true;
        } else {
            for (String e : LWConfig.COMMON.extraCompatibleEnchantmentCategories.get()) {
                if (enchantment.category.name().equals(e)) {
                    return true;
                }
            }
            return super.canApplyAtEnchantingTable(stack, enchantment);
        }
    }
}