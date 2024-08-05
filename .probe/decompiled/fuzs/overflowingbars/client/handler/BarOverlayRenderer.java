package fuzs.overflowingbars.client.handler;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public class BarOverlayRenderer {

    static final ResourceLocation GUI_ICONS_LOCATION = new ResourceLocation("textures/gui/icons.png");

    static final ResourceLocation OVERFLOWING_ICONS_LOCATION = new ResourceLocation("overflowingbars", "textures/gui/icons.png");

    public static void renderHealthLevelBars(GuiGraphics guiGraphics, int screenWidth, int screenHeight, Minecraft minecraft, int leftHeight, boolean rowCount) {
        Player player = getCameraPlayer(minecraft);
        if (player != null) {
            int posX = screenWidth / 2 - 91;
            int posY = screenHeight - leftHeight;
            HealthBarRenderer.INSTANCE.renderPlayerHealth(guiGraphics, posX, posY, player, minecraft.getProfiler());
            if (rowCount) {
                int allHearts = Mth.ceil(player.m_21223_());
                RowCountRenderer.drawBarRowCount(guiGraphics, posX - 2, posY, allHearts, true, minecraft.font);
                int maxAbsorption = (20 - Mth.ceil((float) Math.min(20, allHearts) / 2.0F)) * 2;
                RowCountRenderer.drawBarRowCount(guiGraphics, posX - 2, posY - 10, Mth.ceil(player.getAbsorptionAmount()), true, maxAbsorption, minecraft.font);
            }
        }
    }

    public static void renderArmorLevelBar(GuiGraphics guiGraphics, int screenWidth, int screenHeight, Minecraft minecraft, int leftHeight, boolean rowCount, boolean unmodified) {
        Player player = getCameraPlayer(minecraft);
        if (player != null) {
            int posX = screenWidth / 2 - 91;
            int posY = screenHeight - leftHeight;
            ArmorBarRenderer.renderArmorBar(guiGraphics, posX, posY, player, minecraft.getProfiler(), unmodified);
            if (rowCount && !unmodified) {
                RowCountRenderer.drawBarRowCount(guiGraphics, posX - 2, posY, player.m_21230_(), true, minecraft.font);
            }
        }
    }

    public static void renderToughnessLevelBar(GuiGraphics guiGraphics, int screenWidth, int screenHeight, Minecraft minecraft, int rightHeight, boolean rowCount, boolean left, boolean unmodified) {
        Player player = getCameraPlayer(minecraft);
        if (player != null) {
            int posX = screenWidth / 2 + (left ? -91 : 91);
            int posY = screenHeight - rightHeight;
            ArmorBarRenderer.renderToughnessBar(guiGraphics, posX, posY, player, minecraft.getProfiler(), left, unmodified);
            if (rowCount && !unmodified) {
                int toughnessValue = Mth.floor(player.m_21133_(Attributes.ARMOR_TOUGHNESS));
                RowCountRenderer.drawBarRowCount(guiGraphics, posX + (left ? -2 : 2), posY, toughnessValue, left, minecraft.font);
            }
        }
    }

    @Nullable
    private static Player getCameraPlayer(Minecraft minecraft) {
        return minecraft.getCameraEntity() instanceof Player player ? player : null;
    }

    public static void resetRenderState() {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.defaultBlendFunc();
    }
}