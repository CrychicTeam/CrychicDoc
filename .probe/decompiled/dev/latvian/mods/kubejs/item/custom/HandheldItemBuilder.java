package dev.latvian.mods.kubejs.item.custom;

import dev.latvian.mods.kubejs.item.ItemBuilder;
import dev.latvian.mods.kubejs.item.MutableToolTier;
import dev.latvian.mods.kubejs.typings.Info;
import java.util.function.Consumer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;

public abstract class HandheldItemBuilder extends ItemBuilder {

    public transient MutableToolTier toolTier = new MutableToolTier(Tiers.IRON);

    public transient float attackDamageBaseline;

    public transient float speedBaseline;

    public HandheldItemBuilder(ResourceLocation i, float d, float s) {
        super(i);
        this.attackDamageBaseline = d;
        this.speedBaseline = s;
        this.parentModel("minecraft:item/handheld");
        this.unstackable();
    }

    public HandheldItemBuilder tier(Tier t) {
        this.toolTier = t instanceof MutableToolTier mtt ? mtt : new MutableToolTier(t);
        return this;
    }

    @Info("Sets the base attack damage of the tool. Different tools have different baselines.\n\nFor example, a sword has a baseline of 3, while an axe has a baseline of 6.\n\nThe actual damage is the sum of the baseline and the attackDamageBonus from tier.\n")
    public HandheldItemBuilder attackDamageBaseline(float f) {
        this.attackDamageBaseline = f;
        return this;
    }

    @Info("Sets the base attack speed of the tool. Different tools have different baselines.\n\nFor example, a sword has a baseline of -2.4, while an axe has a baseline of -3.1.\n\nThe actual speed is the sum of the baseline and the speed from tier + 4 (bare hand).\n")
    public HandheldItemBuilder speedBaseline(float f) {
        this.speedBaseline = f;
        return this;
    }

    @Info("Modifies the tool tier.")
    public HandheldItemBuilder modifyTier(Consumer<MutableToolTier> callback) {
        callback.accept(this.toolTier);
        return this;
    }

    @Info("Sets the attack damage bonus of the tool.")
    public HandheldItemBuilder attackDamageBonus(float f) {
        this.toolTier.setAttackDamageBonus(f);
        return this;
    }

    @Info("Sets the attack speed of the tool.")
    public HandheldItemBuilder speed(float f) {
        this.toolTier.setSpeed(f);
        return this;
    }
}