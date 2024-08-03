package se.mickelus.tetra.effect.howling;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import se.mickelus.mutil.gui.GuiAttachment;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiRoot;
import se.mickelus.mutil.gui.animation.Applier;
import se.mickelus.mutil.gui.animation.KeyframeAnimation;

@ParametersAreNonnullByDefault
public class HowlingProgressGui extends GuiRoot {

    private static final int width = 16;

    private final KeyframeAnimation showAnimation;

    private final KeyframeAnimation hideAnimation;

    private final GuiElement container = new GuiElement(-1, 40, 15, 15).setAttachment(GuiAttachment.middleCenter).setOpacity(0.0F);

    private final HowlingIndicatorGui[] indicators;

    private GuiElement backdrop;

    public HowlingProgressGui(Minecraft mc) {
        super(mc);
        this.container.setVisible(false);
        this.addChild(this.container);
        GuiElement indicatorGroup = new GuiElement(0, 0, 15, 15);
        this.container.addChild(indicatorGroup);
        this.indicators = new HowlingIndicatorGui[12];
        this.indicators[0] = new HowlingIndicatorGui(11, 2, 3, 4, 21, 0, 2, false);
        this.indicators[1] = new HowlingIndicatorGui(7, 0, 3, 4, 21, 0, 2, false);
        this.indicators[2] = new HowlingIndicatorGui(2, 1, 4, 3, 21, 7, 2, true);
        this.indicators[3] = new HowlingIndicatorGui(0, 5, 4, 3, 21, 7, 2, true);
        this.indicators[4] = new HowlingIndicatorGui(1, 9, 3, 4, 24, 0, -2, false);
        this.indicators[5] = new HowlingIndicatorGui(5, 11, 3, 4, 24, 0, -2, false);
        this.indicators[6] = new HowlingIndicatorGui(9, 11, 4, 3, 21, 4, -2, true);
        this.indicators[7] = new HowlingIndicatorGui(11, 7, 4, 3, 21, 4, -2, true);
        this.indicators[8] = new HowlingIndicatorGui(9, 5, 3, 3, 22, 4, -2, true);
        this.indicators[9] = new HowlingIndicatorGui(5, 3, 3, 3, 21, 0, 2, false);
        this.indicators[10] = new HowlingIndicatorGui(3, 7, 3, 3, 21, 7, 2, true);
        this.indicators[11] = new HowlingIndicatorGui(7, 9, 3, 3, 24, 1, -2, false);
        for (HowlingIndicatorGui indicator : this.indicators) {
            indicatorGroup.addChild(indicator);
        }
        this.showAnimation = new KeyframeAnimation(60, this.container).applyTo(new Applier.Opacity(1.0F), new Applier.TranslateY(40.0F));
        this.hideAnimation = new KeyframeAnimation(100, this.container).applyTo(new Applier.Opacity(0.0F), new Applier.TranslateY(42.0F)).withDelay(500).onStop(finished -> {
            if (finished) {
                this.container.setVisible(false);
            }
        });
    }

    public void updateAmplifier(int progress) {
        if (progress > -1) {
            if (!this.showAnimation.isActive() && this.container.getOpacity() < 1.0F) {
                this.container.setVisible(true);
                this.showAnimation.start();
            }
            if (this.hideAnimation.isActive()) {
                this.hideAnimation.stop();
            }
            for (int i = 0; i < this.indicators.length; i++) {
                if (i <= progress) {
                    this.indicators[i].show();
                } else {
                    this.indicators[i].reset();
                }
            }
        } else {
            if (!this.hideAnimation.isActive() && this.container.getOpacity() > 0.0F) {
                this.hideAnimation.start();
            }
            this.showAnimation.stop();
        }
    }

    @Override
    public void draw(GuiGraphics graphics) {
        if (this.container.isVisible()) {
            Window window = this.mc.getWindow();
            int width = window.getGuiScaledWidth();
            int height = window.getGuiScaledHeight();
            int mouseX = (int) (this.mc.mouseHandler.xpos() * (double) window.getGuiScaledWidth() / (double) window.getScreenWidth());
            int mouseY = (int) (this.mc.mouseHandler.ypos() * (double) window.getGuiScaledHeight() / (double) window.getScreenHeight());
            this.drawChildren(graphics, width / 2, height / 2, 0, 0, mouseX, mouseY, 1.0F);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}