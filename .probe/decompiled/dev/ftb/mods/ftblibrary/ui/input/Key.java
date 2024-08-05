package dev.ftb.mods.ftblibrary.ui.input;

import com.mojang.blaze3d.platform.InputConstants;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import dev.ftb.mods.ftblibrary.ui.input.forge.KeyImpl;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;

public class Key {

    public final int keyCode;

    public final int scanCode;

    public final KeyModifiers modifiers;

    @ExpectPlatform
    @Transformed
    private static boolean matchesWithoutConflicts(KeyMapping keyBinding, InputConstants.Key keyCode) {
        return KeyImpl.matchesWithoutConflicts(keyBinding, keyCode);
    }

    public Key(int k, int s, int m) {
        this.keyCode = k;
        this.scanCode = s;
        this.modifiers = new KeyModifiers(m);
    }

    public boolean is(int k) {
        return this.keyCode == k;
    }

    public InputConstants.Key getInputMapping() {
        return InputConstants.getKey(this.keyCode, this.scanCode);
    }

    public boolean esc() {
        return this.is(256);
    }

    public boolean escOrInventory() {
        return this.esc() || matchesWithoutConflicts(Minecraft.getInstance().options.keyInventory, this.getInputMapping());
    }

    public boolean enter() {
        return this.is(257);
    }

    public boolean backspace() {
        return this.is(259);
    }

    public boolean cut() {
        return Screen.isCut(this.keyCode);
    }

    public boolean paste() {
        return Screen.isPaste(this.keyCode);
    }

    public boolean copy() {
        return Screen.isCopy(this.keyCode);
    }

    public boolean selectAll() {
        return Screen.isSelectAll(this.keyCode);
    }

    public boolean deselectAll() {
        return this.keyCode == 68 && Screen.hasControlDown() && !Screen.hasShiftDown() && !Screen.hasAltDown();
    }
}