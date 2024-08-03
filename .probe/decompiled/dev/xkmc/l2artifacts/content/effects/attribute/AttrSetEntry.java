package dev.xkmc.l2artifacts.content.effects.attribute;

import dev.xkmc.l2artifacts.init.registrate.entries.LinearFuncEntry;
import java.util.function.Supplier;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public record AttrSetEntry(Supplier<Attribute> attr, AttributeModifier.Operation op, LinearFuncEntry func, boolean usePercent) {

    public double getValue(int rank) {
        return this.func().getFromRank(rank);
    }
}