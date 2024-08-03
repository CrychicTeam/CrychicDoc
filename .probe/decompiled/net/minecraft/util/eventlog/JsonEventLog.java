package net.minecraft.util.eventlog;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import java.io.Closeable;
import java.io.IOException;
import java.io.Writer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nullable;
import net.minecraft.Util;

public class JsonEventLog<T> implements Closeable {

    private static final Gson GSON = new Gson();

    private final Codec<T> codec;

    final FileChannel channel;

    private final AtomicInteger referenceCount = new AtomicInteger(1);

    public JsonEventLog(Codec<T> codecT0, FileChannel fileChannel1) {
        this.codec = codecT0;
        this.channel = fileChannel1;
    }

    public static <T> JsonEventLog<T> open(Codec<T> codecT0, Path path1) throws IOException {
        FileChannel $$2 = FileChannel.open(path1, StandardOpenOption.WRITE, StandardOpenOption.READ, StandardOpenOption.CREATE);
        return new JsonEventLog<>(codecT0, $$2);
    }

    public void write(T t0) throws IOException, JsonIOException {
        JsonElement $$1 = Util.getOrThrow(this.codec.encodeStart(JsonOps.INSTANCE, t0), IOException::new);
        this.channel.position(this.channel.size());
        Writer $$2 = Channels.newWriter(this.channel, StandardCharsets.UTF_8);
        GSON.toJson($$1, $$2);
        $$2.write(10);
        $$2.flush();
    }

    public JsonEventLogReader<T> openReader() throws IOException {
        if (this.referenceCount.get() <= 0) {
            throw new IOException("Event log has already been closed");
        } else {
            this.referenceCount.incrementAndGet();
            final JsonEventLogReader<T> $$0 = JsonEventLogReader.create(this.codec, Channels.newReader(this.channel, StandardCharsets.UTF_8));
            return new JsonEventLogReader<T>() {

                private volatile long position;

                @Nullable
                @Override
                public T next() throws IOException {
                    Object var1;
                    try {
                        JsonEventLog.this.channel.position(this.position);
                        var1 = $$0.next();
                    } finally {
                        this.position = JsonEventLog.this.channel.position();
                    }
                    return (T) var1;
                }

                public void close() throws IOException {
                    JsonEventLog.this.releaseReference();
                }
            };
        }
    }

    public void close() throws IOException {
        this.releaseReference();
    }

    void releaseReference() throws IOException {
        if (this.referenceCount.decrementAndGet() <= 0) {
            this.channel.close();
        }
    }
}