package net.minecraft.util.eventlog;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import javax.annotation.Nullable;
import net.minecraft.Util;

public interface JsonEventLogReader<T> extends Closeable {

    static <T> JsonEventLogReader<T> create(final Codec<T> codecT0, Reader reader1) {
        final JsonReader $$2 = new JsonReader(reader1);
        $$2.setLenient(true);
        return new JsonEventLogReader<T>() {

            @Nullable
            @Override
            public T next() throws IOException {
                try {
                    if (!$$2.hasNext()) {
                        return null;
                    } else {
                        JsonElement $$0 = JsonParser.parseReader($$2);
                        return Util.getOrThrow(codecT0.parse(JsonOps.INSTANCE, $$0), IOException::new);
                    }
                } catch (JsonParseException var2) {
                    throw new IOException(var2);
                } catch (EOFException var3) {
                    return null;
                }
            }

            public void close() throws IOException {
                $$2.close();
            }
        };
    }

    @Nullable
    T next() throws IOException;
}