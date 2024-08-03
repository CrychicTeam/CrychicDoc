package net.blay09.mods.balm.api.client.keymappings;

import com.mojang.blaze3d.platform.InputConstants;
import java.util.Optional;
import net.minecraft.client.KeyMapping;

public interface BalmKeyMappings {

    KeyMapping registerKeyMapping(String var1, int var2, String var3);

    KeyMapping registerKeyMapping(String var1, InputConstants.Type var2, int var3, String var4);

    KeyMapping registerKeyMapping(String var1, KeyConflictContext var2, KeyModifier var3, int var4, String var5);

    KeyMapping registerKeyMapping(String var1, KeyConflictContext var2, KeyModifiers var3, int var4, String var5);

    KeyMapping registerKeyMapping(String var1, KeyConflictContext var2, KeyModifier var3, InputConstants.Type var4, int var5, String var6);

    KeyMapping registerKeyMapping(String var1, KeyConflictContext var2, KeyModifiers var3, InputConstants.Type var4, int var5, String var6);

    default boolean isActiveAndMatches(KeyMapping keyMapping, int keyCode, int scanCode) {
        return this.isActiveAndMatches(keyMapping, InputConstants.getKey(keyCode, scanCode));
    }

    default boolean isActiveAndMatches(KeyMapping keyMapping, InputConstants.Type type, int keyCode, int scanCode) {
        return this.isActiveAndMatches(keyMapping, type.getOrCreate(type == InputConstants.Type.SCANCODE ? scanCode : keyCode));
    }

    boolean isActiveAndMatches(KeyMapping var1, InputConstants.Key var2);

    boolean isActiveAndWasPressed(KeyMapping var1);

    boolean isKeyDownIgnoreContext(KeyMapping var1);

    boolean isActiveAndKeyDown(KeyMapping var1);

    Optional<Boolean> conflictsWith(KeyMapping var1, KeyMapping var2);

    void ignoreConflicts(KeyMapping var1);

    boolean shouldIgnoreConflicts(KeyMapping var1);
}