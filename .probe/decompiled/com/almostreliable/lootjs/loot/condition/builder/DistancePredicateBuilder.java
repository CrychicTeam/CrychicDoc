package com.almostreliable.lootjs.loot.condition.builder;

import net.minecraft.advancements.critereon.DistancePredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;

public class DistancePredicateBuilder {

    private MinMaxBounds.Doubles x = MinMaxBounds.Doubles.ANY;

    private MinMaxBounds.Doubles y = MinMaxBounds.Doubles.ANY;

    private MinMaxBounds.Doubles z = MinMaxBounds.Doubles.ANY;

    private MinMaxBounds.Doubles horizontal = MinMaxBounds.Doubles.ANY;

    private MinMaxBounds.Doubles absolute = MinMaxBounds.Doubles.ANY;

    public DistancePredicateBuilder x(MinMaxBounds.Doubles bounds) {
        this.x = bounds;
        return this;
    }

    public DistancePredicateBuilder y(MinMaxBounds.Doubles bounds) {
        this.y = bounds;
        return this;
    }

    public DistancePredicateBuilder z(MinMaxBounds.Doubles bounds) {
        this.z = bounds;
        return this;
    }

    public DistancePredicateBuilder horizontal(MinMaxBounds.Doubles bounds) {
        this.horizontal = bounds;
        return this;
    }

    public DistancePredicateBuilder absolute(MinMaxBounds.Doubles bounds) {
        this.absolute = bounds;
        return this;
    }

    public DistancePredicate build() {
        return new DistancePredicate(this.x, this.y, this.z, this.horizontal, this.absolute);
    }
}