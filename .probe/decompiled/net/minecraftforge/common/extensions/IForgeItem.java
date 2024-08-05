package net.minecraftforge.common.extensions;

import com.google.common.collect.Multimap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.wrapper.ShulkerItemStackInvWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IForgeItem {

    private Item self() {
        return (Item) this;
    }

    default Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        return this.self().getDefaultAttributeModifiers(slot);
    }

    default boolean onDroppedByPlayer(ItemStack item, Player player) {
        return true;
    }

    default Component getHighlightTip(ItemStack item, Component displayName) {
        return displayName;
    }

    default InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        return InteractionResult.PASS;
    }

    default boolean isPiglinCurrency(ItemStack stack) {
        return stack.getItem() == PiglinAi.BARTERING_ITEM;
    }

    default boolean makesPiglinsNeutral(ItemStack stack, LivingEntity wearer) {
        return stack.getItem() instanceof ArmorItem && ((ArmorItem) stack.getItem()).getMaterial() == ArmorMaterials.GOLD;
    }

    boolean isRepairable(ItemStack var1);

    default float getXpRepairRatio(ItemStack stack) {
        return 2.0F;
    }

    @Nullable
    default CompoundTag getShareTag(ItemStack stack) {
        return stack.getTag();
    }

    default void readShareTag(ItemStack stack, @Nullable CompoundTag nbt) {
        stack.setTag(nbt);
    }

    default boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, Player player) {
        return false;
    }

    default void onStopUsing(ItemStack stack, LivingEntity entity, int count) {
    }

    default boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        return false;
    }

    default ItemStack getCraftingRemainingItem(ItemStack itemStack) {
        return !this.hasCraftingRemainingItem(itemStack) ? ItemStack.EMPTY : new ItemStack(this.self().getCraftingRemainingItem());
    }

    default boolean hasCraftingRemainingItem(ItemStack stack) {
        return this.self().hasCraftingRemainingItem();
    }

    default int getEntityLifespan(ItemStack itemStack, Level level) {
        return 6000;
    }

    default boolean hasCustomEntity(ItemStack stack) {
        return false;
    }

    @Nullable
    default Entity createEntity(Level level, Entity location, ItemStack stack) {
        return null;
    }

    default boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
        return false;
    }

    default boolean doesSneakBypassUse(ItemStack stack, LevelReader level, BlockPos pos, Player player) {
        return false;
    }

    @Deprecated(forRemoval = true, since = "1.20.1")
    default void onArmorTick(ItemStack stack, Level level, Player player) {
    }

    default void onInventoryTick(ItemStack stack, Level level, Player player, int slotIndex, int selectedIndex) {
        Inventory inv = player.getInventory();
        int vanillaIndex = slotIndex;
        if (slotIndex >= inv.items.size()) {
            vanillaIndex = slotIndex - inv.items.size();
            if (vanillaIndex >= inv.armor.size()) {
                vanillaIndex -= inv.armor.size();
            } else {
                this.onArmorTick(stack, level, player);
            }
        }
        stack.inventoryTick(level, player, vanillaIndex, selectedIndex == vanillaIndex);
    }

    default boolean canEquip(ItemStack stack, EquipmentSlot armorType, Entity entity) {
        return Mob.m_147233_(stack) == armorType;
    }

    @Nullable
    default EquipmentSlot getEquipmentSlot(ItemStack stack) {
        return null;
    }

    default boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return true;
    }

    @Nullable
    default String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return null;
    }

    default boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        return false;
    }

    default int getDamage(ItemStack stack) {
        return !stack.hasTag() ? 0 : stack.getTag().getInt("Damage");
    }

    default int getMaxDamage(ItemStack stack) {
        return this.self().getMaxDamage();
    }

    default boolean isDamaged(ItemStack stack) {
        return stack.getDamageValue() > 0;
    }

    default void setDamage(ItemStack stack, int damage) {
        stack.getOrCreateTag().putInt("Damage", Math.max(0, damage));
    }

    default boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
        return false;
    }

    default boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
        return this.self().isCorrectToolForDrops(state);
    }

    default int getMaxStackSize(ItemStack stack) {
        return this.self().getMaxStackSize();
    }

    default int getEnchantmentValue(ItemStack stack) {
        return this.self().getEnchantmentValue();
    }

    default boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return enchantment.category.canEnchant(stack.getItem());
    }

    default int getEnchantmentLevel(ItemStack stack, Enchantment enchantment) {
        return EnchantmentHelper.getTagEnchantmentLevel(enchantment, stack);
    }

    default Map<Enchantment, Integer> getAllEnchantments(ItemStack stack) {
        return EnchantmentHelper.deserializeEnchantments(stack.getEnchantmentTags());
    }

    default boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return !oldStack.equals(newStack);
    }

    default boolean shouldCauseBlockBreakReset(ItemStack oldStack, ItemStack newStack) {
        if (!newStack.is(oldStack.getItem())) {
            return true;
        } else if (newStack.isDamageableItem() && oldStack.isDamageableItem()) {
            CompoundTag newTag = newStack.getTag();
            CompoundTag oldTag = oldStack.getTag();
            if (newTag != null && oldTag != null) {
                Set<String> newKeys = new HashSet(newTag.getAllKeys());
                Set<String> oldKeys = new HashSet(oldTag.getAllKeys());
                newKeys.remove("Damage");
                oldKeys.remove("Damage");
                return !newKeys.equals(oldKeys) ? true : !newKeys.stream().allMatch(key -> Objects.equals(newTag.get(key), oldTag.get(key)));
            } else {
                return newTag != null || oldTag != null;
            }
        } else {
            return !ItemStack.isSameItemSameTags(newStack, oldStack);
        }
    }

    default boolean canContinueUsing(ItemStack oldStack, ItemStack newStack) {
        return oldStack == newStack ? true : !oldStack.isEmpty() && !newStack.isEmpty() && ItemStack.isSameItem(newStack, oldStack);
    }

    @Nullable
    default String getCreatorModId(ItemStack itemStack) {
        return ForgeHooks.getDefaultCreatorModId(itemStack);
    }

    @Nullable
    default ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return ShulkerItemStackInvWrapper.createDefaultProvider(stack);
    }

    default boolean canDisableShield(ItemStack stack, ItemStack shield, LivingEntity entity, LivingEntity attacker) {
        return this instanceof AxeItem;
    }

    default int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
        return -1;
    }

    default void onHorseArmorTick(ItemStack stack, Level level, Mob horse) {
    }

    default <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
        return amount;
    }

    default void onDestroyed(ItemEntity itemEntity, DamageSource damageSource) {
        this.self().onDestroyed(itemEntity);
    }

    default boolean isEnderMask(ItemStack stack, Player player, EnderMan endermanEntity) {
        return stack.getItem() == Blocks.CARVED_PUMPKIN.asItem();
    }

    default boolean canElytraFly(ItemStack stack, LivingEntity entity) {
        return false;
    }

    default boolean elytraFlightTick(ItemStack stack, LivingEntity entity, int flightTicks) {
        return false;
    }

    default boolean canWalkOnPowderedSnow(ItemStack stack, LivingEntity wearer) {
        return stack.is(Items.LEATHER_BOOTS);
    }

    default boolean isDamageable(ItemStack stack) {
        return this.self().canBeDepleted();
    }

    @NotNull
    default AABB getSweepHitBox(@NotNull ItemStack stack, @NotNull Player player, @NotNull Entity target) {
        return target.getBoundingBox().inflate(1.0, 0.25, 1.0);
    }

    default int getDefaultTooltipHideFlags(@NotNull ItemStack stack) {
        return 0;
    }

    @Nullable
    default FoodProperties getFoodProperties(ItemStack stack, @Nullable LivingEntity entity) {
        return this.self().getFoodProperties();
    }

    default boolean isNotReplaceableByPickAction(ItemStack stack, Player player, int inventorySlot) {
        return stack.isEnchanted();
    }

    default boolean canGrindstoneRepair(ItemStack stack) {
        return false;
    }
}