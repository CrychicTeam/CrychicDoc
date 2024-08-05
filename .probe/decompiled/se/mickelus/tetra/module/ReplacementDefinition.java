package se.mickelus.tetra.module;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.world.item.ItemStack;

@ParametersAreNonnullByDefault
public class ReplacementDefinition {

    public ItemPredicate predicate;

    public ItemStack itemStack;
}