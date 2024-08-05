package net.minecraft.server.players;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.util.GsonHelper;
import org.slf4j.Logger;

public abstract class StoredUserList<K, V extends StoredUserEntry<K>> {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final File file;

    private final Map<String, V> map = Maps.newHashMap();

    public StoredUserList(File file0) {
        this.file = file0;
    }

    public File getFile() {
        return this.file;
    }

    public void add(V v0) {
        this.map.put(this.getKeyForUser(v0.getUser()), v0);
        try {
            this.save();
        } catch (IOException var3) {
            LOGGER.warn("Could not save the list after adding a user.", var3);
        }
    }

    @Nullable
    public V get(K k0) {
        this.removeExpired();
        return (V) this.map.get(this.getKeyForUser(k0));
    }

    public void remove(K k0) {
        this.map.remove(this.getKeyForUser(k0));
        try {
            this.save();
        } catch (IOException var3) {
            LOGGER.warn("Could not save the list after removing a user.", var3);
        }
    }

    public void remove(StoredUserEntry<K> storedUserEntryK0) {
        this.remove(storedUserEntryK0.getUser());
    }

    public String[] getUserList() {
        return (String[]) this.map.keySet().toArray(new String[0]);
    }

    public boolean isEmpty() {
        return this.map.size() < 1;
    }

    protected String getKeyForUser(K k0) {
        return k0.toString();
    }

    protected boolean contains(K k0) {
        return this.map.containsKey(this.getKeyForUser(k0));
    }

    private void removeExpired() {
        List<K> $$0 = Lists.newArrayList();
        for (V $$1 : this.map.values()) {
            if ($$1.hasExpired()) {
                $$0.add($$1.getUser());
            }
        }
        for (K $$2 : $$0) {
            this.map.remove(this.getKeyForUser($$2));
        }
    }

    protected abstract StoredUserEntry<K> createEntry(JsonObject var1);

    public Collection<V> getEntries() {
        return this.map.values();
    }

    public void save() throws IOException {
        JsonArray $$0 = new JsonArray();
        this.map.values().stream().map(p_11392_ -> Util.make(new JsonObject(), p_11392_::m_6009_)).forEach($$0::add);
        BufferedWriter $$1 = Files.newWriter(this.file, StandardCharsets.UTF_8);
        try {
            GSON.toJson($$0, $$1);
        } catch (Throwable var6) {
            if ($$1 != null) {
                try {
                    $$1.close();
                } catch (Throwable var5) {
                    var6.addSuppressed(var5);
                }
            }
            throw var6;
        }
        if ($$1 != null) {
            $$1.close();
        }
    }

    public void load() throws IOException {
        if (this.file.exists()) {
            BufferedReader $$0 = Files.newReader(this.file, StandardCharsets.UTF_8);
            try {
                JsonArray $$1 = (JsonArray) GSON.fromJson($$0, JsonArray.class);
                this.map.clear();
                for (JsonElement $$2 : $$1) {
                    JsonObject $$3 = GsonHelper.convertToJsonObject($$2, "entry");
                    StoredUserEntry<K> $$4 = this.createEntry($$3);
                    if ($$4.getUser() != null) {
                        this.map.put(this.getKeyForUser($$4.getUser()), $$4);
                    }
                }
            } catch (Throwable var8) {
                if ($$0 != null) {
                    try {
                        $$0.close();
                    } catch (Throwable var7) {
                        var8.addSuppressed(var7);
                    }
                }
                throw var8;
            }
            if ($$0 != null) {
                $$0.close();
            }
        }
    }
}