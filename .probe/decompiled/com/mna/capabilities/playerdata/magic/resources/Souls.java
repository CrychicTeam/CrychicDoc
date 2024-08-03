package com.mna.capabilities.playerdata.magic.resources;

import com.mna.api.capabilities.resource.CastingResourceIDs;
import com.mna.api.capabilities.resource.SimpleCastingResource;
import com.mna.api.config.GeneralConfigValues;
import com.mna.api.items.INoSoulsRecharge;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class Souls extends SimpleCastingResource {

    public Souls() {
        super(GeneralConfigValues.TotalManaRegenTicks);
    }

    @Override
    public ResourceLocation getRegistryName() {
        return CastingResourceIDs.SOULS;
    }

    @Override
    public void setMaxAmountByLevel(int level) {
        this.setMaxAmount((float) (100 + 10 * level * 15));
    }

    @Override
    public int getRegenerationRate(LivingEntity caster) {
        return -1;
    }

    @Override
    public void addRegenerationModifier(String id, float pct) {
    }

    @Override
    public boolean canRechargeFrom(ItemStack batteryItem) {
        return !(batteryItem.getItem() instanceof INoSoulsRecharge);
    }

    @Override
    public boolean artificialRestore() {
        return false;
    }
}