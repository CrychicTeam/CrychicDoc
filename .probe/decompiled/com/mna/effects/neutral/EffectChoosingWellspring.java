package com.mna.effects.neutral;

import com.mna.effects.interfaces.INoCreeperLingering;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.item.ItemStack;

public class EffectChoosingWellspring extends MobEffect implements INoCreeperLingering {

    public EffectChoosingWellspring() {
        super(MobEffectCategory.BENEFICIAL, 0);
    }

    public List<ItemStack> getCurativeItems() {
        return new ArrayList();
    }
}