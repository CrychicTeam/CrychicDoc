package com.simibubi.create.foundation.ponder.ui;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.gui.AbstractSimiScreen;
import com.simibubi.create.foundation.gui.ScreenOpener;
import com.simibubi.create.foundation.gui.Theme;
import com.simibubi.create.foundation.gui.UIRenderHelper;
import com.simibubi.create.foundation.gui.widget.ElementWidget;
import com.simibubi.create.foundation.utility.Color;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableInt;

public abstract class NavigatableSimiScreen extends AbstractSimiScreen {

    public static final String THINK_BACK = "ponder.think_back";

    protected int depthPointX;

    protected int depthPointY;

    public final LerpedFloat transition = LerpedFloat.linear().startWithValue(0.0).chase(0.0, 0.1F, LerpedFloat.Chaser.LINEAR);

    protected final LerpedFloat arrowAnimation = LerpedFloat.linear().startWithValue(0.0).chase(0.0, 0.075F, LerpedFloat.Chaser.LINEAR);

    protected PonderButton backTrack;

    public NavigatableSimiScreen() {
        Window window = Minecraft.getInstance().getWindow();
        this.depthPointX = window.getGuiScaledWidth() / 2;
        this.depthPointY = window.getGuiScaledHeight() / 2;
    }

    @Override
    public void onClose() {
        ScreenOpener.clearStack();
        super.m_7379_();
    }

    @Override
    public void tick() {
        super.tick();
        this.transition.tickChaser();
        this.arrowAnimation.tickChaser();
    }

    @Override
    protected void init() {
        super.init();
        this.backTrack = null;
        List<Screen> screenHistory = ScreenOpener.getScreenHistory();
        if (!screenHistory.isEmpty()) {
            if (screenHistory.get(0) instanceof NavigatableSimiScreen) {
                NavigatableSimiScreen screen = (NavigatableSimiScreen) screenHistory.get(0);
                this.m_142416_(this.backTrack = new PonderButton(31, this.f_96544_ - 31 - 20).<ElementWidget>enableFade(0, 5).withCallback(() -> ScreenOpener.openPreviousScreen(this, Optional.empty())));
                this.backTrack.fade(1.0F);
                screen.initBackTrackIcon(this.backTrack);
            }
        }
    }

    protected abstract void initBackTrackIcon(PonderButton var1);

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.render(graphics, mouseX, mouseY, partialTicks);
        if (this.backTrack != null) {
            PoseStack ms = graphics.pose();
            ms.pushPose();
            ms.translate(0.0F, 0.0F, 500.0F);
            if (this.backTrack.m_198029_()) {
                MutableComponent translate = Lang.translateDirect(this.backTrackingLangKey());
                graphics.drawString(this.f_96547_, translate, 41 - this.f_96547_.width(translate) / 2, this.f_96544_ - 16, Theme.i(Theme.Key.TEXT_DARKER), false);
                if (Mth.equal(this.arrowAnimation.getValue(), this.arrowAnimation.getChaseTarget())) {
                    this.arrowAnimation.setValue(1.0);
                    this.arrowAnimation.setValue(1.0);
                }
            }
            ms.popPose();
        }
    }

    protected String backTrackingLangKey() {
        return "ponder.think_back";
    }

    @Override
    protected void renderWindowBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        if (this.backTrack != null) {
            int x = (int) Mth.lerp(this.arrowAnimation.getValue(partialTicks), -9.0F, 21.0F);
            int maxX = this.backTrack.m_252754_() + this.backTrack.m_5711_();
            if (x + 30 < this.backTrack.m_252754_()) {
                UIRenderHelper.breadcrumbArrow(graphics, x + 30, this.f_96544_ - 51, 0, maxX - (x + 30), 20, 5, Theme.p(Theme.Key.PONDER_BACK_ARROW));
            }
            UIRenderHelper.breadcrumbArrow(graphics, x, this.f_96544_ - 51, 0, 30, 20, 5, Theme.p(Theme.Key.PONDER_BACK_ARROW));
            UIRenderHelper.breadcrumbArrow(graphics, x - 30, this.f_96544_ - 51, 0, 30, 20, 5, Theme.p(Theme.Key.PONDER_BACK_ARROW));
        }
        if (this.transition.getChaseTarget() != 0.0F && !this.transition.settled()) {
            this.m_280273_(graphics);
            PoseStack ms = graphics.pose();
            float transitionValue = this.transition.getValue(partialTicks);
            float scale = 1.0F + 0.5F * transitionValue;
            scale = transitionValue > 0.0F ? 1.0F - 0.5F * (1.0F - transitionValue) : 1.0F + 0.5F * (1.0F + transitionValue);
            ms.translate((float) this.depthPointX, (float) this.depthPointY, 0.0F);
            ms.scale(scale, scale, 1.0F);
            ms.translate((float) (-this.depthPointX), (float) (-this.depthPointY), 0.0F);
        } else {
            this.m_280273_(graphics);
        }
    }

    @Override
    public boolean keyPressed(int code, int p_keyPressed_2_, int p_keyPressed_3_) {
        if (code == 259) {
            ScreenOpener.openPreviousScreen(this, Optional.empty());
            return true;
        } else {
            return super.keyPressed(code, p_keyPressed_2_, p_keyPressed_3_);
        }
    }

    public void centerScalingOn(int x, int y) {
        this.depthPointX = x;
        this.depthPointY = y;
    }

    public void centerScalingOnMouse() {
        Window w = this.f_96541_.getWindow();
        double mouseX = this.f_96541_.mouseHandler.xpos() * (double) w.getGuiScaledWidth() / (double) w.getScreenWidth();
        double mouseY = this.f_96541_.mouseHandler.ypos() * (double) w.getGuiScaledHeight() / (double) w.getScreenHeight();
        this.centerScalingOn((int) mouseX, (int) mouseY);
    }

    public boolean isEquivalentTo(NavigatableSimiScreen other) {
        return false;
    }

    public void shareContextWith(NavigatableSimiScreen other) {
    }

    protected void renderZeloBreadcrumbs(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        List<Screen> history = ScreenOpener.getScreenHistory();
        if (!history.isEmpty()) {
            history.add(0, this.f_96541_.screen);
            int spacing = 20;
            List<String> names = (List<String>) history.stream().map(NavigatableSimiScreen::screenTitle).collect(Collectors.toList());
            int bWidth = names.stream().mapToInt(s -> this.f_96547_.width(s) + spacing).sum();
            MutableInt x = new MutableInt(this.f_96543_ - bWidth);
            MutableInt y = new MutableInt(this.f_96544_ - 18);
            MutableBoolean first = new MutableBoolean(true);
            if (x.getValue() < 25) {
                x.setValue(25);
            }
            PoseStack ms = graphics.pose();
            ms.pushPose();
            ms.translate(0.0F, 0.0F, 600.0F);
            names.forEach(s -> {
                int sWidth = this.f_96547_.width(s);
                UIRenderHelper.breadcrumbArrow(graphics, x.getValue(), y.getValue(), 0, sWidth + spacing, 14, spacing / 2, new Color(-586149872), new Color(1141903376));
                graphics.drawString(this.f_96547_, s, x.getValue() + 5, y.getValue() + 3, first.getValue() ? -1114130 : -2232577, false);
                first.setFalse();
                x.add(sWidth + spacing);
            });
            ms.popPose();
        }
    }

    private static String screenTitle(Screen screen) {
        return screen instanceof NavigatableSimiScreen ? ((NavigatableSimiScreen) screen).getBreadcrumbTitle() : "<";
    }

    protected String getBreadcrumbTitle() {
        return this.getClass().getSimpleName();
    }
}