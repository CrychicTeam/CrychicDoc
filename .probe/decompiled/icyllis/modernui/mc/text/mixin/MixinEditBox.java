package icyllis.modernui.mc.text.mixin;

import com.mojang.blaze3d.vertex.VertexConsumer;
import icyllis.modernui.mc.text.TextLayout;
import icyllis.modernui.mc.text.TextLayoutEngine;
import icyllis.modernui.mc.text.VanillaTextWrapper;
import java.util.function.BiFunction;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ EditBox.class })
public abstract class MixinEditBox extends AbstractWidget {

    @Shadow
    @Final
    private static String CURSOR_APPEND_CHARACTER;

    @Shadow
    private boolean isEditable;

    @Shadow
    private int textColor;

    @Shadow
    private int textColorUneditable;

    @Shadow
    private int cursorPos;

    @Shadow
    private int displayPos;

    @Shadow
    private int highlightPos;

    @Shadow
    private String value;

    @Shadow
    private int frame;

    @Shadow
    private boolean bordered;

    @Shadow
    @Nullable
    private String suggestion;

    @Shadow
    private BiFunction<String, Integer, FormattedCharSequence> formatter;

    public MixinEditBox(int x, int y, int w, int h, Component msg) {
        super(x, y, w, h, msg);
    }

    @Inject(method = { "<init>(Lnet/minecraft/client/gui/Font;IIIILnet/minecraft/client/gui/components/EditBox;Lnet/minecraft/network/chat/Component;)V" }, at = { @At("RETURN") })
    public void EditBox(Font font, int x, int y, int w, int h, @Nullable EditBox src, Component msg, CallbackInfo ci) {
        this.formatter = (s, i) -> new VanillaTextWrapper(s);
    }

    @Shadow
    public abstract int getInnerWidth();

    @Shadow
    protected abstract int getMaxLength();

