package mezz.jei.common.config;

import java.util.function.Supplier;
import mezz.jei.common.config.file.IConfigCategoryBuilder;
import mezz.jei.common.config.file.IConfigSchemaBuilder;
import org.jetbrains.annotations.Nullable;

public final class DebugConfig {

    @Nullable
    private static DebugConfig instance;

    private final Supplier<Boolean> debugModeEnabled;

    private final Supplier<Boolean> debugInputsEnabled;

    private final Supplier<Boolean> crashingTestIngredientsEnabled;

    public static void create(IConfigSchemaBuilder schema) {
        instance = new DebugConfig(schema);
    }

    private DebugConfig(IConfigSchemaBuilder schema) {
        IConfigCategoryBuilder advanced = schema.addCategory("debug");
        this.debugModeEnabled = advanced.addBoolean("DebugMode", false, "Debug mode enabled");
        this.debugInputsEnabled = advanced.addBoolean("DebugInputs", false, "Debug inputs enabled");
        this.crashingTestIngredientsEnabled = advanced.addBoolean("CrashingTestItemsEnabled", false, "Adds ingredients to JEI that intentionally crash, to help debug JEI.");
    }

    public static boolean isDebugModeEnabled() {
        return instance == null ? false : (Boolean) instance.debugModeEnabled.get();
    }

    public static boolean isDebugInputsEnabled() {
        return instance == null ? false : (Boolean) instance.debugInputsEnabled.get();
    }

    public static boolean isCrashingTestIngredientsEnabled() {
        return instance == null ? false : (Boolean) instance.crashingTestIngredientsEnabled.get();
    }
}