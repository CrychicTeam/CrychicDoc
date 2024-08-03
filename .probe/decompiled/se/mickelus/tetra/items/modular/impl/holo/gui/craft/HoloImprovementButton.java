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
import se.mickelus.mutil.gui.animation.KeyframeAnimation;
import se.mickelus.tetra.gui.GuiTextures;

@ParametersAreNonnullByDefault
public class HoloImprovementButton extends GuiClickable {

    private final KeyframeAnimation showAnimation;

    private final KeyframeAnimation hideAnimation;

    GuiString label;

    List<KeyframeAnimation> hoverAnimations;

    List<KeyframeAnimation> blurAnimations;

    boolean hasImprovements = false;

    public HoloImprovementButton(int x, int y, Runnable onClick) {
        super(x, y, 0, 19, onClick);
        this.label = new GuiStringOutline(0, 0, I18n.get("tetra.holo.craft.improvement_button", "00"));
        this.label.setAttachment(GuiAttachment.middleCenter);
        this.width = this.label.getWidth() + 16;
        this.hoverAnimations = new ArrayList();
        this.blurAnimations = new ArrayList();
        for (int i = 0; i < 3; i++) {
            int texX = i * -5 + 8;
            GuiTexture texture = new GuiTexture(texX, 0, 16, 16, 0, 16, GuiTextures.holo);
            texture.setAttachment(GuiAttachment.middleLeft);
            this.addChild(texture);
            this.hoverAnimations.add(new KeyframeAnimation(100, texture).applyTo(new Applier.TranslateX((float) (texX - i * 3 - 2)), new Applier.TranslateY(0.0F)).withDelay((3 - i) * 40));
            this.blurAnimations.add(new KeyframeAnimation(100, texture).applyTo(new Applier.TranslateX((float) texX), new Applier.TranslateY(0.0F)).withDelay(i * 40));
        }
        for (int i = 0; i < 3; i++) {
            int texX = i * 5 - 8;
            GuiTexture texture = new GuiTexture(texX, 0, 16, 16, 16, 16, GuiTextures.holo);
            texture.setAttachment(GuiAttachment.middleRight);
            this.addChild(texture);
            this.hoverAnimations.add(new KeyframeAnimation(100, texture).applyTo(new Applier.TranslateX((float) (texX + i * 3 + 2)), new Applier.TranslateY(0.0F)).withDelay((3 - i) * 40));
            this.blurAnimations.add(new KeyframeAnimation(100, texture).applyTo(new Applier.TranslateX((float) texX), new Applier.TranslateY(0.0F)).withDelay(i * 40));
        }
        this.addChild(new GuiTexture(-26, 1, 11, 11, 17, 0, GuiTextures.holo).setAttachment(GuiAttachment.middleLeft));
        this.addChild(new GuiTexture(26, 1, 11, 11, 17, 0, GuiTextures.holo).setAttachment(GuiAttachment.middleRight));
        this.addChild(this.label);
        this.showAnimation = new KeyframeAnimation(60, this).applyTo(new Applier.Opacity(1.0F)).withDelay(100);
        this.hideAnimation = new KeyframeAnimation(60, this).applyTo(new Applier.Opacity(0.0F)).onStop(complete -> {
            if (complete) {
                this.isVisible = false;
            }
        });
    }

    public void updateCount(int count) {
        this.label.setString(I18n.get("tetra.holo.craft.improvement_button", count));
        this.hasImprovements = count > 0;
        this.label.setColor(this.hasImprovements ? 16777215 : 8355711);
        this.blurAnimations.forEach(KeyframeAnimation::start);
    }

    public void show() {
        this.hideAnimation.stop();
        this.setVisible(true);
        this.setOpacity(0.0F);
        this.showAnimation.start();
    }

    public void hide() {
        this.showAnimation.stop();
        this.hideAnimation.start();
    }

    @Override
    public boolean onMouseClick(int x, int y, int button) {
        return this.hasImprovements ? super.onMouseClick(x, y, button) : false;
    }

    @Override
    protected void onFocus() {
        super.onFocus();
        if (this.hasImprovements) {
            this.label.setColor(16777164);
            this.blurAnimations.forEach(KeyframeAnimation::stop);
            this.hoverAnimations.forEach(KeyframeAnimation::start);
        }
    }

    @Override
    protected void onBlur() {
        super.onBlur();
        if (this.hasImprovements) {
            this.label.setColor(16777215);
            this.hoverAnimations.forEach(KeyframeAnimation::stop);
            this.blurAnimations.forEach(KeyframeAnimation::start);
        }
    }
}