    @Inject(method = { "renderWidget" }, at = { @At(value = "FIELD", target = "Lnet/minecraft/client/gui/components/EditBox;isEditable:Z", opcode = 180) }, cancellable = true)
    public void onRenderWidget(@Nonnull GuiGraphics gr, int mouseX, int mouseY, float deltaTicks, CallbackInfo ci) {
        TextLayoutEngine engine = TextLayoutEngine.getInstance();
        int color = this.isEditable ? this.textColor : this.textColorUneditable;
        String viewText = engine.getStringSplitter().headByWidth(this.value.substring(this.displayPos), (float) this.getInnerWidth(), Style.EMPTY);
        int viewCursorPos = this.cursorPos - this.displayPos;
        int clampedViewHighlightPos = Mth.clamp(this.highlightPos - this.displayPos, 0, viewText.length());
        boolean cursorInRange = viewCursorPos >= 0 && viewCursorPos <= viewText.length();
        boolean cursorVisible = this.m_93696_() && (this.frame / 10 & 1) == 0 && cursorInRange;
        int baseX = this.bordered ? this.m_252754_() + 4 : this.m_252754_();
        int baseY = this.bordered ? this.m_252907_() + (this.f_93619_ - 8) / 2 : this.m_252907_();
        float hori = (float) baseX;
        Matrix4f matrix = gr.pose().last().pose();
        MultiBufferSource.BufferSource bufferSource = gr.bufferSource();
        boolean separate;
        if (!viewText.isEmpty()) {
            String subText = cursorInRange ? viewText.substring(0, viewCursorPos) : viewText;
            FormattedCharSequence subSequence = (FormattedCharSequence) this.formatter.apply(subText, this.displayPos);
            if (subSequence != null && !(subSequence instanceof VanillaTextWrapper)) {
                separate = true;
                hori = engine.getTextRenderer().drawText(subSequence, hori, (float) baseY, color, true, matrix, bufferSource, Font.DisplayMode.NORMAL, 0, 15728880);
            } else {
                separate = false;
                hori = engine.getTextRenderer().drawText(viewText, hori, (float) baseY, color, true, matrix, bufferSource, Font.DisplayMode.NORMAL, 0, 15728880);
            }
        } else {
            separate = false;
        }
        boolean cursorNotAtEnd = this.cursorPos < this.value.length() || this.value.length() >= this.getMaxLength();
        float cursorX;
        if (cursorInRange) {
            if (!separate && !viewText.isEmpty()) {
                TextLayout layout = engine.lookupVanillaLayout(viewText, Style.EMPTY, 1);
                float curAdv = 0.0F;
                int stripIndex = 0;
                for (int i = 0; i < viewCursorPos; i++) {
                    if (viewText.charAt(i) == 167) {
                        i++;
                    } else {
                        curAdv += layout.getAdvances()[stripIndex++];
                    }
                }
                cursorX = (float) baseX + curAdv;
            } else {
                cursorX = hori;
            }
        } else {
            cursorX = viewCursorPos > 0 ? (float) (baseX + this.f_93618_) : (float) baseX;
        }
        if (!viewText.isEmpty() && cursorInRange && viewCursorPos < viewText.length() && separate) {
            String subText = viewText.substring(viewCursorPos);
            FormattedCharSequence subSequence = (FormattedCharSequence) this.formatter.apply(subText, this.cursorPos);
            if (subSequence != null && !(subSequence instanceof VanillaTextWrapper)) {
                engine.getTextRenderer().drawText(subSequence, hori, (float) baseY, color, true, matrix, bufferSource, Font.DisplayMode.NORMAL, 0, 15728880);
            } else {
                engine.getTextRenderer().drawText(subText, hori, (float) baseY, color, true, matrix, bufferSource, Font.DisplayMode.NORMAL, 0, 15728880);
            }
        }
        if (!cursorNotAtEnd && this.suggestion != null) {
            engine.getTextRenderer().drawText(this.suggestion, cursorX, (float) baseY, -8355712, true, matrix, bufferSource, Font.DisplayMode.NORMAL, 0, 15728880);
        }
        if (viewCursorPos != clampedViewHighlightPos) {
            gr.flush();
            TextLayout layout = engine.lookupVanillaLayout(viewText, Style.EMPTY, 1);
            float startX = (float) baseX;
            float endX = cursorX;
            int stripIndex = 0;
            for (int ix = 0; ix < clampedViewHighlightPos; ix++) {
                if (viewText.charAt(ix) == 167) {
                    ix++;
                } else {
                    startX += layout.getAdvances()[stripIndex++];
                }
            }
            if (cursorX < startX) {
                float temp = startX;
                startX = cursorX;
                endX = temp;
            }
            if (startX > (float) (this.m_252754_() + this.f_93618_)) {
                startX = (float) (this.m_252754_() + this.f_93618_);
            }
            if (endX > (float) (this.m_252754_() + this.f_93618_)) {
                endX = (float) (this.m_252754_() + this.f_93618_);
            }
            VertexConsumer consumer = gr.bufferSource().getBuffer(RenderType.guiOverlay());
            consumer.vertex(matrix, startX, (float) (baseY + 10), 0.0F).color(51, 181, 229, 56).endVertex();
            consumer.vertex(matrix, endX, (float) (baseY + 10), 0.0F).color(51, 181, 229, 56).endVertex();
            consumer.vertex(matrix, endX, (float) (baseY - 1), 0.0F).color(51, 181, 229, 56).endVertex();
            consumer.vertex(matrix, startX, (float) (baseY - 1), 0.0F).color(51, 181, 229, 56).endVertex();
            gr.flush();
        } else if (cursorVisible) {
            if (cursorNotAtEnd) {
                gr.flush();
                VertexConsumer consumer = gr.bufferSource().getBuffer(RenderType.guiOverlay());
                consumer.vertex(matrix, cursorX - 0.5F, (float) (baseY + 10), 0.0F).color(208, 208, 208, 255).endVertex();
                consumer.vertex(matrix, cursorX + 0.5F, (float) (baseY + 10), 0.0F).color(208, 208, 208, 255).endVertex();
                consumer.vertex(matrix, cursorX + 0.5F, (float) (baseY - 1), 0.0F).color(208, 208, 208, 255).endVertex();
                consumer.vertex(matrix, cursorX - 0.5F, (float) (baseY - 1), 0.0F).color(208, 208, 208, 255).endVertex();
                gr.flush();
            } else {
                engine.getTextRenderer().drawText(CURSOR_APPEND_CHARACTER, cursorX, (float) baseY, color, true, matrix, bufferSource, Font.DisplayMode.NORMAL, 0, 15728880);
                gr.flush();
            }
        } else {
            gr.flush();
        }
        ci.cancel();
    }
}