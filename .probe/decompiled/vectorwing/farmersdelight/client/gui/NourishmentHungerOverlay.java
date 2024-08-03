package vectorwing.farmersdelight.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.level.GameRules;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.GuiOverlayManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.registry.ModEffects;

public class NourishmentHungerOverlay {

    public static int foodIconsOffset;

    private static final ResourceLocation MOD_ICONS_TEXTURE = new ResourceLocation("farmersdelight", "textures/gui/fd_icons.png");

    static ResourceLocation FOOD_LEVEL_ELEMENT = new ResourceLocation("minecraft", "food_level");

    public static void init() {
        MinecraftForge.EVENT_BUS.register(new NourishmentHungerOverlay());
    }

    @SubscribeEvent
    public void onRenderGuiOverlayPost(RenderGuiOverlayEvent.Post event) {
        if (event.getOverlay() == GuiOverlayManager.findOverlay(FOOD_LEVEL_ELEMENT)) {
            Minecraft mc = Minecraft.getInstance();
            ForgeGui gui = (ForgeGui) mc.gui;
            boolean isMounted = mc.player != null && mc.player.m_20202_() instanceof LivingEntity;
            if (!isMounted && !mc.options.hideGui && gui.shouldDrawSurvivalElements()) {
                renderNourishmentOverlay(gui, event.getGuiGraphics());
            }
        }
    }

    public static void renderNourishmentOverlay(ForgeGui gui, GuiGraphics graphics) {
        if (Configuration.NOURISHED_HUNGER_OVERLAY.get()) {
            foodIconsOffset = gui.rightHeight;
            Minecraft minecraft = Minecraft.getInstance();
            Player player = minecraft.player;
            if (player != null) {
                FoodData stats = player.getFoodData();
                int top = minecraft.getWindow().getGuiScaledHeight() - foodIconsOffset + 10;
                int left = minecraft.getWindow().getGuiScaledWidth() / 2 + 91;
                boolean isPlayerHealingWithSaturation = player.m_9236_().getGameRules().getBoolean(GameRules.RULE_NATURAL_REGENERATION) && player.isHurt() && stats.getFoodLevel() >= 18;
                if (player.m_21124_(ModEffects.NOURISHMENT.get()) != null) {
                    drawNourishmentOverlay(stats, minecraft, graphics, left, top, isPlayerHealingWithSaturation);
                }
            }
        }
    }

    public static void drawNourishmentOverlay(FoodData stats, Minecraft mc, GuiGraphics graphics, int left, int top, boolean naturalHealing) {
        float saturation = stats.getSaturationLevel();
        int foodLevel = stats.getFoodLevel();
        int ticks = mc.gui.getGuiTicks();
        Random rand = new Random();
        rand.setSeed((long) (ticks * 312871));
        RenderSystem.enableBlend();
        for (int j = 0; j < 10; j++) {
            int x = left - j * 8 - 9;
            int y = top;
            if (saturation <= 0.0F && ticks % (foodLevel * 3 + 1) == 0) {
                y = top + (rand.nextInt(3) - 1);
            }
            graphics.blit(MOD_ICONS_TEXTURE, x, y, 0, 0, 9, 9);
            float effectiveHungerOfBar = (float) stats.getFoodLevel() / 2.0F - (float) j;
            int naturalHealingOffset = naturalHealing ? 18 : 0;
            if (effectiveHungerOfBar >= 1.0F) {
                graphics.blit(MOD_ICONS_TEXTURE, x, y, 18 + naturalHealingOffset, 0, 9, 9);
            } else if ((double) effectiveHungerOfBar >= 0.5) {
                graphics.blit(MOD_ICONS_TEXTURE, x, y, 9 + naturalHealingOffset, 0, 9, 9);
            }
        }
        RenderSystem.disableBlend();
    }
}