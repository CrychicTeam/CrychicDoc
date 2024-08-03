package net.mehvahdjukaar.supplementaries.common.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import net.mehvahdjukaar.moonlight.api.misc.StrOpt;
import net.mehvahdjukaar.supplementaries.Supplementaries;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

public class Credits implements Serializable {

    private static final Codec<Credits> CODEC = RecordCodecBuilder.create(i -> i.group(Codec.unboundedMap(Codec.STRING, Credits.Supporter.CODEC).fieldOf("supporters").forGetter(p -> p.supporters), Codec.STRING.listOf().fieldOf("additional_artists").forGetter(p -> p.otherArtists), Codec.STRING.listOf().fieldOf("translators").forGetter(p -> p.translators), Codec.STRING.listOf().fieldOf("mod_compatibility").forGetter(p -> p.modCompatibility), Codec.STRING.listOf().fieldOf("music_and_sounds").forGetter(p -> p.soundArtists), Codec.STRING.listOf().fieldOf("others").forGetter(p -> p.others)).apply(i, Credits::new));

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().registerTypeAdapter(Credits.class, (JsonDeserializer) (json, typeOfT, context) -> (Credits) CODEC.parse(JsonOps.INSTANCE, json).getOrThrow(false, e -> {
    })).create();

    public static Credits INSTANCE = new Credits(Map.of(), List.of(), List.of(), List.of(), List.of(), List.of());

    private final transient List<UUID> devs = new ArrayList();

    private final transient Map<String, Pair<UUID, String>> statues = new HashMap();

    private final transient Map<String, ResourceLocation> globes = new HashMap();

    private final transient Map<String, Credits.Supporter> supporters;

    private final List<String> otherArtists;

    private final List<String> translators;

    private final List<String> modCompatibility;

    private final List<String> soundArtists;

    private final List<String> others;

    public Credits(Map<String, Credits.Supporter> supporters, List<String> artists, List<String> translators, List<String> mod_compat, List<String> sounds, List<String> others) {
        this.supporters = supporters;
        this.otherArtists = artists;
        this.translators = translators;
        this.modCompatibility = mod_compat;
        this.soundArtists = sounds;
        this.others = others;
        this.addSpecialPlayer("Dev", true, false, false, "380df991-f603-344c-a090-369bad2a924a");
        this.addSpecialPlayer("Dev", true, false, true, "5084e6f3-8f54-43f1-8df5-1dca109e430f");
        this.addSpecialPlayer("MehVahdJukaar", true, false, true, "898b3a39-e486-405c-a873-d6b472dc3ba2", "TheEvilGolem");
        this.addSpecialPlayer("Capobianco", true, false, true, "90ceb598-9983-4da3-9cae-436d5afb9d81", "Plantkillable");
        this.addSpecialPlayer("Plantkillable", true, true, true, "720f165c-b066-4113-9622-63fc63c65696", "Capobianco");
        this.addSpecialPlayer("Agrona", true, true, false, (UUID) null, "Pancake", "Pancakes");
        this.supporters.forEach((n, s) -> this.addSpecialPlayer(n, false, s.has_globe, s.has_statue, s.uuid));
    }

    private void addSpecialPlayer(String name, boolean isDev, boolean hasGlobe, boolean hasStatue, String id, String... alias) {
        UUID onlineId = id == null ? null : UUID.fromString(id);
        this.addSpecialPlayer(name, isDev, hasGlobe, hasStatue, onlineId);
    }

    private void addSpecialPlayer(String name, boolean isDev, boolean hasGlobe, boolean hasStatue, @Nullable UUID onlineId, String... alias) {
        name = name.toLowerCase(Locale.ROOT);
        if (isDev) {
            if (onlineId != null) {
                this.devs.add(onlineId);
            }
            this.devs.add(UUIDUtil.createOfflinePlayerUUID(name));
        }
        if (hasGlobe) {
            ResourceLocation texture = new ResourceLocation("supplementaries", "textures/entity/globes/globe_" + name + ".png");
            this.globes.put(name, texture);
            for (String n : alias) {
                this.globes.put(n.toLowerCase(Locale.ROOT), texture);
            }
        }
        Pair<UUID, String> p = Pair.of(onlineId, name);
        if (hasStatue) {
            this.statues.put(name, p);
            for (String n : alias) {
                this.statues.put(n.toLowerCase(Locale.ROOT), p);
            }
        }
    }

