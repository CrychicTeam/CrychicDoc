package io.github.edwinmindcraft.origins.api.event;

import io.github.apace100.origins.badge.Badge;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.Event;

public class AutoBadgeEvent extends Event {

    private final ResourceLocation registryName;

    private final ConfiguredPower<?, ?> power;

    private final List<Badge> badges;

    public AutoBadgeEvent(ResourceLocation registryName, ConfiguredPower<?, ?> power, List<Badge> badges) {
        this.registryName = registryName;
        this.power = power;
        this.badges = badges;
    }

    public ResourceLocation getRegistryName() {
        return this.registryName;
    }

    public ConfiguredPower<?, ?> getPower() {
        return this.power;
    }

    public List<Badge> getBadges() {
        return this.badges;
    }
}