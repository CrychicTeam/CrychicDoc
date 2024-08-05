package dev.ftb.mods.ftbquests.client.gui;

import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.ui.misc.SimpleToast;
import net.minecraft.network.chat.Component;

public class RewardToast extends SimpleToast {

    private final Component text;

    private final Icon icon;

    public RewardToast(Component t, Icon i) {
        this.text = t;
        this.icon = i;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("ftbquests.reward.collected");
    }

    @Override
    public Component getSubtitle() {
        return this.text;
    }

    @Override
    public Icon getIcon() {
        return this.icon;
    }
}