    public Map<String, ResourceLocation> globes() {
        return this.globes;
    }

    public Map<String, Pair<UUID, String>> statues() {
        return this.statues;
    }

    public List<UUID> getDevs() {
        return this.devs;
    }

    public Map<String, Credits.Supporter> supporters() {
        return this.supporters;
    }

    public String createCreditsText() {
        StringBuilder builder = new StringBuilder();
        builder.append("§6\n§lSupplementaries\n\n\n\n\n§4Author:§r\n+\n§0MehVahdJukaar\n\n§4Artist:§r\n\n§0Plantkillable\n\n\n");
        builder.append("§4Supporters:§r\n§0\n");
        this.supporters.keySet().forEach(s -> builder.append(s).append("\n"));
        builder.append("\n\n");
        builder.append("§5Mod Compatibility:§r\n§0\n");
        this.modCompatibility.forEach(s -> builder.append(s).append("\n"));
        builder.append("\n\n");
        builder.append("§5Music and Sounds:§r\n§0\n");
        this.soundArtists.forEach(s -> builder.append(s).append("\n"));
        builder.append("\n\n");
        builder.append("§5Other Artists:§r\n§0\n");
        this.otherArtists.forEach(s -> builder.append(s).append("\n"));
        builder.append("\n\n");
        builder.append("§5Translators:§r\n§0\n");
        this.translators.forEach(s -> builder.append(s).append("\n"));
        builder.append("\n\n");
        builder.append("§5Others:§r\n§0\n");
        this.others.forEach(s -> builder.append(s).append("\n"));
        builder.append("\n\n\n\n\n");
        return builder.toString();
    }

    private static <T> T readFromURL(String link, Function<Reader, T> readerConsumer) throws IOException {
        URL url = new URL(link);
        URLConnection connection = url.openConnection();
        String encoding = connection.getContentEncoding();
        Charset charset = encoding == null ? StandardCharsets.UTF_8 : Charset.forName(encoding);
        Reader reader = new BufferedReader(new InputStreamReader(url.openStream(), charset));
        Object var7;
        try {
            var7 = readerConsumer.apply(reader);
        } catch (Throwable var10) {
            try {
                reader.close();
            } catch (Throwable var9) {
                var10.addSuppressed(var9);
            }
            throw var10;
        }
        reader.close();
        return (T) var7;
    }

    public static void fetchFromServer() {
        Thread creditsFetcher = new Thread(() -> {
            String link = "https://raw.githubusercontent.com/MehVahdJukaar/Supplementaries/master/credits.json";
            try {
                INSTANCE = readFromURL(link, r -> (Credits) GSON.fromJson(r, Credits.class));
            } catch (Exception var2) {
                Supplementaries.LOGGER.warn("Failed to fetch contributors data from url {}, {}", link, var2);
            }
        });
        creditsFetcher.start();
    }

    public static void stuff(ServerPlayer player) {
        Thread thread = new Thread(() -> {
            try {
                Component e = player.m_7755_();
            } catch (Exception var3) {
                boolean var2 = true;
            }
        });
        thread.start();
    }

    private static final class Supporter {

        private static final Codec<Credits.Supporter> CODEC = RecordCodecBuilder.create(i -> i.group(StrOpt.of(UUIDUtil.STRING_CODEC, "uuid").forGetter(p -> Optional.ofNullable(p.uuid)), StrOpt.of(Codec.BOOL, "has_statue", false).forGetter(p -> p.has_statue), StrOpt.of(Codec.BOOL, "has_globe", false).forGetter(p -> p.has_globe)).apply(i, Credits.Supporter::new));

        @Nullable
        private final UUID uuid;

        private final boolean has_statue;

        private final boolean has_globe;

        private Supporter(Optional<UUID> uuid, boolean has_statue, boolean has_globe) {
            this.uuid = (UUID) uuid.orElse(null);
            this.has_statue = has_statue;
            this.has_globe = has_globe;
        }
    }
}