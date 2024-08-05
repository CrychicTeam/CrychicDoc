package dev.xkmc.l2weaponry.content.client;

import dev.xkmc.l2complements.init.data.LCConfig;
import dev.xkmc.l2library.util.Proxy;
import dev.xkmc.l2weaponry.content.capability.LWPlayerData;
import dev.xkmc.l2weaponry.content.item.base.BaseShieldItem;
import dev.xkmc.l2weaponry.init.registrate.LWItems;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.IItemDecorator;

public class ShieldItemDecorationRenderer implements IItemDecorator {

    private static final int COLOR_REFLECT = -16711681;

    private static final int COLOR_USING = -32897;

    @Override
    public boolean render(GuiGraphics g, Font font, ItemStack stack, int x, int y) {
        ItemStack main = Proxy.getClientPlayer().m_21205_();
        if (main != stack) {
            if (Proxy.getClientPlayer().m_21206_() != stack) {
                return false;
            }
            if (main.getItem() instanceof BaseShieldItem) {
                return false;
            }
        }
        if (!(stack.getItem() instanceof BaseShieldItem)) {
            return false;
        } else {
            g.pose().pushPose();
            int height = LCConfig.CLIENT.enchOverlayZVal.get();
            g.pose().translate(0.0F, 0.0F, (float) height);
            LWPlayerData cap = LWPlayerData.HOLDER.get(Proxy.getClientPlayer());
            float defenseLost = (float) cap.getShieldDefense();
            float w = 13.0F * (1.0F - defenseLost);
            boolean using = cap.getRecoverRate() < 0.01;
            int color = cap.canReflect() && defenseLost == 0.0F ? -16711681 : (using ? -32897 : -1);
            CommonDecoUtil.fillRect(g, (float) (x + 2), (float) (y + 14), w, 1.0F, color);
            CommonDecoUtil.fillRect(g, (float) (x + 2) + w, (float) (y + 14), 13.0F - w, 1.0F, -16777216);
            int reflectTimer = cap.getReflectTimer();
            if (cap.canReflect() && reflectTimer > 0) {
                int max = (int) cap.player.m_21133_((Attribute) LWItems.REFLECT_TIME.get());
                w = 13.0F * (float) reflectTimer / (float) max;
                CommonDecoUtil.fillRect(g, (float) (x + 2), (float) (y + 14), w, 1.0F, -16711681);
            }
            g.pose().popPose();
            return true;
        }
    }
}