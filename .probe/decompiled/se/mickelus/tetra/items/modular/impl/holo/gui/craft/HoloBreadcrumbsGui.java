package se.mickelus.tetra.items.modular.impl.holo.gui.craft;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import se.mickelus.mutil.gui.GuiAttachment;
import se.mickelus.mutil.gui.GuiButton;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiString;
import se.mickelus.mutil.gui.animation.Applier;
import se.mickelus.mutil.gui.animation.KeyframeAnimation;
import se.mickelus.tetra.gui.GuiKeybinding;

@ParametersAreNonnullByDefault
public class HoloBreadcrumbsGui extends GuiElement {

    private final Consumer<Integer> onClick;

    private final KeyframeAnimation openAnimation;

    private final KeyframeAnimation showAnimation;

    private final KeyframeAnimation hideAnimation;

    private final KeyframeAnimation focusAnimation;

    private final KeyframeAnimation blurAnimation;

    private final List<GuiElement> separators;

    private final List<GuiButton> buttons;

    public HoloBreadcrumbsGui(int x, int y, int width, Consumer<Integer> onClick) {
        super(x, y, width, 16);
        this.onClick = onClick;
        this.openAnimation = new KeyframeAnimation(80, this).applyTo(new Applier.Opacity(0.0F, 1.0F), new Applier.TranslateX((float) (x - 5), (float) x)).withDelay(80);
        this.showAnimation = new KeyframeAnimation(80, this).applyTo(new Applier.Opacity(1.0F), new Applier.TranslateX((float) x));
        this.hideAnimation = new KeyframeAnimation(80, this).applyTo(new Applier.Opacity(0.0F), new Applier.TranslateX((float) (x - 5))).onStop(complete -> this.isVisible = false);
        this.separators = new ArrayList();
        this.buttons = new ArrayList();
        GuiKeybinding keybinding = new GuiKeybinding(0, 3, "q");
        keybinding.setAttachmentPoint(GuiAttachment.topRight);
        keybinding.setOpacity(0.0F);
        this.addChild(keybinding);
        this.focusAnimation = new KeyframeAnimation(80, keybinding).applyTo(new Applier.Opacity(1.0F), new Applier.TranslateX((float) (keybinding.getX() - 5)));
        this.blurAnimation = new KeyframeAnimation(80, keybinding).applyTo(new Applier.Opacity(0.0F), new Applier.TranslateX((float) keybinding.getX()));
    }

    public void setItems(String[] items) {
        int xOffset = 0;
        int overlapCount = Math.min(this.buttons.size(), items.length);
        for (int i = 0; i < overlapCount; i++) {
            String label = !"".equals(items[i]) ? items[i] : I18n.get("tetra.holo.craft.slot");
            GuiButton button = (GuiButton) this.buttons.get(i);
            button.setText(label);
            button.setX(xOffset);
            if (i != 0) {
                ((GuiElement) this.separators.get(i - 1)).setX(xOffset - 8);
            }
            xOffset = button.getX() + button.getWidth() + 12;
        }
        for (int i = this.buttons.size(); i > items.length; i--) {
            this.removeButton(i - 1);
        }
        for (int i = this.buttons.size(); i < items.length; i++) {
            String label = !"".equals(items[i]) ? items[i] : I18n.get("tetra.holo.craft.slot");
            this.addButton(i, label);
        }
        if (!this.buttons.isEmpty()) {
            GuiButton button = (GuiButton) this.buttons.get(this.buttons.size() - 1);
            this.setWidth(button.getX() + button.getWidth());
        }
    }

    private void addButton(int index, String label) {
        int xOffset = 0;
        if (!this.buttons.isEmpty()) {
            GuiElement last = (GuiElement) this.buttons.get(this.buttons.size() - 1);
            xOffset = last.getX() + last.getWidth() + 12;
        }
        GuiButton button = new GuiButton(xOffset, 4, ChatFormatting.stripFormatting(label), () -> this.onClick.accept(index));
        new KeyframeAnimation(80, button).applyTo(new Applier.Opacity(0.0F, 1.0F), new Applier.TranslateX((float) (xOffset - 2), (float) xOffset)).withDelay(40).start();
        this.buttons.add(button);
        this.addChild(button);
        if (index != 0) {
            GuiElement separator = new GuiString(xOffset - 8, 5, ">", 8355711);
            new KeyframeAnimation(80, separator).applyTo(new Applier.Opacity(0.0F, 1.0F), new Applier.TranslateX((float) (xOffset - 10), (float) (xOffset - 8))).start();
            this.separators.add(separator);
            this.addChild(separator);
        }
    }

    private void removeButton(int index) {
        GuiElement button = (GuiElement) this.buttons.remove(index);
        new KeyframeAnimation(80, button).applyTo(new Applier.Opacity(0.0F), new Applier.TranslateX((float) (button.getX() - 2))).onStop(finished -> button.remove()).start();
        if (index > 0) {
            GuiElement separator = (GuiElement) this.separators.remove(index - 1);
            new KeyframeAnimation(80, separator).applyTo(new Applier.Opacity(0.0F), new Applier.TranslateX((float) (separator.getX() - 2))).withDelay(40).onStop(finished -> separator.remove()).start();
        }
    }

    public void animateOpen(boolean fast) {
        this.openAnimation.withDelay(fast ? 80 : 600).start();
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
    protected void onFocus() {
        if (this.buttons.size() > 0) {
            this.blurAnimation.stop();
            this.focusAnimation.start();
        }
    }

    @Override
    protected void onBlur() {
        this.focusAnimation.stop();
        this.blurAnimation.start();
    }
}