package net.blay09.mods.balm.common.client.keymappings;

import com.google.common.collect.Sets;
import com.mojang.blaze3d.platform.InputConstants;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import net.blay09.mods.balm.api.client.keymappings.BalmKeyMappings;
import net.blay09.mods.balm.api.client.keymappings.KeyConflictContext;
import net.blay09.mods.balm.api.client.keymappings.KeyModifier;
import net.blay09.mods.balm.api.client.keymappings.KeyModifiers;
import net.blay09.mods.balm.mixin.KeyMappingAccessor;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

public abstract class CommonBalmKeyMappings implements BalmKeyMappings {

    private final Set<KeyMapping> ignoreConflicts = Sets.newConcurrentHashSet();

    private final Map<KeyMapping, Set<KeyMapping>> multiModifierKeyMappings = new ConcurrentHashMap();

    @Override
    public KeyMapping registerKeyMapping(String name, int keyCode, String category) {
        return this.registerKeyMapping(name, KeyConflictContext.UNIVERSAL, KeyModifier.NONE, InputConstants.Type.KEYSYM, keyCode, category);
    }

    @Override
    public KeyMapping registerKeyMapping(String name, InputConstants.Type type, int keyCode, String category) {
        return this.registerKeyMapping(name, KeyConflictContext.UNIVERSAL, KeyModifier.NONE, type, keyCode, category);
    }

    @Override
    public KeyMapping registerKeyMapping(String name, KeyConflictContext conflictContext, KeyModifier modifier, int keyCode, String category) {
        return this.registerKeyMapping(name, conflictContext, modifier, InputConstants.Type.KEYSYM, keyCode, category);
    }

    @Override
    public KeyMapping registerKeyMapping(String name, KeyConflictContext conflictContext, KeyModifiers modifiers, int keyCode, String category) {
        return this.registerKeyMapping(name, conflictContext, modifiers, InputConstants.Type.KEYSYM, keyCode, category);
    }

    protected void registerModifierKeyMappings(KeyMapping baseMapping, KeyConflictContext conflictContext, List<KeyModifier> keyModifiers) {
        for (int i = 0; i < keyModifiers.size(); i++) {
            String subName = i > 0 ? baseMapping.getName() + "_modifier_" + i : baseMapping.getName() + "_modifier";
            KeyMapping subKeyMapping = this.registerKeyMapping(subName, conflictContext, KeyModifier.NONE, InputConstants.Type.KEYSYM, this.toKeyCode((KeyModifier) keyModifiers.get(i)), baseMapping.getCategory());
            ((Set) this.multiModifierKeyMappings.computeIfAbsent(baseMapping, it -> new HashSet())).add(subKeyMapping);
            this.ignoreConflicts.add(subKeyMapping);
        }
    }

    protected void registerCustomModifierKeyMappings(KeyMapping baseMapping, KeyConflictContext conflictContext, List<InputConstants.Key> keyModifiers) {
        for (int i = 0; i < keyModifiers.size(); i++) {
            String subName = i > 0 ? baseMapping.getName() + "_modifier_" + i : baseMapping.getName() + "_modifier";
            KeyMapping subKeyMapping = this.registerKeyMapping(subName, conflictContext, KeyModifier.NONE, InputConstants.Type.KEYSYM, ((InputConstants.Key) keyModifiers.get(i)).getValue(), baseMapping.getCategory());
            ((Set) this.multiModifierKeyMappings.computeIfAbsent(baseMapping, it -> new HashSet())).add(subKeyMapping);
            this.ignoreConflicts.add(subKeyMapping);
        }
    }

    private int toKeyCode(KeyModifier keyModifier) {
        return switch(keyModifier) {
            case SHIFT ->
                340;
            case CONTROL ->
                341;
            case ALT ->
                342;
            default ->
                -1;
        };
    }

