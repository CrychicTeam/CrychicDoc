package com.frikinjay.lmd.config;

import com.frikinjay.lmd.LetMeDespawn;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

public class LetMeDespawnConfig {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private Set<String> mobNames = new HashSet();

    public Set<String> getMobNames() {
        return this.mobNames;
    }

    public void addMobName(String mobName) {
        this.mobNames.add(mobName);
        this.save();
    }

    public void removeMobName(String mobName) {
        this.mobNames.remove(mobName);
        this.save();
    }

    public static LetMeDespawnConfig load() {
        if (LetMeDespawn.CONFIG_FILE.exists()) {
            try {
                FileReader reader = new FileReader(LetMeDespawn.CONFIG_FILE);
                LetMeDespawnConfig var1;
                try {
                    var1 = (LetMeDespawnConfig) GSON.fromJson(reader, LetMeDespawnConfig.class);
                } catch (Throwable var4) {
                    try {
                        reader.close();
                    } catch (Throwable var3) {
                        var4.addSuppressed(var3);
                    }
                    throw var4;
                }
                reader.close();
                return var1;
            } catch (JsonSyntaxException | IOException var5) {
                LetMeDespawn.logger.log(Level.SEVERE, "Failed to load LetMeDespawn config", var5);
            }
        }
        return new LetMeDespawnConfig();
    }

    public void save() {
        try {
            FileWriter writer = new FileWriter(LetMeDespawn.CONFIG_FILE);
            try {
                GSON.toJson(this, writer);
            } catch (Throwable var5) {
                try {
                    writer.close();
                } catch (Throwable var4) {
                    var5.addSuppressed(var4);
                }
                throw var5;
            }
            writer.close();
        } catch (IOException var6) {
            LetMeDespawn.logger.log(Level.SEVERE, "Failed to save LetMeDespawn config", var6);
        }
    }

    public static void createDefaultConfig() {
        LetMeDespawnConfig config = new LetMeDespawnConfig();
        config.save();
    }
}