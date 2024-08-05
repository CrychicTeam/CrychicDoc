package org.violetmoon.quark.content.client.tooltip;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Either;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.quark.base.Quark;
import org.violetmoon.quark.base.handler.SimilarBlockTypeHandler;
import org.violetmoon.quark.content.client.module.ChestSearchingModule;
import org.violetmoon.quark.content.client.module.ImprovedTooltipsModule;
import org.violetmoon.zeta.client.event.play.ZGatherTooltipComponents;
import org.violetmoon.zeta.util.ItemNBTHelper;

public class ShulkerBoxTooltips {

    public static final ResourceLocation WIDGET_RESOURCE = new ResourceLocation("quark", "textures/misc/shulker_widget.png");

    public static void makeTooltip(ZGatherTooltipComponents event) {
        ItemStack stack = event.getItemStack();
        if (SimilarBlockTypeHandler.isShulkerBox(stack)) {
            CompoundTag cmp = ItemNBTHelper.getCompound(stack, "BlockEntityTag", false);
            if (cmp.contains("LootTable")) {
                return;
            }
            if (!cmp.contains("id")) {
                return;
            }
            BlockEntity te = BlockEntity.loadStatic(BlockPos.ZERO, ((BlockItem) stack.getItem()).getBlock().defaultBlockState(), cmp);
            if (te != null && te.getCapability(ForgeCapabilities.ITEM_HANDLER).isPresent()) {
                List<Either<FormattedText, TooltipComponent>> tooltip = event.getTooltipElements();
                List<Either<FormattedText, TooltipComponent>> tooltipCopy = new ArrayList(tooltip);
                for (int i = 1; i < tooltipCopy.size(); i++) {
                    Either<FormattedText, TooltipComponent> either = (Either<FormattedText, TooltipComponent>) tooltipCopy.get(i);
                    if (either.left().isPresent()) {
                        String s = ((FormattedText) either.left().get()).getString();
                        if (!s.startsWith("§") || s.startsWith("§o")) {
                            tooltip.remove(either);
                        }
                    }
                }
                if (!ImprovedTooltipsModule.shulkerBoxRequireShift || Screen.hasShiftDown()) {
                    tooltip.add(1, Either.right(new ShulkerBoxTooltips.ShulkerComponent(stack)));
                }
                if (ImprovedTooltipsModule.shulkerBoxRequireShift && !Screen.hasShiftDown()) {
                    tooltip.add(1, Either.left(Component.translatable("quark.misc.shulker_box_shift")));
                }
            }
        }
    }

    public static record ShulkerComponent(ItemStack stack) implements ClientTooltipComponent, TooltipComponent {

        private static final int[][] TARGET_RATIOS = new int[][] { { 1, 1 }, { 9, 3 }, { 9, 5 }, { 9, 6 }, { 9, 8 }, { 9, 9 }, { 12, 9 } };

        private static final int CORNER = 5;

        private static final int BUFFER = 1;

        private static final int EDGE = 18;

