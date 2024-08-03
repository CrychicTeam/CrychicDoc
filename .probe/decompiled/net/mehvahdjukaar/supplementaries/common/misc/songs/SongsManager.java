package net.mehvahdjukaar.supplementaries.common.misc.songs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import it.unimi.dsi.fastutil.ints.Int2IntRBTreeMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntArraySet;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntListIterator;
import it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.Map.Entry;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.supplementaries.Supplementaries;
import net.mehvahdjukaar.supplementaries.common.entities.HatStandEntity;
import net.mehvahdjukaar.supplementaries.common.items.InstrumentItem;
import net.mehvahdjukaar.supplementaries.common.network.ClientBoundPlaySongNotesPacket;
import net.mehvahdjukaar.supplementaries.common.network.ClientBoundSyncSongsPacket;
import net.mehvahdjukaar.supplementaries.common.network.ModNetwork;
import net.mehvahdjukaar.supplementaries.common.utils.MiscUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.random.WeightedRandom;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.NoteBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import org.jetbrains.annotations.NotNull;

public class SongsManager extends SimpleJsonResourceReloadListener {

    private static final Map<String, Song> SONGS = new LinkedHashMap();

    private static final List<WeightedEntry.Wrapper<String>> SONG_WEIGHTED_LIST = new ArrayList();

    private static final List<String> CAROLS = List.of("carol of the bells", "merry xmas", "jingle bells");

    private static final Map<UUID, Song> CURRENTLY_PAYING = new HashMap();

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    public static final SongsManager RELOAD_INSTANCE = new SongsManager();

    private static final Map<Long, List<Integer>> RECORDING = new HashMap();

    private static final List<NoteBlockInstrument> WHITELIST = new ArrayList();

    private static boolean isRecording = false;

    private static SongsManager.Source soundSource = SongsManager.Source.NOTE_BLOCKS;

    public SongsManager() {
        super(GSON, "flute_songs");
    }

    protected void apply(Map<ResourceLocation, JsonElement> jsons, ResourceManager manager, ProfilerFiller profile) {
        SONGS.clear();
        SONG_WEIGHTED_LIST.clear();
        List<Song> temp = new ArrayList();
        jsons.forEach((key, json) -> {
            try {
                DataResult<Song> result = Song.CODEC.parse(JsonOps.INSTANCE, json);
                Song song = (Song) result.getOrThrow(false, ex -> Supplementaries.LOGGER.error("Failed to parse flute song: {}", ex));
                temp.add(song);
                addSong(song);
            } catch (Exception var5) {
                Supplementaries.LOGGER.error("Failed to parse JSON object for song " + key);
            }
        });
        if (temp.size() != 0) {
            Supplementaries.LOGGER.info("Loaded " + temp.size() + " flute songs");
        }
    }

    private static void addSong(Song song) {
        SONGS.put(song.getName(), song);
        int weight = song.getWeight();
        if (weight > 0) {
            SONG_WEIGHTED_LIST.add(WeightedEntry.wrap(song.getName(), weight));
        }
    }

    public static void acceptClientSongs(List<Song> songs) {
        SONGS.clear();
        SONG_WEIGHTED_LIST.clear();
        songs.forEach(SongsManager::addSong);
    }

    public static void sendDataToClient(ServerPlayer player) {
        ModNetwork.CHANNEL.sendToClientPlayer(player, new ClientBoundSyncSongsPacket(SONGS.values()));
    }

    public static Song setCurrentlyPlaying(UUID id, String songKey) {
        Song song = (Song) SONGS.getOrDefault(songKey, Song.EMPTY);
        CURRENTLY_PAYING.put(id, song);
        song.validatePlayReady();
        return song;
    }

    public static void clearCurrentlyPlaying(UUID id) {
        CURRENTLY_PAYING.remove(id);
    }

    @NotNull
    private static String selectRandomSong(RandomSource random) {
        if (MiscUtils.FESTIVITY.isChristmas() && (double) random.nextFloat() > 0.8) {
            return (String) CAROLS.get(random.nextInt(CAROLS.size()));
        } else {
            Optional<WeightedEntry.Wrapper<String>> song = WeightedRandom.getRandomItem(random, SONG_WEIGHTED_LIST);
            return (String) song.map(WeightedEntry.Wrapper::m_146310_).orElse("");
        }
    }

    public static void playRandomSong(ItemStack stack, InstrumentItem instrument, LivingEntity entity, long timeSinceStarted) {
        UUID id = entity.m_20148_();
        Song song;
        if (!CURRENTLY_PAYING.containsKey(id)) {
            String res = null;
            if (stack.hasCustomHoverName()) {
                String name = stack.getHoverName().getString().toLowerCase(Locale.ROOT);
                for (String v : SONGS.keySet()) {
                    if (v.equals(name)) {
                        res = v;
                        break;
                    }
                }
            }
            if (res == null) {
                res = selectRandomSong(entity.m_9236_().random);
            }
            song = setCurrentlyPlaying(id, res);
        } else {
            song = (Song) CURRENTLY_PAYING.get(id);
        }
        playSong(instrument, entity, song, timeSinceStarted);
    }

    public static boolean playSong(InstrumentItem instrumentItem, LivingEntity entity, String sandstorm, long timeSinceStarted) {
        return playSong(instrumentItem, entity, (Song) SONGS.getOrDefault(sandstorm, Song.EMPTY), timeSinceStarted);
    }

