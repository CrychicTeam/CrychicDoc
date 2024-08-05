package dev.shadowsoffire.attributeslib.client;

import dev.shadowsoffire.attributeslib.ALConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import top.theillusivec4.curios.client.gui.CuriosScreen;

public class CuriosClientCompat {

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void addAttribComponent(ScreenEvent.Init.Post e) {
        if (ALConfig.enableAttributesGui && e.getScreen() instanceof CuriosScreen scn) {
            ImageButton button = new ImageButton(scn.getGuiLeft() + 63, scn.getGuiTop() + 10, 10, 10, 131, 0, 10, AttributesGui.TEXTURES, 256, 256, btn -> {
                if (Minecraft.getInstance().player != null) {
                    InventoryScreen invScn = new InventoryScreen(Minecraft.getInstance().player);
                    AttributesGui.swappedFromCurios = true;
                    scn.getMinecraft().setScreen(invScn);
                    btn.m_93692_(false);
                }
            }, Component.translatable("attributeslib.gui.show_attributes")) {

                @Override
                public void setFocused(boolean pFocused) {
                }
            };
            e.addListener(button);
        }
    }
}