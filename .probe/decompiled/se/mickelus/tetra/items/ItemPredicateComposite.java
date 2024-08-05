package se.mickelus.tetra.items;

import java.util.Arrays;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.world.item.ItemStack;

@ParametersAreNonnullByDefault
public class ItemPredicateComposite extends ItemPredicate {

    ItemPredicate[] predicates;

    public ItemPredicateComposite(ItemPredicate[] predicates) {
        this.predicates = predicates;
    }

    @Override
    public boolean matches(ItemStack item) {
        return Arrays.stream(this.predicates).anyMatch(predicate -> predicate.matches(item));
    }
}