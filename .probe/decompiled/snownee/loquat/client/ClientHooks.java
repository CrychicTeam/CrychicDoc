package snownee.loquat.client;

import java.util.List;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.StringUtils;
import snownee.loquat.core.select.SelectionManager;
import snownee.loquat.network.CRequestOutlinesPacket;
import snownee.loquat.network.CSelectAreaPacket;

public interface ClientHooks {

    static boolean handleComponentClicked(String value) {
        String[] s = StringUtils.split(value, " ", 5);
        if (s.length == 5 && s[0].equals("@loquat")) {
            LocalPlayer player = Minecraft.getInstance().player;
            ResourceLocation dimension = ResourceLocation.tryParse(s[2]);
            if (player != null && dimension != null) {
                UUID uuid = UUID.fromString(s[3]);
                String var5 = s[1];
                switch(var5) {
                    case "highlight":
                        if (!player.m_9236_().dimension().location().equals(dimension)) {
                            player.displayClientMessage(Component.translatable("loquat.command.wrongDimension"), false);
                            return true;
                        }
                        CRequestOutlinesPacket.request(60L, List.of(uuid));
                        break;
                    case "info":
                        if (Screen.hasControlDown()) {
                            Minecraft.getInstance().keyboardHandler.setClipboard(s[4]);
                            Minecraft.getInstance().getChatListener().handleSystemMessage(Component.translatable("loquat.msg.copied"), false);
                        } else {
                            Minecraft.getInstance().keyboardHandler.setClipboard(uuid.toString());
                            Minecraft.getInstance().getChatListener().handleSystemMessage(Component.translatable("loquat.msg.copied.uuid"), false);
                        }
                        break;
                    case "select":
                        SelectionManager manager = SelectionManager.of(player);
                        CSelectAreaPacket.send(!manager.getSelectedAreas().contains(uuid), uuid);
                }
                return true;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    static Level getLevel() {
        return Minecraft.getInstance().level;
    }

    static Player getPlayer() {
        return Minecraft.getInstance().player;
    }
}