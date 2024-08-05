package dev.xkmc.l2artifacts.content.mobeffects;

import dev.xkmc.l2library.base.effects.api.InherentEffect;
import java.util.List;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.item.ItemStack;

public class FrostShield extends InherentEffect {

    public FrostShield(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public List<ItemStack> getCurativeItems() {
        return List.of();
    }
}