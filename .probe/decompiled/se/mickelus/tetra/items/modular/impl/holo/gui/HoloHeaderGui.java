package se.mickelus.tetra.items.modular.impl.holo.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import javax.annotation.ParametersAreNonnullByDefault;
import se.mickelus.mutil.gui.GuiAttachment;
import se.mickelus.mutil.gui.GuiButton;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiRect;
import se.mickelus.mutil.gui.animation.Applier;
import se.mickelus.mutil.gui.animation.KeyframeAnimation;
import se.mickelus.tetra.items.modular.impl.holo.HoloPage;

@ParametersAreNonnullByDefault
public class HoloHeaderGui extends GuiElement {

    private final GuiButton[] buttons;

    private final List<KeyframeAnimation> showAnimations = new ArrayList();

    public HoloHeaderGui(int x, int y, int width, Consumer<HoloPage> onPageChange) {
        super(x, y, width, 12);
        this.buttons = (GuiButton[]) Arrays.stream(HoloPage.values()).map(page -> new GuiButton(0, 4, page.label, () -> onPageChange.accept(page))).toArray(GuiButton[]::new);
        int spacing = width / (this.buttons.length + 1);
        for (int i = 0; i < this.buttons.length; i++) {
            this.buttons[i].setAttachmentPoint(GuiAttachment.topCenter);
            this.buttons[i].setX((i + 1) * spacing);
            this.addChild(this.buttons[i]);
        }
        this.changePage(HoloPage.craft);
        for (int i = 0; i < this.buttons.length; i++) {
            KeyframeAnimation animation = this.getButtonAnimation(this.buttons[i], i, this.buttons.length);
            this.showAnimations.add(animation);
        }
        this.setupSeparators();
    }

    private KeyframeAnimation getButtonAnimation(GuiElement button, int i, int size) {
        int delay = 1 + Math.abs(i - size / 2);
        if (i == (size + 1) / 2 - 1 && size % 2 != 0) {
            return new KeyframeAnimation(200, button).withDelay(delay * 300).applyTo(new Applier.TranslateY(3.0F, 0.0F, true), new Applier.Opacity(0.0F, 0.0F, false, true));
        } else {
            return i < size / 2 ? new KeyframeAnimation(200, button).withDelay(delay * 300).applyTo(new Applier.TranslateX(5.0F, 0.0F, true), new Applier.Opacity(0.0F, 0.0F, false, true)) : new KeyframeAnimation(200, button).withDelay(delay * 300).applyTo(new Applier.TranslateX(-5.0F, 0.0F, true), new Applier.Opacity(0.0F, 0.0F, false, true));
        }
    }

    private void setupSeparators() {
        GuiElement separator = new GuiRect(0, 0, this.width, 1, 16777215);
        separator.setAttachment(GuiAttachment.topCenter);
        this.showAnimations.add(new KeyframeAnimation(800, separator).applyTo(new Applier.Width((float) this.width / 2.0F, (float) this.width), new Applier.Opacity(0.0F, 0.3F)));
        this.addChild(separator);
        separator = new GuiRect(0, 0, this.width, 1, 16777215);
        separator.setAttachment(GuiAttachment.topCenter);
        this.showAnimations.add(new KeyframeAnimation(200, separator).applyTo(new Applier.Width(0.0F, (float) this.width), new Applier.Opacity(0.0F, 0.3F)));
        this.addChild(separator);
        GuiElement var3 = new GuiRect(0, 16, this.width, 1, 16777215);
        var3.setAttachment(GuiAttachment.topCenter);
        this.showAnimations.add(new KeyframeAnimation(800, var3).applyTo(new Applier.Width((float) this.width / 1.25F, (float) this.width), new Applier.Opacity(0.0F, 0.3F)));
        this.addChild(var3);
        GuiElement var4 = new GuiRect(0, 16, this.width, 1, 16777215);
        var4.setAttachment(GuiAttachment.topCenter);
        this.showAnimations.add(new KeyframeAnimation(200, var4).applyTo(new Applier.Width((float) this.width / 2.0F, (float) this.width), new Applier.Opacity(0.0F, 0.3F)));
        this.addChild(var4);
    }

    @Override
    protected void onShow() {
        this.showAnimations.forEach(KeyframeAnimation::start);
    }

    public void changePage(HoloPage page) {
        for (GuiButton button : this.buttons) {
            button.setOpacity(0.5F);
        }
        this.buttons[page.ordinal()].setOpacity(1.0F);
    }
}