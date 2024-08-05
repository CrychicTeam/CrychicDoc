package icyllis.modernui.mc.text.mixin;

import icyllis.modernui.mc.text.ModernStringSplitter;
import icyllis.modernui.mc.text.ModernTextRenderer;
import icyllis.modernui.mc.text.TextLayoutEngine;
import javax.annotation.Nonnull;
import net.minecraft.client.StringSplitter;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({ Font.class })
public abstract class MixinFontRenderer {

    @Unique
    private final ModernTextRenderer modernUI_MC$textRenderer = TextLayoutEngine.getInstance().getTextRenderer();

    @Redirect(method = { "<init>" }, at = @At(value = "NEW", target = "(Lnet/minecraft/client/StringSplitter$WidthProvider;)Lnet/minecraft/client/StringSplitter;"))
    private StringSplitter onNewSplitter(StringSplitter.WidthProvider widthProvider) {
        return new ModernStringSplitter(TextLayoutEngine.getInstance(), widthProvider);
    }

    @Overwrite
    public int drawInBatch(@Nonnull String text, float x, float y, int color, boolean dropShadow, @Nonnull Matrix4f matrix, @Nonnull MultiBufferSource source, Font.DisplayMode displayMode, int colorBackground, int packedLight, @Deprecated boolean bidiFlag) {
        return (int) this.modernUI_MC$textRenderer.drawText(text, x, y, color, dropShadow, matrix, source, displayMode, colorBackground, packedLight) + (dropShadow ? 1 : 0);
    }

    @Overwrite
    public int drawInBatch(@Nonnull Component text, float x, float y, int color, boolean dropShadow, @Nonnull Matrix4f matrix, @Nonnull MultiBufferSource source, Font.DisplayMode displayMode, int colorBackground, int packedLight) {
        return (int) this.modernUI_MC$textRenderer.drawText(text, x, y, color, dropShadow, matrix, source, displayMode, colorBackground, packedLight) + (dropShadow ? 1 : 0);
    }

    @Overwrite
    public int drawInBatch(@Nonnull FormattedCharSequence text, float x, float y, int color, boolean dropShadow, @Nonnull Matrix4f matrix, @Nonnull MultiBufferSource source, Font.DisplayMode displayMode, int colorBackground, int packedLight) {
        return (int) this.modernUI_MC$textRenderer.drawText(text, x, y, color, dropShadow, matrix, source, displayMode, colorBackground, packedLight) + (dropShadow ? 1 : 0);
    }

    @Overwrite
    public String bidirectionalShaping(String text) {
        return text;
    }

    @Overwrite
    public void drawInBatch8xOutline(@Nonnull FormattedCharSequence text, float x, float y, int color, int outlineColor, @Nonnull Matrix4f matrix, @Nonnull MultiBufferSource source, int packedLight) {
        this.modernUI_MC$textRenderer.drawText8xOutline(text, x, y, color, outlineColor, matrix, source, packedLight);
    }
}