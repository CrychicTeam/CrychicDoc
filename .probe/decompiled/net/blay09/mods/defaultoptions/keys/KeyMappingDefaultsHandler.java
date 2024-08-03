package net.blay09.mods.defaultoptions.keys;

import com.mojang.blaze3d.platform.InputConstants;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.blay09.mods.balm.api.client.keymappings.KeyModifier;
import net.blay09.mods.defaultoptions.DefaultOptions;
import net.blay09.mods.defaultoptions.PlatformBindings;
import net.blay09.mods.defaultoptions.api.DefaultOptionsCategory;
import net.blay09.mods.defaultoptions.api.DefaultOptionsHandler;
import net.blay09.mods.defaultoptions.api.DefaultOptionsLoadStage;
import net.blay09.mods.defaultoptions.mixin.KeyMappingAccessor;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;

public class KeyMappingDefaultsHandler implements DefaultOptionsHandler {

    private static final Pattern KEY_PATTERN = Pattern.compile("key_([^:]+):([^:]+)(?::(.+))?");

    private static final Map<String, DefaultKeyMapping> defaultKeys = new HashMap();

    private static final List<String> knownKeys = new ArrayList();

    private File getDefaultOptionsFile() {
        return new File(DefaultOptions.getDefaultOptionsFolder(), "keybindings.txt");
    }

    @Override
    public String getId() {
        return "keymappings";
    }

    @Override
    public DefaultOptionsCategory getCategory() {
        return DefaultOptionsCategory.KEYS;
    }

    @Override
    public DefaultOptionsLoadStage getLoadStage() {
        return DefaultOptionsLoadStage.POST_LOAD;
    }

    @Override
    public void saveCurrentOptions() {
        Minecraft.getInstance().options.save();
    }

    @Override
    public void saveCurrentOptionsAsDefault() {
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(new File(DefaultOptions.getDefaultOptionsFolder(), "keybindings.txt")));
            try {
                for (KeyMapping keyMapping : Minecraft.getInstance().options.keyMappings) {
                    KeyModifier keyModifier = PlatformBindings.INSTANCE.getKeyModifier(keyMapping);
                    writer.println("key_" + keyMapping.getName() + ":" + keyMapping.saveString() + ":" + keyModifier.name());
                }
            } catch (Throwable var8) {
                try {
                    writer.close();
                } catch (Throwable var7) {
                    var8.addSuppressed(var7);
                }
                throw var8;
            }
            writer.close();
        } catch (IOException var9) {
            DefaultOptions.logger.error("Failed to save default key mappings", var9);
        }
        this.loadDefaults();
    }

    @Override
    public boolean hasDefaults() {
        return this.getDefaultOptionsFile().exists();
    }

    @Override
    public boolean shouldLoadDefaults() {
        return true;
    }

    @Override
    public void loadDefaults() {
        defaultKeys.clear();
        knownKeys.clear();
        File defaultKeysFile = new File(DefaultOptions.getDefaultOptionsFolder(), "keybindings.txt");
        if (defaultKeysFile.exists()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(defaultKeysFile));
                String line;
                try {
                    while ((line = reader.readLine()) != null) {
                        if (!line.isEmpty()) {
                            Matcher matcher = KEY_PATTERN.matcher(line);
                            if (matcher.matches()) {
                                try {
                                    KeyModifier modifier = matcher.group(3) != null ? KeyModifier.valueOf(matcher.group(3)) : KeyModifier.NONE;
                                    defaultKeys.put(matcher.group(1), new DefaultKeyMapping(InputConstants.getKey(matcher.group(2)), modifier));
                                } catch (Exception var12) {
                                    DefaultOptions.logger.error("Error loading default key binding for {}", line, var12);
                                }
                            }
                        }
                    }
                } catch (Throwable var17) {
                    try {
                        reader.close();
                    } catch (Throwable var11) {
                        var17.addSuppressed(var11);
                    }
                    throw var17;
                }
                reader.close();
            } catch (Exception var18) {
                DefaultOptions.logger.error("Error loading default key bindings", var18);
            }
        }
        File knownKeysFile = new File(DefaultOptions.getMinecraftDataDir(), "knownkeys.txt");
        if (knownKeysFile.exists()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(knownKeysFile));
                String line;
                try {
                    while ((line = reader.readLine()) != null) {
                        if (!line.isEmpty()) {
                            knownKeys.add(line);
                        }
                    }
                } catch (Throwable var15) {
                    try {
                        reader.close();
                    } catch (Throwable var10) {
                        var15.addSuppressed(var10);
                    }
                    throw var15;
                }
                reader.close();
            } catch (IOException var16) {
                DefaultOptions.logger.error("Error loading known key bindings", var16);
            }
        }
        for (KeyMapping keyMapping : Minecraft.getInstance().options.keyMappings) {
            if (defaultKeys.containsKey(keyMapping.getName())) {
                DefaultKeyMapping defaultKeyMapping = (DefaultKeyMapping) defaultKeys.get(keyMapping.getName());
                ((KeyMappingAccessor) keyMapping).setDefaultKey(defaultKeyMapping.input);
                PlatformBindings.INSTANCE.setDefaultKeyModifier(keyMapping, defaultKeyMapping.modifier);
                if (!knownKeys.contains(keyMapping.getName())) {
                    KeyModifier defaultKeyModifier = PlatformBindings.INSTANCE.getDefaultKeyModifier(keyMapping);
                    PlatformBindings.INSTANCE.setKeyModifier(keyMapping, defaultKeyModifier);
                    keyMapping.setKey(keyMapping.getDefaultKey());
                    knownKeys.add(keyMapping.getName());
                }
            }
        }
        KeyMapping.resetMapping();
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(new File(DefaultOptions.getMinecraftDataDir(), "knownkeys.txt")));
            try {
                for (String key : knownKeys) {
                    writer.println(key);
                }
            } catch (Throwable var13) {
                try {
                    writer.close();
                } catch (Throwable var9) {
                    var13.addSuppressed(var9);
                }
                throw var13;
            }
            writer.close();
        } catch (IOException var14) {
            DefaultOptions.logger.error("Error saving known key bindings", var14);
        }
    }
}