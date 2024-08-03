package net.minecraftforge.client.settings;

import com.mojang.blaze3d.platform.InputConstants;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public enum KeyModifier {

    CONTROL {

        @Override
        public boolean matches(InputConstants.Key key) {
            int keyCode = key.getValue();
            return Minecraft.ON_OSX ? keyCode == 343 || keyCode == 347 : keyCode == 341 || keyCode == 345;
        }

        @Override
        public boolean isActive(@Nullable IKeyConflictContext conflictContext) {
            return Screen.hasControlDown();
        }

        @Override
        public Component getCombinedName(InputConstants.Key key, Supplier<Component> defaultLogic) {
            String localizationFormatKey = Minecraft.ON_OSX ? "forge.controlsgui.control.mac" : "forge.controlsgui.control";
            return Component.translatable(localizationFormatKey, defaultLogic.get());
        }
    }
    , SHIFT {

        @Override
        public boolean matches(InputConstants.Key key) {
            return key.getValue() == 340 || key.getValue() == 344;
        }

        @Override
        public boolean isActive(@Nullable IKeyConflictContext conflictContext) {
            return Screen.hasShiftDown();
        }

        @Override
        public Component getCombinedName(InputConstants.Key key, Supplier<Component> defaultLogic) {
            return Component.translatable("forge.controlsgui.shift", defaultLogic.get());
        }
    }
    , ALT {

        @Override
        public boolean matches(InputConstants.Key key) {
            return key.getValue() == 342 || key.getValue() == 346;
        }

        @Override
        public boolean isActive(@Nullable IKeyConflictContext conflictContext) {
            return Screen.hasAltDown();
        }

        @Override
        public Component getCombinedName(InputConstants.Key keyCode, Supplier<Component> defaultLogic) {
            return Component.translatable("forge.controlsgui.alt", defaultLogic.get());
        }
    }
    , NONE {

        @Override
        public boolean matches(InputConstants.Key key) {
            return false;
        }

        @Override
        public boolean isActive(@Nullable IKeyConflictContext conflictContext) {
            if (conflictContext != null && !conflictContext.conflicts(KeyConflictContext.IN_GAME)) {
                for (KeyModifier keyModifier : KeyModifier.VALUES) {
                    if (keyModifier.isActive(conflictContext)) {
                        return false;
                    }
                }
            }
            return true;
        }

        @Override
        public Component getCombinedName(InputConstants.Key key, Supplier<Component> defaultLogic) {
            return (Component) defaultLogic.get();
        }
    }
    ;

    @Deprecated(forRemoval = true, since = "1.20.2")
    public static final KeyModifier[] MODIFIER_VALUES = new KeyModifier[] { SHIFT, CONTROL, ALT };

    private static final KeyModifier[] VALUES = new KeyModifier[] { SHIFT, CONTROL, ALT };

    private static final List<KeyModifier> VALUES_LIST = List.of(SHIFT, CONTROL, ALT);

    private static final List<KeyModifier> ALL = List.of(SHIFT, CONTROL, ALT, NONE);

    @Deprecated(forRemoval = true, since = "1.20.2")
    public static KeyModifier getActiveModifier() {
        for (KeyModifier keyModifier : VALUES) {
            if (keyModifier.isActive(null)) {
                return keyModifier;
            }
        }
        return NONE;
    }

    public static final List<KeyModifier> getValues(boolean includeNone) {
        return includeNone ? ALL : VALUES_LIST;
    }

    @Nullable
    public static KeyModifier getModifier(InputConstants.Key key) {
        for (KeyModifier modifier : VALUES) {
            if (modifier.matches(key)) {
                return modifier;
            }
        }
        return null;
    }

    public static boolean isKeyCodeModifier(InputConstants.Key key) {
        for (KeyModifier keyModifier : VALUES) {
            if (keyModifier.matches(key)) {
                return true;
            }
        }
        return false;
    }

    public static KeyModifier valueFromString(String stringValue) {
        try {
            return valueOf(stringValue);
        } catch (IllegalArgumentException | NullPointerException var2) {
            return NONE;
        }
    }

    public abstract boolean matches(InputConstants.Key var1);

    public abstract boolean isActive(@Nullable IKeyConflictContext var1);

    public abstract Component getCombinedName(InputConstants.Key var1, Supplier<Component> var2);
}