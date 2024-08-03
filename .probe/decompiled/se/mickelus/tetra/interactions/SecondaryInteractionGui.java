package se.mickelus.tetra.interactions;

import java.util.Locale;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyModifier;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.animation.Applier;
import se.mickelus.mutil.gui.animation.KeyframeAnimation;
import se.mickelus.tetra.client.keymap.TetraKeyMappings;
import se.mickelus.tetra.gui.GuiKeybinding;

public class SecondaryInteractionGui extends GuiElement {

    KeyframeAnimation showAnimation;

    KeyframeAnimation hideanimation;

    public SecondaryInteractionGui(int x, int y, SecondaryInteraction interaction) {
        super(x, y, 100, 11);
        KeyMapping keybind = TetraKeyMappings.secondaryUseBinding;
        this.addChild(new GuiKeybinding(0, 0, keybind.getKey().getDisplayName().getString().toUpperCase(Locale.ROOT), keybind.getKeyModifier() != KeyModifier.NONE ? keybind.getKeyModifier().toString() : null, interaction.getLabel()));
        this.opacity = 0.0F;
        this.showAnimation = new KeyframeAnimation(240, this).applyTo(new Applier.Opacity(0.9F)).withDelay(100);
        this.hideanimation = new KeyframeAnimation(100, this).applyTo(new Applier.Opacity(0.0F)).onStop(complete -> this.remove());
    }

    public void show() {
        this.showAnimation.start();
    }

    public void hide() {
        this.showAnimation.stop();
        this.hideanimation.start();
    }
}