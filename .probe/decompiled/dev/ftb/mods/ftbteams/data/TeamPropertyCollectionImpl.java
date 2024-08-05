package dev.ftb.mods.ftbteams.data;

import dev.ftb.mods.ftbteams.api.event.TeamCollectPropertiesEvent;
import dev.ftb.mods.ftbteams.api.event.TeamEvent;
import dev.ftb.mods.ftbteams.api.property.TeamProperty;
import dev.ftb.mods.ftbteams.api.property.TeamPropertyCollection;
import dev.ftb.mods.ftbteams.api.property.TeamPropertyType;
import dev.ftb.mods.ftbteams.api.property.TeamPropertyValue;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import net.minecraft.ResourceLocationException;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class TeamPropertyCollectionImpl implements TeamPropertyCollection {

    private final TeamPropertyCollectionImpl.PropertyMap map = new TeamPropertyCollectionImpl.PropertyMap();

    public static TeamPropertyCollectionImpl fromNetwork(FriendlyByteBuf buf) {
        TeamPropertyCollectionImpl properties = new TeamPropertyCollectionImpl();
        properties.read(buf);
        return properties;
    }

    public void collectProperties() {
        this.map.clear();
        TeamEvent.COLLECT_PROPERTIES.invoker().accept(new TeamCollectPropertiesEvent(this.map::putDefaultProperty));
    }

    @Override
    public <T> void forEach(BiConsumer<TeamProperty<T>, TeamPropertyValue<T>> consumer) {
        this.map.forEachProperty(consumer);
    }

    public TeamPropertyCollectionImpl copy() {
        TeamPropertyCollectionImpl p = new TeamPropertyCollectionImpl();
        this.map.forEachProperty((key, value) -> p.map.putProperty(key, value.copy()));
        return p;
    }

    @Override
    public void updateFrom(TeamPropertyCollection otherProperties) {
        otherProperties.forEach((key, value) -> this.set(key, value.getValue()));
    }

    @Override
    public <T> T get(TeamProperty<T> key) {
        TeamPropertyValue<T> v = this.map.getProperty(key);
        return v == null ? key.getDefaultValue() : v.getValue();
    }

    @Override
    public <T> void set(TeamProperty<T> key, T value) {
        if (this.map.hasProperty(key)) {
            this.map.getProperty(key).setValue(value);
        } else {
            this.map.putProperty(key, new TeamPropertyValue<>(key, value));
        }
    }

    @Override
    public void read(FriendlyByteBuf buffer) {
        int nProperties = buffer.readVarInt();
        this.map.clear();
        for (int i = 0; i < nProperties; i++) {
            TeamProperty<?> tp = TeamPropertyType.read(buffer);
            this.map.putNetworkProperty(tp, buffer);
        }
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeVarInt(this.map.size());
        this.map.forEachProperty((prop, value) -> {
            TeamPropertyType.write(buffer, prop);
            prop.writeValue(buffer, value.getValue());
        });
    }

    public void writeSyncableOnly(FriendlyByteBuf buffer, List<TeamProperty<?>> syncableProps) {
        TeamPropertyCollectionImpl.PropertyMap subMap = new TeamPropertyCollectionImpl.PropertyMap();
        syncableProps.forEach(prop -> {
            if (this.map.hasProperty(prop)) {
                subMap.backingMap.put(prop, this.map.backingMap.get(prop));
            }
        });
        buffer.writeVarInt(subMap.size());
        subMap.forEachProperty((key, value) -> {
            TeamPropertyType.write(buffer, key);
            key.writeValue(buffer, value.getValue());
        });
    }

    public void read(CompoundTag tag) {
        tag.getAllKeys().forEach(key -> this.map.findProperty(key).ifPresent(prop -> this.map.putNBTProperty(prop, tag.get(key))));
    }

    public CompoundTag write(CompoundTag tag) {
        this.map.forEachProperty((key, value) -> tag.put(key.getId().toString(), key.toNBT(value.getValue())));
        return tag;
    }

    private static class PropertyMap {

        Map<Object, Object> backingMap = new LinkedHashMap();

        Map<ResourceLocation, TeamProperty<?>> byId = new HashMap();

        void clear() {
            this.backingMap.clear();
            this.byId.clear();
        }

        boolean hasProperty(TeamProperty<?> prop) {
            return this.backingMap.containsKey(prop);
        }

        int size() {
            return this.backingMap.size();
        }

        <T> void putProperty(TeamProperty<T> prop, TeamPropertyValue<T> value) {
            this.backingMap.put(prop, value);
            this.byId.put(prop.getId(), prop);
        }

        void putDefaultProperty(TeamProperty<?> prop) {
            this.backingMap.put(prop, prop.createDefaultValue());
            this.byId.put(prop.getId(), prop);
        }

        void putNetworkProperty(TeamProperty<?> prop, FriendlyByteBuf buffer) {
            this.backingMap.put(prop, prop.createValueFromNetwork(buffer));
            this.byId.put(prop.getId(), prop);
        }

        void putNBTProperty(TeamProperty<?> prop, Tag tag) {
            this.backingMap.put(prop, prop.createValueFromNBT(tag));
            this.byId.put(prop.getId(), prop);
        }

        <T> TeamPropertyValue<T> getProperty(TeamProperty<T> property) {
            return (TeamPropertyValue<T>) this.backingMap.get(property);
        }

        <T> void forEachProperty(BiConsumer<TeamProperty<T>, TeamPropertyValue<T>> consumer) {
            this.backingMap.forEach((k, v) -> consumer.accept((TeamProperty) k, (TeamPropertyValue) v));
        }

        Optional<TeamProperty<?>> findProperty(String key) {
            try {
                return Optional.ofNullable((TeamProperty) this.byId.get(new ResourceLocation(key)));
            } catch (ResourceLocationException var3) {
                return Optional.empty();
            }
        }
    }
}