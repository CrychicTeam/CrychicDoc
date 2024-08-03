package fuzs.overflowingbars.client.helper;

import fuzs.overflowingbars.OverflowingBars;
import fuzs.overflowingbars.config.ClientConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

public class ChatOffsetHelper {

    public static int getChatOffsetY() {
        int offset = 0;
        Minecraft minecraft = Minecraft.getInstance();
        if (twoHealthRows(minecraft.player)) {
            offset++;
        }
        if (armorRow(minecraft.player)) {
            offset++;
        }
        if (toughnessRow(minecraft.player) && (OverflowingBars.CONFIG.get(ClientConfig.class).toughness.leftSide || offset == 0)) {
            offset++;
        }
        return offset * 10;
    }

    public static boolean twoHealthRows(Player player) {
        return player.getAbsorptionAmount() > 0.0F && player.m_21233_() + player.getAbsorptionAmount() > 20.0F;
    }

    public static boolean armorRow(Player player) {
        return player.m_21230_() > 0;
    }

    public static boolean toughnessRow(Player player) {
        return OverflowingBars.CONFIG.get(ClientConfig.class).toughness.armorToughnessBar && Mth.floor(player.m_21133_(Attributes.ARMOR_TOUGHNESS)) > 0;
    }
}