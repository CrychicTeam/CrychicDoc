package noppes.npcs.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;

public class VersionChecker extends Thread {

    public void run() {
        String name = "§2CustomNpcs§f";
        String link = "§9§nClick here";
        String text = name + " installed. For more info " + link;
        try {
            Player player = Minecraft.getInstance().player;
        } catch (NoSuchMethodError var7) {
            return;
        }
        LocalPlayer var8;
        while ((var8 = Minecraft.getInstance().player) == null) {
            try {
                Thread.sleep(2000L);
            } catch (InterruptedException var6) {
                var6.printStackTrace();
            }
        }
        MutableComponent message = Component.translatable(text);
        message.setStyle(message.getStyle().withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "http://www.kodevelopment.nl/minecraft/customnpcs/")));
        var8.m_213846_(message);
    }
}