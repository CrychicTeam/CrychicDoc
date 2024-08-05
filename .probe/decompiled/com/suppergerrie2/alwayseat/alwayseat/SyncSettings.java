package com.suppergerrie2.alwayseat.alwayseat;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class SyncSettings {

    Config.Mode mode;

    List<String> itemList;

    List<String> uneatableList;

    public SyncSettings(Config.Mode mode, List<String> itemList, List<String> uneatableList) {
        this.mode = mode;
        this.itemList = itemList;
        this.uneatableList = uneatableList;
    }

    public static void encode(SyncSettings msg, FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeEnum(msg.mode);
        friendlyByteBuf.writeCollection(msg.itemList, FriendlyByteBuf::m_130070_);
        friendlyByteBuf.writeCollection(msg.uneatableList, FriendlyByteBuf::m_130070_);
    }

    public static SyncSettings decode(FriendlyByteBuf friendlyByteBuf) {
        Config.Mode mode = friendlyByteBuf.readEnum(Config.Mode.class);
        List<String> itemList = friendlyByteBuf.readCollection(ArrayList::new, FriendlyByteBuf::m_130277_);
        List<String> uneatableList = friendlyByteBuf.readCollection(ArrayList::new, FriendlyByteBuf::m_130277_);
        return new SyncSettings(mode, itemList, uneatableList);
    }

    public static void handle(SyncSettings msg, Supplier<NetworkEvent.Context> contextSupplier) {
        ((NetworkEvent.Context) contextSupplier.get()).enqueueWork(() -> {
            Config.MODE.set(msg.mode);
            Config.ITEM_LIST.set(msg.itemList);
            Config.UNEATABLE_ITEMS.set(msg.uneatableList);
        });
        ((NetworkEvent.Context) contextSupplier.get()).setPacketHandled(true);
    }

    public static SyncSettings fromConfig() {
        return new SyncSettings((Config.Mode) Config.MODE.get(), asStrings(Config.ITEM_LIST.get()), asStrings(Config.UNEATABLE_ITEMS.get()));
    }

    private static List<String> asStrings(List<?> values) {
        return (List<String>) values.stream().map(Object::toString).collect(Collectors.toList());
    }
}