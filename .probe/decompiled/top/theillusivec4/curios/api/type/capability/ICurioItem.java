package top.theillusivec4.curios.api.type.capability;

import com.google.common.collect.Multimap;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nonnull;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;
import top.theillusivec4.curios.api.SlotContext;

public interface ICurioItem {

    ICurio defaultInstance = () -> ItemStack.EMPTY;

    default boolean hasCurioCapability(ItemStack stack) {
        return true;
    }

    default void curioTick(SlotContext slotContext, ItemStack stack) {
        this.curioTick(slotContext.identifier(), slotContext.index(), slotContext.entity(), stack);
    }

    default void onEquip(SlotContext slotContext, ItemStack prevStack, ItemStack stack) {
        this.onEquip(slotContext.identifier(), slotContext.index(), slotContext.entity(), stack);
    }

    default void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        this.onUnequip(slotContext.identifier(), slotContext.index(), slotContext.entity(), stack);
    }

    default boolean canEquip(SlotContext slotContext, ItemStack stack) {
        return this.canEquip(slotContext.identifier(), slotContext.entity(), stack);
    }

    default boolean canUnequip(SlotContext slotContext, ItemStack stack) {
        return this.canUnequip(slotContext.identifier(), slotContext.entity(), stack);
    }

    default List<Component> getSlotsTooltip(List<Component> tooltips, ItemStack stack) {
        return this.getTagsTooltip(tooltips, stack);
    }

    default Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        return this.getAttributeModifiers(slotContext.identifier(), stack);
    }

    default void onEquipFromUse(SlotContext slotContext, ItemStack stack) {
        this.playRightClickEquipSound(slotContext.entity(), stack);
    }

    @Nonnull
    default ICurio.SoundInfo getEquipSound(SlotContext slotContext, ItemStack stack) {
        return new ICurio.SoundInfo(SoundEvents.ARMOR_EQUIP_GENERIC, 1.0F, 1.0F);
    }

    default boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return this.canRightClickEquip(stack);
    }

    default void curioBreak(SlotContext slotContext, ItemStack stack) {
        this.curioBreak(stack, slotContext.entity());
    }

    default boolean canSync(SlotContext slotContext, ItemStack stack) {
        return this.canSync(slotContext.identifier(), slotContext.index(), slotContext.entity(), stack);
    }

    @Nonnull
    default CompoundTag writeSyncData(SlotContext slotContext, ItemStack stack) {
        return this.writeSyncData(stack);
    }

    default void readSyncData(SlotContext slotContext, CompoundTag compound, ItemStack stack) {
        this.readSyncData(compound, stack);
    }

    @Nonnull
    default ICurio.DropRule getDropRule(SlotContext slotContext, DamageSource source, int lootingLevel, boolean recentlyHit, ItemStack stack) {
        return this.getDropRule(slotContext.entity(), stack);
    }

    default List<Component> getAttributesTooltip(List<Component> tooltips, ItemStack stack) {
        return (List<Component>) (this.showAttributesTooltip("", stack) ? tooltips : new ArrayList());
    }

    default int getFortuneLevel(SlotContext slotContext, LootContext lootContext, ItemStack stack) {
        return this.getFortuneBonus(slotContext.identifier(), slotContext.entity(), stack, slotContext.index());
    }

    default int getLootingLevel(SlotContext slotContext, DamageSource source, LivingEntity target, int baseLooting, ItemStack stack) {
        return this.getLootingBonus(slotContext.identifier(), slotContext.entity(), stack, slotContext.index());
    }

    default boolean makesPiglinsNeutral(SlotContext slotContext, ItemStack stack) {
        return stack.makesPiglinsNeutral(slotContext.entity());
    }

    default boolean canWalkOnPowderedSnow(SlotContext slotContext, ItemStack stack) {
        return stack.canWalkOnPowderedSnow(slotContext.entity());
    }

    default boolean isEnderMask(SlotContext slotContext, EnderMan enderMan, ItemStack stack) {
        return slotContext.entity() instanceof Player player ? stack.isEnderMask(player, enderMan) : false;
    }

    @Deprecated(forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.21")
    default void curioTick(String identifier, int index, LivingEntity livingEntity, ItemStack stack) {
        defaultInstance.curioTick(identifier, index, livingEntity);
    }

    @Deprecated(forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.21")
    default void curioAnimate(String identifier, int index, LivingEntity livingEntity, ItemStack stack) {
        defaultInstance.curioAnimate(identifier, index, livingEntity);
    }

    @Deprecated(forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.21")
    default void onEquip(String identifier, int index, LivingEntity livingEntity, ItemStack stack) {
        defaultInstance.onEquip(identifier, index, livingEntity);
    }

    @Deprecated(forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.21")
    default void onUnequip(String identifier, int index, LivingEntity livingEntity, ItemStack stack) {
        defaultInstance.onUnequip(identifier, index, livingEntity);
    }

    @Deprecated(forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.21")
    default boolean canEquip(String identifier, LivingEntity livingEntity, ItemStack stack) {
        return defaultInstance.canEquip(identifier, livingEntity);
    }

    @Deprecated(forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.21")
    default boolean canUnequip(String identifier, LivingEntity livingEntity, ItemStack stack) {
        return defaultInstance.canUnequip(identifier, livingEntity);
    }

    @Deprecated(forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.21")
    default List<Component> getTagsTooltip(List<Component> tagTooltips, ItemStack stack) {
        return defaultInstance.getTagsTooltip(tagTooltips);
    }

    @Deprecated(forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.21")
    default void playRightClickEquipSound(LivingEntity livingEntity, ItemStack stack) {
        ICurio.SoundInfo soundInfo = this.getEquipSound(new SlotContext("", livingEntity, 0, false, true), stack);
        livingEntity.m_9236_().playSound(null, livingEntity.m_20183_(), soundInfo.getSoundEvent(), livingEntity.m_5720_(), soundInfo.getVolume(), soundInfo.getPitch());
    }

    @Deprecated(forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.21")
    default boolean canRightClickEquip(ItemStack stack) {
        return defaultInstance.canRightClickEquip();
    }

    @Deprecated(forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.21")
    default Multimap<Attribute, AttributeModifier> getAttributeModifiers(String identifier, ItemStack stack) {
        return defaultInstance.getAttributeModifiers(identifier);
    }

    @Deprecated(forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.21")
    default void curioBreak(ItemStack stack, LivingEntity livingEntity) {
        defaultInstance.curioBreak(stack, livingEntity);
    }

    @Deprecated(forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.21")
    default boolean canSync(String identifier, int index, LivingEntity livingEntity, ItemStack stack) {
        return defaultInstance.canSync(identifier, index, livingEntity);
    }

    @Nonnull
    @Deprecated(forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.21")
    default CompoundTag writeSyncData(ItemStack stack) {
        return defaultInstance.writeSyncData();
    }

    @Deprecated(forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.21")
    default void readSyncData(CompoundTag compound, ItemStack stack) {
        defaultInstance.readSyncData(compound);
    }

    @Nonnull
    @Deprecated(forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.21")
    default ICurio.DropRule getDropRule(LivingEntity livingEntity, ItemStack stack) {
        return defaultInstance.getDropRule(livingEntity);
    }

    @Deprecated(forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.21")
    default boolean showAttributesTooltip(String identifier, ItemStack stack) {
        return defaultInstance.showAttributesTooltip(identifier);
    }

    @Deprecated(forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.21")
    default int getFortuneBonus(String identifier, LivingEntity livingEntity, ItemStack curio, int index) {
        return defaultInstance.getFortuneBonus(identifier, livingEntity, curio, index);
    }

    @Deprecated(forRemoval = true)
    @ScheduledForRemoval(inVersion = "1.21")
    default int getLootingBonus(String identifier, LivingEntity livingEntity, ItemStack curio, int index) {
        return defaultInstance.getLootingBonus(identifier, livingEntity, curio, index);
    }
}