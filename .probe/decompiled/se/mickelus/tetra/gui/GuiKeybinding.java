package se.mickelus.tetra.gui;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraftforge.client.settings.KeyModifier;
import se.mickelus.mutil.gui.GuiAttachment;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiRect;
import se.mickelus.mutil.gui.GuiString;

@ParametersAreNonnullByDefault
public class GuiKeybinding extends GuiElement {

    public GuiKeybinding(int x, int y, KeyMapping keyBinding) {
        this(x, y, keyBinding.getKey().getDisplayName().getString(), keyBinding.getKeyModifier() != KeyModifier.NONE ? keyBinding.getKeyModifier().toString() : null, I18n.get(keyBinding.getName()));
    }

    public GuiKeybinding(int x, int y, KeyMapping keyBinding, GuiAttachment attachment) {
        this(x, y, keyBinding.getKey().getDisplayName().getString(), keyBinding.getKeyModifier() != KeyModifier.NONE ? keyBinding.getKeyModifier().toString() : null, I18n.get(keyBinding.getName()), attachment);
    }

    public GuiKeybinding(int x, int y, String key, @Nullable String modifier) {
        this(x, y, key, modifier, null);
    }

    public GuiKeybinding(int x, int y, String key) {
        this(x, y, key, null, null);
    }

    public GuiKeybinding(int x, int y, String key, @Nullable String modifier, @Nullable String description) {
        super(x, y, 0, 11);
        if (modifier != null) {
            GuiKeybinding.GuiKey modifierKey = new GuiKeybinding.GuiKey(0, 0, formatModifier(modifier));
            this.addChild(modifierKey);
            GuiString joiner = new GuiString(modifierKey.getWidth() + 2, 2, "+", 8355711);
            this.addChild(joiner);
            this.width = modifierKey.getWidth() + 2 + joiner.getWidth() + 2;
        }
        GuiKeybinding.GuiKey guiKey = new GuiKeybinding.GuiKey(this.width, 0, key.toUpperCase());
        this.addChild(guiKey);
        this.width = this.width + guiKey.getWidth();
        if (description != null) {
            this.width += 4;
            GuiString descriptionElement = new GuiString(this.width, 2, description);
            this.addChild(descriptionElement);
            this.width = this.width + descriptionElement.getWidth();
        }
    }

    public GuiKeybinding(int x, int y, String key, @Nullable String modifier, @Nullable String description, GuiAttachment attachment) {
        this(x, y, key, modifier, description);
        this.setAttachment(attachment);
    }

    private static String formatModifier(String modifier) {
        return "CONTROL".equals(modifier) ? "ctrl" : modifier.toLowerCase();
    }

    private class GuiKey extends GuiElement {

        public GuiKey(int x, int y, String key) {
            this(x, y, key, GuiAttachment.topLeft);
        }

        public GuiKey(int x, int y, String key, GuiAttachment attachment) {
            super(x, y, 0, 11);
            this.setAttachment(attachment);
            this.width = Minecraft.getInstance().font.width(key) + 5;
            this.addChild(new GuiRect(-1, 0, 1, this.height, 8355711));
            this.addChild(new GuiRect(this.width, 0, 1, this.height, 8355711));
            this.addChild(new GuiRect(0, -1, this.width, 1, 8355711));
            this.addChild(new GuiRect(0, this.height, this.width, 1, 8355711));
            this.addChild(new GuiString(3, 2, key));
        }
    }
}