package dev.xkmc.l2library.init.events;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import java.util.Map;
import java.util.function.Consumer;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

@ParametersAreNonnullByDefault
public class BaseJsonReloadListener extends SimpleJsonResourceReloadListener {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    private final Consumer<Map<ResourceLocation, JsonElement>> consumer;

    public BaseJsonReloadListener(String path, Consumer<Map<ResourceLocation, JsonElement>> consumer) {
        super(GSON, path);
        this.consumer = consumer;
    }

    protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager manager, ProfilerFiller profiler) {
        this.consumer.accept(map);
    }
}