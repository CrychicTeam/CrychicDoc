package net.minecraft.server.bossevents;

import com.google.common.collect.Maps;
import java.util.Collection;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class CustomBossEvents {

    private final Map<ResourceLocation, CustomBossEvent> events = Maps.newHashMap();

    @Nullable
    public CustomBossEvent get(ResourceLocation resourceLocation0) {
        return (CustomBossEvent) this.events.get(resourceLocation0);
    }

    public CustomBossEvent create(ResourceLocation resourceLocation0, Component component1) {
        CustomBossEvent $$2 = new CustomBossEvent(resourceLocation0, component1);
        this.events.put(resourceLocation0, $$2);
        return $$2;
    }

    public void remove(CustomBossEvent customBossEvent0) {
        this.events.remove(customBossEvent0.getTextId());
    }

    public Collection<ResourceLocation> getIds() {
        return this.events.keySet();
    }

    public Collection<CustomBossEvent> getEvents() {
        return this.events.values();
    }

    public CompoundTag save() {
        CompoundTag $$0 = new CompoundTag();
        for (CustomBossEvent $$1 : this.events.values()) {
            $$0.put($$1.getTextId().toString(), $$1.save());
        }
        return $$0;
    }

    public void load(CompoundTag compoundTag0) {
        for (String $$1 : compoundTag0.getAllKeys()) {
            ResourceLocation $$2 = new ResourceLocation($$1);
            this.events.put($$2, CustomBossEvent.load(compoundTag0.getCompound($$1), $$2));
        }
    }

    public void onPlayerConnect(ServerPlayer serverPlayer0) {
        for (CustomBossEvent $$1 : this.events.values()) {
            $$1.onPlayerConnect(serverPlayer0);
        }
    }

    public void onPlayerDisconnect(ServerPlayer serverPlayer0) {
        for (CustomBossEvent $$1 : this.events.values()) {
            $$1.onPlayerDisconnect(serverPlayer0);
        }
    }
}