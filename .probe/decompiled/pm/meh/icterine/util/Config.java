package pm.meh.icterine.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;
import pm.meh.icterine.Common;

public class Config {

    private static final String configFileName = "icterine.yml";

    private static final Path configPath = Paths.get("config", "icterine.yml");

    public final boolean DEBUG_MODE;

    public final boolean IGNORE_TRIGGERS_FOR_EMPTIED_STACKS;

    public final boolean IGNORE_TRIGGERS_FOR_DECREASED_STACKS;

    public final boolean OPTIMIZE_MULTIPLE_PREDICATE_TRIGGER;

    public final boolean INITIALIZE_INVENTORY_LAST_SLOTS;

    public final boolean OPTIMIZE_TRIGGERS_FOR_INCREASED_STACKS;

    public final boolean CHECK_COUNT_BEFORE_ITEM_PREDICATE_MATCH;

    private final Map<String, String> configData = new HashMap();

    public Config() {
        try {
            this.load();
        } catch (IOException var2) {
            throw new RuntimeException(var2);
        }
        this.DEBUG_MODE = this.getBoolean("debug_mode", false);
        this.IGNORE_TRIGGERS_FOR_EMPTIED_STACKS = this.getBoolean("ignore_triggers_for_emptied_stacks", true);
        this.IGNORE_TRIGGERS_FOR_DECREASED_STACKS = this.getBoolean("ignore_triggers_for_decreased_stacks", true);
        this.OPTIMIZE_MULTIPLE_PREDICATE_TRIGGER = this.getBoolean("optimize_multiple_predicate_trigger", true);
        this.INITIALIZE_INVENTORY_LAST_SLOTS = this.getBoolean("initialize_inventory_last_slots", true);
        this.OPTIMIZE_TRIGGERS_FOR_INCREASED_STACKS = this.getBoolean("optimize_triggers_for_increased_stacks", true);
        this.CHECK_COUNT_BEFORE_ITEM_PREDICATE_MATCH = this.getBoolean("check_count_before_item_predicate_match", true);
    }

    private void load() throws IOException {
        if (!Files.exists(configPath, new LinkOption[0])) {
            Files.createDirectories(Paths.get("config"));
            Files.copy((InputStream) Objects.requireNonNull(this.getClass().getResourceAsStream("/icterine.yml")), configPath, new CopyOption[0]);
        }
        Stream<String> lines = Files.lines(configPath);
        try {
            lines.forEach(line -> {
                if (!line.trim().isEmpty() && line.charAt(0) != '#') {
                    String[] parts = line.split(": ");
                    if (parts.length != 2) {
                        throw new RuntimeException("Invalid config parameter:\n" + line);
                    }
                    this.configData.put(parts[0], parts[1]);
                }
            });
        } catch (Throwable var5) {
            if (lines != null) {
                try {
                    lines.close();
                } catch (Throwable var4) {
                    var5.addSuppressed(var4);
                }
            }
            throw var5;
        }
        if (lines != null) {
            lines.close();
        }
    }

    private boolean getBoolean(String key, boolean defaultValue) {
        String value = (String) this.configData.get(key);
        if (value == null) {
            try {
                Common.LOG.error("Appending missing Icterine option {} to config file", key);
                Files.write(configPath, ("\n\n" + key + ": " + defaultValue).getBytes(), new OpenOption[] { StandardOpenOption.APPEND });
            } catch (IOException var5) {
                Common.LOG.error("Unable to append missing option {} to config file: {}", key, var5);
            }
            return defaultValue;
        } else {
            return value.equals("true");
        }
    }
}