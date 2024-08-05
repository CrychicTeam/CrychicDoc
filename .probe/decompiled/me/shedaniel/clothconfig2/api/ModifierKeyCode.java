package me.shedaniel.clothconfig2.api;

import com.mojang.blaze3d.platform.InputConstants;
import me.shedaniel.clothconfig2.impl.ModifierKeyCodeImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.glfw.GLFW;

@OnlyIn(Dist.CLIENT)
public interface ModifierKeyCode {

    static ModifierKeyCode of(InputConstants.Key keyCode, Modifier modifier) {
        return new ModifierKeyCodeImpl().setKeyCodeAndModifier(keyCode, modifier);
    }

    static ModifierKeyCode copyOf(ModifierKeyCode code) {
        return of(code.getKeyCode(), code.getModifier());
    }

    static ModifierKeyCode unknown() {
        return of(InputConstants.UNKNOWN, Modifier.none());
    }

    InputConstants.Key getKeyCode();

    ModifierKeyCode setKeyCode(InputConstants.Key var1);

    default InputConstants.Type getType() {
        return this.getKeyCode().getType();
    }

    Modifier getModifier();

    ModifierKeyCode setModifier(Modifier var1);

    default ModifierKeyCode copy() {
        return copyOf(this);
    }

    default boolean matchesMouse(int button) {
        return !this.isUnknown() && this.getType() == InputConstants.Type.MOUSE && this.getKeyCode().getValue() == button && this.getModifier().matchesCurrent();
    }

    default boolean matchesKey(int keyCode, int scanCode) {
        if (this.isUnknown()) {
            return false;
        } else {
            return keyCode == InputConstants.UNKNOWN.getValue() ? this.getType() == InputConstants.Type.SCANCODE && this.getKeyCode().getValue() == scanCode && this.getModifier().matchesCurrent() : this.getType() == InputConstants.Type.KEYSYM && this.getKeyCode().getValue() == keyCode && this.getModifier().matchesCurrent();
        }
    }

    default boolean matchesCurrentMouse() {
        return !this.isUnknown() && this.getType() == InputConstants.Type.MOUSE && this.getModifier().matchesCurrent() ? GLFW.glfwGetMouseButton(Minecraft.getInstance().getWindow().getWindow(), this.getKeyCode().getValue()) == 1 : false;
    }

    default boolean matchesCurrentKey() {
        return !this.isUnknown() && this.getType() == InputConstants.Type.KEYSYM && this.getModifier().matchesCurrent() && InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), this.getKeyCode().getValue());
    }

    default ModifierKeyCode setKeyCodeAndModifier(InputConstants.Key keyCode, Modifier modifier) {
        this.setKeyCode(keyCode);
        this.setModifier(modifier);
        return this;
    }

    default ModifierKeyCode clearModifier() {
        return this.setModifier(Modifier.none());
    }

    String toString();

    Component getLocalizedName();

    default boolean isUnknown() {
        return this.getKeyCode().equals(InputConstants.UNKNOWN);
    }
}