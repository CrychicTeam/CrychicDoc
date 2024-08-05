package com.sihenzhang.crockpot.item.food;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.sihenzhang.crockpot.CrockPotConfigs;
import com.sihenzhang.crockpot.util.I18nUtils;
import com.sihenzhang.crockpot.util.MathUtils;
import com.sihenzhang.crockpot.util.StringUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class CrockPotFoodProperties {

    private final FoodProperties foodProperties;

    final Item.Properties itemProperties;

    private final int duration;

    private final boolean isDrink;

    private final SoundEvent sound;

    private final int cooldown;

    private final float heal;

    private final Pair<ResourceKey<DamageType>, Float> damage;

    private final List<MobEffect> removedEffects;

    private final List<Component> tooltips;

    private final boolean hideEffects;

    private final List<Component> effectTooltips;

    public CrockPotFoodProperties(CrockPotFoodProperties.Builder builder) {
        this.foodProperties = builder.foodBuilder.build();
        this.itemProperties = builder.properties.food(this.foodProperties);
        this.duration = builder.duration;
        this.isDrink = builder.isDrink;
        this.sound = builder.sound;
        this.cooldown = builder.cooldown;
        this.heal = builder.heal;
        this.damage = builder.damage;
        this.removedEffects = List.copyOf(builder.removedEffects);
        this.tooltips = List.copyOf(builder.tooltips);
        this.hideEffects = builder.hideEffects;
        this.effectTooltips = List.copyOf(builder.effectTooltips);
    }

    public static CrockPotFoodProperties.Builder builder() {
        return new CrockPotFoodProperties.Builder();
    }

    public static CrockPotFoodProperties.Builder builder(int nutrition, float saturationModifier) {
        return new CrockPotFoodProperties.Builder(nutrition, saturationModifier);
    }

    public int getUseDuration() {
        return this.duration;
    }

    public UseAnim getUseAnimation() {
        return this.isDrink ? UseAnim.DRINK : UseAnim.EAT;
    }

    public SoundEvent getSound() {
        return this.sound == null ? (this.isDrink ? SoundEvents.GENERIC_DRINK : SoundEvents.GENERIC_EAT) : this.sound;
    }

    public void hurt(Level level, LivingEntity livingEntity) {
        if (!level.isClientSide && this.damage != null) {
            ResourceKey<DamageType> damageTypeKey = (ResourceKey<DamageType>) this.damage.getFirst();
            level.registryAccess().registry(Registries.DAMAGE_TYPE).flatMap(reg -> reg.getHolder(damageTypeKey)).ifPresent(damageType -> livingEntity.hurt(new DamageSource(damageType), (Float) this.damage.getSecond()));
        }
    }

    public void heal(Level level, LivingEntity livingEntity) {
        if (!level.isClientSide && this.heal > 0.0F) {
            livingEntity.heal(this.heal);
        }
    }

    public void removeEffects(Level level, LivingEntity livingEntity) {
        if (!level.isClientSide) {
            this.removedEffects.forEach(livingEntity::m_21195_);
        }
    }

    public void addCooldown(Item item, Player player) {
        player.getCooldowns().addCooldown(item, this.cooldown);
    }

    public List<Component> getTooltips() {
        return this.tooltips;
    }

    public List<Component> getEffectTooltips(boolean hasEaten) {
        if (!CrockPotConfigs.SHOW_FOOD_EFFECTS_TOOLTIP.get() || this.hideEffects) {
            return List.of();
        } else if (!hasEaten) {
            return List.of(I18nUtils.createTooltipComponent("effect.not_eat").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
        } else {
            List<Pair<MobEffectInstance, Float>> effects = this.foodProperties.getEffects();
            if (!effects.isEmpty() || !this.effectTooltips.isEmpty() || !this.removedEffects.isEmpty() || !(this.heal <= 0.0F) || this.damage != null && !((Float) this.damage.getSecond() <= 0.0F)) {
                com.google.common.collect.ImmutableList.Builder<Component> builder = ImmutableList.builder();
                effects.forEach(p -> {
                    MobEffectInstance effect = (MobEffectInstance) p.getFirst();
                    MutableComponent tooltip = Component.translatable(effect.getDescriptionId());
                    if (effect.getAmplifier() > 0) {
                        tooltip = Component.translatable("potion.withAmplifier", tooltip, Component.translatable("potion.potency." + effect.getAmplifier()));
                    }
                    if (!effect.endsWithin(20)) {
                        tooltip = Component.translatable("potion.withDuration", tooltip, MobEffectUtil.formatDuration(effect, 1.0F));
                    }
                    Float probability = (Float) p.getSecond();
                    if (probability < 1.0F) {
                        tooltip = I18nUtils.createTooltipComponent("effect.with_probability", StringUtils.format(probability, "0.##%"), tooltip);
                    }
                    builder.add(tooltip.withStyle(effect.getEffect().getCategory().getTooltipFormatting()));
                });
                if (!this.effectTooltips.isEmpty() || !this.removedEffects.isEmpty() || this.heal > 0.0F || this.damage != null && (Float) this.damage.getSecond() > 0.0F) {
                    builder.add(Component.empty());
                    builder.add(I18nUtils.createTooltipComponent("effect.when_" + (this.isDrink ? "drunk" : "eaten")).withStyle(ChatFormatting.DARK_PURPLE));
                }
                this.effectTooltips.forEach(builder::add);
                this.removedEffects.forEach(e -> builder.add(I18nUtils.createTooltipComponent("effect.remove", Component.translatable(e.getDescriptionId())).withStyle(ChatFormatting.GOLD)));
                if (this.heal > 0.0F) {
                    float hearts = this.heal / 2.0F;
                    builder.add(I18nUtils.createTooltipComponent("effect.heal." + (MathUtils.fuzzyEquals(hearts, 1.0F) ? "single" : "multiple"), StringUtils.format(hearts, "0.#")).withStyle(ChatFormatting.BLUE));
                }
                if (this.damage != null && (Float) this.damage.getSecond() > 0.0F) {
                    float hearts = (Float) this.damage.getSecond() / 2.0F;
                    builder.add(I18nUtils.createTooltipComponent("effect.damage." + (MathUtils.fuzzyEquals(hearts, 1.0F) ? "single" : "multiple"), StringUtils.format(hearts, "0.#")).withStyle(ChatFormatting.RED));
                }
                return builder.build();
            } else {
                return List.of(I18nUtils.createTooltipComponent("effect.no_effect").withStyle(ChatFormatting.DARK_GRAY));
            }
        }
    }

    public static class Builder {

        Item.Properties properties = new Item.Properties();

        FoodProperties.Builder foodBuilder = new FoodProperties.Builder();

        int duration = FoodUseDuration.NORMAL.val;

        boolean isDrink;

        SoundEvent sound;

        int cooldown;

        float heal;

        Pair<ResourceKey<DamageType>, Float> damage;

        final List<MobEffect> removedEffects = new ArrayList();

        final List<Component> tooltips = new ArrayList();

        boolean hideEffects;

        final List<Component> effectTooltips = new ArrayList();

        public Builder() {
        }

        public Builder(int nutrition, float saturationModifier) {
            this.foodBuilder = this.foodBuilder.nutrition(nutrition).saturationMod(saturationModifier);
        }

        public CrockPotFoodProperties.Builder nutrition(int nutrition) {
            this.foodBuilder = this.foodBuilder.nutrition(nutrition);
            return this;
        }

        public CrockPotFoodProperties.Builder saturationMod(float saturationModifier) {
            this.foodBuilder = this.foodBuilder.saturationMod(saturationModifier);
            return this;
        }

        public CrockPotFoodProperties.Builder meat() {
            this.foodBuilder = this.foodBuilder.meat();
            return this;
        }

        public CrockPotFoodProperties.Builder alwaysEat() {
            this.foodBuilder = this.foodBuilder.alwaysEat();
            return this;
        }

        public CrockPotFoodProperties.Builder duration(FoodUseDuration duration) {
            this.duration = duration.val;
            return this;
        }

        public CrockPotFoodProperties.Builder effect(Supplier<MobEffectInstance> effect, float probability) {
            this.foodBuilder = this.foodBuilder.effect(effect, probability);
            return this;
        }

        public CrockPotFoodProperties.Builder effect(Supplier<MobEffectInstance> effect) {
            this.foodBuilder = this.foodBuilder.effect(effect, 1.0F);
            return this;
        }

        public CrockPotFoodProperties.Builder effect(MobEffect effect, int duration, int amplifier, float probability) {
            return this.effect(() -> new MobEffectInstance(effect, duration, amplifier), probability);
        }

        public CrockPotFoodProperties.Builder effect(MobEffect effect, int duration, int amplifier) {
            return this.effect(effect, duration, amplifier, 1.0F);
        }

        public CrockPotFoodProperties.Builder effect(MobEffect effect, int duration, float probability) {
            return this.effect(() -> new MobEffectInstance(effect, duration), probability);
        }

        public CrockPotFoodProperties.Builder effect(MobEffect effect, int duration) {
            return this.effect(effect, duration, 1.0F);
        }

        public CrockPotFoodProperties.Builder effect(Supplier<? extends MobEffect> effect, int duration, int amplifier, float probability) {
            return this.effect(() -> new MobEffectInstance((MobEffect) effect.get(), duration, amplifier), probability);
        }

        public CrockPotFoodProperties.Builder effect(Supplier<? extends MobEffect> effect, int duration, int amplifier) {
            return this.effect(effect, duration, amplifier, 1.0F);
        }

        public CrockPotFoodProperties.Builder effect(Supplier<? extends MobEffect> effect, int duration, float probability) {
            return this.effect(() -> new MobEffectInstance((MobEffect) effect.get(), duration), probability);
        }

        public CrockPotFoodProperties.Builder effect(Supplier<? extends MobEffect> effect, int duration) {
            return this.effect(effect, duration, 1.0F);
        }

        public CrockPotFoodProperties.Builder drink() {
            this.isDrink = true;
            return this;
        }

        public CrockPotFoodProperties.Builder sound(SoundEvent sound) {
            this.sound = sound;
            return this;
        }

        public CrockPotFoodProperties.Builder cooldown(int cooldown) {
            this.cooldown = cooldown;
            return this;
        }

        public CrockPotFoodProperties.Builder heal(float heal) {
            this.heal = heal;
            return this;
        }

        public CrockPotFoodProperties.Builder damage(ResourceKey<DamageType> damageSource, float damageAmount) {
            this.damage = Pair.of(damageSource, damageAmount);
            return this;
        }

        public CrockPotFoodProperties.Builder removeEffect(MobEffect effect) {
            this.removedEffects.add(effect);
            return this;
        }

        public CrockPotFoodProperties.Builder tooltip(String key) {
            this.tooltips.add(I18nUtils.createTooltipComponent(key));
            return this;
        }

        public CrockPotFoodProperties.Builder tooltip(String key, ChatFormatting... formats) {
            this.tooltips.add(I18nUtils.createTooltipComponent(key).withStyle(formats));
            return this;
        }

        public CrockPotFoodProperties.Builder hideEffects() {
            this.hideEffects = true;
            return this;
        }

        public CrockPotFoodProperties.Builder effectTooltip(Component tooltip) {
            this.effectTooltips.add(tooltip);
            return this;
        }

        public CrockPotFoodProperties.Builder effectTooltip(String key, ChatFormatting... formats) {
            return this.effectTooltip(I18nUtils.createTooltipComponent("effect." + key).withStyle(formats));
        }

        public CrockPotFoodProperties.Builder effectTooltip(String key) {
            return this.effectTooltip(I18nUtils.createTooltipComponent("effect." + key));
        }

        public CrockPotFoodProperties.Builder stacksTo(int maxStackSize) {
            this.properties = this.properties.stacksTo(maxStackSize);
            return this;
        }

        public CrockPotFoodProperties.Builder rarity(Rarity rarity) {
            this.properties = this.properties.rarity(rarity);
            return this;
        }

        public CrockPotFoodProperties.Builder craftRemainder(Item craftingRemainingItem) {
            this.properties = this.properties.craftRemainder(craftingRemainingItem);
            return this;
        }

        public CrockPotFoodProperties build() {
            return new CrockPotFoodProperties(this);
        }
    }
}