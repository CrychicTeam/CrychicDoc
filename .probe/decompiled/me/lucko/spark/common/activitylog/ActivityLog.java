package me.lucko.spark.common.activitylog;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

public class ActivityLog {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private static final JsonParser PARSER = new JsonParser();

    private final Path file;

    private final LinkedList<Activity> log = new LinkedList();

    private final Object[] mutex = new Object[0];

    public ActivityLog(Path file) {
        this.file = file;
    }

    public void addToLog(Activity activity) {
        synchronized (this.mutex) {
            this.log.addFirst(activity);
        }
        this.save();
    }

    public List<Activity> getLog() {
        synchronized (this.mutex) {
            return new LinkedList(this.log);
        }
    }

    public void save() {
        JsonArray array = new JsonArray();
        synchronized (this.mutex) {
            for (Activity activity : this.log) {
                if (!activity.shouldExpire()) {
                    array.add(activity.serialize());
                }
            }
        }
        try {
            Files.createDirectories(this.file.getParent());
        } catch (IOException var7) {
        }
        try {
            BufferedWriter writer = Files.newBufferedWriter(this.file, StandardCharsets.UTF_8);
            try {
                GSON.toJson(array, writer);
            } catch (Throwable var8) {
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (Throwable var6) {
                        var8.addSuppressed(var6);
                    }
                }
                throw var8;
            }
            if (writer != null) {
                writer.close();
            }
        } catch (IOException var9) {
            var9.printStackTrace();
        }
    }

    public void load() {
        if (!Files.exists(this.file, new LinkOption[0])) {
            synchronized (this.mutex) {
                this.log.clear();
            }
        } else {
            JsonArray array;
            try {
                BufferedReader reader = Files.newBufferedReader(this.file, StandardCharsets.UTF_8);
                try {
                    array = PARSER.parse(reader).getAsJsonArray();
                } catch (Throwable var12) {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (Throwable var8) {
                            var12.addSuppressed(var8);
                        }
                    }
                    throw var12;
                }
                if (reader != null) {
                    reader.close();
                }
            } catch (IllegalStateException | IOException var14) {
                var14.printStackTrace();
                return;
            }
            boolean save = false;
            synchronized (this.mutex) {
                this.log.clear();
                for (JsonElement element : array) {
                    try {
                        Activity activity = Activity.deserialize(element);
                        if (activity.shouldExpire()) {
                            save = true;
                        } else {
                            this.log.add(activity);
                        }
                    } catch (Exception var11) {
                        var11.printStackTrace();
                    }
                }
            }
            if (save) {
                try {
                    this.save();
                } catch (Exception var10) {
                }
            }
        }
    }
}