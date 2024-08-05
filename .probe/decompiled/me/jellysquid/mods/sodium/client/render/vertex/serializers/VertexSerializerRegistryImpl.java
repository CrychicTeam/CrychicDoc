package me.jellysquid.mods.sodium.client.render.vertex.serializers;

import it.unimi.dsi.fastutil.longs.Long2ReferenceMap;
import it.unimi.dsi.fastutil.longs.Long2ReferenceOpenHashMap;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.concurrent.locks.StampedLock;
import me.jellysquid.mods.sodium.client.render.vertex.serializers.generated.VertexSerializerFactory;
import net.caffeinemc.mods.sodium.api.vertex.format.VertexFormatDescription;
import net.caffeinemc.mods.sodium.api.vertex.serializer.VertexSerializer;
import net.caffeinemc.mods.sodium.api.vertex.serializer.VertexSerializerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VertexSerializerRegistryImpl implements VertexSerializerRegistry {

    private static final Logger LOGGER = LoggerFactory.getLogger(VertexSerializerRegistryImpl.class);

    private static final Path CLASS_DUMP_PATH;

    private final Long2ReferenceMap<VertexSerializer> cache = new Long2ReferenceOpenHashMap();

    private final StampedLock lock = new StampedLock();

    @Override
    public VertexSerializer get(VertexFormatDescription srcFormat, VertexFormatDescription dstFormat) {
        long identifier = createKey(srcFormat, dstFormat);
        VertexSerializer serializer = this.find(identifier);
        if (serializer == null) {
            serializer = this.create(identifier, srcFormat, dstFormat);
        }
        return serializer;
    }

    private VertexSerializer create(long identifier, VertexFormatDescription srcFormat, VertexFormatDescription dstFormat) {
        long stamp = this.lock.writeLock();
        VertexSerializer serializer;
        try {
            VertexSerializer cached = (VertexSerializer) this.cache.get(identifier);
            if (cached == null) {
                serializer = createSerializer(srcFormat, dstFormat);
                this.cache.put(identifier, serializer);
                return serializer;
            }
            serializer = cached;
        } finally {
            this.lock.unlockWrite(stamp);
        }
        return serializer;
    }

    private VertexSerializer find(long identifier) {
        long stamp = this.lock.readLock();
        VertexSerializer var5;
        try {
            var5 = (VertexSerializer) this.cache.get(identifier);
        } finally {
            this.lock.unlockRead(stamp);
        }
        return var5;
    }

    private static VertexSerializer createSerializer(VertexFormatDescription srcVertexFormat, VertexFormatDescription dstVertexFormat) {
        String identifier = String.format("%04X$%04X", srcVertexFormat.id(), dstVertexFormat.id());
        VertexSerializerFactory.Bytecode bytecode = VertexSerializerFactory.generate(srcVertexFormat, dstVertexFormat, identifier);
        if (CLASS_DUMP_PATH != null) {
            dumpClass(identifier, bytecode);
        }
        Class<?> clazz = VertexSerializerFactory.define(bytecode);
        Constructor<?> constructor;
        try {
            constructor = clazz.getConstructor();
        } catch (NoSuchMethodException var11) {
            throw new RuntimeException("Failed to find constructor of generated class", var11);
        }
        Object instance;
        try {
            instance = constructor.newInstance();
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException var10) {
            throw new RuntimeException("Failed to instantiate generated class", var10);
        }
        try {
            return (VertexSerializer) instance;
        } catch (ClassCastException var9) {
            throw new RuntimeException("Failed to cast generated class to interface type", var9);
        }
    }

    private static void dumpClass(String id, VertexSerializerFactory.Bytecode bytecode) {
        Path path = CLASS_DUMP_PATH.resolve("VertexSerializer$Impl$%s.class".formatted(id));
        try {
            Files.write(path, bytecode.copy(), new OpenOption[0]);
        } catch (IOException var4) {
            LOGGER.warn("Could not dump bytecode to location: {}", path, var4);
        }
    }

    private static long createKey(VertexFormatDescription a, VertexFormatDescription b) {
        return (long) a.id() & 4294967295L | ((long) b.id() & 4294967295L) << 32;
    }

    static {
        String classDumpPath = System.getProperty("sodium.codegen.dump", null);
        if (classDumpPath != null) {
            CLASS_DUMP_PATH = Path.of(classDumpPath);
        } else {
            CLASS_DUMP_PATH = null;
        }
    }
}