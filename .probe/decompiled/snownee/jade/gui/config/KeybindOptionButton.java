package snownee.jade.gui.config;

import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class KeybindOptionButton extends OptionButton {

    private final KeyMapping keybind;

    public KeybindOptionButton(OptionsList owner, KeyMapping keybind) {
        super(Component.translatable(keybind.getName()), null);
        this.keybind = keybind;
        Button button = Button.builder(keybind.getTranslatedKeyMessage(), b -> {
            owner.selectedKey = this.keybind;
            owner.resetMappingAndUpdateButtons();
        }).size(100, 20).createNarration(supplier -> this.keybind.isUnbound() ? Component.translatable("narrator.controls.unbound", this.title) : Component.translatable("narrator.controls.bound", this.title, supplier.get())).build();
        this.addWidget(button, 0);
    }

    public void refresh(KeyMapping selectedKey) {
        AbstractWidget button = this.getFirstWidget();
        if (selectedKey == this.keybind) {
            button.setMessage(Component.literal("> ").append(button.getMessage().copy().withStyle(ChatFormatting.WHITE, ChatFormatting.UNDERLINE)).append(" <").withStyle(ChatFormatting.YELLOW));
        } else {
            button.setMessage(this.keybind.getTranslatedKeyMessage());
        }
    }
}