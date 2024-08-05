package com.simibubi.create.content.equipment.potatoCannon;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllPackets;
import com.simibubi.create.foundation.networking.SimplePacketBase;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

public class PotatoProjectileTypeManager {

    private static final Map<ResourceLocation, PotatoCannonProjectileType> BUILTIN_TYPE_MAP = new HashMap();

    private static final Map<ResourceLocation, PotatoCannonProjectileType> CUSTOM_TYPE_MAP = new HashMap();

    private static final Map<Item, PotatoCannonProjectileType> ITEM_TO_TYPE_MAP = new IdentityHashMap();

    public static void registerBuiltinType(ResourceLocation id, PotatoCannonProjectileType type) {
        synchronized (BUILTIN_TYPE_MAP) {
            BUILTIN_TYPE_MAP.put(id, type);
        }
    }

    public static PotatoCannonProjectileType getBuiltinType(ResourceLocation id) {
        return (PotatoCannonProjectileType) BUILTIN_TYPE_MAP.get(id);
    }

    public static PotatoCannonProjectileType getCustomType(ResourceLocation id) {
        return (PotatoCannonProjectileType) CUSTOM_TYPE_MAP.get(id);
    }

    public static PotatoCannonProjectileType getTypeForItem(Item item) {
        return (PotatoCannonProjectileType) ITEM_TO_TYPE_MAP.get(item);
    }

    public static Optional<PotatoCannonProjectileType> getTypeForStack(ItemStack item) {
        return item.isEmpty() ? Optional.empty() : Optional.ofNullable(getTypeForItem(item.getItem()));
    }

    public static void clear() {
        CUSTOM_TYPE_MAP.clear();
        ITEM_TO_TYPE_MAP.clear();
    }

    public static void fillItemMap() {
        for (Entry<ResourceLocation, PotatoCannonProjectileType> entry : BUILTIN_TYPE_MAP.entrySet()) {
            PotatoCannonProjectileType type = (PotatoCannonProjectileType) entry.getValue();
            for (Supplier<Item> delegate : type.getItems()) {
                ITEM_TO_TYPE_MAP.put((Item) delegate.get(), type);
            }
        }
        for (Entry<ResourceLocation, PotatoCannonProjectileType> entry : CUSTOM_TYPE_MAP.entrySet()) {
            PotatoCannonProjectileType type = (PotatoCannonProjectileType) entry.getValue();
            for (Supplier<Item> delegate : type.getItems()) {
                ITEM_TO_TYPE_MAP.put((Item) delegate.get(), type);
            }
        }
        ITEM_TO_TYPE_MAP.remove(AllItems.POTATO_CANNON.get());
    }

    public static void toBuffer(FriendlyByteBuf buffer) {
        buffer.writeVarInt(CUSTOM_TYPE_MAP.size());
        for (Entry<ResourceLocation, PotatoCannonProjectileType> entry : CUSTOM_TYPE_MAP.entrySet()) {
            buffer.writeResourceLocation((ResourceLocation) entry.getKey());
            PotatoCannonProjectileType.toBuffer((PotatoCannonProjectileType) entry.getValue(), buffer);
        }
    }

    public static void fromBuffer(FriendlyByteBuf buffer) {
        clear();
        int size = buffer.readVarInt();
        for (int i = 0; i < size; i++) {
            CUSTOM_TYPE_MAP.put(buffer.readResourceLocation(), PotatoCannonProjectileType.fromBuffer(buffer));
        }
        fillItemMap();
    }

    public static void syncTo(ServerPlayer player) {
        AllPackets.getChannel().send(PacketDistributor.PLAYER.with(() -> player), new PotatoProjectileTypeManager.SyncPacket());
    }

    public static void syncToAll() {
        AllPackets.getChannel().send(PacketDistributor.ALL.noArg(), new PotatoProjectileTypeManager.SyncPacket());
    }

    public static class ReloadListener extends SimpleJsonResourceReloadListener {

        private static final Gson GSON = new Gson();

        public static final PotatoProjectileTypeManager.ReloadListener INSTANCE = new PotatoProjectileTypeManager.ReloadListener();

        protected ReloadListener() {
            super(GSON, "potato_cannon_projectile_types");
        }

        protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager resourceManager, ProfilerFiller profiler) {
            PotatoProjectileTypeManager.clear();
            for (Entry<ResourceLocation, JsonElement> entry : map.entrySet()) {
                JsonElement element = (JsonElement) entry.getValue();
                if (element.isJsonObject()) {
                    ResourceLocation id = (ResourceLocation) entry.getKey();
                    JsonObject object = element.getAsJsonObject();
                    PotatoCannonProjectileType type = PotatoCannonProjectileType.fromJson(object);
                    PotatoProjectileTypeManager.CUSTOM_TYPE_MAP.put(id, type);
                }
            }
            PotatoProjectileTypeManager.fillItemMap();
        }
    }

    public static class SyncPacket extends SimplePacketBase {

        private FriendlyByteBuf buffer;

        public SyncPacket() {
        }

        public SyncPacket(FriendlyByteBuf buffer) {
            this.buffer = buffer;
        }

        @Override
        public void write(FriendlyByteBuf buffer) {
            PotatoProjectileTypeManager.toBuffer(buffer);
        }

        @Override
        public boolean handle(NetworkEvent.Context context) {
            context.enqueueWork(() -> PotatoProjectileTypeManager.fromBuffer(this.buffer));
            return true;
        }
    }
}