    protected boolean areModifiersActive(KeyMapping keyMapping) {
        for (KeyMapping modifierMapping : (Set) this.multiModifierKeyMappings.getOrDefault(keyMapping, Collections.emptySet())) {
            if ((!modifierMapping.matches(340, 0) && !modifierMapping.matches(344, 0) || !Screen.hasShiftDown()) && (!modifierMapping.matches(341, 0) && !modifierMapping.matches(345, 0) || !Screen.hasControlDown()) && (!modifierMapping.matches(342, 0) && !modifierMapping.matches(346, 0) || !Screen.hasAltDown()) && !this.isActiveAndKeyDown(modifierMapping)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isActiveAndKeyDown(@Nullable KeyMapping keyMapping) {
        if (!this.isActive(keyMapping)) {
            return false;
        } else {
            InputConstants.Key key = ((KeyMappingAccessor) keyMapping).getKey();
            return keyMapping.isDown() || key.getValue() != -1 && key.getType() == InputConstants.Type.KEYSYM && InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), key.getValue());
        }
    }

    @Override
    public boolean isKeyDownIgnoreContext(@Nullable KeyMapping keyMapping) {
        if (!this.isActiveIgnoreContext(keyMapping)) {
            return false;
        } else {
            InputConstants.Key key = ((KeyMappingAccessor) keyMapping).getKey();
            return keyMapping.isDown() || key.getValue() != -1 && key.getType() == InputConstants.Type.KEYSYM && InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), key.getValue());
        }
    }

    @Override
    public boolean isActiveAndWasPressed(@Nullable KeyMapping keyMapping) {
        return this.isActive(keyMapping) && keyMapping.consumeClick();
    }

    @Contract("null -> false")
    protected boolean isActive(@Nullable KeyMapping keyMapping) {
        return keyMapping == null ? false : this.isContextActive(keyMapping) && this.areModifiersActive(keyMapping);
    }

    @Contract("null -> false")
    protected boolean isActiveIgnoreContext(@Nullable KeyMapping keyMapping) {
        return keyMapping == null ? false : this.areModifiersActive(keyMapping);
    }

    @Override
    public boolean isActiveAndMatches(@Nullable KeyMapping keyMapping, InputConstants.Key input) {
        if (!this.isActive(keyMapping)) {
            return false;
        } else {
            return input.getType() == InputConstants.Type.MOUSE ? keyMapping.matchesMouse(input.getValue()) : keyMapping.matches(input.getType() == InputConstants.Type.KEYSYM ? input.getValue() : InputConstants.UNKNOWN.getValue(), input.getType() == InputConstants.Type.SCANCODE ? input.getValue() : InputConstants.UNKNOWN.getValue());
        }
    }

    @Override
    public boolean isActiveAndMatches(@Nullable KeyMapping keyMapping, int keyCode, int scanCode) {
        return this.isActive(keyMapping) && keyMapping.matches(keyCode, scanCode);
    }

    @Override
    public boolean isActiveAndMatches(@Nullable KeyMapping keyMapping, InputConstants.Type type, int keyCode, int scanCode) {
        return this.isActive(keyMapping) && (type == InputConstants.Type.MOUSE ? keyMapping.matchesMouse(keyCode) : keyMapping.matches(keyCode, scanCode));
    }

    @Override
    public Optional<Boolean> conflictsWith(KeyMapping first, KeyMapping second) {
        return !this.ignoreConflicts.contains(first) && !this.ignoreConflicts.contains(second) ? Optional.empty() : Optional.of(false);
    }

    @Override
    public void ignoreConflicts(KeyMapping keyMapping) {
        this.ignoreConflicts.add(keyMapping);
        this.ignoreConflicts.addAll((Collection) this.multiModifierKeyMappings.getOrDefault(keyMapping, Collections.emptySet()));
    }

    @Override
    public boolean shouldIgnoreConflicts(KeyMapping keyMapping) {
        return this.ignoreConflicts.contains(keyMapping);
    }

    protected abstract boolean isContextActive(KeyMapping var1);

    protected boolean isContextActive(KeyConflictContext conflictContext) {
        return switch(conflictContext) {
            case GUI ->
                Minecraft.getInstance().screen != null;
            case INGAME ->
                Minecraft.getInstance().screen == null;
            default ->
                true;
        };
    }
}