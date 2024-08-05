package org.violetmoon.quark.content.client.hax;

import com.google.common.collect.Multimap;
import com.mojang.datafixers.util.Pair;
import java.util.List;
import java.util.Map;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.violetmoon.quark.content.client.resources.AttributeSlot;

public interface PseudoAccessorItemStack {

    Map<AttributeSlot, Multimap<Attribute, AttributeModifier>> quark$getCapturedAttributes();

    void quark$capturePotionAttributes(List<Pair<Attribute, AttributeModifier>> var1);
}