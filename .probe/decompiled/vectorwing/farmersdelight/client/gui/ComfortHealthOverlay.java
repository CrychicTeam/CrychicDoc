package vectorwing.farmersdelight.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.GuiOverlayManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.registry.ModEffects;

public class ComfortHealthOverlay {

    protected static int healthIconsOffset;

    private static final ResourceLocation MOD_ICONS_TEXTURE = new ResourceLocation("farmersdelight", "textures/gui/fd_icons.png");

    static ResourceLocation PLAYER_HEALTH_ELEMENT = new ResourceLocation("minecraft", "player_health");

    public static void init() {
        MinecraftForge.EVENT_BUS.register(new ComfortHealthOverlay());
    }

    @SubscribeEvent
    public void onRenderGuiOverlayPost(RenderGuiOverlayEvent.Post event) {
        if (event.getOverlay() == GuiOverlayManager.findOverlay(PLAYER_HEALTH_ELEMENT)) {
            Minecraft mc = Minecraft.getInstance();
            ForgeGui gui = (ForgeGui) mc.gui;
            if (!mc.options.hideGui && gui.shouldDrawSurvivalElements()) {
                renderComfortOverlay(gui, event.getGuiGraphics());
            }
        }
    }

    public static void renderComfortOverlay(ForgeGui gui, GuiGraphics graphics) {
        if (Configuration.COMFORT_HEALTH_OVERLAY.get()) {
            healthIconsOffset = gui.leftHeight;
            Minecraft minecraft = Minecraft.getInstance();
            Player player = minecraft.player;
            if (player != null) {
                FoodData stats = player.getFoodData();
                int top = minecraft.getWindow().getGuiScaledHeight() - healthIconsOffset + 10;
                int left = minecraft.getWindow().getGuiScaledWidth() / 2 - 91;
                boolean isPlayerEligibleForComfort = stats.getSaturationLevel() == 0.0F && player.isHurt() && !player.m_21023_(MobEffects.REGENERATION);
                if (player.m_21124_(ModEffects.COMFORT.get()) != null && isPlayerEligibleForComfort) {
                    drawComfortOverlay(player, minecraft, graphics, left, top);
                }
            }
        }
    }

    public static void drawComfortOverlay(Player player, Minecraft minecraft, GuiGraphics graphics, int left, int top) {
        int ticks = minecraft.gui.getGuiTicks();
        Random rand = new Random();
        rand.setSeed((long) (ticks * 312871));
        int health = Mth.ceil(player.m_21223_());
        float absorb = (float) Mth.ceil(player.getAbsorptionAmount());
        AttributeInstance attrMaxHealth = player.m_21051_(Attributes.MAX_HEALTH);
        float healthMax = (float) attrMaxHealth.getValue();
        int regen = -1;
        if (player.m_21023_(MobEffects.REGENERATION)) {
            regen = ticks % 25;
        }
        int healthRows = Mth.ceil((healthMax + absorb) / 2.0F / 10.0F);
        int rowHeight = Math.max(10 - (healthRows - 2), 3);
        int comfortSheen = ticks % 50;
        int comfortHeartFrame = comfortSheen % 2;
        int[] textureWidth = new int[] { 5, 9 };
        RenderSystem.setShaderTexture(0, MOD_ICONS_TEXTURE);
        RenderSystem.enableBlend();
        int healthMaxSingleRow = Mth.ceil(Math.min(healthMax, 20.0F) / 2.0F);
        int leftHeightOffset = (healthRows - 1) * rowHeight;
        for (int i = 0; i < healthMaxSingleRow; i++) {
            int column = i % 10;
            int x = left + column * 8;
            int y = top + leftHeightOffset;
            if (health <= 4) {
                y += rand.nextInt(2);
            }
            if (i == regen) {
                y -= 2;
            }
            if (column == comfortSheen / 2) {
                graphics.blit(MOD_ICONS_TEXTURE, x, y, 0, 9, textureWidth[comfortHeartFrame], 9);
            }
            if (column == comfortSheen / 2 - 1 && comfortHeartFrame == 0) {
                graphics.blit(MOD_ICONS_TEXTURE, x + 5, y, 5, 9, 4, 9);
            }
        }
        RenderSystem.disableBlend();
    }
}