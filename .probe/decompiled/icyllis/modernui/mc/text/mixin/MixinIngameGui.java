package icyllis.modernui.mc.text.mixin;

import icyllis.modernui.mc.text.ModernTextRenderer;
import icyllis.modernui.mc.text.TextLayoutEngine;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ Gui.class })
public abstract class MixinIngameGui {

    @Shadow
    @Final
    protected Minecraft minecraft;

    @Shadow
    protected int screenWidth;

    @Shadow
    protected int screenHeight;

    @Shadow
    public abstract Font getFont();

    @Redirect(method = { "renderExperienceBar" }, at = @At(value = "FIELD", target = "net/minecraft/client/player/LocalPlayer.experienceLevel:I", opcode = 180))
    private int fakeExperience(LocalPlayer player) {
        return 0;
    }

    @Inject(method = { "renderExperienceBar" }, at = { @At("TAIL") })
    private void drawExperience(GuiGraphics gr, int i, CallbackInfo ci) {
        LocalPlayer player = this.minecraft.player;
        if (player != null && player.f_36078_ > 0) {
            String s = Integer.toString(player.f_36078_);
            TextLayoutEngine engine = TextLayoutEngine.getInstance();
            float w = engine.getStringSplitter().measureText(s);
            float x = ((float) this.screenWidth - w) / 2.0F;
            float y = (float) (this.screenHeight - 31 - 4);
            float offset = ModernTextRenderer.sOutlineOffset;
            Matrix4f pose = gr.pose().last().pose();
            MultiBufferSource.BufferSource source = gr.bufferSource();
            engine.getTextRenderer().drawText(s, x + offset, y, -16777216, false, pose, source, Font.DisplayMode.NORMAL, 0, 15728880);
            source.endBatch();
            engine.getTextRenderer().drawText(s, x - offset, y, -16777216, false, pose, source, Font.DisplayMode.NORMAL, 0, 15728880);
            source.endBatch();
            engine.getTextRenderer().drawText(s, x, y + offset, -16777216, false, pose, source, Font.DisplayMode.NORMAL, 0, 15728880);
            source.endBatch();
            engine.getTextRenderer().drawText(s, x, y - offset, -16777216, false, pose, source, Font.DisplayMode.NORMAL, 0, 15728880);
            source.endBatch();
            engine.getTextRenderer().drawText(s, x, y, -8323296, false, pose, source, Font.DisplayMode.NORMAL, 0, 15728880);
            gr.flush();
        }
    }
}