package com.rekindled.embers.augment;

import net.minecraft.resources.ResourceLocation;

public class CoreAugment extends AugmentBase {

    public CoreAugment() {
        super(new ResourceLocation("embers", "core"), 0.0);
    }

    @Override
    public boolean countTowardsTotalLevel() {
        return false;
    }

    @Override
    public boolean canRemove() {
        return false;
    }

    @Override
    public boolean shouldRenderTooltip() {
        return false;
    }
}