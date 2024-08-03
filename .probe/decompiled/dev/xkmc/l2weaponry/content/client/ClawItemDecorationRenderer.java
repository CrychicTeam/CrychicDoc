package dev.xkmc.l2weaponry.content.client;

import dev.xkmc.l2complements.init.data.LCConfig;
import dev.xkmc.l2library.util.Proxy;
import dev.xkmc.l2weaponry.content.item.base.BaseClawItem;
import dev.xkmc.l2weaponry.init.data.LWConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.IItemDecorator;

public class ClawItemDecorationRenderer implements IItemDecorator {

    @Override
    public boolean render(GuiGraphics g, Font font, ItemStack stack, int x, int y) {
        LocalPlayer player = Proxy.getClientPlayer();
        if (player == null) {
            return false;
        } else {
            ItemStack main = player.m_21205_();
            ItemStack off = player.m_21206_();
            if (main != stack && off != stack) {
                return false;
            } else if (main != stack && off.getItem() != main.getItem()) {
                return false;
            } else if (!(stack.getItem() instanceof BaseClawItem)) {
                return false;
            } else if (main.getItem() instanceof BaseClawItem claw) {
                long var22 = BaseClawItem.getLastTime(main);
                int timeout = LWConfig.COMMON.claw_timeout.get();
                float time = (float) (player.m_9236_().getGameTime() - var22) + Minecraft.getInstance().getPartialTick();
                if (time > (float) timeout) {
                    return false;
                } else {
                    g.pose().pushPose();
                    int height = LCConfig.CLIENT.enchOverlayZVal.get();
                    g.pose().translate(0.0F, 0.0F, (float) height);
                    float defenseLost = Mth.clamp(time, 0.0F, (float) timeout) / (float) timeout;
                    float w = 13.0F * (1.0F - defenseLost);
                    int col = -1;
                    if (time <= claw.getBlockTime(player)) {
                        col = -16711681;
                    }
                    CommonDecoUtil.fillRect(g, (float) (x + 2), (float) (y + 14), w, 1.0F, col);
                    CommonDecoUtil.fillRect(g, (float) (x + 2) + w, (float) (y + 14), 13.0F - w, 1.0F, -16777216);
                    int stored_hit = BaseClawItem.getHitCount(main);
                    int max_hit = claw.getMaxStack(main, Proxy.getClientPlayer());
                    int hit = Math.min(max_hit, stored_hit);
                    String s = hit + "";
                    col = hit < max_hit ? 16744447 : 16744319;
                    g.drawString(font, s, x + 17 - font.width(s), y + 9, col);
                    g.pose().popPose();
                    return true;
                }
            } else {
                return false;
            }
        }
    }
}