package dev.xkmc.l2complements.content.enchantment.core;

import java.util.Set;

public interface CraftableEnchantment {

    Set<Integer> DEF = Set.of(1);

    default Set<Integer> getCraftableLevels() {
        return DEF;
    }
}