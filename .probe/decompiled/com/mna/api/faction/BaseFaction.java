package com.mna.api.faction;

import com.mna.api.capabilities.resource.CastingResourceIDs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public abstract class BaseFaction implements IFaction {

    public static final String NBT_CASTING_RESOURCE_IDX = "faction_casting_resource_idx";

    private ResourceLocation[] __castingResources;

    public BaseFaction() {
        this(CastingResourceIDs.MANA);
    }

    public BaseFaction(ResourceLocation... castingResources) {
        this.__castingResources = castingResources;
    }

    @Override
    public ResourceLocation[] getCastingResources() {
        return this.__castingResources;
    }

    @Override
    public ResourceLocation getCastingResource(Player player) {
        int idx = player.getPersistentData().getInt("faction_casting_resource_idx") % this.__castingResources.length;
        return this.__castingResources[idx];
    }
}