    public static boolean playSong(InstrumentItem instrument, LivingEntity entity, Song song, long timeSinceStarted) {
        boolean played = false;
        if (timeSinceStarted % (long) song.getTempo() == 0L) {
            IntList notes = song.getNoteToPlay(timeSinceStarted);
            if (!notes.isEmpty() && notes.getInt(0) > 0) {
                ModNetwork.CHANNEL.sentToAllClientPlayersTrackingEntityAndSelf(entity, new ClientBoundPlaySongNotesPacket(notes, entity));
                played = true;
            }
            if (timeSinceStarted == 53L && song.getName().equals("skibidi")) {
                HatStandEntity.makeSkibidiInArea(entity);
            }
        }
        return played;
    }

    public static void startRecording(SongsManager.Source source, NoteBlockInstrument[] whitelist) {
        RECORDING.clear();
        isRecording = true;
        soundSource = source;
        WHITELIST.clear();
        WHITELIST.addAll(List.of(whitelist));
    }

    public static String stopRecording(Level level, String name, int speedup) {
        isRecording = false;
        long start = Long.MAX_VALUE;
        for (Long s : RECORDING.keySet()) {
            start = Math.min(start, s);
        }
        Int2IntRBTreeMap treeMap = new Int2IntRBTreeMap();
        for (Entry<Long, List<Integer>> e : RECORDING.entrySet()) {
            int notes = 0;
            List<Integer> noteList = (List<Integer>) e.getValue();
            for (int i = 0; i < Math.min(4, noteList.size()); i++) {
                notes = (int) ((double) notes + (double) ((Integer) noteList.get(i)).intValue() * Math.pow(100.0, (double) i));
            }
            treeMap.put((int) ((Long) e.getKey() - start), notes);
        }
        int largestInterval = 1;
        IntArraySet intervals = new IntArraySet();
        IntList arrayList = new IntArrayList();
        int lastTime = 0;
        ObjectBidirectionalIterator var21 = treeMap.int2IntEntrySet().iterator();
        while (var21.hasNext()) {
            it.unimi.dsi.fastutil.ints.Int2IntMap.Entry entry = (it.unimi.dsi.fastutil.ints.Int2IntMap.Entry) var21.next();
            int note = entry.getIntValue();
            int key = entry.getIntKey();
            int interval = -(key - lastTime);
            lastTime = key;
            if (interval != 0) {
                intervals.add(-interval);
                if (-interval > largestInterval) {
                    largestInterval = -interval;
                }
                arrayList.add(interval);
            }
            arrayList.add(note);
        }
        int GCD = 1;
        for (int div = largestInterval; div > 0; div--) {
            boolean match = intervals.intStream().allMatch(j -> j % div == 0);
            if (match) {
                GCD = Math.abs(div);
                break;
            }
        }
        IntList finalNotes = new IntArrayList();
        IntListIterator var25 = arrayList.iterator();
        while (var25.hasNext()) {
            int i = (Integer) var25.next();
            if (i < 0) {
                finalNotes.add(i / GCD);
            } else {
                finalNotes.add(i);
            }
        }
        if (name.isEmpty()) {
            name = "recorded-" + start;
        }
        Song song = new Song(name, GCD, finalNotes, "recorded in-game", 100);
        saveRecordedSong(song);
        SONGS.clear();
        SONGS.put(name, song);
        if (!level.isClientSide) {
            ModNetwork.CHANNEL.sendToAllClientPlayers(new ClientBoundSyncSongsPacket(SONGS.values()));
        }
        RECORDING.clear();
        return song.getTranslationKey();
    }

    public static void recordNoteFromNoteBlock(LevelAccessor levelAccessor, BlockPos pos) {
        if (isRecording && soundSource == SongsManager.Source.NOTE_BLOCKS && levelAccessor instanceof Level level) {
            BlockState state = level.getBlockState(pos);
            recordNote(level, (Integer) state.m_61143_(NoteBlock.NOTE) + 1, (NoteBlockInstrument) state.m_61143_(NoteBlock.INSTRUMENT));
        }
    }

    public static void recordNoteFromSound(SoundInstance sound, String name) {
        if (isRecording && name.startsWith("block.note_block") && soundSource == SongsManager.Source.SOUND_EVENTS) {
            try {
                String[] parts = name.split("\\.");
                String result = parts[parts.length - 1];
                NoteBlockInstrument inst = NoteBlockInstrument.valueOf(result.toUpperCase(Locale.ROOT));
                float pitch = sound.getPitch();
                int note = (int) Math.round(12.0 * (Math.log((double) pitch) / Math.log(2.0)) + 12.0);
                if (sound.getSource() == SoundSource.RECORDS) {
                    recordNote(Minecraft.getInstance().getCameraEntity().level(), note, inst);
                }
            } catch (Exception var7) {
                boolean resultx = true;
            }
        }
    }

    private static void recordNote(Level level, int note, NoteBlockInstrument instrument) {
        if (WHITELIST.isEmpty() || WHITELIST.contains(instrument)) {
            List<Integer> notes = (List<Integer>) RECORDING.computeIfAbsent(level.getGameTime(), t -> new ArrayList());
            notes.add(note);
        }
    }

    private static void saveRecordedSong(Song song) {
        File folder = PlatHelper.getGamePath().resolve("recorded_songs").toFile();
        if (!folder.exists()) {
            folder.mkdir();
        }
        File exportPath = new File(folder, song.getTranslationKey() + ".json");
        try {
            FileWriter writer = new FileWriter(exportPath);
            try {
                DataResult<JsonElement> r = Song.CODEC.encodeStart(JsonOps.INSTANCE, song);
                r.result().ifPresent(a -> GSON.toJson(a.getAsJsonObject(), writer));
            } catch (Throwable var7) {
                try {
                    writer.close();
                } catch (Throwable var6) {
                    var7.addSuppressed(var6);
                }
                throw var7;
            }
            writer.close();
        } catch (IOException var8) {
            var8.printStackTrace();
        }
    }

    public static enum Source {

        NOTE_BLOCKS, SOUND_EVENTS
    }
}