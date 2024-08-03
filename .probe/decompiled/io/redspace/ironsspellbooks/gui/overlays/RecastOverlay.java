package io.redspace.ironsspellbooks.gui.overlays;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.capabilities.magic.RecastInstance;
import io.redspace.ironsspellbooks.config.ClientConfigs;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import org.joml.Vector3f;

public class RecastOverlay implements IGuiOverlay {

    public static RecastOverlay instance = new RecastOverlay();

    public static final ResourceLocation TEXTURE = new ResourceLocation("irons_spellbooks", "textures/gui/icons.png");

    static final int IMAGE_WIDTH = 54;

    static final int COMPLETION_BAR_WIDTH = 44;

    static final int IMAGE_HEIGHT = 21;

    static final int CONNECTOR_WIDTH = 6;

    static final int ORB_WIDTH = 10;

    static final int ORB_TEXTURE_OFFSET_X = 99;

    static final int ORB_TEXTURE_OFFSET_Y = 5;

    static final int CONNECTOR_TEXTURE_OFFSET_X = 109;

    static final int CONNECTOR_TEXTURE_OFFSET_Y = 8;

    @Override
    public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        if (ClientMagicData.getRecasts().hasRecastsActive()) {
            int totalHeightPerBar = 18;
            int screenTopBuffer = 6;
            List<RecastInstance> activeRecasts = ClientMagicData.getRecasts().getActiveRecasts();
            RenderSystem.setShader(GameRenderer::m_172817_);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RecastOverlay.Anchor anchor = ClientConfigs.RECAST_ANCHOR.get();
            for (int castIndex = 0; castIndex < activeRecasts.size(); castIndex++) {
                RecastInstance recastInstance = (RecastInstance) activeRecasts.get(castIndex);
                AbstractSpell spell = SpellRegistry.getSpell(recastInstance.getSpellId());
                int total = recastInstance.getTotalRecasts();
                int remaining = recastInstance.getRemainingRecasts();
                int totalWidth = total * 10 + (total - 1) * 6;
                int barX = (int) ((float) screenWidth * anchor.m1);
                int barY = (int) ((float) screenHeight * anchor.m2);
                if (anchor == RecastOverlay.Anchor.Center || anchor == RecastOverlay.Anchor.TopCenter) {
                    barX -= totalWidth / 2;
                }
                if (anchor == RecastOverlay.Anchor.TopCenter) {
                    barY += screenTopBuffer;
                }
                barX += ClientConfigs.RECAST_X_OFFSET.get();
                barY += ClientConfigs.RECAST_Y_OFFSET.get();
                barY += totalHeightPerBar * castIndex;
                PoseStack poseStack = guiGraphics.pose();
                poseStack.pushPose();
                poseStack.translate((float) (barX - 18), (float) (barY - 2), 0.0F);
                poseStack.scale(0.85F, 0.85F, 0.85F);
                RenderSystem.setShaderTexture(0, spell.getSpellIconResource());
                guiGraphics.blit(spell.getSpellIconResource(), 0, 0, 0.0F, 0.0F, 16, 16, 16, 16);
                RenderSystem.setShaderTexture(0, TEXTURE);
                guiGraphics.blit(TEXTURE, -2, -2, 116.0F, 0.0F, 20, 20, 256, 256);
                poseStack.popPose();
                for (int i = 0; i < total; i++) {
                    int orbX = barX + 16 * i;
                    int connectorX = orbX + 10;
                    if (i + 1 < total) {
                        guiGraphics.blit(TEXTURE, connectorX, barY + 3, 109.0F, 8.0F, 6, 4, 256, 256);
                    }
                    boolean charged = i < remaining;
                    guiGraphics.blit(TEXTURE, orbX, barY, (float) (99 + (charged ? 0 : 10)), 26.0F, 10, 10, 256, 256);
                    if (charged) {
                        Vector3f color = spell.getSchoolType().getTargetingColor();
                        RenderSystem.setShaderColor(color.x(), color.y(), color.z(), 1.0F);
                        guiGraphics.blit(TEXTURE, orbX, barY, (float) (99 + (charged ? 0 : 10)), 26.0F, 10, 10, 256, 256);
                        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                    }
                    guiGraphics.blit(TEXTURE, orbX, barY, 99.0F, 5.0F, 10, 10, 256, 256);
                }
                int textX = barX + 16 * total;
                guiGraphics.drawString(gui.m_93082_(), formatTime(recastInstance.getTicksRemaining(), recastInstance.getTicksToLive()), textX, barY + (10 - 9) / 2, ChatFormatting.WHITE.getColor());
            }
        }
    }

    private static String formatTime(int ticksRemaining, int totalTicks) {
        int totalSeconds = totalTicks / 20;
        int remainingSeconds = ticksRemaining / 20;
        String time = "";
        if (totalSeconds > 60) {
            time = time + String.format("%s:", remainingSeconds / 60);
            remainingSeconds %= 60;
        }
        if (totalSeconds >= 10) {
            time = time + remainingSeconds / 10;
        }
        time = time + remainingSeconds % 10;
        return time + "s";
    }

    public static enum Anchor {

        Center(0.5F, 0.5F),
        TopCenter(0.5F, 0.0F),
        TopLeft(0.0F, 0.0F),
        TopRight(0.0F, 1.0F),
        BottomLeft(0.0F, 1.0F),
        BottomRight(1.0F, 1.0F);

        final float m1;

        final float m2;

        private Anchor(float mx, float my) {
            this.m1 = mx;
            this.m2 = my;
        }
    }
}