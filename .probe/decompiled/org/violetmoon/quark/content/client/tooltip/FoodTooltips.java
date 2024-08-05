package org.violetmoon.quark.content.client.tooltip;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.quark.content.client.module.ImprovedTooltipsModule;
import org.violetmoon.zeta.client.event.play.ZGatherTooltipComponents;

public class FoodTooltips {

    private static boolean isPoison(FoodProperties food) {
        for (Pair<MobEffectInstance, Float> effect : food.getEffects()) {
            if (effect.getFirst() != null && ((MobEffectInstance) effect.getFirst()).getEffect() != null && ((MobEffectInstance) effect.getFirst()).getEffect().getCategory() == MobEffectCategory.HARMFUL) {
                return true;
            }
        }
        return false;
    }

    public static void makeTooltip(ZGatherTooltipComponents event, boolean showFood, boolean showSaturation) {
        ItemStack stack = event.getItemStack();
        if (stack.isEdible()) {
            FoodProperties food = stack.getItem().getFoodProperties();
            if (food != null) {
                int pips = food.getNutrition();
                if (pips == 0) {
                    return;
                }
                int len = (int) Math.ceil((double) pips / (double) ImprovedTooltipsModule.foodDivisor);
                int saturationSimplified = 0;
                float saturation = Math.min(20.0F, food.getSaturationModifier() * (float) food.getNutrition() * 2.0F);
                if (saturation >= 19.0F) {
                    saturationSimplified = 5;
                } else if (saturation < 10.0F) {
                    if (saturation >= 8.0F) {
                        saturationSimplified = 1;
                    } else if (saturation >= 6.0F) {
                        saturationSimplified = 2;
                    } else if (saturation >= 2.0F) {
                        saturationSimplified = 3;
                    } else {
                        saturationSimplified = 4;
                    }
                }
                String prefix = isPoison(food) ? "quark.misc.bad_saturation" : "quark.misc.saturation";
                Component saturationText = Component.translatable(prefix + saturationSimplified).withStyle(ChatFormatting.GRAY);
                List<Either<FormattedText, TooltipComponent>> tooltip = event.getTooltipElements();
                len *= 9;
                if (tooltip.isEmpty()) {
                    if (showFood) {
                        tooltip.add(Either.right(new FoodTooltips.FoodComponent(stack, len, 10)));
                    }
                    if (showSaturation) {
                        tooltip.add(Either.left(saturationText));
                    }
                } else {
                    int i = 1;
                    if (showFood) {
                        tooltip.add(i, Either.right(new FoodTooltips.FoodComponent(stack, len, 10)));
                        i++;
                    }
                    if (showSaturation) {
                        tooltip.add(i, Either.left(saturationText));
                    }
                }
            }
        }
    }

    public static record FoodComponent(ItemStack stack, int width, int height) implements ClientTooltipComponent, TooltipComponent {

        @Override
        public void renderImage(@NotNull Font font, int tooltipX, int tooltipY, @NotNull GuiGraphics guiGraphics) {
            PoseStack pose = guiGraphics.pose();
            Minecraft mc = Minecraft.getInstance();
            if (this.stack.isEdible()) {
                FoodProperties food = this.stack.getItem().getFoodProperties(this.stack, mc.player);
                if (food != null) {
                    RenderSystem.setShader(GameRenderer::m_172817_);
                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                    int pips = food.getNutrition();
                    if (pips == 0) {
                        return;
                    }
                    boolean poison = FoodTooltips.isPoison(food);
                    int count = (int) Math.ceil((double) pips / (double) ImprovedTooltipsModule.foodDivisor);
                    boolean fract = pips % 2 != 0;
                    int renderCount = count;
                    int y = tooltipY - 1;
                    boolean compress = count > ImprovedTooltipsModule.foodCompressionThreshold;
                    if (compress) {
                        renderCount = 1;
                        if (fract) {
                            count--;
                        }
                    }
                    pose.pushPose();
                    pose.translate(0.0F, 0.0F, 500.0F);
                    RenderSystem.setShader(GameRenderer::m_172817_);
                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                    for (int i = 0; i < renderCount; i++) {
                        int x = tooltipX + i * 9 - 1;
                        int u = 16;
                        if (poison) {
                            u += 117;
                        }
                        int v = 27;
                        guiGraphics.blit(Gui.GUI_ICONS_LOCATION, x, y, (float) u, (float) v, 9, 9, 256, 256);
                        int var19 = 52;
                        if (fract && i == renderCount - 1) {
                            var19 += 9;
                        }
                        if (poison) {
                            var19 += 36;
                        }
                        guiGraphics.blit(Gui.GUI_ICONS_LOCATION, x, y, (float) var19, (float) v, 9, 9, 256, 256);
                    }
                    if (compress) {
                        guiGraphics.drawString(mc.font, "x" + count + (fract ? ".5" : ""), tooltipX + 10, y + 1, -10066330, true);
                    }
                    pose.popPose();
                }
            }
        }

        @Override
        public int getHeight() {
            return this.height;
        }

        @Override
        public int getWidth(@NotNull Font font) {
            return this.width;
        }
    }
}