        @Override
        public void renderImage(@NotNull Font font, int tooltipX, int tooltipY, @NotNull GuiGraphics guiGraphics) {
            Minecraft mc = Minecraft.getInstance();
            PoseStack pose = guiGraphics.pose();
            CompoundTag cmp = ItemNBTHelper.getCompound(this.stack, "BlockEntityTag", true);
            if (cmp != null) {
                if (cmp.contains("LootTable")) {
                    return;
                }
                if (!cmp.contains("id")) {
                    cmp = cmp.copy();
                    cmp.putString("id", "minecraft:shulker_box");
                }
                BlockEntity te = BlockEntity.loadStatic(BlockPos.ZERO, ((BlockItem) this.stack.getItem()).getBlock().defaultBlockState(), cmp);
                if (te != null) {
                    if (te instanceof RandomizableContainerBlockEntity randomizable) {
                        randomizable.setLootTable(null, 0L);
                    }
                    LazyOptional<IItemHandler> handler = te.getCapability(ForgeCapabilities.ITEM_HANDLER, null);
                    handler.ifPresent(capability -> {
                        ItemStack currentBox = this.stack;
                        int currentX = tooltipX;
                        int currentY = tooltipY - 1;
                        int size = capability.getSlots();
                        int[] dims = new int[] { Math.min(size, 9), Math.max(size / 9, 1) };
                        for (int[] testAgainst : TARGET_RATIOS) {
                            if (testAgainst[0] * testAgainst[1] == size) {
                                dims = testAgainst;
                                break;
                            }
                        }
                        int texWidth = 10 + 18 * dims[0];
                        int right = tooltipX + texWidth;
                        Window window = mc.getWindow();
                        if (right > window.getGuiScaledWidth()) {
                            currentX = tooltipX - (right - window.getGuiScaledWidth());
                        }
                        pose.pushPose();
                        pose.translate(0.0F, 0.0F, 700.0F);
                        int color = -1;
                        if (ImprovedTooltipsModule.shulkerBoxUseColors && ((BlockItem) currentBox.getItem()).getBlock() instanceof ShulkerBoxBlock boxBlock) {
                            DyeColor dye = boxBlock.getColor();
                            if (dye != null) {
                                float[] colorComponents = dye.getTextureDiffuseColors();
                                color = (int) (colorComponents[0] * 255.0F) << 16 | (int) (colorComponents[1] * 255.0F) << 8 | (int) (colorComponents[2] * 255.0F);
                            }
                        }
                        renderTooltipBackground(guiGraphics, mc, pose, currentX, currentY, dims[0], dims[1], color);
                        for (int i = 0; i < size; i++) {
                            ItemStack itemstack = capability.getStackInSlot(i);
                            int xp = currentX + 6 + i % 9 * 18;
                            int yp = currentY + 6 + i / 9 * 18;
                            if (!itemstack.isEmpty()) {
                                guiGraphics.renderItem(itemstack, xp, yp);
                                guiGraphics.renderItemDecorations(mc.font, itemstack, xp, yp);
                            }
                            if (!Quark.ZETA.modules.<ChestSearchingModule>get(ChestSearchingModule.class).namesMatch(itemstack)) {
                                RenderSystem.disableDepthTest();
                                guiGraphics.fill(xp, yp, xp + 16, yp + 16, -1442840576);
                            }
                        }
                        pose.popPose();
                    });
                }
            }
        }

        public static void renderTooltipBackground(GuiGraphics guiGraphics, Minecraft mc, PoseStack matrix, int x, int y, int width, int height, int color) {
            RenderSystem.setShader(GameRenderer::m_172817_);
            RenderSystem.setShaderTexture(0, ShulkerBoxTooltips.WIDGET_RESOURCE);
            RenderSystem.setShaderColor((float) ((color & 0xFF0000) >> 16) / 255.0F, (float) ((color & 0xFF00) >> 8) / 255.0F, (float) (color & 0xFF) / 255.0F, 1.0F);
            guiGraphics.blit(ShulkerBoxTooltips.WIDGET_RESOURCE, x, y, 0.0F, 0.0F, 5, 5, 256, 256);
            guiGraphics.blit(ShulkerBoxTooltips.WIDGET_RESOURCE, x + 5 + 18 * width, y + 5 + 18 * height, 25.0F, 25.0F, 5, 5, 256, 256);
            guiGraphics.blit(ShulkerBoxTooltips.WIDGET_RESOURCE, x + 5 + 18 * width, y, 25.0F, 0.0F, 5, 5, 256, 256);
            guiGraphics.blit(ShulkerBoxTooltips.WIDGET_RESOURCE, x, y + 5 + 18 * height, 0.0F, 25.0F, 5, 5, 256, 256);
            for (int row = 0; row < height; row++) {
                guiGraphics.blit(ShulkerBoxTooltips.WIDGET_RESOURCE, x, y + 5 + 18 * row, 0.0F, 6.0F, 5, 18, 256, 256);
                guiGraphics.blit(ShulkerBoxTooltips.WIDGET_RESOURCE, x + 5 + 18 * width, y + 5 + 18 * row, 25.0F, 6.0F, 5, 18, 256, 256);
                for (int col = 0; col < width; col++) {
                    if (row == 0) {
                        guiGraphics.blit(ShulkerBoxTooltips.WIDGET_RESOURCE, x + 5 + 18 * col, y, 6.0F, 0.0F, 18, 5, 256, 256);
                        guiGraphics.blit(ShulkerBoxTooltips.WIDGET_RESOURCE, x + 5 + 18 * col, y + 5 + 18 * height, 6.0F, 25.0F, 18, 5, 256, 256);
                    }
                    guiGraphics.blit(ShulkerBoxTooltips.WIDGET_RESOURCE, x + 5 + 18 * col, y + 5 + 18 * row, 6.0F, 6.0F, 18, 18, 256, 256);
                }
            }
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        }

        @Override
        public int getHeight() {
            return 65;
        }

        @Override
        public int getWidth(@NotNull Font font) {
            return 171;
        }
    }
}