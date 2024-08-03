package org.violetmoon.quark.mixin.mixins.self;

import java.util.Map;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.LevelReader;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.common.extensions.IForgeItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.violetmoon.quark.addons.oddities.item.BackpackItem;
import org.violetmoon.zeta.item.ext.IZetaItemExtensions;

@Mixin({ BackpackItem.class })
public class IZetaItemMixin_FAKE implements IForgeItem, IZetaItemExtensions {

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        return this.onItemUseFirstZeta(stack, context);
    }

    @Override
    public boolean isRepairable(ItemStack stack) {
        return this.isRepairableZeta(stack);
    }

    @Override
    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
        return this.onEntityItemUpdateZeta(stack, entity);
    }

    @Override
    public boolean doesSneakBypassUse(ItemStack stack, LevelReader level, BlockPos pos, Player player) {
        return this.doesSneakBypassUseZeta(stack, level, pos, player);
    }

    @Override
    public boolean canEquip(ItemStack stack, EquipmentSlot equipmentSlot, Entity entity) {
        return this.canEquipZeta(stack, equipmentSlot, entity);
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return this.isBookEnchantableZeta(stack, book);
    }

    @Nullable
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return this.getArmorTextureZeta(stack, entity, slot, type);
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return this.getMaxDamageZeta(stack);
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
        return toolAction == ToolActions.SHEARS_CARVE ? this.canShearZeta(stack) : false;
    }

    @Override
    public int getEnchantmentValue(ItemStack stack) {
        return this.getEnchantmentValueZeta(stack);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return this.canApplyAtEnchantingTableZeta(stack, enchantment);
    }

    @Override
    public int getEnchantmentLevel(ItemStack stack, Enchantment enchantment) {
        return this.getEnchantmentLevelZeta(stack, enchantment);
    }

    @Override
    public Map<Enchantment, Integer> getAllEnchantments(ItemStack stack) {
        return this.getAllEnchantmentsZeta(stack);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return this.shouldCauseReequipAnimationZeta(oldStack, newStack, slotChanged);
    }

    @Override
    public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
        return this.getBurnTimeZeta(itemStack, recipeType);
    }

    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
        return this.damageItemZeta(stack, amount, entity, onBroken);
    }

    @Override
    public boolean isEnderMask(ItemStack stack, Player player, EnderMan endermanEntity) {
        return this.isEnderMaskZeta(stack, player, endermanEntity);
    }

    @Override
    public boolean canElytraFly(ItemStack stack, LivingEntity entity) {
        return this.canElytraFlyZeta(stack, entity);
    }

    @Override
    public int getDefaultTooltipHideFlags(@NotNull ItemStack stack) {
        return this.getDefaultTooltipHideFlagsZeta(stack);
    }
}