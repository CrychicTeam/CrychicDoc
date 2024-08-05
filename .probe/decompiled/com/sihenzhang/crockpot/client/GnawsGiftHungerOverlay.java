package com.sihenzhang.crockpot.client;

import com.sihenzhang.crockpot.CrockPotConfigs;
import com.sihenzhang.crockpot.effect.CrockPotEffects;
import com.sihenzhang.crockpot.util.RLUtils;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodData;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.GuiOverlayManager;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(value = { Dist.CLIENT }, modid = "crockpot")
public class GnawsGiftHungerOverlay {

    private static final ResourceLocation GNAWS_GIFT_ICONS = RLUtils.createRL("textures/gui/gnaws_gift.png");

    private static final Random RAND = new Random();

    private static int hungerBarOffset;

    @SubscribeEvent
    public static void onClientSetupEvent(RenderGuiOverlayEvent.Pre event) {
        if (event.getOverlay() == GuiOverlayManager.findOverlay(VanillaGuiOverlay.FOOD_LEVEL.id())) {
            if (shouldRender()) {
                hungerBarOffset = ((ForgeGui) Minecraft.getInstance().gui).rightHeight;
            }
        }
    }

    @SubscribeEvent
    public static void onRenderGuiOverlayPost(RenderGuiOverlayEvent.Post event) {
        if (event.getOverlay() == GuiOverlayManager.findOverlay(VanillaGuiOverlay.FOOD_LEVEL.id())) {
            if (shouldRender()) {
                Minecraft mc = Minecraft.getInstance();
                GuiGraphics guiGraphics = event.getGuiGraphics();
                int left = mc.getWindow().getGuiScaledWidth() / 2 + 91;
                int top = mc.getWindow().getGuiScaledHeight() - hungerBarOffset;
                int tickCount = mc.gui.getGuiTicks();
                RAND.setSeed((long) tickCount * 312871L);
                FoodData foodData = mc.player.m_36324_();
                int foodLevel = foodData.getFoodLevel();
                for (int i = 0; i < 10; i++) {
                    int idx = i * 2 + 1;
                    int x = left - i * 8 - 9;
                    int y = top;
                    if (foodData.getSaturationLevel() <= 0.0F && tickCount % (foodLevel * 3 + 1) == 0) {
                        y = top + (RAND.nextInt(3) - 1);
                    }
                    guiGraphics.blit(GNAWS_GIFT_ICONS, x, y, 0, 0, 9, 9);
                    if (idx < foodLevel) {
                        guiGraphics.blit(GNAWS_GIFT_ICONS, x, y, 9, 0, 9, 9);
                    } else if (idx == foodLevel) {
                        guiGraphics.blit(GNAWS_GIFT_ICONS, x, y, 18, 0, 9, 9);
                    }
                }
            }
        }
    }

    private static boolean shouldRender() {
        if (!CrockPotConfigs.GNAWS_GIFT_HUNGER_OVERLAY.get()) {
            return false;
        } else {
            Minecraft mc = Minecraft.getInstance();
            LocalPlayer player = mc.player;
            if (player == null) {
                return false;
            } else if (!player.m_21023_(CrockPotEffects.GNAWS_GIFT.get())) {
                return false;
            } else {
                ForgeGui gui = (ForgeGui) mc.gui;
                boolean isMounted = player.m_20202_() instanceof LivingEntity;
                return !isMounted && !mc.options.hideGui && gui.shouldDrawSurvivalElements();
            }
        }
    }
}