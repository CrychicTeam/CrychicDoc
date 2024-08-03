package dev.ftb.mods.ftbquests.util;

import dev.architectury.networking.NetworkManager;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftbquests.quest.ServerQuestFile;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;

public class NetUtils {

    public static boolean canEdit(NetworkManager.PacketContext context) {
        Player player = context.getPlayer();
        return player != null && ServerQuestFile.INSTANCE.getOrCreateTeamData(player).getCanEdit(player);
    }

    public static <T> void write(FriendlyByteBuf buffer, Collection<T> list, BiConsumer<FriendlyByteBuf, T> writer) {
        buffer.writeVarInt(list.size());
        for (T value : list) {
            writer.accept(buffer, value);
        }
    }

    public static <K, V> void write(FriendlyByteBuf buffer, Map<K, V> map, BiConsumer<FriendlyByteBuf, K> keyWriter, BiConsumer<FriendlyByteBuf, V> valueWriter) {
        buffer.writeVarInt(map.size());
        for (Entry<K, V> entry : map.entrySet()) {
            keyWriter.accept(buffer, entry.getKey());
            valueWriter.accept(buffer, entry.getValue());
        }
    }

    public static void writeStrings(FriendlyByteBuf buffer, Collection<String> list) {
        write(buffer, list, (b, s) -> b.writeUtf(s, 32767));
    }

    public static <T> void read(FriendlyByteBuf buffer, Collection<T> list, Function<FriendlyByteBuf, T> reader) {
        list.clear();
        int s = buffer.readVarInt();
        for (int i = 0; i < s; i++) {
            list.add(reader.apply(buffer));
        }
    }

    public static <K, V> void read(FriendlyByteBuf buffer, Map<K, V> map, Function<FriendlyByteBuf, K> keyReader, BiFunction<K, FriendlyByteBuf, V> valueReader) {
        map.clear();
        int s = buffer.readVarInt();
        for (int i = 0; i < s; i++) {
            K key = (K) keyReader.apply(buffer);
            map.put(key, valueReader.apply(key, buffer));
        }
    }

    public static void readStrings(FriendlyByteBuf buffer, Collection<String> list) {
        read(buffer, list, b -> b.readUtf(32767));
    }

    public static void writeIcon(FriendlyByteBuf buffer, Icon icon) {
        buffer.writeUtf(icon.toString(), 32767);
    }

    public static Icon readIcon(FriendlyByteBuf buffer) {
        return Icon.getIcon(buffer.readUtf(32767));
    }
}