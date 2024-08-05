package net.blay09.mods.balm.api.network;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import net.blay09.mods.balm.api.Balm;
import net.blay09.mods.balm.api.config.BalmConfigData;
import net.blay09.mods.balm.api.config.Synced;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class SyncConfigMessage<TData> {

    private final TData data;

    public SyncConfigMessage(TData data) {
        this.data = data;
    }

    public TData getData() {
        return this.data;
    }

    public static <TData> Supplier<TData> createDeepCopyFactory(Supplier<TData> from, Supplier<TData> factory) {
        return () -> {
            TData to = (TData) factory.get();
            ConfigReflection.deepCopy(from.get(), to);
            return to;
        };
    }

    public static <TData, TMessage extends SyncConfigMessage<TData>> Function<FriendlyByteBuf, TMessage> createDecoder(Class<?> clazz, Function<TData, TMessage> messageFactory, Supplier<TData> dataFactory) {
        return buf -> {
            TData data = (TData) dataFactory.get();
            readSyncedFields(buf, data, false);
            return (SyncConfigMessage) messageFactory.apply(data);
        };
    }

    private static <TData> void readSyncedFields(FriendlyByteBuf buf, TData data, boolean forceSynced) {
        List<Field> syncedFields = !forceSynced ? ConfigReflection.getSyncedFields(data.getClass()) : Arrays.asList(data.getClass().getFields());
        syncedFields.sort(Comparator.comparing(Field::getName));
        for (Field field : syncedFields) {
            Class<?> type = field.getType();
            try {
                Object value;
                if (String.class.isAssignableFrom(type)) {
                    value = buf.readUtf();
                } else if (Enum.class.isAssignableFrom(type)) {
                    value = type.getEnumConstants()[buf.readByte()];
                } else if (int.class.isAssignableFrom(type)) {
                    value = buf.readInt();
                } else if (float.class.isAssignableFrom(type)) {
                    value = buf.readFloat();
                } else if (double.class.isAssignableFrom(type)) {
                    value = buf.readDouble();
                } else if (boolean.class.isAssignableFrom(type)) {
                    value = buf.readBoolean();
                } else if (long.class.isAssignableFrom(type)) {
                    value = buf.readLong();
                } else {
                    value = field.get(data);
                    readSyncedFields(buf, value, field.getAnnotation(Synced.class) != null);
                }
                field.set(data, value);
            } catch (IllegalAccessException var9) {
                throw new IllegalStateException(var9);
            }
        }
    }

    public static <TData, TMessage extends SyncConfigMessage<TData>> BiConsumer<TMessage, FriendlyByteBuf> createEncoder(Class<TData> clazz) {
        return (message, buf) -> {
            TData data = (TData) message.getData();
            writeSyncedFields(buf, data, false);
        };
    }

    private static <TData> void writeSyncedFields(FriendlyByteBuf buf, TData data, boolean forceSynced) {
        List<Field> syncedFields = !forceSynced ? ConfigReflection.getSyncedFields(data.getClass()) : Arrays.asList(data.getClass().getFields());
        syncedFields.sort(Comparator.comparing(Field::getName));
        for (Field field : syncedFields) {
            Class<?> type = field.getType();
            try {
                Object value = field.get(data);
                if (String.class.isAssignableFrom(type)) {
                    buf.writeUtf((String) value);
                } else if (Enum.class.isAssignableFrom(type)) {
                    buf.writeByte(((Enum) value).ordinal());
                } else if (int.class.isAssignableFrom(type)) {
                    buf.writeInt((Integer) value);
                } else if (float.class.isAssignableFrom(type)) {
                    buf.writeFloat((Float) value);
                } else if (double.class.isAssignableFrom(type)) {
                    buf.writeDouble((Double) value);
                } else if (boolean.class.isAssignableFrom(type)) {
                    buf.writeBoolean((Boolean) value);
                } else if (long.class.isAssignableFrom(type)) {
                    buf.writeLong((Long) value);
                } else {
                    writeSyncedFields(buf, field.get(data), field.getAnnotation(Synced.class) != null);
                }
            } catch (IllegalAccessException var9) {
                throw new IllegalStateException(var9);
            }
        }
    }

    public static <TMessage extends SyncConfigMessage<TData>, TData extends BalmConfigData> void register(ResourceLocation resourceLocation, Class<TMessage> messageClass, Function<TData, TMessage> messageFactory, Class<TData> dataClass, Supplier<TData> dataFactory) {
        Supplier<TData> copyFactory = createDeepCopyFactory(() -> Balm.getConfig().getBackingConfig(dataClass), dataFactory);
        Balm.getNetworking().registerClientboundPacket(resourceLocation, messageClass, (message, buf) -> {
            TData data = (TData) message.getData();
            writeSyncedFields(buf, data, false);
        }, buf -> {
            TData data = (TData) copyFactory.get();
            readSyncedFields(buf, data, false);
            return (SyncConfigMessage) messageFactory.apply(data);
        }, Balm.getConfig()::handleSync);
    }
}