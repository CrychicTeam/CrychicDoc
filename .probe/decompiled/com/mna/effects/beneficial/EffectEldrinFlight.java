package com.mna.effects.beneficial;

import com.mna.effects.interfaces.IInputDisable;
import com.mna.effects.interfaces.INoCreeperLingering;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.item.ItemStack;

public class EffectEldrinFlight extends MobEffect implements IInputDisable, INoCreeperLingering {

    public EffectEldrinFlight() {
        super(MobEffectCategory.BENEFICIAL, 0);
    }

    @Override
    public EnumSet<IInputDisable.InputMask> getDisabledFlags() {
        return EnumSet.of(IInputDisable.InputMask.LEFT_CLICK, IInputDisable.InputMask.RIGHT_CLICK, IInputDisable.InputMask.MOVEMENT);
    }

    public List<ItemStack> getCurativeItems() {
        return new ArrayList();
    }
}