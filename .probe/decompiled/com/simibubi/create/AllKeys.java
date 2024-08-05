package com.simibubi.create;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(value = { Dist.CLIENT }, bus = Bus.MOD)
public enum AllKeys {

    TOOL_MENU("toolmenu", 342), ACTIVATE_TOOL("", 341), TOOLBELT("toolbelt", 342);

    private KeyMapping keybind;

    private String description;

    private int key;

    private boolean modifiable;

    private AllKeys(String description, int defaultKey) {
        this.description = "create.keyinfo." + description;
        this.key = defaultKey;
        this.modifiable = !description.isEmpty();
    }

    @SubscribeEvent
    public static void register(RegisterKeyMappingsEvent event) {
        for (AllKeys key : values()) {
            key.keybind = new KeyMapping(key.description, key.key, "Create");
            if (key.modifiable) {
                event.register(key.keybind);
            }
        }
    }

    public KeyMapping getKeybind() {
        return this.keybind;
    }

    public boolean isPressed() {
        return !this.modifiable ? isKeyDown(this.key) : this.keybind.isDown();
    }

    public String getBoundKey() {
        return this.keybind.getTranslatedKeyMessage().getString().toUpperCase();
    }

    public int getBoundCode() {
        return this.keybind.getKey().getValue();
    }

    public static boolean isKeyDown(int key) {
        return InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), key);
    }

    public static boolean isMouseButtonDown(int button) {
        return GLFW.glfwGetMouseButton(Minecraft.getInstance().getWindow().getWindow(), button) == 1;
    }

    public static boolean ctrlDown() {
        return Screen.hasControlDown();
    }

    public static boolean shiftDown() {
        return Screen.hasShiftDown();
    }

    public static boolean altDown() {
        return Screen.hasAltDown();
    }
}