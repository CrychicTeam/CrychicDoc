package com.mna.capabilities.playerdata.magic.resources;

import com.mna.api.capabilities.resource.CastingResourceIDs;
import com.mna.api.capabilities.resource.SimpleCastingResource;
import com.mna.api.config.GeneralConfigValues;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class WinterIce extends SimpleCastingResource {

    public WinterIce() {
        super(GeneralConfigValues.TotalManaRegenTicks);
    }

    @Override
    public int getRegenerationRate(LivingEntity caster) {
        return (int) ((float) GeneralConfigValues.TotalManaRegenTicks * this.getRegenerationModifier(caster));
    }

    @Override
    public ResourceLocation getRegistryName() {
        return CastingResourceIDs.WINTER_ICE;
    }

    @Override
    public void setMaxAmountByLevel(int level) {
        this.setMaxAmount((float) (100 + 20 * level));
    }
}