package icyllis.modernui.mc.mixin;

import icyllis.modernui.mc.MuiModApi;
import icyllis.modernui.mc.ScrollController;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ AbstractSelectionList.class })
public abstract class MixinSelectionList implements ScrollController.IListener {

    @Shadow
    private double scrollAmount;

    @Shadow
    @Final
    protected int itemHeight;

    @Shadow
    @Final
    protected Minecraft minecraft;

    @Unique
    @Nullable
    private ScrollController modernUI_MC$mScrollController;

    @Shadow
    public abstract int getMaxScroll();

    @Shadow
    public abstract double getScrollAmount();

    @Overwrite
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollY) {
        if (scrollY != 0.0) {
            if (this.modernUI_MC$mScrollController != null) {
                this.modernUI_MC$mScrollController.setMaxScroll((float) this.getMaxScroll());
                this.modernUI_MC$mScrollController.scrollBy((float) Math.round(-scrollY * 40.0));
            } else {
                this.setScrollAmount(this.getScrollAmount() - scrollY * (double) this.itemHeight / 2.0);
            }
            return true;
        } else {
            return false;
        }
    }

    @Inject(method = { "render" }, at = { @At("HEAD") })
    private void preRender(GuiGraphics gr, int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        if (this.modernUI_MC$mScrollController == null) {
            this.modernUI_MC$mScrollController = new ScrollController(this);
            this.modernUI_MC$skipAnimationTo(this.scrollAmount);
        }
        this.modernUI_MC$mScrollController.update(MuiModApi.getElapsedTime());
    }

    @Inject(method = { "render" }, at = { @At(value = "INVOKE", shift = Shift.BEFORE, target = "Lnet/minecraft/client/gui/components/AbstractSelectionList;renderHeader(Lnet/minecraft/client/gui/GuiGraphics;II)V") })
    private void preRenderHeader(@Nonnull GuiGraphics gr, int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        gr.pose().pushPose();
        gr.pose().translate(0.0, (double) ((int) (((double) ((int) this.getScrollAmount()) - this.getScrollAmount()) * this.minecraft.getWindow().getGuiScale())) / this.minecraft.getWindow().getGuiScale(), 0.0);
    }

    @Inject(method = { "render" }, at = { @At(value = "INVOKE", shift = Shift.AFTER, target = "Lnet/minecraft/client/gui/components/AbstractSelectionList;renderHeader(Lnet/minecraft/client/gui/GuiGraphics;II)V") })
    private void postRenderHeader(@Nonnull GuiGraphics gr, int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        gr.pose().popPose();
    }

    @Inject(method = { "render" }, at = { @At(value = "INVOKE", shift = Shift.BEFORE, target = "Lnet/minecraft/client/gui/components/AbstractSelectionList;renderList(Lnet/minecraft/client/gui/GuiGraphics;IIF)V") })
    private void preRenderList(@Nonnull GuiGraphics gr, int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        gr.pose().pushPose();
        gr.pose().translate(0.0, (double) ((int) (((double) ((int) this.getScrollAmount()) - this.getScrollAmount()) * this.minecraft.getWindow().getGuiScale())) / this.minecraft.getWindow().getGuiScale(), 0.0);
    }

    @Inject(method = { "render" }, at = { @At(value = "INVOKE", shift = Shift.AFTER, target = "Lnet/minecraft/client/gui/components/AbstractSelectionList;renderList(Lnet/minecraft/client/gui/GuiGraphics;IIF)V") })
    private void postRenderList(@Nonnull GuiGraphics gr, int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        gr.pose().popPose();
    }

    @Overwrite
    public void setScrollAmount(double target) {
        if (this.modernUI_MC$mScrollController != null) {
            this.modernUI_MC$skipAnimationTo(target);
        } else {
            this.scrollAmount = Mth.clamp(target, 0.0, (double) this.getMaxScroll());
        }
    }

    @Override
    public void onScrollAmountUpdated(ScrollController controller, float amount) {
        this.scrollAmount = Mth.clamp((double) amount, 0.0, (double) this.getMaxScroll());
    }

    @Unique
    public void modernUI_MC$skipAnimationTo(double target) {
        assert this.modernUI_MC$mScrollController != null;
        this.modernUI_MC$mScrollController.setMaxScroll((float) this.getMaxScroll());
        this.modernUI_MC$mScrollController.scrollTo((float) target);
        this.modernUI_MC$mScrollController.abortAnimation();
    }
}