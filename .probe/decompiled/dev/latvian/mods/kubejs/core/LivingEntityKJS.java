package dev.latvian.mods.kubejs.core;

import dev.latvian.mods.kubejs.bindings.event.ItemEvents;
import dev.latvian.mods.kubejs.entity.EntityPotionEffectsJS;
import dev.latvian.mods.kubejs.entity.RayTraceResultJS;
import dev.latvian.mods.kubejs.item.FoodEatenEventJS;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import dev.latvian.mods.kubejs.platform.LevelPlatformHelper;
import dev.latvian.mods.rhino.util.RemapPrefixForJS;
import java.util.UUID;
import java.util.function.Consumer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

@RemapPrefixForJS("kjs$")
public interface LivingEntityKJS extends EntityKJS {

    UUID KJS_PLAYER_CUSTOM_SPEED = UUID.fromString("6715D9C6-1DA0-4B78-971A-5C32F5709F66");

    String KJS_PLAYER_CUSTOM_SPEED_NAME = "kubejs.player.speed.modifier";

    default LivingEntity kjs$self() {
        return (LivingEntity) this;
    }

    default void kjs$foodEaten(ItemStack is) {
        if (this instanceof LivingEntity entity) {
            FoodEatenEventJS event = new FoodEatenEventJS(entity, is);
            Item i = is.getItem();
            ItemBuilder b = i.kjs$getItemBuilder();
            if (b != null && b.foodBuilder != null && b.foodBuilder.eaten != null) {
                b.foodBuilder.eaten.accept(event);
            }
            if (ItemEvents.FOOD_EATEN.hasListeners()) {
                ItemEvents.FOOD_EATEN.post(entity, i, event);
            }
        }
    }

    @Override
    default boolean kjs$isLiving() {
        return true;
    }

    default void kjs$setMaxHealth(float hp) {
        this.kjs$self().getAttribute(Attributes.MAX_HEALTH).setBaseValue((double) hp);
    }

    default boolean kjs$isUndead() {
        return this.kjs$self().isInvertedHealAndHarm();
    }

    default EntityPotionEffectsJS kjs$getPotionEffects() {
        return new EntityPotionEffectsJS(this.kjs$self());
    }

    default void kjs$swing(InteractionHand hand) {
        this.kjs$self().swing(hand, true);
    }

    default void kjs$swing() {
        this.kjs$self().swing(InteractionHand.MAIN_HAND, true);
    }

    default ItemStack kjs$getEquipment(EquipmentSlot slot) {
        return this.kjs$self().getItemBySlot(slot);
    }

    default void kjs$setEquipment(EquipmentSlot slot, ItemStack item) {
        this.kjs$self().setItemSlot(slot, item);
    }

    default ItemStack kjs$getHeldItem(InteractionHand hand) {
        return this.kjs$self().getItemInHand(hand);
    }

    default void kjs$setHeldItem(InteractionHand hand, ItemStack item) {
        this.kjs$self().setItemInHand(hand, item);
    }

    default ItemStack kjs$getMainHandItem() {
        return this.kjs$getEquipment(EquipmentSlot.MAINHAND);
    }

    default void kjs$setMainHandItem(ItemStack item) {
        this.kjs$setEquipment(EquipmentSlot.MAINHAND, item);
    }

    default ItemStack kjs$getOffHandItem() {
        return this.kjs$getEquipment(EquipmentSlot.OFFHAND);
    }

    default void kjs$setOffHandItem(ItemStack item) {
        this.kjs$setEquipment(EquipmentSlot.OFFHAND, item);
    }

    default ItemStack kjs$getHeadArmorItem() {
        return this.kjs$getEquipment(EquipmentSlot.HEAD);
    }

    default void kjs$setHeadArmorItem(ItemStack item) {
        this.kjs$setEquipment(EquipmentSlot.HEAD, item);
    }

    default ItemStack kjs$getChestArmorItem() {
        return this.kjs$getEquipment(EquipmentSlot.CHEST);
    }

    default void kjs$setChestArmorItem(ItemStack item) {
        this.kjs$setEquipment(EquipmentSlot.CHEST, item);
    }

    default ItemStack kjs$getLegsArmorItem() {
        return this.kjs$getEquipment(EquipmentSlot.LEGS);
    }

    default void kjs$setLegsArmorItem(ItemStack item) {
        this.kjs$setEquipment(EquipmentSlot.LEGS, item);
    }

    default ItemStack kjs$getFeetArmorItem() {
        return this.kjs$getEquipment(EquipmentSlot.FEET);
    }

    default void kjs$setFeetArmorItem(ItemStack item) {
        this.kjs$setEquipment(EquipmentSlot.FEET, item);
    }

    default void kjs$damageEquipment(EquipmentSlot slot, int amount, Consumer<ItemStack> onBroken) {
        ItemStack stack = this.kjs$self().getItemBySlot(slot);
        if (!stack.isEmpty()) {
            stack.hurtAndBreak(amount, this.kjs$self(), livingEntity -> onBroken.accept(stack));
            if (stack.isEmpty()) {
                this.kjs$self().setItemSlot(slot, ItemStack.EMPTY);
            }
        }
    }

