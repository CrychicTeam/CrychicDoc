package dev.ftb.mods.ftbxmodcompat.config;

import dev.ftb.mods.ftblibrary.config.NameMap;
import dev.ftb.mods.ftblibrary.snbt.config.EnumValue;
import dev.ftb.mods.ftblibrary.snbt.config.SNBTConfig;
import dev.ftb.mods.ftbxmodcompat.FTBXModCompat;
import java.util.function.BooleanSupplier;

public interface FTBXModConfig {

    SNBTConfig CONFIG = SNBTConfig.create("ftbxmodcompat");

    EnumValue<FTBXModConfig.StageSelector> STAGE_SELECTOR = CONFIG.addEnum("stage_selector", NameMap.of(FTBXModConfig.StageSelector.DEFAULT, FTBXModConfig.StageSelector.values()).create()).comment(new String[] { "Select the game stages implementation to use", "DEFAULT: use KubeJS, Game Stages, vanilla in preference order, depending on mod availability" });

    EnumValue<FTBXModConfig.PermSelector> PERMISSION_SELECTOR = CONFIG.addEnum("permission_selector", NameMap.of(FTBXModConfig.PermSelector.DEFAULT, FTBXModConfig.PermSelector.values()).create()).comment(new String[] { "Select the permissions implementation to use", "DEFAULT: use FTB Ranks then Luckperms in preference order, depending on mod availability" });

    public static enum PermSelector {

        DEFAULT(() -> true), FTB_RANKS(() -> FTBXModCompat.isFTBRanksLoaded), LUCKPERMS(() -> FTBXModCompat.isLuckPermsLoaded);

        private final BooleanSupplier usable;

        private PermSelector(BooleanSupplier usable) {
            this.usable = usable;
        }

        public boolean isUsable() {
            return this.usable.getAsBoolean();
        }
    }

    public static enum StageSelector {

        DEFAULT(() -> true), VANILLA(() -> true), KUBEJS(() -> FTBXModCompat.isKubeJSLoaded), GAMESTAGES(() -> FTBXModCompat.isGameStagesLoaded);

        private final BooleanSupplier usable;

        private StageSelector(BooleanSupplier usable) {
            this.usable = usable;
        }

        public boolean isUsable() {
            return this.usable.getAsBoolean();
        }
    }
}