package portb.biggerstacks.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DecimalFormat;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import portb.biggerstacks.config.ClientConfig;

@Mixin({ GuiGraphics.class })
public abstract class ItemRendererMixin {

    private static final DecimalFormat BILLION_FORMAT = new DecimalFormat("#.##B");

    private static final DecimalFormat MILLION_FORMAT = new DecimalFormat("#.##M");

    private static final DecimalFormat THOUSAND_FORMAT = new DecimalFormat("#.##K");

    private static String getStringForBigStackCount(int count) {
        if (ClientConfig.enableNumberShortening.get()) {
            BigDecimal decimal = new BigDecimal(count).round(new MathContext(3));
            double value = decimal.doubleValue();
            if (value >= 1.0E9) {
                return BILLION_FORMAT.format(value / 1.0E9);
            }
            if (value >= 1000000.0) {
                return MILLION_FORMAT.format(value / 1000000.0);
            }
            if (value > 1000.0) {
                return THOUSAND_FORMAT.format(value / 1000.0);
            }
        }
        return String.valueOf(count);
    }

    private static double calculateStringScale(Font font, String countString) {
        int width = font.width(countString);
        return width < 16 ? 1.0 : 16.0 / (double) width;
    }

    @Redirect(method = { "renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V" }, at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(FFF)V"))
    private void doNothing1(PoseStack instance, float float0, float float1, float float2) {
    }

    @Redirect(method = { "renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Ljava/lang/String;IIIZ)I"))
    private int doNothing2(GuiGraphics instance, Font font0, String string1, int int2, int int3, int int4, boolean boolean5) {
        return 0;
    }

    @Inject(method = { "renderItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Ljava/lang/String;IIIZ)I") })
    private void renderText(Font font, ItemStack itemStack, int x, int y, String alternateCount, CallbackInfo ci) {
        PoseStack poseStack = ((GuiGraphics) this).pose();
        String text = alternateCount == null ? getStringForBigStackCount(itemStack.getCount()) : alternateCount;
        float scale = (float) calculateStringScale(font, text);
        float inverseScale = 1.0F / scale;
        poseStack.scale(scale, scale, 1.0F);
        poseStack.translate((float) (x + 16) * inverseScale - (float) font.width(text), (float) (y + 16) * inverseScale - 9.0F, 200.0F);
        MultiBufferSource.BufferSource bufferSource = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
        font.drawInBatch(text, 0.0F, 0.0F, 16777215, true, poseStack.last().pose(), bufferSource, Font.DisplayMode.NORMAL, 0, 15728880);
        bufferSource.endBatch();
    }
}