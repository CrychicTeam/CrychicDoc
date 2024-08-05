package org.violetmoon.quark.addons.oddities.util;

import java.util.List;
import net.minecraft.world.item.enchantment.Enchantment;

public record Influence(List<Enchantment> boost, List<Enchantment> dampen) {
}