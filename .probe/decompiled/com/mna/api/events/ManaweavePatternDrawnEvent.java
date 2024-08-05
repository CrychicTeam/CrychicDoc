package com.mna.api.events;

import com.mna.api.recipes.IManaweavePattern;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.eventbus.api.Event;

public class ManaweavePatternDrawnEvent extends Event {

    private LivingEntity crafter;

    private IManaweavePattern pattern;

    public ManaweavePatternDrawnEvent(IManaweavePattern pattern, LivingEntity crafter) {
        this.pattern = pattern;
        this.crafter = crafter;
    }

    public LivingEntity getCrafter() {
        return this.crafter;
    }

    public IManaweavePattern getPattern() {
        return this.pattern;
    }
}