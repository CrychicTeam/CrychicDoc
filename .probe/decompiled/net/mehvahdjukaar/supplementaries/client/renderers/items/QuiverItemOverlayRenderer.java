package net.mehvahdjukaar.supplementaries.client.renderers.items;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.mehvahdjukaar.moonlight.api.item.IItemDecoratorRenderer;
import net.mehvahdjukaar.supplementaries.common.items.QuiverItem;
import net.mehvahdjukaar.supplementaries.configs.ClientConfigs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;

public class QuiverItemOverlayRenderer implements IItemDecoratorRenderer {

    @Override
    public boolean render(GuiGraphics graphics, Font font, ItemStack stack, int x, int y) {
        boolean overlay = (Boolean) ClientConfigs.Items.QUIVER_OVERLAY.get();
        if (overlay) {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player != null) {
                ItemStack ammo = QuiverItem.getQuiverData(stack).getSelected();
                renderAmmo(graphics, x, y, ammo);
            }
            return true;
        } else {
            return false;
        }
    }

    public static void renderAmmo(GuiGraphics graphics, int x, int y, ItemStack ammo) {
        if (!ammo.isEmpty()) {
            PoseStack poseStack = graphics.pose();
            poseStack.pushPose();
            float xOff = 22.0F;
            float yOff = 8.0F;
            poseStack.translate(-4.0 + (double) ((xOff + (float) x) * 0.6F), 4.4 + (double) ((yOff + (float) y) * 0.6F), 136.0);
            poseStack.scale(0.4F, 0.4F, 0.4F);
            RenderSystem.applyModelViewMatrix();
            graphics.renderFakeItem(ammo, x, y);
            poseStack.popPose();
            RenderSystem.applyModelViewMatrix();
        }
    }
}