package snownee.kiwi.contributor.impl;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.ImmutableSet.Builder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import snownee.kiwi.Kiwi;
import snownee.kiwi.contributor.ITierProvider;
import snownee.kiwi.contributor.client.CosmeticLayer;

public class JsonTierProvider implements ITierProvider {

    public static final Gson GSON = new GsonBuilder().setLenient().create();

    public static final Codec<Map<String, List<String>>> CODEC = Codec.unboundedMap(Codec.STRING, Codec.STRING.listOf());

    private final String author;

    private ImmutableSetMultimap<String, String> contributors = ImmutableSetMultimap.of();

    protected ImmutableSet<String> superusers = ImmutableSet.of();

    private final Supplier<List<String>> urlProvider;

    public JsonTierProvider(String author, Supplier<List<String>> urlProvider) {
        this.author = author;
        this.urlProvider = urlProvider;
        this.refresh();
    }

    public boolean load(String url) {
        try {
            InputStreamReader reader = new InputStreamReader(URI.create(url).toURL().openStream());
            boolean var7;
            try {
                JsonElement json = (JsonElement) GSON.fromJson(reader, JsonElement.class);
                Map<String, List<String>> map = (Map<String, List<String>>) CODEC.parse(JsonOps.INSTANCE, json).result().orElseThrow();
                Builder<String> superusers = ImmutableSet.builder();
                if (map.containsKey("*")) {
                    superusers.addAll((Iterable) map.get("*"));
                    superusers.add("Dev");
                } else {
                    superusers.add(this.getAuthor());
                }
                com.google.common.collect.ImmutableSetMultimap.Builder<String, String> contributors = ImmutableSetMultimap.builder();
                map.forEach((reward, users) -> {
                    if (!"*".equals(reward)) {
                        users.forEach(user -> contributors.put(user, reward));
                    }
                });
                this.contributors = contributors.build();
                this.superusers = superusers.build();
                Kiwi.LOGGER.debug("Successfully loaded {} contributors", this.contributors.keySet().size());
                var7 = true;
            } catch (Throwable var9) {
                try {
                    reader.close();
                } catch (Throwable var8) {
                    var9.addSuppressed(var8);
                }
                throw var9;
            }
            reader.close();
            return var7;
        } catch (Exception var10) {
            Kiwi.LOGGER.debug("Failed to load contributors from %s".formatted(url), var10);
            return var10 instanceof UnknownHostException;
        }
    }

    @Override
    public CompletableFuture<Void> refresh() {
        return CompletableFuture.runAsync(() -> {
            int tried = 0;
            List<String> url = (List<String>) this.urlProvider.get();
            while (tried < url.size() && !this.load((String) url.get(tried))) {
                tried++;
            }
        });
    }

    @Override
    public String getAuthor() {
        return this.author;
    }

    @Override
    public Set<String> getPlayerTiers(String playerName) {
        return (Set<String>) (this.superusers.contains(playerName) ? this.getTiers() : this.contributors.get(playerName));
    }

    @Override
    public Set<String> getTiers() {
        return this.contributors.keySet();
    }

    @Override
    public List<String> getRenderableTiers() {
        return List.of();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public CosmeticLayer createRenderer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> entityRenderer, String tier) {
        return null;
    }
}