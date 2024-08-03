package net.minecraft.server.packs;

import java.util.Map;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;

public class BuiltInMetadata {

    private static final BuiltInMetadata EMPTY = new BuiltInMetadata(Map.of());

    private final Map<MetadataSectionSerializer<?>, ?> values;

    private BuiltInMetadata(Map<MetadataSectionSerializer<?>, ?> mapMetadataSectionSerializer0) {
        this.values = mapMetadataSectionSerializer0;
    }

    public <T> T get(MetadataSectionSerializer<T> metadataSectionSerializerT0) {
        return (T) this.values.get(metadataSectionSerializerT0);
    }

    public static BuiltInMetadata of() {
        return EMPTY;
    }

    public static <T> BuiltInMetadata of(MetadataSectionSerializer<T> metadataSectionSerializerT0, T t1) {
        return new BuiltInMetadata(Map.of(metadataSectionSerializerT0, t1));
    }

    public static <T1, T2> BuiltInMetadata of(MetadataSectionSerializer<T1> metadataSectionSerializerT0, T1 t1, MetadataSectionSerializer<T2> metadataSectionSerializerT2, T2 t3) {
        return new BuiltInMetadata(Map.of(metadataSectionSerializerT0, t1, metadataSectionSerializerT2, t3));
    }
}