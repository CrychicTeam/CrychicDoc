package icyllis.modernui.mc.mixin;

import icyllis.modernui.mc.MuiModApi;
import icyllis.modernui.mc.ScrollController;
import javax.annotation.Nonnull;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.client.gui.widget.ScrollPanel;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ ScrollPanel.class })
public abstract class MixinScrollPanel implements ScrollController.IListener {

    @Shadow(remap = false)
    protected float scrollDistance;

    @Shadow(remap = false)
    private boolean scrolling;

    @Shadow(remap = false)
    @Final
    protected int height;

    @Shadow(remap = false)
    @Final
    private Minecraft client;

    @Unique
    private final ScrollController modernUI_MC$mScrollController = new ScrollController(this);

    @Shadow(remap = false)
    protected abstract void applyScrollLimits();

    @Shadow(remap = false)
    protected abstract int getScrollAmount();

    @Shadow(remap = false)
    protected abstract int getMaxScroll();

    @Shadow(remap = false)
    protected abstract int getBarHeight();

    @Overwrite
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollY) {
        if (scrollY != 0.0) {
            this.modernUI_MC$mScrollController.setMaxScroll((float) this.getMaxScroll());
            this.modernUI_MC$mScrollController.scrollBy((float) Math.round(-scrollY * (double) this.getScrollAmount()));
            return true;
        } else {
            return false;
        }
    }

    @Inject(method = { "render" }, at = { @At("HEAD") })
    private void preRender(GuiGraphics gr, int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        this.modernUI_MC$mScrollController.update(MuiModApi.getElapsedTime());
    }

    @Inject(method = { "render" }, at = { @At(value = "INVOKE", shift = Shift.BEFORE, target = "Lnet/minecraftforge/client/gui/widget/ScrollPanel;drawPanel(Lnet/minecraft/client/gui/GuiGraphics;IILcom/mojang/blaze3d/vertex/Tesselator;II)V") }, remap = false)
    private void preDrawPanel(@Nonnull GuiGraphics gr, int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        gr.pose().pushPose();
        gr.pose().translate(0.0, (double) ((int) ((double) ((float) ((int) this.scrollDistance) - this.scrollDistance) * this.client.getWindow().getGuiScale())) / this.client.getWindow().getGuiScale(), 0.0);
    }

    @Inject(method = { "render" }, at = { @At(value = "INVOKE", shift = Shift.AFTER, target = "Lnet/minecraftforge/client/gui/widget/ScrollPanel;drawPanel(Lnet/minecraft/client/gui/GuiGraphics;IILcom/mojang/blaze3d/vertex/Tesselator;II)V") }, remap = false)
    private void postDrawPanel(@Nonnull GuiGraphics gr, int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        gr.pose().popPose();
    }

    @Override
    public void onScrollAmountUpdated(ScrollController controller, float amount) {
        this.scrollDistance = amount;
        this.applyScrollLimits();
    }

    @Overwrite
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (this.scrolling) {
            int maxScroll = this.height - this.getBarHeight();
            float moved = (float) (deltaY / (double) maxScroll);
            this.modernUI_MC$mScrollController.setMaxScroll((float) this.getMaxScroll());
            this.modernUI_MC$mScrollController.scrollBy((float) this.getMaxScroll() * moved);
            this.modernUI_MC$mScrollController.abortAnimation();
            return true;
        } else {
            return false;
        }
    }
}