package dev.xkmc.l2library.base.effects.api;

import java.util.List;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.item.ItemStack;

public class InherentEffect extends MobEffect {

    protected InherentEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    public List<ItemStack> getCurativeItems() {
        return List.of();
    }
}