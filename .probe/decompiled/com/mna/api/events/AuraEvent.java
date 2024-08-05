package com.mna.api.events;

import javax.annotation.Nullable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.Event.HasResult;

public class AuraEvent extends Event {

    private final Enchantment AuraEnchantment;

    public AuraEvent(Enchantment auraEnchantment) {
        this.AuraEnchantment = auraEnchantment;
    }

    public Enchantment getAura() {
        return this.AuraEnchantment;
    }

    @HasResult
    public static class Compatibility extends AuraEvent {

        private final ItemStack placedOn;

        private final LivingEntity enchanter;

        private final Enchantment otherEnchantment;

        public Compatibility(Enchantment auraEnchantment, @Nullable Enchantment otherEnchantment, ItemStack placedOn, LivingEntity enchanter) {
            super(auraEnchantment);
            this.placedOn = placedOn.copy();
            this.enchanter = enchanter;
            this.otherEnchantment = otherEnchantment;
        }

        public ItemStack getPlacedOn() {
            return this.placedOn;
        }

        public LivingEntity getEnchanter() {
            return this.enchanter;
        }

        @Nullable
        public Enchantment getOtherEnchantment() {
            return this.otherEnchantment;
        }
    }

    public static class Numerics extends AuraEvent {

        private final boolean hasSelfishBelt;

        private final Player source;

        private final LivingEntity affecting;

        private final double radius;

        private float manaCost;

        private int magnitude;

        public Numerics(Enchantment auraEnchantment, Player source, LivingEntity affecting, double radius, int defaultMagnitude, float defaultManaCost, boolean hasSelfishBelt) {
            super(auraEnchantment);
            this.source = source;
            this.affecting = affecting;
            this.hasSelfishBelt = hasSelfishBelt;
            this.radius = radius;
            this.magnitude = defaultMagnitude;
            this.manaCost = defaultManaCost;
        }

        public Player getSource() {
            return this.source;
        }

        public LivingEntity getAffecting() {
            return this.affecting;
        }

        public boolean hasSelfishBelt() {
            return this.hasSelfishBelt;
        }

        public double getRadius() {
            return this.radius;
        }

        public int getMagnitude() {
            return this.magnitude;
        }

        public float getManaCost() {
            return this.manaCost;
        }

        public void setManaCost(float value) {
            this.manaCost = value;
        }

        public void setMagnitude(int value) {
            this.magnitude = value;
        }
    }

    public static class Radius extends AuraEvent {

        private final Player source;

        private double radius;

        public Radius(Enchantment auraEnchantment, Player source, double defaultRadius) {
            super(auraEnchantment);
            this.source = source;
            this.radius = defaultRadius;
        }

        public Player getSource() {
            return this.source;
        }

        public double getRadius() {
            return this.radius;
        }

        public void setRadius(double value) {
            this.radius = value;
        }
    }

    @Cancelable
    public static class Tick extends AuraEvent {

        private final LivingEntity wearer;

        public Tick(Enchantment aurEnchantment, LivingEntity wearer) {
            super(aurEnchantment);
            this.wearer = wearer;
        }

        public LivingEntity getWearer() {
            return this.wearer;
        }
    }
}