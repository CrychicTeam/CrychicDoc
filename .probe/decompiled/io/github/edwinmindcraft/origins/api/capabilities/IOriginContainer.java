package io.github.edwinmindcraft.origins.api.capabilities;

import io.github.apace100.origins.component.OriginComponent;
import io.github.edwinmindcraft.calio.api.registry.ICalioDynamicRegistryManager;
import io.github.edwinmindcraft.origins.api.OriginsAPI;
import io.github.edwinmindcraft.origins.api.origin.Origin;
import io.github.edwinmindcraft.origins.api.origin.OriginLayer;
import io.github.edwinmindcraft.origins.common.network.S2CSynchronizeOrigin;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import net.minecraft.core.Holder;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.Nullable;

public interface IOriginContainer extends INBTSerializable<Tag> {

    static LazyOptional<IOriginContainer> get(@Nullable Entity entity) {
        return entity != null ? entity.getCapability(OriginsAPI.ORIGIN_CONTAINER) : LazyOptional.empty();
    }

    void setOrigin(ResourceKey<OriginLayer> var1, ResourceKey<Origin> var2);

    default void setOrigin(Holder<OriginLayer> layer, Holder<Origin> origin) {
        ResourceKey<OriginLayer> layerKey = (ResourceKey<OriginLayer>) layer.unwrap().map(Function.identity(), originLayer -> (ResourceKey) OriginsAPI.getLayersRegistry().getResourceKey(originLayer).orElseThrow(() -> new IllegalArgumentException("Tried to assign an unregistered layer to player " + this.getOwner().getScoreboardName() + ": " + layer)));
        ResourceKey<Origin> originKey = (ResourceKey<Origin>) origin.unwrap().map(Function.identity(), originLayer -> (ResourceKey) OriginsAPI.getOriginsRegistry().getResourceKey(originLayer).orElseThrow(() -> new IllegalArgumentException("Tried to assign an unregistered origin to player " + this.getOwner().getScoreboardName() + ": " + layer)));
        this.setOrigin(layerKey, originKey);
    }

    default void setOrigin(OriginLayer layer, Origin origin) {
        Optional<ResourceKey<OriginLayer>> layerKey = OriginsAPI.getLayersRegistry().getResourceKey(layer);
        Optional<ResourceKey<Origin>> originKey = OriginsAPI.getOriginsRegistry().getResourceKey(origin);
        if (layerKey.isEmpty()) {
            throw new IllegalArgumentException("Tried to assign an unregistered layer to player " + this.getOwner().getScoreboardName() + ": " + layer);
        } else if (originKey.isEmpty()) {
            throw new IllegalArgumentException("Tried to assign an unregistered origin to player " + this.getOwner().getScoreboardName() + ": " + origin);
        } else {
            this.setOrigin((ResourceKey<OriginLayer>) layerKey.get(), (ResourceKey<Origin>) originKey.get());
        }
    }

    ResourceKey<Origin> getOrigin(ResourceKey<OriginLayer> var1);

    default ResourceKey<Origin> getOrigin(Holder<OriginLayer> layer) {
        ResourceKey<OriginLayer> key = (ResourceKey<OriginLayer>) ((Optional) layer.unwrap().map(Optional::of, OriginsAPI.getLayersRegistry()::m_7854_)).orElse(null);
        Validate.notNull(key, "Cannot get status for unregistered layer.", new Object[0]);
        return this.getOrigin(key);
    }

    @Deprecated
    default ResourceKey<Origin> getOrigin(OriginLayer layer) {
        ResourceKey<OriginLayer> key = (ResourceKey<OriginLayer>) OriginsAPI.getLayersRegistry().getResourceKey(layer).orElse(null);
        Validate.notNull(key, "Cannot get status for unregistered layer.", new Object[0]);
        return this.getOrigin(key);
    }

    boolean hasOrigin(ResourceKey<OriginLayer> var1);

    default boolean hasOrigin(Holder<OriginLayer> layer) {
        ResourceKey<OriginLayer> key = (ResourceKey<OriginLayer>) ((Optional) layer.unwrap().map(Optional::of, OriginsAPI.getLayersRegistry()::m_7854_)).orElse(null);
        Validate.notNull(key, "Cannot get status for unregistered layer.", new Object[0]);
        return this.hasOrigin(key);
    }

    @Deprecated
    default boolean hasOrigin(OriginLayer layer) {
        ResourceKey<OriginLayer> key = (ResourceKey<OriginLayer>) OriginsAPI.getLayersRegistry().getResourceKey(layer).orElse(null);
        Validate.notNull(key, "Cannot get status for unregistered layer.", new Object[0]);
        return this.hasOrigin(key);
    }

    default boolean hasAllOrigins() {
        return OriginsAPI.getActiveLayers().stream().filter(x -> !((OriginLayer) x.value()).empty(this.getOwner())).allMatch(this::hasOrigin);
    }

    boolean hadAllOrigins();

    Map<ResourceKey<OriginLayer>, ResourceKey<Origin>> getOrigins();

    void synchronize();

    boolean shouldSync();

    void tick();

    void validateSynchronization();

    S2CSynchronizeOrigin getSynchronizationPacket();

    boolean checkAutoChoosingLayers(boolean var1);

    void onChosen(ResourceKey<Origin> var1, boolean var2);

    void onChosen(boolean var1);

    void onReload(ICalioDynamicRegistryManager var1);

    OriginComponent asLegacyComponent();

    Player getOwner();
}