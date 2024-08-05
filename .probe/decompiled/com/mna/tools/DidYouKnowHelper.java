package com.mna.tools;

import com.mna.config.ClientConfig;
import com.mna.network.ServerMessageDispatcher;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class DidYouKnowHelper {

    public static void CheckAndShowDidYouKnow(Player to, String id) {
        if (to instanceof ServerPlayer serverPlayer) {
            ServerMessageDispatcher.sendDidYouKnow(serverPlayer, id);
        } else if (!HasTipBeenShown(id)) {
            to.m_213846_(Component.translatable(id).withStyle(ChatFormatting.GOLD));
            ClientConfig.SetDidYouKnowTipShown(id);
        }
    }

    @OnlyIn(Dist.CLIENT)
    private static boolean HasTipBeenShown(String id) {
        return ClientConfig.DidYouKnowTipsShown == null ? true : ClientConfig.DidYouKnowTipsShown.contains(id);
    }
}