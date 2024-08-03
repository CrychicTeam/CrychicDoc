package net.minecraft.stats;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.mojang.datafixers.DataFixer;
import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Map.Entry;
import net.minecraft.SharedConstants;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.protocol.game.ClientboundAwardStatsPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.entity.player.Player;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;

public class ServerStatsCounter extends StatsCounter {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final MinecraftServer server;

    private final File file;

    private final Set<Stat<?>> dirty = Sets.newHashSet();

    public ServerStatsCounter(MinecraftServer minecraftServer0, File file1) {
        this.server = minecraftServer0;
        this.file = file1;
        if (file1.isFile()) {
            try {
                this.parseLocal(minecraftServer0.getFixerUpper(), FileUtils.readFileToString(file1));
            } catch (IOException var4) {
                LOGGER.error("Couldn't read statistics file {}", file1, var4);
            } catch (JsonParseException var5) {
                LOGGER.error("Couldn't parse statistics file {}", file1, var5);
            }
        }
    }

    public void save() {
        try {
            FileUtils.writeStringToFile(this.file, this.toJson());
        } catch (IOException var2) {
            LOGGER.error("Couldn't save stats", var2);
        }
    }

    @Override
    public void setValue(Player player0, Stat<?> stat1, int int2) {
        super.setValue(player0, stat1, int2);
        this.dirty.add(stat1);
    }

    private Set<Stat<?>> getDirty() {
        Set<Stat<?>> $$0 = Sets.newHashSet(this.dirty);
        this.dirty.clear();
        return $$0;
    }

    public void parseLocal(DataFixer dataFixer0, String string1) {
        try {
            JsonReader $$2 = new JsonReader(new StringReader(string1));
            label47: {
                try {
                    $$2.setLenient(false);
                    JsonElement $$3 = Streams.parse($$2);
                    if (!$$3.isJsonNull()) {
                        CompoundTag $$4 = fromJson($$3.getAsJsonObject());
                        $$4 = DataFixTypes.STATS.updateToCurrentVersion(dataFixer0, $$4, NbtUtils.getDataVersion($$4, 1343));
                        if (!$$4.contains("stats", 10)) {
                            break label47;
                        }
                        CompoundTag $$5 = $$4.getCompound("stats");
                        Iterator var7 = $$5.getAllKeys().iterator();
                        while (true) {
                            if (!var7.hasNext()) {
                                break label47;
                            }
                            String $$6 = (String) var7.next();
                            if ($$5.contains($$6, 10)) {
                                Util.ifElse(BuiltInRegistries.STAT_TYPE.getOptional(new ResourceLocation($$6)), p_12844_ -> {
                                    CompoundTag $$3x = $$5.getCompound($$6);
                                    for (String $$4x : $$3x.getAllKeys()) {
                                        if ($$3x.contains($$4x, 99)) {
                                            Util.ifElse(this.getStat(p_12844_, $$4x), p_144252_ -> this.f_13013_.put(p_144252_, $$3x.getInt($$4x)), () -> LOGGER.warn("Invalid statistic in {}: Don't know what {} is", this.file, $$4x));
                                        } else {
                                            LOGGER.warn("Invalid statistic value in {}: Don't know what {} is for key {}", new Object[] { this.file, $$3x.get($$4x), $$4x });
                                        }
                                    }
                                }, () -> LOGGER.warn("Invalid statistic type in {}: Don't know what {} is", this.file, $$6));
                            }
                        }
                    }
                    LOGGER.error("Unable to parse Stat data from {}", this.file);
                } catch (Throwable var10) {
                    try {
                        $$2.close();
                    } catch (Throwable var9) {
                        var10.addSuppressed(var9);
                    }
                    throw var10;
                }
                $$2.close();
                return;
            }
            $$2.close();
        } catch (IOException | JsonParseException var11) {
            LOGGER.error("Unable to parse Stat data from {}", this.file, var11);
        }
    }

    private <T> Optional<Stat<T>> getStat(StatType<T> statTypeT0, String string1) {
        return Optional.ofNullable(ResourceLocation.tryParse(string1)).flatMap(statTypeT0.getRegistry()::m_6612_).map(statTypeT0::m_12902_);
    }

    private static CompoundTag fromJson(JsonObject jsonObject0) {
        CompoundTag $$1 = new CompoundTag();
        for (Entry<String, JsonElement> $$2 : jsonObject0.entrySet()) {
            JsonElement $$3 = (JsonElement) $$2.getValue();
            if ($$3.isJsonObject()) {
                $$1.put((String) $$2.getKey(), fromJson($$3.getAsJsonObject()));
            } else if ($$3.isJsonPrimitive()) {
                JsonPrimitive $$4 = $$3.getAsJsonPrimitive();
                if ($$4.isNumber()) {
                    $$1.putInt((String) $$2.getKey(), $$4.getAsInt());
                }
            }
        }
        return $$1;
    }

    protected String toJson() {
        Map<StatType<?>, JsonObject> $$0 = Maps.newHashMap();
        ObjectIterator $$3 = this.f_13013_.object2IntEntrySet().iterator();
        while ($$3.hasNext()) {
            it.unimi.dsi.fastutil.objects.Object2IntMap.Entry<Stat<?>> $$1 = (it.unimi.dsi.fastutil.objects.Object2IntMap.Entry<Stat<?>>) $$3.next();
            Stat<?> $$2 = (Stat<?>) $$1.getKey();
            ((JsonObject) $$0.computeIfAbsent($$2.getType(), p_12822_ -> new JsonObject())).addProperty(getKey($$2).toString(), $$1.getIntValue());
        }
        JsonObject $$3x = new JsonObject();
        for (Entry<StatType<?>, JsonObject> $$4 : $$0.entrySet()) {
            $$3x.add(BuiltInRegistries.STAT_TYPE.getKey((StatType<?>) $$4.getKey()).toString(), (JsonElement) $$4.getValue());
        }
        JsonObject $$5 = new JsonObject();
        $$5.add("stats", $$3x);
        $$5.addProperty("DataVersion", SharedConstants.getCurrentVersion().getDataVersion().getVersion());
        return $$5.toString();
    }

    private static <T> ResourceLocation getKey(Stat<T> statT0) {
        return statT0.getType().getRegistry().getKey(statT0.getValue());
    }

    public void markAllDirty() {
        this.dirty.addAll(this.f_13013_.keySet());
    }

    public void sendStats(ServerPlayer serverPlayer0) {
        Object2IntMap<Stat<?>> $$1 = new Object2IntOpenHashMap();
        for (Stat<?> $$2 : this.getDirty()) {
            $$1.put($$2, this.m_13015_($$2));
        }
        serverPlayer0.connection.send(new ClientboundAwardStatsPacket($$1));
    }
}