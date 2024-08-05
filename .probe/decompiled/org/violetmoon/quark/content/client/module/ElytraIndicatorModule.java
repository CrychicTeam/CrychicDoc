package org.violetmoon.quark.content.client.module;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.violetmoon.quark.base.client.handler.ClientUtil;
import org.violetmoon.zeta.client.event.play.ZRenderGuiOverlay;
import org.violetmoon.zeta.event.bus.PlayEvent;
import org.violetmoon.zeta.module.ZetaLoadModule;
import org.violetmoon.zeta.module.ZetaModule;

@ZetaLoadModule(category = "client")
public class ElytraIndicatorModule extends ZetaModule {

    public int getArmorLimit(int curr) {
        return curr;
    }

    @ZetaLoadModule(clientReplacement = true)
    public static class Client extends ElytraIndicatorModule {

        private int shift = 0;

        @PlayEvent
        public void hudPre(ZRenderGuiOverlay.ArmorLevel.Pre event) {
            if (event.shouldDrawSurvivalElements()) {
                Minecraft mc = Minecraft.getInstance();
                Player player = mc.player;
                ItemStack itemstack = player.getItemBySlot(EquipmentSlot.CHEST);
                if (this.zeta.itemExtensions.get(itemstack).canElytraFlyZeta(itemstack, player)) {
                    int armor = player.m_21230_();
                    this.shift = armor >= 20 ? 0 : 9;
                    GuiGraphics guiGraphics = event.getGuiGraphics();
                    PoseStack pose = guiGraphics.pose();
                    Window window = event.getWindow();
                    pose.translate((float) this.shift, 0.0F, 0.0F);
                    pose.pushPose();
                    pose.translate(0.0F, 0.0F, 100.0F);
                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                    int x = window.getGuiScaledWidth() / 2 - 100;
                    int y = window.getGuiScaledHeight() - event.getLeftHeight();
                    guiGraphics.blit(ClientUtil.GENERAL_ICONS, x, y, 184.0F, 35.0F, 9, 9, 256, 256);
                    pose.popPose();
                }
            }
        }

        @PlayEvent
        public void hudPost(ZRenderGuiOverlay.ArmorLevel.Post event) {
            if (this.shift != 0) {
                event.getGuiGraphics().pose().translate((float) (-this.shift), 0.0F, 0.0F);
                this.shift = 0;
            }
        }

        @Override
        public int getArmorLimit(int curr) {
            return !this.enabled ? curr : 20 - this.shift / 9 * 2;
        }
    }
}