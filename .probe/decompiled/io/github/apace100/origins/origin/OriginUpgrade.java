package io.github.apace100.origins.origin;

import com.mojang.serialization.Codec;
import io.github.edwinmindcraft.origins.api.OriginsAPI;
import io.github.edwinmindcraft.origins.api.registry.OriginsDynamicRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

@Deprecated
public class OriginUpgrade {

    public static final Codec<OriginUpgrade> CODEC = io.github.edwinmindcraft.origins.api.origin.OriginUpgrade.MAP_CODEC.xmap(OriginUpgrade::new, OriginUpgrade::getWrapped).codec();

    private final io.github.edwinmindcraft.origins.api.origin.OriginUpgrade wrapped;

    public OriginUpgrade(ResourceLocation advancementCondition, ResourceLocation upgradeToOrigin, String announcement) {
        this(new io.github.edwinmindcraft.origins.api.origin.OriginUpgrade(advancementCondition, OriginsAPI.getOriginsRegistry().m_246971_(ResourceKey.create(OriginsDynamicRegistries.ORIGINS_REGISTRY, upgradeToOrigin)), announcement));
    }

    public OriginUpgrade(io.github.edwinmindcraft.origins.api.origin.OriginUpgrade wrapped) {
        this.wrapped = wrapped;
    }

    public io.github.edwinmindcraft.origins.api.origin.OriginUpgrade getWrapped() {
        return this.wrapped;
    }

    public ResourceLocation getAdvancementCondition() {
        return this.wrapped.advancement();
    }

    public ResourceLocation getUpgradeToOrigin() {
        return (ResourceLocation) this.wrapped.origin().unwrapKey().map(ResourceKey::m_135782_).orElse(null);
    }

    public String getAnnouncement() {
        return this.wrapped.announcement();
    }
}