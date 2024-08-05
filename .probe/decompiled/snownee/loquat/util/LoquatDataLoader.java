package snownee.loquat.util;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.lang.reflect.Type;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import snownee.kiwi.loader.Platform;
import snownee.kiwi.util.Util;
import snownee.loquat.spawner.Difficulty;
import snownee.lychee.core.contextual.ContextualHolder;
import snownee.lychee.core.post.PostAction;
import snownee.lychee.fragment.Fragments;

public class LoquatDataLoader<T> extends SimpleJsonResourceReloadListener {

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().setLenient().registerTypeAdapter(ResourceLocation.class, new ResourceLocation.Serializer()).registerTypeAdapter(PostAction.class, new LoquatDataLoader.PostActionSerializer()).registerTypeAdapter(ContextualHolder.class, new LoquatDataLoader.ContextualHolderSerializer()).registerTypeAdapter(Difficulty.Provider.class, new Difficulty.DifficultyProviderSerializer()).create();

    private final Map<ResourceLocation, T> objects = Maps.newHashMap();

    public final SuggestionProvider<CommandSourceStack> suggestionProvider;

    private final Function<JsonElement, T> parser;

    public boolean supportsFragment;

    public LoquatDataLoader(ResourceLocation id, String dir, Function<JsonElement, T> parser) {
        super(GSON, dir);
        this.suggestionProvider = FallbackSuggestionProvider.register(id, this::suggest);
        this.parser = parser;
        if (Platform.isPhysicalClient()) {
            ClientProxy.registerDisconnectListener(this::clear);
        }
    }

    protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        this.clear();
        map.forEach((id, json) -> {
            if (this.supportsFragment) {
                Fragments.INSTANCE.process(json);
            }
            T object = (T) this.parser.apply(json);
            if (object != null) {
                this.objects.put(id, object);
            }
        });
    }

    public void clear() {
        this.objects.clear();
    }

    public T get(ResourceLocation id) {
        return (T) this.objects.get(id);
    }

    private CompletableFuture<Suggestions> suggest(CommandContext<SharedSuggestionProvider> context, SuggestionsBuilder builder) {
        String s = builder.getRemaining().toLowerCase(Locale.ROOT);
        SharedSuggestionProvider.filterResources(this.objects.keySet(), s, Function.identity(), id -> builder.suggest(Util.trimRL(id)));
        return builder.buildFuture();
    }

    private static class ContextualHolderSerializer implements JsonDeserializer<ContextualHolder> {

        public ContextualHolder deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            ContextualHolder holder = new ContextualHolder();
            holder.parseConditions(jsonElement);
            return holder;
        }
    }

    private static class PostActionSerializer implements JsonDeserializer<PostAction> {

        public PostAction deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            return PostAction.parse(jsonElement.getAsJsonObject());
        }
    }
}