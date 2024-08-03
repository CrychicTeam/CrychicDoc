package com.github.einjerjar.mc.keymap.config;

import com.github.einjerjar.mc.keymap.Keymap;
import com.github.einjerjar.mc.keymap.cross.Services;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;

public class KeymapConfig {

    static KeymapConfig instance;

    static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private static File cfgFile = null;

    protected boolean firstOpenDone = false;

    protected boolean autoSelectLayout = false;

    protected String customLayout = "en_us";

    protected boolean replaceKeybindScreen = true;

    protected boolean malilibSupport = true;

    protected boolean debug = false;

    protected boolean debug2 = false;

    protected boolean crashOnProblematicError = false;

    protected boolean showHelpTooltips = true;

    private static synchronized File cfgFile() {
        if (cfgFile == null) {
            cfgFile = Services.PLATFORM.config("keymap.json");
        }
        return cfgFile;
    }

    public static synchronized KeymapConfig instance() {
        if (instance == null) {
            instance = new KeymapConfig();
        }
        return instance;
    }

    public static void save() {
        try {
            FileWriter writer = new FileWriter(cfgFile());
            try {
                gson.toJson(instance(), writer);
            } catch (Throwable var4) {
                try {
                    writer.close();
                } catch (Throwable var3) {
                    var4.addSuppressed(var3);
                }
                throw var4;
            }
            writer.close();
        } catch (Exception var5) {
            Keymap.logger().error("!! Cant save config !!");
            var5.printStackTrace();
        }
    }

    public static void load() {
        try {
            FileReader reader = new FileReader(cfgFile());
            try {
                instance = (KeymapConfig) gson.fromJson(reader, KeymapConfig.class);
            } catch (Throwable var4) {
                try {
                    reader.close();
                } catch (Throwable var3) {
                    var4.addSuppressed(var3);
                }
                throw var4;
            }
            reader.close();
        } catch (FileNotFoundException var5) {
            Keymap.logger().warn("!! Config not found, using default settings !!");
            save();
        } catch (Exception var6) {
            Keymap.logger().warn("!! Cant read config, using default settings !!");
            var6.printStackTrace();
            save();
        }
    }

    public boolean firstOpenDone() {
        return this.firstOpenDone;
    }

    public boolean autoSelectLayout() {
        return this.autoSelectLayout;
    }

    public String customLayout() {
        return this.customLayout;
    }

    public boolean replaceKeybindScreen() {
        return this.replaceKeybindScreen;
    }

    public boolean malilibSupport() {
        return this.malilibSupport;
    }

    public boolean debug() {
        return this.debug;
    }

    public boolean debug2() {
        return this.debug2;
    }

    public boolean crashOnProblematicError() {
        return this.crashOnProblematicError;
    }

    public boolean showHelpTooltips() {
        return this.showHelpTooltips;
    }

    public KeymapConfig firstOpenDone(boolean firstOpenDone) {
        this.firstOpenDone = firstOpenDone;
        return this;
    }

    public KeymapConfig autoSelectLayout(boolean autoSelectLayout) {
        this.autoSelectLayout = autoSelectLayout;
        return this;
    }

    public KeymapConfig customLayout(String customLayout) {
        this.customLayout = customLayout;
        return this;
    }

    public KeymapConfig replaceKeybindScreen(boolean replaceKeybindScreen) {
        this.replaceKeybindScreen = replaceKeybindScreen;
        return this;
    }

    public KeymapConfig malilibSupport(boolean malilibSupport) {
        this.malilibSupport = malilibSupport;
        return this;
    }

    public KeymapConfig debug(boolean debug) {
        this.debug = debug;
        return this;
    }

    public KeymapConfig debug2(boolean debug2) {
        this.debug2 = debug2;
        return this;
    }

    public KeymapConfig crashOnProblematicError(boolean crashOnProblematicError) {
        this.crashOnProblematicError = crashOnProblematicError;
        return this;
    }

    public KeymapConfig showHelpTooltips(boolean showHelpTooltips) {
        this.showHelpTooltips = showHelpTooltips;
        return this;
    }

    public String toString() {
        return "KeymapConfig(firstOpenDone=" + this.firstOpenDone() + ", autoSelectLayout=" + this.autoSelectLayout() + ", customLayout=" + this.customLayout() + ", replaceKeybindScreen=" + this.replaceKeybindScreen() + ", malilibSupport=" + this.malilibSupport() + ", debug=" + this.debug() + ", debug2=" + this.debug2() + ", crashOnProblematicError=" + this.crashOnProblematicError() + ", showHelpTooltips=" + this.showHelpTooltips() + ")";
    }

    public KeymapConfig() {
    }

    public KeymapConfig(boolean firstOpenDone, boolean autoSelectLayout, String customLayout, boolean replaceKeybindScreen, boolean malilibSupport, boolean debug, boolean debug2, boolean crashOnProblematicError, boolean showHelpTooltips) {
        this.firstOpenDone = firstOpenDone;
        this.autoSelectLayout = autoSelectLayout;
        this.customLayout = customLayout;
        this.replaceKeybindScreen = replaceKeybindScreen;
        this.malilibSupport = malilibSupport;
        this.debug = debug;
        this.debug2 = debug2;
        this.crashOnProblematicError = crashOnProblematicError;
        this.showHelpTooltips = showHelpTooltips;
    }

    @FunctionalInterface
    public interface KeymapConfigDirProvider {

        File execute(String var1);
    }
}