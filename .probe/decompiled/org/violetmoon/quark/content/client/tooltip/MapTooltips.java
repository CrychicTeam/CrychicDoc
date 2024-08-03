package org.violetmoon.quark.content.client.tooltip;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Either;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.quark.content.client.module.ImprovedTooltipsModule;
import org.violetmoon.zeta.client.event.play.ZGatherTooltipComponents;

public class MapTooltips {

    private static final ResourceLocation RES_MAP_BACKGROUND = new ResourceLocation("textures/map/map_background.png");

    public static void makeTooltip(ZGatherTooltipComponents event) {
        ItemStack stack = event.getItemStack();
        if (!stack.isEmpty() && stack.getItem() instanceof MapItem) {
            List<Either<FormattedText, TooltipComponent>> tooltip = event.getTooltipElements();
            if (!ImprovedTooltipsModule.mapRequireShift || Screen.hasShiftDown()) {
                tooltip.add(1, Either.right(new MapTooltips.MapComponent(stack)));
            } else if (ImprovedTooltipsModule.mapRequireShift && !Screen.hasShiftDown()) {
                tooltip.add(1, Either.left(Component.translatable("quark.misc.map_shift")));
            }
        }
    }

    public static record MapComponent(ItemStack stack) implements ClientTooltipComponent, TooltipComponent {

        @Override
        public void renderImage(@NotNull Font font, int tooltipX, int tooltipY, @NotNull GuiGraphics guiGraphics) {
            Minecraft mc = Minecraft.getInstance();
            PoseStack pose = guiGraphics.pose();
            MapItemSavedData mapdata = MapItem.getSavedData(this.stack, mc.level);
            Integer mapID = MapItem.getMapId(this.stack);
            if (mapdata != null) {
                RenderSystem.setShader(GameRenderer::m_172817_);
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                int pad = 7;
                int size = 135 + pad;
                float scale = 0.5F;
                pose.pushPose();
                pose.translate((float) (tooltipX + 3), (float) (tooltipY + 3), 500.0F);
                pose.scale(scale, scale, 1.0F);
                RenderSystem.enableBlend();
                guiGraphics.blit(MapTooltips.RES_MAP_BACKGROUND, -pad, -pad, 0.0F, 0.0F, size, size, size, size);
                pose.translate(0.0F, 0.0F, 1.0F);
                mc.gameRenderer.getMapRenderer().render(pose, guiGraphics.bufferSource(), mapID, mapdata, true, 240);
            }
        }

        @Override
        public int getHeight() {
            Minecraft mc = Minecraft.getInstance();
            MapItemSavedData mapdata = MapItem.getSavedData(this.stack, mc.level);
            return mapdata != null ? 75 : 0;
        }

        @Override
        public int getWidth(@NotNull Font font) {
            return 72;
        }
    }
}