    default void kjs$damageEquipment(EquipmentSlot slot, int amount) {
        this.kjs$damageEquipment(slot, amount, stack -> {
        });
    }

    default void kjs$damageEquipment(EquipmentSlot slot) {
        this.kjs$damageEquipment(slot, 1);
    }

    default void kjs$damageHeldItem(InteractionHand hand, int amount, Consumer<ItemStack> onBroken) {
        this.kjs$damageEquipment(hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND, amount, onBroken);
    }

    default void kjs$damageHeldItem(InteractionHand hand, int amount) {
        this.kjs$damageHeldItem(hand, amount, stack -> {
        });
    }

    default void kjs$damageHeldItem() {
        this.kjs$damageHeldItem(InteractionHand.MAIN_HAND, 1);
    }

    default boolean kjs$isHoldingInAnyHand(Ingredient i) {
        return i.test(this.kjs$self().getItemInHand(InteractionHand.MAIN_HAND)) || i.test(this.kjs$self().getItemInHand(InteractionHand.OFF_HAND));
    }

    default double kjs$getTotalMovementSpeed() {
        return this.kjs$self().getAttributeValue(Attributes.MOVEMENT_SPEED);
    }

    default double kjs$getDefaultMovementSpeed() {
        return this.kjs$self().getAttributeBaseValue(Attributes.MOVEMENT_SPEED);
    }

    default void kjs$setDefaultMovementSpeed(double speed) {
        this.kjs$self().getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(speed);
    }

    default void kjs$setMovementSpeedAddition(double speed) {
        AttributeInstance instance = this.kjs$self().getAttribute(Attributes.MOVEMENT_SPEED);
        if (instance != null) {
            instance.removeModifier(KJS_PLAYER_CUSTOM_SPEED);
            instance.addTransientModifier(this.kjs$createSpeedModifier(speed, AttributeModifier.Operation.ADDITION));
        }
    }

    default void kjs$setDefaultMovementSpeedMultiplier(double speed) {
        AttributeInstance instance = this.kjs$self().getAttribute(Attributes.MOVEMENT_SPEED);
        if (instance != null) {
            instance.removeModifier(KJS_PLAYER_CUSTOM_SPEED);
            instance.addTransientModifier(this.kjs$createSpeedModifier(speed, AttributeModifier.Operation.MULTIPLY_BASE));
        }
    }

    default void kjs$setTotalMovementSpeedMultiplier(double speed) {
        AttributeInstance instance = this.kjs$self().getAttribute(Attributes.MOVEMENT_SPEED);
        if (instance != null) {
            instance.removeModifier(KJS_PLAYER_CUSTOM_SPEED);
            instance.addTransientModifier(this.kjs$createSpeedModifier(speed, AttributeModifier.Operation.MULTIPLY_TOTAL));
        }
    }

    default boolean kjs$canEntityBeSeen(LivingEntity entity) {
        return BehaviorUtils.canSee(this.kjs$self(), entity);
    }

    default double kjs$getReachDistance() {
        return LevelPlatformHelper.get().getReachDistance(this.kjs$self());
    }

    default RayTraceResultJS kjs$rayTrace() {
        return this.kjs$rayTrace(this.kjs$getReachDistance());
    }

    default double kjs$getAttributeTotalValue(Attribute attribute) {
        AttributeInstance instance = this.kjs$self().getAttribute(attribute);
        return instance != null ? instance.getValue() : 0.0;
    }

    default double kjs$getAttributeBaseValue(Attribute attribute) {
        AttributeInstance instance = this.kjs$self().getAttribute(attribute);
        return instance != null ? instance.getBaseValue() : 0.0;
    }

    default void kjs$setAttributeBaseValue(Attribute attribute, double value) {
        AttributeInstance instance = this.kjs$self().getAttribute(attribute);
        if (instance != null) {
            instance.setBaseValue(value);
        }
    }

    default void kjs$modifyAttribute(Attribute attribute, String identifier, double d, AttributeModifier.Operation operation) {
        AttributeInstance instance = this.kjs$self().getAttribute(attribute);
        if (instance != null) {
            UUID uuid = new UUID((long) identifier.hashCode(), (long) identifier.hashCode());
            instance.removeModifier(uuid);
            instance.addTransientModifier(new AttributeModifier(uuid, identifier, d, operation));
        }
    }

    default void kjs$removeAttribute(Attribute attribute, String identifier) {
        AttributeInstance instance = this.kjs$self().getAttribute(attribute);
        if (instance != null) {
            instance.removeModifier(new UUID((long) identifier.hashCode(), (long) identifier.hashCode()));
        }
    }

    private AttributeModifier kjs$createSpeedModifier(double speed, AttributeModifier.Operation operation) {
        return new AttributeModifier(KJS_PLAYER_CUSTOM_SPEED, "kubejs.player.speed.modifier", speed, operation);
    }
}