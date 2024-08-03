package org.violetmoon.zetaimplforge.mixin.mixins;

import java.util.List;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ PotionBrewing.class })
public interface AccessorPotionBrewing {

    @Accessor("POTION_MIXES")
    static List<PotionBrewing.Mix<Potion>> zeta$getPotionMixes() {
        throw new UnsupportedOperationException();
    }
}