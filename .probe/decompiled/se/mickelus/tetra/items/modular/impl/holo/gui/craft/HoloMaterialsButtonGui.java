package se.mickelus.tetra.items.modular.impl.holo.gui.craft;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.resources.language.I18n;
import se.mickelus.mutil.gui.GuiAttachment;
import se.mickelus.mutil.gui.GuiClickable;
import se.mickelus.mutil.gui.GuiString;
import se.mickelus.mutil.gui.GuiStringOutline;
import se.mickelus.mutil.gui.GuiTexture;
import se.mickelus.mutil.gui.animation.Applier;
import se.mickelus.mutil.gui.animation.GuiAnimation;
import se.mickelus.mutil.gui.animation.KeyframeAnimation;
import se.mickelus.tetra.gui.GuiTextures;

@ParametersAreNonnullByDefault
public class HoloMaterialsButtonGui extends GuiClickable {

    private final GuiTexture backdrop;

    private final GuiTexture icon;

    private final GuiString label;

    private final List<GuiAnimation> hoverAnimations = new ArrayList();

    private final List<GuiAnimation> blurAnimations = new ArrayList();

    private final KeyframeAnimation showAnimation;

    private final KeyframeAnimation hideAnimation;

    public HoloMaterialsButtonGui(int x, int y, Runnable onClickHandler) {
        super(x, y, 64, 64, onClickHandler);
        this.backdrop = new GuiTexture(0, 0, 48, 48, GuiTextures.workbench);
        this.backdrop.setAttachment(GuiAttachment.middleCenter);
        this.addChild(this.backdrop);
        this.icon = new GuiTexture(0, 0, 38, 38, 0, 180, GuiTextures.holo);
        this.icon.setAttachment(GuiAttachment.middleCenter);
        this.addChild(this.icon);
        this.label = new GuiStringOutline(0, -1, I18n.get("tetra.holo.craft.materials"));
        this.label.setAttachment(GuiAttachment.middleCenter);
        this.label.setOpacity(0.0F);
        this.addChild(this.label);
        this.showAnimation = new KeyframeAnimation(80, this).applyTo(new Applier.Opacity(1.0F));
        this.hideAnimation = new KeyframeAnimation(80, this).applyTo(new Applier.Opacity(0.0F)).onStop(complete -> this.isVisible = false);
        this.hoverAnimations.add(new KeyframeAnimation(80, this.label).applyTo(new Applier.Opacity(1.0F), new Applier.TranslateY(-2.0F, 0.0F)));
        this.blurAnimations.add(new KeyframeAnimation(120, this.label).applyTo(new Applier.Opacity(0.0F), new Applier.TranslateY(0.0F, 2.0F)));
    }

    @Override
    protected void onFocus() {
        this.backdrop.setColor(16777164);
        this.label.setColor(16777164);
        this.icon.setColor(8355711);
        this.blurAnimations.forEach(GuiAnimation::stop);
        this.hoverAnimations.forEach(GuiAnimation::start);
    }

    @Override
    protected void onBlur() {
        this.backdrop.setColor(16777215);
        this.label.setColor(16777215);
        this.icon.setColor(16777215);
        this.hoverAnimations.forEach(GuiAnimation::stop);
        this.blurAnimations.forEach(GuiAnimation::start);
    }

    @Override
    protected void onShow() {
        super.onShow();
        this.hideAnimation.stop();
        this.showAnimation.start();
    }

    @Override
    protected boolean onHide() {
        super.onHide();
        this.showAnimation.stop();
        this.hideAnimation.start();
        return false;
    }

    @Override
    public void updateFocusState(int refX, int refY, int mouseX, int mouseY) {
        mouseX -= refX + this.x;
        mouseY -= refY + this.y;
        boolean gainFocus = mouseX + mouseY >= 44;
        if (mouseX + mouseY > 84) {
            gainFocus = false;
        }
        if (mouseX - mouseY > 16) {
            gainFocus = false;
        }
        if (mouseY - mouseX > 19) {
            gainFocus = false;
        }
        if (gainFocus != this.hasFocus) {
            this.hasFocus = gainFocus;
            if (this.hasFocus) {
                this.onFocus();
            } else {
                this.onBlur();
            }
        }
    }
}