package dev.xkmc.l2library.serial.config;

import com.google.gson.JsonElement;
import dev.xkmc.l2serial.serialization.codec.JsonCodec;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;

public abstract class RecordDataProvider implements DataProvider {

    private final DataGenerator generator;

    private final String name;

    private final Map<String, Record> map = new HashMap();

    public RecordDataProvider(DataGenerator generator, String name) {
        this.generator = generator;
        this.name = name;
    }

    public abstract void add(BiConsumer<String, Record> var1);

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        Path folder = this.generator.getPackOutput().getOutputFolder();
        this.add(this.map::put);
        List<CompletableFuture<?>> list = new ArrayList();
        this.map.forEach((k, v) -> {
            JsonElement elem = JsonCodec.toJson(v);
            if (elem != null) {
                Path path = folder.resolve("data/" + k + ".json");
                list.add(DataProvider.saveStable(cache, elem, path));
            }
        });
        return CompletableFuture.allOf((CompletableFuture[]) list.toArray(CompletableFuture[]::new));
    }

    @Override
    public String getName() {
        return this.name;
    }
}