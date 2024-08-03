package io.redspace.ironsspellbooks.capabilities.magic;

import net.minecraft.world.entity.LivingEntity;

@Deprecated(forRemoval = true)
public class CastTargetingData extends TargetEntityCastData {

    public CastTargetingData(LivingEntity target) {
        super(target);
    }
}