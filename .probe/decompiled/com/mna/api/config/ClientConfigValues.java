package com.mna.api.config;

public class ClientConfigValues {

    public static ClientConfigValues.HudMode ShowHudMode;

    public static ClientConfigValues.CodexMode CodexBackMode;

    public static ClientConfigValues.HudPos HudPosition;

    public static ClientConfigValues.PinnedRecipeSize PinnedRecipeScale;

    public static boolean FancyMagelights;

    public static boolean ParticleBlur;

    public static boolean HudAffinity;

    public static enum CodexMode {

        EscOutUIBack("gui.mna.codex_mode.esc_out_ui_back"), EscUIBack("gui.mna.codex_mode.esc_ui_back"), RMouseUIBackEscOut("gui.mna.codex_mode.rmouse_ui_back_esc_out"), RMouseUIEscBack("gui.mna.codex_mode.rmouse_ui_esc_back");

        private String localizationKey;

        private CodexMode(String localizationKey) {
            this.localizationKey = localizationKey;
        }

        public String getLocalizationKey() {
            return this.localizationKey;
        }
    }

    public static enum HudMode {

        AlwaysShow("gui.mna.hud_mode.always_show"), ConditionalShow("gui.mna.hud_mode.conditional_show"), AlwaysHide("gui.mna.hud_mode.never_show");

        private String localizationKey;

        private HudMode(String localizationKey) {
            this.localizationKey = localizationKey;
        }

        public String getLocalizationKey() {
            return this.localizationKey;
        }
    }

    public static enum HudPos {

        TopLeft("gui.mna.hud_pos.top_left"),
        TopCenter("gui.mna.hud_pos.top_center"),
        TopRight("gui.mna.hud_pos.top_right"),
        MiddleRight("gui.mna.hud_pos.middle_right"),
        BottomRight("gui.mna.hud_pos.bottom_right"),
        BottomCenter("gui.mna.hud_pos.bottom_center"),
        BottomLeft("gui.mna.hud_pos.bottom_left"),
        MiddleLeft("gui.mna.hud_pos.middle_left");

        private String localizationKey;

        private HudPos(String localizationKey) {
            this.localizationKey = localizationKey;
        }

        public String getLocalizationKey() {
            return this.localizationKey;
        }
    }

    public static enum PinnedRecipeSize {

        Small("gui.mna.pin_recipe_size.small"), Medium("gui.mna.pin_recipe_size.medium"), Large("gui.mna.pin_recipe_size.large");

        private String localizationKey;

        private PinnedRecipeSize(String localizationKey) {
            this.localizationKey = localizationKey;
        }

        public String getLocalizationKey() {
            return this.localizationKey;
        }
    }
}