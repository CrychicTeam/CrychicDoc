package fuzs.puzzleslib.impl.network.serialization;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import fuzs.puzzleslib.api.network.v3.serialization.MessageSerializer;
import fuzs.puzzleslib.api.network.v3.serialization.MessageSerializers;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Constructor;
import java.lang.reflect.RecordComponent;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import net.minecraft.network.FriendlyByteBuf;

public final class RecordSerializer<T extends Record> implements MessageSerializer<T> {

    private final Class<T> recordType;

    private final List<RecordSerializer.RecordAccess<?, T>> recordAccess;

    private final Function<Object[], T> instanceFactory;

    private RecordSerializer(Class<T> recordType, List<RecordSerializer.RecordAccess<?, T>> recordAccess, Function<Object[], T> instanceFactory) {
        this.recordType = recordType;
        this.instanceFactory = instanceFactory;
        this.recordAccess = recordAccess;
    }

    public static <T extends Record> MessageSerializer<T> createRecordSerializer(Class<T> clazz) {
        if (!clazz.isRecord()) {
            throw new IllegalArgumentException("Message of type %s is not a record".formatted(clazz));
        } else {
            Builder<RecordSerializer.RecordAccess<?, T>> builder = ImmutableList.builder();
            for (RecordComponent component : clazz.getRecordComponents()) {
                builder.add(RecordSerializer.RecordAccess.fromRecordComponent(component));
            }
            List<RecordSerializer.RecordAccess<?, T>> recordAccess = builder.build();
            Class<?>[] constructorArguments = (Class<?>[]) recordAccess.stream().map(RecordSerializer.RecordAccess::type).toArray(Class[]::new);
            try {
                Constructor<T> constructor = clazz.getConstructor(constructorArguments);
                return new RecordSerializer<>(clazz, recordAccess, args -> {
                    try {
                        return (Record) constructor.newInstance(args);
                    } catch (ReflectiveOperationException var4) {
                        throw new RuntimeException("Unable to create new record instance of type %s".formatted(clazz), var4);
                    }
                });
            } catch (NoSuchMethodException var6) {
                throw new RuntimeException("Unable to find constructor with arguments %s for record type %s".formatted(Arrays.toString(constructorArguments), clazz), var6);
            }
        }
    }

    public Class<T> getRecordType() {
        return this.recordType;
    }

    public void write(FriendlyByteBuf buf, T instance) {
        for (RecordSerializer.RecordAccess<?, T> access : this.recordAccess) {
            access.write(buf, instance);
        }
    }

    public T read(FriendlyByteBuf buf) {
        Object[] values = this.recordAccess.stream().map(recordAccess -> recordAccess.read(buf)).toArray();
        return (T) this.instanceFactory.apply(values);
    }

    private static record RecordAccess<T, R extends Record>(Class<? extends T> type, Function<R, T> fieldAccess, MessageSerializer<T> serializer) {

        static <T, R extends Record> RecordSerializer.RecordAccess<T, R> fromRecordComponent(RecordComponent component) {
            Lookup lookup = MethodHandles.publicLookup();
            Class<T> type = component.getType();
            Function<R, T> fieldAccess = instance -> {
                try {
                    return (Object) lookup.unreflect(component.getAccessor()).invoke(instance);
                } catch (Throwable var5) {
                    throw new RuntimeException("Unable to get record value of type %s from record component from record type %s".formatted(type, component.getDeclaringRecord()), var5);
                }
            };
            MessageSerializer<T> serializer = (MessageSerializer<T>) MessageSerializers.findByGenericType(component.getGenericType());
            return new RecordSerializer.RecordAccess<>(type, fieldAccess, serializer);
        }

        public void write(FriendlyByteBuf buf, R instance) {
            this.serializer.write(buf, (T) this.fieldAccess.apply(instance));
        }

        public T read(FriendlyByteBuf buf) {
            return this.serializer.read(buf);
        }
    }
}