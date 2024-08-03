package dev.xkmc.l2hostility.compat.gateway;

import dev.shadowsoffire.gateways.gate.Wave;
import dev.xkmc.l2hostility.content.config.EntityConfig;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import java.util.List;
import java.util.function.Function;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;

public record HostilityGateways(MobTrait trait, int count, int xp, ItemLike mat, List<Wave> waves, List<Function<ResourceLocation, EntityConfig.Config>> configs) {

    public ResourceLocation path() {
        return new ResourceLocation("gateways", "hostility/" + this.trait().getRegistryName().getPath());
    }
}