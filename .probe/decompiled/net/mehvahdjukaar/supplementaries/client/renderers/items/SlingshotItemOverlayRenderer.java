package net.mehvahdjukaar.supplementaries.client.renderers.items;

import net.mehvahdjukaar.moonlight.api.item.IItemDecoratorRenderer;
import net.mehvahdjukaar.supplementaries.configs.ClientConfigs;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

public class SlingshotItemOverlayRenderer implements IItemDecoratorRenderer {

    @Override
    public boolean render(GuiGraphics graphics, Font font, ItemStack stack, int x, int y) {
        boolean overlay = (Boolean) ClientConfigs.Items.SLINGSHOT_OVERLAY.get();
        boolean outline = (Boolean) ClientConfigs.Items.SLINGSHOT_OUTLINE.get();
        if (!overlay && !outline) {
            return false;
        } else {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player != null && (player.m_21205_() == stack || player.m_21206_() == stack)) {
                if (overlay) {
                    ItemStack ammo = SlingshotRendererHelper.getAmmoForPreview(stack, Minecraft.getInstance().level, player);
                    QuiverItemOverlayRenderer.renderAmmo(graphics, x, y, ammo);
                }
                if (outline && EnchantmentHelper.getItemEnchantmentLevel((Enchantment) ModRegistry.STASIS_ENCHANTMENT.get(), stack) != 0) {
                    SlingshotRendererHelper.grabNewLookPos(player);
                }
            }
            return true;
        }
    }
}