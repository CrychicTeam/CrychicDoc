package mezz.jei.library.plugins.vanilla;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import mezz.jei.common.Internal;
import mezz.jei.common.platform.IPlatformRenderHelper;
import mezz.jei.common.platform.IPlatformScreenHelper;
import mezz.jei.common.platform.Services;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.inventory.AbstractContainerMenu;

public final class InventoryEffectRendererGuiHandler<T extends AbstractContainerMenu> implements IGuiContainerHandler<EffectRenderingInventoryScreen<T>> {

    public List<Rect2i> getGuiExtraAreas(EffectRenderingInventoryScreen<T> containerScreen) {
        if (!Internal.getJeiFeatures().getInventoryEffectRendererGuiHandlerEnabled()) {
            return List.of();
        } else {
            Minecraft minecraft = Minecraft.getInstance();
            LocalPlayer player = minecraft.player;
            if (player == null) {
                return Collections.emptyList();
            } else {
                Collection<MobEffectInstance> activePotionEffects = player.m_21220_();
                if (activePotionEffects.isEmpty()) {
                    return Collections.emptyList();
                } else {
                    IPlatformScreenHelper screenHelper = Services.PLATFORM.getScreenHelper();
                    List<Rect2i> areas = new ArrayList();
                    int x = screenHelper.getGuiLeft(containerScreen) + screenHelper.getXSize(containerScreen) + 2;
                    int y = screenHelper.getGuiTop(containerScreen);
                    int width = 32;
                    int height = 33;
                    if (activePotionEffects.size() > 5) {
                        height = 132 / (activePotionEffects.size() - 1);
                    }
                    IPlatformRenderHelper renderHelper = Services.PLATFORM.getRenderHelper();
                    for (MobEffectInstance potionEffect : activePotionEffects) {
                        if (renderHelper.shouldRender(potionEffect)) {
                            areas.add(new Rect2i(x, y, width, height));
                            y += height;
                        }
                    }
                    return areas;
                }
            }
        }
    }
}